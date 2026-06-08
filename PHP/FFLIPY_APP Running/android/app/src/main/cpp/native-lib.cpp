#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_logicsoftbd_fflipy_MainActivity_getEncryptionKey(
        JNIEnv* env,
        jobject /* this */) {
    std::string apiKey = "dc3c9f9ec0f958e07cd2f6ab1dc31f67";
    return env->NewStringUTF(apiKey.c_str());
}
