package com.practicum.playlist_maker.data.network

import com.practicum.playlist_maker.data.DatabaseMock
import com.practicum.playlist_maker.data.dto.TracksSearchRequest
import com.practicum.playlist_maker.data.dto.TracksSearchResponse
import com.practicum.playlist_maker.data.dto.toDomain
import com.practicum.playlist_maker.domain.NetworkClient
import com.practicum.playlist_maker.domain.PlaylistsRepository
import com.practicum.playlist_maker.domain.TracksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class PlaylistsRepositoryImpl(
    private val scope: CoroutineScope,
    private val database: DatabaseMock
) : PlaylistsRepository {

    override fun getPlaylist(playlistId: Long): Flow<Playlist?> {
        return database.getPlaylist(playlistId)
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return database.getAllPlaylists()
    }

    override suspend fun addNewPlaylist(
        name: String,
        description: String,
        coverUri: String?
    ) {
        database.addNewPlaylist(name, description, coverUri)
    }


    override suspend fun deletePlaylistById(id: Long) {
        database.deletePlaylistById(id)
        database.deleteTracksByPlaylistId(id)
    }
}

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val database: DatabaseMock
) : TracksRepository {

    override suspend fun searchTracks(expression: String): List<Track> {
        val request = TracksSearchRequest(expression)
        val response = networkClient.doRequest(request)

        if (response.resultCode != 200 || response !is TracksSearchResponse) {
            return emptyList()
        }

        val result = mutableListOf<Track>()

        for (dto in response.results) {
            val netTrack = dto.toDomain()

            val localTrack = database.getTrackSync(netTrack.id)

            if (localTrack != null) {
                result.add(localTrack)
            } else {
                database.insertTrack(netTrack)
                result.add(netTrack)
            }
        }

        return result
    }

    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> {
        return database.getTrackByNameAndArtist(track)
    }

    override fun getTrackById(trackId: Long): Flow<Track?> {
        return database.getTrackById(trackId)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return database.getFavoriteTracks()
    }

    override suspend fun addTrackToPlaylist(trackId: Long, playlistId: Long) {
        database.addTrackToPlaylist(trackId, playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long) {
        database.removeTrackFromPlaylist(trackId, playlistId)
    }

    override suspend fun updateTrackFavoriteStatus(trackId: Long, isFavorite: Boolean) {
        database.updateTrackFavoriteStatus(trackId, isFavorite)
    }

    override suspend fun deleteTracksByPlaylistId(playlistId: Long) {
        database.deleteTracksByPlaylistId(playlistId)
    }
}
