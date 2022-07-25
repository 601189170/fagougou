package com.fagougou.government.contractReviewPage.camera

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.blankj.utilcode.util.FileUtils
import com.fagougou.government.contractReviewPage.camera.ScanModel.setCameraStatus
import com.fagougou.government.model.BaseViewModel

object CameraModel : BaseViewModel {
    var index by mutableStateOf(-1)
    val fileList = mutableStateListOf<String>()
    //重拍
    const val Reshoot = "Reshoot"
    //完成拍照
    const val Completed = "Completed"
    //正常拍照
    const val TackPhoto = "TackPhoto"
    override fun clear() {
        fileList.forEach { FileUtils.delete(it) }
        fileList.clear()
        setCameraStatus(TackPhoto)
        index=-1
    }
}
