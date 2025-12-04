package com.practicum.playlist_maker.data

import com.practicum.playlist_maker.data.network.Playlist
import com.practicum.playlist_maker.data.network.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DatabaseMock(
    private val scope: CoroutineScope,
) {
    private val historyList = mutableListOf<String>()
    private val _historyUpdates = MutableSharedFlow<Unit>()

    private val playlists = mutableListOf<Playlist>()

    private val tracks = mutableListOf(
        Track(1, "25", "Markul", "2:53", "", false, mutableListOf()),
        Track(2, "Baptized In Fear", "The Weeknd", "3:52", "", false, mutableListOf()),
        Track(3, "BOYS DON'T CRY", "GONE.Fludd", "2:27", "", false, mutableListOf()),
        Track(4, "позову звезды смотреть на тебя", "мартин", "3:21", "", false, mutableListOf()),
        Track(5, "St. Patrick's", "Central Cee", "2:40", "", false, mutableListOf()),
        Track(6, "Zima Blue", "Markul", "3:02", "", false, mutableListOf()),
        Track(7, "Obsessed With You", "Central Cee", "1:48", "", false, mutableListOf()),
        Track(8, "Ordinary", "Fabiene Se", "2:20", "", false, mutableListOf()),
        Track(9, "Lucid Dreams", "Juice WRLD", "3:59", "", false, mutableListOf()),
        Track(10, "Ordinary Life", "The Weeknd", "3:41", "", false, mutableListOf())
    )

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
        flow {
            val playlist = playlists.find { it.id == id }
            playlist?.let {
                val playlistTracks = tracks.filter { t -> id in t.playlistId }
                emit(playlist.copy(tracks = playlistTracks))
            } ?: emit(null)
        }

    fun addNewPlaylist(name: String, description: String) {
        playlists.add(
            Playlist(
                id = playlists.size.toLong() + 1,
                name = name,
                description = description,
                tracks = emptyList()
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
        tracks.find { it.id == trackId }?.playlistId?.remove(playlistId)
        notifyTracksChanged()
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

}
