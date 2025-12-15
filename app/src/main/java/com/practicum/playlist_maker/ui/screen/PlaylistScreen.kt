package com.practicum.playlist_maker.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.data.network.Track
import com.practicum.playlist_maker.ui.viewmodel.PlaylistViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(
    modifier: Modifier = Modifier,
    playlistViewModel: PlaylistViewModel,
    onTrackClick: (Long) -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onShareClick: () -> Unit,
    onBack: () -> Unit
) {
    val playlist = playlistViewModel.playlist.collectAsState(initial = null).value
    val isLoading by playlistViewModel.isLoading.collectAsState()

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (playlist == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.playlist_not_found))
        }
        return
    }

    val tracks = remember(playlist.tracks) { playlist.tracks.asReversed() }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isSheetOpen by remember { mutableStateOf(false) }
    var trackToDelete by remember { mutableStateOf<Track?>(null) }

    val totalMinutes by playlistViewModel.totalMinutes.collectAsState()
    val trackCountText by playlistViewModel.trackCountText.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = playlist.playlistName,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    ) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.arrowBack),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { isSheetOpen = true }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = playlist.coverUri,
                contentDescription = stringResource(
                    R.string.track_template,
                    playlist.playlistName
                ),
                placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                error = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.medium_cover_size))
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.cover_corner_radius)))
            )


            Spacer(Modifier.height(dimensionResource(R.dimen.space_12)))

            Text(
                text = playlist.playlistName,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.padding_8)))

            Text(
                "$totalMinutes мин • $trackCountText",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.space_16)))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = dimensionResource(R.dimen.padding_24))
            ) {
                items(tracks, key = { it.id }) { track ->
                    PlaylistTrackItem(
                        track = track,
                        onClick = { onTrackClick(track.id) },
                        onLongClick = {
                            trackToDelete = track
                        }
                    )
                }
            }
        }
    }

    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { isSheetOpen = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_16))
            ) {
                Text(playlist.playlistName, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                Text(
                    "$totalMinutes мин • $trackCountText",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(dimensionResource(R.dimen.padding_20)))
                SheetButton(stringResource(R.string.share_playlist)) { isSheetOpen = false; onShareClick() }
                SheetButton(stringResource(R.string.edit_info)) { isSheetOpen = false; onEditClick() }
                SheetButton(stringResource(R.string.delete_playlist), color = Color.Red) { isSheetOpen = false; onDeleteClick() }
                Spacer(Modifier.height(dimensionResource(R.dimen.padding_20)))
            }
        }
    }

    if (trackToDelete != null) {
        AlertDialog(
            onDismissRequest = { trackToDelete = null },
            title = {
                Text(stringResource(R.string.delete_track_from_playlist))
            },
            text = {
                Text(
                    stringResource(R.string.delete_track_confirm, trackToDelete!!.trackName)
                )
            },
            containerColor = MaterialTheme.colorScheme.background,
            confirmButton = {
                TextButton(
                    onClick = {
                        playlistViewModel.deleteTrackFromPlaylist(trackToDelete!!.id)
                        trackToDelete = null
                    }
                ) {
                    Text(stringResource(R.string.delete), color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { trackToDelete = null }
                ) {
                    Text(stringResource(R.string.cancel), color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }
}

@Composable
fun PlaylistTrackItem(
    track: Track,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.track_item_height))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(vertical = dimensionResource(R.dimen.padding_5)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AsyncImage(
            model = track.image,
            contentDescription = stringResource(R.string.track_template, track.trackName),
            placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
            error = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier
                .padding(start = dimensionResource(R.dimen.padding_16))
                .size(dimensionResource(R.dimen.cover_size))
                .clip(RoundedCornerShape(dimensionResource(R.dimen.cover_corner_radius)))
        )

        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.space_12)))

        Column(
            modifier = Modifier.weight(0.85f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = track.trackName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = track.artistName,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                Text(
                    text = " · ${track.trackTime}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = stringResource(R.string.select_track, track.trackName),
            modifier = Modifier
                .size(dimensionResource(R.dimen.icon_20))
                .padding(end = dimensionResource(R.dimen.padding_16))
                .weight(0.15f),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun SheetButton(
    text: String,
    color: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit) {
    Text(
        text = text,
        color = color,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = dimensionResource(R.dimen.padding_12))
    )
}