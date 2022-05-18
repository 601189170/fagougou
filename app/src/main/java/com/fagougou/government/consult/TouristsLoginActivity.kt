package com.fagougou.xiaoben.consult


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import com.eseid.sdtapi.*
import com.fagougou.government.R
import com.fagougou.government.databinding.ActivityReadCardMsgBinding
import com.fagougou.government.utils.ImSdkUtils
import com.fagougou.government.utils.MMKV.kv
import com.fagougou.government.utils.MessageCheckUtils
import com.fagougou.government.utils.Time
import com.fagougou.government.utils.Wechat
import com.fagougou.government.utils.Wechat.showQrCode
import org.json.JSONException
import java.text.SimpleDateFormat
import java.util.*

class TouristsLoginActivity : AppCompatActivity() {
    var sdk: EsSdtSdk? = null
    var isStart = false
    var sex=""

    private var binding: ActivityReadCardMsgBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityReadCardMsgBinding.inflate(getLayoutInflater());
        if (binding!=null){
            val rootView: View = binding!!.root
            setContentView(rootView)
        }

        Time.hideSystemUI()
        //初始化读卡
        sdk = EsSdtSdk.getInst()
        onBtnStart()
        initView();
    }
    fun initView(){
        setSelectSexBg(0)
        binding!!.edName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                isPost();
            }
        })
        binding!!.edPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                isPost();
            }
        })
        binding!!.edCard.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                isPost();
            }
        })

        binding!!.fgMan.setOnClickListener { setSelectSexBg(0) }
        binding!!.fgWoman.setOnClickListener { setSelectSexBg(1) }
        binding!!.btnPost.setOnClickListener {
            PostMsg();
        }

        binding!!.topLayout.tvBack.setOnClickListener { finish() }
        binding!!.topLayout.tvWechat.setOnClickListener {showQrCode.value = true }
    }
    fun isPost(){
        var  name=binding!!.edName.text.toString().trim();
        var  phone=binding!!.edPhone.text.toString().trim()
        var  cardNo=binding!!.edCard.text.toString().trim()
        if (TextUtils.isEmpty(name)||TextUtils.isEmpty(phone)||TextUtils.isEmpty(cardNo)){
            binding!!.btnPost.background=this.resources.getDrawable(R.drawable.tourists_btn_bg_nomal)
        }else{
            binding!!.btnPost.background=this.resources.getDrawable(R.drawable.tourists_btn_bg_true)
        }

    }
    fun PostMsg() {
       var  name=binding!!.edName.text.toString().trim();
       var  phone=binding!!.edPhone.text.toString().trim()
       var  cardNo=binding!!.edCard.text.toString().trim()

        if (TextUtils.isEmpty(name)||TextUtils.isEmpty(phone)||TextUtils.isEmpty(cardNo)){
            ToastUtils.showShort("请填完信息")
            return
        }
        if (!MessageCheckUtils.isLegalName(name)){
            ToastUtils.showShort("请输入正确的名字")
            return
        }
        if (!MessageCheckUtils.checkPhone(phone)){
            ToastUtils.showShort("请输入正确的手机号")
            return
        }
        if (!MessageCheckUtils.isLegalPattern(cardNo)){
            ToastUtils.showShort("请输入正确的身份证")
            return
        }
        ImSdkUtils.userName=name;
        ImSdkUtils.userId=phone;
        val intent = Intent(this, ChooseDomainActivity::class.java)
        startActivity(intent)
        finish()

    }

    fun setSelectSexBg(type: Int){
        binding!!.fgMan.background=this.resources.getDrawable(R.drawable.tourists_edit_bg_nomal)
        binding!!.fgWoman.background=this.resources.getDrawable(R.drawable.tourists_edit_bg_nomal)
        binding!!.imgMan.background=this.resources.getDrawable(R.drawable.ic_icon_man)
        binding!!.imgWoman.background=this.resources.getDrawable(R.drawable.ic_icon_woman)
        binding!!.tvMan.setTextColor(this.resources.getColor(R.color.black303))
        binding!!.tvWoman.setTextColor(this.resources.getColor(R.color.black303))
        binding!!.imgMarkMan.visibility=View.GONE
        binding!!.imgMarkWoman.visibility=View.GONE
        if (type==0){
            sex="0"
            binding!!.imgMarkMan.visibility=View.VISIBLE
            binding!!.fgMan.background=this.resources.getDrawable(R.drawable.tourists_edit_bg_true)
            binding!!.imgMan.background=this.resources.getDrawable(R.drawable.ic_icon_man2)
            binding!!.tvMan.setTextColor(this.resources.getColor(R.color.blue007))
        }else{
            sex="1"
            binding!!.imgMarkWoman.visibility=View.VISIBLE
            binding!!.fgWoman.background=this.resources.getDrawable(R.drawable.tourists_edit_bg_true)
            binding!!.imgWoman.background=this.resources.getDrawable(R.drawable.ic_icon_woman2)
            binding!!.tvWoman.setTextColor(this.resources.getColor(R.color.blue007))
        }

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
//            Log.e("TAG", "onBtnStart: " )
            return
        }
        sdk!!.SetReadDelay(1)
        if (sdk!!.Start(this, sdtCB, logCB, sdtStatusCB)) {
            isStart = true
        }
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

                        Log.e("读卡信息", ": "+"姓名：" + info.getString("name" ))
                        Log.e("读卡信息", ": "+ "性别：" + info.getString("sex"))
                        binding!!.edName.setText(info.getString("name" ))
                        if (info.getString("sex").equals("男")){
                            setSelectSexBg(0)
                        }else{
                            setSelectSexBg(1)
                        }
                        binding!!.edCard.setText(info.getString("idnum" ))
//                        binding!!.edPhone.setText(info.getString("idnum" ))

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