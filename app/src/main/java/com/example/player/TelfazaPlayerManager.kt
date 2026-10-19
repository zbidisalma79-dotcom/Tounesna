package com.example.player

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.example.data.StreamServer
import com.example.data.TvChannel
import com.example.data.VodEpisode
import com.example.data.VodItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class VideoScaleMode {
    FIT_SCREEN,
    SIXTEEN_BY_NINE,
    ZOOM_FILL
}

data class PlayerUiState(
    val currentChannel: TvChannel? = null,
    val currentServer: StreamServer? = null,
    val currentVodItem: VodItem? = null,
    val currentVodEpisode: VodEpisode? = null,
    val isLiveStream: Boolean = true,
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = true,
    val hasError: Boolean = false,
    val errorMessage: String? = null,
    val isMuted: Boolean = false,
    val volume: Float = 1.0f,
    val isFullscreen: Boolean = false,
    val scaleMode: VideoScaleMode = VideoScaleMode.SIXTEEN_BY_NINE,
    val currentPositionMs: Long = 0L,
    val durationMs: Long = 0L
)

@OptIn(UnstableApi::class)
class TelfazaPlayerManager(private val context: Context) {

    private var exoPlayer: ExoPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    private var progressTrackerJob: Job? = null

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    private val httpDataSourceFactory = DefaultHttpDataSource.Factory()
        .setUserAgent("Mozilla/5.0 (Linux; Android 14; Mobile; rv:109.0) Gecko/109.0 Firefox/119.0")
        .setAllowCrossProtocolRedirects(true)
        .setConnectTimeoutMs(20000)
        .setReadTimeoutMs(20000)

    // Tuned Load Control for fast buffer-free playback
    private val loadControl = DefaultLoadControl.Builder()
        .setBufferDurationsMs(
            /* minBufferMs = */ 15_000,
            /* maxBufferMs = */ 60_000,
            /* bufferForPlaybackMs = */ 1_000,
            /* bufferForPlaybackAfterRebufferMs = */ 2_500
        )
        .setBackBuffer(15_000, true)
        .build()

    fun getPlayer(): ExoPlayer {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context)
                .setLoadControl(loadControl)
                .build().apply {
                    repeatMode = Player.REPEAT_MODE_OFF
                    playWhenReady = true
                    videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING

                    addListener(object : Player.Listener {
                        override fun onPlaybackStateChanged(playbackState: Int) {
                            when (playbackState) {
                                Player.STATE_BUFFERING -> {
                                    _uiState.value = _uiState.value.copy(
                                        isBuffering = true,
                                        hasError = false
                                    )
                                }
                                Player.STATE_READY -> {
                                    val dur = duration.coerceAtLeast(0L)
                                    _uiState.value = _uiState.value.copy(
                                        isBuffering = false,
                                        isPlaying = true,
                                        hasError = false,
                                        errorMessage = null,
                                        durationMs = if (dur == C.TIME_UNSET) 0L else dur
                                    )
                                }
                                Player.STATE_ENDED -> {
                                    _uiState.value = _uiState.value.copy(
                                        isPlaying = false,
                                        isBuffering = false
                                    )
                                }
                                Player.STATE_IDLE -> {
                                    // idle
                                }
                            }
                        }

                        override fun onIsPlayingChanged(isPlaying: Boolean) {
                            _uiState.value = _uiState.value.copy(isPlaying = isPlaying)
                        }

                        override fun onPlayerError(error: PlaybackException) {
                            val currentChannel = _uiState.value.currentChannel
                            val currentServer = _uiState.value.currentServer
                            if (currentChannel != null && currentServer != null) {
                                val allServers = currentChannel.allServers
                                val currentIndex = allServers.indexOfFirst { it.id == currentServer.id }
                                if (currentIndex >= 0 && currentIndex + 1 < allServers.size) {
                                    val nextServer = allServers[currentIndex + 1]
                                    switchServer(nextServer)
                                    return
                                }
                            }

                            _uiState.value = _uiState.value.copy(
                                isBuffering = false,
                                isPlaying = false,
                                hasError = true,
                                errorMessage = "تعذر تشغيل هذا المقطع حالياً. يرجى إعادة المحاولة أو تجربة سيرفر بديل."
                            )
                        }
                    })
                }

            startProgressTracker()
        }
        return exoPlayer!!
    }

    private fun startProgressTracker() {
        progressTrackerJob?.cancel()
        progressTrackerJob = scope.launch {
            while (isActive) {
                exoPlayer?.let { player ->
                    if (player.isPlaying) {
                        val pos = player.currentPosition.coerceAtLeast(0L)
                        val dur = player.duration.coerceAtLeast(0L)
                        _uiState.value = _uiState.value.copy(
                            currentPositionMs = pos,
                            durationMs = if (dur == C.TIME_UNSET) 0L else dur
                        )
                    }
                }
                delay(500)
            }
        }
    }

    // Play Live TV Channel
    fun playChannel(channel: TvChannel, server: StreamServer? = null) {
        val targetServer = server ?: channel.primaryServer
        _uiState.value = _uiState.value.copy(
            currentChannel = channel,
            currentServer = targetServer,
            currentVodItem = null,
            currentVodEpisode = null,
            isLiveStream = true,
            isBuffering = true,
            hasError = false,
            errorMessage = null,
            currentPositionMs = 0L,
            durationMs = 0L
        )

        val player = getPlayer()
        val mediaSource = createMediaSource(targetServer.url)
        player.setMediaSource(mediaSource)
        player.prepare()
        player.playWhenReady = true
    }

    // Play Series Episode or Movie (VOD)
    fun playVod(vodItem: VodItem, episode: VodEpisode? = null) {
        val url = episode?.videoUrl ?: vodItem.directMovieUrl ?: return
        _uiState.value = _uiState.value.copy(
            currentChannel = null,
            currentServer = null,
            currentVodItem = vodItem,
            currentVodEpisode = episode,
            isLiveStream = false,
            isBuffering = true,
            hasError = false,
            errorMessage = null,
            currentPositionMs = 0L,
            durationMs = 0L
        )

        val player = getPlayer()
        val mediaSource = createMediaSource(url)
        player.setMediaSource(mediaSource)
        player.prepare()
        player.playWhenReady = true
    }

    fun switchServer(server: StreamServer) {
        val channel = _uiState.value.currentChannel ?: return
        _uiState.value = _uiState.value.copy(
            currentServer = server,
            isBuffering = true,
            hasError = false,
            errorMessage = null
        )
        val player = getPlayer()
        val mediaSource = createMediaSource(server.url)
        player.setMediaSource(mediaSource)
        player.prepare()
        player.playWhenReady = true
    }

    fun retryCurrent() {
        val channel = _uiState.value.currentChannel
        if (channel != null) {
            val server = _uiState.value.currentServer ?: channel.primaryServer
            switchServer(server)
            return
        }
        val vod = _uiState.value.currentVodItem
        if (vod != null) {
            playVod(vod, _uiState.value.currentVodEpisode)
        }
    }

    fun togglePlayPause() {
        val player = exoPlayer ?: return
        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }
    }

    fun seekTo(positionMs: Long) {
        exoPlayer?.seekTo(positionMs)
        _uiState.value = _uiState.value.copy(currentPositionMs = positionMs)
    }

    fun seekForward10s() {
        val current = exoPlayer?.currentPosition ?: 0L
        val dur = exoPlayer?.duration ?: 0L
        val newPos = (current + 10_000L).coerceAtMost(if (dur > 0) dur else current + 10_000L)
        seekTo(newPos)
    }

    fun seekBack10s() {
        val current = exoPlayer?.currentPosition ?: 0L
        val newPos = (current - 10_000L).coerceAtLeast(0L)
        seekTo(newPos)
    }

    fun toggleMute() {
        val player = exoPlayer ?: return
        val currentlyMuted = _uiState.value.isMuted
        if (currentlyMuted) {
            player.volume = _uiState.value.volume
            _uiState.value = _uiState.value.copy(isMuted = false)
        } else {
            player.volume = 0f
            _uiState.value = _uiState.value.copy(isMuted = true)
        }
    }

    fun setScaleMode(mode: VideoScaleMode) {
        _uiState.value = _uiState.value.copy(scaleMode = mode)
    }

    fun setFullscreen(fullscreen: Boolean) {
        _uiState.value = _uiState.value.copy(isFullscreen = fullscreen)
    }

    fun closePlayer() {
        exoPlayer?.stop()
        _uiState.value = _uiState.value.copy(
            currentChannel = null,
            currentServer = null,
            currentVodItem = null,
            currentVodEpisode = null,
            isPlaying = false,
            isFullscreen = false,
            hasError = false
        )
    }

    fun release() {
        progressTrackerJob?.cancel()
        exoPlayer?.release()
        exoPlayer = null
    }

    private fun createMediaSource(url: String): MediaSource {
        val uri = android.net.Uri.parse(url)
        val mediaItem = MediaItem.fromUri(uri)
        return if (url.contains(".m3u8") || url.contains("chunklist") || url.contains("playlist")) {
            HlsMediaSource.Factory(httpDataSourceFactory)
                .setAllowChunklessPreparation(true)
                .createMediaSource(mediaItem)
        } else {
            ProgressiveMediaSource.Factory(httpDataSourceFactory)
                .createMediaSource(mediaItem)
        }
    }
}
