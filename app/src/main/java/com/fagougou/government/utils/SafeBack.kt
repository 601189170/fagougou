package com.fagougou.government.utils

import androidx.navigation.NavController

object SafeBack{
    var lastBackTime = 0L
    fun NavController.safeBack(){
        val deltaTime = System.currentTimeMillis() - lastBackTime
        if (deltaTime>800) {
            lastBackTime = System.currentTimeMillis()
            this.popBackStack()
        }
    }
}