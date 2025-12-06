package com.practicum.playlist_maker.data.dto

data class TrackDto(
    val id: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val image: String,
    var favorite: Boolean,
    var playlistId: MutableList<Long>
)