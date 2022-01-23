package com.todoer.ui.main.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.todoer.data.model.LoginResponse
import com.todoer.data.repository.MainRepository
import kotlinx.coroutines.*
import java.util.regex.Pattern

class LoginViewModel constructor(private val mainRepository: MainRepository) : ViewModel()  {

    val email = ObservableField<String>("eve.holt@reqres.in")
    val password = ObservableField<String>("cityslicka")
    val emailError = ObservableField<String>()
    val passwordError = ObservableField<String>()
    val errorMessage = MutableLiveData<String>()
    val successMessage = MutableLiveData<String>()
    val loginSuccess = MutableLiveData<String>()
    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    val loading = MutableLiveData<Boolean>()

    fun login() {
        if (isValidEmail() && isValidPassword()) {
            job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                loading.postValue(true)
                val response = mainRepository.login(email.get()!!, password.get()!!)
                withContext(Dispatchers.Main) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            loginSuccess.postValue(response.body()?.token)
                            onSuccess("Login Successful")
                        } else {
                            val gson = Gson()
                            val type = object : TypeToken<LoginResponse>() {}.type
                            val errorResponse: LoginResponse? = gson.fromJson(response.errorBody()?.string(), type)
                            onError("Error : ${errorResponse?.error} ")
                        }
                    }else{
                        onError("Error : Something went wrong ")
                    }
                }
            }
        }

    }

    private fun isValidEmail(): Boolean {
        var isValid = true
        val expression = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email.get())
        if (!matcher.matches()) {
            isValid = false
            emailError.set("Invalid Email")
        }else emailError.set("")
        return isValid
    }

    private fun isValidPassword(): Boolean {
        if (password.get().isNullOrEmpty()){
            passwordError.set("Invalid Password")
            return false
        }else{
            passwordError.set("")
            return true
        }
    }

    private fun onError(message: String) {
        errorMessage.postValue(message)
        loading.postValue(false)
    }

    private fun onSuccess(message: String) {
        successMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}