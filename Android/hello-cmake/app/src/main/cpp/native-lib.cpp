#include <jni.h>
#include <string>
#include <syslog.h>
#include <android/log.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_tonynowater_hello_1cmake_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject instance) {
    std::string hello = "Hello from C++";

    jclass c = env->FindClass("com/tonynowater/hello_cmake/MyClass");
    jclass c2 = env->FindClass("com/tonynowater/hello_cmake/MyClass2");

    __android_log_print(ANDROID_LOG_DEBUG, "Tag", "hello jni android log = %d", c);

    syslog(LOG_DEBUG, "hello jni syslog = %d", c2);

    return env->NewStringUTF(hello.c_str());
}