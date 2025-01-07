package com.example.navhost

import Screen.PetViewModel
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vetapp.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.storage.storage
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.placeholder.placeholder.PlaceholderPlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream



@Composable
fun EditPetScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    viewModel: PetViewModel,
    petID: String,
) {


    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate(route = "login")
            else -> Unit
        }
    }


    val petData = viewModel.PetListViewState.value.find { it.ID == petID }

    var petName by remember { mutableStateOf(petData?.name ?: "") }
    var petAge by remember { mutableStateOf(petData?.age ?: "") }
    var petType by remember { mutableStateOf(petData?.type ?: "") }



    var selectedImage by remember { mutableStateOf<Uri?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImage = uri }
    )


    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
        bitmap?.let {
            // Convert bitmap to byte array
            val baos = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageData = baos.toByteArray()

            // Save to gallery
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.TITLE, "photo_${System.currentTimeMillis()}")
                put(MediaStore.Images.Media.DISPLAY_NAME, "photo_${System.currentTimeMillis()}.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }

            val imageUri =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            imageUri?.let { uri ->
                resolver.openOutputStream(uri).use { outputStream ->
                    outputStream?.write(imageData)
                }
            }
        }
    }



    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        TextField(value = petName, onValueChange = { petName = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "age") },
            placeholder = { Text(petName) }
        )
        TextField(value = petAge, onValueChange = { petAge = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "age") },
            placeholder = { Text(petAge) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number // Ustaw klawiaturę na numeryczną
            ))
        TextField(value = petType, onValueChange = { petType = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "type") },
            placeholder = { Text(petType) })
        GlideImage(
            imageModel = {selectedImage ?: petData?.Img}, modifier = modifier.size(128.dp), component = rememberImageComponent {
                +PlaceholderPlugin.Failure(
                    painterResource(id = R.drawable.doges)
                )
            }
        )

        Button(onClick =
        {

            val petImgString = selectedImage.toString()
            val userID = Firebase.auth.currentUser?.uid.toString()
            viewModel.savePet(petName, petID, petAge, petType, userID, petImgString)
        }
        ) {
            Text(text = "save edit")


        }

        Button(onClick = {
            photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))


        }) {
            Text(text = "Pick from gallery")
        }

        Button(onClick = {
            cameraLauncher.launch()

        }) {
            Text(text = "Capture Photo")
        }

    }

}