package com.ericthecoder.shopshopshoppinglist.ui.dialogs.rename

import android.content.Context
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.databinding.DialogRenameBinding
import java.io.Serializable

@Suppress("UNCHECKED_CAST")
class RenameDialogFragment : DialogFragment() {

    private lateinit var binding: DialogRenameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.DialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_rename, null, false)
        binding.title.text = arguments?.getString(EXTRA_HEADER)
        binding.newName.setText(arguments?.getString(EXTRA_AUTOFILL_TEXT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonClicks(view.context)
        addEditTextChangedListener(view.context)

        binding.newName.post {
            binding.newName.requestFocus()
            binding.newName.selectAll()
            val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }
    }

    private fun addEditTextChangedListener(context: Context) {
        val defaultEditTextBackground = AppCompatResources.getDrawable(context, R.drawable.bg_edit_new_item)
        binding.newName.addTextChangedListener { binding.newName.background = defaultEditTextBackground }
    }

    private fun setButtonClicks(context: Context) {
        binding.positiveButton.setOnClickListener {
            validateThenPerformPositiveOnClick(context)
        }
        binding.negativeButton.setOnClickListener {
            performNegativeOnClick()
        }
    }

    private fun validateThenPerformPositiveOnClick(context: Context) {
        val errorEditTextBackground = AppCompatResources.getDrawable(context, R.drawable.bg_edit_new_item_error)
        if (binding.newName.text.isNotBlank()) {
            performPositiveOnClick()
        } else {
            binding.newName.background = errorEditTextBackground
            binding.newName.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake))
            binding.newName.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        }
    }

    private fun performPositiveOnClick() {
        val positiveOnClick = arguments?.getSerializable(EXTRA_POSITIVE_CLICK) as? ((String) -> Unit)
        positiveOnClick?.invoke(binding.newName.text.toString())
        dismiss()
    }

    private fun performNegativeOnClick() {
        val negativeOnClick = arguments?.getSerializable(EXTRA_NEGATIVE_CLICK) as? (() -> Unit)
        negativeOnClick?.invoke()
        dismiss()
    }

    class Builder {

        private var header = ""
        private var autofillText = ""
        private var positiveOnClick: ((String) -> Unit)? = null
        private var negativeOnClick: (() -> Unit)? = null
        private var cancellable = true

        fun setHeader(header: String) = apply {
            this.header = header
        }

        fun setAutofillText(autofillText: String) = apply {
            this.autofillText = autofillText
        }

        fun setPositiveOnClick(onClick: ((String) -> Unit)?) = apply {
            this.positiveOnClick = onClick
        }

        fun setNegativeOnClick(onClick: (() -> Unit)?) = apply {
            this.negativeOnClick = onClick
        }

        fun setCancellable(cancellable: Boolean) = apply {
            this.cancellable = cancellable
        }

        fun build() = RenameDialogFragment().apply {
            val args = Bundle().apply {
                putString(EXTRA_HEADER, header)
                putString(EXTRA_AUTOFILL_TEXT, autofillText)
                putSerializable(EXTRA_POSITIVE_CLICK, positiveOnClick as Serializable?)
                putSerializable(EXTRA_NEGATIVE_CLICK, negativeOnClick as Serializable?)
            }

            arguments = args
            isCancelable = cancellable
        }
    }

    companion object {
        internal const val EXTRA_HEADER = "EXTRA_HEADER"
        internal const val EXTRA_AUTOFILL_TEXT = "EXTRA_AUTOFILL_TEXT"
        internal const val EXTRA_POSITIVE_CLICK = "EXTRA_POSITIVE_CLICK"
        internal const val EXTRA_NEGATIVE_CLICK = "EXTRA_NEGATIVE_CLICK"
    }

}
