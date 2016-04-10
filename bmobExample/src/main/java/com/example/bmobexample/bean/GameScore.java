package com.example.bmobexample.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**  
 * 游戏得分
 * @class  GameScore  
 * @author smile   
 * @date   2015-4-23 上午11:56:20  
 *   
 */
public class GameScore extends BmobObject {
	/**  
	 *  
	 */  
	private static final long serialVersionUID = 1L;
	
	public GameScore(){
		super();
	}
	
	public GameScore(String tableName){
		super(tableName);
	} 
	
	/**  
	 * 玩家
	 */  
	private MyUser player;
	
	/**  
	 * 玩家昵称--对应User表的用户名
	 */  
	private String name;
	/**  
	 * 游戏得分  
	 */  
	private Integer playScore;
	
	/**  
	 * 签到得分
	 */  
	private Integer signScore;
	
	/**  
	 * 游戏（玩家所玩的游戏）  
	 */  
	private String game;
	
	private BmobGeoPoint gps;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BmobGeoPoint getGps() {
		return gps;
	}

	public void setGps(BmobGeoPoint gps) {
		this.gps = gps;
	}

	public MyUser getPlayer() {
		return player;
	}

	public void setPlayer(MyUser player) {
		this.player = player;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public Integer getPlayScore() {
		return playScore;
	}
	
	public void setPlayScore(Integer playScore) {
		this.playScore = playScore;
	}
	
	public Integer getSignScore() {
		return signScore;
	}
	
	public void setSignScore(Integer signScore) {
		this.signScore = signScore;
	}
	
}
