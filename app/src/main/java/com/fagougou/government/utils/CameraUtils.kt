package com.fagougou.government.utils

import android.content.Context
import android.net.Uri
import android.util.Size
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import timber.log.Timber
import java.io.File

object CameraUtils {

    private val imageCapture = ImageCapture.Builder()
        .setTargetResolution(Size(2592, 1944))
        .build()

    fun initCamera(context: Context, previewView: PreviewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()
                // Bind use cases to camera
                (context as LifecycleOwner?)?.let {
                    cameraProvider.bindToLifecycle(
                        it,
                        cameraSelector,
                        preview,
                        imageCapture,
                    )
                }
                preview.setSurfaceProvider(previewView.surfaceProvider)
            } catch (e: Exception) {
                Timber.e(e)
            }

        }, ContextCompat.getMainExecutor(context))
    }

    fun takePhoto(context: Context):String {
        val photoFile=File(context.cacheDir, System.currentTimeMillis().toString() + ".jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(e: ImageCaptureException) {
                    Timber.e(e)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                }
            }
        )

        return photoFile.path
    }
}