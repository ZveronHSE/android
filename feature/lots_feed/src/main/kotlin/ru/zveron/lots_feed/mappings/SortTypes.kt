package ru.zveron.lots_feed.mappings

import ru.zveron.contract.lot.TypeSort
import ru.zveron.contract.lot.WaterfallRequestKt
import ru.zveron.contract.lot.sortByDate
import ru.zveron.contract.lot.sortByPrice
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
            this.sortByPrice = sortByPrice { typeSort = TypeSort.ASC }
        }
        SortType.Expensive -> {
            this.sortByPrice = sortByPrice { typeSort = TypeSort.DESC }
        }
        SortType.Newest -> {
            this.sortByDate = sortByDate { typeSort = TypeSort.DESC }
        }
    }
}