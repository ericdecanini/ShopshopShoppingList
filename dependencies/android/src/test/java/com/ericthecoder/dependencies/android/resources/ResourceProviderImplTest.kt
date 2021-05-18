package com.ericthecoder.dependencies.android.resources

import android.content.res.Resources
import com.ericthecoder.dependencies.android.R
import com.nhaarman.mockitokotlin2.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ResourceProviderImplTest {

    private val resources: Resources = mock()
    private val resourceProvider = ResourceProviderImpl(resources)

    @Test
    fun whenGettingString_thenStringFromResourceReturned() {
        val expectedString = "expected_string"
        given(resources.getString(R.string.abc_action_bar_home_description)).willReturn(expectedString)

        val string = resourceProvider.getString(R.string.abc_action_bar_home_description)

        assertThat(string).isEqualTo(expectedString)
    }

    @Test
    fun givenFormatArgs_whenGettingString_thenStringFromResourceWithVarargsReturned() {
        val arg1 = "arg1"
        val arg2 = "arg2"
        val expectedString = "expected_string"
        given(resources.getString(eq(R.string.abc_action_bar_home_description), anyVararg())).willReturn(expectedString)

        val string = resourceProvider.getString(R.string.abc_action_bar_home_description, arg1, arg2)

        assertThat(string).isEqualTo(expectedString)
        verify(resources).getString(R.string.abc_action_bar_home_description, arg1, arg2)
    }

}
