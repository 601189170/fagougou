package com.fagougou.government.utils

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.fagougou.government.utils.IFly.TAG
import com.fagougou.government.utils.IFly.mIat
import com.fagougou.government.utils.IFly.mRecognizerListener
import com.fagougou.government.utils.Tips.toast
import com.iflytek.cloud.*
import kotlinx.coroutines.*
import java.util.*

object TTS {
    val TTSQueue = ArrayDeque<String>()
    var SpeakingProcess = 100
    var lastWord = ""
    private val mTtsInitListener = InitListener { code ->
        Log.d( TAG,"InitListener init() code = $code")
        if (code != ErrorCode.SUCCESS) toast("初始化失败,错误码：$code")
    }
    // 初始化合成对象
    lateinit var mTts : SpeechSynthesizer
    private val mTtsListener: SynthesizerListener = object : SynthesizerListener {
        override fun onSpeakBegin() {
            Log.d(TAG, "开始播放：" + System.currentTimeMillis())
        }

        override fun onSpeakPaused() {
            toast("暂停播放")
        }

        override fun onSpeakResumed() {
        }

        override fun onBufferProgress(percent: Int, beginPos: Int, endPos: Int, info: String) {
            // 合成进度
        }

        override fun onSpeakProgress(percent: Int, beginPos: Int, endPos: Int) {
            // 播放进度
            if(percent%20 == 1)Log.d(TAG, "SpeakProgress：$percent")
            SpeakingProcess = percent
        }

        override fun onCompleted(error: SpeechError?) {
            Log.d(TAG, "Speak Complete")
            SpeakingProcess = 100
        }

        override fun onEvent(eventType: Int, arg1: Int, arg2: Int, obj: Bundle?) {
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
        mTts.setParameter(SpeechConstant.SPEED,"100")
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH,"50")
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME,"50")
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE,"3")
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true")
    }

    fun init(context: Context) {
        mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener)
        // 设置参数
        setParam()
        CoroutineScope(Dispatchers.Default).launch{
            while (true){
                delay(100)
                if (TTSQueue.isNotEmpty() && SpeakingProcess==100){
                    SpeakingProcess=0
                    val text = TTSQueue.pop()
                    delay(50)
                    mTts.startSpeaking(text, mTtsListener)
                    Log.d(TAG, "Speak $text")
                    lastWord = text
                }else if(SpeakingProcess==100 && lastWord=="您请说"){
                    lastWord = ""
                    mIat.startListening(mRecognizerListener)
                }
            }
        }
    }

    fun stopSpeaking(){
        mTts.stopSpeaking()
        TTSQueue.clear()
        SpeakingProcess = 100
    }

    fun speak(text:String) {
        val regex = Regex("[a-zA-Z]")
        TTSQueue.add(text.replace(regex,""))
    }

}