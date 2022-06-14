package com.fagougou.government.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fagougou.government.databinding.ItemBannerBinding

class BannerAdapter : RecyclerView.Adapter<BannerHolder>() {

    private val defaultUrl = "https://advertise-1251511189.cos.ap-nanjing.myqcloud.com/%E8%BD%AE%E6%92%AD1.png"
    val imageList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerHolder {
        val inflater = ItemBannerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BannerHolder(inflater)
    }

    override fun onBindViewHolder(holder: BannerHolder, position: Int) {
        if(imageList.isEmpty()) holder.setImage(defaultUrl)
        else holder.setImage(imageList[position % imageList.size])
    }

    override fun getItemCount() = Int.MAX_VALUE

}

class BannerHolder(private val binding: ItemBannerBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setImage(url: String){
        binding.image.load(url)
    }
}