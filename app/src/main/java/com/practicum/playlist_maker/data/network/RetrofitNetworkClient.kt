package com.practicum.playlist_maker.data.network

import com.practicum.playlist_maker.data.ITunesApiService
import com.practicum.playlist_maker.data.dto.BaseResponse
import com.practicum.playlist_maker.domain.NetworkClient
import com.practicum.playlist_maker.data.dto.TracksSearchRequest
import java.io.IOException


class RetrofitNetworkClient(private val api: ITunesApiService) : NetworkClient {

    override suspend fun doRequest(dto: Any): BaseResponse {
        return try {
            when (dto) {
                is TracksSearchRequest -> api.searchTracks(
                    query = dto.expression,
                    media = "music",
                    entity = "song",
                    limit = 30
                ).apply {
                    resultCode = 200
                }

                else -> BaseResponse().apply {
                    resultCode = 400
                    errorMessage = "Invalid request type: expected TracksSearchRequest or String"
                }
            }
        } catch (e: IOException) {
            BaseResponse().apply {
                resultCode = -1
                errorMessage = "Network error: ${e.message ?: "Unknown IO error"}"
            }
        } catch (e: Exception) {
            BaseResponse().apply {
                resultCode = -2
                errorMessage = "Unexpected error: ${e.message ?: "Unknown error"}"
            }
        }
    }
}