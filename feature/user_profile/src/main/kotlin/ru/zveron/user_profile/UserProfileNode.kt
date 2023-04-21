package ru.zveron.user_profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.zveron.appyx.viewmodel.ViewModelNode
import ru.zveron.user_profile.ui.UserProfile
import ru.zveron.user_profile.ui.UserProfileViewModel

class UserProfileNode(
    buildContext: BuildContext,
    private val params: UserProfileParams,
    private val component: UserProfileComponent = UserProfileComponent(),
): ViewModelNode(
    buildContext = buildContext,
    plugins = listOf(component),
) {
    @Composable
    override fun View(modifier: Modifier) {
        val viewModel = koinViewModel<UserProfileViewModel>(
            scope = component.scope,
            viewModelStoreOwner = this,
            parameters = { parametersOf(params) },
        )

        val uiState = viewModel.uiState.collectAsState()

        UserProfile(
            uiState = uiState.value,
            modifier = modifier,
            onBackClick = ::navigateUp,
            onLotClick = viewModel::onLotClick,
            onLotLikeClick = viewModel::onLotLikeClick,
            onTabClick = viewModel::onTabClick,
            onReviewsClick = viewModel::onReviewsClick,
        )
    }
}