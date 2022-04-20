package com.ericthecoder.shopshopshoppinglist.ui.dialog.rename

import android.app.Activity
import android.view.HapticFeedbackConstants
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.addTextChangedListener
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.databinding.DialogRenameBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
        addEditTextChangedListener(binding.newName)
        binding.newName.setText(autofillText)

        MaterialAlertDialogBuilder(activity)
            .setIcon(R.drawable.ic_sticky_note)
            .setTitle(header)
            .setView(binding.root)
            .setCancelable(cancellable)
            .show()
            .apply { setButtonClickListeners(binding, positiveOnClick, negativeOnClick) }
    }

    private fun AlertDialog.setButtonClickListeners(
        binding: DialogRenameBinding,
        positiveOnClick: (String) -> Unit,
        negativeOnClick: (() -> Unit)?,
    ) {
        binding.positiveButton.setOnClickListener {
            validateThenPerformPositiveOnClick(binding.newName) {
                positiveOnClick.invoke(it)
                dismiss()
            }
        }
        binding.negativeButton.setOnClickListener {
            negativeOnClick?.invoke()
            dismiss()
        }
    }

    private fun addEditTextChangedListener(editText: EditText) {
        val defaultEditTextBackground = AppCompatResources.getDrawable(editText.context, R.drawable.bg_edit_new_item)
        editText.addTextChangedListener { editText.background = defaultEditTextBackground }
    }

    private fun validateThenPerformPositiveOnClick(editText: EditText, onClick: (String) -> Unit) {
        editText.text.toString().apply {
            if (isNotBlank()) {
                onClick(this)
            } else {
                signalBlankNewName(editText)
            }
        }
    }

    private fun signalBlankNewName(editText: EditText) {
        val errorEditTextBackground = AppCompatResources.getDrawable(editText.context, R.drawable.bg_edit_new_item_error)
        editText.background = errorEditTextBackground
        editText.startAnimation(AnimationUtils.loadAnimation(editText.context, R.anim.shake))
        editText.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    }
}