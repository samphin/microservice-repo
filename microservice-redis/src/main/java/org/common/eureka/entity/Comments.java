package org.common.eureka.entity;

import lombok.Data;

import java.util.List;

/**
 * 评论信息
 *
 * @author samphin
 */
@Data
public class Comments {

    private String cid;

    private List<Comment> lists;

}
