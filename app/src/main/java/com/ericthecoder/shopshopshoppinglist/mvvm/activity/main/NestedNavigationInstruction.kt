package com.ericthecoder.shopshopshoppinglist.mvvm.activity.main

import java.io.Serializable

sealed class NestedNavigationInstruction : Serializable {
    object OpenNewList : NestedNavigationInstruction()
}
