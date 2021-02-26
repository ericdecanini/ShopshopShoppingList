package com.ericdecanini.dependencies.android.resources

import androidx.annotation.StringRes

interface ResourceProvider {

    fun getString(@StringRes resourceId: Int): String

}
