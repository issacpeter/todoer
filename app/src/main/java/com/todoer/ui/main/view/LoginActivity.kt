package com.todoer.ui.main.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.todoer.R
import com.todoer.data.api.RetrofitService
import com.todoer.data.local.TodoListDatabase
import com.todoer.data.repository.MainRepository
import com.todoer.databinding.ActivityLoginBinding
import com.todoer.ui.main.viewmodel.LoginViewModel
import com.todoer.ui.main.viewmodel.MyViewModelFactory


class LoginActivity : AppCompatActivity() {

    lateinit var viewModel: LoginViewModel
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (shouldShowLogin()){
            binding =
                DataBindingUtil.setContentView(this, R.layout.activity_login)
            val retrofitService = RetrofitService.getInstance()
            val mainRepository = MainRepository(TodoListDatabase.getInstance(application), retrofitService)

            viewModel = ViewModelProvider(this,
                MyViewModelFactory(mainRepository)).get(LoginViewModel::class.java)

            viewModel.loginSuccess.observe(this, {token ->
                val preferences: SharedPreferences =
                    this.getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
                preferences.edit().putString("TOKEN", token).apply()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            })

            viewModel.successMessage.observe(this, {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            })

            viewModel.errorMessage.observe(this, {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            })

            viewModel.loading.observe(this, {
                if (it) {
                    binding.progressDialog.visibility = View.VISIBLE
                } else {
                    binding.progressDialog.visibility = View.GONE
                }
            })

            binding.viewModel = viewModel
        }else{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun shouldShowLogin(): Boolean {
        val preferences: SharedPreferences =
            this.getSharedPreferences("MY_APP", MODE_PRIVATE)
        val retrivedToken = preferences.getString("TOKEN", null) //second parameter default value.
        return retrivedToken.isNullOrEmpty()
    }
}