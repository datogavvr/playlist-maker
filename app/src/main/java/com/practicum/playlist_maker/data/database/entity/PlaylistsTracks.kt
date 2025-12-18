package com.practicum.playlist_maker.data.database.entity

import androidx.room.Entity

@Entity(
    tableName = "playlists_tracks",
    primaryKeys = ["playlistId", "trackId"]
)
data class PlaylistsTracks(
    val playlistId: Long,
    val trackId: Long
)
