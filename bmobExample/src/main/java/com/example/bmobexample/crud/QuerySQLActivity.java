package com.example.bmobexample.crud;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.StatisticQueryListener;

import com.example.bmobexample.BaseActivity;
import com.example.bmobexample.R;
import com.example.bmobexample.bean.GameScore;
import com.example.bmobexample.bean.MyUser;

/**  
 *   
 * @class  QuerySQLActivity  
 * @author smile   
 * @date   2015-5-8 下午4:55:10  
 *   
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("SimpleDateFormat")
public class QuerySQLActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mListview = (ListView) findViewById(R.id.listview);
		mAdapter = new ArrayAdapter<String>(this, R.layout.list_item,R.id.tv_item, getResources().getStringArray(R.array.bmob_sql_list));
		mListview.setAdapter(mAdapter);
		mListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				testBmob(position + 1);
			}
		});
		//如果需要测试数据的话，可以使用QueryStatisticActivity类里面的createGameScores方法来创建
	}
	
	private void testBmob(int pos) {
		switch (pos) {
		case 1:
			querySql();
			break;
		case 2:
			queryCountSql();
			break;
		case 3:
			queryStatisticSql();
			break;
		case 4:
			queryStatmentSql();
			break;
		}
	}
	
	private void querySql(){
		//------------查询所有数据-------------------------------------------
//		String sql = "select * from GameScore";//查询所有的游戏得分记录
		
		//------------比较查询：相等、不等、大于、小于、大于等于、小于等于...-------------
//		String sql = "select * from GameScore where playScore<10";//查询玩家得分在10以下的信息
//		String sql = "select * from GameScore where game='地铁跑酷'";//查询所有玩地铁跑酷这个游戏的游戏记录
//		String sql = "select * from GameScore where game!='地铁跑酷'";//查询除地铁跑酷这个游戏以外的游戏记录
		
		//------------值是否存在查询------------------------------------------
//		String sql = "select * from GameScore where gps is not exists";//查询GameScore表中没有地理位置的所有信息(值是否存在的界定就是你没有为这个字段设置初始值，空字符串代表有值)
		
		//------------模糊查询------------------------------------------------
//		String sql = "select * from GameScore where game like 地铁%";//查询GameScore表中游戏名以地铁开头的信息
//		String sql = "select * from GameScore where game not like 地铁%";//查询GameScore表中游戏名不以地铁开头的信息
//		String sql = "select * from GameScore where game regexp 地铁.*";//查询GameScore表中游戏名以地铁开头的信息
		
		//------------数组查询------------------------------------------------
		//查询有游泳爱好的人（注意hobby是Person表中表示爱好的一个数组类型的字段）
//		String sql = "select * from Person where hobby ='游泳'";//
		//查询既爱好游泳也爱好看书的人
//		String sql = "select * from Person where hobby all ('游泳','看书')";//
//		new BmobQuery<Person>().doSQLQuery(this, sql, new SQLQueryListener<Person>(){
//			
//			@Override
//			public void done(BmobQueryResult<Person> result, BmobException e) {
//				// TODO Auto-generated method stub
//				if(e ==null){
//					List<Person> list = (List<Person>) result.getResults();
//					if(list!=null && list.size()>0){
//						for(int i=0;i<list.size();i++){
//							Person p = list.get(i);
//							Log.i("smile", ""+p.getName()+"-"+p.getAddress()+"-"+p.getAge());
//						}
//					}else{
//						Log.i("smile", "查询成功，无数据返回");
//					}
//				}else{
//					Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
//				}
//			}
//		});
		
		//内置函数：
		//------------Date查询------------------------------------------------
//		String dateString = "2015-05-12";  
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Date date  = null;
//		try {
//			date = sdf.parse(dateString);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}  
//		String sql = "select * from GameScore where createdAt < date('"+new BmobDate(date).getDate()+"')";//查询2015年5月12日之前的数据
		//上面的sql等同于下面的写法
//		String sql = "select * from GameScore where createdAt < {'__type': 'Date','iso': '"+new BmobDate(date).getDate()+"'}";//查询2015年5月12日之前的数据
		
		//------------pointer查询------------------------------------------------
		//查询当前用户的游戏记录
//		MyUser user = BmobUser.getCurrentUser(this, MyUser.class);
//		//第一种写法：
//		String sql = "select * from GameScore where player = pointer('_User', "+"'"+user.getObjectId()+"')";
//		//第二种写法：
//		String d = "{'__type':'Pointer','className':'_User','objectId':'"+user.getObjectId()+"'}";
//		String sql = "select * from GameScore where player = "+d;
		
		//------------地理位置查询-----------------------------------------------------
		//1、查询指定地理位置
		//第一种写法
//		String sql = "select * from GameScore where gps = geopoint(112.934755,24.52065)";//查询在指定地理位置的游戏记录
		//第二种写法
//		String sql = "select * from GameScore where gps = {'__type':'GeoPoint','latitude':24.52065,'longitude':112.934755}";
		
		//2、查询指定地理位置附近的信息（类似搜索附近的人）
//		String sql = "select * from GameScore where gps near geopoint(112.934755,24.52065)";//查询在指定地理位置附近的游戏记录（由近及远的排列）
//		String sql = "select * from GameScore where gps near [112.934755,24.52065]";//查询在指定地理位置附近的游戏记录
//		
//		//3、为附近的人限定最大搜索距离
//		String sql = "select * from GameScore where gps near [112.934755,24.52065] max 1 km";//查询在指定地理位置附近1km以内的游戏记录
//		
//		//4、查询矩形范围内
//		String sql = "select * from GameScore where gps within [102.934755,24.52065] and [112.934755,24.52065]";
		
		//------------include查询：------------------------------------------------
		//比如你想将游戏玩家的信息也查询出来
//		String sql = "select include player,* from GameScore limit 5";
		
		//------------子查询--------------------------------------使用in来做子查询
		//1、in后面可以是个列表，比如查询游戏名为地铁跑酷、部落冲突的游戏记录：
//		String sql = "select * from GameScore where game in ('地铁跑酷','部落冲突')";
		//2、in后面也可以是个子查询
		//比如要查询游戏得分大于10的用户数据,这里需要username的值必须要在子查询产生的数组中，因此我在GameScore表中新建了一个name字段来表示玩家的姓名
//		String sql="select * from _User where username in (select name from GameScore where playScore>10)";
		//查询年龄大于20的玩家的游戏信息
//		String sql="select * from GameScore where name in (select username from _User where age>20)";
//		比如 要查询带有图片的微博的评论列表：
		
		//------------关系查询------------------------------------------------------------
		//查询某条微博的所有评论信息-这里只是举个例子，具体可查看该工程中CommentListActivity类
		//Weibo下面有个Relation类型的字段叫comment，存储了这条微博所有的评论信息，你可以查询到这些评论信息，因为他们都关联到了同一条微博
//		String sql="select include author,* from Comment where related comment to pointer('Weibo','15e67b68ce')";
		
		//------------复合查询------------------------------------------------------------
		//1、复合与查询：查询游戏得分在10-15之间的数据
//		String sql ="select * from GameScore where playScore>10 and playScore<=15";
		//2、复合或查询：再加个条件，游戏得分为5
//		String sql ="select * from GameScore where playScore>10 and playScore<=15 or playScore=5";
		//等价于下面的语句
//		String sql ="select * from GameScore where (playScore>10 and playScore<=15) or playScore=5";
		
		//------------分页查询------------------------------------------------------------
//		String sql = "select * from GameScore limit 10,10";//比如从第 10 条开始向后查找 10 条GameScore数据
		
		//------------结果排序------------------------------------------------------------
		String sql = "select * from GameScore order by playScore,signScore desc";//根据得分（playScore）升序排序，当playScore相同情况下再根据signScore降序排列
		//------------缓存查询------------------------------------------------------------
		BmobQuery<GameScore> query = new BmobQuery<GameScore>();
		query.setSQL(sql);
		boolean isCache = query.hasCachedResult(this,GameScore.class);
		Log.i("life", "isCache = "+isCache);
		if(isCache){
			query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);	// 先从缓存取数据，如果没有的话，再从网络取。
		}else{
			query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);	// 如果没有缓存的话，则先从网络中取
		}
		//执行SQL查询操作
		query.doSQLQuery(this, sql, new SQLQueryListener<GameScore>(){
			
			@Override
			public void done(BmobQueryResult<GameScore> result, BmobException e) {
				// TODO Auto-generated method stub
				if(e ==null){
					List<GameScore> list = (List<GameScore>) result.getResults();
					if(list!=null && list.size()>0){
						for(int i=0;i<list.size();i++){
							GameScore p = list.get(i);
							Log.i("smile", ""+p.getPlayer()+"-"+p.getGame()+"-"+p.getPlayScore()+"-"+p.getSignScore());
						}
					}else{
						Log.i("smile", "查询成功，无数据返回");
					}
				}else{
					Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
				}
			}
		});
		
	}
	
	private void queryCountSql(){
//		String sql = "select count(*) from GameScore";//查询GameScore表中的总记录数
		String sql = "select count(objectId) from GameScore";//查询GameScore表中的总记录数
//		String sql = "select count(*),* from GameScore";//查询GameScore表中总记录数并返回所有记录信息
//		String sql = "select count(*),game from GameScore";//查询GameScore表中的总数并返回每条记录的游戏名（允许返回特定字段）
//		String sql = "select count(*) from GameScore where playScore>10 and where playScore<20";//查询总的记录数并返回得分在10-20之间的信息
		new BmobQuery<GameScore>().doSQLQuery(this, sql, new SQLQueryListener<GameScore>(){
			
			@Override
			public void done(BmobQueryResult<GameScore> result, BmobException e) {
				// TODO Auto-generated method stub
				if(e ==null){
					int count = result.getCount();
					Log.i("smile", "个数："+count);
					List<GameScore> list = (List<GameScore>) result.getResults();
					if(list.size()>0){
						for(int i=0;i<list.size();i++){
							GameScore p = list.get(i);
							Log.i("smile", ""+p.getPlayer()+"-"+p.getGame()+"-"+p.getPlayScore()+"-"+p.getSignScore());
						}
					}else{
						Log.i("smile", "查询成功，无数据");
					}
				}else{
					Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
				}
			}
		});
	}
	
	
	/**统计查询
	 * @method queryStatisticql 
	 * @param     
	 * @return void  
	 * @exception   
	 */
	private void queryStatisticSql(){
		String bql = "select sum(playScore) from GameScore group by name order by -createdAt";//求和,按照玩家名进行分组并按照时间降序排列
		new BmobQuery<GameScore>().doStatisticQuery(this, bql,new StatisticQueryListener(){
			
			@Override
			public void done(Object result, BmobException e) {
				// TODO Auto-generated method stub
				if(e ==null){
					JSONArray ary = (JSONArray) result;
					if(ary!=null){//
						try {
							JSONObject obj = ary.getJSONObject(0);
							int sum = obj.getInt("_sumPlayScore");
							String name = obj.getString("name");
							showToast("游戏总得分：" + sum+",name:"+name);
						} catch (JSONException e1) {
							e1.printStackTrace();
						}
					}else{
						showToast("查询成功，无数据");
					}
				}else{
					Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
				}
			}
		});
	}
	
	/**占位符查询
	 * @method queryStatmentSql 
	 * @param     
	 * @return void  
	 * @exception   
	 */
	private void queryStatmentSql(){
		MyUser user = BmobUser.getCurrentUser(this, MyUser.class);
		//查询当前用户在2015年5月12日之前，在特定地理位置附近的游戏记录
		String sql = "select * from GameScore where createdAt > date(?) and player = pointer(?,?) and gps near geopoint(?,?)";
		BmobQuery<GameScore> query = new BmobQuery<GameScore>();
		query.setSQL(sql);
		query.setPreparedParams(new Object[]{"2015-05-12 00:00:00","_User",user.getObjectId(),112.934755,24.52065});
		boolean isCache = query.hasCachedResult(this,GameScore.class);
		Log.i("life", "isCache = "+isCache);
		if(isCache){
			query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);	// 先从缓存取数据，如果没有的话，再从网络取。
		}else{
			query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);	// 如果没有缓存的话，则先从网络中取
		}
		query.doSQLQuery(this,new SQLQueryListener<GameScore>(){
			
			@Override
			public void done(BmobQueryResult<GameScore> result, BmobException e) {
				// TODO Auto-generated method stub
				if(e ==null){
					List<GameScore> list = (List<GameScore>) result.getResults();
					if(list!=null && list.size()>0){
						for(int i=0;i<list.size();i++){
							GameScore p = list.get(i);
							Log.i("smile", ""+p.getPlayer()+"-"+p.getGame()+"-"+p.getPlayScore()+"-"+p.getSignScore());
						}
					}else{
						Log.i("smile", "查询成功，无数据返回");
					}
				}else{
					Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
				}
			}
		});
		//等同于下面的查询方法
//		new BmobQuery<GameScore>().doSQLQuery(this, sql,new SQLQueryListener<GameScore>(){
//			
//			@Override
//			public void done(BmobQueryResult<GameScore> result, BmobException e) {
//				// TODO Auto-generated method stub
//				if(e ==null){
//					List<GameScore> list = (List<GameScore>) result.getResults();
//					if(list!=null && list.size()>0){
//						for(int i=0;i<list.size();i++){
//							GameScore p = list.get(i);
//							Log.i("smile", ""+p.getPlayer()+"-"+p.getGame()+"-"+p.getPlayScore()+"-"+p.getSignScore());
//						}
//					}else{
//						Log.i("smile", "查询成功，无数据返回");
//					}
//				}else{
//					Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
//				}
//			}
//		},"2015-05-12 00:00:00","_User",user.getObjectId(),112.934755,24.52065);
		
		//有关统计查询的占位符功能
//		String bql = "select sum(playScore),count(*) from GameScore group by game having _sumPlayScore>200";//按照游戏进行分组并获取总分组大于10的统计信息
//		new BmobQuery<GameScore>().doStatisticQuery(this, bql,new StatisticQueryListener(){
//			
//			@Override
//			public void done(Object result, BmobException e) {
//				// TODO Auto-generated method stub
//				if(e ==null){
//					JSONArray ary = (JSONArray) result;
//					if(ary!=null){//
//						try {
//							JSONObject obj = ary.getJSONObject(0);
//							int sum = obj.getInt("_sumPlayScore");
//							showToast("游戏总得分：" + sum);
//						} catch (JSONException e1) {
//							e1.printStackTrace();
//						}
//					}else{
//						showToast("查询成功，无数据");
//					}
//				}else{
//					Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
//				}
//			}
//		});
	}
	
}
