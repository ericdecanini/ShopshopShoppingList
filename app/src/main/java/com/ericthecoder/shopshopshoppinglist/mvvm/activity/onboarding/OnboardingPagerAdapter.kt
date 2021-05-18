package com.ericthecoder.shopshopshoppinglist.mvvm.activity.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.databinding.PagerItemOnboardingBinding
import com.ericthecoder.shopshopshoppinglist.util.navigator.Navigator

class OnboardingPagerAdapter(
    private val pager: ViewPager,
    private val navigator: Navigator
) : PagerAdapter() {

    private val context = pager.context

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        val binding = DataBindingUtil.inflate<PagerItemOnboardingBinding>(
            inflater,
            R.layout.pager_item_onboarding,
            container,
            false
        )

        applyUiData(binding, position)
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

    private fun applyUiData(binding: PagerItemOnboardingBinding, position: Int) {
        val onboardingPage = OnboardingPage.values()[position]
        binding.title.text = context.getString(onboardingPage.titleRes)
        binding.body.text = context.getString(onboardingPage.descriptionRes)
        binding.image.setImageResource(onboardingPage.imageRes)
        binding.bgImage.setImageResource(onboardingPage.backgroundRes)

        if (position == OnboardingPage.values().lastIndex) {
            binding.cta.text = context.getString(R.string.get_started)
            binding.cta.setOnClickListener { navigator.goToMainAndOpenList() }
        } else {
            binding.cta.text = context.getString(R.string.next)
            binding.cta.setOnClickListener { pager.setCurrentItem(position + 1, true) }
        }
    }
}
