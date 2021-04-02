package com.ericdecanini.shopshopshoppinglist.ui.toast

import android.widget.Toast

interface ToastNavigator {

    fun show(message: String, length: Int = Toast.LENGTH_SHORT)

    fun show(stringRes: Int, length: Int = Toast.LENGTH_SHORT)

}
