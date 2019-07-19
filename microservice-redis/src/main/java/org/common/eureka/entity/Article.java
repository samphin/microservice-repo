package org.common.eureka.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文章类
 *
 * @author samphin
 */
@Data
public class Article implements Serializable {

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
    private Double[] location;

    /**
     * 通过DBRef可以将子集评论信息分开存储
     */
    private Comments comments;

}
