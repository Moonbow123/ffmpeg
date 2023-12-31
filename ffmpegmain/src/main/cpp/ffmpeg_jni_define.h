//
// Created by frank on 2019/11/9.
//

#ifndef FFMPEGANDROID_FFMPEG_JNI_DEFINE_H
#define FFMPEGANDROID_FFMPEG_JNI_DEFINE_H

#include <android/log.h>

#define LOGI(TAG, FORMAT, ...) __android_log_print(ANDROID_LOG_INFO, TAG, FORMAT, ##__VA_ARGS__)
#define LOGE(TAG, FORMAT, ...) __android_log_print(ANDROID_LOG_ERROR, TAG, FORMAT, ##__VA_ARGS__)

#define AUDIO_PLAYER_FUNC(RETURN_TYPE, FUNC_NAME, ...) \
extern "C" { \
    JNIEXPORT RETURN_TYPE JNICALL Java_com_hft_ffmpeg_main_utils_AudioPlayer_ ## FUNC_NAME \
    (JNIEnv *env, jobject thiz, ##__VA_ARGS__);\
}\
    JNIEXPORT RETURN_TYPE JNICALL Java_com_hft_ffmpeg_main_utils_AudioPlayer_ ## FUNC_NAME \
    (JNIEnv *env, jobject thiz, ##__VA_ARGS__)\

#define FFMPEG_FUNC(RETURN_TYPE, FUNC_NAME, ...) \
    JNIEXPORT RETURN_TYPE JNICALL Java_com_hft_ffmpeg_main_utils_FFmpegKit_ ## FUNC_NAME \
    (JNIEnv *env, jclass thiz, ##__VA_ARGS__)\

#define FFPROBE_FUNC(RETURN_TYPE, FUNC_NAME, ...) \
extern "C" { \
    JNIEXPORT RETURN_TYPE JNICALL Java_com_hft_ffmpeg_main_utils_FFmpegKit_ ## FUNC_NAME \
    (JNIEnv *env, jclass thiz, ##__VA_ARGS__);\
}\
    JNIEXPORT RETURN_TYPE JNICALL Java_com_hft_ffmpeg_main_utils_FFmpegKit_ ## FUNC_NAME \
    (JNIEnv *env, jclass thiz, ##__VA_ARGS__)\

#define VIDEO_PLAYER_FUNC(RETURN_TYPE, FUNC_NAME, ...) \
    JNIEXPORT RETURN_TYPE JNICALL Java_com_hft_ffmpeg_main_utils_VideoPlayer_ ## FUNC_NAME \
    (JNIEnv *env, jobject thiz, ##__VA_ARGS__)\

#define MEDIA_PLAYER_FUNC(RETURN_TYPE, FUNC_NAME, ...) \
extern "C" { \
    JNIEXPORT RETURN_TYPE JNICALL Java_com_hft_ffmpeg_main_utils_MediaPlayer_ ## FUNC_NAME \
    (JNIEnv *env, jobject thiz, ##__VA_ARGS__);\
}\
    JNIEXPORT RETURN_TYPE JNICALL Java_com_hft_ffmpeg_main_utils_MediaPlayer_ ## FUNC_NAME \
    (JNIEnv *env, jobject thiz, ##__VA_ARGS__)\

#define PUSHER_FUNC(RETURN_TYPE, FUNC_NAME, ...) \
    JNIEXPORT RETURN_TYPE JNICALL Java_com_hft_ffmpeg_main_utils_Pusher_ ## FUNC_NAME \
    (JNIEnv *env, jobject thiz, ##__VA_ARGS__)\

#endif //FFMPEGANDROID_FFMPEG_JNI_DEFINE_H
