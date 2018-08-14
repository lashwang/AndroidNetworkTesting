#include <jni.h>
#include <string>
#include <android/log.h>



extern "C" JNIEXPORT jstring

JNICALL
Java_com_example_simon_androidnetworktesting_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    struct timespec ts;
    int ret = clock_gettime(CLOCK_REALTIME, &ts);
    __android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "clock_gettime,tv_sec:%ld,tv_nsec:%ld",
                        ts.tv_sec,ts.tv_nsec);
    return env->NewStringUTF(hello.c_str());
}
