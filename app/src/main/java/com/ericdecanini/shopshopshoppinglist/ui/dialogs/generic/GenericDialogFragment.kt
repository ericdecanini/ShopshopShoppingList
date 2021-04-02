package com.ericdecanini.shopshopshoppinglist.ui.dialogs.generic

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.ericdecanini.shopshopshoppinglist.BR
import com.ericdecanini.shopshopshoppinglist.R
import com.ericdecanini.shopshopshoppinglist.databinding.DialogGenericBinding

class GenericDialogFragment private constructor(
    private val controllerDataGeneric: GenericDialogControllerData
) : DialogFragment() {

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
    binding.setVariable(BR.controller, GenericDialogController(this, controllerDataGeneric))
    return binding.root
  }

  class Builder(context: Context) {

    private val controllerData = GenericDialogControllerData(context)

    fun setTitle(title: String) = apply {
      controllerData.title = title
    }

    fun setMessage(message: String) = apply {
      controllerData.message = message
    }

    fun setPositiveButton(text: String, onClick: (() -> Unit)?) = apply {
      controllerData.positiveText = text
      controllerData.positiveOnClick = onClick
    }

    fun setNegativeButton(text: String, onClick: (() -> Unit)?) = apply {
      controllerData.negativeText = text
      controllerData.negativeOnClick = onClick
    }

    fun setCancellable(cancellable: Boolean) = apply {
      controllerData.cancellable = cancellable
    }

    fun create(): GenericDialogFragment = GenericDialogFragment(controllerData).apply {
      isCancelable = controllerDataGeneric.cancellable
    }
  }

}
