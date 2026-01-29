package com.practicum.playlist_maker.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.practicum.playlist_maker.R
import java.io.File
import androidx.core.net.toUri
import com.practicum.playlist_maker.ui.viewmodel.PlaylistViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun EditPlaylistScreen(
    onBack: () -> Unit,
    viewModel: PlaylistViewModel = viewModel()
) {
    val playlist = viewModel.playlist.collectAsState(initial = null).value
    val isLoading by viewModel.isLoading.collectAsState()

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    var title by remember { mutableStateOf(playlist?.playlistName ?: "") }
    var description by remember { mutableStateOf(playlist?.description ?: "") }
    var titleFocused by remember { mutableStateOf(false) }
    var descriptionFocused by remember { mutableStateOf(false) }
    var coverUri by remember { mutableStateOf<Uri?>(playlist?.coverUri?.toUri()) }
    var showImagePickerDialog by remember { mutableStateOf(false) }

    val isNameValid = title.isNotBlank() && title.length <= 50
    val isDescriptionValid = description.length <= 100
    val focusManager: FocusManager = LocalFocusManager.current

    val context = LocalContext.current

    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val path = saveUriToInternalStorage(context, it)
            coverUri = Uri.fromFile(File(path))
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success && tempCameraUri != null) {
            coverUri = tempCameraUri
        }
    }

    LaunchedEffect(cameraPermissionState.status) {
        if (cameraPermissionState.status.isGranted && tempCameraUri != null) {
            cameraLauncher.launch(tempCameraUri!!)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = playlist?.playlistName ?: "",
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
                    if (isNameValid && isDescriptionValid) {
                        viewModel.updatePlaylist(
                            playlistId = playlist!!.id,
                            playlistName = title.trim(),
                            description = description.trim(),
                            coverUri = coverUri?.toString()
                        )
                        onBack()
                    } else if (!isNameValid) {
                        Toast.makeText(context, R.string.title_hint, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, R.string.description_hint, Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .padding(bottom = dimensionResource(R.dimen.padding_16))
                    .padding(horizontal = dimensionResource(R.dimen.padding_16))
                    .height(dimensionResource(R.dimen.create_playlist_cover_icon_height))
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isNameValid && isDescriptionValid)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = stringResource(R.string.save),
                    fontSize = 16.sp,
                    color = if (isNameValid && isDescriptionValid)
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
                        contentDescription = stringResource(R.string.cover),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            LabeledTextField(
                value = title,
                onValueChange = { title = it.trimStart() },
                label = stringResource(R.string.title),
                isError = title.isNotBlank() && !isNameValid,
                isFocused = titleFocused,
                onFocusChange = { titleFocused = it },
            )

            LabeledTextField(
                value = description,
                onValueChange = { description = it.trimStart() },
                label = stringResource(R.string.description),
                isError = !isDescriptionValid,
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
                                tempCameraUri = createImageUri(context)
                                if (cameraPermissionState.status.isGranted) {
                                    cameraLauncher.launch(tempCameraUri!!)
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
            confirmButton = {}
        )
    }
}