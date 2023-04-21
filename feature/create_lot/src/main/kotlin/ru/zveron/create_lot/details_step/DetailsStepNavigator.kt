package ru.zveron.create_lot.details_step

interface DetailsStepNavigator {
    fun completeDetailsStep()

    fun pickParameterValue(id: Int, parameterName: String)
}