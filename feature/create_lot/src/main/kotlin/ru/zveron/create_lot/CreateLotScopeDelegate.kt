package ru.zveron.create_lot

import kotlinx.coroutines.CoroutineScope

interface CreateLotScopeDelegate {
    val coroutineScope: CoroutineScope
}