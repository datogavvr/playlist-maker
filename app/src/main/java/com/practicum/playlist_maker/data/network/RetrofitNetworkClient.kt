package com.practicum.playlist_maker.data.network

import com.practicum.playlist_maker.creator.Storage
import com.practicum.playlist_maker.domain.NetworkClient
import com.practicum.playlist_maker.data.dto.TracksSearchRequest
import com.practicum.playlist_maker.data.dto.TracksSearchResponse


class RetrofitNetworkClient(private val storage: Storage) : NetworkClient {

    override fun doRequest(request: Any): TracksSearchResponse {
        val searchList = storage.search((request as TracksSearchRequest).expression)
        return TracksSearchResponse(searchList).apply { resultCode = 200 }
    }
}