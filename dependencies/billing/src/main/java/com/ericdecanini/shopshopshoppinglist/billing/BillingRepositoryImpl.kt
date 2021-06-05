package com.ericdecanini.shopshopshoppinglist.billing

import android.content.Context
import android.util.Log
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PurchasesUpdatedListener
import com.ericthecoder.shopshopshoppinglist.usecases.repository.BillingRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

@ExperimentalCoroutinesApi
class BillingRepositoryImpl(context: Context) : BillingRepository {

    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        // TODO: Implement
    }

    private val billingClient = BillingClient
        .newBuilder(context)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases()
        .build()

    override suspend fun isSubscriptionSupported(): Boolean {
        if (!connectIfNeeded()) return false

        val billingResult = billingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS)

        var succeeded = false

        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.SERVICE_DISCONNECTED -> connectIfNeeded()
            BillingClient.BillingResponseCode.OK -> succeeded = true
            else -> Log.w("BillingClientProvider", "isSubscriptionSupported() error: ${billingResult.debugMessage}")
        }
        return succeeded
    }

    override fun disconnectBillingClient() = billingClient.endConnection()

    private suspend fun connectIfNeeded(): Boolean {
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
    }
}
