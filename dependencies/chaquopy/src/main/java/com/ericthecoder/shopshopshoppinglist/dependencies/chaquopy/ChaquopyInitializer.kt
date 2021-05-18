package com.ericthecoder.shopshopshoppinglist.dependencies.chaquopy

import android.content.Context
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.ericthecoder.shopshopshoppinglist.usecases.python.PythonInitializer

class ChaquopyInitializer(private val context: Context) : PythonInitializer {

    override fun initialize() {
        Python.start(AndroidPlatform(context))
    }
}
