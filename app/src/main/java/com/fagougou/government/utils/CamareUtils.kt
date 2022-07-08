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


object CamareUtils {
    var preview: Preview? = null
    var imageCapture: ImageCapture? = null
    fun initCamera(context: Context, previewView: PreviewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            // Preview
            preview = Preview.Builder().build()
            imageCapture = ImageCapture.Builder()
                .setTargetResolution(Size(2592, 1944))
                .build()//拍照用例配置
            // Select back camera
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


                preview?.setSurfaceProvider(previewView.surfaceProvider)

            } catch (e: Exception) {
                Timber.e(e)
            }

        }, ContextCompat.getMainExecutor(context))
    }



    fun takePhoto(context: Context):String {

        // Create timestamped output file to hold the image

        val photoFile=File(context.cacheDir, System.currentTimeMillis().toString() + ".jpg")

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()


        imageCapture?.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                }
            })

        return photoFile.path
    }
}