package com.example.cameraapp

import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun Permission(modifier: Modifier = Modifier) {
    val permission = listOf(android.Manifest.permission.CAMERA)

    val isGranted = remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            isGranted.value = permissions[android.Manifest.permission.CAMERA] == true
        }
    )

    // Check initial permission status
    LaunchedEffect(Unit) {
        isGranted.value = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    if (isGranted.value) {
        CameraScreen()
    } else {
        // Show request permission button
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Camera permission is required.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                launcher.launch(permission.toTypedArray())
            }) {
                Text(text = "Request Permission")
            }
        }
    }
}



@Composable
fun CameraScreen(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView: PreviewView = remember {
        PreviewView(context)
    }

    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    val preview = Preview.Builder().build()
    val imageCapture = remember {
        ImageCapture.Builder().build()
    }

    LaunchedEffect(Unit) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        AndroidView(factory = {
            previewView
        }, modifier = Modifier.fillMaxSize())

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.7f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = {
                capturePhoto(imageCapture,context)
            },
                modifier = Modifier
                    .size(50.dp)
                    .background(color = Color.White, shape = CircleShape)
                    .padding(8.dp)
                    .background(color = Color.Red, shape = CircleShape)
            ) {
                Icon(imageVector = Icons.Filled.Call, contentDescription = "Camera")
            }
        }


    }


}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            continuation.resume(cameraProviderFuture.get())
        }, ContextCompat.getMainExecutor(this))
    }

private fun capturePhoto(imageCapture: ImageCapture,context: Context) {

    val name = "MyCamera_${System.currentTimeMillis()}.jpg"

    val contentValue = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME,name)
        put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg")
        put(MediaStore.MediaColumns.RELATIVE_PATH,"Pictures/Camerax-Image")
    }

    val outputOptions = ImageCapture.OutputFileOptions.Builder(
        context.contentResolver,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValue
    ).build()

    imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(context), object : ImageCapture.OnImageSavedCallback{
        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            Toast.makeText(context,"Image Saved ", Toast.LENGTH_SHORT).show()
        }

        override fun onError(exception: ImageCaptureException) {
            Toast.makeText(context,exception.message.toString(), Toast.LENGTH_SHORT).show()
        }

    })

}