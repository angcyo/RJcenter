package com.example.bmobexample.crud;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.example.bmobexample.BaseActivity;
import com.example.bmobexample.R;
import com.example.bmobexample.bean.BankCard;
import com.example.bmobexample.bean.Person;

/**增删改查
 * @author Administrator
 *
 */
public class CRUDActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mListview = (ListView) findViewById(R.id.listview);
		mAdapter = new ArrayAdapter<String>(this, R.layout.list_item,
				R.id.tv_item, getResources().getStringArray(
						R.array.bmob_crud_list));
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
			testinsertObject();
			break;
		case 2:
			testUpdateObjet();
			break;
		case 3:
			testDeleteObject();
			break;
		case 4:
			startActivity(new Intent(this, QueryActivity.class));
			break;
		}
	}
	
	public static String objectId="";
	/**
	 * 插入对象
	 */
	private void testinsertObject() {
		final Person p2 = new Person();
		p2.setName("lucky");
		p2.setAddress("北京市海淀区");
		p2.setAge(25);
		//添加Object类型
		p2.setBankCard(new BankCard("哈哈", "111"));
		//添加Object类型的数组
		List<BankCard> cards =new ArrayList<BankCard>();
		for(int i=0;i<2;i++){
			cards.add(new BankCard("建行", "111"+i));
		}
		p2.addAll("cards", cards);
		//添加String类型的数组
		p2.addAll("hobby", Arrays.asList("游泳", "看书"));    // 一次添加多个值到hobby字段中
//		p2.add("cards",new BankCard("建行", "111"));//一次添加单个值
		p2.setGpsAdd(new BmobGeoPoint(112.934755, 24.52065));
		p2.setUploadTime(new BmobDate(new Date()));
		p2.save(this, new SaveListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				objectId = p2.getObjectId();
				toast("创建数据成功：" + p2.getObjectId());
				Log.d("bmob", "objectId = " + p2.getObjectId());
				Log.d("bmob", "name =" + p2.getName());
				Log.d("bmob", "age =" + p2.getAge());
				Log.d("bmob", "address =" + p2.getAddress());
				Log.d("bmob", "gender =" + p2.isGender());
				Log.d("bmob", "createAt = " + p2.getCreatedAt());
			}

			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				toast("创建数据失败：" + msg+"("+code+")");
			}
		});
	}

	/**
	 * 更新对象
	 */
	private void testUpdateObjet() {
		final Person p2 = new Person();
		//更新数组中的某个位置的对象值
		p2.setValue("cards.0", new BankCard("cards.0", "cards.0的值"));
		//更新对象数组中指定对象的指定字段的值
//		p2.setValue("cards.0.bankName", "银行卡");
//		p2.setValue("cards.0.cardNumber", "卡号");
//		p2.setValue("cards.1.bankName", "银行卡");
		//更新BmobObject的值
//		p2.setValue("author", BmobUser.getCurrentUser(this, MyUser.class));
		//更新Object类型的数组
//		List<BankCard> cards =new ArrayList<BankCard>();
//		for(int i=0;i<2;i++){
//			cards.add(new BankCard("叫姐姐"+i, "111"+i));
//		}
//		p2.setValue("cards",cards);
		//更新Object对象
		p2.setValue("bankCard",new BankCard("bankCard", "bankCard的值"));
		//更新Object对象的值
//		p2.setValue("bankCard.bankName","你妹");
		//更新Integer类型
//		p2.setValue("age",11);
//		p2.setValue("gender", true);
		p2.update(this, objectId, new UpdateListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				toast("更新成功：" + p2.getUpdatedAt());
			}

			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				toast("更新失败：" + msg);
			}
		});

	}

	/**
	 * 删除对象
	 */
	private void testDeleteObject() {
		Person p2 = new Person();
		p2.removeAll("cards", Arrays.asList(new BankCard("建行", "111")));
		p2.setObjectId(objectId);
		p2.update(this, new UpdateListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				toast("删除成功");
			}

			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				toast("删除失败：" + msg);
			}
		});
	}
	
	
}
