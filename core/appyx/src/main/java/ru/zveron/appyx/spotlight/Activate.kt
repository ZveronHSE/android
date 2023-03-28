package ru.zveron.appyx.spotlight

import android.os.Parcelable
import com.bumble.appyx.core.navigation.NavElements
import com.bumble.appyx.core.navigation.Operation
import com.bumble.appyx.navmodel.spotlight.Spotlight
import kotlinx.parcelize.Parcelize

@Parcelize
class Activate<T : Parcelable>(
    private val navTarget: T,
) : Operation<T, Spotlight.State> {
    override fun invoke(elements: NavElements<T, Spotlight.State>): NavElements<T, Spotlight.State> {
        val elementToReplace = elements.find { it.key.navTarget == navTarget }
        val activateIndex = elements.indexOf(elementToReplace)

        return elements.mapIndexed { index, element ->
            when {
                index < activateIndex -> element.transitionTo(
                    newTargetState = Spotlight.State.INACTIVE_BEFORE,
                    operation = this
                )
                index == activateIndex -> element.transitionTo(
                    newTargetState = Spotlight.State.ACTIVE,
                    operation = this,
                )
                else -> {
                    element.transitionTo(
                        newTargetState = Spotlight.State.INACTIVE_AFTER,
                        operation = this,
                    )
                }
            }
        }
    }

    override fun isApplicable(elements: NavElements<T, Spotlight.State>): Boolean {
        return elements.find { it.key.navTarget == navTarget } != null
    }
}

fun <T : Parcelable> Spotlight<T>.activate(element: T) {
    accept(Activate(element))
}