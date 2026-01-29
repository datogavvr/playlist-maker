package com.practicum.playlist_maker.data.database

import com.practicum.playlist_maker.data.database.entity.PlaylistEntity
import com.practicum.playlist_maker.data.database.entity.TrackEntity
import com.practicum.playlist_maker.data.database.relation.PlaylistWithTracks
import com.practicum.playlist_maker.data.network.Playlist
import com.practicum.playlist_maker.data.network.Track

fun TrackEntity.toDomain(
    playlistIds: List<Long> = emptyList()
): Track {
    return Track(
        id = id,
        trackName = trackName,
        artistName = artistName,
        trackTimeMillis = trackTimeMillis ?: 0L,
        trackTime = trackTime,
        image = image,
        favorite = playlistIds.contains(0L),
        playlistId = playlistIds.toMutableList(),
        genre = genre ?: "Не указан",
        releaseDate = releaseDate ?: "Дата неизвестна",
        isExplicit = isExplicit ?: false
    )
}

fun Track.toEntity(): TrackEntity {
    return TrackEntity(
        id = id,
        trackName = trackName,
        artistName = artistName,
        trackTimeMillis = trackTimeMillis ?: 0L,
        trackTime = trackTime,
        image = image,
        genre = genre,
        releaseDate = releaseDate,
        isExplicit = isExplicit
    )
}

fun PlaylistEntity.toDomain(): Playlist {
    return Playlist(
        id = id,
        playlistName = name,
        description = description ?: "",
        coverUri = coverUri,
        tracks = emptyList()
    )
}

fun PlaylistWithTracks.toDomain(): Playlist {
    return Playlist(
        id = playlist.id,
        playlistName = playlist.name,
        description = playlist.description ?: "",
        coverUri = playlist.coverUri,
        tracks = tracks.map {
            it.toDomain(
                playlistIds = listOf(playlist.id)
            )
        }
    )
}