package com.practicum.playlist_maker.domain

import com.practicum.playlist_maker.data.dto.BaseResponse

interface NetworkClient {
    suspend fun doRequest(dto: Any): BaseResponse
}