package com.ericthecoder.shopshopshoppinglist.usecases.storage

import com.ericthecoder.shopshopshoppinglist.entities.premium.PremiumStatus

interface PersistentStorageWriter {

    fun setOnboardingShown(onboardingShown: Boolean)

    fun setPremiumStatus(premiumStatus: PremiumStatus)

    fun setCurrentTheme(index: Int)
}
