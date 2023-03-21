package ru.zveron.lots_feed.mappings

import ru.zveron.contract.parameter.model.Parameter
import ru.zveron.contract.parameter.model.Type
import ru.zveron.lots_feed.models.parameters.Parameter as DomainParameter
import ru.zveron.lots_feed.models.parameters.ParameterType

fun Parameter.toDomainModel(): DomainParameter {
    return DomainParameter(
        id = this.id,
        name = this.name,
        type = this.type.toDomainType(),
        isRequired = this.isRequired,
        possibleValues = this.valuesList,
    )
}

fun Type.toDomainType(): ParameterType {
    return when (this) {
        Type.STRING -> ParameterType.STRING
        Type.DATE -> ParameterType.DATE
        Type.INTEGER -> ParameterType.INTEGER
        Type.UNRECOGNIZED -> ParameterType.UNKNOWN
    }
}