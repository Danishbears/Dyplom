package com.example.diplomich.`interface`

import android.view.View
import java.text.FieldPosition

interface ClickInteface {
    fun onClick(v: View,position: Int,isLongClick:Boolean)
}