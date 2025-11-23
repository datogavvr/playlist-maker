package com.practicum.playlist_maker.ui

import com.practicum.playlist_maker.data.network.Track

sealed class SearchState {
    object Initial: SearchState() // первоначальное cостояние экрана
    object Searching: SearchState() // состояние экрана при начале поиска
    data class Success(val list: List<Track>): SearchState() // состояние экрана при успешном завершении поиска
    data class Fail(val error: String): SearchState() // состояние экрана, если при запросе к серверу произошла ошибка
}