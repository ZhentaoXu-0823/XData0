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
include $(BUILD_PACKAGE)
