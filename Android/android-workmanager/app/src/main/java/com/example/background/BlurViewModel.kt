/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.background

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkStatus
import com.example.background.workers.BlurWorker
import com.example.background.workers.CleanUpWorker
import com.example.background.workers.SaveFileWorker


class BlurViewModel : ViewModel() {

    private val mWorkManager = WorkManager.getInstance()

    internal var imageUri: Uri? = null
    internal var outputUri: Uri? = null

    internal val outputStatus: LiveData<List<WorkStatus>>

    init {
        outputStatus = mWorkManager.getStatusesByTagLiveData(TAG_OUTPUT)
    }

    private fun uriOrNull(uriString: String?): Uri? {
        return if (!uriString.isNullOrEmpty()) {
            Uri.parse(uriString)
        } else {
            null
        }
    }

    /**
     * Setters
     */
    internal fun setImageUri(uri: String?) {
        imageUri = uriOrNull(uri)
    }

    internal fun setOutputUri(outputImageUri: String?) {
        outputUri = uriOrNull(outputImageUri)
    }

    internal fun cancleWork() {
        mWorkManager.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)
    }

    /**
     * 使用beginUniqueWork可以指定重覆發送request時的策略
     *
     * ExistingWorkPolicy.REPLACE -> 會將舊的Request刪除，然後新的Request重新執行
     *
     * ExistingWorkPolicy.KEEP -> 繼續執行舊的Request，新的Request不會執行
     *
     * ExistingWorkPolicy.APPEND -> 所有的Request都會執行到, 一直APPEND在後面
     */
    fun applyBlur(blurLevel: Int) {
        var continuation = mWorkManager.beginUniqueWork(IMAGE_MANIPULATION_WORK_NAME
                                                        , ExistingWorkPolicy.REPLACE
                                                        , OneTimeWorkRequest.Builder(CleanUpWorker::class.java).build())

        for (i in 0 until blurLevel) {

            val blurRequest = OneTimeWorkRequest.Builder(BlurWorker::class.java)

            if (i == 0) {
                blurRequest.setInputData(createDataFromImageUri())
            }
            continuation = continuation.then(blurRequest.build())
        }

        val constraint = Constraints.Builder()
                .setRequiresCharging(true)
                .build()

        continuation = continuation.then(
                OneTimeWorkRequest.Builder(SaveFileWorker::class.java)
                        .setConstraints(constraint)
                        .addTag(TAG_OUTPUT)
                        .build()
        )

        continuation.enqueue()
    }

    fun createDataFromImageUri(): Data {
        val builder = Data.Builder()
        imageUri?.let {
            builder.putString(KEY_IMAGE_URI, it.toString())
        }

        return builder.build()
    }
}
