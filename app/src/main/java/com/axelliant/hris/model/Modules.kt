package com.axelliant.hris.model

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes

//data class Modules(val id:Int, val name:String,val description: String, val color: Int, val drawable:Drawable?)
data class Modules(val id:Int, val name:String,val description: String, val color: Drawable?,@ColorRes val iconBgColor: Int
                   , val drawable:Drawable?)