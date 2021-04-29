package com.ericdecanini.shopshopshoppinglist.mvvm.activity.onboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.ericdecanini.shopshopshoppinglist.R
import com.ericdecanini.shopshopshoppinglist.databinding.PagerItemOnboardingBinding

class OnboardingPagerAdapter(private val context: Context) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val onboardingPage = OnboardingPage.values()[position]
        val inflater = LayoutInflater.from(context)
        val binding = DataBindingUtil.inflate<PagerItemOnboardingBinding>(
            inflater,
            R.layout.pager_item_onboarding,
            container,
            false
        )

        applyUiData(binding, onboardingPage)
        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }

    override fun getCount() = OnboardingPage.values().size

    override fun isViewFromObject(view: View, item: Any) = view == item

    override fun getPageTitle(position: Int): CharSequence {
        val customPagerEnum = OnboardingPage.values()[position]
        return context.getString(customPagerEnum.titleRes)
    }

    private fun applyUiData(binding: PagerItemOnboardingBinding, onboardingPage: OnboardingPage) {
        binding.title.text = context.getString(onboardingPage.titleRes)
        binding.body.text = context.getString(onboardingPage.descriptionRes)
        binding.image.setImageResource(onboardingPage.imageRes)
    }
}
