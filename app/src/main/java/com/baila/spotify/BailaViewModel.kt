package com.baila.spotify

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BailaViewModel(application: Application) : AndroidViewModel(application) {

    private val authManager = SpotifyAuthManager(application.applicationContext)
    private val apiService = SpotifyApiService()

    private val _uiState = MutableStateFlow(BailaUiState())
    val uiState: StateFlow<BailaUiState> = _uiState.asStateFlow()

    init {
        val existingToken = authManager.getAccessToken()
        if (!existingToken.isNullOrBlank()) {
            syncSpotifyLibrary(existingToken)
        }
    }

    fun selectTab(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun selectGenre(genre: String) {
        _uiState.update { it.copy(selectedGenre = genre) }
    }

    fun selectDateIndex(index: Int) {
        _uiState.update { it.copy(selectedDateIndex = index) }
    }

    fun toggleRsvp(eventId: String) {
        _uiState.update { state ->
            val updated = if (eventId in state.rsvpedEventIds) {
                state.rsvpedEventIds - eventId
            } else {
                state.rsvpedEventIds + eventId
            }
            state.copy(rsvpedEventIds = updated)
        }
    }

    fun connectSpotify() {
        if (SpotifyConfig.CLIENT_ID == "YOUR_SPOTIFY_CLIENT_ID") {
            _uiState.update {
                it.copy(
                    statusMessage = "Add your Spotify Client ID in SpotifyConfig.kt or tap 'Try Demo Liked Songs' below!"
                )
            }
            return
        }
        authManager.startSpotifyLogin()
    }

    fun onSpotifyRedirect(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingSpotify = true, statusMessage = "Connecting Spotify...") }
            val tokenResult = authManager.handleAuthCallback(uri)
            tokenResult.fold(
                onSuccess = { token -> syncSpotifyLibrary(token) },
                onFailure = { err ->
                    _uiState.update {
                        it.copy(isLoadingSpotify = false, statusMessage = "Spotify login error: ${err.message}")
                    }
                }
            )
        }
    }

    fun enableDemoLikedSongs() {
        val demoTracks = SampleClubData.demoLikedTracks
        val rankedGenres = demoTracks.groupingBy { it.inferredGenre }.eachCount()
            .entries.sortedByDescending { it.value }.map { it.key }
        val scoredEvents = scoreEventsBySpotify(SampleClubData.defaultEvents, rankedGenres)

        _uiState.update {
            it.copy(
                isSpotifyConnected = true,
                isDemoMode = true,
                spotifyUserName = "Alex (Demo Spotify)",
                likedTracks = demoTracks,
                topGenres = rankedGenres,
                events = scoredEvents,
                statusMessage = "Synced 8 Liked Songs! Events re-ranked by your Spotify Vibe Match."
            )
        }
    }

    fun disconnectSpotify() {
        authManager.clearSession()
        _uiState.update {
            it.copy(
                isSpotifyConnected = false,
                isDemoMode = false,
                spotifyUserName = "Guest Clubber",
                likedTracks = emptyList(),
                topGenres = emptyList(),
                events = SampleClubData.defaultEvents,
                statusMessage = "Spotify disconnected."
            )
        }
    }

    private fun syncSpotifyLibrary(token: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingSpotify = true) }
            val result = apiService.fetchUserProfileAndLikedSongs(token)
            result.fold(
                onSuccess = { (name, tracks, genres) ->
                    val scoredEvents = scoreEventsBySpotify(SampleClubData.defaultEvents, genres)
                    _uiState.update {
                        it.copy(
                            isSpotifyConnected = true,
                            isDemoMode = false,
                            isLoadingSpotify = false,
                            spotifyUserName = name,
                            likedTracks = tracks,
                            topGenres = genres,
                            events = scoredEvents,
                            statusMessage = "Synced ${tracks.size} Liked Songs from Spotify!"
                        )
                    }
                },
                onFailure = { err ->
                    _uiState.update {
                        it.copy(
                            isLoadingSpotify = false,
                            statusMessage = "Sync failed: ${err.message}"
                        )
                    }
                }
            )
        }
    }

    private fun scoreEventsBySpotify(events: List<ClubEvent>, userTopGenres: List<String>): List<ClubEvent> {
        return events.map { event ->
            val matchIndex = event.genres.mapNotNull { g ->
                val idx = userTopGenres.indexOf(g)
                if (idx >= 0) idx else null
            }.minOrNull()

            val score = when (matchIndex) {
                0 -> 97
                1 -> 91
                2 -> 85
                else -> 74
            }
            event.copy(vibeMatchPercent = score)
        }.sortedByDescending { it.vibeMatchPercent }
            .mapIndexed { idx, event -> event.copy(rank = idx + 1) }
    }
}
