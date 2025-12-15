package com.practicum.playlist_maker.data.dto

class TracksSearchResponse(
    val resultCount: Int = 0,
    val results: List<TrackDto>
) : BaseResponse()