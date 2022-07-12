package com.fagougou.government.contractReviewPage.camera

import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.fagougou.government.CommonApplication
import com.fagougou.government.utils.CameraUtils

@Composable
fun CameraPreview(callBack:ImageCapture.OnImageSavedCallback){
    AndroidView(
        {
            PreviewView(CommonApplication.activity).apply {
                CameraUtils.initCamera(CommonApplication.activity, this)
                CameraUtils.ImgAddCallback = callBack
            }
        },
        Modifier
            .height(836.dp)
            .fillMaxWidth()
    )
}