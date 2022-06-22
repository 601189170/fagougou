package com.fagougou.government.chatPage

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
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.chatPage.PAG.unwakePAGView
import com.fagougou.government.chatPage.PAG.wakedPAGView
import com.fagougou.government.utils.IFly
import kotlinx.coroutines.*
import org.libpag.PAGFile
import org.libpag.PAGView
import java.lang.Integer.max
import java.lang.Integer.min

object PAG {
    lateinit var unwakePAGView: PAGView
    lateinit var wakedPAGView: PAGView

    init {
        CoroutineScope(Dispatchers.Default).launch(Dispatchers.Default) {
            var unwakeAlpha = 100
            while (isActive) {
                delay(50)
                when (IFly.recognizeResult.value) {
                    IFly.UNWAKE_TEXT -> if (unwakeAlpha < 100) unwakeAlpha = min(unwakeAlpha+10,100)
                    else -> if (unwakeAlpha > 1) unwakeAlpha = max(unwakeAlpha-10,0)
                }
                withContext(Dispatchers.Main) {
                    if (::unwakePAGView.isInitialized) unwakePAGView.alpha =
                        unwakeAlpha.toFloat() / 100f
                    if (::wakedPAGView.isInitialized) wakedPAGView.alpha =
                        (100 - unwakeAlpha).toFloat() / 100f
                }
            }
        }
    }
}

@Composable
fun PAG(){
    Surface(color = Color.Transparent) {
        AndroidView(
            {
                PAGView(activity).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    val pagFile = PAGFile.Load(context.assets, "pag/chat_unwaked.pag")
                    composition = pagFile
                    setRepeatCount(0)
                    play()
                    unwakePAGView = this
                    setOnClickListener {
                        if(alpha==1f)IFly.recognizeMode()
                    }
                }
            },
            Modifier.height(96.dp).width(360.dp),
        )
        AndroidView(
            {
                PAGView(activity).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    val pagFile = PAGFile.Load(context.assets, "pag/chat_waked.pag")
                    composition = pagFile
                    setRepeatCount(0)
                    play()
                    wakedPAGView = this
                }
            },
            Modifier.height(80.dp).width(320.dp),
        )
    }
}