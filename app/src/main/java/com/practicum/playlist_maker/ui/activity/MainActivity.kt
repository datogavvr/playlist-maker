package com.practicum.playlist_maker.ui.activity

import PlaylistHost
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.ui.theme.PlaylistmakerTheme

class MainActivity : ComponentActivity() {
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

@Composable
fun GenericButton(
    icon: ImageVector,
    nameButton: String,
    onClick: () -> Unit,
    showToast: Boolean = false
) {
    val context = LocalContext.current

    Button(
        onClick = {
            if (showToast) {
                Toast.makeText(context, "Нажата кнопка \"$nameButton\"", Toast.LENGTH_SHORT).show()
            }
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.button_height)),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = nameButton,
                modifier = Modifier.size(dimensionResource(R.dimen.icon_main))
            )

            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.space_12)))

            Text(
                text = nameButton,
                textAlign = TextAlign.Start,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = stringResource(R.string.arrowForward),
                modifier = Modifier.size(dimensionResource(R.dimen.icon_arrow)),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun MainScreen(
    onSearchClick: () -> Unit = {},
    onPlaylistsClick: () -> Unit = {},
    onFavoritesClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.header_height))
                    .padding(dimensionResource(R.dimen.padding_12)),
                text = stringResource(R.string.app_name),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(
                            topStart = dimensionResource(R.dimen.corner_16),
                            topEnd = dimensionResource(R.dimen.corner_16)
                        )
                    )
            ) {
                Column(
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_16))
                ) {
                    GenericButton(
                        icon = Icons.Filled.Search,
                        nameButton = stringResource(R.string.search),
                        onClick = onSearchClick
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_16)))

                    GenericButton(
                        icon = Icons.Filled.LibraryMusic,
                        nameButton = stringResource(R.string.playlists),
                        onClick = onPlaylistsClick
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_16)))

                    GenericButton(
                        icon = Icons.Filled.FavoriteBorder,
                        nameButton = stringResource(R.string.favorite),
                        onClick = onFavoritesClick,
                        showToast = true
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_16)))

                    GenericButton(
                        icon = Icons.Filled.Settings,
                        nameButton = stringResource(R.string.settings),
                        onClick = onSettingsClick
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    PlaylistmakerTheme {
        MainScreen()
    }
}

@Preview
@Composable
fun DarkMainScreenPreview() {
    PlaylistmakerTheme(darkTheme = true) {
        MainScreen()
    }
}