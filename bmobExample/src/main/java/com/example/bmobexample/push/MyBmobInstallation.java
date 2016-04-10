package com.example.bmobexample.push;

import android.content.Context;
import cn.bmob.v3.BmobInstallation;

public class MyBmobInstallation extends BmobInstallation {

	/**  
	 *  
	 */  
	private static final long serialVersionUID = 1L;

	/**  
	 * 用户id-这样可以将设备与用户之间进行绑定
	 */  
	private String uid;
	
	public MyBmobInstallation(Context context) {
		super(context);
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
}
