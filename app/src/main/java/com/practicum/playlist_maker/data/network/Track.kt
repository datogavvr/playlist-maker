package com.practicum.playlist_maker.data.network

data class Track(
    val id: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long?,
    val trackTime: String,
    val image: String?,
    var favorite: Boolean,
    var playlistId: MutableList<Long>
)