package com.practicum.playlist_maker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlist_maker.data.database.dao.PlaylistsDao
import com.practicum.playlist_maker.data.database.dao.TracksDao
import com.practicum.playlist_maker.data.database.entity.PlaylistEntity
import com.practicum.playlist_maker.data.database.entity.PlaylistsTracks
import com.practicum.playlist_maker.data.database.entity.TrackEntity


@Database(
    entities = [
        TrackEntity::class,
        PlaylistEntity::class,
        PlaylistsTracks::class
    ], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun TracksDao(): TracksDao
    abstract fun PlaylistsDao(): PlaylistsDao
}