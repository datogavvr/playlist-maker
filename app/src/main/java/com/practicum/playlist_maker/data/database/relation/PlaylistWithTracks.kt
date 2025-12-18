package com.practicum.playlist_maker.data.database.relation

import androidx.room.Embedded
import androidx.room.Junction
import com.practicum.playlist_maker.data.database.entity.PlaylistEntity
import androidx.room.Relation
import com.practicum.playlist_maker.data.database.entity.PlaylistsTracks
import com.practicum.playlist_maker.data.database.entity.TrackEntity

data class PlaylistWithTracks(
    @Embedded val playlist: PlaylistEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            PlaylistsTracks::class,
            parentColumn = "playlistId",
            entityColumn = "trackId"
        )
    )
    val tracks: List<TrackEntity>
)
