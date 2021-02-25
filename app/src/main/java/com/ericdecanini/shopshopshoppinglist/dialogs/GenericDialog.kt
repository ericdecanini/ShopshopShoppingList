package com.ericdecanini.shopshopshoppinglist.dialogs

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

class GenericDialog private constructor(
    private val controller: DialogController
) : DialogFragment() {

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View {
    val binding: DialogGenericBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_generic, null, false)
    binding.setVariable(BR.controller, controller)
    return binding.root
  }

  class Builder(context: Context) {

    private val controller = DialogController(context)

    fun setTitle(title: String) = apply {
      controller.title = title
    }

    fun setMessage(message: String) = apply {
      controller.message = message
    }

    fun setPositiveButton(text: String, onClick: (() -> Unit)?) = apply {
      controller.positiveText = text
      controller.positiveOnClick = onClick
    }

    fun setNegativeButton(text: String, onClick: (() -> Unit)?) = apply {
      controller.negativeText = text
      controller.negativeOnClick = onClick
    }

    fun setCancellable(cancellable: Boolean) = apply {
      controller.cancellable = cancellable
    }

    fun create(): GenericDialog = GenericDialog(controller)
  }

}
