/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.hellojni;


import java.io.File;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;


public class HelloJni extends Activity
{
    /** Called when the activity is first created. */
	private TextView m_TextNum;
	private Button m_Button2Asset;
	private Button m_Button2URL;
	private Boolean m_bLoadAssetsSuccess;
	private Boolean m_bLoadHTTPSuccess;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /* Create a TextView and set its content.
         * the text is retrieved by calling a native
         * function.
         */
        setContentView(R.layout.activity_main);
        m_TextNum = (TextView)this.findViewById(R.id.textView1);
        //tv.setText( stringFromJNI());
        m_bLoadAssetsSuccess = false;
		m_bLoadHTTPSuccess = false;
        Button m_Button2Asset = (Button)this.findViewById(R.id.button1);
        Button m_Button2URL = (Button)this.findViewById(R.id.button2);
        MyButtonListener linsener = new MyButtonListener();
		
		m_Button2Asset.setOnClickListener((OnClickListener) linsener);
		
		m_Button2URL.setOnClickListener((OnClickListener) linsener);
       // TextView  tv = new TextView(this);
       // tv.setText( stringFromJNI() );
        //setContentView(tv);
    }

    /* A native method that is implemented by the
     * 'hello-jni' native library, which is packaged
     * with this application.
     */
    public native String  stringFromJNI();
    public native String  stringFromServerJNI();

    /* This is another native method declaration that is *not*
     * implemented by 'hello-jni'. This is simply to show that
     * you can declare as many native methods in your Java code
     * as you want, their implementation is searched in the
     * currently loaded native libraries only the first time
     * you call them.
     *
     * Trying to call this function will result in a
     * java.lang.UnsatisfiedLinkError exception !
     */
    public native String  unimplementedStringFromJNI();

    /* this is used to load the 'hello-jni' library on application
     * startup. The library has already been unpacked into
     * /data/data/com.example.hellojni/lib/libhello-jni.so at
     * installation time by the package manager.
     */
   // static {
      //  System.loadLibrary("hello-jni");
  //  }
  //---------------------------------------------------------------------------------
  	private void __LoadFormAssets() 
  	{
  		
  		if(m_bLoadAssetsSuccess)
  		{
  			return;
  		}
  		
  		File dir = getDir("lib", Context.MODE_PRIVATE);
  		File soFile = new File(dir, "libhello-jni.so");
  		FileUtils.AssetToFile(this, "libhello-jni.so", soFile);

  		try 
  		{
  			System.load(soFile.getAbsolutePath());
  			m_bLoadAssetsSuccess = true;
  		} 
  		catch (Exception e) 
  		{
  		}
  		
  	}
  	//---------------------------------------------------------------------------------
  	private void __LoadFormURL()
  	{
  		 
  		if(m_bLoadHTTPSuccess)
  		{
  			return;
  		}
  		
  		File dir = getDir("lib", Context.MODE_PRIVATE);
  		File soFile = new File(dir, "libhellohttp-jni.so");
  		// ！Warnning！
  		// 这个URL上的so文件只保存30天，若这个实效了？
  		// 0.请到 assets文件夹取 librectdymanichttp.so
  		// 1.自行上传到随便一个服务器
  		// 2.修改一下URL为你的URL即可
  		FileUtils.URLToFile( "http://10.10.10.111:8090/libhellohttp-jni.so", soFile);
  		try 
  		{
  			System.load(soFile.getAbsolutePath());
  			m_bLoadHTTPSuccess = true;
  		} 
  		catch (Exception e) 
  		{
  		}
  		
  	}
  //---------------------------------------------------------------------------------
  	class MyButtonListener implements OnClickListener {

  		@Override
  		public void onClick(View v) {
  			
  			String str = "no Message";
  			switch(v.getId())
  			{
  			
  			case R.id.button1:
  				__LoadFormAssets();
  				//str = "Form Assets so:" + CallDymanicFuncFormAssets(1, 2);
  				str = "Form Assets so:"+stringFromJNI();
  				break;
  				
  			case R.id.button2:
  				__LoadFormURL();
  				//str = "Form URL so:" + CallDymanicFuncFormHTTP(1, 2);
  				str = "Form Http so:"+stringFromServerJNI();
  				break;
  				
  			default:
  					break;
  			}
  			m_TextNum.setText(m_TextNum.getText() + "\n" + str.toString());
  		}

  	}
  	//---------------------------------------------------------------------------------
    public static native String CallDymanicFuncFormAssets(int x, int y);
	public static native String CallDymanicFuncFormHTTP(int x, int y);
}
