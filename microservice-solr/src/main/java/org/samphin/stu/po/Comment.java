package org.samphin.stu.po;

import java.util.Date;

/**
 * 评论信息
 * @author samphin
 */
public class Comment {

	/**
	 * 作者
	 */
	private String author;
	
	/**
	 * 评论内容
	 */
	private String content;
	
	/**
	 * 评论时间
	 */
	private Date createDate;
	
	public Comment() {
		super();
	}

	public Comment(String author, String content, Date createDate) {
		super();
		this.author = author;
		this.content = content;
		this.createDate = createDate;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@Override
	public String toString() {
		return "Comment [author=" + author + ", createDate=" + createDate
				+ ", content=" + content + "]";
	}
}
