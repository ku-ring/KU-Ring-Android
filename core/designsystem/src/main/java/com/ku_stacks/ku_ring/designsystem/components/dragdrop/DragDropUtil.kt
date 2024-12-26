package com.ku_stacks.ku_ring.designsystem.components.dragdrop

internal fun <T> MutableList<T>.move(from: Int, to: Int) {
    if (from == to) return
    val element = this.removeAt(from)
    this.add(to, element)
}