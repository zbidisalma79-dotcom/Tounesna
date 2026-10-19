package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.data.ChannelCategory
import com.example.data.ProgramItem
import com.example.data.SatelliteFrequency
import com.example.data.StreamServer
import com.example.data.TunisianChannelsData
import com.example.data.TunisianVodData
import com.example.data.TvChannel
import com.example.data.VodEpisode
import com.example.data.VodItem
import com.example.player.TelfazaPlayerManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AppTab {
    CHANNELS,
    SERIES,
    MOVIES,
    FAVORITES,
    GUIDE,
    FREQUENCIES
}

data class MainUiState(
    val currentTab: AppTab = AppTab.CHANNELS,
    val selectedCategory: ChannelCategory = ChannelCategory.ALL,
    val searchQuery: String = "",
    val favoriteIds: Set<String> = setOf("watania_1", "watania_2", "el_hiwar", "mosaique_fm_tv"),
    val filteredChannels: List<TvChannel> = emptyList(),
    val filteredSeries: List<VodItem> = emptyList(),
    val filteredMovies: List<VodItem> = emptyList(),
    val selectedSeriesForEpisodes: VodItem? = null,
    val selectedSeasonNumber: Int = 1,
    val isServerSheetOpen: Boolean = false,
    val selectedChannelForInfo: TvChannel? = null,
    val isCompactPlayerVisible: Boolean = false
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val playerManager = TelfazaPlayerManager(application)
    private val prefs = application.getSharedPreferences("telfazetna_prefs", Context.MODE_PRIVATE)

    private val _selectedTab = MutableStateFlow(AppTab.CHANNELS)
    private val _selectedCategory = MutableStateFlow(ChannelCategory.ALL)
    private val _searchQuery = MutableStateFlow("")
    private val _favoriteIds = MutableStateFlow(loadFavorites())
    private val _selectedSeriesForEpisodes = MutableStateFlow<VodItem?>(TunisianVodData.choufliHalSeries)
    private val _selectedSeasonNumber = MutableStateFlow(1)
    private val _isServerSheetOpen = MutableStateFlow(false)
    private val _selectedChannelForInfo = MutableStateFlow<TvChannel?>(null)

    val allChannels: List<TvChannel> = TunisianChannelsData.channels
    val allPrograms: List<ProgramItem> = TunisianChannelsData.programs
    val allFrequencies: List<SatelliteFrequency> = TunisianChannelsData.satelliteFrequencies
    val allSeries: List<VodItem> = TunisianVodData.allSeries
    val allMovies: List<VodItem> = TunisianVodData.allMovies

    private val _uiState = MutableStateFlow(
        MainUiState(
            filteredChannels = allChannels,
            filteredSeries = allSeries,
            filteredMovies = allMovies,
            selectedSeriesForEpisodes = TunisianVodData.choufliHalSeries,
            selectedSeasonNumber = 1
        )
    )
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        updateFilteredContent()
    }

    private fun loadFavorites(): Set<String> {
        val saved = prefs.getStringSet("favorite_channels", null)
        return saved ?: setOf("watania_1", "watania_2", "el_hiwar", "mosaique_fm_tv")
    }

    private fun saveFavorites(favs: Set<String>) {
        prefs.edit().putStringSet("favorite_channels", favs).apply()
    }

    fun selectTab(tab: AppTab) {
        _selectedTab.value = tab
        updateFilteredContent()
    }

    fun selectCategory(category: ChannelCategory) {
        _selectedCategory.value = category
        updateFilteredContent()
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        updateFilteredContent()
    }

    fun selectSeries(series: VodItem, seasonNumber: Int = 1) {
        _selectedSeriesForEpisodes.value = series
        _selectedSeasonNumber.value = seasonNumber
        _uiState.value = _uiState.value.copy(
            selectedSeriesForEpisodes = series,
            selectedSeasonNumber = seasonNumber
        )
    }

    fun selectSeason(seasonNumber: Int) {
        _selectedSeasonNumber.value = seasonNumber
        _uiState.value = _uiState.value.copy(selectedSeasonNumber = seasonNumber)
    }

    fun toggleFavorite(channelId: String) {
        val current = _favoriteIds.value.toMutableSet()
        if (current.contains(channelId)) {
            current.remove(channelId)
        } else {
            current.add(channelId)
        }
        _favoriteIds.value = current
        saveFavorites(current)
        updateFilteredContent()
    }

    fun playChannel(channel: TvChannel, server: StreamServer? = null) {
        playerManager.playChannel(channel, server)
        _uiState.value = _uiState.value.copy(isCompactPlayerVisible = true)
    }

    fun playVod(vodItem: VodItem, episode: VodEpisode? = null) {
        playerManager.playVod(vodItem, episode)
        _uiState.value = _uiState.value.copy(isCompactPlayerVisible = true)
    }

    fun openServerSheet() {
        _isServerSheetOpen.value = true
        _uiState.value = _uiState.value.copy(isServerSheetOpen = true)
    }

    fun closeServerSheet() {
        _isServerSheetOpen.value = false
        _uiState.value = _uiState.value.copy(isServerSheetOpen = false)
    }

    fun selectServer(server: StreamServer) {
        playerManager.switchServer(server)
        closeServerSheet()
    }

    fun showChannelInfo(channel: TvChannel) {
        _selectedChannelForInfo.value = channel
        _uiState.value = _uiState.value.copy(selectedChannelForInfo = channel)
    }

    fun dismissChannelInfo() {
        _selectedChannelForInfo.value = null
        _uiState.value = _uiState.value.copy(selectedChannelForInfo = null)
    }

    fun zapNextChannel() {
        val current = playerManager.uiState.value.currentChannel ?: return
        val list = allChannels
        val currentIndex = list.indexOfFirst { it.id == current.id }
        if (currentIndex != -1) {
            val nextIndex = (currentIndex + 1) % list.size
            playChannel(list[nextIndex])
        }
    }

    fun zapPrevChannel() {
        val current = playerManager.uiState.value.currentChannel ?: return
        val list = allChannels
        val currentIndex = list.indexOfFirst { it.id == current.id }
        if (currentIndex != -1) {
            val prevIndex = if (currentIndex - 1 < 0) list.size - 1 else currentIndex - 1
            playChannel(list[prevIndex])
        }
    }

    fun playNextEpisode() {
        val vod = playerManager.uiState.value.currentVodItem ?: return
        val currentEp = playerManager.uiState.value.currentVodEpisode ?: return
        val currentSeasonNum = _selectedSeasonNumber.value
        val season = vod.seasons.find { it.seasonNumber == currentSeasonNum } ?: vod.seasons.firstOrNull() ?: return
        val epIndex = season.episodes.indexOfFirst { it.episodeNumber == currentEp.episodeNumber }
        if (epIndex != -1 && epIndex + 1 < season.episodes.size) {
            playVod(vod, season.episodes[epIndex + 1])
        }
    }

    fun playPrevEpisode() {
        val vod = playerManager.uiState.value.currentVodItem ?: return
        val currentEp = playerManager.uiState.value.currentVodEpisode ?: return
        val currentSeasonNum = _selectedSeasonNumber.value
        val season = vod.seasons.find { it.seasonNumber == currentSeasonNum } ?: vod.seasons.firstOrNull() ?: return
        val epIndex = season.episodes.indexOfFirst { it.episodeNumber == currentEp.episodeNumber }
        if (epIndex > 0) {
            playVod(vod, season.episodes[epIndex - 1])
        }
    }

    fun closePlayer() {
        playerManager.closePlayer()
        _uiState.value = _uiState.value.copy(isCompactPlayerVisible = false)
    }

    private fun updateFilteredContent() {
        val query = _searchQuery.value.trim().lowercase()
        val category = _selectedCategory.value
        val tab = _selectedTab.value
        val favorites = _favoriteIds.value

        var channels = allChannels
        var series = allSeries
        var movies = allMovies

        if (tab == AppTab.FAVORITES) {
            channels = channels.filter { favorites.contains(it.id) }
        }

        if (category != ChannelCategory.ALL) {
            channels = channels.filter { it.category == category }
        }

        if (query.isNotEmpty()) {
            channels = channels.filter {
                it.nameAr.contains(query, ignoreCase = true) ||
                it.nameFr.lowercase().contains(query) ||
                it.currentProgram.contains(query, ignoreCase = true) ||
                it.description.contains(query, ignoreCase = true)
            }
            series = series.filter {
                it.titleAr.contains(query, ignoreCase = true) ||
                it.description.contains(query, ignoreCase = true) ||
                it.cast.contains(query, ignoreCase = true)
            }
            movies = movies.filter {
                it.titleAr.contains(query, ignoreCase = true) ||
                it.description.contains(query, ignoreCase = true) ||
                it.cast.contains(query, ignoreCase = true)
            }
        }

        _uiState.value = _uiState.value.copy(
            currentTab = tab,
            selectedCategory = category,
            searchQuery = _searchQuery.value,
            favoriteIds = favorites,
            filteredChannels = channels,
            filteredSeries = series,
            filteredMovies = movies,
            selectedSeriesForEpisodes = _selectedSeriesForEpisodes.value ?: TunisianVodData.choufliHalSeries
        )
    }

    override fun onCleared() {
        super.onCleared()
        playerManager.release()
    }
}
