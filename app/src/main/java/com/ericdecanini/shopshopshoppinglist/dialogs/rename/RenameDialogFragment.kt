package com.ericdecanini.shopshopshoppinglist.dialogs.rename

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.ericdecanini.shopshopshoppinglist.BR
import com.ericdecanini.shopshopshoppinglist.R
import com.ericdecanini.shopshopshoppinglist.databinding.DialogRenameBinding

class RenameDialogFragment private constructor(
    private val controllerData: RenameDialogControllerData
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
    val binding: DialogRenameBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_rename, null, false)
    binding.setVariable(BR.controller, RenameDialogController(this, controllerData))
    return binding.root
  }

  class Builder(context: Context) {

    private val controllerData = RenameDialogControllerData(context)

    fun setListTitle(listTitle: String) = apply {
      controllerData.listTitleText = listTitle
    }

    fun setPositiveOnClick(onClick: ((String) -> Unit)?) = apply {
      controllerData.positiveOnClick = onClick
    }

    fun setNegativeOnClick(onClick: (() -> Unit)?) = apply {
      controllerData.negativeOnClick = onClick
    }

    fun setCancellable(cancellable: Boolean) = apply {
      controllerData.cancellable = cancellable
    }

    fun create(): RenameDialogFragment = RenameDialogFragment(controllerData).apply {
      isCancelable = controllerData.cancellable
    }
  }

}
