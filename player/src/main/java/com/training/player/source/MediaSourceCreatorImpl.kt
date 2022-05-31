package com.training.player.source

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ts.DefaultTsPayloadReaderFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ads.AdsMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultLoadErrorHandlingPolicy
import com.google.android.exoplayer2.util.Util
import com.training.player.cache.HttpCacheProvider
import com.training.player.extractor.QTHlsExtractorFactory
import com.training.player.utils.BANDWIDTH_METER
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * [MediaSource]构建器默认实现。
 * - 网络请求采用okhttp
 * - 支持DASH、SmoothStreaming、HLS和其它通用编解码
 */
internal class MediaSourceCreatorImpl(private val context: Context) : MediaSourceCreator {
    private val httpCacheProvider = HttpCacheProvider(context)
    private val dataSourceFactory: DataSource.Factory by lazy {
        OkHttpClient.Builder()
                .connectTimeout(10_000, TimeUnit.MILLISECONDS)
                .readTimeout(10_000, TimeUnit.MILLISECONDS)
                .writeTimeout(10_000, TimeUnit.MILLISECONDS)
                .cache(httpCacheProvider.cache)
                .followRedirects(true)
                .followSslRedirects(true)
//                    .addNetworkInterceptor(StethoInterceptor())
                .build()
                .let {
                    // 用于播放器网络请求的ua
                    val userAgent = Util.getUserAgent(context, context.packageName)
                    OkHttpDataSourceFactory(it, userAgent, BANDWIDTH_METER)
                }.let {
                    // 使用DefaultDataSourceFactory的原因是：DefaultDataSource内部支持FileDataSource、
                    // AssetDataSource、ContentDataSource、 RtmpDataSource（rtmp没有暴露超时设置方法，使用的RtmpClient默认的超时貌似是10）、
                    // DataSchemeDataSource和RawResourceDataSource，会根据uri的schema决定使用何种DataSource，避免额外的构建
                    DefaultDataSourceFactory(context, BANDWIDTH_METER, it)
                }
    }
    private val interceptors = mutableListOf<Interceptor>()

    override fun mediaSource(uriStr: String, overrideExtension: String?): MediaSource {
        val uri = Uri.parse(uriStr)
        return mediaSource(uri, overrideExtension)
    }

    override fun mediaSource(uri: Uri, overrideExtension: String?): MediaSource {
        @C.ContentType val mediaType = Util.inferContentType(uri, overrideExtension)
        val mediaSourceFactory = when (mediaType) {
            C.TYPE_DASH/*mpd*/ -> DashMediaSource.Factory(dataSourceFactory)
                    .setLivePresentationDelayMs(10000, true)
                    .setLoadErrorHandlingPolicy(DefaultLoadErrorHandlingPolicy(5))
            C.TYPE_SS/*".*\\.ism(l)?(/manifest(\\(.+\\))?)?"*/ -> SsMediaSource.Factory(dataSourceFactory)
                    .setLivePresentationDelayMs(10000)
                    .setLoadErrorHandlingPolicy(DefaultLoadErrorHandlingPolicy(5))
            C.TYPE_HLS/*m3u8*/ -> HlsMediaSource.Factory(dataSourceFactory)
                    .setExtractorFactory(QTHlsExtractorFactory(DefaultTsPayloadReaderFactory.FLAG_ALLOW_NON_IDR_KEYFRAMES))
                    .setAllowChunklessPreparation(true)
                    .setLoadErrorHandlingPolicy(DefaultLoadErrorHandlingPolicy(5))
            C.TYPE_OTHER -> ExtractorMediaSource.Factory(dataSourceFactory)
                    .setExtractorsFactory(DefaultExtractorsFactory().also {
                        it.setTsExtractorFlags(DefaultTsPayloadReaderFactory.FLAG_ALLOW_NON_IDR_KEYFRAMES)
                    })
                    .setLoadErrorHandlingPolicy(DefaultLoadErrorHandlingPolicy(5))
            else ->
                throw IllegalStateException("Unsupported media type: $mediaType")
        }

        return getMediaSourceWithInterceptorChain(mediaSourceFactory, uri)
    }

    override fun release() {}

    override fun addInterceptor(interceptor: Interceptor) {
        interceptors.add(interceptor)
    }

    private fun getMediaSourceWithInterceptorChain(mediaSourceFactory: AdsMediaSource.MediaSourceFactory, uri: Uri): MediaSource {
        val interceptorsWrapper = mutableListOf<Interceptor>()
        interceptorsWrapper.addAll(interceptors)
        interceptorsWrapper.add(CreateInterceptor())
        val realChain = RealInterceptorChain(interceptorsWrapper, 0, mediaSourceFactory, uri)
        return realChain.proceed(uri)
    }
}