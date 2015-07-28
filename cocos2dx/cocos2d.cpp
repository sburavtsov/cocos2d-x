/****************************************************************************
Copyright (c) 2010-2012 cocos2d-x.org
Copyright (c) 2008-2010 Ricardo Quesada
Copyright (c) 2011      Zynga Inc.

http://www.cocos2d-x.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
****************************************************************************/

#include "cocos2d.h"

NS_CC_BEGIN

const char* cocos2dVersion()
{
    return "cocos2d-x 2.2.6";
}

#ifdef CC_USE_ASSERTS

static IAssertListener* s_pAssertListener = NULL;

void CCAssertions::SetAssertListener(IAssertListener* pListener)
{
	s_pAssertListener = pListener;
}

void CCAssertions::AssertMessage(const char* szMessage, const char* szFile, int nLine)
{
	cocos2d::CCLog(ASSERT_MESSAGE_FORMAT, szFile, nLine, szMessage); 

	if (NULL != s_pAssertListener)	
		s_pAssertListener->OnAssert(szMessage, szFile, nLine); 
}

#endif

NS_CC_END

