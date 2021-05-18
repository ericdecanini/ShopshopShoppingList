package com.ericthecoder.shopshopshoppinglist.mvvm.activity.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.databinding.ActivityOnboardingBinding
import com.ericthecoder.shopshopshoppinglist.util.navigator.Navigator
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class OnboardingActivity : DaggerAppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_onboarding)
        binding.lifecycleOwner = this

        val w: Window = window
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        with(binding.viewpager) {
            adapter = OnboardingPagerAdapter(this, navigator)
            binding.dotsTabLayout.setupWithViewPager(this)
        }
    }

    companion object {

        fun getIntent(context: Context) = Intent(context, OnboardingActivity::class.java)
    }
}
