package com.practicum.playlist_maker.data.network

import com.practicum.playlist_maker.data.database.AppDatabase
import com.practicum.playlist_maker.data.database.toDomain
import com.practicum.playlist_maker.data.database.toEntity
import com.practicum.playlist_maker.data.database.entity.PlaylistEntity
import com.practicum.playlist_maker.data.database.entity.PlaylistsTracks
import com.practicum.playlist_maker.data.dto.TracksSearchRequest
import com.practicum.playlist_maker.data.dto.TracksSearchResponse
import com.practicum.playlist_maker.data.dto.toDomain
import com.practicum.playlist_maker.domain.NetworkClient
import com.practicum.playlist_maker.domain.PlaylistsRepository
import com.practicum.playlist_maker.domain.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    private val database: AppDatabase
) : PlaylistsRepository {

    private val playlistDao = database.PlaylistsDao()
    private val tracksDao = database.TracksDao()

    override fun getPlaylist(playlistId: Long): Flow<Playlist?> {
        return playlistDao
            .getPlaylistWithTracks(playlistId)
            .map { it?.toDomain() }
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistDao
            .getAllPlaylistsWithTracks()
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun addNewPlaylist(
        name: String,
        description: String,
        coverUri: String?
    ) {
        playlistDao.insertPlaylist(
            PlaylistEntity(
                name = name,
                description = description,
                coverUri = coverUri
            )
        )
    }

    override suspend fun deletePlaylistById(id: Long) {
        playlistDao.deletePlaylistById(id)
        tracksDao.deleteTracksByPlaylistId(id)
    }
}

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val database: AppDatabase
) : TracksRepository {

    private val tracksDao = database.TracksDao()

    override suspend fun searchTracks(expression: String): List<Track> {
        val request = TracksSearchRequest(expression)
        val response = networkClient.doRequest(request)

        if (response !is TracksSearchResponse || response.resultCode != 200) {
            return emptyList()
        }

        return response.results.map { dto ->
            val netTrack = dto.toDomain()

            val existingEntity = tracksDao.getTrackSync(netTrack.id)
            val favoriteStatus = existingEntity?.favorite ?: false

            tracksDao.insertTrack(netTrack.toEntity().copy(favorite = favoriteStatus))


            val playlistIds =
                tracksDao.getPlaylistIdsForTrack(netTrack.id)

            netTrack.copy(
                playlistId = playlistIds.toMutableList()
            )
        }
    }

    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> {
        return tracksDao
            .getTrackByNameAndArtist(track.trackName, track.artistName)
            .map { entity ->
                entity?.let {
                    val playlistIds =
                        tracksDao.getPlaylistIdsForTrack(it.id)
                    it.toDomain(playlistIds)
                }
            }
    }

    override fun getTrackById(trackId: Long): Flow<Track?> {
        return tracksDao
            .getTrackById(trackId)
            .map { entity ->
                entity?.let {
                    val playlistIds =
                        tracksDao.getPlaylistIdsForTrack(it.id)
                    it.toDomain(playlistIds)
                }
            }
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return tracksDao
            .getFavoriteTracks()
            .map { list ->
                list.map { entity ->
                    val playlistIds =
                        tracksDao.getPlaylistIdsForTrack(entity.id)
                    entity.toDomain(playlistIds)
                }
            }
    }

    override suspend fun addTrackToPlaylist(trackId: Long, playlistId: Long) {
        tracksDao.addTrackToPlaylist(
            PlaylistsTracks(
                trackId = trackId,
                playlistId = playlistId
            )
        )
    }

    override suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long) {
        tracksDao.removeTrackFromPlaylist(trackId, playlistId)
    }

    override suspend fun updateTrackFavoriteStatus(trackId: Long, isFavorite: Boolean) {
        val entity = tracksDao.getTrackSync(trackId) ?: return
        tracksDao.insertTrack(
            entity.copy(favorite = isFavorite)
        )
    }

    override suspend fun deleteTracksByPlaylistId(playlistId: Long) {
        tracksDao.deleteTracksByPlaylistId(playlistId)
    }
}
