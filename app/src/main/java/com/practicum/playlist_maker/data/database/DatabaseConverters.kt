package com.practicum.playlist_maker.data.database

import com.practicum.playlist_maker.data.database.entity.TrackEntity
import com.practicum.playlist_maker.data.database.relation.PlaylistWithTracks
import com.practicum.playlist_maker.data.network.Playlist
import com.practicum.playlist_maker.data.network.Track

fun TrackEntity.toDomain(
    playlistIds: List<Long> = emptyList()
): Track {
    return Track(
        id = id,
        trackName = trackName ?: "Unknown",
        artistName = artistName ?: "Unknown",
        trackTimeMillis = trackTimeMillis ?: 0L,
        trackTime = trackTime ?: "0:00",
        image = image,
        favorite = favorite,
        playlistId = playlistIds.toMutableList()
    )
}

fun Track.toEntity(): TrackEntity {
    return TrackEntity(
        id = id,
        trackName = trackName ?: "Unknown",
        artistName = artistName ?: "Unknown",
        trackTimeMillis = trackTimeMillis ?: 0L,
        trackTime = trackTime ?: "0:00",
        image = image,
        favorite = favorite
    )
}

fun PlaylistWithTracks.toDomain(): Playlist {
    return Playlist(
        id = playlist.id,
        playlistName = playlist.name ?: "Unknown",
        description = playlist.description ?: "",
        coverUri = playlist.coverUri,
        tracks = tracks.map {
            it.toDomain(
                playlistIds = listOf(playlist.id)
            )
        }
    )
}
