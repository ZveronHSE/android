package ru.zveron.create_lot.gender_step

import androidx.annotation.StringRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.zveron.choose_item.ChooseItem
import ru.zveron.choose_item.ChooseItemItemProvider
import ru.zveron.choose_item.ChooseItemUiState
import ru.zveron.create_lot.R
import ru.zveron.create_lot.data.LotCreateInfoRepository
import ru.zveron.design.resources.ZveronText
import ru.zveron.models.gender.Gender

internal class GenderItemProvider(
    private val genderStepNavigator: GenderStepNavigator,
    private val lotCreateInfoRepository: LotCreateInfoRepository,
): ChooseItemItemProvider {
    private val gendersList by lazy {
        UiGender.values().mapIndexed { index, gender ->
            ChooseItem(index, ZveronText.RawResource(gender.resId))
        }
    }

    private val gendersMap by lazy {
        UiGender.values().mapIndexed { index, gender -> index to gender }.toMap()
    }

    override val uiState: StateFlow<ChooseItemUiState> by lazy {
        MutableStateFlow(ChooseItemUiState.Success(gendersList)).asStateFlow()
    }

    override fun itemPicked(id: Int) {
        val gender = when (gendersMap.getValue(id)) {
            UiGender.MALE -> Gender.MALE
            UiGender.FEMALE -> Gender.FEMALE
        }
        lotCreateInfoRepository.setGender(gender)
        genderStepNavigator.completeGenderStep()
    }
}

private enum class UiGender(@StringRes val resId: Int) {
    MALE(R.string.gender_male),
    FEMALE(R.string.gender_female),
}