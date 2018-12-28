package com.example.android.kotlincoroutines.main

import com.example.android.kotlincoroutines.main.fakes.makeFailureCall
import com.example.android.kotlincoroutines.main.fakes.makeSuccessCall
import com.example.android.kotlincoroutines.util.FakeNetworkException
import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FakeNetworkCallAwaitTest {

    @Test
    fun whenFakeNetworkCallSuccess_resumeWithResult() {
        val subject = makeSuccessCall("the title")
        runBlocking {
            Truth.assertThat(subject.await()).isEqualTo("the title")
        }
    }

    // Example of launch in a test (always fails)
    @Test(expected = FakeNetworkException::class)
    fun whenFakeNetworkCallFailure_throws() {
        val subject = makeFailureCall(FakeNetworkException("the error"))

        runBlocking {
            subject.await()
        }

        //=======================
        //val scope = CoroutineScope(Dispatchers.Default)
        // launch starts the coroutine and then returns immediately
        //scope.launch {
        // since this is asynchronous code, this may be called *after* the test completes
        //subject.await()
        //}
        // test function returns immediately, and
        // doesn't get the exception raised by await()
        //=======================
    }
}
