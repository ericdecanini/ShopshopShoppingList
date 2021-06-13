package com.ericthecoder.shopshopshoppinglist.usecases.storage

interface PersistentStorageReader {

    fun hasOnboardingShown(): Boolean

    fun isPremium(): Boolean

}
