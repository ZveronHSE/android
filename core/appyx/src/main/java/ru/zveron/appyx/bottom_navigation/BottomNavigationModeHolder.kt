package ru.zveron.appyx.bottom_navigation

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.Flow

interface BottomNavigationModeHolder {
    val bottomNavigationMode: Flow<BottomNavigationMode>
}

@Immutable
enum class BottomNavigationMode {
    SHOW_BOTTOM_BAR,
    HIDE_BOTTOM_BAR,
}