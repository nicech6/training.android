package com.xpleemoon.player.listener

import com.google.android.exoplayer2.Player
import com.xpleemoon.player.exception.PlaybackException
import com.xpleemoon.sample.myexoplayer.playercontroller.PlaybackState

/**
 * 播放监听接口
 */
interface PlaybackListener {
    /**
     * 播放状态变化回调
     * @param playbackState 播放状态
     */
    fun onPlaybackStateChanged(playbackState: PlaybackState)

    /**
     * 播放进度改变回调，时间单位：mills
     * @param currentPositionMS 当前进度
     * @param bufferedPositionMS 当前缓冲进度
     * @param durationMS 总时间
     */
    fun onPlaybackProgressChanged(currentPositionMS: Long, bufferedPositionMS: Long, durationMS: Long)

    /**
     * 播放器错误回调
     */
    fun onPlayerError(error: PlaybackException)

    /**
     * 播放倍速变更回调
     */
    fun onPlaybackSpeedChanged(speed: Float)

    /**
     * 循环模式变更回调
     */
    fun onRepeatModeChanged(@Player.RepeatMode repeatMode: Int)
}