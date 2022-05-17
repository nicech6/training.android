package com.xpleemoon.player.listener

import com.xpleemoon.player.exception.PlaybackException
import com.xpleemoon.sample.myexoplayer.playercontroller.PlaybackState

open class SimplePlaybackListener : PlaybackListener {
    override fun onPlaybackStateChanged(playbackState: PlaybackState) {}

    override fun onPlaybackProgressChanged(currentPositionMS: Long, bufferedPositionMS: Long, durationMS: Long) {}

    override fun onPlayerError(error: PlaybackException) {}

    override fun onPlaybackSpeedChanged(speed: Float) {}

    override fun onRepeatModeChanged(repeatMode: Int) {}
}