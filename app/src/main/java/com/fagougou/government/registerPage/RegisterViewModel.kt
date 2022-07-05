package com.fagougou.government.registerPage

import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import com.fagougou.government.CommonApplication
import com.fagougou.government.Router
import com.fagougou.government.model.BaseViewModel
import com.fagougou.government.model.SerialRegisterRequest
import com.fagougou.government.model.SerialRegisterResponse
import com.fagougou.government.repo.Client
import com.fagougou.government.utils.Tips.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

object RegisterViewModel:BaseViewModel {
    val registerCode = mutableStateOf("")
    val registerAction = mutableStateOf("立即绑定")
    var registerBalance = mutableStateOf(-1)
    fun login(navController: NavController){
        registerAction.value = "绑定中..."
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = SerialRegisterRequest(CommonApplication.serial,registerCode.value)
                Client.mainRegister.register(request).enqueue(
                    Client.callBack {
                        it?.let {
                            registerBalance.value = it.balance
                            if (it.balance<0) toast(it.errorMessage)
                            else{
                                registerCode.value = ""
                                navController.navigate(Router.registerResult)
                            }
                        }
                    }
                )
            } catch (e: Exception){
                Client.handleException(e)
            }finally {
                registerAction.value = "立即绑定"
            }
        }
    }

    override fun clear() {
        registerCode.value = ""
        registerAction.value = "立即绑定"
        registerBalance.value = -1
    }
}