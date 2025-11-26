package com.practicum.playlist_maker.creator

import com.practicum.playlist_maker.data.dto.TrackDto

class Storage {
    private val listTracks = listOf(
        TrackDto(
            trackName = "25",
            artistName = "Markul",
            trackTimeMillis = 173000 // 2:53
        ),
        TrackDto(
            trackName = "Baptized In Fear",
            artistName = "The Weeknd",
            trackTimeMillis = 232000 // 3:52
        ),
        TrackDto(
            trackName = "BOYS DON'T CRY",
            artistName = "GONE.Fludd",
            trackTimeMillis = 147000 // 2:27
        ),
        TrackDto(
            trackName = "позову звезды смотреть на тебя",
            artistName = "мартин",
            trackTimeMillis = 201000
        ),
        TrackDto(
            trackName = "St. Patrick's",
            artistName = "Central Cee",
            trackTimeMillis = 160000
        ),
        TrackDto(
            trackName = "Zima Blue",
            artistName = "Markul",
            trackTimeMillis = 182000
        ),
        TrackDto(
            trackName = "Obsessed With You",
            artistName = "Central Cee",
            trackTimeMillis = 108000
        ),
        TrackDto(
            trackName = "Ordinary",
            artistName = "Fabiene Se",
            trackTimeMillis = 140000
        ),
        TrackDto(
            trackName = "Lucid Dreams",
            artistName = "Juice WRLD",
            trackTimeMillis = 239000
        ),
        TrackDto(
            trackName = "Ordinary Life",
            artistName = "The Weeknd",
            trackTimeMillis = 221000
        )
    )

    fun search(request: String): List<TrackDto> {
        if (request.isBlank()) return emptyList()
        val result = listTracks.filter {
            it.trackName.lowercase().contains(request.lowercase()) ||
                    it.artistName.lowercase().contains(request.lowercase())
        }
        return result
    }
}