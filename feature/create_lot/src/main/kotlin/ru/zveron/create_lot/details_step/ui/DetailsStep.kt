package ru.zveron.create_lot.details_step.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.create_lot.R
import ru.zveron.design.components.ActionButton
import ru.zveron.design.components.Chip
import ru.zveron.design.components.LoadingChip
import ru.zveron.design.resources.ZveronText
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.design.theme.gray1
import ru.zveron.design.theme.gray5
import ru.zveron.design.wrappers.ListWrapper
import ru.zveron.design.R as DesignR

@Composable
internal fun DetailsStep(
    parametersUiState: ParametersUiState,
    continueButtonState: ContinueButtonState,
    description: String,
    setDescription: (String) -> Unit,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
    onParameterClick: (Long) -> Unit = {},
    onContinueButtonClick: () -> Unit = {},
) {
    Column(modifier = modifier.padding(top = 16.dp)) {
        DetailsStepHeader(onBackClicked = onBackClicked)

        Spacer(Modifier.height(32.dp))

        DetailsDescriptionInput(input = description, setInput = setDescription)

        Spacer(Modifier.height(58.dp))

        DetailsParameters(
            parametersUiState = parametersUiState,
            onParameterClick = onParameterClick,
        )

        Spacer(Modifier.weight(1f))

        DetailsContinueButton(
            continueButtonState = continueButtonState,
            onContinueButtonClick = onContinueButtonClick,
        )
    }
}

@Composable
private fun DetailsStepHeader(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
) {
    Box(modifier = modifier.fillMaxWidth()) {
        IconButton(
            onClick = onBackClicked,
            modifier = Modifier.align(Alignment.CenterStart),
        ) {
            Icon(
                painterResource(DesignR.drawable.ic_back),
                null,
            )
        }

        Text(
            stringResource(R.string.first_step_header),
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = (27.6).sp,
                letterSpacing = (-0.69).sp,
            ),
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
private fun DetailsDescriptionInput(
    input: String,
    setInput: (String) -> Unit,
) {
    Text(
        text = stringResource(R.string.description_step_title),
        style = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 34.sp,
            letterSpacing = (0.36).sp,
        ),
        modifier = Modifier.padding(start = 16.dp),
    )

    Spacer(Modifier.height(16.dp))

    Text(
        text = stringResource(R.string.description_input_label),
        style = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp,
        ),
        modifier = Modifier.padding(start = 16.dp),
    )

    Spacer(Modifier.height(4.dp))

    TextField(
        value = input,
        onValueChange = setInput,
        placeholder = { Text(stringResource(R.string.description_input_placeholder)) },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 34.dp)
            .height(120.dp),
    )
}

@Composable
private fun DetailsParameters(
    parametersUiState: ParametersUiState,
    onParameterClick: (Long) -> Unit = {},
) {
    Text(
        text = stringResource(R.string.parameters_label),
        style = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
        ),
        modifier = Modifier.padding(horizontal = 16.dp),
    )

    Spacer(Modifier.height(16.dp))

    val modifier = Modifier.padding(start = 16.dp)
    when (parametersUiState) {
        ParametersUiState.Loading -> DetailsParametersLoading(modifier)
        is ParametersUiState.Success -> DetailsParametersSuccess(
            parametersUiState = parametersUiState,
            modifier = modifier,
            onParameterClick = onParameterClick,
        )
    }
}

@Composable
private fun DetailsParametersLoading(
    modifier: Modifier = Modifier,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier,
    ) {
        items(3) {
            LoadingChip(width = 100.dp)
        }
    }
}

@Composable
private fun DetailsParametersSuccess(
    parametersUiState: ParametersUiState.Success,
    modifier: Modifier = Modifier,
    onParameterClick: (Long) -> Unit = {},
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier,
    ) {
        items(parametersUiState.parameters.list, { it.id }) { parameter ->
            val clicker = remember {
                { onParameterClick.invoke(parameter.id) }
            }

            val tintColor = if (parameter.selected) gray1 else gray5

            Chip(
                title = ZveronText.RawString(parameter.title),
                onClick = clicker,
                isActive = parameter.selected,
                leadSlot = {
                    Icon(
                        painter = painterResource(DesignR.drawable.ic_add),
                        contentDescription = null,
                        tint = tintColor,
                    )
                },
            )
        }
    }
}

@Composable
private fun DetailsContinueButton(
    continueButtonState: ContinueButtonState,
    onContinueButtonClick: () -> Unit = {},
) {
    ActionButton(
        enabled = continueButtonState.enabled,
        onClick = onContinueButtonClick,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 26.dp)
            .fillMaxWidth(),
    ) {
        Text(
            text = stringResource(R.string.continue_button_title),
            style = MaterialTheme.typography.body1,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailsStepPreview() {
    ZveronTheme {
        val input = remember {
            mutableStateOf("")
        }

        val parametersUiState = ParametersUiState.Success(
            ListWrapper(
                listOf(
                    ParameterUiState(1, "Порода", true),
                    ParameterUiState(2, "Цвет", false),
                    ParameterUiState(3, "Возраст", true),
                )
            )
        )

        val continueButtonState = ContinueButtonState(true)

        DetailsStep(
            parametersUiState = parametersUiState,
            continueButtonState = continueButtonState,
            description = input.value,
            setDescription = { input.value = it },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailsStepPLoadingreview() {
    ZveronTheme {
        val input = remember {
            mutableStateOf("")
        }

        val parametersUiState = ParametersUiState.Loading

        val continueButtonState = ContinueButtonState(false)

        DetailsStep(
            parametersUiState = parametersUiState,
            continueButtonState = continueButtonState,
            description = input.value,
            setDescription = { input.value = it },
            modifier = Modifier.fillMaxSize(),
        )
    }
}