package ru.zveron.create_lot.details_step.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.zveron.create_lot.data.LotCreateInfoRepository
import ru.zveron.create_lot.data.LotCreateSelectedCategoriesRepository
import ru.zveron.models.parameters.Parameter
import ru.zveron.parameters.data.ParametersRepository

internal class LoadParametersInteractor(
    private val parametersRepository: ParametersRepository,
    private val lotCreateSelectedCategoriesRepository: LotCreateSelectedCategoriesRepository,
    private val lotCreateInfoRepository: LotCreateInfoRepository,
) {
    suspend fun loadParameters(): List<Parameter> {
        val categoryId = lotCreateSelectedCategoriesRepository.currentCategorySelection.value.innerCategory?.id
            ?: throw IllegalStateException("No inner category selected!")
        val lotFormId = lotCreateInfoRepository.getLotFormId() ?: throw IllegalStateException("No lot form selected!")

        return withContext(Dispatchers.IO) {
            parametersRepository.loadParameters(categoryId, lotFormId)
        }
    }
}