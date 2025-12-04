package com.practicum.playlist_maker.data.network

import com.practicum.playlist_maker.data.DatabaseMock
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

    override suspend fun addNewPlaylist(name: String, description: String) {
        database.addNewPlaylist(name, description)
    }

    override suspend fun deletePlaylistById(id: Long) {
        // удалить плейлист
        database.deletePlaylistById(id)

        // удалить id плейлиста у всех треков
        database.deleteTracksByPlaylistId(id)
    }
}

class TracksRepositoryImpl(
    private val scope: CoroutineScope,
    private val database: DatabaseMock
) : TracksRepository {

    override suspend fun searchTracks(expression: String): List<Track> {
        return database.searchTracks(expression)
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

    // добавить трек в плейлист
    override suspend fun addTrackToPlaylist(trackId: Long, playlistId: Long) {
        database.addTrackToPlaylist(trackId, playlistId)
    }

    // удалить трек из конкретного плейлиста
    override suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long) {
        database.removeTrackFromPlaylist(trackId, playlistId)
    }

    // обновить избранное
    override suspend fun updateTrackFavoriteStatus(trackId: Long, isFavorite: Boolean) {
        database.updateTrackFavoriteStatus(trackId, isFavorite)
    }

    // удалить плейлист из всех треков
    override suspend fun deleteTracksByPlaylistId(playlistId: Long) {
        database.deleteTracksByPlaylistId(playlistId)
    }
}
