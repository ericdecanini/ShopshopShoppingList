package com.ericthecoder.shopshopshoppinglist.usecases.storage

import com.ericthecoder.shopshopshoppinglist.entities.premium.PremiumStatus

interface PersistentStorageReader {

    fun hasOnboardingShown(): Boolean

    fun getPremiumStatus(): PremiumStatus

}
