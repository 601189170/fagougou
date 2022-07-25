package com.fagougou.government.contractReviewPage.camera

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.fagougou.government.contractReviewPage.camera.BackButton


object ScanModel{

    var RePhoto= mutableStateOf(false)

    var TackPhoto= mutableStateOf(true)


    fun setCameraStatus(status: String){

        when(status){
            CameraModel.Completed -> TackPhoto.value=false

            CameraModel.Reshoot -> {
                RePhoto.value=true
                TackPhoto.value=true
            }
            CameraModel.TackPhoto -> {
                RePhoto.value=false
                TackPhoto.value=true
            }
        }
    }

}
@Composable
fun ScanPage(navController: NavController) {
    Box(Modifier.fillMaxSize()) {

        PreviewPage()

        PreviewColumn()

        BackButton(navController)

    }
}