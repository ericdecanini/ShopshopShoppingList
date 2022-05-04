package com.ericthecoder.shopshopshoppinglist.ui.dialog.theme

import android.app.Activity
import com.ericthecoder.shopshopshoppinglist.BR
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.databinding.DialogThemeBinding
import com.ericthecoder.shopshopshoppinglist.theme.Theme
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object ThemeDialogBuilder {

    fun show(activity: Activity, onThemeSelected: (Theme) -> Unit) {
        val binding = DialogThemeBinding.inflate(activity.layoutInflater)

        MaterialAlertDialogBuilder(activity)
            .setTitle(activity.getString(R.string.theme_dialog_title))
            .setView(binding.root)
            .show()
            .apply {
                binding.setVariable(BR.listener, object : ThemeSelectedListener {
                    override fun onThemeSelected(theme: Theme) {
                        dismiss()
                        onThemeSelected(theme)
                    }
                })
            }
    }
}