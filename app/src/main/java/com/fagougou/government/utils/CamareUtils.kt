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
import org.apache.pdfbox.multipdf.PDFMergerUtility
import java.io.File


object CamareUtils {

    private const val TAG = "CameraXBasic"

    var preview: Preview? = null

    var imageCapture: ImageCapture? = null


    fun initCamera(context: Context, previewView: PreviewView) {

        var cameraProviderFuture = ProcessCameraProvider.getInstance(context)

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

            } catch (exc: Exception) {
                Log.e("TAG", "Use case binding failed", exc)
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
                    val msg = "Photo capture succeeded: $savedUri"
                    Log.d(TAG, msg)


                }
            })

        return photoFile.path
    }


    @Throws(java.lang.Exception::class)
    fun MergePdf2(listfile:List<String>,path: String) {
        //pdf合并工具类
        val mergePdf = PDFMergerUtility()

        listfile.forEach(){
            mergePdf.addSource(it)
        }
        //设置合并生成pdf文件名称
        mergePdf.destinationFileName = path
        //合并pdf
        mergePdf.mergeDocuments()
    }
}