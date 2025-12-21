package com.practicum.playlist_maker.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey
    val id: Long,
    val trackName: String = "Unknown",
    val artistName: String = "Unknown",
    val trackTimeMillis: Long? = 0L,
    val trackTime: String = "0:00",
    val image: String? = null
)