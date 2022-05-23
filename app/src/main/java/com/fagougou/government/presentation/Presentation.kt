package com.fagougou.government.presentation

import android.app.Presentation
import android.content.Context
import android.os.Bundle
import android.view.Display
import android.widget.TextView
import androidx.compose.material.Text
import androidx.compose.ui.platform.ComposeView
import com.fagougou.government.R
import com.fagougou.government.databinding.ActivityChooseDomainBinding
import com.fagougou.government.databinding.LayoutPresentationBinding

class BannerPresentation(context: Context, display: Display) : Presentation(context,display) {
    val binding = LayoutPresentationBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}