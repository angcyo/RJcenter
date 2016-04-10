package com.example.bmobexample.relation;

import com.example.bmobexample.bean.MyUser;

import cn.bmob.v3.BmobObject;

/**
 * 
 * @ClassName: Comment
 * @Description: 评论实体
 * @author smile
 * @date 2014年4月17日 上午11:29:41
 *
 */
public class Comment extends BmobObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**  
	 * 评论内容  
	 */  
	private String content;
	
	/**  
	 * 评论的用户
	 */  
	private MyUser user;
	
	/**  
	 *  所评论的帖子
	 */  
	private Post post; //一个评论只能属于一个微博
	
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public MyUser getUser() {
		return user;
	}
	public void setUser(MyUser user) {
		this.user = user;
	}

}
