package com.tonynowater.hello_cmake

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("DEBUG", "onCreate")
        // Example of a call to a native method
        sample_text.text = "${LoadLibrary.stringFromJNI()} ${LoadLibrary.sumIntegers(1, 2)} \n${LoadLibrary.sayHelloToMe("Tony", false)}"
        LoadLibrary.voidFromJNI()
    }

    fun click(view: View) {
        //LoadLibrary.throwExceptionFromNative("throwExceptionFromNative!!!")
        LoadLibrary.readKotlinStaticValueFromNative()
        LoadLibrary.static = 0
    }
}
