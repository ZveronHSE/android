package ru.zveron.appyx.viewmodel

import android.os.Bundle
import com.bumble.appyx.core.integrationpoint.ActivityIntegrationPoint
import com.bumble.appyx.core.integrationpoint.NodeComponentActivity

open class ViewModelNodeComponentActivity: NodeComponentActivity() {
    override fun createIntegrationPoint(savedInstanceState: Bundle?): ActivityIntegrationPoint {
        return ViewModelActivityIntegrationPoint(
            activity = this,
            savedInstanceState = savedInstanceState,
        )
    }
}