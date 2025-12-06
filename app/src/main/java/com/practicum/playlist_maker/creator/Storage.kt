package com.practicum.playlist_maker.creator

import com.practicum.playlist_maker.data.dto.TrackDto

class Storage {
    private val listTracks = listOf(
        TrackDto(
            id = 1,
            trackName = "25",
            artistName = "Markul",
            trackTime = "2:53",
            image = "",
            favorite = false,
            playlistId = mutableListOf(0)
        ),
        TrackDto(
            id = 2,
            trackName = "Baptized In Fear",
            artistName = "The Weeknd",
            trackTime = "3:52", // 3:52
            image = "",
            favorite = false,
            playlistId = mutableListOf(0)
        ),
        TrackDto(
            id = 3,
            trackName = "BOYS DON'T CRY",
            artistName = "GONE.Fludd",
            trackTime = "2:27",
            image = "",
            favorite = false,
            playlistId = mutableListOf(0)
        ),
        TrackDto(
            id = 4,
            trackName = "позову звезды смотреть на тебя",
            artistName = "мартин",
            trackTime = "3:21",
            image = "",
            favorite = false,
            playlistId = mutableListOf(0)
        ),
        TrackDto(
            id = 5,
            trackName = "St. Patrick's",
            artistName = "Central Cee",
            trackTime = "2:40",
            image = "",
            favorite = false,
            playlistId = mutableListOf(0)
        ),
        TrackDto(
            id = 6,
            trackName = "Zima Blue",
            artistName = "Markul",
            trackTime = "3:02",
            image = "",
            favorite = false,
            playlistId = mutableListOf(0)
        ),
        TrackDto(
            id = 7,
            trackName = "Obsessed With You",
            artistName = "Central Cee",
            trackTime = "1:48",
            image = "",
            favorite = false,
            playlistId = mutableListOf(0)
        ),
        TrackDto(
            id = 8,
            trackName = "Ordinary",
            artistName = "Fabiene Se",
            trackTime = "2:20",
            image = "",
            favorite = false,
            playlistId = mutableListOf(0)
        ),
        TrackDto(
            id = 9,
            trackName = "Lucid Dreams",
            artistName = "Juice WRLD",
            trackTime = "3:59",
            image = "",
            favorite = false,
            playlistId = mutableListOf(0)
        ),
        TrackDto(
            id = 9,
            trackName = "Ordinary Life",
            artistName = "The Weeknd",
            trackTime = "3:41",
            image = "",
            favorite = false,
            playlistId = mutableListOf(0)
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