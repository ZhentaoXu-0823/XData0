LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional
LOCAL_SRC_FILES := $(call all-java-files-under, src) src/com/custom/mdm/ICustomAPI.aidl
LOCAL_AIDL_INCLUDES := src/com/custom/mdm/ICustomAPI.aidl
LOCAL_PACKAGE_NAME := CustomMDM
LOCAL_CERTIFICATE := platform
LOCAL_PRIVATE_PLATFORM_APIS = true

LOCAL_STATIC_JAVA_LIBRARIES += \
    vendor.mediatek.hardware.nvram-V1.0-java \
    libspossdk

LOCAL_TARGET_CPU_ABI := $(TARGET_CPU_ABI)
LOCAL_MULTILIB := 32

ifeq ($(TARGET_CPU_ABI), arm64-v8a)
    LOCAL_MULTILIB := 64
else
    ifeq ($(TARGET_CPU_ABI), armeabi-v7a)
        LOCAL_TARGET_CPU_ABI := armeabi
    endif
endif

LOCAL_PREBUILT_JNI_LIBS := \
    libs/$(LOCAL_TARGET_CPU_ABI)/libserial_port.so \
    libs/$(LOCAL_TARGET_CPU_ABI)/libandroid_socketcan.so

LOCAL_PROGUARD_ENABLED := disabled

include $(BUILD_PACKAGE)
