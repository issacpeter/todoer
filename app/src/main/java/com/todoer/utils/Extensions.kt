package com.todoer.utils

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.children
import java.text.SimpleDateFormat
import java.util.*

object Extensions {

    fun Date.toStringDate(): String? = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(this)

    fun RadioGroup.getCheckedRadioButtonPosition(): Int {
        val radioButtonId = checkedRadioButtonId
        return children.filter { it is RadioButton }
            .mapIndexed { index: Int, view: View ->
                index to view
            }.firstOrNull {
                it.second.id == radioButtonId
            }?.first ?: -1
    }
}