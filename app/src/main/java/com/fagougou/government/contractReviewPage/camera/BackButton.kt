package com.fagougou.government.contractReviewPage.camera

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.dialog.DialogViewModel
import com.fagougou.government.model.ContentStyle

@Composable
fun BackButton(navController: NavController){
    Surface(
        Modifier.padding(start = 24.dp, top = 24.dp),
        color = Color(0x33000000),
        shape = RoundedCornerShape(32.dp)
    ) {
        Row(
            Modifier
                .width(136.dp)
                .height(64.dp)
                .clickable {
                    with(DialogViewModel) {
                        clear()
                        title = "温馨提示"
                        firstButtonText.value = "取消"
                        firstButtonOnClick.value = { content.clear() }
                        secondButtonText.value = "确定"
                        secondButtonOnClick.value = {
                            content.clear()
                            CameraModel.clear()
                            navController.popBackStack(Router.home, false)
                        }
                        content.add(ContentStyle("返回后将丢失本次上传的图片"))
                    }
                },
            Arrangement.Center,
            Alignment.CenterVertically,
        ) {
            Image(painterResource(R.drawable.ic_back), null)
            Text("返回", Modifier.padding(start = 5.dp), fontSize = 20.sp, color = Color.White)
        }
    }
}