package com.ericthecoder.shopshopshoppinglist.mvvm.activity.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.databinding.ActivitySettingsBinding
import dagger.android.support.DaggerAppCompatActivity

class SettingsActivity : DaggerAppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, SettingsActivity::class.java)
    }
}
