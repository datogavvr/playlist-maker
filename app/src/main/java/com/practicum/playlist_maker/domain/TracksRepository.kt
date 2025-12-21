package com.practicum.playlist_maker.domain

import com.practicum.playlist_maker.data.network.Playlist
import com.practicum.playlist_maker.data.network.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    // получение плейлиста по id
    fun getPlaylist(playlistId: Long): Flow<Playlist?>

    // получение всех плейлистов
    fun getAllPlaylists(): Flow<List<Playlist>>

    // получение избранного с его параметрами
    suspend fun getFavoritePlaylistOnce(): Playlist?

    // получение потока с избранным
    fun getFavoritePlaylist(): Flow<Playlist?>

    // добавление плейлиста
    suspend fun addNewPlaylist(name: String, description: String, coverUri: String?)

    // удаление плейлиста по id
    suspend fun deletePlaylistById(id: Long)

    // проверка наличия плейлиста для избранного
    suspend fun ensureFavoritePlaylistExists()
}


interface TracksRepository {

    suspend fun searchTracks(expression: String): List<Track>

    fun getTrackByNameAndArtist(track: Track): Flow<Track?>

    fun getTrackById(trackId: Long): Flow<Track?>

    fun getFavoriteTracks(): Flow<List<Track>>

    // добавить трек в плейлист
    suspend fun addTrackToPlaylist(trackId: Long, playlistId: Long)

    // удалить трек из конкретного плейлиста
    suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long)

    // обновление избранного
    suspend fun updateTrackFavoriteStatus(trackId: Long, isFavorite: Boolean)

    // при удалении плейлиста нужно убрать его id из всех треков
    suspend fun deleteTracksByPlaylistId(playlistId: Long)
}
