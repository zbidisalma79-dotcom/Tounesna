package com.example.ui.components

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import com.example.data.TvChannel
import com.example.player.PlayerUiState
import com.example.player.TelfazaPlayerManager
import com.example.player.VideoScaleMode
import com.example.ui.theme.JasmineGold
import com.example.ui.theme.LiveRed
import com.example.ui.theme.TunisianRedPrimary
import kotlinx.coroutines.delay

@OptIn(UnstableApi::class)
@Composable
fun TelfazaVideoPlayerView(
    playerManager: TelfazaPlayerManager,
    playerState: PlayerUiState,
    allChannels: List<TvChannel>,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onOpenServerSheet: () -> Unit,
    onChannelSelect: (TvChannel) -> Unit,
    onNextChannel: () -> Unit,
    onPrevChannel: () -> Unit,
    onNextEpisode: () -> Unit = {},
    onPrevEpisode: () -> Unit = {},
    onClosePlayer: () -> Unit,
    onToggleFullscreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLive = playerState.isLiveStream
    val channel = playerState.currentChannel
    val vodItem = playerState.currentVodItem
    val vodEpisode = playerState.currentVodEpisode

    if (channel == null && vodItem == null) return

    var areControlsVisible by remember { mutableStateOf(true) }
    var isUserSeeking by remember { mutableStateOf(false) }
    var seekProgressFraction by remember { mutableStateOf(0f) }

    // Auto-hide controls after 5 seconds of inactivity
    LaunchedEffect(areControlsVisible, playerState.isPlaying, isUserSeeking) {
        if (areControlsVisible && playerState.isPlaying && !playerState.isBuffering && !isUserSeeking) {
            delay(5000)
            areControlsVisible = false
        }
    }

    val context = LocalContext.current
    val exoPlayer = remember { playerManager.getPlayer() }

    val displayTitle = if (isLive) {
        channel?.nameAr ?: "بث مباشر"
    } else {
        vodEpisode?.title ?: vodItem?.titleAr ?: "مشاهدة"
    }

    val displaySubtitle = if (isLive) {
        channel?.currentProgram ?: "بث فضائي مباشر"
    } else {
        if (vodEpisode != null) {
            "${vodItem?.titleAr ?: ""} • ${vodEpisode.durationText}"
        } else {
            vodItem?.durationText ?: vodItem?.genre ?: "سينما تونسية"
        }
    }

    Box(
        modifier = modifier
            .background(Color.Black)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                areControlsVisible = !areControlsVisible
            }
            .testTag("video_player_container")
    ) {
        // Native AndroidView with Media3 PlayerView
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                PlayerView(ctx).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    useController = false
                    this.player = exoPlayer
                }
            },
            update = { view ->
                view.player = exoPlayer
            }
        )

        // Gradient Vignette for clear contrast
        AnimatedVisibility(
            visible = areControlsVisible || playerState.isBuffering || playerState.hasError,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.88f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.92f)
                            )
                        )
                    )
            )
        }

        // Top Control Bar
        AnimatedVisibility(
            visible = areControlsVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Identity
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                if (isLive) Color(channel?.accentColorHex ?: 0xFFC40018) else JasmineGold
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isLive) Icons.Default.Tv else Icons.Default.Movie,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = displayTitle,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.width(6.dp))

                            if (isLive) {
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(LiveRed.copy(alpha = 0.9f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(5.dp)
                                            .clip(CircleShape)
                                            .background(Color.White)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "مباشر",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = JasmineGold.copy(alpha = 0.2f),
                                    border = androidx.compose.foundation.BorderStroke(0.5.dp, JasmineGold)
                                ) {
                                    Text(
                                        text = "كامل بدون تقطيع",
                                        color = JasmineGold,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }

                        Text(
                            text = displaySubtitle,
                            color = Color(0xFFCBD5E1),
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Action buttons: Servers, Aspect, Fullscreen, Close
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isLive) {
                        FilledTonalButton(
                            onClick = onOpenServerSheet,
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier
                                .height(32.dp)
                                .testTag("player_servers_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Dns,
                                contentDescription = "السيرفرات",
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = playerState.currentServer?.quality ?: "السيرفرات",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        IconButton(
                            onClick = onToggleFavorite,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "المفضلة",
                                tint = if (isFavorite) TunisianRedPrimary else Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Aspect Ratio Toggle
                    IconButton(
                        onClick = {
                            val nextMode = when (playerState.scaleMode) {
                                VideoScaleMode.SIXTEEN_BY_NINE -> VideoScaleMode.ZOOM_FILL
                                VideoScaleMode.ZOOM_FILL -> VideoScaleMode.FIT_SCREEN
                                VideoScaleMode.FIT_SCREEN -> VideoScaleMode.SIXTEEN_BY_NINE
                            }
                            playerManager.setScaleMode(nextMode)
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AspectRatio,
                            contentDescription = "أبعاد الفيديو",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Fullscreen Toggle
                    IconButton(
                        onClick = onToggleFullscreen,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (playerState.isFullscreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                            contentDescription = "ملء الشاشة",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // Close Button
                    IconButton(
                        onClick = onClosePlayer,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق المشغل",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }

        // Center: Buffering or Play/Pause controls
        Box(
            modifier = Modifier.align(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {
            when {
                playerState.isBuffering -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = TunisianRedPrimary,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(42.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isLive) "جاري تحميل البث المباشر…" else "جاري تحميل المقطع…",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                playerState.hasError -> {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF1E293B).copy(alpha = 0.95f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155)),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "تنبيه التشغيل",
                                color = Color(0xFFF87171),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = playerState.errorMessage ?: "تعذر تشغيل المقطع",
                                color = Color.White,
                                fontSize = 12.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row {
                                FilledTonalButton(
                                    onClick = { playerManager.retryCurrent() },
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "إعادة المحاولة",
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("إعادة المحاولة", fontSize = 12.sp)
                                }
                                if (isLive) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    FilledTonalButton(
                                        onClick = onOpenServerSheet,
                                        modifier = Modifier.height(34.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Dns,
                                            contentDescription = "سيرفر بديل",
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("سيرفر بديل", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                areControlsVisible -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (!isLive) {
                            // Rewind 10s button
                            IconButton(
                                onClick = { playerManager.seekBack10s() },
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.5f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FastRewind,
                                    contentDescription = "ترجيع 10 ثواني",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                        } else {
                            // Prev channel
                            IconButton(
                                onClick = onPrevChannel,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.5f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SkipPrevious,
                                    contentDescription = "القناة السابقة",
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                        }

                        // Play / Pause
                        IconButton(
                            onClick = { playerManager.togglePlayPause() },
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(TunisianRedPrimary.copy(alpha = 0.9f))
                        ) {
                            Icon(
                                imageVector = if (playerState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (playerState.isPlaying) "إيقاف مؤقت" else "تشغيل",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        if (!isLive) {
                            // Forward 10s button
                            IconButton(
                                onClick = { playerManager.seekForward10s() },
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.5f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FastForward,
                                    contentDescription = "تقديم 10 ثواني",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        } else {
                            // Next channel
                            IconButton(
                                onClick = onNextChannel,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.5f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SkipNext,
                                    contentDescription = "القناة التالية",
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom Controls: Slider/Timeline (if VOD) + Quick Zapper / Next Episode
        AnimatedVisibility(
            visible = areControlsVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                // If VOD: Seek bar with timestamps
                if (!isLive && playerState.durationMs > 0L) {
                    val currentPos = if (isUserSeeking) {
                        (seekProgressFraction * playerState.durationMs).toLong()
                    } else {
                        playerState.currentPositionMs
                    }
                    val fraction = if (playerState.durationMs > 0) {
                        (currentPos.toFloat() / playerState.durationMs.toFloat()).coerceIn(0f, 1f)
                    } else 0f

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = formatTimeMs(currentPos),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Slider(
                            value = if (isUserSeeking) seekProgressFraction else fraction,
                            onValueChange = {
                                isUserSeeking = true
                                seekProgressFraction = it
                            },
                            onValueChangeFinished = {
                                val target = (seekProgressFraction * playerState.durationMs).toLong()
                                playerManager.seekTo(target)
                                isUserSeeking = false
                            },
                            colors = SliderDefaults.colors(
                                thumbColor = TunisianRedPrimary,
                                activeTrackColor = TunisianRedPrimary,
                                inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp)
                        )

                        Text(
                            text = formatTimeMs(playerState.durationMs),
                            color = Color(0xFFCBD5E1),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Volume and Navigation Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (isLive) {
                        Text(
                            text = "نايل سات: " + (channel?.frequencyNilesat ?: "11658 V"),
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp
                        )
                    } else if (vodEpisode != null) {
                        Row {
                            FilledTonalButton(
                                onClick = onPrevEpisode,
                                modifier = Modifier.height(30.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("الحلقة السابقة", fontSize = 10.sp)
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            FilledTonalButton(
                                onClick = onNextEpisode,
                                modifier = Modifier.height(30.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("الحلقة التالية", fontSize = 10.sp)
                            }
                        }
                    }

                    // Mute / Unmute
                    IconButton(
                        onClick = { playerManager.toggleMute() },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = if (playerState.isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                            contentDescription = "كتم/تشغيل الصوت",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Quick live channels carousel (if in Live mode)
                if (isLive) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "تبديل سريع للقنوات:",
                        color = Color(0xFFE2E8F0),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(vertical = 2.dp)
                    ) {
                        items(allChannels) { ch ->
                            val isCurrent = ch.id == channel?.id
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isCurrent) TunisianRedPrimary else Color(0xFF1E293B).copy(alpha = 0.85f),
                                border = if (isCurrent) androidx.compose.foundation.BorderStroke(1.dp, Color.White) else null,
                                modifier = Modifier.clickable { onChannelSelect(ch) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(Color(ch.accentColorHex)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = ch.nameAr.take(1),
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = ch.nameAr,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatTimeMs(millis: Long): String {
    val totalSeconds = (millis / 1000).coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    val hours = minutes / 60
    return if (hours > 0) {
        val remMin = minutes % 60
        String.format("%d:%02d:%02d", hours, remMin, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}
