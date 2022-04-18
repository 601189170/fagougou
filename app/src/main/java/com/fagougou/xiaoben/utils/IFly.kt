package com.fagougou.xiaoben.utils

import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.fagougou.xiaoben.CommonApplication.Companion.context
import com.fagougou.xiaoben.utils.Tips.toast
import com.iflytek.cloud.*
import org.json.JSONException
import org.json.JSONObject

object IFly {
    val TAG = javaClass.simpleName
    val result = mutableStateOf("暂无内容")
    val recordingState = mutableStateOf("开始录音")
    val mInitListener = InitListener { code: Int ->
        Log.d(TAG,"SpeechRecognizer init() code = $code")
        if (code != ErrorCode.SUCCESS)
            toast("初始化失败，错误码：$code,请点击网址https://www.xfyun.cn/document/error-code查询解决方案")
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
            result.value = resultBuffer.toString()
        }

        override fun onError(error: SpeechError) {
            recordingState.value = "开始录音"
            Log.d(TAG, "onError " + error.getPlainDescription(true))
            toast(error.getPlainDescription(true))
        }

        override fun onEvent(eventType: Int, arg1: Int, arg2: Int, obj: Bundle?) { }

    }
    val mIat = SpeechRecognizer.createRecognizer(context, mInitListener)
    fun setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null)
        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD)
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json")
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn")
        // 设置语言区域
        val lag = "mandarin"
        mIat.setParameter(SpeechConstant.ACCENT, lag)
        Log.e(TAG, "last language:" + mIat.getParameter(SpeechConstant.LANGUAGE))

        //此处用于设置dialog中不显示错误码信息
        //mIat.setParameter("view_tips_plain","false");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS,"4000")

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS,"1000")

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT,"1")

        // 设置音频保存路径，保存音频格式支持pcm、wav.
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav")
        val dir = context.getExternalFilesDir("msc")?.absolutePath ?: return
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, "$dir/iat.wav")
    }
}