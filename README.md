# Baila Android + Spotify Liked Songs Integration

An Android app inspired by **Baila** (nightlife & real-time club discovery) enhanced with **Spotify OAuth 2.0 (PKCE)** so your **Spotify Liked Songs** and top genres automatically score and rank club events & DJ nights by **Vibe Match %**.

## Features Included
1. **Real-Time Top 10 Club Events**: Ranked `#1`–`#10` hottest club nights and DJ sets with live capacity indicators and **Spotify Vibe Match %** badges.
2. **Event Calendar**: Horizontal date picker for upcoming club nights, DJ lineups, and guestlist specials.
3. **Smart Search & Genre Filters**: Filter by genre (*Tech House, Afro House, Reggaeton, Techno, Hip-Hop, Latin*) or search clubs/DJs.
4. **Spotify Liked Songs Sync (OAuth 2.0 PKCE)**:
   - Connects directly to your Spotify account (`user-library-read`, `user-top-read`).
   - Displays your **Liked Songs** inside the **My Vibe** tab.
   - Matches your Liked Songs & artist genres against club event lineups.
   - Includes **Instant Demo Mode** so you can test the entire UI before configuring your Spotify Client ID.
5. **Social Crew Activity**: See which friends are attending an event and share club nights.

---

## Step 1: Configure Your Spotify Client ID
1. Go to the [Spotify Developer Dashboard](https://developer.spotify.com/dashboard) and log in.
2. Click **Create App**:
   - **App Name**: `Baila Android`
   - **Redirect URI**: `bailaandroid://callback`
   - **APIs used**: Check **Web API**
3. Copy your **Client ID**.
4. Open `app/src/main/java/com/baila/spotify/SpotifyConfig.kt` and replace `YOUR_SPOTIFY_CLIENT_ID` with your Client ID.

---

## Step 2: Build Your `.apk` File

### Option A: Android Studio (Local PC/Mac)
1. Extract this ZIP archive and open the folder in **Android Studio** (Ladybug or newer).
2. Let Gradle sync dependencies automatically.
3. In the top menu, click **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
4. Once finished, click **locate** in the notification banner to grab `app/build/outputs/apk/debug/app-debug.apk`.
5. Transfer `app-debug.apk` to your Android phone and install it (enable *Install unknown apps* if prompted).

### Option B: Build in the Cloud via GitHub Actions (No PC Setup Needed)
1. Create a new repository on GitHub and upload all files from this ZIP.
2. Open the **Actions** tab in your GitHub repository and run **Build Android APK**.
3. After ~3 minutes, download the compiled **`baila-debug-apk`** artifact directly to your phone!
