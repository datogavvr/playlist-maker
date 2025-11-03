package com.practicum.playlist_maker

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.playlist_maker.ui.theme.PlaylistmakerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlaylistmakerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Первый маленький бокс сверху

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(color = MaterialTheme.colorScheme.primary)
                    .padding(12.dp),
                text = "Playlist maker",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )


        // Второй бокс на всю оставшуюся часть экрана
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Занимает все оставшееся пространство
                .background(Color.White)
        ) {


            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                // Кнопка Поиск
                Button(
                    onClick = {
                        Toast.makeText(context, "Нажата кнопка \"Поиск\"", Toast.LENGTH_SHORT)
                            .show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Поиск")
                }

                // Кнопка Плейлисты
                Button(
                    onClick = {
                        Toast.makeText(context, "Нажата кнопка \"Плейлисты\"", Toast.LENGTH_SHORT)
                            .show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Плейлисты")
                }

                // Кнопка Избранное
                Button(
                    onClick = {
                        Toast.makeText(context, "Нажата кнопка \"Избранное\"", Toast.LENGTH_SHORT)
                            .show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Избранное")
                }

                // Кнопка Настройки
                Button(
                    onClick = {
                        Toast.makeText(context, "Нажата кнопка \"Настройки\"", Toast.LENGTH_SHORT)
                            .show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Настройки")
                }
            }
        }
    }
}

@Preview(name = "portrait", showSystemUi = true)
@Composable
fun MainScreenPreview() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        MainScreen(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}
