package com.ericdecanini.shopshopshoppinglist.dialogs

import android.app.Activity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

class DialogNavigatorImpl(
    private val supportFragmentManager: FragmentManager,
    private val activity: Activity
) : DialogNavigator {

  override fun displayDialog(
      title: String?,
      message: String?,
      positiveText: String?,
      positiveOnClick: (() -> Unit)?,
      negativeText: String?,
      negativeOnClick: (() -> Unit)?,
      cancellable: Boolean
  ) {
    val builder = GenericDialog.Builder(activity)
    title?.let { builder.setTitle(it) }
    message?.let { builder.setMessage(it) }
    positiveText?.let { builder.setPositiveButton(it, positiveOnClick) }
    negativeText?.let { builder.setNegativeButton(it, negativeOnClick) }
    builder.setCancellable(cancellable)

    val fragment = builder.create()
    fragment.showAllowingStateLoss(DIALOG_TAG)
  }

  private fun DialogFragment.showAllowingStateLoss(tag: String) =
      supportFragmentManager.showDialogAllowingStateLoss(this, tag)

  private fun FragmentManager.showDialogAllowingStateLoss(dialogFragment: DialogFragment, tag: String) {
    beginTransaction().add(dialogFragment, tag).commitAllowingStateLoss()
  }

  companion object {
    private const val DIALOG_TAG = "DIALOG_TAG"
  }
}
