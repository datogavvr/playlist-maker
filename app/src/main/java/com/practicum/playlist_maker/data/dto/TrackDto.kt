package com.practicum.playlist_maker.data.dto

data class TrackDto(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long?,
    val previewUrl: String?,
    val artworkUrl100: String?,
)