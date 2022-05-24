package com.fagougou.government.presentation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.R
import com.fagougou.government.databinding.ItemBannerBinding

class BannerAdapter : RecyclerView.Adapter<BannerHolder>() {
    val imageList = listOf(
        R.drawable.banner_1,
        R.drawable.banner_2,
        R.drawable.banner_3,
        R.drawable.banner_4,
        R.drawable.banner_5,
        R.drawable.banner_6,
        R.drawable.banner_7,
        R.drawable.banner_8,
        R.drawable.banner_9,
        R.drawable.banner_10,
    )

    val bitmapList = imageList.map { BitmapFactory.decodeResource(activity.resources,it) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerHolder {
        val inflater = ItemBannerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BannerHolder(inflater)
    }

    override fun onBindViewHolder(holder: BannerHolder, position: Int) {
        val index = position % bitmapList.size
        holder.setImage(bitmapList[index])
    }

    override fun getItemCount() = Int.MAX_VALUE
}

class BannerHolder(val binding: ItemBannerBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setImage(bitmap: Bitmap){
        binding.image.setImageBitmap(bitmap)
    }
}