import android.widget.Toast
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.practicum.playlist_maker.ui.activity.MainScreen
import com.practicum.playlist_maker.ui.screen.CreatePlaylistScreen
import com.practicum.playlist_maker.ui.screen.PlaylistsScreen
import com.practicum.playlist_maker.ui.screen.Screen
import com.practicum.playlist_maker.ui.viewmodel.SearchViewModel
import com.practicum.playlist_maker.ui.screen.SearchScreen
import com.practicum.playlist_maker.ui.screen.SettingsScreen
import com.practicum.playlist_maker.ui.screen.TrackDetailsScreen
import com.practicum.playlist_maker.ui.viewmodel.PlaylistsViewModel
import com.practicum.playlist_maker.ui.viewmodel.TrackDetailsViewModel

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
                onPlaylistsClick = {navController.navigate(Screen.PLAYLISTS.name) },
                onSettingsClick = { navController.navigate(Screen.SETTINGS.name) }
            )
        }

        // экран поиска
        composable(Screen.SEARCH.name) {
            val searchViewModel: SearchViewModel = viewModel(factory = SearchViewModel.getViewModelFactory())
            SearchScreen(
                onBack = { navController.popBackStack() },
                viewModel = searchViewModel,
                onTrackClick = { trackId ->
                    navController.navigate("${Screen.TRACK_DETAILS.name}/$trackId")
                }
            )
        }

        // экран деталей трека
        composable(
            route = "${Screen.TRACK_DETAILS.name}/{trackId}"
        ) { backStack ->
            val id = backStack.arguments?.getString("trackId")?.toLong() ?: 0L
            val vm: TrackDetailsViewModel = viewModel()
            TrackDetailsScreen(
                trackId = id,
                viewModel = vm,
                onBack = { navController.popBackStack() }
            )
        }

        // экран плейлистов
        composable(Screen.PLAYLISTS.name) {
            val playlistsViewModel: PlaylistsViewModel = viewModel()
            val context = LocalContext.current
            PlaylistsScreen(
                viewModel = playlistsViewModel,
                addNewPlaylist = { navController.navigate(Screen.CREATE_PLAYLIST.name) },
                onPlaylistClick = { playlistId ->
                    Toast.makeText(context, "Плейлист $playlistId", Toast.LENGTH_SHORT).show()
//                    navController.navigate("${Screen.PLAYLIST_DETAILS.name}/$playlistId")
                },
                onBack = { navController.popBackStack() },
            )
        }

        // экран создания плейлиста
        composable(Screen.CREATE_PLAYLIST.name) {
            CreatePlaylistScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // экран избранного
        composable(Screen.FAVORITES.name) {
        }

        // экран настроек
        composable(Screen.SETTINGS.name) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}