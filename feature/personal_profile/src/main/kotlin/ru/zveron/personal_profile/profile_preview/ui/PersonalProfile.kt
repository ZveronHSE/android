package ru.zveron.personal_profile.profile_preview.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.design.components.ActionButton
import ru.zveron.design.components.RegularButton
import ru.zveron.design.components.Stars
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.design.theme.gray3
import ru.zveron.personal_profile.R
import ru.zveron.design.R as DesignR

private const val BOTTOM_BAR_HEIGHT = 98

@Composable
internal fun PersonalProfile(
    uiState: PersonalProfileUiState,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        PersonalProfileUiState.Loading -> PersonalProfileLoading(modifier = modifier)
        is PersonalProfileUiState.Success -> PersonalProfileSuccess(
            uiState = uiState,
            modifier = modifier,
        )
    }
}

@Composable
private fun PersonalProfileLoading(
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth(),
    ) {
        Spacer(Modifier.weight(1f))

        CircularProgressIndicator()

        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun PersonalProfileSuccess(
    uiState: PersonalProfileUiState.Success,
    modifier: Modifier = Modifier,
    onEditProfileClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onDeleteProfileClick: () -> Unit = {},
) {
    Column(
        modifier = modifier.padding(top = 16.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.personal_profile_header),
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    lineHeight = (27.6).sp,
                    letterSpacing = (-0.69).sp,
                ),
            )
        }

        Spacer(Modifier.height(40.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top,
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            ZveronImage(
                zveronImage = uiState.avatar,
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape),
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = uiState.displayName,
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                        lineHeight = 20.sp,
                        letterSpacing = (-0.5).sp,
                    )
                )

                val displayStars = uiState.rating.toInt()
                Stars(
                    currentStars = displayStars,
                    maxStars = 5,
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        RegularButton(
            onClick = onEditProfileClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Text(
                text = stringResource(R.string.personal_profile_edit),
                style = MaterialTheme.typography.body1,
            )
        }

        Spacer(Modifier.weight(1f))

        ActionButton(
            onClick = onLogoutClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Text(
                text = stringResource(R.string.personal_profile_logout),
                style = MaterialTheme.typography.body1,
            )
        }

        TextButton(
            onClick = onDeleteProfileClick,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = (BOTTOM_BAR_HEIGHT + 24).dp),
            colors = ButtonDefaults.textButtonColors(
                contentColor = gray3,
            )
        ) {
            Text(
                stringResource(R.string.personal_profile_delete_account),
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                ),
            )
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFF9F9F9)
private fun PersonalProfilePreview() {
    ZveronTheme {
        val state = PersonalProfileUiState.Success(
            avatar = ZveronImage.ResourceImage(DesignR.drawable.ic_no_avatar),
            displayName = "Егор Шпак",
            rating = 4.2,
        )
        PersonalProfile(
            uiState = state,
            modifier = Modifier.fillMaxSize(),
        )
    }
}