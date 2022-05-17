package com.xpleemoon.player.source

import android.net.Uri
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ads.AdsMediaSource

interface Interceptor {
    fun intercept(chain: Chain): MediaSource

    interface Chain {
        val mediaSourceFactory: AdsMediaSource.MediaSourceFactory
        val uri: Uri
        fun proceed(uri: Uri): MediaSource
    }
}