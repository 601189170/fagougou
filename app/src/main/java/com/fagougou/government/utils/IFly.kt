package com.fagougou.government.utils

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.bugsnag.android.Bugsnag
import com.fagougou.government.Router
import com.fagougou.government.chatPage.ChatViewModel.nextChat
import com.fagougou.government.chatPage.ChatViewModel.selectedChatBot
import com.fagougou.government.chatPage.ChatViewModel.voiceInputMode
import com.fagougou.government.Router.routeMirror
import com.fagougou.government.utils.Time.stampL
import com.fagougou.government.utils.Tips.toast
import com.iflytek.cloud.*
import com.iflytek.cloud.util.ResourceUtil
import com.iflytek.cloud.util.ResourceUtil.RESOURCE_TYPE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import timber.log.Timber
import java.lang.Exception

object IFly {
    const val UNWAKE_TEXT = "请说,你好小法"
    const val WAKE_TEXT = "请说出您的问题"
    val TAG = javaClass.simpleName
    val resultBuilder = StringBuilder()
    val recognizeResult = mutableStateOf(UNWAKE_TEXT)
    val volumeState = mutableStateOf("=")

    var mIatResults = mutableMapOf<String, String>()
    val mInitListener = InitListener { code ->
        if (code != ErrorCode.SUCCESS) Timber.e("讯飞初始化失败，代码%d",code)
    }
    val mRecognizerListener = object:RecognizerListener{
        override fun onVolumeChanged(volume: Int, data: ByteArray) {
            val stringBuilder = StringBuilder(volume+3)
            for (i in 0..volume+3)stringBuilder.append('=')
            volumeState.value = stringBuilder.toString()
        }

        override fun onBeginOfSpeech() {
            recognizeResult.value = WAKE_TEXT
        }

        override fun onEndOfSpeech() {
            volumeState.value = ""
        }

        override fun onResult(results: RecognizerResult, isLast: Boolean) {
            val text: String = parseIatResult(results.resultString)
            var sn = ""
            var pgs = ""
            var rg = ""
            // 读取json结果中的sn字段
            try {
                val resultJson = JSONObject(results.resultString)
                sn = resultJson.optString("sn")
                pgs = resultJson.optString("pgs")
                rg = resultJson.optString("rg")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            //如果pgs是rpl就在已有的结果中删除掉要覆盖的sn部分
            if (pgs == "rpl") {
                val strings = rg.replace("[", "").replace("]", "").split(",".toRegex()).toTypedArray()
                val begin = strings[0].toInt()
                val end = strings[1].toInt()
                for (i in begin..end) mIatResults.remove(i.toString() + "")
            }
            mIatResults[sn] = text
            for (key in mIatResults.keys) resultBuilder.append(mIatResults[key])
            recognizeResult.value = resultBuilder.toString()
            if(isLast && selectedChatBot.value!="小笨") {
                val result = resultBuilder.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    if(routeMirror == Router.chat) nextChat(result)
                }
                mIatResults.clear()
                wakeMode()
            }
            resultBuilder.clear()
        }

        override fun onError(error: SpeechError?) {
            error?.let { Timber.e("讯飞识别失败，代码%d",error.errorCode)}
        }

        override fun onEvent(eventType: Int, arg1: Int, arg2: Int, obj: Bundle?) { }

    }
    val mWakeuperListener = object:WakeuperListener{
        override fun onBeginOfSpeech() {
        }

        override fun onResult(results: WakeuperResult) = recognizeMode()

        override fun onError(error: SpeechError?) {
            error?.let { Timber.e("讯飞唤醒失败，代码%d",error.errorCode)}
        }

        override fun onEvent(eventType: Int, arg1: Int, arg2: Int, obj: Bundle?) { }

        override fun onVolumeChanged(volume: Int) { }

    }
    lateinit var mIat : SpeechRecognizer
    lateinit var mIvw : VoiceWakeuper

    fun init(context: Context) {
        mIat = SpeechRecognizer.createRecognizer(context, mInitListener)
        mIvw = VoiceWakeuper.createWakeuper(context, mInitListener)
        // 设置动态修正
        mIat.setParameter("dwa", "wpgs")
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS,"3000")
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS,"2500")
        // 清空参数
        mIvw.setParameter(SpeechConstant.PARAMS, null)
        // 唤醒门限值，根据资源携带的唤醒词个数按照“id:门限;id:门限”的格式传入
        mIvw.setParameter(SpeechConstant.IVW_THRESHOLD, "0:1100")
        // 设置唤醒模式
        mIvw.setParameter(SpeechConstant.IVW_SST, "wakeup")
        // 设置持续进行唤醒
        mIvw.setParameter(SpeechConstant.KEEP_ALIVE, "1")
        // 设置闭环优化网络模式
        mIvw.setParameter(SpeechConstant.IVW_NET_MODE, "0")
        // 设置唤醒资源路径
        mIvw.setParameter(
            SpeechConstant.IVW_RES_PATH,
            ResourceUtil.generateResourcePath(context, RESOURCE_TYPE.assets, "ivw/b9efca3f.jet")
        )
    }

    fun stopAll(){
        recognizeResult.value = UNWAKE_TEXT
        mIat.stopListening()
        mIvw.stopListening()
        TTS.stopSpeaking()
    }

    fun wakeMode(){
        Router.lastTouchTime = stampL
        mIat.stopListening()
        recognizeResult.value = UNWAKE_TEXT
        mIvw.startListening(mWakeuperListener)
    }

    fun recognizeMode(){
        if(routeMirror == Router.chat && voiceInputMode.value) {
            Router.lastTouchTime = stampL
            TTS.stopSpeaking()
            TTS.speak("您请说")
            mIvw.stopListening()
        }
    }

    fun parseIatResult(json: String): String {
        val ret = StringBuffer()
        try {
            val tokenizer = JSONTokener(json)
            val joResult = JSONObject(tokenizer)
            val words = joResult.getJSONArray("ws")
            for (i in 0 until words.length()) {
                // 转写结果词，默认使用第一个结果
                val items = words.getJSONObject(i).getJSONArray("cw")
                val obj = items.getJSONObject(0)
                ret.append(obj.getString("w"))
            }
        } catch (e: Exception) {
            Bugsnag.notify(e)
        }
        return ret.toString()
    }
}