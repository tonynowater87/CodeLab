package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.KEY_IMAGE_URI
import java.lang.IllegalArgumentException

class BlurWorker constructor(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {
    override fun doWork(): Result {
        makeStatusNotification("Blurring image", applicationContext)
        sleep()

        return try {
            val originUri = inputData.getString(KEY_IMAGE_URI) ?: throw IllegalArgumentException("uri is null")

            val originBitmap = BitmapFactory.decodeStream(
                    applicationContext.contentResolver.openInputStream(Uri.parse(originUri)))
            val blurBitmap = blurBitmap(originBitmap, applicationContext)
            val blurBitmapUri = writeBitmapToFile(applicationContext, blurBitmap)
            makeStatusNotification("blurring:$blurBitmapUri", applicationContext)

            outputData = Data.Builder()
                    .putString(KEY_IMAGE_URI, blurBitmapUri.toString())
                    .build()
            Result.SUCCESS
        } catch (error: Throwable) {
            Result.FAILURE
        }
    }
}