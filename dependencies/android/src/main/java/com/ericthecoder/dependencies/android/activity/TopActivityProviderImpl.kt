package com.ericthecoder.dependencies.android.activity

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class TopActivityProviderImpl : TopActivityProvider {

    private var topActivity: Activity? = null

    override fun getTopActivity() = topActivity as? AppCompatActivity

    override fun setupWith(app: Application) {
        app.registerActivityLifecycleCallbacks(callbacks)
    }

    private val callbacks: Application.ActivityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            if (activity is AppCompatActivity)
                topActivity = activity
        }

        override fun onActivityStarted(activity: Activity) {
            /* do nothing */
        }

        override fun onActivityResumed(activity: Activity) {
            if (activity is AppCompatActivity)
                topActivity = activity
        }

        override fun onActivityPaused(activity: Activity) {
            /* do nothing */
        }

        override fun onActivityStopped(activity: Activity) {
            /* do nothing */
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            /* do nothing */
        }

        override fun onActivityDestroyed(activity: Activity) {
            /* do nothing */
        }
    }
}
