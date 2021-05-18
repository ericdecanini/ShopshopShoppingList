package com.ericthecoder.shopshopshoppinglist.util.navigator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.main.MainActivity
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.main.NestedNavigationInstruction.OpenNewList
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.onboarding.OnboardingActivity

class NavigatorImpl(private val originActivity: AppCompatActivity) : Navigator {

    override fun goToMain() = MainActivity.getIntent(originActivity).start()

    override fun goToMainAndOpenList() = MainActivity.getIntent(originActivity, OpenNewList).start()

    override fun goToOnboarding() = OnboardingActivity.getIntent(originActivity).start()

    private fun Intent.start() = originActivity.startActivity(this)
}
