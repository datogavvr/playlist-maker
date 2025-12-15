package com.practicum.playlist_maker.data

import androidx.compose.ui.res.painterResource
import com.practicum.playlist_maker.data.network.Playlist
import com.practicum.playlist_maker.data.network.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DatabaseMock(
    private val scope: CoroutineScope,
) {
    private val historyList = mutableListOf<String>()
    private val _historyUpdates = MutableSharedFlow<Unit>()

    private val playlists = mutableListOf<Playlist>()
    private var nextPlaylistId = 1L

    private val tracks = mutableListOf<Track>()

    private val _tracksFlow = MutableStateFlow(tracks.toList())
    val tracksFlow: Flow<List<Track>> get() = _tracksFlow
    private val _playlistsFlow = MutableStateFlow<List<Playlist>>(emptyList())
    val playlistsFlow: Flow<List<Playlist>> = _playlistsFlow


    fun getHistoryRequests(count: Int = 3): List<String> {
        return historyList.takeLast(count)
    }

    fun addToHistory(word: String) {
        if (word.isBlank()) return
        historyList.remove(word)
        historyList.add(0, word)
        if (historyList.size > 3) historyList.subList(3, historyList.size).clear()
        scope.launch(Dispatchers.IO) { _historyUpdates.emit(Unit) }
    }


    fun getAllPlaylists(): Flow<List<Playlist>> = playlistsFlow

    fun getPlaylist(id: Long): Flow<Playlist?> =
        _playlistsFlow.map { list ->
            list.find { it.id == id }?.copy(tracks = tracks.filter { t -> id in t.playlistId })
        }

    fun addNewPlaylist(
        name: String,
        description: String,
        coverUri: String?
    ) {
        playlists.add(
            Playlist(
                id = nextPlaylistId++,
                playlistName = name,
                description = description,
                coverUri = coverUri,
                tracks = emptyList(),
            )
        )
        notifyPlaylistsChanged()
    }

    fun deletePlaylistById(playlistId: Long) {
        playlists.removeIf { it.id == playlistId }
        tracks.forEach { it.playlistId.remove(playlistId) }
        notifyTracksChanged()
        notifyPlaylistsChanged()
    }

    fun getTrackByNameAndArtist(track: Track): Flow<Track?> =
        flow { emit(tracks.find { it.trackName == track.trackName && it.artistName == track.artistName }) }

    fun getTrackById(trackId: Long): Flow<Track?> =
        tracksFlow.map { list -> list.firstOrNull { it.id == trackId } }

    fun insertTrack(track: Track) {
        tracks.removeIf { it.id == track.id }
        tracks.add(track)
        notifyTracksChanged()
    }

    fun updateTrackFavoriteStatus(trackId: Long, favorite: Boolean) {
        tracks.find { it.id == trackId }?.favorite = favorite
        notifyTracksChanged()
    }

    fun addTrackToPlaylist(trackId: Long, playlistId: Long) {
        tracks.find { it.id == trackId }?.playlistId?.apply {
            if (!contains(playlistId)) add(playlistId)
        }
        notifyTracksChanged()
        notifyPlaylistsChanged()
    }

    fun removeTrackFromPlaylist(trackId: Long, playlistId: Long) {
        tracks.find { it.id == trackId }?.playlistId?.apply {
            if (contains(playlistId)) remove(playlistId)
        }
        notifyTracksChanged()
        notifyPlaylistsChanged()
    }

    fun deleteTracksByPlaylistId(playlistId: Long) {
        tracks.forEach { it.playlistId.remove(playlistId) }
        notifyTracksChanged()
    }

    fun getFavoriteTracks(): Flow<List<Track>> =
        tracksFlow.map { list -> list.filter { it.favorite } }

    fun searchTracks(expression: String): List<Track> =
        tracks.filter {
            it.trackName.contains(expression, ignoreCase = true) ||
                    it.artistName.contains(expression, ignoreCase = true)
        }

    private fun notifyTracksChanged() {
        _tracksFlow.value = tracks.toList()
    }

    private fun notifyPlaylistsChanged() {
        val result = playlists.map { playlist ->
            val playlistTracks = tracks.filter { t -> playlist.id in t.playlistId }
            playlist.copy(tracks = playlistTracks)
        }
        _playlistsFlow.value = result
    }

    fun getTrackSync(id: Long): Track? {
        return tracks.find { it.id == id }
    }
}
