package com.ericthecoder.shopshopshoppinglist.library.firebase.admob

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.ericthecoder.shopshopshoppinglist.entities.premium.PremiumStatus
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageReader
import com.ericthecoder.shopshopshoppinglist.util.constants.AppSessionVariables
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback

class AppOpenAdManager private constructor(
    private val application: Application,
    private val persistentStorageReader: PersistentStorageReader,
) : LifecycleObserver, Application.ActivityLifecycleCallbacks {

    private var isShowingAd = false

    private var appOpenAd: AppOpenAd? = null
    private var loadCallback: AppOpenAdLoadCallback? = null
    private var currentActivity: Activity? = null

    private val adRequest: AdRequest
        get() = AdRequest.Builder().build()

    private val isAdAvailable: Boolean
        get() = appOpenAd != null

    init {
        application.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    private fun fetchAd() {
        if (isAdAvailable)
            return

        loadCallback = object: AppOpenAdLoadCallback() {
            override fun onAdLoaded(ad: AppOpenAd) {
                this@AppOpenAdManager.appOpenAd = ad
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                super.onAdFailedToLoad(error)
            }
        }

        AppOpenAd.load(application, AD_UNIT_ID, adRequest, APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback!!)
    }

    private fun showAdIfAvailable() {
        if (!isShowingAd && isAdAvailable) {
            val fullScreenContentCallback: FullScreenContentCallback = object: FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    this@AppOpenAdManager.appOpenAd = null
                    isShowingAd = false
                    fetchAd()
                }

                override fun onAdShowedFullScreenContent() {
                    isShowingAd = true
                }
            }

            appOpenAd?.fullScreenContentCallback = fullScreenContentCallback
            appOpenAd?.show(currentActivity!!)
            AppSessionVariables.hasAppOpenAdDisplayed = true
        } else {
            fetchAd()
        }
    }

    private fun shouldShowAd() = !AppSessionVariables.hasAppOpenAdDisplayed
            && persistentStorageReader.getPremiumStatus() != PremiumStatus.PREMIUM

    @OnLifecycleEvent(ON_START)
    fun onStart() {
        if (shouldShowAd()) { showAdIfAvailable() }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        currentActivity = null
    }

    companion object {
        // TODO: Replace with actual ad unit id
        private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/3419835294"

        fun initialize(application: Application, persistentStorageReader: PersistentStorageReader) {
            AppOpenAdManager(application, persistentStorageReader)
        }
    }
}
