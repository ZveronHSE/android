package ru.zveron.lots_feed.models.filters

sealed class FilterOperationAndValue(val operation: FilterOperation) {
    abstract fun toFilterValue(): String

    data class Equality(val value: String) : FilterOperationAndValue(FilterOperation.EQUALITY) {
        override fun toFilterValue(): String {
            return value
        }
    }

    data class Negation(val value: String) : FilterOperationAndValue(FilterOperation.NEGATION) {
        override fun toFilterValue(): String {
            return value
        }
    }

    data class GreaterThan(val value: String) :
        FilterOperationAndValue(FilterOperation.GREATER_THAN) {
        override fun toFilterValue(): String {
            return value
        }
    }

    data class GreaterThanEqual(val value: String) :
        FilterOperationAndValue(FilterOperation.GREATER_THAN_EQUALITY) {
        override fun toFilterValue(): String {
            return value
        }
    }

    data class LessThan(val value: String) : FilterOperationAndValue(FilterOperation.LESS_THAN) {
        override fun toFilterValue(): String {
            return value
        }
    }

    data class LessThanEqual(val value: String) :
        FilterOperationAndValue(FilterOperation.LESS_THAN_EQUALITY) {
        override fun toFilterValue(): String {
            return value
        }
    }

    data class InValues(
        val values: List<String>,
    ) : FilterOperationAndValue(FilterOperation.IN) {
        override fun toFilterValue(): String {
            return values.joinToString(",")
        }
    }

    data class NotInValues(
        val values: List<String>,
    ) : FilterOperationAndValue(FilterOperation.NOT_IN) {
        override fun toFilterValue(): String {
            return values.joinToString(",")
        }
    }
}
