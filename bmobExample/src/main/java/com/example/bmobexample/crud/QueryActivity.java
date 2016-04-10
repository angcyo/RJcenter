package com.example.bmobexample.crud;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

import com.example.bmobexample.BaseActivity;
import com.example.bmobexample.R;
import com.example.bmobexample.bean.Person;

/**
  * @ClassName: QueryActivity
  * @Description: 查询数据
  * @author smile
  * @date 2014-12-8 下午2:49:57
  */
@SuppressLint("SimpleDateFormat")
public class QueryActivity extends BaseActivity {
	
	protected ListView mListview;
	protected BaseAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_find);
		mListview = (ListView) findViewById(R.id.listview);
		mAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.tv_item, getResources().getStringArray(
				R.array.bmob_findtest_list));
		mListview.setAdapter(mAdapter);
		mListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				testFind(position + 1);
			}
		});
	}
	
	private void testFind(int pos) {
		switch (pos) {
			case 1:
				queryObject();
//				queryOne();
				break;
			case 2:
				queryObjects();
				break;
			case 3:
				countObjects();
				break;
			case 4:
				compositeAndQuery();
				break;
			case 5:
				compositeOrQuery();
				break;
		}
	}
	
	
	private void queryObjects(){
		final BmobQuery<Person> bmobQuery	 = new BmobQuery<Person>();
		bmobQuery.addWhereEqualTo("age", 25);
		bmobQuery.setLimit(10);
		bmobQuery.order("createdAt");
		//先判断是否有缓存
		boolean isCache = bmobQuery.hasCachedResult(QueryActivity.this,Person.class);
		if(isCache){
			bmobQuery.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);	// 先从缓存取数据，如果没有的话，再从网络取。
		}else{
			bmobQuery.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);	// 如果没有缓存的话，则先从网络中取
		}
		bmobQuery.findObjects(this, new FindListener<Person>() {

			@Override
			public void onSuccess(List<Person> object) {
				// TODO Auto-generated method stub
				toast("查询成功：共"+object.size()+"条数据。");
				for (Person person : object) {
					Log.d(TAG, "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ");
					Log.d(TAG, "ObjectId = "+person.getObjectId());
					Log.d(TAG, "Name = "+person.getName());
					Log.d(TAG, "Age = "+person.getAge());
					Log.d(TAG, "Address = "+person.getAddress());
					Log.d(TAG, "Gender = "+person.isGender());
					Log.d(TAG, "CreatedAt = "+person.getCreatedAt());
					Log.d(TAG, "UpdatedAt = "+person.getUpdatedAt());
				}
			}

			@Override
			public void onError(int code, String msg) {
				// TODO Auto-generated method stub
				toast("查询失败："+msg);
			}
		});
	}
	
	public void queryOne(){
		BmobQuery<Person> query = new BmobQuery<Person>();
		query.getObject(this, "4cab44a404", new GetListener<Person>() {
			
			@Override
			public void onSuccess(Person object) {
				// TODO Auto-generated method stub
				Log.i("life", ""+object.getName());
			}
			
			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				Log.i("life", "onFailure = "+code+",msg = "+msg);
			}
		});
	}
	
	private void queryObject(){
//		BmobQuery<Person> bmobQuery	 = new BmobQuery<Person>();
		BmobQuery<Person> bmobQuery = new BmobQuery<Person>();
		String dateString = "2015-02-11";  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		Date date  = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		bmobQuery.addWhereGreaterThan("createdAt",new BmobDate(date));
		bmobQuery.setLimit(3);
		bmobQuery.order("-createdAt");
//		bmobQuery.addWhereLessThan("createdAt",new BmobDate(date));
//		bmobQuery.addWhereEqualTo("age", 25);
//		bmobQuery.addWhereNotEqualTo("age", 25);
//		bmobQuery.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);	// 先从缓存取数据，如果没有的话，再从网络取。
		bmobQuery.findObjects(this, new FindListener<Person>() {
			
			@Override
			public void onSuccess(List<Person> objects) {
				// TODO Auto-generated method stub
				if(objects!=null && objects.size()>0){
					Person object = objects.get(0);
					toast("查询成功："+object.getObjectId());
					Log.d(TAG, "ObjectId = "+object.getObjectId());
					Log.d(TAG, "Name = "+object.getName());
					Log.d(TAG, "Age = "+object.getAge());
					Log.d(TAG, "Address = "+object.getAddress());
					Log.d(TAG, "Gender = "+object.isGender());
					Log.d(TAG, "CreatedAt = "+object.getCreatedAt());
					Log.d(TAG, "UpdatedAt = "+object.getUpdatedAt());
				}
			}

			@Override
			public void onError(int code, String arg0) {
				// TODO Auto-generated method stub
				toast("查询失败："+arg0);
			}

		});
	}
	
	private void countObjects(){
		BmobQuery<Person> bmobQuery = new BmobQuery<Person>();
		bmobQuery.count(this, Person.class, new CountListener() {
			
			@Override
			public void onSuccess(int count) {
				// TODO Auto-generated method stub
				toast("count对象个数为："+count);
			}
			
			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				toast("count对象个数失败："+msg);
			}
		});
	}
	
	/**
	  * @Description: 复合与查询：查询年龄6-29岁之间，且姓名以"y"或者"e"结尾的人
	  * @param  
	  * @return void
	  * @throws
	  */
	private void compositeAndQuery(){
		//查询年龄6-29岁之间的人，每一个查询条件都需要new一个BmobQuery对象
		//--and条件1
		BmobQuery<Person> eq1 = new BmobQuery<Person>();
		eq1.addWhereLessThanOrEqualTo("age", 29);//年龄<=29
		//--and条件2
		BmobQuery<Person> eq2 = new BmobQuery<Person>();
		eq2.addWhereGreaterThanOrEqualTo("age", 6);//年龄>=6

		//查询姓名以"y"或者"e"结尾的人--这个需要使用到or查询
		//--and条件3
		BmobQuery<Person> eq3 = new BmobQuery<Person>();
		eq3.addWhereEndsWith("name", "y");
		BmobQuery<Person> eq4 = new BmobQuery<Person>();
		eq4.addWhereEndsWith("name", "e");
		List<BmobQuery<Person>> queries = new ArrayList<BmobQuery<Person>>();
		queries.add(eq3);
		queries.add(eq4);
		BmobQuery<Person> mainQuery = new BmobQuery<Person>();
		BmobQuery<Person> or = mainQuery.or(queries);

		//最后组装完整的and条件
		List<BmobQuery<Person>> andQuerys = new ArrayList<BmobQuery<Person>>();
		andQuerys.add(eq1);
		andQuerys.add(eq2);
		andQuerys.add(or);
		//查询符合整个and条件的人
		BmobQuery<Person> query = new BmobQuery<Person>();
		query.and(andQuerys);
		query.findObjects(this, new FindListener<Person>() {
		    @Override
		    public void onSuccess(List<Person> object) {
		        // TODO Auto-generated method stub
		    	toast("查询年龄6-29岁之间，姓名以'y'或者'e'结尾的人个数："+object.size());
		    }
		    @Override
		    public void onError(int code, String msg) {
		        // TODO Auto-generated method stub
		    	toast("复合查询失败："+code+",msg:"+msg);
		    }
		    
		});
	}
	
	/**
	 * @Description: 复合或查询：查询name字段有值且不为"",查询age 等于 29 或者 age 等于 6 的人
	 * @param  
	 * @return void
	 * @throws
	 */
	private void compositeOrQuery(){
		BmobQuery<Person> and1 = new BmobQuery<Person>();
		and1.addWhereExists("name");
		BmobQuery<Person> and2 = new BmobQuery<Person>();
		and2.addWhereNotEqualTo("name", "");
		List<BmobQuery<Person>> ands = new ArrayList<BmobQuery<Person>>();
		ands.add(and1);
		ands.add(and2);
		
		BmobQuery<Person> or1 = new BmobQuery<Person>();
		or1.addWhereEqualTo("age", 6);
		BmobQuery<Person> or2 = new BmobQuery<Person>();
		or2.addWhereEqualTo("age", 29);
		List<BmobQuery<Person>> ors = new ArrayList<BmobQuery<Person>>();
		ors.add(or1);
		ors.add(or2);
		
		BmobQuery<Person> mainQuery = new BmobQuery<Person>();
		mainQuery.order("-createdAt");
		mainQuery.and(ands);
		mainQuery.or(ors);
		mainQuery.findObjects(this, new FindListener<Person>() {
			@Override
			public void onSuccess(List<Person> object) {
				// TODO Auto-generated method stub
				toast("年龄为29或者6岁人的个数："+object.size());
			}
			@Override
			public void onError(int code, String msg) {
				// TODO Auto-generated method stub
				toast("复合查询失败："+code+",msg:"+msg);
			}
		});
	}

}
