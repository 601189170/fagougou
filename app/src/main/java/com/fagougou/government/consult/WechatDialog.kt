package com.fagougou.government.consult

import android.content.Context
import android.os.Bundle
import cn.udesk.eventBus.BaseDialog
import com.fagougou.government.R
import com.fagougou.government.component.QrCodeViewModel
import com.fagougou.government.databinding.WechatLayoutBinding



class WechatDialog(context: Context) : BaseDialog(context, R.style.shareDialog) {
    lateinit var binding : WechatLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WechatLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.code.setImageBitmap(QrCodeViewModel.bitmap())
        binding.tvHint.text = QrCodeViewModel.hint
        binding.close.setOnClickListener{
            QrCodeViewModel.clear()
            cancel()
        }
    }
}