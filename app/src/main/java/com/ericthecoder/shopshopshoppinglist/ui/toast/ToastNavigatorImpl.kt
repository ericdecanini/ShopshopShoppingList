package com.ericthecoder.shopshopshoppinglist.ui.toast

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ToastNavigatorImpl(
    private val activity: AppCompatActivity
) : ToastNavigator {

    override fun show(message: String, length: Int) {
        activity.runOnUiThread { Toast.makeText(activity, message, length).show() }
    }

    override fun show(stringRes: Int, length: Int) {
        show(activity.getString(stringRes), length)
    }
}
