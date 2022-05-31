package com.xpleemoon.sample.myexoplayer

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.PlaybackPreparer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.TimeBar
import com.google.android.exoplayer2.ui.TimeBar.OnScrubListener
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.xpleemoon.player.AudioPlayer
import com.xpleemoon.player.exception.PlaybackException
import com.xpleemoon.player.listener.PlaybackListener
import com.xpleemoon.player.listener.SimplePlaybackListener
import com.xpleemoon.sample.myexoplayer.playercontroller.PlaybackState

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
        "钢琴-故事的开始" to "https://img.tukuppt.com/preview_music/00/08/60/preview-5b835eb33f4f15003.mp3",
        "周星驰笑声本地asset" to "asset:///周星驰.mp3",
        "新闻联播本地raw" to RawResourceDataSource.buildRawResourceUri(R.raw.cctv).toString(),
    )

    private var debugView: TextView? = null
    private var timeBar: DefaultTimeBar? = null
    private var exoPlay: PlayerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        debugView = findViewById(R.id.debug_view)
        timeBar = findViewById(R.id.timeBar)
        exoPlay = findViewById(R.id.exo_play)
        val uri = Uri.parse("https://cdn.xxxxxx.com/new-sing/66c3d05eaa177e07d57465f948f0d8b934b7a7ba.mp4")
        val player = ExoPlayerFactory.newSimpleInstance(this,
            DefaultTrackSelector(), DefaultLoadControl()
        )

        exoPlay?.player = player

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

        val playbackListener: PlaybackListener = object : SimplePlaybackListener() {

            override fun onPlaybackProgressChanged(
                currentPositionMS: Long,
                bufferedPositionMS: Long,
                durationMS: Long
            ) {
                Log.i(
                    "onPlaybackProgressChanged",
                    "currentPositionMS" + currentPositionMS + "bufferedPositionMS" + bufferedPositionMS + "durationMS" + durationMS
                )
                timeBar?.setDuration(durationMS)
                timeBar?.setBufferedPosition(bufferedPositionMS)
                timeBar?.setPosition(currentPositionMS)
            }

            override fun onPlaybackStateChanged(playbackState: PlaybackState) {

            }

            override fun onPlayerError(error: PlaybackException) {

            }
        }
        audioPlayer.addPlaybackListener(playbackListener)
        timeBar?.addListener(object : OnScrubListener {
            override fun onScrubStart(timeBar: TimeBar, position: Long) {

            }

            override fun onScrubMove(timeBar: TimeBar, position: Long) {
                audioPlayer.seekTo(position)
            }

            override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {}
        })
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
