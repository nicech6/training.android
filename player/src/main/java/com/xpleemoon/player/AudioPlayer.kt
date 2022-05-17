package com.xpleemoon.player

import android.content.Context
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.MappingTrackSelector
import com.google.android.exoplayer2.util.EventLogger
import com.xpleemoon.player.controller.PlayController
import com.xpleemoon.player.controller.PlayControllerImpl
import com.xpleemoon.player.source.MediaSourceCreator
import com.xpleemoon.player.source.MediaSourceCreatorImpl
import com.xpleemoon.player.utils.*

/**
 * 音频播放器
 * - 对于[PlayController]和[MediaSourceCreator]这两个继承接口通过[playController]和[mediaSourceCreator]委托实现，
 * 在重写接口方法和属性时需要特别注意，**有接口属性重写需要的场景，建议直接修改属性而非重写**
 * - [next]和[previous]只针对[com.google.android.exoplayer2.source.ConcatenatingMediaSource]有效，
 * 若需要对其他[com.google.android.exoplayer2.source.MediaSource]作支持，请override这两个接口
 */
class AudioPlayer(context: Context,
                  trackSelector: MappingTrackSelector = newDefaultTrackSelector(),
                  private val player: SimpleExoPlayer = newAudioPlayerInstance(context, trackSelector = trackSelector),
                  private val playController: PlayController = PlayControllerImpl(player),
                  private val mediaSourceCreator: MediaSourceCreator = MediaSourceCreatorImpl(context))
    : PlayController by playController, MediaSourceCreator by mediaSourceCreator {
    private val eventLoggerHelper: EventLoggerHelper by lazy { EventLoggerHelper(player, EventLogger(trackSelector)) }
    private var playbackDebugHelper: PlaybackDebugHelper? = null

    override fun release() {
        stopPlaybackDebug()
        stopEventLogger()
        playController.release()
        mediaSourceCreator.release()
    }

    fun startEventLogger() {
        eventLoggerHelper.start()
    }

    fun stopEventLogger() {
        eventLoggerHelper.stop()
    }

    fun startPlaybackDebug(callback: (String) -> Unit) {
        playbackDebugHelper?.stop()
        playbackDebugHelper = PlaybackDebugHelper(player, callback).also {
            it.start()
        }
    }

    fun stopPlaybackDebug() {
        playbackDebugHelper?.stop()
    }
}