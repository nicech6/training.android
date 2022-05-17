package com.xpleemoon.sample.myexoplayer

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.PlaybackPreparer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.xpleemoon.player.AudioPlayer

class MainActivity : AppCompatActivity() {
    private val audioPlayer by lazy {
        AudioPlayer(applicationContext).also {
            it.startEventLogger()
        }
    }
    /**
     * flv貌似不能正常播
     */
    private val sources = mapOf(
                        "周星驰笑声本地asset" to "asset:///周星驰.mp3",
            "新闻联播本地raw" to RawResourceDataSource.buildRawResourceUri(R.raw.cctv).toString(),
            "钢琴-故事的开始" to "https://img.tukuppt.com/preview_music/00/08/60/preview-5b835eb33f4f15003.mp3",
    )

    private var debugView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        debugView = findViewById(R.id.debug_view)

        // 设置播放准备接口
        audioPlayer.setPlaybackPreparer(PlaybackPreparer {
            // TODO 可用于播放页面的初始化
            val mediaSource = ConcatenatingMediaSource()
            sources.forEach {
                mediaSource.addMediaSource(audioPlayer.mediaSource(it.value))
            }
            audioPlayer.prepare(mediaSource)
//            PlayerManager.prepare(PlayerManager.mediaSource("http://rtmpcnr001.cnr.cn/live/zgzs/playlist.m3u8"))
        })
        // 启动播放调试
        audioPlayer.startPlaybackDebug {
            debugView?.text = it
        }
        // 启动播放事件打印
        audioPlayer.startEventLogger()
    }

    fun onPlayControl(view: View) {
        when (view.id) {
            R.id.play -> {
                audioPlayer.play()
            }
            R.id.pause -> {
                audioPlayer.pause()
            }
            R.id.stop -> {
                audioPlayer.stop()
            }
            R.id.repeat_toggle -> {
                audioPlayer.toggleRepeatModes()
            }
            R.id.prev -> {
                audioPlayer.previous()
            }
            R.id.rew -> {
                audioPlayer.rewind()
            }
            R.id.ffwd -> {
                audioPlayer.fastForward()
            }
            R.id.next -> {
                audioPlayer.next()
            }
            R.id.speed_toggle -> {
                audioPlayer.togglePlaybackSpeeds()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        audioPlayer.play()
    }

    override fun onPause() {
        super.onPause()
//        audioPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        audioPlayer.stopPlaybackDebug()
        audioPlayer.stopEventLogger()
        audioPlayer.stop()
        audioPlayer.release()
    }
}
