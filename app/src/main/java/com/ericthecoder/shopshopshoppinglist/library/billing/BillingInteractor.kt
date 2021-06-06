package com.ericthecoder.shopshopshoppinglist.library.billing

import android.content.Context
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.ericthecoder.dependencies.android.activity.TopActivityProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

@ExperimentalCoroutinesApi
class BillingInteractor(
    applicationContext: Context,
    private val topActivityProvider: TopActivityProvider
) {

    private var premiumSkuDetails: SkuDetails? = null

    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        when {
            billingResult.responseCode == BillingResponseCode.OK && purchases?.any() == true -> {
                handlePurchase(purchases.first())
            }
            billingResult.responseCode == BillingResponseCode.USER_CANCELED -> {}
            else -> {}
        }
    }

    private val billingClient = BillingClient
        .newBuilder(applicationContext)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases()
        .build()

    suspend fun connectIfNeeded(): Boolean {
        var result = billingClient.isReady || billingClient.connect()

        if (!result) {
            repeat(RETRY_POLICY_REPEAT) {
                if (!result) {
                    result = billingClient.isReady || billingClient.connect()
                }
            }
        }
        return result
    }

    suspend fun getPremiumSkuDetails() = premiumSkuDetails
        ?:  SkuDetailsParams
            .newBuilder()
            .setSkusList(listOf(PREMIUM_PRODUCT_ID))
            .setType(BillingClient.SkuType.INAPP)
            .build()
            .let { params -> billingClient.querySkuDetails(params) }
            .skuDetailsList
            ?.firstOrNull()

    suspend fun launchBillingFlow(): Int {
        val activity = topActivityProvider.getTopActivity()
        val billingFlowParams = getBillingFlowParams()

        return when {
            activity == null -> BillingResponseCode.ERROR
            billingFlowParams == null -> BillingResponseCode.ITEM_UNAVAILABLE
            else -> billingClient.launchBillingFlow(activity, billingFlowParams).responseCode
        }
    }

    fun disconnectBillingClient() = billingClient.endConnection()

    private suspend fun getBillingFlowParams() = getPremiumSkuDetails()?.let { skuDetails ->
        BillingFlowParams
            .newBuilder()
            .setSkuDetails(skuDetails)
            .build()
    }

    private fun handlePurchase(purchase: Purchase) {

    }

    private suspend fun BillingClient.connect(): Boolean =
        suspendCancellableCoroutine { continuation ->
            startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingResponseCode.OK) {
                        continuation.resume(true) {
                            continuation.resumeWithException(it)
                        }
                    } else {
                        continuation.resume(false) {
                            continuation.resumeWithException(it)
                        }
                    }
                }

                override fun onBillingServiceDisconnected() {
                    continuation.resume(false) {
                        continuation.resumeWithException(it)
                    }
                }
            })
        }

    companion object {

        private const val RETRY_POLICY_REPEAT = 5
        private const val PREMIUM_PRODUCT_ID = "shopshop_premium"
    }
}
