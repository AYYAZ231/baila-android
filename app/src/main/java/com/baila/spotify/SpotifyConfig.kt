package com.baila.spotify

object SpotifyConfig {
    // Replace with your Client ID from https://developer.spotify.com/dashboard
    const val CLIENT_ID = "YOUR_SPOTIFY_CLIENT_ID"
    const val REDIRECT_URI = "bailaandroid://callback"
    const val SCOPES = "user-library-read user-top-read user-read-private"
    const val AUTH_ENDPOINT = "https://accounts.spotify.com/authorize"
    const val TOKEN_ENDPOINT = "https://accounts.spotify.com/api/token"
    const val API_BASE_URL = "https://api.spotify.com/v1"
}
