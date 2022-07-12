package com.fagougou.government.contractReviewPage.camera

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.blankj.utilcode.util.FileUtils
import com.fagougou.government.model.BaseViewModel

object CameraModel : BaseViewModel {
    var index by mutableStateOf(-1)
    val fileList = mutableStateListOf<String>()
    override fun clear() {
        fileList.forEach { FileUtils.delete(it) }
        fileList.clear()
        index=-1
    }
}
