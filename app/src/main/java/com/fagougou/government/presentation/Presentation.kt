package com.fagougou.government.presentation

import android.app.Presentation
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.SurfaceHolder
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.R
import com.fagougou.government.databinding.LayoutPresentationBinding
import kotlinx.coroutines.*

class BannerPresentation(context: Context, display: Display) : Presentation(context,display) {
    companion object{
        val mediaPlayer = MediaPlayer()
    }
    val binding = LayoutPresentationBinding.inflate(layoutInflater)
    val bannerAdapter = BannerAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        binding.viewPager.adapter = bannerAdapter
//        CoroutineScope(Dispatchers.Default).launch{
//            var i = 0
//            while (isActive){
//                delay(3000)
//                i++
//                withContext(Dispatchers.Main){
//                    binding.viewPager.currentItem = i
//                }
//            }
//        }
        mediaPlayer.setOnPreparedListener { mediaPlayer.start() }
        mediaPlayer.setOnCompletionListener {  }
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