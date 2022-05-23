package com.fagougou.government.presentation

import android.app.Presentation
import android.content.Context
import android.os.Bundle
import android.view.Display
import com.fagougou.government.databinding.LayoutPresentationBinding
import kotlinx.coroutines.*

class BannerPresentation(context: Context, display: Display) : Presentation(context,display) {
    val binding = LayoutPresentationBinding.inflate(layoutInflater)
    val adapter = BannerAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewPager.adapter = adapter
        CoroutineScope(Dispatchers.Default).launch{
            var i = 0
            while (isActive){
                delay(1500)
                i++
                withContext(Dispatchers.Main){
                    binding.viewPager.currentItem = i
                }
            }
        }
    }
}