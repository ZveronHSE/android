package ru.zveron.design.wrappers

import androidx.compose.runtime.Immutable

@JvmInline
@Immutable
value class ListWrapper<T>(val list: List<T>)