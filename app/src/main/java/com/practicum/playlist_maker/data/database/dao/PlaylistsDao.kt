package com.practicum.playlist_maker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.practicum.playlist_maker.data.database.entity.PlaylistEntity
import com.practicum.playlist_maker.data.database.relation.PlaylistWithTracks
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistsDao {

    @Insert
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Transaction
    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    fun getPlaylistWithTracks(playlistId: Long): Flow<PlaylistWithTracks?>

    @Transaction
    @Query("SELECT * FROM playlists WHERE isSystem = 0")
    fun getAllPlaylistsWithTracks(): Flow<List<PlaylistWithTracks>>

    @Query("SELECT * FROM playlists WHERE isSystem = 1 LIMIT 1")
    suspend fun getFavoritePlaylistOnce(): PlaylistEntity?

    @Transaction
    @Query("SELECT * FROM playlists WHERE isSystem = 1 LIMIT 1")
    fun getFavoritePlaylist(): Flow<PlaylistWithTracks?>

    @Query("DELETE FROM playlists WHERE id = :id")
    suspend fun deletePlaylistById(id: Long)
}
