package com.fagougou.xiaoben.consult

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.eseid.sdtapi.*
import com.fagougou.government.R
import com.fagougou.government.utils.MMKV.kv



import org.json.JSONException
import java.text.SimpleDateFormat
import java.util.*

class TouristsLoginActivity : Activity() {
    var sdk: EsSdtSdk? = null
    var isStart = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_card_msg)
        //初始化读卡
        sdk = EsSdtSdk.getInst()
        onBtnStart()
    }
    override fun onDestroy() {
        super.onDestroy()
        sdk!!.Stop()
    }
    fun saveUser(TouristName:String,TouristPhone:String){
        kv.encode("TouristName",TouristName)
        kv.encode("TouristPhone",TouristPhone)
    }

    fun onBtnStart() {
//        btStart.setEnabled(false)
        if (isStart) {
            isStart = false
            sdk!!.Stop()
//            btStart.setText("开始读证")
//            btStart.setEnabled(true)
            return
        }
        sdk!!.SetReadDelay(1)
        if (sdk!!.Start(this, sdtCB, logCB, sdtStatusCB)) {
            isStart = true
//            btStart.setText("停止读证")
        }
//        btStart.setEnabled(true)
    }

    // ---------------------------------------------------------------------------------------------
    // 读证
    // ---------------------------------------------------------------------------------------------
    var logCB = EsLogCB { level, msg -> onLog(msg) }
    var mFindTime: Long = 0
    var mSuccessTime: Long = 0
    var sdtCB = EsSdtCB { type, msg ->
        when (type) {
            SdtCode.SDT_NOTIFY_SAMID -> {
                onLog("设备序列号: $msg")
            }
            SdtCode.SDT_NOTIFY_FIND -> {
                onLog("发现证件")
//                APP.showLoading(this@MainActivity, "发现证件")
                mFindTime = System.currentTimeMillis()
            }
            SdtCode.SDT_NOTIFY_SUCCESS -> {
                val eidInfo = SdtInfo(msg)
                val info = eidInfo.info
                val img = eidInfo.img
                onLog("证件读取成功")
                mSuccessTime = System.currentTimeMillis()
                Handler(Looper.getMainLooper()).post {
                    try {
//                        // info.getString("classify"): ESIDCARD_CLASSIFY_IDCARD(实体证件) | ESIDCARD_CLASSIFY_EID(电子标识)
//                        // info.getString("idType"): ESIDCARD_IDTYPE_N(常规身份证) | ESIDCARD_IDTYPE_J(港澳台居民居住证) | ESIDCARD_IDTYPE_I(外国人永久居留证)
//                        (findViewById<View>(R.id.tvName) as TextView).text =
//                            "姓名：" + info.getString("name")
//                        if (info.getString("idType") == SdtCode.SDT_IDTYPE_J) {
//                            // 港澳台居民居住证，显示通行证号码
//                            (findViewById<View>(R.id.tvCard) as TextView).text =
//                                """
//                                      号码：${info.getString("idnum")}
//                                      通行证：
//                                      """.trimIndent() + info.getString(
//                                    "otherIdNum"
//                                )
//                            (findViewById<View>(R.id.tvNation) as TextView).visibility =
//                                View.GONE
//                        } else {
//                            (findViewById<View>(R.id.tvCard) as TextView).text =
//                                "号码：" + info.getString("idnum")
//                            (findViewById<View>(R.id.tvNation) as TextView).text =
//                                "民族：" + info.getString("nation")
//                            (findViewById<View>(R.id.tvNation) as TextView).visibility =
//                                View.VISIBLE
//                        }
//                        (findViewById<View>(R.id.tvAddress) as TextView).text =
//                            "地址：" + info.getString("address")
//                        (findViewById<View>(R.id.tvSigningOrganization) as TextView).text =
//                            "签发机关：" + info.getString("signingOrganization")
//                        (findViewById<View>(R.id.tvBirthDate) as TextView).text =
//                            "出生日期：" + info.getString("birthDate")
//                        (findViewById<View>(R.id.tvSex) as TextView).text =
//                            "性别：" + info.getString("sex")
//                        val bTime = info.getString("beginTime")
//                        val eTime = info.getString("endTime")
//                        (findViewById<View>(R.id.tvBeginTime) as TextView).text =
//                            "有效日期：$bTime - $eTime"
//                        (findViewById<View>(R.id.imageView) as ImageView).setImageBitmap(
//                            img
//                        )
//                        (findViewById<View>(R.id.tvError) as TextView).text =
//                            "读卡成功"
//                        (findViewById<View>(R.id.tvFullTime) as TextView).text =
//                            "读卡时间：" + (mSuccessTime - mFindTime).toString() + "ms"
                        Log.e("读卡信息", ": "+"姓名：" + info.getString("name" ))
                        Log.e("读卡信息", ": "+ "性别：" + info.getString("sex"))
                        if (info.getString("idType") == SdtCode.SDT_IDTYPE_J) {
//                            // 港澳台居民居住证，显示通行证号码
                            Log.e("读卡信息", ": "+ "号码：" + """
                                      号码：${info.getString("idnum")}
                                      通行证：
                                      """.trimIndent() + info.getString(
                                "otherIdNum"
                            ))

                        } else {
                            Log.e("读卡信息", ": "+ "号码：" + info.getString("idnum"))
                            Log.e("读卡信息", ": "+ "民族：" + info.getString("nation"))
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
//                    APP.hideLoading()

                }
            }
            SdtCode.SDT_NOTIFY_ERROR -> {
                onLog("读证失败")
//                APP.hideLoading()
            }
        }
    }
    var sdtStatusCB = EsSdtStatusCB { type ->
        when (type) {
            SdtCode.SDT_ERROR_IO -> onLog("SDT_ERROR_IO")
            SdtCode.SDT_ERROR -> onLog("SDT_ERROR")
            SdtCode.SDT_STOP -> onLog("SDT_STOP")
            SdtCode.SDT_STOPPING -> onLog("SDT_STOPPING")
            SdtCode.SDT_STARTING -> onLog("SDT_STARTING")
            SdtCode.SDT_RUNNING -> onLog("SDT_RUNNING")
            SdtCode.SDT_PAUSE -> onLog("SDT_PAUSE")
        }
    }

    // ---------------------------------------------------------------------------------------------
    // 日志
    // ---------------------------------------------------------------------------------------------
    // 日志Buf
    var buffer = ArrayList<String>()

    private fun getDateTime(): String? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINESE)
        return dateFormat.format(Date(System.currentTimeMillis()))
    }

    @SuppressLint("SetTextI18n")
    fun onLog(msg: String?) {
        val log = String.format("[%s]\n%s\n", getDateTime(), msg)
        buffer.add(0, log)
        if (buffer.size > 50) buffer.removeAt(buffer.size - 1)
        Handler(Looper.getMainLooper()).post {
            val str = StringBuilder()
            for (i in buffer.indices) str.append(buffer[i]).append("\n")

        }
    }
}