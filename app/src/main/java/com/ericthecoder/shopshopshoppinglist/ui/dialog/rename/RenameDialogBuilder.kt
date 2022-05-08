package com.ericthecoder.shopshopshoppinglist.ui.dialog.rename

import android.app.Activity
import android.view.HapticFeedbackConstants
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.databinding.DialogRenameBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object RenameDialogBuilder {

    fun show(
        activity: Activity,
        header: String,
        autofillText: String,
        positiveButton: Pair<String, ((String) -> Unit)>,
        negativeButton: Pair<String, (() -> Unit)>?,
        cancellable: Boolean,
    ) {
        val binding = DialogRenameBinding.inflate(activity.layoutInflater)
        binding.newName.setText(autofillText)

        MaterialAlertDialogBuilder(activity)
            .setTitle(header)
            .setView(binding.root)
            .setCancelable(cancellable)
            .show()
            .apply { addDialogEvents(binding, positiveButton, negativeButton) }
    }

    private fun AlertDialog.addDialogEvents(
        binding: DialogRenameBinding,
        positiveButton: Pair<String, ((String) -> Unit)>,
        negativeButton: Pair<String, (() -> Unit)>?,
    ) {
        binding.positiveButton.apply {
            text = positiveButton.first
            setOnClickListener { onPositiveButtonClick(binding, positiveButton.second) }
        }

        binding.negativeButton.apply {
            negativeButton?.first?.let { text = it }
            setOnClickListener { onNegativeButtonClick(negativeButton?.second) }
        }

        binding.newName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                onPositiveButtonClick(binding, positiveButton.second)
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun AlertDialog.onPositiveButtonClick(binding: DialogRenameBinding, onClick: (String) -> Unit) {
        validateThenCallback(binding) {
            onClick.invoke(it)
            dismiss()
        }
    }

    private fun AlertDialog.onNegativeButtonClick(onClick: (() -> Unit)?) {
        onClick?.invoke()
        dismiss()
    }

    private fun validateThenCallback(binding: DialogRenameBinding, callback: (String) -> Unit) {
        binding.newName.text.toString().apply {
            if (isNotBlank()) {
                callback(this)
            } else {
                binding.newNameLayout.shake()
            }
        }
    }

    private fun FrameLayout.shake() {
        startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake))
        performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    }
}