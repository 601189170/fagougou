package com.fagougou.government.contractReviewPage

import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.fagougou.government.CommonApplication
import com.fagougou.government.utils.CameraUtils
import com.github.chrisbanes.photoview.PhotoView
import java.io.File

@Composable

fun PhotoImge (filePath:String){
    AndroidView(
        {
            PhotoView(CommonApplication.activity).apply {
                Glide.with(this).load(File(filePath)).into(this)
            }
        },
        modifier = Modifier.width(518.dp).height(732.dp)
    )
}