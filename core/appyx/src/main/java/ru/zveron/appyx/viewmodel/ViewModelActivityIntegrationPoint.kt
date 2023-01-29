package ru.zveron.appyx.viewmodel

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.bumble.appyx.core.integrationpoint.ActivityIntegrationPoint

class ViewModelActivityIntegrationPoint(
    private val activity: ComponentActivity,
    savedInstanceState: Bundle?,
): ActivityIntegrationPoint(activity, savedInstanceState) {
    val viewModel = activity.getNodeViewModelInstance()

    fun isChangingConfiguration() = activity.isChangingConfigurations
}