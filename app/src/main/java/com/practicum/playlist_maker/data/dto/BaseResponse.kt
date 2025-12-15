package com.practicum.playlist_maker.data.dto

open class BaseResponse {
    var resultCode: Int = 0
    var errorMessage: String? = null
    var data: Any? = null
}