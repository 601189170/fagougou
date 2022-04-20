package com.fagougou.xiaoben.utils

import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.fagougou.xiaoben.CommonApplication.Companion.context
import com.fagougou.xiaoben.chatPage.ChatPage
import com.fagougou.xiaoben.chatPage.ChatPage.selectedChatBot
import com.fagougou.xiaoben.model.Message
import com.fagougou.xiaoben.model.Speaker
import com.fagougou.xiaoben.utils.Tips.toast
import com.iflytek.cloud.*
import com.iflytek.cloud.util.ResourceUtil
import com.iflytek.cloud.util.ResourceUtil.RESOURCE_TYPE
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.lang.Exception

object IFly {
    val TAG = javaClass.simpleName
    val resultBuilder = StringBuilder()
    val recognizeResult = mutableStateOf("暂无内容")
    val wakeUpResult = mutableStateOf("未唤醒")
    val volumeState = mutableStateOf("=")

    var mIatResults = mutableMapOf<String, String>()
    val mInitListener = InitListener { code ->
        if (code != ErrorCode.SUCCESS) toast("初始化失败，错误码：$code")
    }
    val mRecognizerListener = object:RecognizerListener{
        override fun onVolumeChanged(volume: Int, data: ByteArray) {
            val stringBuilder = StringBuilder(volume+3)
            for (i in 0..volume+3)stringBuilder.append('=')
            volumeState.value = stringBuilder.toString()
        }

        override fun onBeginOfSpeech() {
            wakeUpResult.value = "唤醒成功,录音中"
        }

        override fun onEndOfSpeech() {
            volumeState.value = ""
            wakeMode()
        }

        override fun onResult(results: RecognizerResult, isLast: Boolean) {
            Log.d(TAG, results.resultString)
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
                ChatPage.history.add(Message(Speaker.USER,resultBuilder.toString()))
                mIatResults.clear()
                recognizeResult.value = ""
            }
            resultBuilder.clear()
        }

        override fun onError(error: SpeechError) = toast(error.getPlainDescription(true))

        override fun onEvent(eventType: Int, arg1: Int, arg2: Int, obj: Bundle?) { }

    }
    val mWakeuperListener = object:WakeuperListener{
        override fun onBeginOfSpeech() {
            wakeUpResult.value = "等待唤醒"
        }

        override fun onResult(results: WakeuperResult) = recognizeMode()

        override fun onError(error: SpeechError) {
            Log.d(TAG, "onError " + error.getPlainDescription(true))
            toast(error.getPlainDescription(true))
        }

        override fun onEvent(eventType: Int, arg1: Int, arg2: Int, obj: Bundle?) { }

        override fun onVolumeChanged(volume: Int) { }

    }
    val mIat = SpeechRecognizer.createRecognizer(context, mInitListener)
    val mIvw = VoiceWakeuper.createWakeuper(context, mInitListener)

    init {
        // 设置动态修正
        mIat.setParameter("dwa", "wpgs")
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS,"3000")
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS,"3000")
        // 清空参数
        mIvw.setParameter(SpeechConstant.PARAMS, null)
        // 唤醒门限值，根据资源携带的唤醒词个数按照“id:门限;id:门限”的格式传入
        mIvw.setParameter(SpeechConstant.IVW_THRESHOLD, "0:1300")
        // 设置唤醒模式
        mIvw.setParameter(SpeechConstant.IVW_SST, "wakeup")
        // 设置持续进行唤醒
        mIvw.setParameter(SpeechConstant.KEEP_ALIVE, "1")
        // 设置闭环优化网络模式
        mIvw.setParameter(SpeechConstant.IVW_NET_MODE, "0")
        // 设置唤醒资源路径
        mIvw.setParameter(
            SpeechConstant.IVW_RES_PATH,
            ResourceUtil.generateResourcePath(context, RESOURCE_TYPE.assets, "ivw/33b963d0.jet")
        )
        // 设置唤醒录音保存路径，保存最近一分钟的音频
        val path = context.getExternalFilesDir("msc")?.absolutePath ?: ""
        mIvw.setParameter( SpeechConstant.IVW_AUDIO_PATH, "$path/ivw.wav")
        mIvw.setParameter(SpeechConstant.AUDIO_FORMAT, "wav")
        mIvw.startListening(mWakeuperListener)
    }

    fun wakeMode(){
        mIat.stopListening()
        mIvw.startListening(mWakeuperListener)
    }

    fun recognizeMode(){
        mIvw.stopListening()
        mIat.startListening(mRecognizerListener)
    }

    fun parseIatResult(json: String): String {
        val ret = StringBuffer()
        try {
            val tokener = JSONTokener(json)
            val joResult = JSONObject(tokener)
            val words = joResult.getJSONArray("ws")
            for (i in 0 until words.length()) {
                // 转写结果词，默认使用第一个结果
                val items = words.getJSONObject(i).getJSONArray("cw")
                val obj = items.getJSONObject(0)
                ret.append(obj.getString("w"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ret.toString()
    }
}