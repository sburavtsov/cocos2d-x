#ifndef __CCPLATFORMDEFINE_H__
#define __CCPLATFORMDEFINE_H__

#include "android/log.h"

/* Define NULL pointer value */
#ifndef NULL
#ifdef __cplusplus
#define NULL    0
#else
#define NULL    ((void *)0)
#endif
#endif

#define CC_DLL

#define CC_NO_MESSAGE_PSEUDOASSERT(cond)                        \
    if (!(cond)) {                                              \
        __android_log_print(ANDROID_LOG_ERROR,                  \
                            "cocos2d-x assert",                 \
                            "%s function:%s line:%d",           \
                            __FILE__, __FUNCTION__, __LINE__);  \
																\
		volatile int* a = (int*)(NULL);							\
		*a = 1;													\
    }

#define CC_MESSAGE_PSEUDOASSERT(cond, msg)                          \
    if (!(cond)) {                                                  \
        __android_log_print(ANDROID_LOG_ERROR,                      \
                            "cocos2d-x assert",                     \
                            "file:%s function:%s line:%d, %s",      \
                            __FILE__, __FUNCTION__, __LINE__, msg); \
																	\
		volatile int* a = (int*)(NULL);								\
		*a = 1;														\
    }

#define CC_SYSTEM_ASSERT(cond) CC_NO_MESSAGE_PSEUDOASSERT(cond)

#define CC_UNUSED_PARAM(unusedparam) (void)unusedparam

#endif /* __CCPLATFORMDEFINE_H__*/
