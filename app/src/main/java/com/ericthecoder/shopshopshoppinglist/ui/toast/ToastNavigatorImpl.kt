package com.ericthecoder.shopshopshoppinglist.ui.toast

import android.widget.Toast
import com.ericthecoder.shopshopshoppinglist.util.providers.TopActivityProvider

class ToastNavigatorImpl(
    private val topActivityProvider: TopActivityProvider
) : ToastNavigator {

    private val activity get() = topActivityProvider.getTopActivity()
        ?: throw NullPointerException()

    override fun show(message: String, length: Int) {
        activity.runOnUiThread { Toast.makeText(activity, message, length).show() }
    }

    override fun show(stringRes: Int, length: Int) {
        show(activity.getString(stringRes), length)
    }
}
