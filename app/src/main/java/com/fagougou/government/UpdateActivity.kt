package com.fagougou.government

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.fagougou.government.utils.Tips.toast
import com.king.app.dialog.AppDialog
import com.king.app.updater.AppUpdater
import com.king.app.updater.UpdateConfig
import com.king.app.updater.callback.UpdateCallback
import com.king.app.updater.http.OkHttpManager
import java.io.File

class UpdateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        update()
    }
    lateinit var tvProgress: TextView
    lateinit var progressBar: ProgressBar
    private fun updateProgress(progress: Long, total: Long) {
        if (progress > 0) {
            val currProgress = (progress * 1.0f / total * 100.0f).toInt()
            tvProgress.text ="正在下载$currProgress%"
            progressBar.progress = currProgress
        } else tvProgress.text = "正在获取下载数据"
    }
    private fun update() {
        val config = UpdateConfig()
        config.url = intent.getStringExtra("downloadUrl")
        config.addHeader("token", "xxxxxx")
        val mAppUpdater = AppUpdater(this, config)
            .setHttpManager(OkHttpManager.getInstance())
            .setUpdateCallback(object : UpdateCallback {
                override fun onDownloading(isDownloading: Boolean) {
                    if (isDownloading) toast("已经在下载中,请勿重复下载。")
                    else {
                        val view: View = LayoutInflater.from(this@UpdateActivity).inflate(R.layout.dialog_progress, null)
                        tvProgress = view.findViewById(R.id.tvProgress)
                        progressBar = view.findViewById(R.id.progressBar)
                        AppDialog.INSTANCE.showDialog(this@UpdateActivity, view, false)
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
        mAppUpdater.start()
    }
}