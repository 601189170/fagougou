package com.fagougou.government

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.databinding.ActivityUpdateBinding
import com.fagougou.government.databinding.DialogProgressBinding
import com.fagougou.government.utils.Tips.toast
import com.king.app.dialog.AppDialog
import com.king.app.updater.AppUpdater
import com.king.app.updater.UpdateConfig
import com.king.app.updater.callback.UpdateCallback
import com.king.app.updater.http.OkHttpManager
import java.io.File

class UpdateActivity : AppCompatActivity() {
    lateinit var binding:ActivityUpdateBinding
    lateinit var dialogBinding:DialogProgressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        update()
    }

    private fun updateProgress(progress: Long, total: Long) {
        if (progress > 0) {
            val currProgress = (progress * 1.0f / total * 100.0f).toInt()
            dialogBinding.tvProgress.text ="正在下载$currProgress%"
            dialogBinding.progressBar.progress = currProgress
        } else dialogBinding.tvProgress.text = "正在获取下载数据"
    }

    private fun update() {
        val config = UpdateConfig()
        config.url = intent.getStringExtra("downloadUrl")
        config.addHeader("token", "xxxxxx")
        val appUpdater = AppUpdater(this, config)
            .setHttpManager(OkHttpManager.getInstance())
            .setUpdateCallback(object : UpdateCallback {
                override fun onDownloading(isDownloading: Boolean) {
                    if (isDownloading) toast("已经在下载中,请勿重复下载。")
                    else {
                        dialogBinding = DialogProgressBinding.inflate(layoutInflater)
                        AppDialog.INSTANCE.showDialog(this@UpdateActivity, dialogBinding.root, false)
                    }
                }

                override fun onStart(url: String) {
                    updateProgress(0, 100)
                }

                override fun onProgress(progress: Long, total: Long, isChange: Boolean) {
                    if (isChange) updateProgress(progress, total)
                }

                override fun onFinish(file: File) {
                    AppDialog.INSTANCE.dismissDialog()
                    toast("下载完成")
                }

                override fun onError(e: Exception) {
                    AppDialog.INSTANCE.dismissDialog()
                    toast("下载失败")
                }

                override fun onCancel() {
                    AppDialog.INSTANCE.dismissDialog()
                    toast("取消下载")
                }
            }
        )
        appUpdater.start()
    }
}