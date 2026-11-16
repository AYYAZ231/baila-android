package com.baila.spotify

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class SpotifyApiService {

    private val client = OkHttpClient()

    suspend fun fetchUserProfileAndLikedSongs(accessToken: String): Result<Triple<String, List<LikedTrack>, List<String>>> =
        withContext(Dispatchers.IO) {
            try {
                // 1. Fetch User Profile
                val profileJson = executeGet("${SpotifyConfig.API_BASE_URL}/me", accessToken)
                val displayName = profileJson.optString("display_name", "Spotify User")

                // 2. Fetch User's Top Artists for accurate genre preferences
                val topArtistsJson = executeGet("${SpotifyConfig.API_BASE_URL}/me/top/artists?limit=15", accessToken)
                val artistItems = topArtistsJson.optJSONArray("items")
                val genreCounts = mutableMapOf<String, Int>()
                if (artistItems != null) {
                    for (i in 0 until artistItems.length()) {
                        val genresArr = artistItems.getJSONObject(i).optJSONArray("genres") ?: continue
                        for (g in 0 until genresArr.length()) {
                            val normalized = normalizeGenre(genresArr.getString(g))
                            genreCounts[normalized] = (genreCounts[normalized] ?: 0) + 1
                        }
                    }
                }

                // 3. Fetch User's Liked Songs (/me/tracks)
                val tracksJson = executeGet("${SpotifyConfig.API_BASE_URL}/me/tracks?limit=30", accessToken)
                val items = tracksJson.optJSONArray("items")
                val likedTracks = mutableListOf<LikedTrack>()

                if (items != null) {
                    for (i in 0 until items.length()) {
                        val trackObj = items.getJSONObject(i).getJSONObject("track")
                        val id = trackObj.getString("id")
                        val name = trackObj.getString("name")
                        val artistsArr = trackObj.getJSONArray("artists")
                        val artistNames = (0 until artistsArr.length()).joinToString(", ") {
                            artistsArr.getJSONObject(it).getString("name")
                        }
                        val albumObj = trackObj.getJSONObject("album")
                        val albumName = albumObj.getString("name")
                        val imagesArr = albumObj.optJSONArray("images")
                        val imageUrl = if (imagesArr != null && imagesArr.length() > 0) {
                            imagesArr.getJSONObject(0).getString("url")
                        } else null
                        val externalUrl = trackObj.optJSONObject("external_urls")
                            ?.optString("spotify") ?: "https://open.spotify.com"

                        val inferred = inferGenreFromMetadata(name, artistNames, genreCounts.keys.toList())
                        likedTracks.add(
                            LikedTrack(
                                id = id,
                                title = name,
                                artist = artistNames,
                                album = albumName,
                                imageUrl = imageUrl,
                                spotifyUrl = externalUrl,
                                inferredGenre = inferred
                            )
                        )
                    }
                }

                val rankedGenres = genreCounts.entries
                    .sortedByDescending { it.value }
                    .map { it.key }
                    .ifEmpty { listOf("Afro House", "Tech House", "Reggaeton") }

                Result.success(Triple(displayName, likedTracks, rankedGenres))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    private fun executeGet(url: String, accessToken: String): JSONObject {
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        client.newCall(request).execute().use { response ->
            val body = response.body?.string().orEmpty()
            if (!response.isSuccessful) {
                throw Exception("Spotify API HTTP ${response.code}: $body")
            }
            return JSONObject(body)
        }
    }

    private fun normalizeGenre(raw: String): String {
        val lower = raw.lowercase()
        return when {
            "afro" in lower || "amapiano" in lower -> "Afro House"
            "tech house" in lower || "house" in lower -> "Tech House"
            "reggaeton" in lower || "urbano" in lower -> "Reggaeton"
            "techno" in lower -> "Techno"
            "hip hop" in lower || "rap" in lower || "trap" in lower -> "Hip-Hop"
            "latin" in lower || "salsa" in lower -> "Latin"
            else -> "Tech House"
        }
    }

    private fun inferGenreFromMetadata(title: String, artist: String, topGenres: List<String>): String {
        val text = "$title $artist".lowercase()
        return when {
            "port" in text || "rema" in text || "keinemusik" in text || "afro" in text -> "Afro House"
            "fisher" in text || "dolla" in text || "summit" in text -> "Tech House"
            "bunny" in text || "feid" in text || "karol" in text || "rauw" in text -> "Reggaeton"
            "witte" in text || "amelie" in text || "techno" in text -> "Techno"
            "travis" in text || "drake" in text || "kendrick" in text -> "Hip-Hop"
            "farruko" in text || "shakira" in text || "latin" in text -> "Latin"
            else -> topGenres.firstOrNull() ?: "Tech House"
        }
    }
}
