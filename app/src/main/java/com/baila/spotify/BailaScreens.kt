package com.baila.spotify

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BailaMainScreen(viewModel: BailaViewModel) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = BailaObsidian,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BailaObsidian)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "baila",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            color = BailaTextPrimary,
                            letterSpacing = (-1).sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = if (state.isSpotifyConnected) SpotifyGreen.copy(alpha = 0.2f) else BailaCardSurface,
                            border = BorderStroke(1.dp, if (state.isSpotifyConnected) SpotifyGreen else BailaCardBorder)
                        ) {
                            Row(
                                modifier = Modifier
                                    .clickable { viewModel.selectTab(2) }
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MusicNote,
                                    contentDescription = null,
                                    tint = if (state.isSpotifyConnected) SpotifyGreen else BailaTextSecondary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (state.isSpotifyConnected) "${state.likedTracks.size} Liked" else "Link Spotify",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (state.isSpotifyConnected) SpotifyGreen else BailaTextSecondary
                                )
                            }
                        }
                    }

                    Text(
                        text = "LIVE • TONIGHT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BailaNeonPink
                    )
                }

                if (state.selectedTab != 2) {
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = state.searchQuery,
                        onValueChange = viewModel::updateSearchQuery,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text("Search clubs, DJs, or vibes...", color = BailaTextSecondary, fontSize = 14.sp)
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = BailaTextSecondary)
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = BailaCardSurface,
                            unfocusedContainerColor = BailaCardSurface,
                            focusedBorderColor = BailaNeonPink,
                            unfocusedBorderColor = BailaCardBorder,
                            focusedTextColor = BailaTextPrimary,
                            unfocusedTextColor = BailaTextPrimary
                        )
                    )
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = BailaCardSurface,
                contentColor = BailaTextPrimary
            ) {
                NavigationBarItem(
                    selected = state.selectedTab == 0,
                    onClick = { viewModel.selectTab(0) },
                    icon = { Icon(Icons.Default.LocalFireDepartment, contentDescription = "Top 10") },
                    label = { Text("Top 10") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BailaNeonPink,
                        selectedTextColor = BailaNeonPink,
                        indicatorColor = BailaNeonPink.copy(alpha = 0.15f),
                        unselectedIconColor = BailaTextSecondary,
                        unselectedTextColor = BailaTextSecondary
                    )
                )
                NavigationBarItem(
                    selected = state.selectedTab == 1,
                    onClick = { viewModel.selectTab(1) },
                    icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Calendar") },
                    label = { Text("Calendar") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BailaElectricCyan,
                        selectedTextColor = BailaElectricCyan,
                        indicatorColor = BailaElectricCyan.copy(alpha = 0.15f),
                        unselectedIconColor = BailaTextSecondary,
                        unselectedTextColor = BailaTextSecondary
                    )
                )
                NavigationBarItem(
                    selected = state.selectedTab == 2,
                    onClick = { viewModel.selectTab(2) },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "My Spotify") },
                    label = { Text("My Vibe") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SpotifyGreen,
                        selectedTextColor = SpotifyGreen,
                        indicatorColor = SpotifyGreen.copy(alpha = 0.15f),
                        unselectedIconColor = BailaTextSecondary,
                        unselectedTextColor = BailaTextSecondary
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (state.selectedTab) {
                0 -> TopEventsTab(state, viewModel)
                1 -> CalendarTab(state, viewModel)
                2 -> SpotifyVibeTab(state, viewModel)
            }
        }
    }
}

@Composable
fun TopEventsTab(state: BailaUiState, viewModel: BailaViewModel) {
    val filteredEvents = state.events.filter { event ->
        val matchesGenre = state.selectedGenre == "All" || event.genres.contains(state.selectedGenre)
        val matchesSearch = state.searchQuery.isBlank() ||
            event.title.contains(state.searchQuery, ignoreCase = true) ||
            event.clubName.contains(state.searchQuery, ignoreCase = true) ||
            event.headlinerDj.contains(state.searchQuery, ignoreCase = true)
        matchesGenre && matchesSearch
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(SampleClubData.genres) { genre ->
                    val isSelected = state.selectedGenre == genre
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectGenre(genre) },
                        label = { Text(genre, fontWeight = FontWeight.SemiBold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BailaNeonPink,
                            selectedLabelColor = Color.White,
                            containerColor = BailaCardSurface,
                            labelColor = BailaTextSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = BailaCardBorder,
                            selectedBorderColor = BailaNeonPink
                        )
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TOP CLUB EVENTS IN REAL-TIME",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = BailaTextSecondary,
                    letterSpacing = 1.sp
                )
                if (state.isSpotifyConnected) {
                    Text(
                        text = "Sorted by Spotify Match",
                        fontSize = 12.sp,
                        color = SpotifyGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        items(filteredEvents, key = { it.id }) { event ->
            ClubEventCard(
                event = event,
                isRsvped = event.id in state.rsvpedEventIds,
                isSpotifyLinked = state.isSpotifyConnected,
                onToggleRsvp = { viewModel.toggleRsvp(event.id) }
            )
        }
    }
}

@Composable
fun CalendarTab(state: BailaUiState, viewModel: BailaViewModel) {
    val selectedDateStr = SampleClubData.calendarDates[state.selectedDateIndex]
    val dateEvents = state.events.filter { it.dateLabel == selectedDateStr }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "EVENT CALENDAR",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = BailaElectricCyan,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(SampleClubData.calendarDates.size) { index ->
                    val selected = state.selectedDateIndex == index
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (selected) BailaElectricCyan else BailaCardSurface,
                        border = BorderStroke(1.dp, if (selected) BailaElectricCyan else BailaCardBorder),
                        modifier = Modifier
                            .width(84.dp)
                            .clickable { viewModel.selectDateIndex(index) }
                    ) {
                        Text(
                            text = SampleClubData.calendarDates[index],
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selected) Color.Black else BailaTextPrimary
                        )
                    }
                }
            }
        }

        if (dateEvents.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = BailaCardSurface),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "New DJ lineups for this date drop at 18:00.",
                        color = BailaTextSecondary,
                        modifier = Modifier.padding(24.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            items(dateEvents, key = { it.id }) { event ->
                ClubEventCard(
                    event = event,
                    isRsvped = event.id in state.rsvpedEventIds,
                    isSpotifyLinked = state.isSpotifyConnected,
                    onToggleRsvp = { viewModel.toggleRsvp(event.id) }
                )
            }
        }
    }
}

@Composable
fun SpotifyVibeTab(state: BailaUiState, viewModel: BailaViewModel) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = BailaCardSurface),
                border = BorderStroke(1.dp, if (state.isSpotifyConnected) SpotifyGreen else BailaCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(SpotifyGreen.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.LibraryMusic, contentDescription = null, tint = SpotifyGreen)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = state.spotifyUserName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = BailaTextPrimary
                            )
                            Text(
                                text = if (state.isSpotifyConnected) {
                                    "${state.likedTracks.size} Liked Songs synced to Baila"
                                } else {
                                    "Link Spotify to match clubs with your Liked Songs"
                                },
                                fontSize = 13.sp,
                                color = BailaTextSecondary
                            )
                        }
                    }

                    state.statusMessage?.let { msg ->
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = msg, fontSize = 12.sp, color = BailaElectricCyan)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (!state.isSpotifyConnected) {
                        Button(
                            onClick = { viewModel.connectSpotify() },
                            colors = ButtonDefaults.buttonColors(containerColor = SpotifyGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Connect Spotify Account", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { viewModel.enableDemoLikedSongs() },
                            border = BorderStroke(1.dp, BailaNeonPink),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Try Demo Liked Songs", color = BailaNeonPink, fontWeight = FontWeight.SemiBold)
                        }
                    } else {
                        OutlinedButton(
                            onClick = { viewModel.disconnectSpotify() },
                            border = BorderStroke(1.dp, BailaCardBorder),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Disconnect Spotify", color = BailaTextSecondary)
                        }
                    }
                }
            }
        }

        if (state.topGenres.isNotEmpty()) {
            item {
                Text(
                    text = "YOUR DETECTED CLUB VIBES",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = BailaTextSecondary,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.topGenres) { genre ->
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = BailaNeonPink.copy(alpha = 0.18f),
                            border = BorderStroke(1.dp, BailaNeonPink)
                        ) {
                            Text(
                                text = genre,
                                color = BailaNeonPink,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        if (state.likedTracks.isNotEmpty()) {
            item {
                Text(
                    text = "YOUR SPOTIFY LIKED SONGS (${state.likedTracks.size})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = BailaTextSecondary,
                    letterSpacing = 1.sp
                )
            }

            items(state.likedTracks, key = { it.id }) { track ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = BailaCardSurface,
                    border = BorderStroke(1.dp, BailaCardBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(track.spotifyUrl))
                            context.startActivity(intent)
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    Brush.linearGradient(listOf(BailaNeonPink, BailaElectricCyan))
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.MusicNote, contentDescription = null, tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = track.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = BailaTextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = track.artist,
                                fontSize = 13.sp,
                                color = BailaTextSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SpotifyGreen.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = track.inferredGenre,
                                color = SpotifyGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClubEventCard(
    event: ClubEvent,
    isRsvped: Boolean,
    isSpotifyLinked: Boolean,
    onToggleRsvp: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = BailaCardSurface),
        border = BorderStroke(1.dp, BailaCardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Neon gradient header banner mimicking Baila's event card visual
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(118.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF2B0B3F),
                                Color(0xFF4A0E4E),
                                Color(0xFF0A2E46)
                            )
                        )
                    )
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = BailaNeonPink
                    ) {
                        Text(
                            text = "#${event.rank} TOP EVENT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color.Black.copy(alpha = 0.65f),
                        border = BorderStroke(1.dp, SpotifyGreen)
                    ) {
                        Text(
                            text = if (isSpotifyLinked) {
                                "${event.vibeMatchPercent}% Spotify Match"
                            } else {
                                "${event.vibeMatchPercent}% Vibe Score"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SpotifyGreen,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Column(modifier = Modifier.align(Alignment.BottomStart)) {
                    Text(
                        text = event.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${event.clubName} • ${event.neighborhood}",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "DJ: ${event.headlinerDj}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BailaTextPrimary
                    )
                    Text(
                        text = event.timeRange,
                        fontSize = 12.sp,
                        color = BailaElectricCyan,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    event.genres.forEach { genre ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = BailaObsidian,
                            border = BorderStroke(1.dp, BailaCardBorder)
                        ) {
                            Text(
                                text = genre,
                                fontSize = 11.sp,
                                color = BailaTextSecondary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = BailaObsidian,
                        border = BorderStroke(1.dp, BailaCardBorder)
                    ) {
                        Text(
                            text = event.crowdEnergy,
                            fontSize = 11.sp,
                            color = BailaNeonPink,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Crew: ${event.friendsGoing.joinToString(", ")}",
                            fontSize = 12.sp,
                            color = BailaTextSecondary
                        )
                        Text(
                            text = event.priceLabel,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = BailaTextPrimary
                        )
                    }

                    Button(
                        onClick = onToggleRsvp,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isRsvped) SpotifyGreen else BailaNeonPink
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (isRsvped) "Going ✓" else "Join Night",
                            color = if (isRsvped) Color.Black else Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
