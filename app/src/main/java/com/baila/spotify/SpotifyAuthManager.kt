package com.baila.spotify

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Base64
import androidx.browser.customtabs.CustomTabsIntent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.security.MessageDigest
import java.security.SecureRandom

class SpotifyAuthManager(private val context: Context) {

    private val prefs = context.getSharedPreferences("baila_spotify_prefs", Context.MODE_PRIVATE)
    private val client = OkHttpClient()

    fun getAccessToken(): String? = prefs.getString("access_token", null)

    fun saveAccessToken(token: String) {
        prefs.edit().putString("access_token", token).apply()
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    fun startSpotifyLogin() {
        val codeVerifier = generateCodeVerifier()
        val codeChallenge = generateCodeChallenge(codeVerifier)
        prefs.edit().putString("pkce_verifier", codeVerifier).apply()

        val authUri = Uri.parse(SpotifyConfig.AUTH_ENDPOINT).buildUpon()
            .appendQueryParameter("client_id", SpotifyConfig.CLIENT_ID)
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("redirect_uri", SpotifyConfig.REDIRECT_URI)
            .appendQueryParameter("code_challenge_method", "S256")
            .appendQueryParameter("code_challenge", codeChallenge)
            .appendQueryParameter("scope", SpotifyConfig.SCOPES)
            .build()

        try {
            val customTabsIntent = CustomTabsIntent.Builder().build()
            customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            customTabsIntent.launchUrl(context, authUri)
        } catch (e: Exception) {
            val browserIntent = Intent(Intent.ACTION_VIEW, authUri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(browserIntent)
        }
    }

    suspend fun handleAuthCallback(uri: Uri): Result<String> = withContext(Dispatchers.IO) {
        val code = uri.getQueryParameter("code")
            ?: return@withContext Result.failure(Exception(uri.getQueryParameter("error") ?: "Missing auth code"))
        val verifier = prefs.getString("pkce_verifier", null)
            ?: return@withContext Result.failure(Exception("Missing PKCE verifier"))

        val formBody = FormBody.Builder()
            .add("client_id", SpotifyConfig.CLIENT_ID)
            .add("grant_type", "authorization_code")
            .add("code", code)
            .add("redirect_uri", SpotifyConfig.REDIRECT_URI)
            .add("code_verifier", verifier)
            .build()

        val request = Request.Builder()
            .url(SpotifyConfig.TOKEN_ENDPOINT)
            .post(formBody)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                val bodyStr = response.body?.string().orEmpty()
                if (!response.isSuccessful) {
                    return@withContext Result.failure(Exception("Token exchange failed: ${response.code} $bodyStr"))
                }
                val json = JSONObject(bodyStr)
                val accessToken = json.getString("access_token")
                saveAccessToken(accessToken)
                Result.success(accessToken)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun generateCodeVerifier(): String {
        val bytes = ByteArray(64)
        SecureRandom().nextBytes(bytes)
        return Base64.encodeToString(bytes, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
    }

    private fun generateCodeChallenge(verifier: String): String {
        val bytes = verifier.toByteArray(Charsets.US_ASCII)
        val digest = MessageDigest.getInstance("SHA-256").digest(bytes)
        return Base64.encodeToString(digest, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
    }
}
