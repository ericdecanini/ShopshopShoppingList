package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ericdecanini.shopshopshoppinglist.usecases.viewstate.ListViewState
import com.ericdecanini.shopshopshoppinglist.util.ViewStateProvider
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before
import org.junit.Rule

class ListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val viewStateProvider: ViewStateProvider = mock()
    private val viewState: ListViewState = mock()

    private lateinit var viewModel: ListViewModel

    @Before
    fun setUp() {
        given(viewStateProvider.create(ListViewState::class.java)).willReturn(viewState)
        viewModel = ListViewModel(viewStateProvider)
    }
}
