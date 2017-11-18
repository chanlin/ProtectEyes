LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES :=\
    $(call all-java-files-under,src\main)


LOCAL_STATIC_JAVA_LIBRARIES := android-support-v4 appcompat-v7


LOCAL_PACKAGE_NAME := ProtectEyes


include $(BUILD_PACKAGE)