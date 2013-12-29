#include "text_input_node/CCIMEDispatcher.h"
#include "CCDirector.h"
#include "../CCApplication.h"
#include "platform/CCFileUtils.h"
#include "CCEventType.h"
#include "support/CCNotificationCenter.h"
#include "JniHelper.h"
#include <jni.h>

using namespace cocos2d;

extern "C" {
    JNIEXPORT void JNICALL Java_org_cocos2dx_lib_Cocos2dxRenderer_nativeRender(JNIEnv* env) {
        cocos2d::CCDirector::sharedDirector()->mainLoop();
    }

    JNIEXPORT void JNICALL Java_org_cocos2dx_lib_Cocos2dxRenderer_nativeOnPause()
	{
		if (CCDirector::sharedDirector()->getOpenGLView())
		{
			__android_log_print(ANDROID_LOG_DEBUG, "Cocos2dxRenderer", "nativeOnPause");
		
			CCApplication::sharedApplication()->applicationDidEnterBackground();
	//		CCNotificationCenter::sharedNotificationCenter()->postNotification(EVENT_COME_TO_BACKGROUND, NULL);

		}
		else
		{
			__android_log_print(ANDROID_LOG_DEBUG, "Cocos2dxRenderer", "nativeOnPause - NO VIEW");
		}
    }

    JNIEXPORT void JNICALL Java_org_cocos2dx_lib_Cocos2dxRenderer_nativeOnResume()
	{
        if (CCDirector::sharedDirector()->getOpenGLView())
		{
			__android_log_print(ANDROID_LOG_DEBUG, "Cocos2dxRenderer", "nativeOnResume");
			CCApplication::sharedApplication()->applicationWillEnterForeground();
        }
		else
		{
			__android_log_print(ANDROID_LOG_DEBUG, "Cocos2dxRenderer", "nativeOnResume - NO VIEW");
		}
    }

    JNIEXPORT void JNICALL Java_org_cocos2dx_lib_Cocos2dxRenderer_nativeInsertText(JNIEnv* env, jobject thiz, jstring text) {
        const char* pszText = env->GetStringUTFChars(text, NULL);
        cocos2d::CCIMEDispatcher::sharedDispatcher()->dispatchInsertText(pszText, strlen(pszText));
        env->ReleaseStringUTFChars(text, pszText);
    }

    JNIEXPORT void JNICALL Java_org_cocos2dx_lib_Cocos2dxRenderer_nativeDeleteBackward(JNIEnv* env, jobject thiz) {
        cocos2d::CCIMEDispatcher::sharedDispatcher()->dispatchDeleteBackward();
    }

    JNIEXPORT jstring JNICALL Java_org_cocos2dx_lib_Cocos2dxRenderer_nativeGetContentText() {
        JNIEnv * env = 0;

        if (JniHelper::getJavaVM()->GetEnv((void**)&env, JNI_VERSION_1_4) != JNI_OK || ! env) {
            return 0;
        }
        const char * pszText = cocos2d::CCIMEDispatcher::sharedDispatcher()->getContentText();
        return env->NewStringUTF(pszText);
    }
}
