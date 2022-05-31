package com.training.player.utils

import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.EventLogger

internal class EventLoggerHelper(private val player: SimpleExoPlayer, private val eventLogger: EventLogger) {
    private var isStarted = false

    @Synchronized
    fun start() {
        if (isStarted) {
            return
        }

        isStarted = true
        player.addAnalyticsListener(eventLogger)
    }

    @Synchronized
    fun stop() {
        isStarted = false
        player.removeAnalyticsListener(eventLogger)
    }
}