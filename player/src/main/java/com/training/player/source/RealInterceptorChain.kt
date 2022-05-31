package com.training.player.source

import android.net.Uri
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ads.AdsMediaSource

internal class RealInterceptorChain(private val interceptors: List<Interceptor>,
                                    private val index: Int,
                                    override val mediaSourceFactory: AdsMediaSource.MediaSourceFactory,
                                    override val uri: Uri) : Interceptor.Chain {

    override fun proceed(uri: Uri): MediaSource {
        if (index >= interceptors.size) throw AssertionError()

        // Call the next interceptor in the chain.
        val next = RealInterceptorChain(interceptors, index + 1, mediaSourceFactory, uri)
        val interceptor = interceptors[index]
        return interceptor.intercept(next)
    }
}