package com.example.bmobexample.user;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.EmailVerifyListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.ResetPasswordByCodeListener;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.example.bmobexample.BaseActivity;
import com.example.bmobexample.R;
import com.example.bmobexample.bean.MyUser;

/**用户管理
 * @author Administrator
 *
 */
public class UserActivity extends BaseActivity {
	
	EditText et_number,et_code;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_code);
		et_number = (EditText) findViewById(R.id.et_number);
		et_code = (EditText) findViewById(R.id.et_code);
		
		mListview = (ListView) findViewById(R.id.listview);
		mAdapter = new ArrayAdapter<String>(this, R.layout.list_item,
				R.id.tv_item, getResources().getStringArray(R.array.bmob_user_list));
		mListview.setAdapter(mAdapter);
		mListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				testBmob(position + 1);
			}
		});

	}
	
	private void testBmob(int pos) {
		switch (pos) {
		case 1:
			testSignUp();
			break;
		case 2:
			testLogin();
			break;
		case 3:
			testGetCurrentUser();
			break;
		case 4:
			testLogOut();
			break;
		case 5:
			updateUser();
			break;
		case 6:
			checkPassword();
			break;
		case 7:
			testResetPasswrod();
			break;
		case 8:
			emailVerify();
			break;
		case 9:
			testFindBmobUser();
			break;
		case 10://通过邮箱和密码登陆
			loginByEmailPwd();
			break;
		case 11://通过手机号码和密码登陆
			loginByPhonePwd();
			break;
		case 12://通过手机号码和短信验证码登陆
			loginByPhoneCode();
			break;
		case 13://一键注册登录
			signOrLogin();
			break;
		case 14://通过手机号码充值密码
			resetPasswordBySMS();
			break;
		case 15://根据旧密码和新密码来修改当前用户密码
			updateCurrentUserPwd();
			break;
		}
	}
	
	/**
	 * 注册用户
	 */
	private void testSignUp() {
		final MyUser myUser = new MyUser();
		myUser.setUsername("smile");
		myUser.setPassword("123456");
		myUser.setAge(18);
		myUser.signUp(this, new SaveListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				toast("注册成功:" + myUser.getUsername() + "-"
						+ myUser.getObjectId() + "-" + myUser.getCreatedAt()
						+ "-" + myUser.getSessionToken()+",是否验证："+myUser.getEmailVerified());
			}

			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				toast("注册失败:" + msg);
			}
		});
	}

	/**
	 * 登陆用户
	 */
	private void testLogin() {
		final BmobUser bu2 = new BmobUser();
		bu2.setUsername("smile");
		bu2.setPassword("123456");
		bu2.login(this, new SaveListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				toast(bu2.getUsername() + "登陆成功");
				testGetCurrentUser();
			}

			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				toast("登陆失败:" + msg);
			}
		});
	}

	/**
	 * 获取本地用户
	 */
	private void testGetCurrentUser() {
//		MyUser myUser = BmobUser.getCurrentUser(this, MyUser.class);
//		if (myUser != null) {
//			Log.i("life","本地用户信息:objectId = " + myUser.getObjectId() + ",name = " + myUser.getUsername()
//					+ ",age = "+ myUser.getAge());
//		} else {
//			toast("本地用户为null,请登录。");
//		}
		//V3.4.5版本新增加getObjectByKey方法获取本地用户对象中某一列的值
		String username = (String) BmobUser.getObjectByKey(this, "username");
		String mobilePhoneNumber = (String) BmobUser.getObjectByKey(this, "mobilePhoneNumber");
		String objectId = (String) BmobUser.getObjectByKey(this, "objectId");
		String createdAt = (String) BmobUser.getObjectByKey(this, "createdAt");
		String sessionToken = (String) BmobUser.getObjectByKey(this, "sessionToken");
		Integer age = (Integer) BmobUser.getObjectByKey(this, "age");
		Boolean gender = (Boolean) BmobUser.getObjectByKey(this, "gender");
		Log.i("bmob", ""+username+",\n"+mobilePhoneNumber+",\n"+objectId+",\n"+createdAt+",\n"+sessionToken+",\n"+age+",\n"+gender);
	}

	/**
	 * 清除本地用户
	 */
	private void testLogOut() {
		BmobUser.logOut(this);
	}

	/**
	 * 更新用户
	 */
	private void updateUser() {
		final MyUser bmobUser = BmobUser.getCurrentUser(this, MyUser.class);
		if (bmobUser != null) {
			Log.d("bmob", "getObjectId = " + bmobUser.getObjectId());
			Log.d("bmob", "getUsername = " + bmobUser.getUsername());
			Log.d("bmob", "getEmail = "    + bmobUser.getEmail());
			Log.d("bmob", "getCreatedAt = " + bmobUser.getCreatedAt());
			Log.d("bmob", "getUpdatedAt = " + bmobUser.getUpdatedAt());
			MyUser newUser = new MyUser();
			newUser.setAge(25);
			newUser.update(this,bmobUser.getObjectId(),new UpdateListener() {

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					testGetCurrentUser();
				}

				@Override
				public void onFailure(int code, String msg) {
					// TODO Auto-generated method stub
					toast("更新用户信息失败:" + msg);
				}
			});
		} else {
			toast("本地用户为null,请登录。");
		}
	}

	/**
	 * 验证旧密码是否正确
	 * @Title: updatePassword
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	private void checkPassword() {
		BmobQuery<MyUser> query = new BmobQuery<MyUser>();
		final MyUser bmobUser = BmobUser.getCurrentUser(this, MyUser.class);
		// 如果你传的密码是正确的，那么arg0.size()的大小是1，这个就代表你输入的旧密码是正确的，否则是失败的
		query.addWhereEqualTo("password", "123456");
		query.addWhereEqualTo("username", bmobUser.getUsername());
		query.findObjects(this, new FindListener<MyUser>() {

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(List<MyUser> arg0) {
				// TODO Auto-generated method stub
				toast("查询密码成功:" + arg0.size());
			}
		});
	}

	/**
	 * 重置密码
	 */
	private void testResetPasswrod() {
		final String email = "123456789@qq.com";
		BmobUser.resetPasswordByEmail(this, email, new ResetPasswordByEmailListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				toast("重置密码请求成功，请到" + email + "邮箱进行密码重置操作");
			}

			@Override
			public void onFailure(int code, String e) {
				// TODO Auto-generated method stub
				toast("重置密码失败:" + e);
			}
		});
	}

	/**
	 * 查询用户
	 */
	private void testFindBmobUser() {
		BmobQuery<MyUser> query = new BmobQuery<MyUser>();
		query.addWhereEqualTo("username", "lucky");
		query.findObjects(this, new FindListener<MyUser>() {

			@Override
			public void onSuccess(List<MyUser> object) {
				// TODO Auto-generated method stub
				toast("查询用户成功：" + object.size());

			}

			@Override
			public void onError(int code, String msg) {
				// TODO Auto-generated method stub
				toast("查询用户失败：" + msg);
			}
		});
	}

	/**
	 * 验证邮件
	 */
	private void emailVerify() {
		final String email = "75727433@qq.com";
		BmobUser.requestEmailVerify(this, email, new EmailVerifyListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				toast("请求验证邮件成功，请到" + email + "邮箱中进行激活账户。");
			}

			@Override
			public void onFailure(int code, String e) {
				// TODO Auto-generated method stub
				toast("请求验证邮件失败:" + e);
			}
		});
	}
	
	private void loginByEmailPwd(){
		BmobUser.loginByAccount(this, "123456@163.com", "123456", new LogInListener<MyUser>() {
			
			@Override
			public void done(MyUser user, BmobException e) {
				// TODO Auto-generated method stub
				if(user!=null){
					Log.i("smile", ""+user.getUsername()+"-"+user.getAge()+"-"+user.getObjectId()+"-"+user.getEmail());
				}
			}
		});
	}
	
	private void loginByPhonePwd(){
		String number = et_number.getText().toString();
		BmobUser.loginByAccount(this, number, "123456", new LogInListener<MyUser>() {
			
			@Override
			public void done(MyUser user, BmobException e) {
				// TODO Auto-generated method stub
				if(user!=null){
					toast("登录成功");
					Log.i("smile", ""+user.getUsername()+"-"+user.getAge()+"-"+user.getObjectId()+"-"+user.getEmail());
				}else{
					toast("错误码："+e.getErrorCode()+",错误原因："+e.getLocalizedMessage());
				}
			}
		});
	}
	
	private void loginByPhoneCode(){
		//1、调用请求验证码接口
//		BmobSMS.requestSMSCode(this, "手机号码", "模板名称",new RequestSMSCodeListener() {
//			
//			@Override
//			public void done(Integer smsId,BmobException ex) {
//				// TODO Auto-generated method stub
//				if(ex==null){//验证码发送成功
//					Log.i("smile", "短信id："+smsId);
//				}
//			}
//		});
		String number = et_number.getText().toString();
		String code = et_code.getText().toString();
		//2、使用验证码进行登陆
		BmobUser.loginBySMSCode(this, number, code, new LogInListener<MyUser>() {
			
			@Override
			public void done(MyUser user, BmobException e) {
				// TODO Auto-generated method stub
				if(user!=null){
					toast("登录成功");
					Log.i("smile", ""+user.getUsername()+"-"+user.getAge()+"-"+user.getObjectId()+"-"+user.getEmail());
				}else{
					toast("错误码："+e.getErrorCode()+",错误原因："+e.getLocalizedMessage());
				}
			}
		});
	}
	/** 一键注册登录 
	 * @method signOrLogin    
	 * @return void  
	 * @exception   
	 */
	private void signOrLogin(){
		//1、调用请求验证码接口
//		BmobSMS.requestSMSCode(this, "18312662735", "模板名称",new RequestSMSCodeListener() {
//			
//			@Override
//			public void done(Integer smsId,BmobException ex) {
//				// TODO Auto-generated method stub
//				if(ex==null){//验证码发送成功
//					Log.i("smile", "短信id："+smsId);
//				}
//			}
//
//		});
		String number = et_number.getText().toString();
		String code = et_code.getText().toString();
		//2、使用手机号和短信验证码进行一键注册登录,这步有两种方式可以选择
//		//第一种：
//		BmobUser.signOrLoginByMobilePhone(this, number, code,new LogInListener<MyUser>() {
//			
//			@Override
//			public void done(MyUser user, BmobException e) {
//				// TODO Auto-generated method stub
//				if(user!=null){
//					toast("登录成功");
//					Log.i("smile", ""+user.getUsername()+"-"+user.getAge()+"-"+user.getObjectId()+"-"+user.getEmail());
//				}else{
//					toast("错误码："+e.getErrorCode()+",错误原因："+e.getLocalizedMessage());
//				}
//			}
//		});
		//第二种：这种方式比较灵活，可以在注册或登录的同时设置保存多个字段值
		final MyUser user = new MyUser();
		user.setPassword("123456");
		user.setMobilePhoneNumber(number);
		user.signOrLogin(this, code, new SaveListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				toast("登录成功");
				Log.i("smile", ""+BmobUser.getCurrentUser(UserActivity.this,MyUser.class).getUsername()+"-"+user.getAge()+"-"+user.getObjectId()+"-"+user.getEmail());
			}
			
			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				toast("错误码："+code+",错误原因："+msg);
			}
		});
	}
	
	/** 通过短信验证码来重置用户密码 
	 * @method requestSmsCode    
	 * @return void  
	 * 注：整体流程是先调用请求验证码的接口获取短信验证码，随后调用短信验证码重置密码接口来重置该手机号对应的用户的密码
	 */
	private void resetPasswordBySMS(){
		//1、请求短信验证码
//		BmobSMS.requestSMSCode(this, "手机号码", "模板名称",new RequestSMSCodeListener() {
//		
//			@Override
//			public void done(String smsId,BmobException ex) {
//				// TODO Auto-generated method stub
//				if(ex==null){//验证码发送成功
//					Log.i("smile", "短信id："+smsId);
//				}
//			}
//		});
		String code = et_code.getText().toString();
		//2、重置的是绑定了该手机号的账户的密码
		BmobUser.resetPasswordBySMSCode(this, code,"1234567", new ResetPasswordByCodeListener() {
			
			@Override
			public void done(BmobException e) {
				// TODO Auto-generated method stub
				if(e==null){
					toast("密码重置成功");
				}else{
					toast("错误码："+e.getErrorCode()+",错误原因："+e.getLocalizedMessage());
				}
			}
		});
	}
	
	/**修改当前用户密码 
	 * @return void  
	 * @exception   
	 */
	private void updateCurrentUserPwd(){
		BmobUser.updateCurrentUserPassword(this, "旧密码", "新密码", new UpdateListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				toast("密码修改成功，可以用新密码进行登录");
			}
			
			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				toast("密码修改失败："+msg+"("+code+")");
			}
		});
	}
	
}
