package com.fagougou.government.consult


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.eseid.sdtapi.*
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.component.QrCodeViewModel
import com.fagougou.government.databinding.ActivityReadCardMsgBinding
import com.fagougou.government.utils.ImSdkUtils
import com.fagougou.government.utils.MessageCheckUtils
import com.fagougou.government.utils.Time
import com.fagougou.government.utils.Tips.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class TouristsLoginActivity : BaseActivity() {
    lateinit var binding: ActivityReadCardMsgBinding
    lateinit var wechatDialog : WechatDialog
    val esSdt = EsSdtSdk.getInst()
    var logCB = EsLogCB { _, msg -> onLog(msg) }
    var mFindTime: Long = 0
    var mSuccessTime: Long = 0
    val sdtStatusCB = EsSdtStatusCB { type ->
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

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadCardMsgBinding.inflate(layoutInflater)
        wechatDialog = WechatDialog(this)
        setContentView(binding.root)
        initView()
        esSdt.SetReadDelay(1)
    }

    override fun onResume() {
        super.onResume()
        esSdt.Start(this, sdtCB, logCB, sdtStatusCB)
    }

    fun initView() {
        binding.edName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                Router.lastTouchTime = Time.stampL
                isPost()
                binding.tipsName.visibility =
                    if (MessageCheckUtils.isLegalName(editable.toString())) View.GONE else View.VISIBLE
            }
        })
        binding.edPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                Router.lastTouchTime = Time.stampL
                isPost()
                binding.tipsPhone.visibility =
                    if (MessageCheckUtils.checkPhone(editable.toString())) View.GONE else View.VISIBLE
            }
        })
        binding.edCard.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                Router.lastTouchTime = Time.stampL
                isPost()
                binding.tipsCard.visibility =
                    if (MessageCheckUtils.isLegalPattern(editable.toString())) View.GONE else View.VISIBLE
            }
        })

        binding.fgMan.setOnClickListener { setSelectSexBg("男") }
        binding.fgWoman.setOnClickListener { setSelectSexBg("女") }
        binding.btnPost.setOnClickListener { PostMsg() }
        binding.topLayout.tvBack.setOnClickListener { finish() }
        binding.topLayout.tvWechat.setOnClickListener {
            QrCodeViewModel.clear()
            QrCodeViewModel.content.value = QrCodeViewModel.constWechatUrl
            wechatDialog.show()
        }
        binding.topLayout.tvZn.setOnClickListener { finish() }
    }

    fun isPost() {
        val name = binding.edName.text.toString().trim();
        val phone = binding.edPhone.text.toString().trim()
        val cardNo = binding.edCard.text.toString().trim()
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(cardNo)) {
            binding.btnPost.background = resources.getDrawable(R.drawable.tourists_btn_bg_nomal)
        }
        if (
            MessageCheckUtils.isLegalName(name) &&
            MessageCheckUtils.checkPhone(phone) &&
            MessageCheckUtils.isLegalPattern(cardNo)
        ) binding.btnPost.background = resources.getDrawable(R.drawable.tourists_btn_bg_true)
        else binding.btnPost.background = resources.getDrawable(R.drawable.tourists_btn_bg_nomal)
    }

    fun PostMsg() {
        val name = binding.edName.text.toString().trim();
        val phone = binding.edPhone.text.toString().trim()
        val cardNo = binding.edCard.text.toString().trim()
        when {
            name.isBlank() || phone.isBlank() || cardNo.isBlank() -> toast("请填完信息")
            !MessageCheckUtils.isLegalName(name) -> toast("请输入正确的名字")
            !MessageCheckUtils.checkPhone(phone) -> toast("请输入正确的手机号")
            !MessageCheckUtils.isLegalPattern(cardNo) -> toast("请输入正确的身份证")
            else -> {
                ImSdkUtils.userName = name
                ImSdkUtils.userId = phone
                val intent = Intent(this, ChooseDomainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    fun setSelectSexBg(sex: String) {
        binding.fgMan.background = resources.getDrawable(R.drawable.tourists_edit_bg_nomal, null)
        binding.fgWoman.background = resources.getDrawable(R.drawable.tourists_edit_bg_nomal, null)
        binding.imgMan.background = resources.getDrawable(R.drawable.ic_icon_man, null)
        binding.imgWoman.background = resources.getDrawable(R.drawable.ic_icon_woman, null)
        binding.tvMan.setTextColor(resources.getColor(R.color.black303, null))
        binding.tvWoman.setTextColor(resources.getColor(R.color.black303, null))
        binding.imgMarkMan.visibility = View.GONE
        binding.imgMarkWoman.visibility = View.GONE
        when (sex) {
            "男" -> {
                binding.imgMarkMan.visibility = View.VISIBLE
                binding.fgMan.background = resources.getDrawable(R.drawable.tourists_edit_bg_true, null)
                binding.imgMan.background = resources.getDrawable(R.drawable.ic_icon_man2, null)
                binding.tvMan.setTextColor(resources.getColor(R.color.blue007, null))
            }
            else -> {
                binding.imgMarkWoman.visibility = View.VISIBLE
                binding.fgWoman.background = resources.getDrawable(R.drawable.tourists_edit_bg_true, null)
                binding.imgWoman.background = resources.getDrawable(R.drawable.ic_icon_woman2, null)
                binding.tvWoman.setTextColor(resources.getColor(R.color.blue007, null))
            }
        }
    }

    override fun onPause() {
        super.onPause()
        esSdt.Stop()
    }

    // ---------------------------------------------------------------------------------------------
    // 读证
    // ---------------------------------------------------------------------------------------------
    var sdtCB = EsSdtCB { type, msg ->
        when (type) {
            SdtCode.SDT_NOTIFY_SAMID -> {
                onLog("设备序列号: $msg")
            }
            SdtCode.SDT_NOTIFY_FIND -> {
                onLog("发现证件")
                mFindTime = System.currentTimeMillis()
            }
            SdtCode.SDT_NOTIFY_SUCCESS -> {
                val eidInfo = SdtInfo(msg)
                val info = eidInfo.info
                onLog("证件读取成功")
                mSuccessTime = System.currentTimeMillis()
                lifecycleScope.launch(Dispatchers.Main) {
                    binding.edName.setText(info.getString("name"))
                    setSelectSexBg(info.getString("sex"))
                    binding.edCard.setText(info.getString("idnum"))
                }
            }
            SdtCode.SDT_NOTIFY_ERROR -> onLog("读证失败")
        }
    }

    @SuppressLint("SetTextI18n")
    fun onLog(msg: String) {
        Timber.d(msg)
    }
}