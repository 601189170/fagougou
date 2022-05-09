package com.fagougou.xiaoben.chatPage

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.fagougou.xiaoben.CommonApplication
import com.fagougou.xiaoben.CommonApplication.Companion.activity
import com.fagougou.xiaoben.chatPage.PAG.unwakePAGView
import com.fagougou.xiaoben.chatPage.PAG.wakedPAGView
import com.fagougou.xiaoben.utils.IFly
import kotlinx.coroutines.*
import org.libpag.PAGFile
import org.libpag.PAGView

object PAG {
    lateinit var unwakePAGView: PAGView
    lateinit var wakedPAGView: PAGView

    init {
        CoroutineScope(Dispatchers.Default).launch(Dispatchers.Default) {
            var unwakeAlpha = 100
            while (true) {
                delay(50)
                when (IFly.recognizeResult.value) {
                    IFly.UNWAKE_TEXT -> if(unwakeAlpha<100)unwakeAlpha+=10
                    else -> if (unwakeAlpha>1)unwakeAlpha-=10
                }
                if(::unwakePAGView.isInitialized)unwakePAGView.alpha = unwakeAlpha.toFloat() / 100f
                if(::wakedPAGView.isInitialized)wakedPAGView.alpha = (100-unwakeAlpha).toFloat() / 100f
            }
        }
    }
}

@Composable
fun PAG(){
    Surface(color = Color.Transparent) {
        AndroidView(
            modifier = Modifier
                .height(100.dp)
                .width(400.dp),
            factory = {
                PAGView(activity).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    val pagFile = PAGFile.Load(context.assets, "chat_unwaked.pag")
                    composition = pagFile
                    setRepeatCount(0)
                    play()
                    unwakePAGView = this
                }
            }
        )
        AndroidView(
            modifier = Modifier
                .height(100.dp)
                .width(400.dp),
            factory = {
                PAGView(activity).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    val pagFile = PAGFile.Load(context.assets, "chat_waked.pag")
                    composition = pagFile
                    setRepeatCount(0)
                    play()
                    wakedPAGView = this
                }
            }
        )
    }
}