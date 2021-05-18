package com.ericthecoder.shopshopshoppinglist.mvvm.activity.onboarding

import com.ericthecoder.shopshopshoppinglist.R

enum class OnboardingPage(
    val titleRes: Int,
    val descriptionRes: Int,
    val imageRes: Int,
    val backgroundRes: Int
) {
    FIRST(
        R.string.onboarding_title_one,
        R.string.onboarding_description_one,
        R.drawable.onboard1,
        R.drawable.onboardbg1
    ),
    SECOND(
        R.string.onboarding_title_two,
        R.string.onboarding_description_two,
        R.drawable.onboard2,
        R.drawable.onboardbg2
    ),
    THIRD(
        R.string.onboarding_title_three,
        R.string.onboarding_description_three,
        R.drawable.onboard3,
        R.drawable.onboardbg3
    )
}
