package com.example.bmobexample.acl;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cn.bmob.v3.BmobACL;
import cn.bmob.v3.BmobRole;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.example.bmobexample.BaseActivity;
import com.example.bmobexample.R;
import com.example.bmobexample.bean.Person;

/**  
 * ACL权限控制和角色管理
 * @class  ACLActivity  
 * @author smile   
 * @date   2015-4-13 上午11:43:55  
 * 注：具体案例可查看BmobACLDemo
 */
public class ACLActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mListview = (ListView) findViewById(R.id.listview);
		mAdapter = new ArrayAdapter<String>(this, R.layout.list_item,
				R.id.tv_item, getResources().getStringArray(
						R.array.bmob_acl_list));
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
			// 创建数据时添加ACL
			createACLData();
			break;
		case 2:
			// 创建角色
			createRole();
			break;
		case 3:
			// 更新角色
			updateRole();
			break;
		case 4:
			// 更新角色(删除角色中的用户)
			removeRole();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 创建数据时添加ACL
	 */
	private void createACLData(){
		Person person = new Person();
		person.setName("职员");
		person.setAddress("广州市");
		//添加ACL权限控制
		BmobACL aCL = new BmobACL();
		aCL.setPublicReadAccess(true);//设置所有人可读的权限
		aCL.setPublicWriteAccess(true);//设置所有人可写的权限
		aCL.setWriteAccess(BmobUser.getCurrentUser(this), true);//设置当前用户可写的权限
//		aCL.setReadAccess("用户的objectId", false);//指定特定用户不可读
//		aCL.setWriteAccess("用户的objectId", true);//指定特定用户可写
		BmobRole hr = new BmobRole("hr");
		aCL.setRoleReadAccess(hr, true);//指定hr类的人可读
		aCL.setRoleWriteAccess("hr", true);//指定hr类的人可写
		person.setACL(aCL);
		person.save(this, new SaveListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				toast("创建成功");
			}
			
			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				toast("创建失败："+msg);
			}
		});
	}
	
	/**创建角色
	 * @method createRole 
	 * @params    
	 * @return void  
	 * @exception   
	 */
	private void createRole(){
		BmobRole hr_role = new BmobRole("hr");//为当前用户分配角色
		hr_role.getUsers().add(BmobUser.getCurrentUser(this));
		hr_role.save(this, new SaveListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				toast("创建成功");
			}
			
			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				toast("创建失败："+msg);
			}
		});
	}
	
	/**
	 * 更新角色
	 */
	private void updateRole() {
		BmobRole role = new BmobRole("hr");
		role.setObjectId("6f35f87f3a");
		role.getUsers().add(BmobUser.getCurrentUser(this));
		role.update(this, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				toast("更新成功");
			}
			
			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				toast("更新失败："+msg);
			}
		});
		
	}
	
	/**删除角色
	 * @method removeRole 
	 * @params    
	 * @return void  
	 * @exception   
	 */
	private void removeRole(){
		BmobRole role = new BmobRole("hr");
		role.setObjectId("6f35f87f3a");
		role.getUsers().remove(BmobUser.getCurrentUser(this));
		role.update(this, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				toast("更新成功");
			}
			
			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				toast("更新失败："+msg);
			}
		});
	}
	
	
}
