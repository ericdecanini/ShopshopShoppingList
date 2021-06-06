package com.ericthecoder.shopshopshoppinglist.library.billing

import android.content.Context
import com.android.billingclient.api.*
import com.ericthecoder.dependencies.android.activity.TopActivityProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

@ExperimentalCoroutinesApi
class BillingInteractor(
    applicationContext: Context,
    private val topActivityProvider: TopActivityProvider
) {

    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        // TODO: Implement
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

    suspend fun getPremiumSkuDetails() = SkuDetailsParams
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

        return if (activity != null && billingFlowParams != null) {
            billingClient.launchBillingFlow(activity, billingFlowParams).responseCode
        } else
            -1
    }

    fun disconnectBillingClient() = billingClient.endConnection()

    private suspend fun getBillingFlowParams() = getPremiumSkuDetails()?.let { skuDetails ->
        BillingFlowParams
            .newBuilder()
            .setSkuDetails(skuDetails)
            .build()
    }

    private suspend fun BillingClient.connect(): Boolean =
        suspendCancellableCoroutine { continuation ->
            startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
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
