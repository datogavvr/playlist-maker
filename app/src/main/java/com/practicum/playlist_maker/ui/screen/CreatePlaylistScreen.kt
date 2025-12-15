package com.practicum.playlist_maker.ui.screen

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.ui.viewmodel.PlaylistsViewModel
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CreatePlaylistScreen(
    onBack: () -> Unit,
    viewModel: PlaylistsViewModel = viewModel()
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var titleFocused by remember { mutableStateOf(false) }
    var descriptionFocused by remember { mutableStateOf(false) }
    var coverUri by remember { mutableStateOf<Uri?>(null) }
    var showImagePickerDialog by remember { mutableStateOf(false) }

    val isCreateEnabled = title.isNotBlank()
    val focusManager: FocusManager = LocalFocusManager.current

    val context = LocalContext.current

    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) coverUri = uri
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            coverUri = saveBitmapToCache(context, it)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.new_playlist),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.arrowBack)
                        )
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    if (isCreateEnabled) {
                        viewModel.createNewPlayList(
                            playlistName = title,
                            description = description,
                            coverUri = coverUri?.toString()
                        )
                        onBack()
                    } else {
                        Toast.makeText(context, R.string.title_hint, Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .padding(bottom = dimensionResource(R.dimen.padding_16))
                    .padding(horizontal = dimensionResource(R.dimen.padding_16))
                    .height(dimensionResource(R.dimen.create_playlist_cover_icon_height))
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCreateEnabled)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = stringResource(R.string.create),
                    fontSize = 16.sp,
                    color = if (isCreateEnabled)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                        titleFocused = false
                        descriptionFocused = false
                    })
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(top = dimensionResource(R.dimen.padding_24))
                    .size(dimensionResource(R.dimen.create_playlist_cover_image_size))
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.padding_12)))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        showImagePickerDialog = true
                    },
                contentAlignment = Alignment.Center
            ) {
                if (coverUri == null) {
                    Icon(
                        imageVector = Icons.Filled.AddPhotoAlternate,
                        contentDescription = stringResource(R.string.add_cover),
                        modifier = Modifier.size(dimensionResource(R.dimen.padding_72)),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Image(
                        painter = rememberAsyncImagePainter(coverUri),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            LabeledTextField(
                value = title,
                onValueChange = { title = it },
                label = stringResource(R.string.title),
                isFocused = titleFocused,
                onFocusChange = { titleFocused = it },
            )

            LabeledTextField(
                value = description,
                onValueChange = { description = it },
                label = stringResource(R.string.description),
                isFocused = descriptionFocused,
                onFocusChange = { descriptionFocused = it },
                singleLine = false
            )
        }
    }

    if (showImagePickerDialog) {
        AlertDialog(
            onDismissRequest = { showImagePickerDialog = false },
            title = { Text(stringResource(R.string.choose_cover_source)) },
            text = {
                Column {
                    Text(
                        text = stringResource(R.string.gallery),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showImagePickerDialog = false
                                galleryLauncher.launch("image/*")
                            }
                            .padding(dimensionResource(R.dimen.padding_12)),
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.height(dimensionResource(R.dimen.padding_8)))

                    Text(
                        text = stringResource(R.string.camera),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showImagePickerDialog = false
                                if (cameraPermissionState.status.isGranted) {
                                    cameraLauncher.launch(null)
                                } else {
                                    cameraPermissionState.launchPermissionRequest()
                                }
                            }
                            .padding(dimensionResource(R.dimen.padding_12)),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.background,
            confirmButton = {},
            dismissButton = {}
        )
    }

}

fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri {
    val file = File(context.cacheDir, "cover_${System.currentTimeMillis()}.jpg")
    val out = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
    out.flush()
    out.close()
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}

@Composable
fun LabeledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isFocused: Boolean,
    onFocusChange: (Boolean) -> Unit,
    singleLine: Boolean = true
) {
    val hasText = value.isNotEmpty()

    val borderColor =
        if (isFocused) MaterialTheme.colorScheme.surfaceVariant
        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.padding_16), vertical = dimensionResource(R.dimen.padding_12))
            .border(
                width = dimensionResource(R.dimen.width_1_5),
                color = borderColor,
                shape = RoundedCornerShape(dimensionResource(R.dimen.cover_corner_radius))
            )
            .padding(horizontal = dimensionResource(R.dimen.padding_12), vertical = dimensionResource(R.dimen.padding_4))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            AnimatedVisibility(
                visible = isFocused || hasText,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant ,
                )
            }

            TextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = singleLine,
                placeholder = {
                    if (!hasText && !isFocused) {
                        Text(
                            text = label,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { onFocusChange(it.isFocused) }
            )
        }
    }
}
