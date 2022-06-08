package com.fagougou.government.registerPage

import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import com.fagougou.government.CommonApplication
import com.fagougou.government.Router
import com.fagougou.government.model.SerialRegisterRequest
import com.fagougou.government.model.SerialRegisterResponse
import com.fagougou.government.repo.Client
import com.fagougou.government.utils.Tips.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

object RegisterViewModel {
    val registerCode = mutableStateOf("")
    val registerAction = mutableStateOf("立即绑定")
    var registerBalance = mutableStateOf(-1)
    fun login(navController: NavController){
        registerAction.value = "绑定中..."
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = Client.mainRegister.register(SerialRegisterRequest(CommonApplication.serial,registerCode.value)).execute()
                val body = response.body() ?: SerialRegisterResponse()
                registerBalance.value = body.balance
                if (body.balance<0){
                    toast(body.errorMessage)
                    return@launch
                }
                registerCode.value = ""
                withContext(Dispatchers.Main) {
                    navController.navigate(Router.registerResult)
                }
            } catch (e: Exception){
                Client.handleException(e)
            }finally {
                registerAction.value = "立即绑定"
            }
        }
    }
}