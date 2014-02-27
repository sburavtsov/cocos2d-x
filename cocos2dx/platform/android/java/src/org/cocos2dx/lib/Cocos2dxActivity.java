/****************************************************************************
Copyright (c) 2010-2013 cocos2d-x.org

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

import org.cocos2dx.lib.Cocos2dxHelper.Cocos2dxHelperListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.FrameLayout;
import android.net.Uri;

public abstract class Cocos2dxActivity extends Activity implements Cocos2dxHelperListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final String TAG = Cocos2dxActivity.class.getSimpleName();

	// ===========================================================
	// Fields
	// ===========================================================
	
	protected Cocos2dxGLSurfaceView mGLSurfaceView;
	private Cocos2dxHandler mHandler;
	protected static Activity me = null;
	private static Context sContext = null;
	
	private boolean haveFocus;
	private boolean isResumed;
	
	public static Context getContext() {
		return sContext;
	}
	
	// ===========================================================
	// Constructors
	// ===========================================================
	
	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		haveFocus = false;
		isResumed = false; 
		
		super.onCreate(savedInstanceState);
		sContext = this;
		me = this;
    	this.mHandler = new Cocos2dxHandler(this);

    	this.init();

		Cocos2dxHelper.init(this, this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onResume()
	{
		super.onResume();
		isResumed = true;
		
		Cocos2dxHelper.onResume();
		TryResumeRenderer();
	}

	@Override
	protected void onPause()
	{	
		isResumed = false;
		// http://gamedev.stackexchange.com/questions/12629/workaround-to-losing-the-opengl-context-when-android-pauses
		// Basically the trick is to detach your GLSurfaceView from the view hierarchy
		// from your Activity's onPause(). Since it's not in the view hierarchy
		// at the point onPause() runs, the context never gets destroyed.

		if (null != mGLSurfaceView)
		{
			mGLSurfaceView.onPause();
			mGLSurfaceView.setVisibility(View.GONE);
		}
		
		Cocos2dxHelper.onPause();
		super.onPause();
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		Log.e(TAG, "onWindowFocusChanged hasFocus=" + hasFocus);
	
		haveFocus = hasFocus;
		
		if (true == hasFocus)
		{
			TryResumeRenderer();
		}
	}
	
	
	private void TryResumeRenderer()
	{
		if (null != mGLSurfaceView)
		{
			if (mGLSurfaceView.getVisibility() == View.GONE)
			{
				mGLSurfaceView.setVisibility(View.VISIBLE);
			}
		
			// We need both callbacks OnResume + onWindowFocusChanged
			if (haveFocus && isResumed)
			{
				mGLSurfaceView.onResume();
				isResumed = false; // Do not resume surface until next Activity.OnResume callback
			}
		}
	}
	
/*
	/*@Override 
	protected void onStop()
	{
		super.onStop();
		
	    //android.os.Process.killProcess(android.os.Process.myPid());
	}*/
	
	@Override
	public void showDialog(final String pTitle, final String pMessage) {
		Message msg = new Message();
		msg.what = Cocos2dxHandler.HANDLER_SHOW_DIALOG;
		msg.obj = new Cocos2dxHandler.DialogMessage(pTitle, pMessage);
		this.mHandler.sendMessage(msg);
	}

	@Override
	public void showEditTextDialog(final String pTitle, final String pContent, final int pInputMode, final int pInputFlag, final int pReturnType, final int pMaxLength) { 
		Message msg = new Message();
		msg.what = Cocos2dxHandler.HANDLER_SHOW_EDITBOX_DIALOG;
		msg.obj = new Cocos2dxHandler.EditBoxMessage(pTitle, pContent, pInputMode, pInputFlag, pReturnType, pMaxLength);
		this.mHandler.sendMessage(msg);
	}
	
	@Override
	public void runOnGLThread(final Runnable pRunnable)
	{
		if (null != mGLSurfaceView)
		{
			mGLSurfaceView.queueEvent(pRunnable);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================
	public void init() {
		
    	// FrameLayout
        ViewGroup.LayoutParams framelayout_params =
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                                       ViewGroup.LayoutParams.FILL_PARENT);
        FrameLayout framelayout = new FrameLayout(this);
        framelayout.setLayoutParams(framelayout_params);

        // Cocos2dxEditText layout
        ViewGroup.LayoutParams edittext_layout_params =
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                                       ViewGroup.LayoutParams.WRAP_CONTENT);
        Cocos2dxEditText edittext = new Cocos2dxEditText(this);
        edittext.setLayoutParams(edittext_layout_params);

        // ...add to FrameLayout
        framelayout.addView(edittext);
        // WW add HDR icon here!

        Log.e("Activity", "onCreateView()");
        // Cocos2dxGLSurfaceView
        this.mGLSurfaceView = this.onCreateView();

        // ...add to FrameLayout
        framelayout.addView(this.mGLSurfaceView);

        // Switch to supported OpenGL (ARGB888) mode on emulator
        if (isAndroidEmulator())
           this.mGLSurfaceView.setEGLConfigChooser(8 , 8, 8, 8, 16, 0);

        this.mGLSurfaceView.setCocos2dxRenderer(new Cocos2dxRenderer());
        this.mGLSurfaceView.setCocos2dxEditText(edittext);

        // Set framelayout as the content view
		setContentView(framelayout);
	}
	
    public Cocos2dxGLSurfaceView onCreateView() {
    	Log.d(TAG, "new Cocos2dxGLSurfaceView!");
    	return new Cocos2dxGLSurfaceView(this);
    }
		
		// Opens URL as the new activity
		public static void openURL(String url) {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			me.startActivity(i);
		}

   private final static boolean isAndroidEmulator() {
      String model = Build.MODEL;
      Log.d(TAG, "model=" + model);
      String product = Build.PRODUCT;
      Log.d(TAG, "product=" + product);
      boolean isEmulator = false;
      if (product != null) {
         isEmulator = product.equals("sdk") || product.contains("_sdk") || product.contains("sdk_");
      }
      Log.d(TAG, "isEmulator=" + isEmulator);
      return isEmulator;
   }

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
