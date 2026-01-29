package com.practicum.playlist_maker.data.dto

data class TrackDto(
    val trackId: Long,             // идентификатор трека
    val trackName: String,         // название трека
    val artistName: String,        // имя исполнителя
    val trackTimeMillis: Long?,    // время трека в миллисекундах
    val previewUrl: String?,
    val artworkUrl100: String?,    // обложка
    val primaryGenreName: String?, // жанр
    val releaseDate: String?,      // дата релиза
    val trackExplicitness: String?,// возрастное ограничение
)