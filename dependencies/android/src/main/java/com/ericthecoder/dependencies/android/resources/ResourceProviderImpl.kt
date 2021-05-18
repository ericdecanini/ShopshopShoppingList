package com.ericthecoder.dependencies.android.resources

import android.content.res.Resources

class ResourceProviderImpl(private val resources: Resources) : ResourceProvider {

    override fun getString(resourceId: Int) = resources.getString(resourceId)

    override fun getString(resourceId: Int, vararg formatArgs: Any)
            = resources.getString(resourceId, *formatArgs)
}
