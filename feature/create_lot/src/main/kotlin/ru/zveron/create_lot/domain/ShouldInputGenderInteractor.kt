package ru.zveron.create_lot.domain

import ru.zveron.create_lot.data.LotCreateSelectedCategoriesRepository

internal class ShouldInputGenderInteractor(
    private val lotCreateSelectedCategoriesRepository: LotCreateSelectedCategoriesRepository,
) {
    fun shouldSelectGender(): Boolean {
        // id 1 stands for animals root category, for which we should select gender during lot create
        // TOOD: think how to do this more nicely
        return lotCreateSelectedCategoriesRepository.currentCategorySelection.value.rootCategory?.id == 1
    }
}