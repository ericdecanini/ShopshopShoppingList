package com.ericdecanini.dependencies.android.resources

import android.content.res.Resources
import com.ericdecanini.dependencies.android.R
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ResourceProviderImplTest {

    private val resources: Resources = mock()
    private val resourceProvider = ResourceProviderImpl(resources)

    @Test
    fun whenGettingString_thenStringFromResourceReturned() {
        given(resources.getString(R.string.abc_action_bar_home_description)).willReturn("string_value")

        val string = resourceProvider.getString(R.string.abc_action_bar_home_description)

        assertThat(string).isEqualTo("string_value")
    }

}
