package com.fagougou.xiaoben

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fagougou.xiaoben.ui.theme.XiaoBenTheme
import com.google.accompanist.pager.ExperimentalPagerApi

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            XiaoBenTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) { Home() }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Home() {
    val navController = rememberNavController()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.fillMaxHeight(0.5f)
        ) {
            composable("home") { HomePage(navController) }
            composable("friendslist") { Text("friendslist") }
        }
        Text(text = "智能法律计算器", fontSize = 36.sp, modifier = Modifier.padding(horizontal = 36.dp))
        IFlyUi()
    }
}