package com.ericthecoder.shopshopshoppinglist.ui.dialog.theme

import androidx.appcompat.app.AppCompatActivity
import com.ericthecoder.shopshopshoppinglist.BR
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.databinding.DialogThemeBinding
import com.ericthecoder.shopshopshoppinglist.theme.Theme
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object ThemeDialogBuilder {

    var currentTheme = Theme.BLUE
    var selectedTheme = Theme.BLUE

    fun show(activity: AppCompatActivity, onThemeSelected: (Theme) -> Unit) {
        val binding = DialogThemeBinding.inflate(activity.layoutInflater)
        binding.setVariable(BR.listener, createThemeSelectedListener(binding))
        binding.setVariable(BR.selectedTheme, selectedTheme)

        MaterialAlertDialogBuilder(activity)
            .setTitle(activity.getString(R.string.theme_dialog_title))
            .setView(binding.root)
            .setPositiveButton(R.string.restart) { _, _ -> selectTheme(onThemeSelected) }
            .setNegativeButton(R.string.cancel) { _, _ -> selectedTheme = currentTheme }
            .show()
    }

    private fun selectTheme(onThemeSelected: (Theme) -> Unit) {
        currentTheme = selectedTheme
        onThemeSelected(selectedTheme)
    }

    private fun createThemeSelectedListener(binding: DialogThemeBinding) = object : ThemeSelectedListener {
        override fun onThemeSelected(theme: Theme) {
            selectedTheme = theme
            binding.setVariable(BR.selectedTheme, selectedTheme)
        }
    }
}