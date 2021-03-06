package com.ericthecoder.shopshopshoppinglist.ui.dialogs.generic

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.ericthecoder.shopshopshoppinglist.BR
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.databinding.DialogGenericBinding
import java.io.Serializable

class GenericDialogFragment : DialogFragment() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setStyle(STYLE_NORMAL, R.style.DialogTheme)
  }

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View {
    val binding: DialogGenericBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_generic, null, false)
    binding.setVariable(BR.controller, GenericDialogController(this, arguments))
    return binding.root
  }

  class Builder(context: Context) {

    private var title = ""
    private var message = ""
    private var positiveText: String? = context.getString(R.string.ok)
    private var positiveOnClick: (() -> Unit)? = null
    private var negativeText: String? = null
    private var negativeOnClick: (() -> Unit)? = null
    private var cancellable = true

    fun setTitle(title: String?) = apply {
      this.title = title ?: ""
    }

    fun setMessage(message: String?) = apply {
      this.message = message ?: ""
    }

    fun setPositiveButton(text: String?, onClick: (() -> Unit)?) = apply {
      this.positiveText = text
      this.positiveOnClick = onClick
    }

    fun setNegativeButton(text: String?, onClick: (() -> Unit)?) = apply {
      this.negativeText = text
      this.negativeOnClick = onClick
    }

    fun setCancellable(cancellable: Boolean) = apply {
      this.cancellable = cancellable
    }

    fun create(): GenericDialogFragment = GenericDialogFragment().apply {
      val args = Bundle().apply {
        putString(EXTRA_TITLE, title)
        putString(EXTRA_MESSAGE, message)
        putString(EXTRA_POSITIVE_TEXT, positiveText)
        putSerializable(EXTRA_POSITIVE_CLICK, positiveOnClick as Serializable?)
        putString(EXTRA_NEGATIVE_TEXT, negativeText)
        putSerializable(EXTRA_NEGATIVE_CLICK, negativeOnClick as Serializable?)
      }

      arguments = args
      isCancelable = cancellable
    }
  }

  companion object {
    internal const val EXTRA_TITLE = "EXTRA_TITLE"
    internal const val EXTRA_MESSAGE = "EXTRA_MESSAGE"
    internal const val EXTRA_POSITIVE_TEXT = "EXTRA_POSITIVE_TEXT"
    internal const val EXTRA_POSITIVE_CLICK = "EXTRA_POSITIVE_CLICK"
    internal const val EXTRA_NEGATIVE_TEXT = "EXTRA_NEGATIVE_TEXT"
    internal const val EXTRA_NEGATIVE_CLICK = "EXTRA_NEGATIVE_CLICK"
  }
}
