package ru.zveron.lots_feed.mappings

import ru.zveron.contract.lot.Operation as GrpcOperation
import ru.zveron.contract.lot.Field as GrpcField
import ru.zveron.contract.lot.filter
import ru.zveron.lots_feed.models.filters.Filter
import ru.zveron.lots_feed.models.filters.FilterField
import ru.zveron.lots_feed.models.filters.FilterOperation
import ru.zveron.contract.lot.Filter as GrpcFilter

fun Filter.toGrpcFilter(): GrpcFilter {
    return filter {
        this.field = this@toGrpcFilter.field.toGrpcField()
        this.value = this@toGrpcFilter.value
        this.operation = this@toGrpcFilter.operation.toGrpcOperation()
    }
}

fun FilterField.toGrpcField(): GrpcField {
    return when (this) {
        FilterField.PRICE -> GrpcField.PRICE
        FilterField.LOT_FORM_ID -> GrpcField.LOT_FORM_ID
        FilterField.DATE_CREATION -> GrpcField.DATE_CREATION
        FilterField.GENDER -> GrpcField.GENDER
    }
}

fun FilterOperation.toGrpcOperation(): GrpcOperation {
    return when (this) {
        FilterOperation.EQUALITY -> GrpcOperation.EQUALITY
        FilterOperation.NEGATION -> GrpcOperation.NEGATION
        FilterOperation.GREATER_THAN -> GrpcOperation.GREATER_THAN
        FilterOperation.GREATER_THAN_EQUALITY -> GrpcOperation.GREATER_THAN_EQUALITY
        FilterOperation.LESS_THAN -> GrpcOperation.LESS_THAN
        FilterOperation.LESS_THAN_EQUALITY -> GrpcOperation.LESS_THAN_EQUALITY
        FilterOperation.IN -> GrpcOperation.IN
        FilterOperation.NOT_IN -> GrpcOperation.NOT_IN
    }
}

