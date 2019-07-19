package org.common.eureka.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 评论信息
 * @author samphin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
	
}
