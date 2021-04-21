package com.ericdecanini.shopshopshoppinglist.util

import android.app.Activity
import android.app.Application

interface TopActivityProvider {

    fun getTopActivity(): Activity?

    fun setupWith(app: Application)

}
