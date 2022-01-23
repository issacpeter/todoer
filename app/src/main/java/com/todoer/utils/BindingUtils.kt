package com.todoer.utils

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout


object BindingUtils {

    @JvmStatic
    @BindingAdapter("app:errorText")
    fun setErrorMessage(view: TextInputLayout, errorMessage: String?) {
        view.error = errorMessage
    }

    @JvmStatic
    @BindingAdapter("app:visibility")
    fun View.bindVisibility(visible: Boolean?) {
        isVisible = visible == true
        // visibility = if (visible == true) View.VISIBLE else View.GONE
    }

}