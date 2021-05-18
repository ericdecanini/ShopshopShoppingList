package com.ericthecoder.shopshopshoppinglist.util.providers

import android.app.Application
import androidx.appcompat.app.AppCompatActivity

interface TopActivityProvider {

    fun getTopActivity(): AppCompatActivity?

    fun setupWith(app: Application)

}
