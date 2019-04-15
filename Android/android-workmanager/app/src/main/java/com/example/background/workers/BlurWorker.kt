package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.work.Data
import androidx.work.RxWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.KEY_IMAGE_URI
import io.reactivex.Single
import java.lang.IllegalArgumentException

class BlurWorker constructor(context: Context, workerParameters: WorkerParameters) : RxWorker(context, workerParameters) {

    override fun createWork(): Single<Result> {
        return Single.fromCallable {
            makeStatusNotification("Blurring image", applicationContext)
            sleep()
            try {
                val originUri = inputData.getString(KEY_IMAGE_URI) ?: throw IllegalArgumentException("uri is null")

                val originBitmap = BitmapFactory.decodeStream(
                    applicationContext.contentResolver.openInputStream(Uri.parse(originUri))
                )
                val blurBitmap = blurBitmap(originBitmap, applicationContext)
                val blurBitmapUri = writeBitmapToFile(applicationContext, blurBitmap)
                makeStatusNotification("blurring:$blurBitmapUri", applicationContext)
                val outputData = Data.Builder()
                    .putString(KEY_IMAGE_URI, blurBitmapUri.toString())
                    .build()
                Result.success(outputData)
            } catch (error: Throwable) {
                Result.failure()
            }
        }
    }
}