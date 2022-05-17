package com.xpleemoon.player.source

import com.google.android.exoplayer2.source.MediaSource

internal class CreateInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): MediaSource {
        val realChain = chain as RealInterceptorChain
        val mediaSourceFactory = realChain.mediaSourceFactory
        val uri = realChain.uri
        return mediaSourceFactory.createMediaSource(uri)
    }
}