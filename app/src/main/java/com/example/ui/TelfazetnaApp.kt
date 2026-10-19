package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.LocalMovies
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SettingsInputAntenna
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TheaterComedy
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.ChannelCategory
import com.example.data.TvChannel
import com.example.ui.components.ChannelCard
import com.example.ui.components.ChannelDetailDialog
import com.example.ui.components.HeroSection
import com.example.ui.components.MoviesView
import com.example.ui.components.ProgramGuideView
import com.example.ui.components.SatelliteFrequenciesView
import com.example.ui.components.SeriesView
import com.example.ui.components.ServerSelectionSheet
import com.example.ui.components.TelfazaVideoPlayerView
import com.example.ui.theme.JasmineGold
import com.example.ui.theme.LiveRed
import com.example.ui.theme.TunisianRedPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelfazetnaApp(
    viewModel: MainViewModel,
    onToggleFullscreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val playerState by viewModel.playerManager.uiState.collectAsState()
    var isSearchExpanded by remember { mutableStateOf(false) }
    var guideSubTab by remember { mutableStateOf(0) } // 0 = Guide, 1 = Frequencies

    // Enforce Arabic RTL layout for authentic Tunisian experience
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                if (!playerState.isFullscreen) {
                    Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
                        TopAppBar(
                            title = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable {
                                        viewModel.selectTab(AppTab.CHANNELS)
                                    }
                                ) {
                                    // App Emblem
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_app_logo),
                                        contentDescription = "شعار تلفزتنا",
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "تلفزتنا",
                                                style = MaterialTheme.typography.titleLarge,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = TunisianRedPrimary
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = LiveRed.copy(alpha = 0.15f),
                                                border = androidx.compose.foundation.BorderStroke(0.5.dp, LiveRed)
                                            ) {
                                                Text(
                                                    text = "تونس",
                                                    color = LiveRed,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                )
                                            }
                                        }
                                        Text(
                                            text = when (uiState.currentTab) {
                                                AppTab.CHANNELS -> "البث المباشر للقنوات التونسية"
                                                AppTab.SERIES -> "شوفلي حل والمسلسلات التونسية كاملة"
                                                AppTab.MOVIES -> "روائع السينما التونسية بدون تقطيع"
                                                AppTab.FAVORITES -> "قنواتك ومسلسلاتك المفضلة"
                                                AppTab.GUIDE, AppTab.FREQUENCIES -> "دليل البرامج وترددات نايل سات"
                                            },
                                            style = MaterialTheme.typography.bodySmall,
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            },
                            actions = {
                                IconButton(
                                    onClick = { isSearchExpanded = !isSearchExpanded },
                                    modifier = Modifier.testTag("toggle_search_button")
                                ) {
                                    Icon(
                                        imageVector = if (isSearchExpanded) Icons.Default.Clear else Icons.Default.Search,
                                        contentDescription = "بحث",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        )

                        // Search Field when expanded
                        AnimatedVisibility(visible = isSearchExpanded) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                OutlinedTextField(
                                    value = uiState.searchQuery,
                                    onValueChange = { viewModel.setSearchQuery(it) },
                                    placeholder = { Text("ابحث عن قناة، حلقة شوفلي حل، أو فيلم…", fontSize = 13.sp) },
                                    singleLine = true,
                                    trailingIcon = {
                                        if (uiState.searchQuery.isNotEmpty()) {
                                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                                Icon(imageVector = Icons.Default.Clear, contentDescription = "مسح")
                                            }
                                        }
                                    },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = TunisianRedPrimary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("search_text_field")
                                )
                            }
                        }
                    }
                }
            },
            bottomBar = {
                if (!playerState.isFullscreen) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp,
                        modifier = Modifier.testTag("bottom_nav_bar")
                    ) {
                        // 1. Live TV Channels
                        NavigationBarItem(
                            selected = uiState.currentTab == AppTab.CHANNELS,
                            onClick = { viewModel.selectTab(AppTab.CHANNELS) },
                            icon = {
                                Icon(imageVector = Icons.Default.LiveTv, contentDescription = "القنوات")
                            },
                            label = { Text("القنوات", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = TunisianRedPrimary.copy(alpha = 0.2f),
                                selectedIconColor = TunisianRedPrimary,
                                selectedTextColor = TunisianRedPrimary
                            ),
                            modifier = Modifier.testTag("tab_channels")
                        )

                        // 2. Choufli Hal & Series
                        NavigationBarItem(
                            selected = uiState.currentTab == AppTab.SERIES,
                            onClick = { viewModel.selectTab(AppTab.SERIES) },
                            icon = {
                                Icon(imageVector = Icons.Default.TheaterComedy, contentDescription = "شوفلي حل")
                            },
                            label = { Text("شوفلي حل", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = TunisianRedPrimary.copy(alpha = 0.2f),
                                selectedIconColor = TunisianRedPrimary,
                                selectedTextColor = TunisianRedPrimary
                            ),
                            modifier = Modifier.testTag("tab_series")
                        )

                        // 3. Tunisian Cinema & Movies
                        NavigationBarItem(
                            selected = uiState.currentTab == AppTab.MOVIES,
                            onClick = { viewModel.selectTab(AppTab.MOVIES) },
                            icon = {
                                Icon(imageVector = Icons.Default.LocalMovies, contentDescription = "أفلام")
                            },
                            label = { Text("أفلام تونسية", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = TunisianRedPrimary.copy(alpha = 0.2f),
                                selectedIconColor = TunisianRedPrimary,
                                selectedTextColor = TunisianRedPrimary
                            ),
                            modifier = Modifier.testTag("tab_movies")
                        )

                        // 4. Favorites
                        NavigationBarItem(
                            selected = uiState.currentTab == AppTab.FAVORITES,
                            onClick = { viewModel.selectTab(AppTab.FAVORITES) },
                            icon = {
                                Icon(imageVector = Icons.Default.Favorite, contentDescription = "المفضلة")
                            },
                            label = { Text("المفضلة", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = TunisianRedPrimary.copy(alpha = 0.2f),
                                selectedIconColor = TunisianRedPrimary,
                                selectedTextColor = TunisianRedPrimary
                            ),
                            modifier = Modifier.testTag("tab_favorites")
                        )

                        // 5. Guide & Frequencies
                        NavigationBarItem(
                            selected = uiState.currentTab == AppTab.GUIDE || uiState.currentTab == AppTab.FREQUENCIES,
                            onClick = { viewModel.selectTab(AppTab.GUIDE) },
                            icon = {
                                Icon(imageVector = Icons.Default.DateRange, contentDescription = "الدليل والترددات")
                            },
                            label = { Text("الدليل", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = TunisianRedPrimary.copy(alpha = 0.2f),
                                selectedIconColor = TunisianRedPrimary,
                                selectedTextColor = TunisianRedPrimary
                            ),
                            modifier = Modifier.testTag("tab_guide")
                        )
                    }
                }
            },
            modifier = modifier
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // If a channel or VOD is playing, display the Video Player
                val currentChannel = playerState.currentChannel
                val currentVod = playerState.currentVodItem
                val isMediaActive = currentChannel != null || currentVod != null

                if (isMediaActive) {
                    val playerModifier = if (playerState.isFullscreen) {
                        Modifier.fillMaxSize()
                    } else {
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                    }

                    TelfazaVideoPlayerView(
                        playerManager = viewModel.playerManager,
                        playerState = playerState,
                        allChannels = viewModel.allChannels,
                        isFavorite = currentChannel != null && uiState.favoriteIds.contains(currentChannel.id),
                        onToggleFavorite = {
                            currentChannel?.let { viewModel.toggleFavorite(it.id) }
                        },
                        onOpenServerSheet = { viewModel.openServerSheet() },
                        onChannelSelect = { ch -> viewModel.playChannel(ch) },
                        onNextChannel = { viewModel.zapNextChannel() },
                        onPrevChannel = { viewModel.zapPrevChannel() },
                        onNextEpisode = { viewModel.playNextEpisode() },
                        onPrevEpisode = { viewModel.playPrevEpisode() },
                        onClosePlayer = { viewModel.closePlayer() },
                        onToggleFullscreen = onToggleFullscreen,
                        modifier = playerModifier
                    )
                }

                // If not in fullscreen, display the active tab's content
                if (!playerState.isFullscreen) {
                    when (uiState.currentTab) {
                        AppTab.CHANNELS, AppTab.FAVORITES -> {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 14.dp),
                                contentPadding = PaddingValues(top = 10.dp, bottom = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Hero Banner (only on Channels tab when search is empty and player is not open)
                                if (uiState.currentTab == AppTab.CHANNELS &&
                                    uiState.searchQuery.isEmpty() &&
                                    !isMediaActive
                                ) {
                                    item {
                                        val featured = viewModel.allChannels.firstOrNull { it.isFeatured }
                                            ?: viewModel.allChannels.first()
                                        HeroSection(
                                            featuredChannel = featured,
                                            onWatchClick = { ch -> viewModel.playChannel(ch) }
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                    }
                                }

                                // Category Chips Bar
                                item {
                                    LazyRow(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        contentPadding = PaddingValues(vertical = 4.dp),
                                        modifier = Modifier.testTag("category_filter_row")
                                    ) {
                                        items(ChannelCategory.values()) { cat ->
                                            val isSelected = uiState.selectedCategory == cat
                                            FilterChip(
                                                selected = isSelected,
                                                onClick = { viewModel.selectCategory(cat) },
                                                label = {
                                                    Text(
                                                        text = cat.titleAr,
                                                        fontSize = 12.sp,
                                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                                    )
                                                },
                                                colors = FilterChipDefaults.filterChipColors(
                                                    selectedContainerColor = TunisianRedPrimary,
                                                    selectedLabelColor = Color.White
                                                ),
                                                shape = RoundedCornerShape(20.dp),
                                                modifier = Modifier.testTag("category_chip_${cat.id}")
                                            )
                                        }
                                    }
                                }

                                // Channels Count & Header
                                item {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = if (uiState.currentTab == AppTab.FAVORITES) "القنوات المفضلة" else "البث الفضائي المباشر",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        Text(
                                            text = "${uiState.filteredChannels.size} قناة",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                // Empty State
                                if (uiState.filteredChannels.isEmpty()) {
                                    item {
                                        Card(
                                            shape = RoundedCornerShape(16.dp),
                                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 24.dp)
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(24.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Icon(
                                                    imageVector = if (uiState.currentTab == AppTab.FAVORITES) Icons.Default.Star else Icons.Default.Tv,
                                                    contentDescription = null,
                                                    tint = JasmineGold,
                                                    modifier = Modifier.size(48.dp)
                                                )
                                                Spacer(modifier = Modifier.height(12.dp))
                                                Text(
                                                    text = if (uiState.currentTab == AppTab.FAVORITES) {
                                                        "لا توجد قنوات في المفضلة حالياً"
                                                    } else {
                                                        "لم يتم العثور على قنوات تطابق البحث"
                                                    },
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 15.sp,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text(
                                                    text = if (uiState.currentTab == AppTab.FAVORITES) {
                                                        "اضغط على رمز النجمة بجانب أي قناة لإضافتها إلى قائمتك المفضلة"
                                                    } else {
                                                        "جرب البحث بكلمات أخرى مثل: الوطنية، الحوار، نسمة، موزاييك"
                                                    },
                                                    fontSize = 12.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    // Channel Cards List
                                    items(uiState.filteredChannels) { channel ->
                                        ChannelCard(
                                            channel = channel,
                                            isPlayingThisChannel = currentChannel?.id == channel.id,
                                            isFavorite = uiState.favoriteIds.contains(channel.id),
                                            onSelect = { viewModel.playChannel(channel) },
                                            onToggleFavorite = { viewModel.toggleFavorite(channel.id) },
                                            onInfoClick = { viewModel.showChannelInfo(channel) }
                                        )
                                    }
                                }
                            }
                        }

                        AppTab.SERIES -> {
                            SeriesView(
                                allSeries = uiState.filteredSeries,
                                selectedSeries = uiState.selectedSeriesForEpisodes,
                                selectedSeasonNumber = uiState.selectedSeasonNumber,
                                currentPlayingEpisode = playerState.currentVodEpisode,
                                onSelectSeries = { series -> viewModel.selectSeries(series, 1) },
                                onSelectSeason = { seasonNum -> viewModel.selectSeason(seasonNum) },
                                onPlayEpisode = { series, ep -> viewModel.playVod(series, ep) }
                            )
                        }

                        AppTab.MOVIES -> {
                            MoviesView(
                                movies = uiState.filteredMovies,
                                currentPlayingMovie = playerState.currentVodItem,
                                onPlayMovie = { movie -> viewModel.playVod(movie, null) }
                            )
                        }

                        AppTab.GUIDE, AppTab.FREQUENCIES -> {
                            Column(modifier = Modifier.fillMaxSize()) {
                                TabRow(
                                    selectedTabIndex = guideSubTab,
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    indicator = { tabPositions ->
                                        TabRowDefaults.Indicator(
                                            modifier = Modifier.tabIndicatorOffset(tabPositions[guideSubTab]),
                                            color = TunisianRedPrimary
                                        )
                                    }
                                ) {
                                    Tab(
                                        selected = guideSubTab == 0,
                                        onClick = { guideSubTab = 0 },
                                        text = { Text("جدول البرامج اليومي", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
                                    )
                                    Tab(
                                        selected = guideSubTab == 1,
                                        onClick = { guideSubTab = 1 },
                                        text = { Text("ترددات الأقمار الفضائية", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
                                    )
                                }

                                if (guideSubTab == 0) {
                                    ProgramGuideView(
                                        programs = viewModel.allPrograms,
                                        channels = viewModel.allChannels,
                                        onChannelSelect = { ch ->
                                            viewModel.playChannel(ch)
                                            viewModel.selectTab(AppTab.CHANNELS)
                                        },
                                        modifier = Modifier.padding(14.dp)
                                    )
                                } else {
                                    SatelliteFrequenciesView(
                                        frequencies = viewModel.allFrequencies,
                                        modifier = Modifier.padding(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Server Selection Sheet
        if (uiState.isServerSheetOpen) {
            ServerSelectionSheet(
                channel = playerState.currentChannel,
                currentServer = playerState.currentServer,
                onSelectServer = { server -> viewModel.selectServer(server) },
                onDismiss = { viewModel.closeServerSheet() }
            )
        }

        // Channel Detail & Frequencies Dialog
        if (uiState.selectedChannelForInfo != null) {
            ChannelDetailDialog(
                channel = uiState.selectedChannelForInfo,
                onPlay = { ch -> viewModel.playChannel(ch) },
                onDismiss = { viewModel.dismissChannelInfo() }
            )
        }
    }
}
