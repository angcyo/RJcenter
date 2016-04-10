package com.example.bmobexample;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;
import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.datatype.BmobTableSchema;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.GetAllTableSchemaListener;
import cn.bmob.v3.listener.GetServerTimeListener;
import cn.bmob.v3.listener.GetTableSchemaListener;
import cn.bmob.v3.listener.ValueEventListener;

import com.example.bmobexample.acl.ACLActivity;
import com.example.bmobexample.autoupdate.ActAutoUpdate;
import com.example.bmobexample.crud.CRUDActivity;
import com.example.bmobexample.crud.QuerySQLActivity;
import com.example.bmobexample.crud.QueryStatisticActivity;
import com.example.bmobexample.file.BmobFileActivity;
import com.example.bmobexample.location.LocationActivity;
import com.example.bmobexample.newfile.NewBmobFileActivity;
import com.example.bmobexample.push.ActBmobPush;
import com.example.bmobexample.relation.WeiboListActivity;
import com.example.bmobexample.sms.SMSCodeActivity;
import com.example.bmobexample.user.UserActivity;

/**  
 *   
 * @class  MainActivity  
 * @author smile   
 * @date   2015-7-27 下午3:42:41  
 *   
 */
@SuppressLint("SimpleDateFormat")
public class MainActivity extends BaseActivity {

	protected ListView mListview;
	protected BaseAdapter mAdapter;

	/**
	 * SDK初始化建议放在启动页
	 */
	public static String APPID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Bmob.initialize(getApplicationContext(),APPID);
		Toast.makeText(this, "请记得将BmobApplication当中的APPID替换为你的appid",Toast.LENGTH_LONG).show();

		mListview = (ListView) findViewById(R.id.listview);
		mAdapter = new ArrayAdapter<String>(this, R.layout.list_item,R.id.tv_item, getResources().getStringArray(R.array.bmob_list));
		mListview.setAdapter(mAdapter);
		mListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				testBmob(position + 1);
			}
		});

		ChangeLogDialog changeLogDialog = new ChangeLogDialog(this);
		changeLogDialog.show();
	}
	
	
	private void testBmob(int pos) {
		switch (pos) {
		case 1:
			startActivity(new Intent(this, UserActivity.class));
			break;
		case 2:
			startActivity(new Intent(this, CRUDActivity.class));
			break;
		case 3:
			// 关联数据
			startActivity(new Intent(this, WeiboListActivity.class));
			break;
		case 4:
			// 批量操作
			startActivity(new Intent(this, BatchActionActivity.class));
			break;
		case 5:
			startActivity(new Intent(this, ACLActivity.class));
			break;
		case 6:
			startActivity(new Intent(this, BmobFileActivity.class));
			break;
		case 7:
			//新版文件服务
			startActivity(new Intent(this, NewBmobFileActivity.class));
			break;
		case 8:
			cloudCode();
			break;
		case 9:
			realTime();
			break;
		case 10:
			// 推送服务
			startActivity(new Intent(this, ActBmobPush.class));
			break;
		case 11:
			// 应用自动更新
			startActivity(new Intent(this, ActAutoUpdate.class));
			break;
		case 12:
			//地理位置
			startActivity(new Intent(this, LocationActivity.class));
			break;
		case 13:
			getServerTime();
			break;
		case 14://统计查询
			startActivity(new Intent(this, QueryStatisticActivity.class));			
			break;
		case 15://SQL查询
			startActivity(new Intent(this, QuerySQLActivity.class));			
			break;
		case 16://
			startActivity(new Intent(this, SMSCodeActivity.class));			
			break;
		case 17://表结构
			getAllTableSchema();
//			getTableSchema();
			break;
		}
	}

	/**
	 * 获取服务器时间
	 */
	private void getServerTime() {
		Bmob.getServerTime(MainActivity.this, new GetServerTimeListener() {

			@Override
			public void onSuccess(long time) {
				// TODO Auto-generated method stub
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm");
				String times = formatter.format(new Date(time * 1000L));
				toast("当前服务器时间为:" + times);
			}

			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				toast("获取服务器时间失败:" + msg);
			}
		});
	}

	/**
	 * 云端代码
	 */
	private void cloudCode() {
//		后台的云端代码：
//		function onRequest(request, response, modules) {
//			  //获取SDK客户端上传的name参数
//			  var name = request.body.inputData;
//			  var json = JSON.parse(name);
//			  response.end(json.Type);
//			} 
		//测试json请求参数
		AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
		JSONObject obj = new JSONObject();
		try {
			JSONObject  o= new JSONObject();
			o.put("Type", "SignUp");
			o.put("Phone", "111");
			obj.put("inputData", o);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		ace.callEndpoint(this, "t", obj, new CloudCodeListener() {
			
			@Override
			public void onSuccess(Object object) {
				// TODO Auto-generated method stub
				String result = object.toString();
				Log.i("life", "云端usertest方法返回:"+result);
				toast("云端usertest方法返回:" + result);
			}
			
			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				toast("访问云端usertest方法失败:" + msg);
			}
		});
//		//不带请求的云端代码
//		ace.callEndpoint(MainActivity.this, "testJSONObject", new CloudCodeListener() {
//
//			@Override
//			public void onSuccess(Object object) {
//				toast("云端usertest方法返回:" + object.toString());
//				String json = object.toString();
//				try {
//					JSONObject obj = new JSONObject(json);
//					String ud = obj.getString("ud");
//					toast("云端usertest方法返回ud:" + ud);
//				} catch (Exception e) {
//					// TODO: handle exception
//					toast("云端usertest方法返回错误:" + e.getMessage());
//				}
//			}
//
//			@Override
//			public void onFailure(int code, String msg) {
//				// TODO Auto-generated method stub
//				toast("访问云端usertest方法失败:" + msg);
//			}
//		});
	}
	
	private void realTime(){
		final BmobRealTimeData rtd = new BmobRealTimeData();
		rtd.start(this, new ValueEventListener() {
			
			@Override
			public void onDataChange(JSONObject data) {
				// TODO Auto-generated method stub
				Log.i("life", "onDataChange：data = "+data);
			}
			
			@Override
			public void onConnectCompleted() {
				// TODO Auto-generated method stub
				Log.d("life", "连接成功:"+rtd.isConnected());
				if(rtd.isConnected()){
				    // 监听表更新
				    rtd.subTableUpdate("Person");
				}
			}
		});
	}

	/**获取指定账户下的所有表的表结构信息  
	 * @method getAllTableSchema    
	 * @return void  
	 * @exception   
	 */
	private void getAllTableSchema(){
		Bmob.getAllTableSchema(this, new GetAllTableSchemaListener() {
			
			@Override
			public void done(List<BmobTableSchema> schemas, BmobException ex) {
				// TODO Auto-generated method stub
				if(ex==null && schemas!=null && schemas.size()>0){
					Log.i("life", ""+schemas.get(0).getClassName()+"---"+schemas.get(0).getFields().toString());
				}else{
					toast("获取所有表的表结构信息失败:" + ex.getLocalizedMessage()+"("+ex.getErrorCode()+")");
				}
			}
		});
	}
	
	/** 获取指定表的表结构信息 
	 * @method getTableSchema    
	 * @return void  
	 * @exception   
	 */
	public void getTableSchema(){
		Bmob.getTableSchema(this,"_User", new GetTableSchemaListener() {
			
			@Override
			public void done(BmobTableSchema schema, BmobException ex) {
				// TODO Auto-generated method stub
				if(ex==null){
					Log.i("life", ""+schema.getClassName()+"---"+schema.getFields().toString());
				}else{
					toast("获取用户表的表结构信息失败:" + ex.getLocalizedMessage()+"("+ex.getErrorCode()+")");
				}
			}
		});
	}
	
	public void onBackPressed() {
		finish();
	};
}
