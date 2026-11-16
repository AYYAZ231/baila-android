package com.baila.spotify

data class LikedTrack(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val imageUrl: String?,
    val spotifyUrl: String,
    val inferredGenre: String
)

data class ClubEvent(
    val id: String,
    val rank: Int,
    val title: String,
    val clubName: String,
    val neighborhood: String,
    val dateLabel: String,
    val timeRange: String,
    val genres: List<String>,
    val headlinerDj: String,
    val friendsGoing: List<String>,
    val crowdEnergy: String,
    val priceLabel: String,
    val vibeMatchPercent: Int = 78
)

data class BailaUiState(
    val selectedTab: Int = 0, // 0 = Top 10, 1 = Calendar, 2 = My Vibe (Spotify)
    val searchQuery: String = "",
    val selectedGenre: String = "All",
    val selectedDateIndex: Int = 0,
    val isSpotifyConnected: Boolean = false,
    val isDemoMode: Boolean = false,
    val isLoadingSpotify: Boolean = false,
    val spotifyUserName: String = "Guest Clubber",
    val likedTracks: List<LikedTrack> = emptyList(),
    val topGenres: List<String> = emptyList(),
    val events: List<ClubEvent> = SampleClubData.defaultEvents,
    val rsvpedEventIds: Set<String> = emptySet(),
    val statusMessage: String? = null
)

object SampleClubData {
    val genres = listOf("All", "Afro House", "Tech House", "Reggaeton", "Techno", "Hip-Hop", "Latin")

    val calendarDates = listOf(
        "TONIGHT\nFRI 20",
        "SAT\n21",
        "SUN\n22",
        "THU\n26",
        "FRI\n27",
        "SAT\n28"
    )

    val demoLikedTracks = listOf(
        LikedTrack("1", "Move", "Adam Port, Stryv", "Move - Single", null, "https://open.spotify.com", "Afro House"),
        LikedTrack("2", "Losing It", "FISHER", "Losing It", null, "https://open.spotify.com", "Tech House"),
        LikedTrack("3", "Tití Me Preguntó", "Bad Bunny", "Un Verano Sin Ti", null, "https://open.spotify.com", "Reggaeton"),
        LikedTrack("4", "Miracle Maker", "Dom Dolla", "Miracle Maker", null, "https://open.spotify.com", "Tech House"),
        LikedTrack("5", "Calm Down", "Rema", "Rave & Roses", null, "https://open.spotify.com", "Afro House"),
        LikedTrack("6", "FE!N", "Travis Scott", "UTOPIA", null, "https://open.spotify.com", "Hip-Hop"),
        LikedTrack("7", "Pepas", "Farruko", "La 167", null, "https://open.spotify.com", "Latin"),
        LikedTrack("8", "Age of Love (Charlotte de Witte Remix)", "Charlotte de Witte", "Selected", null, "https://open.spotify.com", "Techno")
    )

    val defaultEvents = listOf(
        ClubEvent(
            id = "ev1",
            rank = 1,
            title = "NOCHE DE BAILA: AFRO & HOUSE",
            clubName = "Club Velvet",
            neighborhood = "Downtown • 1.2 km",
            dateLabel = "TONIGHT\nFRI 20",
            timeRange = "23:00 – 05:00",
            genres = listOf("Afro House", "Tech House"),
            headlinerDj = "DJ Kian & Stryv Sound",
            friendsGoing = listOf("Maya", "Lukas", "Sofia", "+14"),
            crowdEnergy = "98% Full • Hot",
            priceLabel = "Guestlist till 00:30"
        ),
        ClubEvent(
            id = "ev2",
            rank = 2,
            title = "PERREO INTENSO X REGGAETON",
            clubName = "La Bodega Rooftop",
            neighborhood = "Marina • 2.4 km",
            dateLabel = "TONIGHT\nFRI 20",
            timeRange = "22:30 – 04:30",
            genres = listOf("Reggaeton", "Latin"),
            headlinerDj = "DJ Mateo & Reina",
            friendsGoing = listOf("Carlos", "Elena", "+9"),
            crowdEnergy = "94% Full • Trending",
            priceLabel = "$15 Entry"
        ),
        ClubEvent(
            id = "ev3",
            rank = 3,
            title = "WAREHOUSE BERLIN SESSIONS",
            clubName = "Bunker 88",
            neighborhood = "Industrial District • 3.8 km",
            dateLabel = "SAT\n21",
            timeRange = "00:00 – 07:00",
            genres = listOf("Techno", "Tech House"),
            headlinerDj = "Klangwerk Live",
            friendsGoing = listOf("Jonas", "+21"),
            crowdEnergy = "91% Full • Peak",
            priceLabel = "$20 Ticket"
        ),
        ClubEvent(
            id = "ev4",
            rank = 4,
            title = "808 AFTERHOURS: TRAP & R&B",
            clubName = "Onyx Lounge",
            neighborhood = "Midtown • 1.9 km",
            dateLabel = "SAT\n21",
            timeRange = "23:00 – 04:00",
            genres = listOf("Hip-Hop"),
            headlinerDj = "DJ Blaze",
            friendsGoing = listOf("Tariq", "Zoe"),
            crowdEnergy = "88% Full",
            priceLabel = "Free before 00:00"
        ),
        ClubEvent(
            id = "ev5",
            rank = 5,
            title = "SUNSET RITUALS: MELODIC & AFRO",
            clubName = "Skyline Deck",
            neighborhood = "Harbor • 4.1 km",
            dateLabel = "SUN\n22",
            timeRange = "18:00 – 01:00",
            genres = listOf("Afro House", "Latin"),
            headlinerDj = "Sora & Friends",
            friendsGoing = listOf("Maya", "Nadia", "+6"),
            crowdEnergy = "85% Full",
            priceLabel = "$12 Early Bird"
        )
    )
}
