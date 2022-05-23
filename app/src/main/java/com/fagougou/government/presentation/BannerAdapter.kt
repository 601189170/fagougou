package com.fagougou.government.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fagougou.government.R
import com.fagougou.government.databinding.ItemBannerBinding

class BannerAdapter : RecyclerView.Adapter<BannerHolder>() {
    val imageList = mutableListOf(
        R.drawable.about_bussiness,
        R.drawable.about_customer_service,
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerHolder {
        val inflater = ItemBannerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BannerHolder(inflater)
    }

    override fun onBindViewHolder(holder: BannerHolder, position: Int) {
        val index = position % imageList.size
        val imageId = imageList[index]
        holder.setImage(imageId)
    }

    override fun getItemCount() = Int.MAX_VALUE
}

class BannerHolder(val binding: ItemBannerBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setImage(id:Int){
        binding.image.setImageResource(id)
    }
}