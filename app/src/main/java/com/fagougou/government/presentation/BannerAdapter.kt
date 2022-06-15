package com.fagougou.government.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fagougou.government.databinding.ItemBannerBinding

class BannerAdapter : RecyclerView.Adapter<BannerHolder>() {

    val imageList = mutableListOf("https://img.3dmgame.com/uploads/images/news/20210713/1626165488_100567.jpg")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerHolder {
        val inflater = ItemBannerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BannerHolder(inflater)
    }

    override fun onBindViewHolder(holder: BannerHolder, position: Int) {
        val index = position % imageList.size
        holder.setImage(imageList[index])
    }

    override fun getItemCount() = Int.MAX_VALUE
}

class BannerHolder(val binding: ItemBannerBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setImage(url: String){
        binding.image.load(url)
    }
}