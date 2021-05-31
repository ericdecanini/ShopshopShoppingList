package com.ericthecoder.shopshopshoppinglist.util.navigator

import android.content.Intent
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.main.MainActivity
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.main.NestedNavigationInstruction.OpenNewList
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.onboarding.OnboardingActivity
import com.ericthecoder.shopshopshoppinglist.util.providers.TopActivityProvider

class NavigatorImpl(private val topActivityProvider: TopActivityProvider) : Navigator {

    private val originActivity get() = topActivityProvider.getTopActivity()

    override fun goToMain() {
        originActivity?.let {
            MainActivity.getIntent(it).start()
        }
    }

    override fun goToMainAndOpenList() {
        originActivity?.let {
            MainActivity.getIntent(it, OpenNewList).start()
        }
    }

    override fun goToOnboarding() {
        originActivity?.let {
            OnboardingActivity.getIntent(it).start()
        }
    }

    private fun Intent.start() = originActivity?.startActivity(this)
}
