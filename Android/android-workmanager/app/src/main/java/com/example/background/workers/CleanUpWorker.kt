package com.example.background.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.OUTPUT_PATH
import java.io.File

class CleanUpWorker constructor(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {
    private val TAG by lazy { SaveFileWorker::class.java.simpleName }

    override fun doWork(): Result {
        Log.e(TAG, "開始清檔")
        makeStatusNotification("clean up temp files...", applicationContext)
        sleep()

        return try {
            val outputDirectory = File(applicationContext.filesDir, OUTPUT_PATH)
            if (outputDirectory.exists()) {
                val entries = outputDirectory.listFiles()
                if (entries != null) {
                    for (entry in entries) {
                        val name = entry.name
                        if (name.isNotEmpty() && name.endsWith(".png")) {
                            val deleted = entry.delete()
                            Log.i(TAG, String.format("Deleted %s - %s", name, deleted))
                        }
                    }
                }
            }
            Result.SUCCESS
        } catch (exception: Exception) {
            Log.e(TAG, "Error cleaning up", exception)
            Result.FAILURE
        }
    }
}