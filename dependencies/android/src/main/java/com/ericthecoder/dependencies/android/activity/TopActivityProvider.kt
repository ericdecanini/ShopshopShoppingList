package com.ericthecoder.dependencies.android.activity

import android.app.Application
import androidx.appcompat.app.AppCompatActivity

interface TopActivityProvider {

    fun getTopActivity(): AppCompatActivity?

    fun setupWith(app: Application)

}
