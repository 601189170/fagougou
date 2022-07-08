package com.fagougou.government.contractReviewPage


import android.os.Bundle
import android.util.Log
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
import com.fagougou.government.utils.CamareUtils
import com.fagougou.government.utils.Printer
import com.j256.ormlite.stmt.query.In
import java.io.File


class PaperUploadActivity : AppCompatActivity() {

    lateinit var binding: ActivityUploadLayoutBinding
    var file:String?=null
    val showCamera=1
    val showScan=2
    val showPreview=3
    val showWebview=4
    val filelist = mutableListOf<String>()
    var imgAdapter=ImgAdapter()
    var url="https://www.baidu.com/"
    var type=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CamareUtils.initCamera(this,binding.cameraLayout.camera)
        initView()
        setWorkStatus(showCamera)
        setPhotoType(0)
        showMoreLayout(false)
    }
    fun setPhotoType(i:Int){
        when(i){
            0-> {
                type=0
                binding.cameraLayout.one.isChecked=true
                binding.cameraLayout.more.isChecked=false
            }
            1-> {
                type=1
                binding.cameraLayout.one.isChecked=false
                binding.cameraLayout.more.isChecked=true
            }
        }
    }
    fun showMoreLayout(boolean: Boolean){
        when(boolean){
            false-> {
                binding.cameraLayout.moreRightLayout.visibility=View.GONE
                binding.cameraLayout.moreLeftLayout.visibility=View.GONE
            }
            true-> {
                binding.cameraLayout.moreRightLayout.visibility=View.VISIBLE
                binding.cameraLayout.moreLeftLayout.visibility=View.VISIBLE
            }
        }
    }
    fun initView(){
        binding.cameraLayout.one.setOnClickListener { setPhotoType(0) }
        binding.cameraLayout.more.setOnClickListener { setPhotoType(1) }
        binding.cameraLayout.moreRightLayout.setOnClickListener {  }
        binding.cameraLayout.moreLeftLayout.setOnClickListener { setWorkStatus(showPreview) }
        //showCamera
        binding.close.setOnClickListener { finish() }
        binding.cameraLayout.goScan.setOnClickListener {
            file=CamareUtils.takePhoto(this)
            file?.let { it1 -> filelist.add(it1) }
            if (type==0)setWorkStatus(showPreview) else showMoreLayout(true)

        }

        //showScan
        binding.scanLayout.img.rotation=270f
        binding.scanLayout.btnCancel.setOnClickListener { finish() }
        binding.scanLayout.btnReScan.setOnClickListener {
            file?.let { it1 -> filelist.remove(it1)
                    FileUtils.delete(it1) }
            setWorkStatus(showCamera)
        }
        binding.scanLayout.btnGoScan.setOnClickListener { setWorkStatus(showCamera) }
        binding.scanLayout.btnScanDone.setOnClickListener { setWorkStatus(showPreview) }

        //showPreview
        binding.previewLayout.viewPager.adapter = imgAdapter
        binding.previewLayout.preGoOnScan.setOnClickListener { setWorkStatus(showCamera) }
        binding.previewLayout.preNext.setOnClickListener {
            //提交合同审查
            setWorkStatus(showWebview)
        }
        //showWebview
        initWebViewSettings(binding.webviewLayout.web)
        binding.webviewLayout.btnCancel.setOnClickListener { finish() }
        binding.webviewLayout.btnPrint.setOnClickListener { Printer.printWebView(binding.webviewLayout.web) }
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
//            showScan->{
//                binding.scanLayout.allLayout.visibility=View.VISIBLE
//                Glide.with(this).load(file).into(binding.scanLayout.img)
//                binding.scanLayout.pageNum.text="当前页预览第("+filelist.size+")页"
//            }
            showPreview->{
                binding.previewLayout.allLayout.visibility=View.VISIBLE
                binding.previewLayout.pageNum.text="当前预览（"+filelist.size+"页)"
                imgAdapter.imageList=filelist
                imgAdapter.notifyDataSetChanged()
            }
//            showWebview->{
//                binding.webviewLayout.allLayout.visibility=View.VISIBLE
//                binding.webviewLayout.web.loadUrl(url)
//            }
        }

    }

    class ImgAdapter : RecyclerView.Adapter<BannerHolder>() {


        var imageList = mutableListOf<String>()

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
        fun setImage(file: String){
            binding.img.rotation=90f
            Glide.with(binding.root.context).load(file).into(binding.img)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
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
    }



}