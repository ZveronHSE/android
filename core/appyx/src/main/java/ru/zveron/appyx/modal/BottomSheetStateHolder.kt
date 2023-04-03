package ru.zveron.appyx.modal

import kotlinx.coroutines.flow.Flow

interface BottomSheetStateHolder {
    val shouldBlockBottomSheet: Flow<Boolean>
}