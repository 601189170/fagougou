package com.fagougou.government.aboutUsPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.R
import com.fagougou.government.Header

@Composable
fun AboutUs(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        Header(title = "关于我们", navController = navController)
        Column(modifier = Modifier.padding(horizontal = 100.dp)) {
            Image(
                modifier = Modifier.padding(top = 40.dp),
                painter = painterResource(R.drawable.about_robot),
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(top = 32.dp),
                color = Color.White,
                fontSize = 24.sp,
                lineHeight = 24.sp,
                letterSpacing = 1.2f.sp,
                text = "法狗狗智能是法狗狗（深圳）科技有限公司旗下品牌，法狗狗是一家业界领先的AI人工智能企业，在自然语言处理及知识图谱方面拥有核心竞争力。我们致力于通过革命性的技术服务及产品解决方案，帮助政府及企业在信息化及智能化改造过程中获得优质稳定的技术服务支持。"
            )
            Text(
                modifier = Modifier.padding(top = 16.dp),
                color = Color.White,
                fontSize = 24.sp,
                lineHeight = 24.sp,
                letterSpacing = 1.2f.sp,
                text = "公司的使命是通过革命性的技术，为客户提供严谨高效的文本、语音智能交互解决方案。希望通过持续领先的自然语言处理技术，去解决各行业文本与语音交互场景的痛点。"
            )
            Text(
                modifier = Modifier.padding(top = 16.dp),
                color = Color.White,
                fontSize = 24.sp,
                lineHeight = 24.sp,
                letterSpacing = 1.2f.sp,
                text = "公司先后获得松禾资本、蚁米基金数千万的投资，获得20多项国家级技术专利、知识产权、“深圳高新技术企业”、深圳第一届人工智能理事会会员单位、多次创新技术比赛冠军等奖项。为100+ 个省、市、区级政务部门提供AI咨询技术产品支持，与中国最大规模电商之一京东合作打造智能法务产品“法咚咚”，与三大云服务商之一金山云达成战略合作。"
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    modifier = Modifier.width(460.dp).height(236.dp),
                    painter = painterResource(id = R.drawable.about_customer_service),
                    contentDescription = "Customer Services 400-8815-899"
                )
                Image(
                    modifier = Modifier.width(460.dp).height(236.dp),
                    painter = painterResource(id = R.drawable.about_bussiness),
                    contentDescription = "Business yunong@fagougou.com"
                )
            }
        }
    }
}