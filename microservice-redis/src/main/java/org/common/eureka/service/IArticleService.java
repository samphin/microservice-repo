package org.common.eureka.service;

import org.common.eureka.entity.Article;
import org.springframework.data.domain.Page;

public interface IArticleService {

	void save(Article article);

	Article findByTitle(String title);
	
	/**
	 * 根据标题模糊匹配
	 * @param title
	 * @param page
	 * @param pageSize
	 * @return
	 */
	Page<Article> findByTitleLike(String title,int page,int pageSize);
	
	/**
	 * 查询标题不为空的
	 * @param pageable
	 * @return
	 */
	public Page<Article> findByTitleNotNull(int page,int pageSize);
}
