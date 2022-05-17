package com.xpleemoon.player.utils

import android.os.Handler
import android.os.Looper
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.decoder.DecoderCounters
import java.util.*

internal class PlaybackDebugHelper(private val player: SimpleExoPlayer,
                                   private val debugCallback: (String) -> Unit,
                                   private val handler: Handler = Handler(Looper.getMainLooper()))
    : Player.DefaultEventListener(), Runnable {

    private var started: Boolean = false

    /**
     * Starts periodic updates of the [PlaybackDebugCallback]. Must be called from the application's main
     * thread.
     */
    fun start() {
        if (started) {
            return
        }
        started = true
        player.addListener(this)
        updateAndPost()
    }

    /**
     * Stops periodic updates of the [PlaybackDebugCallback]. Must be called from the application's main
     * thread.
     */
    fun stop() {
        if (!started) {
            return
        }
        started = false
        player.removeListener(this)
        handler.removeCallbacks(this)
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        updateAndPost()
    }

    override fun onPositionDiscontinuity(@Player.DiscontinuityReason reason: Int) {
        updateAndPost()
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
        updateAndPost()
    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
        updateAndPost()
    }

    override fun run() {
        updateAndPost()
    }

    private fun updateAndPost() {
        val debugInfo = getDebugString()
        debugCallback(debugInfo)
        handler.removeCallbacks(this)
        handler.postDelayed(this, 1000L)
    }

    /** Returns the debugging information string to be shown by the target [PlaybackDebugCallback].  */
    private fun getDebugString(): String {
        return "repeatMode:${player.repeatMode} playbackSpeed:${player.playbackParameters.speed}\n" +
                getPlayerStateString() + getVideoString() + getAudioString()
    }

    /** Returns a string containing player state debugging information.  */
    private fun getPlayerStateString(): String {
        val playbackStateString: String = when (player.playbackState) {
            Player.STATE_BUFFERING -> "buffering"
            Player.STATE_ENDED -> "ended"
            Player.STATE_IDLE -> "idle"
            Player.STATE_READY -> "ready"
            else -> "unknown"
        }
        return String.format(
                "playWhenReady:%s playbackState:%s window:%s",
                player.playWhenReady, playbackStateString, player.currentWindowIndex)
    }

    /** Returns a string containing video debugging information.  */
    private fun getVideoString(): String {
        val format = player.videoFormat ?: return ""
        return ("\n" + format.sampleMimeType + "(id:" + format.id + " r:" + format.width + "x"
                + format.height + getPixelAspectRatioString(format.pixelWidthHeightRatio)
                + getDecoderCountersBufferCountString(player.videoDecoderCounters) + ")")
    }

    /** Returns a string containing audio debugging information.  */
    private fun getAudioString(): String {
        val format = player.audioFormat ?: return ""
        return ("\n" + format.sampleMimeType + "(id:" + format.id + " hz:" + format.sampleRate + " ch:"
                + format.channelCount
                + getDecoderCountersBufferCountString(player.audioDecoderCounters) + ")")
    }

    private fun getDecoderCountersBufferCountString(counters: DecoderCounters?): String {
        if (counters == null) {
            return ""
        }
        counters.ensureUpdated()
        return (" sib:" + counters.skippedInputBufferCount
                + " sb:" + counters.skippedOutputBufferCount
                + " rb:" + counters.renderedOutputBufferCount
                + " db:" + counters.droppedBufferCount
                + " mcdb:" + counters.maxConsecutiveDroppedBufferCount
                + " dk:" + counters.droppedToKeyframeCount)
    }

    private fun getPixelAspectRatioString(pixelAspectRatio: Float): String {
        return if (pixelAspectRatio == Format.NO_VALUE.toFloat() || pixelAspectRatio == 1f)
            ""
        else
            " par:" + String.format(Locale.US, "%.02f", pixelAspectRatio)
    }
}