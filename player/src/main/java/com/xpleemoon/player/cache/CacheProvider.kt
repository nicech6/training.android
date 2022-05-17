package com.xpleemoon.player.cache

import android.content.Context
import java.io.File

/**
 * 缓存提供器
 * @param T 泛型类型为[cache]对应的类型
 * @param dirName 缓存目录名，相对路径
 */
internal abstract class CacheProvider<T>(context: Context, dirName: String) {
    /**
     * 缓存目录
     */
    val dir: File by lazy {
        val parentDir = context.getExternalFilesDir(null) ?: context.filesDir
        File(parentDir, dirName)
    }
    /**
     * 缓存
     */
    abstract val cache: T
}