package ru.zveron.lots_feed.mappings

import ru.zveron.contract.lot.Sort
import ru.zveron.contract.lot.TypeSort
import ru.zveron.contract.lot.WaterfallRequestKt
import ru.zveron.contract.lot.sort
import ru.zveron.lots_feed.models.sort.SortType
import ru.zveron.lots_feed.feed.ui.SortType as UiSortType

fun UiSortType.toSortType() = when (this) {
    UiSortType.DATE -> SortType.Newest
    UiSortType.CHEAP -> SortType.Cheap
    UiSortType.EXPENSIVE -> SortType.Expensive
}

fun SortType.toUiSortType() = when (this) {
    SortType.Newest -> UiSortType.DATE
    SortType.Cheap -> UiSortType.CHEAP
    SortType.Expensive -> UiSortType.EXPENSIVE
}

fun WaterfallRequestKt.Dsl.addSortType(sortType: SortType) {
    when (sortType) {
        SortType.Cheap -> {
            this.sort = sort {
                this.sortBy = Sort.SortBy.PRICE
                this.typeSort = TypeSort.ASC
            }
        }
        SortType.Expensive -> {
            this.sort = sort {
                this.sortBy = Sort.SortBy.PRICE
                this.typeSort = TypeSort.DESC
            }
        }
        SortType.Newest -> {
            this.sort = sort {
                this.sortBy = Sort.SortBy.DATE
                this.typeSort = TypeSort.DESC
            }
        }
    }
}