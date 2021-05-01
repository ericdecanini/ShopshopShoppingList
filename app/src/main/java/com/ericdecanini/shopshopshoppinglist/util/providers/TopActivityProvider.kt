package com.ericdecanini.shopshopshoppinglist.util.providers

import android.app.Activity
import android.app.Application

interface TopActivityProvider {

    fun getTopActivity(): Activity?

    fun setupWith(app: Application)

}
