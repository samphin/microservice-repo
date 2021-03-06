package org.samphin.stu.po;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 评论信息
 * @author samphin
 */
@Document(collection="comments")
public class Comments {
	
	@Id
	private String cid;

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	private List<Comment> lists;

	public List<Comment> getLists() {
		return lists;
	}

	public void setLists(List<Comment> lists) {
		this.lists = lists;
	}
}
