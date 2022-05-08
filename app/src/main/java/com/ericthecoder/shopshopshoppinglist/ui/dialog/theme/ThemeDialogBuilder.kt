package com.ericthecoder.shopshopshoppinglist.ui.dialog.theme

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.ericthecoder.shopshopshoppinglist.BR
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.databinding.DialogThemeBinding
import com.ericthecoder.shopshopshoppinglist.entities.theme.Theme
import com.google.android.material.color.DynamicColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object ThemeDialogBuilder {

    var selectedTheme = Theme.BLUE

    fun show(
        activity: AppCompatActivity,
        currentTheme: Theme,
        isPremium: Boolean,
        onThemeSelected: (Theme) -> Unit,
    ) {
        selectedTheme = currentTheme
        val binding = inflateBinding(activity.layoutInflater, isPremium)

        MaterialAlertDialogBuilder(activity)
            .setTitle(activity.getString(R.string.theme_dialog_title))
            .setView(binding.root)
            .setPositiveButton(R.string.select) { _, _ -> selectTheme(onThemeSelected) }
            .setNegativeButton(R.string.cancel) { _, _ -> selectedTheme = currentTheme }
            .show()
    }

    private fun inflateBinding(layoutInflater: LayoutInflater, isPremium: Boolean): DialogThemeBinding {
        val binding = DialogThemeBinding.inflate(layoutInflater)
        binding.setVariable(BR.listener, createThemeSelectedListener(binding))
        binding.setVariable(BR.selectedTheme, selectedTheme)
        binding.notice.isVisible = !isPremium
        if (!DynamicColors.isDynamicColorAvailable()) {
            binding.themeDynamic.isVisible = false
        }

        return binding
    }

    private fun selectTheme(onThemeSelected: (Theme) -> Unit) {
        onThemeSelected(selectedTheme)
    }

    private fun createThemeSelectedListener(binding: DialogThemeBinding) = object : ThemeSelectedListener {
        override fun onThemeSelected(theme: Theme) {
            selectedTheme = theme
            binding.setVariable(BR.selectedTheme, selectedTheme)
        }
    }
}