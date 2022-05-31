package com.training.player.cache

import android.content.Context
import okhttp3.Cache
import java.io.File

/**
 * 网络缓存提供器
 */
internal class HttpCacheProvider(context: Context, dirName: String = "player${File.separator}http")
    : CacheProvider<Cache>(context, dirName) {
    override val cache: Cache by lazy {
        Cache(dir, 1024 * 1024 * 10L)
    }
}