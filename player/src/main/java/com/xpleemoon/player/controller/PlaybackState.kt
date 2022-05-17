@file:JvmName("PlaybackStateKT")
package com.xpleemoon.sample.myexoplayer.playercontroller

import com.google.android.exoplayer2.Player

/**
 * 通过exoplayer的播放状态码，获取播放状态
 */
fun getPlaybackStateByCode(code: Int, playWhenReady: Boolean?) =
        when (code) {
            Player.STATE_IDLE/*没有资源可供播放，触发该状态的条件有：调用stop；播放器错误；播放资源未prepare*/ -> PlaybackState.IDLE
            Player.STATE_BUFFERING/*缓冲*/ -> PlaybackState.BUFFERING
            Player.STATE_READY/*playWhenReady为true表示播放，反之为暂停*/ ->
                if (playWhenReady == null || playWhenReady == false) {
                    PlaybackState.PAUSE
                } else {
                    PlaybackState.PLAYING
                }
            Player.STATE_ENDED/*完成所有播放*/ -> PlaybackState.ENDED
            else/*unknown*/ -> PlaybackState.IDLE
        }

/**
 * @param value
 * @param code 播放状态码，取值范围如下
 * - [Player.STATE_IDLE]
 * - [Player.STATE_BUFFERING]
 * - [Player.STATE_READY]
 * - [Player.STATE_ENDED]
 * @param playWhenReady 只有当[code] = [Player.STATE_READY]时，该值才有意义，在这种情形下：true表示播放；false表示暂停
 */
enum class PlaybackState(val value: String, val code: Int, val playWhenReady: Boolean? = null) {
    /**
     * 没有资源可供播放，触发该状态的条件有：
     * - 调用stop
     * - 播放器错误
     * - 播放资源未prepare
     */
    IDLE("IDLE", Player.STATE_IDLE),
    /**
     * 媒体资源缓冲加载
     */
    BUFFERING("BUFFERING", Player.STATE_BUFFERING),
    PLAYING("PLAYING", Player.STATE_READY, true),
    PAUSE("PAUSE", Player.STATE_READY, false),
    /**
     * 完成播放
     */
    ENDED("ENDED", Player.STATE_ENDED)
}