package com.ku_stacks.ku_ring.remote.util

import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class Default

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class Library

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class KuringSpace