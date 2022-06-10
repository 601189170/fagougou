package com.fagougou.government.consult

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fagougou.government.R
import com.fagougou.government.databinding.ActivityChooseDomainBinding

class ChooseDomainActivity : BaseActivity() {
    lateinit var binding: ActivityChooseDomainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChooseDomainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initView()
    }
    fun initView(){
        binding.topLayout.tvBack.setOnClickListener { finish() }
        binding.topLayout.tvWechat.setOnClickListener { WechatDialog(this).show() }
        binding.topLayout.tvZn.setOnClickListener { finish() }
        binding.recyclerView.setLayoutManager(GridLayoutManager(this, 5))
        binding.recyclerView.adapter=madapter
        val botResMap = mapOf(
            Pair("公司财税", R.drawable.bot_tax),
            Pair("交通事故", R.drawable.bot_traffic),
            Pair("婚姻家事", R.drawable.bot_marry),
            Pair("员工纠纷", R.drawable.bot_employee),
            Pair("知识产权", R.drawable.bot_knowledge),
            Pair("刑事犯罪", R.drawable.bot_crime),
            Pair("房产纠纷", R.drawable.bot_house),
            Pair("企业人事", R.drawable.bot_employer),
            Pair("民间借贷", R.drawable.bot_loan),
            Pair("消费维权", R.drawable.bot_consumer),
        )
        val botList = botResMap.toList()
        madapter.setList(botList)
        madapter.setOnItemClickListener { adapter, view, position ->
            val intent = Intent(this, WaitActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private val madapter =
        object : BaseQuickAdapter<Pair<String,Int>, BaseViewHolder>(R.layout.layout_select_rule_item),
            LoadMoreModule {
            @SuppressLint("SetTextI18n")
            override fun convert(holder: BaseViewHolder, item: Pair<String,Int>) {
                holder.setImageResource(R.id.img, item.second)
            }
        }

}