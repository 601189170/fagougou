package com.fagougou.government.presentation

import android.app.Presentation
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.R
import com.fagougou.government.databinding.LayoutPresentationBinding
import kotlinx.coroutines.*

class BannerPresentation(context: Context, display: Display) : Presentation(context,display) {
    companion object{
        val mediaPlayer = MediaPlayer()
        var videoView: SurfaceView? = null
        fun playVideo(id:Int){
            mediaPlayer.stop()
            mediaPlayer.seekTo(0)
            mediaPlayer.setDataSource(activity.resources.openRawResourceFd(id))
            videoView?.visibility = View.VISIBLE
            mediaPlayer.prepareAsync()
        }
    }
    val binding = LayoutPresentationBinding.inflate(layoutInflater)
    val bannerAdapter = BannerAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewPager.adapter = bannerAdapter
        videoView = binding.videoView
        CoroutineScope(Dispatchers.Default).launch{
            var i = 0
            while (isActive){
                delay(3000)
                if(binding.videoView.visibility == View.GONE){
                    i++
                    withContext(Dispatchers.Main){
                        binding.viewPager.currentItem = i
                    }
                }
            }
        }
        mediaPlayer.setOnPreparedListener { mediaPlayer.start() }
        mediaPlayer.setOnCompletionListener {
            binding.videoView.visibility = View.GONE
        }
        mediaPlayer.setOnErrorListener { _, _, _ -> true }

        val videoCallback = object:SurfaceHolder.Callback2{
            override fun surfaceCreated(holder: SurfaceHolder) {
                mediaPlayer.setSurface(holder.surface)
            }

            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) { }

            override fun surfaceDestroyed(p0: SurfaceHolder) { }

            override fun surfaceRedrawNeeded(p0: SurfaceHolder) { }
        }

        binding.videoView.holder.addCallback(videoCallback)
    }
}