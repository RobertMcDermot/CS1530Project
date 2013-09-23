package com.cs1530.group4.addendum.shared;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable, Comparable<Comment>
{
	private static final long serialVersionUID = 6157099786142218688L;
	private String username, content;
	private Date commentTime, lastEdit;
	private String commentKey;
	
	public Comment(){}
	public Comment(String username, String content)
	{
		this.username = username;
		this.content = content;
		commentTime = new Date();
	}
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public Date getCommentTime()
	{
		return commentTime;
	}
	public void setCommentTime(Date commentTime)
	{
		this.commentTime = commentTime;
	}
	
	@Override
	public int compareTo(Comment other)
	{
		return commentTime.compareTo(other.commentTime);
	}
	public String getCommentKey()
	{
		return commentKey;
	}
	public void setCommentKey(String commentKey)
	{
		this.commentKey = commentKey;
	}
	public Date getLastEdit()
	{
		return lastEdit;
	}
	public void setLastEdit(Date lastEdit)
	{
		this.lastEdit = lastEdit;
	}
}
