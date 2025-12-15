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

fun TrackDto.toDomain(): Track {
    val cover = artworkUrl100?.replace("100x100bb", "500x500bb")
    return Track(
        id = trackId,
        trackName = trackName,
        artistName = artistName,
        trackTimeMillis = trackTimeMillis ?: 0L,
        trackTime = formatTrackTime(trackTimeMillis),
        image = cover,
        favorite = false,
        playlistId = mutableListOf()
    )
}

