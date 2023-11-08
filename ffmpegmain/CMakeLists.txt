# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        ffcommand

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        src/main/cpp/ffmpeg/cmdutils.c
        src/main/cpp/ffmpeg/ffmpeg.c
        src/main/cpp/ffmpeg/ffmpeg_filter.c
        src/main/cpp/ffmpeg/ffmpeg_opt.c
        src/main/cpp/ffmpeg_cmd.c
        src/main/cpp/ffmpeg/ffprobe.c
        src/main/cpp/ffmpeg/ffmpeg_hw.c
        src/main/cpp/audio_player.cpp
        src/main/cpp/opensl_audio_player.cpp
        src/main/cpp/video_player.cpp
        src/main/cpp/ffmpeg_pusher.cpp
        src/main/cpp/packet_queue.cpp
        src/main/cpp/media_player.cpp
        src/main/cpp/video_filter.c
        src/main/cpp/fast_start.c
        src/main/cpp/ffprobe_cmd.cpp
        src/main/cpp/audio_lame.c)

# 添加ffmpeg库
add_library( ffmpeg
        SHARED
        IMPORTED )
set_target_properties( ffmpeg
        PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${CMAKE_ANDROID_ARCH_ABI}/libffmpeg.so )

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

# 添加include目录路径
include_directories(src/main/cpp)
include_directories(src/main/cpp/include)
if(${CMAKE_ANDROID_ARCH_ABI} MATCHES "armeabi-v7a")
    include_directories(src/main/cpp/include/armeabi-v7a)
    message("This is armeabi-v7a")
elseif(${CMAKE_ANDROID_ARCH_ABI} MATCHES "arm64-v8a")
    include_directories(src/main/cpp/include/arm64-v8a)
    message("This is arm64-v8a")
endif()

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        ffcommand

        #the third library
        ffmpeg

        -landroid #native_window
        -ljnigraphics #bitmap
        -lOpenSLES #openSLES
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib} )