package com.ericthecoder.shopshopshoppinglist.di.module.app.theme

import androidx.lifecycle.ViewModel
import com.ericthecoder.shopshopshoppinglist.di.module.app.ViewModelKey
import com.ericthecoder.shopshopshoppinglist.theme.ThemeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
interface ThemeModule {

    @Binds
    @Singleton
    @IntoMap
    @ViewModelKey(ThemeViewModel::class)
    fun bindThemeViewModel(viewModel: ThemeViewModel): ViewModel
}