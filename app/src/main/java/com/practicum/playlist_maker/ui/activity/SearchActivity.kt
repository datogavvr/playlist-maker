package com.practicum.playlist_maker.ui.activity

import PlaylistHost
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.ui.SearchState
import com.practicum.playlist_maker.ui.SearchViewModel
import com.practicum.playlist_maker.data.network.Track
import com.practicum.playlist_maker.ui.theme.PlaylistmakerTheme

class SearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaylistmakerTheme {
                val navController = rememberNavController()
                PlaylistHost(navController = navController)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBack: () -> Unit = {},
    viewModel: SearchViewModel
) {
    var searchText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val screenState by viewModel.searchScreenState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.search),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(
                        width = 1.5.dp,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Поиск",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(onClick = {
                                keyboardController?.hide()
                                viewModel.search(searchText)
                            }),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    TextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text(
                                text = stringResource(R.string.search),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                keyboardController?.hide()
                                viewModel.search(searchText)
                            }
                        )
                    )

                    if (searchText.isNotEmpty()) {
                        IconButton(
                            onClick = { searchText = "" }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = "Очистить",
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            when (screenState) {
                is SearchState.Initial -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Здесь будут результаты поиска",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Введите запрос в поле выше",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                is SearchState.Searching -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is SearchState.Success -> {
                    val tracks = (screenState as SearchState.Success).list
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(tracks.size) { index ->
                            TrackListItem(track = tracks[index])
                        }
                    }
                }

                is SearchState.Fail -> {
                    val error = (screenState as SearchState.Fail).error
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Ошибка: $error", color = Color.Red)
                    }
                }
            }
        }
    }
}

@Composable
fun TrackListItem(track: Track) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_music),
            contentDescription = "Трек ${track.trackName}",
            modifier = Modifier
                .padding(vertical = 10.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
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
            contentDescription = "Выбрать данную композицию",
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}