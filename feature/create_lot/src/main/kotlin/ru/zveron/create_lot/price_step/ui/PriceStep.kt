package ru.zveron.create_lot.price_step.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.create_lot.R
import ru.zveron.design.components.ActionButton
import ru.zveron.design.selectors.CheckboxSelector
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.design.R as DesignR

@Composable
internal fun PriceStep(
    price: Int?,
    setPrice: (Int?) -> Unit,
    isNegotiated: Boolean,
    onNegotiatedClicked: () -> Unit,
    canContinue: Boolean,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onContinueClick: () -> Unit = {},
) {
    Column(
        modifier = modifier.padding(top = 16.dp).windowInsetsPadding(WindowInsets.ime),
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                painterResource(DesignR.drawable.ic_back),
                null,
            )
        }

        Spacer(Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.price_header),
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
            text = stringResource(R.string.price_input_label),
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
            ),
            modifier = Modifier.padding(start = 16.dp),
        )

        Spacer(Modifier.height(6.dp))

        val priceSetter = remember {
            { input: String ->
                val formattedInout = input.filter { it.isDigit() }
                if (formattedInout.isBlank()) {
                    setPrice(null)
                } else {
                    setPrice(formattedInout.toInt())
                }
            }
        }

        TextField(
            value = price?.toString().orEmpty(),
            onValueChange = priceSetter,
            enabled = !isNegotiated,
            visualTransformation = PriceVisualTransformation(stringResource(R.string.price_input_format)),
            placeholder = { Text(stringResource(R.string.price_input_placeholder)) },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.surface,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 34.dp),
        )

        Spacer(Modifier.height(6.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 18.dp),
        ) {
            CheckboxSelector(selected = isNegotiated, onClick = onNegotiatedClicked)

            Text(
                text = stringResource(R.string.negotiate_price),
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                )
            )
        }

        Spacer(Modifier.weight(1f))

        ActionButton(
            enabled = canContinue,
            onClick = onContinueClick,
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
}

@Preview(showBackground = true)
@Composable
private fun PriceStepPreview() {
    ZveronTheme {
        val price = remember { mutableStateOf<Int?>(null) }
        val isNegotiated = remember { mutableStateOf(false) }


        PriceStep(
            price = price.value,
            setPrice = { price.value = it },
            isNegotiated = isNegotiated.value,
            onNegotiatedClicked = { isNegotiated.value = !isNegotiated.value },
            canContinue = isNegotiated.value || (price.value ?: 0) > 0,
            modifier = Modifier.fillMaxSize(),
        )
    }
}