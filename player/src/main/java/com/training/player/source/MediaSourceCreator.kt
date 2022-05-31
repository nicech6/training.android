package com.training.player.source

import android.net.Uri
import com.google.android.exoplayer2.source.MediaSource

/**
 * [MediaSource]构建器
 */
interface MediaSourceCreator {
    /**
     * 根据[uriStr]和[overrideExtension]创建基础[MediaSource]（非复合类型[com.google.android.exoplayer2.source.CompositeMediaSource]），
     * 创建逻辑为：若[overrideExtension]不为空，则根据该值获取[MediaSource]；反之，则根据[uriStr]的path获取
     * @param uriStr 媒体资源uri地址
     * @param overrideExtension 媒体资源的格式
     */
    fun mediaSource(uriStr: String, overrideExtension: String? = null): MediaSource

    /**
     * 根据[uri]和[overrideExtension]创建基础[MediaSource]，非复合类型[com.google.android.exoplayer2.source.CompositeMediaSource]，
     * 创建逻辑为：若[overrideExtension]不为空，则根据该值获取[MediaSource]；反之，则根据[uri]的path获取
     * @param uri 媒体资源uri地址
     * @param overrideExtension 媒体资源的格式
     */
    fun mediaSource(uri: Uri, overrideExtension: String? = null): MediaSource

    /**
     * 释放资源后，意味着当前构建器已经失效
     * - 切记在不需要使用的时候调用
     */
    fun release()

    fun addInterceptor(interceptor: Interceptor)
}