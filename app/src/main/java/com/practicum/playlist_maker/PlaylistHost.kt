import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.practicum.playlist_maker.ui.activity.MainScreen
import com.practicum.playlist_maker.Screen
import com.practicum.playlist_maker.ui.SearchViewModel
import com.practicum.playlist_maker.ui.activity.SearchScreen
import com.practicum.playlist_maker.ui.activity.SettingsScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlaylistHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.MAIN.name,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        }
    ) {
        // главный экран
        composable(Screen.MAIN.name) {
            MainScreen(
                onSearchClick = { navController.navigate(Screen.SEARCH.name) },
                onSettingsClick = { navController.navigate(Screen.SETTINGS.name) }
            )
        }

        // экран поиска
        composable(Screen.SEARCH.name) {
            val searchViewModel: SearchViewModel = viewModel(factory = SearchViewModel.getViewModelFactory())
            SearchScreen(onBack = { navController.popBackStack() }, viewModel = searchViewModel)
        }

        // экран настроек
        composable(Screen.SETTINGS.name) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}