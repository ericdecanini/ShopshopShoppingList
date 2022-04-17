package com.ericthecoder.shopshopshoppinglist.library.extension

fun <T> MutableList<T>.moveItem(from: Int, to: Int) {
    val item = get(from)
    removeAt(from)
    add(to, item)
}