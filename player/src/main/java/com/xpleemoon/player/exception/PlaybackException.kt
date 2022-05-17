package com.xpleemoon.player.exception

sealed class PlaybackException(message: String?, cause: Throwable?) : Exception(message, cause)

class PlaybackSourceException(cause: Throwable?) : PlaybackException("媒体资源加载错误：The error occurred loading data from a MediaSource", cause)

class PlaybackRenderException(cause: Throwable?) : PlaybackException("播放器渲染错误：The error occurred in a Renderer", cause)

class PlaybackUnexpectedException(cause: Throwable?) : PlaybackException("播放器未预期错误：The error was an unexpected link RuntimeException", cause)