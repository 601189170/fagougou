package com.fagougou.xiaoben.utils

import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.fagougou.xiaoben.CommonApplication.Companion.context
import com.fagougou.xiaoben.utils.Tips.toast
import com.iflytek.cloud.*
import com.iflytek.cloud.util.ResourceUtil
import com.iflytek.cloud.util.ResourceUtil.RESOURCE_TYPE
import org.json.JSONException
import org.json.JSONObject

object IFly {
    val TAG = javaClass.simpleName
    val recognizeResult = mutableStateOf("暂无内容")
    val wakeUpResult = mutableStateOf("未唤醒")
    val recordingState = mutableStateOf("开始录音")
    val mInitListener = InitListener { code ->
        Log.d(TAG,"SpeechRecognizer init() code = $code")
        if (code != ErrorCode.SUCCESS)
            toast("初始化失败，错误码：$code")
    }
    var mIatResults = mutableMapOf<String, String>()
    val mRecognizerListener = object:RecognizerListener{
        override fun onVolumeChanged(p0: Int, p1: ByteArray) { }

        override fun onBeginOfSpeech() {
            recordingState.value = "录音中"
            toast("开始录音")
        }

        override fun onEndOfSpeech() {
            recordingState.value = "开始录音"
            toast("录音结束")
            mIat.stopListening()
            mIvw.startListening(mWakeuperListener)
        }

        override fun onResult(results: RecognizerResult, isLast: Boolean) {
            Log.d(TAG, results.resultString)
            val text: String = IFlyJsonParser.parseIatResult(results.resultString)
            var sn = ""
            // 读取json结果中的sn字段
            // 读取json结果中的sn字段
            try {
                val resultJson = JSONObject(results.resultString)
                sn = resultJson.optString("sn")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            mIatResults[sn] = text

            val resultBuffer = StringBuffer()
            for (key in mIatResults.keys) {
                resultBuffer.append(mIatResults[key])
            }
            recognizeResult.value = resultBuffer.toString()
        }

        override fun onError(error: SpeechError) {
            recordingState.value = "开始录音"
            Log.d(TAG, "onError " + error.getPlainDescription(true))
            toast(error.getPlainDescription(true))
        }

        override fun onEvent(eventType: Int, arg1: Int, arg2: Int, obj: Bundle?) { }

    }
    val mWakeuperListener = object:WakeuperListener{
        override fun onBeginOfSpeech() {
            wakeUpResult.value = "输入音频"
        }

        override fun onResult(results: WakeuperResult) {
            Log.d(TAG, "唤醒成功 ")
            wakeUpResult.value = "唤醒成功"
            mIvw.stopListening()
            startRecognize()
        }

        override fun onError(error: SpeechError) {
            Log.d(TAG, "onError " + error.getPlainDescription(true))
            toast(error.getPlainDescription(true))
        }

        override fun onEvent(eventType: Int, arg1: Int, arg2: Int, obj: Bundle?) { }

        override fun onVolumeChanged(volume: Int) { }

    }
    val mIat = SpeechRecognizer.createRecognizer(context, mInitListener)
    val mIvw = VoiceWakeuper.createWakeuper(context, mInitListener)
    fun startRecognize() {
        if (recordingState.value!="开始录音")return
        //setParam()
        val ret = mIat.startListening(mRecognizerListener)
        if (ret != ErrorCode.SUCCESS) toast("听写失败,错误码：$ret")
    }
    fun startWakeUp(){
        // 清空参数
        mIvw.setParameter(SpeechConstant.PARAMS, null)
        // 唤醒门限值，根据资源携带的唤醒词个数按照“id:门限;id:门限”的格式传入
        mIvw.setParameter(SpeechConstant.IVW_THRESHOLD, "0:1450")
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
        val path = context.getExternalFilesDir("msc")?.absolutePath ?: return
        mIvw.setParameter(
            SpeechConstant.IVW_AUDIO_PATH,
            "$path/ivw.wav"
        )
        mIvw.setParameter(SpeechConstant.AUDIO_FORMAT, "wav")
        // 如有需要，设置 NOTIFY_RECORD_DATA 以实时通过 onEvent 返回录音音频流字节
        //mIvw.setParameter( SpeechConstant.NOTIFY_RECORD_DATA, "1" );
        // 启动唤醒
        /*	mIvw.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");*/
        val ret = mIvw.startListening(mWakeuperListener)
        if (ret != ErrorCode.SUCCESS) toast("听写失败,错误码：$ret")
    }
}