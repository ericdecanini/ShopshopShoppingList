package com.ericdecanini.shopshopshoppinglist.mvvm.activity.main

import java.io.Serializable

sealed class NestedNavigationInstruction : Serializable {
    object OpenNewList : NestedNavigationInstruction()
}
