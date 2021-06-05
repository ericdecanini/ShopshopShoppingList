package com.ericthecoder.shopshopshoppinglist.usecases.repository

interface BillingRepository {

    suspend fun isSubscriptionSupported(): Boolean

    fun disconnectBillingClient()
}
