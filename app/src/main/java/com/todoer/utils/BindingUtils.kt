package com.todoer.utils

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout


object BindingUtils {

    @JvmStatic
    @BindingAdapter("app:errorText")
    fun setErrorMessage(view: TextInputLayout, errorMessage: String?) {
        view.error = errorMessage
    }

}