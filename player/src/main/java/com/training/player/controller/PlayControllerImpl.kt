package com.training.player.controller

import android.os.Handler
import android.os.Looper
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.ui.PlayerView
import com.training.player.exception.PlaybackRenderException
import com.training.player.exception.PlaybackSourceException
import com.training.player.exception.PlaybackUnexpectedException
import com.training.player.listener.PlaybackListener
import com.xpleemoon.sample.myexoplayer.playercontroller.PlaybackState
import com.xpleemoon.sample.myexoplayer.playercontroller.getPlaybackStateByCode

private const val TAG = "PlayControllerImpl"
private const val REFRESH_INTERVAL_MS = 1000L
private const val DEFAULT_REWIND_MS: Int = 15 * 1000
private const val DEFAULT_FAST_FORWARD_MS: Int = 15 * 1000

/**
 * 播放控制器默认实现
 */
internal class PlayControllerImpl(private val player: SimpleExoPlayer) : PlayController {
    /**
     * 播放控制分发器
     */
    private val controlDispatcher: ControlDispatcher = DefaultControlDispatcher()
    private var playbackPreparer: PlaybackPreparer? = null

    override var volume: Float
        get() = player.volume
        set(audioVolume) {
            player.volume = audioVolume
        }
    override var supportRepeatModes: IntArray =
        intArrayOf(Player.REPEAT_MODE_OFF, Player.REPEAT_MODE_ONE, Player.REPEAT_MODE_ALL)

    /**
     * 循环模式
     * [细节猛戳](https://medium.com/google-exoplayer/repeat-modes-in-exoplayer-19dd85f036d3)
     */
    override var repeatMode: Int = player.repeatMode
        set(mode) {
            if (mode !in supportRepeatModes) {
                throw IllegalStateException("UnSupport repeat mode:$mode")
            }

            field = mode
            controlDispatcher.dispatchSetRepeatMode(player, mode)
        }
    override var supportPlaybackSpeeds: FloatArray = floatArrayOf(0.5f, 1f, 1.25f, 1.5f, 2f)

    /**
     * 倍速播放
     * [细节猛戳](https://medium.com/google-exoplayer/variable-speed-playback-with-exoplayer-e6e6a71e0343)
     */
    override var playbackSpeed: Float = PlaybackParameters.DEFAULT.speed
        set(speed) {
            if (speed !in supportPlaybackSpeeds) {
                throw IllegalStateException("UnSupport playback speeds:$speed")
            }

            field = speed
            player.playbackParameters = with(player.playbackParameters) {
                PlaybackParameters(speed, this.pitch, this.skipSilence)
            }
        }
    override var rewindIncrementMs: Int = DEFAULT_REWIND_MS
    override var fastForwardIncrementMs: Int = DEFAULT_FAST_FORWARD_MS
    private val updateHandler = Handler(Looper.getMainLooper())
    private val updateProgressAction = Runnable { updateProgress() }
    private var playbackListeners: HashSet<PlaybackListener>? = null
    private val playbackMonitor = object : Player.DefaultEventListener() {

        override fun onTimelineChanged(
            timeline: Timeline?,
            manifest: Any?,
            @Player.TimelineChangeReason reason: Int
        ) {
            updateProgress()
        }

        override fun onPositionDiscontinuity(@Player.DiscontinuityReason reason: Int) {
            updateProgress()
        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            updatePlaybackState()
            updateProgress()
        }

        override fun onPlayerError(error: ExoPlaybackException?) {
            val playbackException =
                if (error != null) {
                    when (error.type) {
                        ExoPlaybackException.TYPE_SOURCE -> PlaybackSourceException(error)
                        ExoPlaybackException.TYPE_RENDERER -> PlaybackRenderException(error)
                        else -> PlaybackUnexpectedException(error)
                    }
                } else {
                    PlaybackUnexpectedException(error)
                }
            playbackListeners?.forEach { it.onPlayerError(playbackException) }
        }

        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
            updatePlaybackSpeed()
        }

        override fun onRepeatModeChanged(@Player.RepeatMode/*没有随机播放模式*/ repeatMode: Int) {
            updateRepeatMode()
        }
    }

    init {
        player.addListener(playbackMonitor)
    }

    override fun setPlaybackPreparer(playbackPreparer: PlaybackPreparer?) {
        this.playbackPreparer = playbackPreparer
    }

    override fun bindPlayView(playerView: PlayerView) {
        playerView.player = player
    }

    override fun unbindPlayView(playerView: PlayerView) {
        playerView.player = null
    }

    /**
     * 播放器准备[MediaSource]
     * - 底层实现为[com.google.android.exoplayer2.ExoPlayer.prepare]
     */
    override fun prepare(mediaSource: MediaSource) {
        player.prepare(mediaSource)
    }

    /**
     * 播放[MediaSource]
     * - [play]某一[MediaSource]之前，确保播放器针对该[MediaSource]已执行[prepare]，否则无法正常播放
     * - 底层实现为[com.google.android.exoplayer2.ExoPlayer.setPlayWhenReady]
     * - 播放不同的[MediaSource]之前，需要重新执行[prepare]
     */
    override fun play() {
        when (player.playbackState) {
            Player.STATE_IDLE -> {
                // 在prepare之前获取当前播放索引（player.currentWindowIndex），避免prepare后当前播放索引被重置为0
                val currentWindowIndexTmp = player.currentWindowIndex
                playbackPreparer?.preparePlayback()
                if (currentWindowIndexTmp > 0) {
                    controlDispatcher.dispatchSeekTo(player, currentWindowIndexTmp, C.TIME_UNSET)
                }
            }
            Player.STATE_ENDED -> controlDispatcher.dispatchSeekTo(
                player,
                player.currentWindowIndex,
                C.TIME_UNSET
            )
        }

        controlDispatcher.dispatchSetPlayWhenReady(player, true)
    }

    /**
     * 暂停播放
     * - 底层实现为[com.google.android.exoplayer2.ExoPlayer.setPlayWhenReady]
     */
    override fun pause() {
        controlDispatcher.dispatchSetPlayWhenReady(player, false)
    }

    /**
     * 停止播放
     * - 底层实现为[com.google.android.exoplayer2.ExoPlayer.stop]
     * - 停止后想要继续播放，需要先调用[prepare]
     */
    override fun stop(reset: Boolean) {
        controlDispatcher.dispatchStop(player, reset)
    }

    override fun toggleRepeatModes() {
        val currentSupportRepeatModes = supportRepeatModes
        val index = currentSupportRepeatModes.indexOf(repeatMode)
        repeatMode =
            if (index >= currentSupportRepeatModes.size - 1)
                currentSupportRepeatModes[0]
            else
                currentSupportRepeatModes[index + 1]
    }

    override fun togglePlaybackSpeeds() {
        val currentSupportPlaybackSpeeds = supportPlaybackSpeeds
        val index = currentSupportPlaybackSpeeds.indexOf(playbackSpeed)
        playbackSpeed =
            if (index >= currentSupportPlaybackSpeeds.size - 1)
                currentSupportPlaybackSpeeds[0]
            else
                currentSupportPlaybackSpeeds[index + 1]
    }

    override fun next() {
        player.currentTimeline?.isEmpty ?: return

        val currentWindowIndex = player.currentWindowIndex
        val nextWindowIndex = player.nextWindowIndex
        if (nextWindowIndex != C.INDEX_UNSET) {
            seekTo(nextWindowIndex, C.TIME_UNSET)
        } else if (player.isCurrentWindowDynamic) {
            seekTo(currentWindowIndex, C.TIME_UNSET)
        }
    }

    override fun previous() {
        player.currentTimeline?.isEmpty ?: return

        val previousWindowIndex = player.previousWindowIndex
        if (previousWindowIndex != C.INDEX_UNSET) {
//                && (player.currentPosition <= MAX_POSITION_FOR_SEEK_TO_PREVIOUS/*3000*/
//                        || player.isCurrentWindowDynamic
//                        && !player.isCurrentWindowSeekable)) {
            seekTo(previousWindowIndex, C.TIME_UNSET)
        } else {
            seekTo(0)
        }
    }

    /**
     * 快进，每次快进时间间隔为[fastForwardIncrementMs]
     */
    override fun fastForward() {
        if (fastForwardIncrementMs <= 0) {
            return
        }

        val durationMs = player.duration
        var seekPositionMs = player.currentPosition + fastForwardIncrementMs
        if (durationMs != C.TIME_UNSET) {
            seekPositionMs = Math.min(seekPositionMs, durationMs)
        }
        seekTo(seekPositionMs)
    }

    /**
     * 后退，每次后退时间间隔为[rewindIncrementMs]
     */
    override fun rewind() {
        if (rewindIncrementMs <= 0) {
            return
        }

        seekTo(Math.max(player.currentPosition - rewindIncrementMs, 0))
    }

    /**
     * 拖动当前媒体资源的播放索引（[player.currentWindowIndex]）和进度
     * @param positionMs 播放进度
     */
    override fun seekTo(positionMs: Long) {
        seekTo(player.currentWindowIndex, positionMs)
    }

    /**
     * 拖动当前媒体资源的播放索引和进度
     * @param windowIndex 播放窗口索引
     * @param positionMs 播放进度
     */
    private fun seekTo(windowIndex: Int, positionMs: Long) {
        if (!controlDispatcher.dispatchSeekTo(player, windowIndex, positionMs)) {
            updateProgress()
        }
    }

    override fun release() {
        setPlaybackPreparer(null)
        playbackListeners?.clear()
        playbackListeners = null
        player.removeListener(playbackMonitor)
        player.release()
    }

    override fun getDurationMS(): Long = player.duration

    override fun getPositionMS(): Long = player.currentPosition

    override fun getBufferedPositionMS(): Long = player.bufferedPosition

    override fun isPlaying(): Boolean = player.playbackState != Player.STATE_ENDED
            && player.playbackState != Player.STATE_IDLE
            && player.playWhenReady

    override fun getPlaybackState(): PlaybackState =
        getPlaybackStateByCode(player.playbackState, player.playWhenReady)

    override fun addPlaybackListener(playbackListener: PlaybackListener) =
        synchronized(this) {
            if (playbackListeners == null) {
                playbackListeners = HashSet(4)
            }
            playbackListeners
        }!!.add(playbackListener)

    override fun removePlaybackListener(playbackListener: PlaybackListener) =
        playbackListeners?.remove(playbackListener) ?: false

    private fun updateAll() {
        updatePlaybackState()
        updatePlaybackSpeed()
        updateRepeatMode()
        updateProgress()
    }

    /**
     * 更新播放状态，比如播放、暂停等
     */
    private fun updatePlaybackState() {
        val state: PlaybackState = getPlaybackState()
        playbackListeners?.forEach { it.onPlaybackStateChanged(state) }
    }

    /**
     * 更新倍速播放
     */
    private fun updatePlaybackSpeed() {
        val currentPlaybackSpeed = playbackSpeed
        playbackListeners?.forEach { it.onPlaybackSpeedChanged(currentPlaybackSpeed) }
    }

    /**
     * 更新循环模式
     */
    private fun updateRepeatMode() {
        val currentRepeatMode = repeatMode
        playbackListeners?.forEach { it.onRepeatModeChanged(currentRepeatMode) }
    }

    /**
     * 更新播放进度
     */
    private fun updateProgress() {
        val currentPositionMS = getPositionMS()
        val currentBufferedPositionMS = getBufferedPositionMS()
        val currentDurationMS = getDurationMS()
        playbackListeners?.forEach {
            it.onPlaybackProgressChanged(
                currentPositionMS,
                currentBufferedPositionMS,
                currentDurationMS
            )
        }

        updateHandler.removeCallbacks(updateProgressAction)
        val playbackState = player.playbackState
        if (playbackState != Player.STATE_IDLE && playbackState != Player.STATE_ENDED) {
            updateHandler.postDelayed(updateProgressAction, REFRESH_INTERVAL_MS)
        }
    }
}