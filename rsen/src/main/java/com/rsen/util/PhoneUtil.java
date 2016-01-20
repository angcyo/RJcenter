package com.rsen.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneUtil {
	public static String formatIPNumber(String number, Context context) {
		String temp = number;
		if (TextUtils.isEmpty(temp)) {
			return "";
		}
		StringBuffer sb = new StringBuffer(temp);
		temp = sb.toString();
		temp = temp.replace(" ", "");
		// 过滤ip号码
		if (temp.startsWith("17951")) {
			temp = temp.replace("17951", "");
		}
		else if (temp.startsWith("17911")) {
			temp = temp.replace("17911", "");
		}
		else if (temp.startsWith("12593")) {
			temp = temp.replace("12593", "");
		}
		else if (temp.startsWith("17909")) {
			temp = temp.replace("17909", "");
		}
		else {
			// 过滤自定义ip拨号前缀
		}
		// 国家/地区编号过滤
		if (temp.startsWith("0086")) {
			temp = temp.substring(4);
		}
		else if (temp.startsWith("00")) {
			temp = temp.substring(2);
		}
		if (temp.startsWith("+")) {
			temp = temp.substring(1);
		}
		if (temp.startsWith("86")) {
			temp = temp.substring(2);
		}
		// //过滤加号
		// if(sb.charAt(0) == '+'){
		// sb.deleteCharAt(0);
		// }
		// //过滤86
		// if(sb.charAt(0) == '8' && sb.charAt(1) == '6'){
		// sb.delete(0, 2);
		// }

		temp = PhoneNumberUtils.formatNumber(temp);
		if (temp != null && temp.contains("-")) {
			temp = temp.replaceAll("-", "");
		}
		return temp;
	}
	
	public static void TelCALL(Context context, String tel){
	       Intent intent = new Intent();
	       intent.setAction("android.intent.action.CALL");
	       intent.setData(Uri.parse("tel:" + tel));
	       context.startActivity(intent);
		}
	
	public static String TelPhoneStr(String tel){
		if (tel!=null&&tel.length()>7) {
			String head = tel.substring(0, 3);
			String loSt = tel.substring(tel.length()-4, tel.length());
			String zhong = "";
			for (int i = 0; i < tel.length()-7; i++) {
				zhong +="*";
			}
			return head+zhong+loSt;
		}
		return tel;
	}
	
	public static boolean IsPhoneStr(String tel){
		Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(tel);
        return m.matches();
	}

}
