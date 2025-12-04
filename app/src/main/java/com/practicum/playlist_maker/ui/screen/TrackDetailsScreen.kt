package com.practicum.playlist_maker.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.data.network.Track
import com.practicum.playlist_maker.ui.theme.MyLightGreen
import com.practicum.playlist_maker.ui.viewmodel.TrackDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackDetailsScreen(
    trackId: Long,
    viewModel: TrackDetailsViewModel,
    onBack: () -> Unit
) {
    val trackFlow = viewModel.getTrack(trackId).collectAsState(initial = null)
    var track by remember { mutableStateOf(trackFlow.value) }

    val playlists by viewModel.getPlaylists().collectAsState(initial = emptyList())

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isSheetOpen by remember { mutableStateOf(false) }

    LaunchedEffect(trackFlow.value) {
        track = trackFlow.value
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.track_details),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.arrowBack),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        track?.let { t ->
            TrackDetailsContent(
                track = t,
                modifier = Modifier.padding(innerPadding),
                onAddToPlaylist = { isSheetOpen = true },
                onToggleFavorite = { newValue ->
                    track = track!!.copy(favorite = newValue)
                    viewModel.updateTrackFavoriteStatus(track!!.id, newValue)
                }
            )
        } ?: Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { isSheetOpen = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            Text(
                text = "Выберите плейлист",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )

            playlists.forEach { playlist ->

                // проверка, есть ли трек в этом плейлисте
                val trackInPlaylist = track?.playlistId?.contains(playlist.id) == true

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .clickable {
                            viewModel.addTrackToPlaylist(track, playlist.id)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Обложка
                    Image(
                        painter = painterResource(id = R.drawable.ic_music),
                        contentDescription = playlist.name,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    // Название
                    Text(
                        text = playlist.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        imageVector = if (trackInPlaylist)
                            Icons.Filled.CheckCircle
                        else
                            Icons.Filled.Add,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = if (trackInPlaylist)
                            MyLightGreen
                        else
                            Color.Gray
                        )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

}

@Composable
fun TrackDetailsContent(
    track: Track,
    modifier: Modifier = Modifier,
    onAddToPlaylist: () -> Unit = {},
    onToggleFavorite: (Boolean) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // обложка трека
        Image(
            painter = painterResource(R.drawable.ic_music),
            contentDescription = track.trackName,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(260.dp)
                .clip(RoundedCornerShape(dimensionResource(R.dimen.cover_corner_radius)))
        )

        Spacer(modifier = Modifier.height(20.dp))

        // название трека и исполнитель
        Text(
            text = track.trackName,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = track.artistName,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // кнопка "Добавить в плейлист"
            IconButton(
                onClick = onAddToPlaylist,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    imageVector = Icons.Filled.LibraryAdd,
                    contentDescription = stringResource(R.string.add_to_playlist),
                    tint = Color.White
                )
            }

            // кнопка "Добавить в избранное"
            IconButton(
                onClick = {
                    onToggleFavorite(!track.favorite)
                },
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
            ) {
                Icon(
                    imageVector = if (track.favorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = stringResource(R.string.add_to_favorite),
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        DetailRow(title = stringResource(R.string.duration), value = track.trackTime)
    }
}

@Composable
fun DetailRow(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Gray)
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}
