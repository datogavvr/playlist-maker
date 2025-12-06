package com.practicum.playlist_maker.data.network

data class Playlist(
    val id: Long = 0,
    val name: String,
    val description: String,
    var tracks: List<Track>
)