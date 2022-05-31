package com.training.player.utils

import android.content.Context
import android.os.Handler
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.drm.DrmSessionManager
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.video.VideoRendererEventListener
import java.util.ArrayList

/**
 * 默认带宽监测器
 */
internal val BANDWIDTH_METER = DefaultBandwidthMeter()

/**
 * 创建[AdaptiveTrackSelection.Factory]
 * @param bandwidthMeter 测量播放过程中的带宽，如果不需要，可以为null
 */
internal fun newAdaptiveTrackSelectionFactory(bandwidthMeter: BandwidthMeter? = BANDWIDTH_METER) =
    AdaptiveTrackSelection.Factory(bandwidthMeter)

internal fun newDefaultTrackSelector(factory: TrackSelection.Factory) =
    DefaultTrackSelector(factory)

internal fun newDefaultTrackSelector(bandwidthMeter: BandwidthMeter? = BANDWIDTH_METER): DefaultTrackSelector {
    val factory = newAdaptiveTrackSelectionFactory(bandwidthMeter)
    return newDefaultTrackSelector(factory)
}

internal fun newDefaultPlayerInstance(
    context: Context,
    renderersFactory: RenderersFactory = DefaultRenderersFactory(context)/*媒体渲染组件*/,
    trackSelector: TrackSelector = newDefaultTrackSelector()
) =
    ExoPlayerFactory.newSimpleInstance(context, renderersFactory, trackSelector)

internal fun newAudioPlayerInstance(
    context: Context,
    trackSelector: TrackSelector = newDefaultTrackSelector()
)
        : SimpleExoPlayer {
    val renderersFactory: RenderersFactory = object : DefaultRenderersFactory(context) {
        /**
         * 空实现的目的：禁用视频渲染
         */
        override fun buildVideoRenderers(
            context: Context?,
            drmSessionManager: DrmSessionManager<FrameworkMediaCrypto>?,
            allowedVideoJoiningTimeMs: Long,
            eventHandler: Handler?,
            eventListener: VideoRendererEventListener?,
            extensionRendererMode: Int,
            out: ArrayList<Renderer>?
        ) {
//            super.buildVideoRenderers(context, drmSessionManager, allowedVideoJoiningTimeMs, eventHandler, eventListener, extensionRendererMode, out)
        }
    }
    return newDefaultPlayerInstance(context, renderersFactory, trackSelector)
}