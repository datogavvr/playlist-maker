package com.practicum.playlist_maker.data.dto

import android.annotation.SuppressLint
import com.practicum.playlist_maker.data.network.Track
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("ConstantLocale")
private val durationFormatter = SimpleDateFormat("m:ss", Locale.getDefault()).apply {
    timeZone = TimeZone.getTimeZone("UTC")
}

private fun formatTrackTime(ms: Long?): String {
    return ms?.let { durationFormatter.format(it) } ?: "0:00"
}

@SuppressLint("SimpleDateFormat")
fun TrackDto.toDomain(): Track {
    val cover = artworkUrl100?.replace("100x100bb", "500x500bb")

    val formattedDate = releaseDate?.let { dateString ->
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("d MMMM yyyy")
            val date = inputFormat.parse(dateString)
            date?.let { outputFormat.format(it) } ?: dateString.take(10)
        } catch (e: Exception) {
            dateString.take(10)
        }
    } ?: "Дата неизвестна"

    val isExplicitTrack = when (trackExplicitness?.lowercase()) {
        "explicit" -> true
        "cleaned" -> true
        else -> false
    }

    return Track(
        id = trackId,
        trackName = trackName,
        artistName = artistName,
        trackTimeMillis = trackTimeMillis ?: 0L,
        trackTime = formatTrackTime(trackTimeMillis),
        image = cover,
        favorite = false,
        playlistId = mutableListOf(),
        genre = primaryGenreName ?: "Не указан",
        releaseDate = formattedDate,
        isExplicit = isExplicitTrack
    )
}