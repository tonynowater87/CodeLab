package com.tonynowater.hello_cmake

import android.util.Log

object LoadLibrary {
    // Used to load the 'native-lib' library on application startup.
    init {
        Log.d("DEBUG", "init loadLibrary start")
        System.loadLibrary("native-lib")
        Log.d("DEBUG", "init loadLibrary end")
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    external fun voidFromJNI()

    external fun sumIntegers(first: Int, second: Int): Int

    external fun sayHelloToMe(name: String, isFemale: Boolean): String
}