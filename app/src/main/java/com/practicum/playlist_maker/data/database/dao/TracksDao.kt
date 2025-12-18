package com.practicum.playlist_maker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlist_maker.data.database.entity.PlaylistsTracks
import com.practicum.playlist_maker.data.database.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TracksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Query("SELECT * FROM tracks WHERE trackName = :name AND artistName = :artist")
    fun getTrackByNameAndArtist(
        name: String,
        artist: String
    ): Flow<TrackEntity?>

    @Query("SELECT * FROM tracks WHERE id = :trackId")
    fun getTrackById(trackId: Long): Flow<TrackEntity?>

    @Query("SELECT * FROM tracks WHERE favorite = 1")
    fun getFavoriteTracks(): Flow<List<TrackEntity>>

    @Query("SELECT * FROM tracks WHERE id = :trackId")
    suspend fun getTrackSync(trackId: Long): TrackEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackToPlaylist(crossRef: PlaylistsTracks)

    @Query("""
        DELETE FROM playlists_tracks
        WHERE trackId = :trackId AND playlistId = :playlistId
    """)
    suspend fun removeTrackFromPlaylist(
        trackId: Long,
        playlistId: Long
    )

    @Query("""
        DELETE FROM playlists_tracks
        WHERE playlistId = :playlistId
    """)
    suspend fun deleteTracksByPlaylistId(playlistId: Long)

    @Query("""
        SELECT playlistId FROM playlists_tracks
        WHERE trackId = :trackId
    """)
    suspend fun getPlaylistIdsForTrack(trackId: Long): List<Long>
}
