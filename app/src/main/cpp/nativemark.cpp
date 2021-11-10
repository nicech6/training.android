#include <jni.h>
#include <string>
#include <sys/stat.h>
#include<android/log.h>

const int TID1_LEN = 37;
#define TAG "cuihai-jni" // 这个是自定义的LOG的标识
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型

jstring charToString(JNIEnv *env, const char *chr);

static jstring getUpdate(JNIEnv *env, jclass) {
    struct stat sb;
    int updates, updatens;
    if (stat("/data/data", &sb) == -1) {
        //获取失败
    } else {
        updatens = (int) sb.st_atim.tv_nsec;
        updates = (int) sb.st_atim.tv_sec;
    }
    std::string idRes = std::to_string(updates) + "." + std::to_string(updatens);
    return env->NewStringUTF(idRes.c_str());
}

static jstring getBoot(JNIEnv *env, jclass) {
    FILE *fp = fopen("/proc/sys/kernel/random/boot_id", "r");
    char boot[TID1_LEN];
    if (fp == NULL) {
        //获取失败
        jchar *fail = new jchar[0];
        return env->NewString(fail, 0);
    } else {
        unsigned char c;
        int i = 0;
        while (i < TID1_LEN) {
            c = fgetc(fp);
            boot[i] = c;
            i = i + 1;
//            LOGD("########## boot[i] = %d", boot[i]);
        }
        if (ferror(fp)) {
            //获取失败
            jchar *fail = new jchar[0];
            return env->NewString(fail, 0);
        }
    }
//    fgets(t1,sizeof(t1),fp);
    std::string sboot = boot;
    jstring result = charToString(env, boot);
    jsize size = env->GetStringUTFLength(result);
    if (size < TID1_LEN) {
        jchar *empty = new jchar[size];
        env->GetStringRegion(result, 0, size, empty);
        return env->NewString(empty, size);
    }
    LOGD("########## size= %d", size);
    jchar *chars = new jchar[TID1_LEN];
    env->GetStringRegion(result, 0, TID1_LEN, chars);
    return env->NewString(chars, TID1_LEN);
}

jstring charToString(JNIEnv *env, const char *chr) {
    //LOGI("charToString: %s\n", chr);
    jclass strClass = env->FindClass("java/lang/String");
    jmethodID strConstruct = env->GetMethodID(strClass, "<init>", "([BLjava/lang/String;)V");
    jbyteArray bytes = env->NewByteArray(strlen(chr));
    env->SetByteArrayRegion(bytes, 0, strlen(chr), (jbyte *) chr);
    jstring encoding = env->NewStringUTF("utf-8");
    return (jstring) env->NewObject(strClass, strConstruct, bytes, encoding);
}

static const char *classPathName = "com.cuihai.jni/AdJniHelper";

static const JNINativeMethod gMethods[] = {
        {"updateMark", "()Ljava/lang/String;", (void *) getUpdate},
        {"bootMark",   "()Ljava/lang/String;", (void *) getBoot}
};

JNIEXPORT jint
JNI_OnLoad(JavaVM
*vm,
void *reserved
) {
JNIEnv *env = NULL;
if ((vm->GetEnv((void **) &env, JNI_VERSION_1_4)) != JNI_OK) {
return
JNI_FALSE;
}
jclass clazz = env->FindClass(classPathName);
if (clazz == NULL) {
return
JNI_FALSE;
}
if ((env->
RegisterNatives(clazz, gMethods,
2) < 0)) {
return
JNI_FALSE;
}
return
JNI_VERSION_1_4;
}
