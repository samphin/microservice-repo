package org.samphin.stu.po;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 *  文章类
 * @author samphin
 */
@SuppressWarnings("serial")
@Document(collection="article")
public class Article implements Serializable {

	/**
	 * 通过分布式ID生成策略，雪花算法生成 文章ID
	 */
	//id属性是给mongodb用的，用@Id注解修饰
	@Id
	private String articleId;
	
	/**
	 * 文章标题
	 */
	private String title;
	
	/**
	 * 文章内容
	 */
	private String content;
	
	/**
	 * 文章发布人
	 */
	private String publishUserId;
	
	/**
	 * 文章发布时间
	 */
	private Date publishDate;
	
	/**
      * 位置 [lng(经度),lat(纬度)]
     */
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
	private Double[] location;
    
    /**
      * 通过DBRef可以将子集评论信息分开存储
     */
    @DBRef(db="test")
    private Comments comments;
    
	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPublishUserId() {
		return publishUserId;
	}

	public void setPublishUserId(String publishUserId) {
		this.publishUserId = publishUserId;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public Double[] getLocation() {
		return location;
	}

	public void setLocation(Double[] location) {
		this.location = location;
	}

	public Comments getComments() {
		return comments;
	}

	public void setComments(Comments comments) {
		this.comments = comments;
	}
}
