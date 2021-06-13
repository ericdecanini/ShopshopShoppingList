package com.ericthecoder.shopshopshoppinglist.usecases.storage

interface PersistentStorageWriter {

    fun setOnboardingShown(onboardingShown: Boolean)

    fun setIsPremium(isPremium: Boolean)

}
