package com.ericthecoder.shopshopshoppinglist.ui.dialogs.rename

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.ericthecoder.shopshopshoppinglist.BR
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.databinding.DialogRenameBinding
import java.io.Serializable

class RenameDialogFragment private constructor() : DialogFragment() {

  private lateinit var binding: DialogRenameBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setStyle(STYLE_NORMAL, R.style.DialogTheme)
  }

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View {
    binding = DataBindingUtil.inflate(inflater, R.layout.dialog_rename, null, false)
    binding.setVariable(BR.controller, RenameDialogController(this, arguments))
    return binding.root
  }

  class Builder {

    private var listTitleText = ""
    private var positiveOnClick: ((String) -> Unit)? = null
    private var negativeOnClick: (() -> Unit)? = null
    private var cancellable = true

    fun setListTitle(listTitle: String) = apply {
      this.listTitleText = listTitle
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
        putString(EXTRA_TITLE, listTitleText)
        putSerializable(EXTRA_POSITIVE_CLICK, positiveOnClick as Serializable?)
        putSerializable(EXTRA_NEGATIVE_CLICK, negativeOnClick as Serializable?)
      }

      arguments = args
      isCancelable = cancellable
    }
  }

  companion object {
    internal const val EXTRA_TITLE = "EXTRA_TITLE"
    internal const val EXTRA_POSITIVE_CLICK = "EXTRA_POSITIVE_CLICK"
    internal const val EXTRA_NEGATIVE_CLICK = "EXTRA_NEGATIVE_CLICK"
  }

}
