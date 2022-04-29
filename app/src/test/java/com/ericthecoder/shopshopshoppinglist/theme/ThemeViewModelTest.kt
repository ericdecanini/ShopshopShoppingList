package com.ericthecoder.shopshopshoppinglist.theme

import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageReader
import com.ericthecoder.shopshopshoppinglist.usecases.storage.PersistentStorageWriter
import com.ericthecoder.shopshopshoppinglist.util.InstantTaskExecutorExtension
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class ThemeViewModelTest {

    private val persistentStorageReader: PersistentStorageReader = mockk()
    private val persistentStorageWriter: PersistentStorageWriter = mockk(relaxUnitFun = true)

    @Test
    fun `init gets currently stored theme`() {
        val currentTheme = Theme.GREEN
        every { persistentStorageReader.getCurrentTheme() } returns currentTheme.ordinal

        val viewModel = initViewModel()

        assertThat(viewModel.theme.value).isEqualTo(currentTheme)
    }

    @Test
    fun `getTheme returns theme from live data`() {
        val currentTheme = Theme.GREEN
        every { persistentStorageReader.getCurrentTheme() } returns currentTheme.ordinal
        val viewModel = initViewModel()

        val theme = viewModel.getTheme()

        assertThat(theme).isEqualTo(currentTheme)
    }

    @Test
    fun `cycleNextTheme cycles to the next theme`() {
        val currentTheme = Theme.GREEN
        every { persistentStorageReader.getCurrentTheme() } returns currentTheme.ordinal
        val viewModel = initViewModel()

        viewModel.cycleNextTheme()

        assertThat(viewModel.theme.value).isEqualTo(Theme.values()[currentTheme.ordinal + 1])
        verify { persistentStorageWriter.setCurrentTheme(currentTheme.ordinal + 1) }
    }

    @Test
    fun `when current theme color is last in set, cycleNextTheme cycles back to the first theme`() {
        val currentTheme = Theme.values()[Theme.values().lastIndex]
        every { persistentStorageReader.getCurrentTheme() } returns currentTheme.ordinal
        val viewModel = initViewModel()

        viewModel.cycleNextTheme()

        assertThat(viewModel.theme.value).isEqualTo(Theme.values().first())
        verify { persistentStorageWriter.setCurrentTheme(Theme.values().first().ordinal) }
    }

    private fun initViewModel() = ThemeViewModel(persistentStorageReader, persistentStorageWriter)
}