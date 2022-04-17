package com.ericthecoder.dependencies.android.resources

import android.content.res.Resources
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.annotation.StringRes

interface ResourceProvider {

    fun getString(@StringRes resourceId: Int): String

    fun getString(@StringRes resourceId: Int, vararg formatArgs: Any): String

    fun getColor(@ColorRes resourceId: Int, theme: Resources.Theme): Int

    fun getColorArray(@ArrayRes resourceId: Int): IntArray
}
