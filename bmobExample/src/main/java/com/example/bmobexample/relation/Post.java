package com.example.bmobexample.relation;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

import com.example.bmobexample.bean.MyUser;
/**
 * 
 * @ClassName: 帖子
 * @Description: 帖子实体
 * @author smile
 * @date 2014年4月17日 上午11:10:44
 *
 */
public class Post extends BmobObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**  
	 *  帖子标题
	 */  
	private String title;
	
	/**  
	 *  帖子内容
	 */  
	private String content;
	
	/**  
	 *  微博发布者
	 */
	private MyUser author;
	/**  
	 *  微博图片
	 */
	private BmobFile image;
	
	/**  
	 *  一对多关系：用于存储喜欢该帖子的所有用户
	 */  
	private BmobRelation likes;
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public BmobRelation getLikes() {
		return likes;
	}
	public void setLikes(BmobRelation likes) {
		this.likes = likes;
	}
	public BmobFile getImage() {
		return image;
	}
	public void setImage(BmobFile image) {
		this.image = image;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public MyUser getAuthor() {
		return author;
	}
	public void setAuthor(MyUser author) {
		this.author = author;
	}
	
//	/**
//	 * 微博的评论，一条微博是对应多条评论的，像这种一对多的情形，请使用BmobRelation类型
//	 */
//	private BmobRelation comment;
	
//	public BmobRelation getComment() {
//		return comment;
//	}
//	public void setComment(BmobRelation comment) {
//		this.comment = comment;
//	}
}
