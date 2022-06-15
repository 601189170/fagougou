package com.fagougou.government.presentation

import android.app.Presentation
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Display
import android.view.SurfaceHolder
import android.view.View
import com.bugsnag.android.Bugsnag
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.databinding.LayoutPresentationBinding
import com.fagougou.government.utils.Tips.toast
import kotlinx.coroutines.*
import java.io.IOException

class BannerPresentation(context: Context, display: Display) : Presentation(context,display) {

    val binding = LayoutPresentationBinding.inflate(layoutInflater)
    val mediaPlayer = MediaPlayer()
    val bannerAdapter = BannerAdapter()
    val videoCallback = object:SurfaceHolder.Callback2{
        override fun surfaceCreated(holder: SurfaceHolder) { mediaPlayer.setSurface(holder.surface) }
        override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) { }
        override fun surfaceDestroyed(p0: SurfaceHolder) { }
        override fun surfaceRedrawNeeded(p0: SurfaceHolder) { }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewPager.adapter = bannerAdapter
        mediaPlayer.setOnPreparedListener { mediaPlayer.start() }
        mediaPlayer.setOnCompletionListener { binding.videoView.visibility = View.GONE }
        mediaPlayer.setOnErrorListener { _, _, _ -> true }
        binding.videoView.holder.addCallback(videoCallback)
        CoroutineScope(Dispatchers.Default).launch{
            var i = 0
            while (isActive){
                delay(5000)
                if(binding.videoView.visibility == View.GONE){
                    i++
                    withContext(Dispatchers.Main){ binding.viewPager.currentItem = i }
                }
            }
        }
    }

    fun playVideo(id:Int){
        mediaPlayer.stop()
        mediaPlayer.seekTo(0)
        mediaPlayer.setDataSource(activity.resources.openRawResourceFd(id))
        binding.videoView.visibility = View.VISIBLE
        try {
            mediaPlayer.prepareAsync()
        }catch (e: IOException){
            toast("副屏已断开")
        }catch (e: Exception){
            Bugsnag.notify(e)
        }
    }
}