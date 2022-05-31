package com.training.player.controller

import com.google.android.exoplayer2.PlaybackPreparer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.training.player.listener.PlaybackListener
import com.xpleemoon.sample.myexoplayer.playercontroller.PlaybackState

/**
 * 播放控制器
 */
interface PlayController {
    /**
     * 播放器音量
     * - 取值范围：0~1
     */
    var volume: Float

    /**
     * 支持的循环模式
     */
    var supportRepeatModes: IntArray

    /**
     * 当前循环模式
     */
    var repeatMode: Int

    /**
     * 支持的倍速
     */
    var supportPlaybackSpeeds: FloatArray

    /**
     * 当前的播放倍速
     * - 必须大于0
     * - 使用案例：1.5f表示1.5X倍速播放
     */
    var playbackSpeed: Float

    /**
     * 倒退递进时间，时间单位ms
     */
    var rewindIncrementMs: Int

    /**
     * 快进递进时间，时间单位ms
     */
    var fastForwardIncrementMs: Int

    fun setPlaybackPreparer(playbackPreparer: PlaybackPreparer?)

    fun bindPlayView(playerView: PlayerView)

    fun unbindPlayView(playerView: PlayerView)

    /**
     * 播放器准备[MediaSource]
     */
    fun prepare(mediaSource: MediaSource)

    /**
     * 播放
     */
    fun play()

    /**
     * 暂停播放
     */
    fun pause()

    /**
     * 停止播放
     */
    fun stop(reset: Boolean = false)

    /**
     * 开关循环模式
     */
    fun toggleRepeatModes()

    /**
     * 开关倍速播放
     */
    fun togglePlaybackSpeeds()

    /**
     * 下一个
     */
    fun next()

    /**
     * 前一个
     */
    fun previous()

    /**
     * 快进
     */
    fun fastForward()

    /**
     * 后退
     */
    fun rewind()

    /**
     * 拖动当前媒体资源的播放进度
     * @param positionMs 拖动的时间进度节点，单位：ms
     */
    fun seekTo(positionMs: Long)

    /**
     * 释放播放控制器，意味着当前控制器已经失效
     * - 切记在不需要使用的时候调用
     */
    fun release()

    /**
     * 是否播放中
     */
    fun isPlaying(): Boolean

    /**
     * 持续时间
     * - 时间单位为milliseconds
     * - 小于等于0表明播放的媒体资源未设置时间
     */
    fun getDurationMS(): Long

    /**
     * 当前播放位置
     * - 时间单位为milliseconds
     */
    fun getPositionMS(): Long

    /**
     * 缓冲位置
     * - 时间单位为milliseconds
     */
    fun getBufferedPositionMS(): Long

    /**
     * 获取播放状态
     */
    fun getPlaybackState(): PlaybackState

    fun addPlaybackListener(playbackListener: PlaybackListener): Boolean

    fun removePlaybackListener(playbackListener: PlaybackListener): Boolean
}