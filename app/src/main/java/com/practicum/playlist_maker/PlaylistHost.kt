import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.practicum.playlist_maker.MainScreen
import com.practicum.playlist_maker.Screen
import com.practicum.playlist_maker.SearchScreen
import com.practicum.playlist_maker.SettingsScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlaylistHost(navController: NavHostController) {
    AnimatedNavHost(
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
            SearchScreen(onBack = { navController.popBackStack() })
        }

        // экран настроек
        composable(Screen.SETTINGS.name) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}