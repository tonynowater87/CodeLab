package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.work.Data
import androidx.work.RxWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.KEY_IMAGE_URI
import io.reactivex.Single
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SaveFileWorker constructor(context: Context, workerParameters: WorkerParameters) : RxWorker(context, workerParameters) {

    private val TAG by lazy { SaveFileWorker::class.java.simpleName }
    private val Title = "Blurred Image"
    private val dateFormatter = SimpleDateFormat(
            "yyyy.MM.dd 'at' HH:mm:ss z",
            Locale.getDefault()
    )

    override fun createWork(): Single<Result> {
        return Single.fromCallable {
            // Makes a notification when the work starts and slows down the work so that
            // it's easier to see each WorkRequest start, even on emulated devices
            makeStatusNotification("Saving image", applicationContext)
            sleep()

            val resolver = applicationContext.contentResolver
            try {
                val resourceUri = inputData.getString(KEY_IMAGE_URI)
                val bitmap = BitmapFactory.decodeStream(
                    resolver.openInputStream(Uri.parse(resourceUri)))
                val imageUrl = MediaStore.Images.Media.insertImage(
                    resolver, bitmap, Title, dateFormatter.format(Date()))
                if (!imageUrl.isNullOrEmpty()) {
                    val output = Data.Builder()
                        .putString(KEY_IMAGE_URI, imageUrl)
                        .build()
                    Log.e(TAG, "存檔成功")
                    Result.success(output)
                } else {
                    Log.e(TAG, "Writing to MediaStore failed")
                    Result.failure()
                }
            } catch (exception: Exception) {
                Log.e(TAG, "Unable to save image to Gallery", exception)
                Result.failure()
            }
        }
    }
}