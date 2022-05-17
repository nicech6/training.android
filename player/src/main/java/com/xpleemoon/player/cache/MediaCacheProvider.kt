package com.xpleemoon.player.cache

import android.content.Context
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File

/**
 * 媒体缓存提供器
 * - 构建同一目录的[SimpleCache]，会抛出异常"Another SimpleCache instance uses the folder: " + cacheDir
 * 不用的时候，请调用[SimpleCache.release]
 */
internal class MediaCacheProvider(context: Context, dirName: String = "player${File.separator}media")
    : CacheProvider<Cache>(context, dirName) {
    override val cache: Cache by lazy {
        SimpleCache(dir, NoOpCacheEvictor())
    }
}