package com.practicum.playlist_maker.domain

import com.practicum.playlist_maker.data.network.Track

interface TracksRepository {
    suspend fun searchTracks(expression: String): List<Track>
}