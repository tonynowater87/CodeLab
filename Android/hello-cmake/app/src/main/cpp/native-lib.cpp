#include <jni.h>
#include <string>
#include <syslog.h>
#include <android/log.h>
#include <stdio.h>
#include <iostream>

extern "C" JNIEXPORT jstring JNICALL
Java_com_tonynowater_hello_1cmake_LoadLibrary_stringFromJNI(
        JNIEnv *env,
        jobject instance) {
    std::string hello = "Hello from C++";

    jclass c = env->FindClass("com/tonynowater/hello_cmake/MyClass");
    jclass c2 = env->FindClass("com/tonynowater/hello_cmake/MyClass2");

    __android_log_print(ANDROID_LOG_DEBUG, "DEBUG", "hello jni android log = %d", c);

    syslog(LOG_DEBUG, "hello jni syslog = %d", c2);

    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT void JNICALL
Java_com_tonynowater_hello_1cmake_LoadLibrary_voidFromJNI(
                JNIEnv *env,
                jobject instance) {
    __android_log_print(ANDROID_LOG_DEBUG, "DEBUG", "voidFromJNI");
}

extern "C" JNIEXPORT jint JNICALL
Java_com_tonynowater_hello_1cmake_LoadLibrary_sumIntegers(
                JNIEnv *env,
                jobject instance,
                jint first,
                jint second) {

    __android_log_print(ANDROID_LOG_DEBUG, "DEBUG", "first:%d, second%d", first, second);
    return first + second;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_tonynowater_hello_1cmake_LoadLibrary_sayHelloToMe(
                JNIEnv *env,
                jobject instance,
                jstring name,
                jboolean isFemale) {

    const char* namePointer = env->GetStringUTFChars(name, NULL);

    __android_log_print(ANDROID_LOG_DEBUG, "DEBUG", "name:%s, jboolean %s", namePointer, isFemale?"true":"false");

    std::string title;

    if(isFemale) {
        title = "Ms. ";
    } else {
        title = "Mr. ";
    }

    std::string fullName = title + namePointer;

    return env->NewStringUTF(fullName.c_str());
}

