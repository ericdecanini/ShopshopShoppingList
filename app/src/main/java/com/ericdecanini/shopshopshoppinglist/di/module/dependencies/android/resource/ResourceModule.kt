package com.ericdecanini.shopshopshoppinglist.di.module.dependencies.android.resource

import android.content.Context
import android.content.res.Resources
import com.ericdecanini.dependencies.android.resources.ResourceProvider
import com.ericdecanini.dependencies.android.resources.ResourceProviderImpl
import dagger.Module
import dagger.Provides

@Module
class ResourceModule {

    @Provides
    fun provideResources(context: Context): Resources = context.resources

    @Provides
    fun provideResourceProvider(resources: Resources): ResourceProvider
            = ResourceProviderImpl(resources)

}
