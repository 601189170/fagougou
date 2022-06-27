package com.fagougou.government.fileupload


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.FileUtils
import com.bumptech.glide.Glide
import com.fagougou.government.databinding.ActivityUploadLayoutBinding
import com.fagougou.government.databinding.ItemPreviewLayoutBinding
import java.io.File


class PaperUploadActivity : AppCompatActivity() {

    lateinit var binding: ActivityUploadLayoutBinding
    var file:File?=null
    val showCamera=1
    val showScan=2
    val showPreview=3
    val showWebview=4
    val filelist = mutableListOf<File>()
    var imgAdapter=ImgAdapter()
    var url="https://www.baidu.com/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        setWorkStatus(showCamera)
    }

    fun initView(){
        //showCamera
        binding.cameraLayout.close.setOnClickListener { finish() }
        binding.cameraLayout.goScan.setOnClickListener {
            file=binding.cameraLayout.camera.nv21()
            file?.let { it1 -> filelist.add(it1) }
            setWorkStatus(showScan)
        }

        //showScan
        binding.scanLayout.btnCancel.setOnClickListener { finish() }
        binding.scanLayout.btnReScan.setOnClickListener {
            file?.let { it1 -> filelist.remove(it1)
                    FileUtils.delete(it1) }
            setWorkStatus(showCamera)
        }
        binding.scanLayout.btnGoScan.setOnClickListener {
            setWorkStatus(showCamera)
        }
        binding.scanLayout.btnScanDone.setOnClickListener {
            setWorkStatus(showPreview)
        }

        //showPreview
        binding.previewLayout.viewPager.adapter = imgAdapter
        binding.previewLayout.btnCancel.setOnClickListener { finish() }
        binding.previewLayout.btnPost.setOnClickListener {
            //提交合同审查
            setWorkStatus(showWebview)
        }
        //showWebview
        initWebViewSettings(binding.webviewLayout.web)
        binding.webviewLayout.btnCancel.setOnClickListener { finish() }
    }
    fun setWorkStatus(status:Int){
        binding.cameraLayout.allLayout.visibility=View.GONE
        binding.scanLayout.allLayout.visibility=View.GONE
        binding.previewLayout.allLayout.visibility=View.GONE
        binding.webviewLayout.allLayout.visibility=View.GONE
        when(status){
            showCamera->{
                binding.cameraLayout.allLayout.visibility=View.VISIBLE
            }
            showScan->{
                binding.scanLayout.allLayout.visibility=View.VISIBLE
                Glide.with(this).load(file).into(binding.scanLayout.img)
                binding.scanLayout.pageNum.text="当前页预览第("+filelist.size+")页"
            }
            showPreview->{
                binding.previewLayout.allLayout.visibility=View.VISIBLE
                binding.previewLayout.pageNum.text="共"+filelist.size+"页"
                imgAdapter.imageList=filelist
                imgAdapter.notifyDataSetChanged()
            }
            showWebview->{
                binding.webviewLayout.allLayout.visibility=View.VISIBLE
                binding.webviewLayout.web.loadUrl(url)
            }
        }

    }

    class ImgAdapter : RecyclerView.Adapter<BannerHolder>() {


        var imageList = mutableListOf<File>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerHolder {
            val inflater = ItemPreviewLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return BannerHolder(inflater)
        }

        override fun onBindViewHolder(holder: BannerHolder, position: Int) {


             holder.setImage(imageList[position % imageList.size])
        }

        override fun getItemCount() = imageList.size

    }


    class BannerHolder(private val binding: ItemPreviewLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setImage(file: File){
            Glide.with(binding.root.context).load(file).into(binding.image)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.cameraLayout.camera.releaseCamera()
        filelist.forEach { FileUtils.delete(it)}
    }

    private fun initWebViewSettings(webView: WebView) {
        val webSetting: WebSettings = webView.getSettings()
        webSetting.javaScriptEnabled = true
        webSetting.javaScriptCanOpenWindowsAutomatically = true
        webSetting.allowFileAccess = true
        webSetting.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        webSetting.setSupportZoom(true)
        webSetting.builtInZoomControls = true
        webSetting.useWideViewPort = true
        webSetting.setSupportMultipleWindows(true)
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true)
        // webSetting.setDatabaseEnabled(true);
        webSetting.domStorageEnabled = true
        webSetting.setGeolocationEnabled(true)
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE)
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
//		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.cacheMode = WebSettings.LOAD_NO_CACHE

        // this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
        // settings 的设计
    }

}