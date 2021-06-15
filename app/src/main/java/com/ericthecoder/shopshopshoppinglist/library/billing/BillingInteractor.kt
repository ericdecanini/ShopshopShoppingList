package com.ericthecoder.shopshopshoppinglist.library.billing

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val purchaseResultEmitter = MutableLiveData<PurchaseResult>()
    val purchaseResultLiveData: LiveData<PurchaseResult> get() = purchaseResultEmitter

    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        if (billingResult.responseCode == BillingResponseCode.OK && purchases?.any() == true)
            purchaseResultEmitter.postValue(PurchaseResult.Success(purchases.first()))
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

    suspend fun launchBillingFlow() {
        val activity = topActivityProvider.getTopActivity()
        val billingFlowParams = getBillingFlowParams()

        when {
            activity == null -> purchaseResultEmitter.postValue(PurchaseResult.Error)
            billingFlowParams == null -> purchaseResultEmitter.postValue(PurchaseResult.Unavailable)
            else -> billingClient.launchBillingFlow(activity, billingFlowParams)
        }
    }

    suspend fun acknowledgePurchase(purchase: Purchase): BillingResult {
        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        return billingClient.acknowledgePurchase(acknowledgePurchaseParams)
    }

    suspend fun getPremiumState(): PremiumState {
        val purchases = billingClient
            .queryPurchasesAsync(BillingClient.SkuType.INAPP)
            .purchasesList

        purchases.firstOrNull { it.isPremium() }?.let {
            return if (!it.isAcknowledged) {
                acknowledgePurchase(it)
                PremiumState.FRESHLY_ACKNOWLEDGED
            } else {
                PremiumState.PREMIUM
            }
        }

        return purchases.firstOrNull { it.isPending() }?.let {
            PremiumState.PENDING
        } ?: PremiumState.FREE
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

    private fun Purchase.isPremium() = purchaseState == Purchase.PurchaseState.PURCHASED
            && skus.any { it == PREMIUM_PRODUCT_ID }

    private fun Purchase.isPending() = purchaseState == Purchase.PurchaseState.PENDING
            && skus.any { it == PREMIUM_PRODUCT_ID }

    sealed class PurchaseResult {
        data class Success(val purchase: Purchase) : PurchaseResult()
        object Unavailable : PurchaseResult()
        object Error : PurchaseResult()
    }

    companion object {

        private const val RETRY_POLICY_REPEAT = 5
        private const val PREMIUM_PRODUCT_ID = "shopshop_premium"
    }
}
