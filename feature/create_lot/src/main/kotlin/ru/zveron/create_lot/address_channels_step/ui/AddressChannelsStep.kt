package ru.zveron.create_lot.address_channels_step.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
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
import ru.zveron.design.selectors.CheckboxSelector
import ru.zveron.design.shimmering.shimmeringBackground
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.design.R as DesignR

@Composable
internal fun AddressChannelsStep(
    address: String,
    setAddress: (String) -> Unit,
    addressChannelUiState: AddressChannelsStepUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onAddressClick: () -> Unit = {},
    onCommunicationChannelsClick: (CommunicationChannelUiState) -> Unit = {},
    onContinueButtonClick: () -> Unit = {},
) {
    Column(modifier = modifier.padding(top = 16.dp)) {
        IconButton(onClick = onBackClick) {
            Icon(
                painterResource(DesignR.drawable.ic_back),
                null,
            )
        }

        Spacer(Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.address_title),
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
            text = stringResource(R.string.address_input_label),
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
            ),
            modifier = Modifier.padding(start = 16.dp),
        )

        Spacer(Modifier.height(6.dp))

        // TODO: use this when integrating some maps sdk
//        Box(
//            contentAlignment = Alignment.CenterStart,
//            modifier = Modifier
//                .padding(start = 16.dp, end = 34.dp)
//                .fillMaxWidth()
//                .height(48.dp)
//                .clip(RoundedCornerShape(10.dp))
//                .background(MaterialTheme.colors.surface)
//                .clickable(
//                    onClick = onAddressClick,
//                    role = Role.Button,
//                    onClickLabel = stringResource(R.string.address_title),
//                )
//        ) {
//            Text(
//                text = addressChannelUiState.addressTitle
//                    ?: stringResource(R.string.address_input_placeholder),
//                style = TextStyle(
//                    fontWeight = FontWeight.Normal,
//                    fontSize = 16.sp,
//                    color = gray3,
//                ),
//                modifier = Modifier.padding(start = 16.dp),
//            )
//        }
        TextField(
            value = address,
            onValueChange = setAddress,
            placeholder = { Text(stringResource(R.string.address_input_placeholder)) },
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
                .padding(start = 16.dp, end = 34.dp),
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.communication_channels_title),
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                lineHeight = 34.sp,
                letterSpacing = (0.36).sp,
            ),
            modifier = Modifier.padding(start = 16.dp),
        )

        Spacer(Modifier.height(16.dp))

        addressChannelUiState.channelsState.forEach { (selected, channel) ->
            key(channel) {
                val clicker = remember {
                    { onCommunicationChannelsClick.invoke(channel) }
                }

                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CheckboxSelector(selected = selected, onClick = clicker)

                    Text(
                        text = stringResource(channel.resId),
                        style = TextStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                        ),
                    )
                }

                Spacer(Modifier.height(16.dp))
            }
        }

        Spacer(Modifier.weight(1f))

        ActionButton(
            enabled = addressChannelUiState.continueButtonState.enabled,
            onClick = onContinueButtonClick,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 26.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.continue_button_title),
                style = MaterialTheme.typography.body1,
            )

            if (addressChannelUiState.continueButtonState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shimmeringBackground(this.maxWidth)
                )
            }
        }
    }

}

@Composable
@Preview(showBackground = true)
private fun AddressChannelsStepPreview() {
    ZveronTheme {
        val address = remember {
            mutableStateOf("")
        }

        val state = AddressChannelsStepUiState(
            addressTitle = "ул. Пушкина, дом Колотушкина",
            channelsState = CommunicationChannelUiState.values().map { false to it },
            continueButtonState = ContinueButtonState(isLoading = true, enabled = false),
        )

        AddressChannelsStep(
            address = address.value,
            setAddress = { address.value = it },
            addressChannelUiState = state,
            modifier = Modifier.fillMaxSize()
        )
    }
}