package com.ku_stacks.ku_ring.util

import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

fun <K, V> MutableStateFlow<MutableMap<K, V>>.modifyMap(block: MutableMap<K, V>.() -> Unit) {
    val newValue = this.value.toMutableMap().apply { block() }
    Timber.d("before: $value, after: $newValue")
    this.value = newValue
}

fun <E> MutableStateFlow<MutableSet<E>>.modifySet(block: MutableSet<E>.() -> Unit) {
    val newValue = this.value.toMutableSet().apply { block() }
    Timber.d("before: $value, after: $newValue")
    this.value = newValue
}