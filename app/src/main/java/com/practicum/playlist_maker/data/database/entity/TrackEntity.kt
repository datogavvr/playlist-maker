package com.practicum.playlist_maker.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey
    val id: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long?,
    val trackTime: String,
    val image: String?,
    val genre: String?,          // жанр
    val releaseDate: String?,    // дата релиза
    val isExplicit: Boolean?,    // возрастное ограничение
)