package com.practicum.playlist_maker.data.network

data class Playlist(
    val id: Long = 0,
    var playlistName: String,
    var description: String,
    val coverUri: String?,
    var tracks: List<Track>
)