package com.ericthecoder.dependencies.android.resources

import androidx.annotation.StringRes

interface ResourceProvider {

    fun getString(@StringRes resourceId: Int): String

    fun getString(@StringRes resourceId: Int, vararg formatArgs: Any): String

}
