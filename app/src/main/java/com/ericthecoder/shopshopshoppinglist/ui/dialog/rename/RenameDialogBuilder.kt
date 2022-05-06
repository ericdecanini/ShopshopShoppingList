package com.ericthecoder.shopshopshoppinglist.ui.dialog.rename

import android.app.Activity
import android.content.res.ColorStateList
import android.view.HapticFeedbackConstants
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.databinding.DialogRenameBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.elevation.SurfaceColors

object RenameDialogBuilder {

    fun show(
        activity: Activity,
        header: String,
        autofillText: String,
        positiveOnClick: (String) -> Unit,
        negativeOnClick: (() -> Unit)?,
        cancellable: Boolean,
    ) {
        val binding = DialogRenameBinding.inflate(activity.layoutInflater)
        binding.newName.setText(autofillText)

        MaterialAlertDialogBuilder(activity)
            .setTitle(header)
            .setView(binding.root)
            .setCancelable(cancellable)
            .show()
            .apply { addDialogEvents(binding, positiveOnClick, negativeOnClick) }
    }

    private fun AlertDialog.addDialogEvents(
        binding: DialogRenameBinding,
        positiveOnClick: (String) -> Unit,
        negativeOnClick: (() -> Unit)?,
    ) {
        binding.positiveButton.setOnClickListener {
            validateThenPerformPositiveOnClick(binding) {
                positiveOnClick.invoke(it)
                dismiss()
            }
        }
        binding.negativeButton.setOnClickListener {
            negativeOnClick?.invoke()
            dismiss()
        }
        binding.newName.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                positiveOnClick(v.text.toString())
                dismiss()
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun validateThenPerformPositiveOnClick(binding: DialogRenameBinding, onClick: (String) -> Unit) {
        binding.newName.text.toString().apply {
            if (isNotBlank()) {
                onClick(this)
            } else {
                signalBlankNewName(binding)
            }
        }
    }

    private fun signalBlankNewName(binding: DialogRenameBinding) = with(binding.newNameLayout) {
        startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake))
        performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    }
}