package com.example.bmobexample;

import android.app.Application;
import android.content.Context;

import com.bmob.BmobConfiguration;
import com.bmob.BmobPro;

/**
  * @ClassName: BmobApplication
  * @Description: 
  * @author smile
  * @date 2014-12-9 上午10:29:50
  */
public class BmobApplication extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		initConfig(getApplicationContext());
	}
	
	/**
	 * 初始化文件配置
	 * @param context
	 */
	public static void initConfig(Context context) {
		BmobConfiguration config = new BmobConfiguration.Builder(context).customExternalCacheDir("Smile").build();
		BmobPro.getInstance(context).initConfig(config);
	}
	
}
