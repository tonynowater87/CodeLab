#include <jni.h>
#include <string>
#include <syslog.h>
#include <android/log.h>
#include <stdio.h>
#include <iostream>

//You can load native code from shared libraries with the standard System.loadLibrary call. In this time the method JNI_onLoad is invoked.
extern "C" JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    __android_log_print(ANDROID_LOG_INFO, __FUNCTION__, "onLoad");
    JNIEnv *env;
    if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }
    return JNI_VERSION_1_6;
}

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

    const char *namePointer = env->GetStringUTFChars(name, NULL);

    __android_log_print(ANDROID_LOG_DEBUG, "DEBUG", "name:%s, jboolean %s", namePointer,
                        isFemale ? "true" : "false");

    std::string title;

    if (isFemale) {
        title = "Ms. ";
    } else {
        title = "Mr. ";
    }

    std::string fullName = title + namePointer;

    return env->NewStringUTF(fullName.c_str());
}

extern "C" JNIEXPORT void JNICALL
Java_com_tonynowater_hello_1cmake_LoadLibrary_throwExceptionFromNative(
        JNIEnv *env,
        jobject instance,
        jstring description) {

    jclass clazz = env->FindClass("java/lang/IllegalArgumentException");
    if (clazz != NULL) {
        env->ThrowNew(clazz, env->GetStringUTFChars(description, NULL));
    }
    env->DeleteLocalRef(clazz);
}

extern "C" JNIEXPORT void JNICALL
Java_com_tonynowater_hello_1cmake_LoadLibrary_readKotlinStaticValueFromNative(
        JNIEnv *env,
        jobject instance) {

    jclass clazz = env->FindClass("com/tonynowater/hello_cmake/LoadLibrary");
    jfieldID fieldId = env->GetStaticFieldID(clazz, "static", "I");
    if (fieldId == NULL) {
        __android_log_print(ANDROID_LOG_DEBUG, __FUNCTION__, "fieldId == null");
    } else {
        jint fieldValue = env->GetStaticIntField(clazz, fieldId);
        __android_log_print(ANDROID_LOG_DEBUG, __FUNCTION__, "Field value: %d", fieldValue);
    }
}

