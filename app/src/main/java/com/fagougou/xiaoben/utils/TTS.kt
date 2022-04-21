package com.fagougou.xiaoben.utils

import android.os.Bundle
import android.util.Log
import com.fagougou.xiaoben.CommonApplication.Companion.context
import com.fagougou.xiaoben.utils.IFly.TAG
import com.fagougou.xiaoben.utils.Tips.toast
import com.iflytek.cloud.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

object TTS {
    val TTSQueue = ArrayDeque<String>()
    var SpeakingProcess = 100
    private val mTtsInitListener = InitListener { code ->
        Log.d( TAG,"InitListener init() code = $code")
        if (code != ErrorCode.SUCCESS) toast("初始化失败,错误码：$code")
    }
    // 初始化合成对象
    val mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener)
    private val mTtsListener: SynthesizerListener = object : SynthesizerListener {
        override fun onSpeakBegin() {
            Log.d(TAG, "开始播放：" + System.currentTimeMillis())
        }

        override fun onSpeakPaused() {
            toast("暂停播放")
        }

        override fun onSpeakResumed() {
            toast("继续播放")
        }

        override fun onBufferProgress(percent: Int, beginPos: Int, endPos: Int, info: String) {
            // 合成进度
        }

        override fun onSpeakProgress(percent: Int, beginPos: Int, endPos: Int) {
            // 播放进度
            Log.d(TAG, "SpeakProgress$percent")
            SpeakingProcess = percent
        }

        override fun onCompleted(error: SpeechError) {
            Log.d(TAG, "com")
            toast(error.getPlainDescription(true))
        }

        override fun onEvent(eventType: Int, arg1: Int, arg2: Int, obj: Bundle) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                val sid = obj.getString(SpeechEvent.KEY_EVENT_AUDIO_URL)
                Log.d(TAG, "session id =$sid")
            }

            //实时音频流输出参考
            /*if (SpeechEvent.EVENT_TTS_BUFFER == eventType) {
				byte[] buf = obj.getByteArray(SpeechEvent.KEY_EVENT_TTS_BUFFER);
				Log.e("MscSpeechLog", "buf is =" + buf);
			}*/
        }
    }

    /**
     * 参数设置
     */
    private fun setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null)
        //设置使用云端引擎
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD)
        //设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME,"xiaoyu")
        //mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY,"1");//支持实时音频流抛出，仅在synthesizeToUri条件下支持
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED,"75")
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH,"50")
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME,"50")
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE,"3")
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true")
    }
    init {
        // 设置参数
        setParam()
        CoroutineScope(Dispatchers.Default).launch{
            while (true){
                delay(200)
                if (TTSQueue.isNotEmpty() && SpeakingProcess>97){
                    SpeakingProcess=0
                    val text = TTSQueue.pop()
                    delay(200)
                    mTts.startSpeaking(text, mTtsListener)
                    Log.d(TAG, "Said $text")
                }
            }
        }
    }
    fun speak(text:String) = TTSQueue.add(text)
}