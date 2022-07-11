package com.fagougou.government.utils


import android.content.Context
import android.net.Uri
import android.util.Log
import android.util.Size
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.fagougou.government.model.ContractFolder
import com.iflytek.cloud.SynthesizerListener

import java.io.File


object CameraUtils {

    private const val TAG = "CameraXBasic"


    private val imageCapture = ImageCapture.Builder()
        .setTargetResolution(Size(2592, 1944))
        .build()


    var photoFile:File?=null

    fun initCamera(context: Context, previewView: PreviewView) {

        var cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val preview = Preview.Builder().build()

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            // Preview


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


                preview.setSurfaceProvider(previewView.surfaceProvider)

            } catch (exc: Exception) {
                Log.e("TAG", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(context))
    }



    fun takePhoto(context: Context) {

        // Create timestamped output file to hold the image

        photoFile=File(context.cacheDir, System.currentTimeMillis().toString() + ".jpg")

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile!!).build()


        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            ImgAddCallback)

    }

     var ImgAddCallback: ImageCapture.OnImageSavedCallback = object : ImageCapture.OnImageSavedCallback {
        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {


        }

        override fun onError(exception: ImageCaptureException) {

        }

    }


}