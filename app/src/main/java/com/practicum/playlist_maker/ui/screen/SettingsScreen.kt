package com.practicum.playlist_maker.ui.screen

import PlaylistHost
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.practicum.playlist_maker.ui.theme.PlaylistmakerTheme
import androidx.core.net.toUri
import androidx.navigation.compose.rememberNavController
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.ui.theme.ThemeManager

class SettingsActivity : ComponentActivity() {
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
fun SettingsScreen(onBack: () -> Unit = {}) {
    val context = LocalContext.current
    val currentTheme = remember { mutableIntStateOf(ThemeManager.currentTheme) }
    val borderColor = MaterialTheme.colorScheme.surfaceVariant

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings),
                        fontSize = 18.sp,
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // карточка с настройками темы
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_16))
                    .border(
                        width = dimensionResource(R.dimen.settings_card_border_width),
                        color = borderColor,
                        shape = RoundedCornerShape(dimensionResource(R.dimen.settings_card_corner_radius))
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(dimensionResource(R.dimen.settings_padding_medium))
                ) {
                    Text(
                        text = stringResource(R.string.theme),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_12))
                    )

                    // три кнопки выбора темы в одну строку
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ThemeOptionButton(
                            text = stringResource(R.string.system),
                            isSelected = currentTheme.intValue == ThemeManager.THEME_SYSTEM,
                            onClick = {
                                ThemeManager.setTheme(context, ThemeManager.THEME_SYSTEM)
                                currentTheme.intValue = ThemeManager.THEME_SYSTEM
                            },
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.settings_spacing_small)))

                        ThemeOptionButton(
                            text = stringResource(R.string.light),
                            isSelected = currentTheme.intValue == ThemeManager.THEME_LIGHT,
                            onClick = {
                                ThemeManager.setTheme(context, ThemeManager.THEME_LIGHT)
                                currentTheme.intValue = ThemeManager.THEME_LIGHT
                            },
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.settings_spacing_small)))

                        ThemeOptionButton(
                            text = stringResource(R.string.dark),
                            isSelected = currentTheme.intValue == ThemeManager.THEME_DARK,
                            onClick = {
                                ThemeManager.setTheme(context, ThemeManager.THEME_DARK)
                                currentTheme.intValue = ThemeManager.THEME_DARK
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // кнопка "Поделиться приложением"
            SettingsActionButton(
                text = stringResource(R.string.share_app),
                rightIcon = Icons.Filled.Share,
                onClick = {
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT,context.getString(R.string.share_app_text)                        )
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_app)))
                }
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.settings_spacing_small)))

            // кнопка "Написать в техподдержку"
            SettingsActionButton(
                text = stringResource(R.string.contact_support),
                rightIcon = Icons.Filled.SupportAgent,
                onClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, context.getString(R.string.contact_support_link).toUri()))
                }
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.settings_spacing_small)))

            // кнопка "Пользовательское соглашение"
            SettingsActionButton(
                text = stringResource(R.string.user_agreement),
                rightIcon = Icons.AutoMirrored.Filled.ArrowForwardIos,
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, context.getString(R.string.user_agreement_file).toUri())
                    context.startActivity(intent)
                }
            )
        }
    }
}

// кнопки смены темы
@Composable
fun ThemeOptionButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
            contentColor = if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.theme_button_corner_radius)),
        contentPadding = PaddingValues(
            horizontal = dimensionResource(R.dimen.theme_button_padding_h),
            vertical = dimensionResource(R.dimen.theme_button_padding_v)
        )
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// обычные кнопки
@Composable
fun SettingsActionButton(
    text: String,
    rightIcon: ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.button_height))
            .padding(horizontal = dimensionResource(R.dimen.settings_padding_large)),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.theme_button_corner_radius))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Start,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurface
            )

            Icon(
                imageVector = rightIcon,
                contentDescription = text,
                modifier = Modifier.size(dimensionResource(R.dimen.settings_icon_size)),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
fun LightSettingsScreenPreview() {
    PlaylistmakerTheme {
        SettingsScreen()
    }
}

@Preview
@Composable
fun DarkSettingsScreenPreview() {
    PlaylistmakerTheme(darkTheme = true) {
        SettingsScreen()
    }
}