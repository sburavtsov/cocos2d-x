/****************************************************************************
Copyright (c) 2010-2011 cocos2d-x.org

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
package org.cocos2dx.lib;

import java.util.concurrent.TimeUnit;
//import android.content.Context;
//import android.opengl.GLSurfaceView;
//import android.os.Handler;
//import android.os.Message;
//import android.util.AttributeSet;
import android.util.Log;
//import android.view.KeyEvent;
//import android.view.MotionEvent;
//import android.view.inputmethod.InputMethodManager;


public class MyRunnable implements Runnable
{
	private volatile boolean m_jobFinished;
	private Runnable m_job;
	private long m_createTime;
	
	public MyRunnable(Runnable job)
	{
		m_jobFinished = false;
		m_job = job;
	
		m_createTime = System.nanoTime();
	}
	
	@Override
	public void run()
	{
		m_job.run();
		
		try { TimeUnit.MILLISECONDS.sleep(5); }
		catch (Exception e){}
		
		m_jobFinished = true;
	}
	
	boolean WaitForFinish(int maxWaitTimeMS)
	{
		final long deadLineTime = m_createTime + (1000000 * maxWaitTimeMS);
		
		while (false == m_jobFinished)
		{
			try
			{
				TimeUnit.MILLISECONDS.sleep(10);
			}
			catch (Exception e)
			{
				Log.d("WaitForFinish", "Terminated");
				return false;
			}
			
			if (System.nanoTime() > deadLineTime)
			{
				Log.d("WaitForFinish", "Terminated after " + maxWaitTimeMS + "ms");
				return false;
			}
		}
		
		return true;
	}
}
