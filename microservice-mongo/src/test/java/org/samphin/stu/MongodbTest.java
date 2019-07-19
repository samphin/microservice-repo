package org.samphin.stu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.WriteConcern;
import com.mongodb.client.result.UpdateResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.samphin.stu.dao.ArticleRepository;
import org.samphin.stu.po.Article;
import org.samphin.stu.po.Comment;
import org.samphin.stu.po.Comments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Update.PushOperatorBuilder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * MongoDB测试用例
 */
@SpringBootTest(classes = MongoApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MongodbTest {

    Logger logger = LoggerFactory.getLogger(MongodbTest.class);

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 发布文章
     */
    @Test
    public void publishArticle() {
        Article article = new Article();
        article.setTitle("Spring JPA");
        article.setContent("自 JPA 伴随 Java EE 5 发布以来，受到了各大厂商及开源社区的追捧，各种商用的和开源的 JPA 框架如雨后春笋般出现，为开发者提供了丰富的选择。它一改之前 EJB 2.x 中实体 Bean 笨重且难以使用的形象，充分吸收了在开源社区已经相对成熟的 ORM 思想。另外，它并不依赖于 EJB 容器，可以作为一个独立的持久层技术而存在。目前比较成熟的 JPA 框架主要包括 Jboss 的 Hibernate EntityManager、Oracle 捐献给 Eclipse 社区的 EclipseLink、Apache 的 OpenJPA 等。");
        article.setPublishUserId("10001");
        article.setPublishDate(new Date());
        mongoTemplate.save(article);
        logger.info("===============文章发布成功");
    }

    /**
     * 查询文章内容包含spring关键字，且标题是SpringBoot的文章，并更新文章的作者ID，添加作者
     */
    @Test
    public void updateArticlePublishUserId() {
        String content = "Spring";
        String title = "SpringBoot";
        /**
         * 经过查看文档和源代码才发现，Criteria的where方法是一个静态工厂方法，它会返回一个实例化的criteria对象，
         * 所以就不需要自己new 一个criteria对象了。
         * 否则 find(new Query(criteria)里的criteria没有任何判断条件，因此会返回所有的数据。
         */
        Criteria criteria = Criteria.where("title").is(title).and("content").regex(".*" + content + ".*");
        //定义查询器
        Query query = new Query(criteria);
        //建造者模式，修改已存在的字段publishUserId=10003，并添加一个新字段author=james
        Update update = new Update().set("publishUserId", "10003").set("author", "james");
        //更新查询返回结果集的第一条
        //this.mongoTemplate.updateFirst(query, update, Article.class);
        //更新查询返回结果集的所有
        this.mongoTemplate.updateMulti(query, update, Article.class);
        logger.info("==============操作成功");
    }

    /**
     * 查询文章内容包含spring关键字，且标题是SpringCloud的文章，并向catalogs数组中再添加两条元素
     */
    @Test
    public void pushArticleArray() {
        String content = "EJB";
        String title = "Spring JPA";
        /**
         * 经过查看文档和源代码才发现，Criteria的where方法是一个静态工厂方法，它会返回一个实例化的criteria对象，
         * 所以就不需要自己new 一个criteria对象了。
         * 否则 find(new Query(criteria)里的criteria没有任何判断条件，因此会返回所有的数据。
         */
        Criteria criteria = Criteria.where("title").is(title).and("content").regex(".*" + content + ".*");
        //定义查询器
        Query query = new Query(criteria);
        Object[] catalogs = {"Spring JPA高级应用", "Spring JPA与SpringBoot集成"};
        Object[] catalog_new = {"Spring JPA概况", "Spring JPA入门", "Spring JPA应用与实践", "Spring JPA应用与实战性能优化"};
        Update update = new Update().pushAll("catalogs", catalog_new);//.each(catalog_new) ;
        //更新查询返回结果集的所有
        this.mongoTemplate.updateMulti(query, update, Article.class);
        logger.info("======操作成功");
    }

    /**
     * 查询文章内容包含spring关键字，且标题是SpringCloud的文章，并向catalogs数组中再添加两条元素，并对插入的数据进行排序
     */
    @Test
    public void pushArticleArrayForSort() {
        try {
            String content = "EJB";
            String title = "Spring JPA";
            Criteria criteria = Criteria.where("title").is(title).and("content").regex(".*" + content + ".*");
            Query query = new Query(criteria);
            Update update = new Update();
            PushOperatorBuilder pob = update.push("comments");
            //准备三条评论
            List<Comment> comments = new LinkedList<Comment>();
            comments.add(new Comment("wade", "wade评论", DateUtils.parseDate("2019-02-07 10:36:30", "yyyy-MM-dd HH:mm:ss")));
            comments.add(new Comment("james", "james评论", DateUtils.parseDate("2019-04-01 11:36:30", "yyyy-MM-dd HH:mm:ss")));
            comments.add(new Comment("kobe", "kobe评论", DateUtils.parseDate("2019-04-07 13:36:30", "yyyy-MM-dd HH:mm:ss")));
            pob.each(comments.toArray(new Object[]{}));
            pob.sort(new Sort(Direction.DESC, "createDate"));//根据评论创建时间倒序排，并插入
            //更新查询返回结果集的所有
            this.mongoTemplate.updateFirst(query, update, Article.class);
            logger.info("======操作成功");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询文章内容包含spring关键字，且标题是SpringCloud的文章，并向catalogs数组中再添加两条元素，并对插入的数据进行排序
     */
    @Test
    public void pushArticleArrayForSort2() {
        try {
            String content = "EJB";
            String title = "Spring JPA";
            Criteria criteria = Criteria.where("title").is(title).and("content").regex(".*" + content + ".*");
            Query query = new Query(criteria);
            Update update = new Update();
            PushOperatorBuilder pob = update.push("comments");
            //准备三条评论
            List<Comment> comments = new LinkedList<Comment>();
            comments.add(new Comment("paul", "paul评论", DateUtils.parseDate("2019-02-07 14:30:30", "yyyy-MM-dd HH:mm:ss")));
            pob.each(comments.toArray(new Object[]{}));
            pob.sort(new Sort(Direction.DESC, "createDate"));//根据评论创建时间倒序排，并插入
            //更新查询返回结果集的所有
            this.mongoTemplate.updateFirst(query, update, Article.class);
            logger.info("======操作成功");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改Sprig JPA这篇文章中，评论信息作者是paul的评论信息
     */
    @Test
    public void updateArticleCommentArrayElement() {
        String content = "EJB";
        String title = "Spring JPA";
        Criteria criteria = Criteria.where("title").is(title).and("content").regex(".*" + content + ".*");
        Query query = new Query(criteria);
        //通过评论对象来拼接匹配条件
        Comment comment = new Comment();
        comment.setAuthor("paul");
        Update update = new Update().set("comments.$.content", "paul评论，666");
        //更新查询返回结果集的所有
        this.mongoTemplate.updateFirst(query, update, "article");
        logger.info("======操作成功");
    }

    /**
     * 修改Sprig JPA这篇文章中标题信息，然后返回修改后新数据信息
     */
    @Test
    public void updateArticleTitleForFindAndModify() {
        Criteria criteria = Criteria.where("_id").is(new ObjectId("5ca80dcb15c702c0a84283a5"));
        Query query = new Query(criteria);
        Update update = new Update().set("title", "Nginx教程");
        FindAndModifyOptions fmo = FindAndModifyOptions.options().returnNew(true);
        //更新查询返回结果集的所有
        Article newArticle = this.mongoTemplate.findAndModify(query, update, fmo, Article.class);
        logger.info("======操作成功，新文章信息：" + JSON.toJSONString(newArticle));
    }

    /**
     * 删除Sprig JPA这篇文章中，评论信息作者是paul的评论信息
     */
    @Test
    public void deleteArticleCommentArrayElement() {
        String content = "EJB";
        String title = "Spring JPA";
        Criteria criteria = Criteria.where("title").is(title).and("content").regex(".*" + content + ".*");
        Query query = new Query(criteria);
        //通过评论对象来拼接匹配条件
        Comment comment = new Comment();
        comment.setAuthor("paul");
        Update update = new Update().pull("comments", comment);
        //更新查询返回结果集的所有
        this.mongoTemplate.updateFirst(query, update, "article");
        logger.info("======操作成功");
    }

    /**
     * 查询文章内容包含spring关键字，且标题是SpringCloud的文章，并删除catalogs数据中匹配到的元素
     */
    @Test
    public void deleteArticleArray() {
        String content = "EJB";
        String title = "Spring JPA";
        /**
         * 经过查看文档和源代码才发现，Criteria的where方法是一个静态工厂方法，它会返回一个实例化的criteria对象，
         * 所以就不需要自己new 一个criteria对象了。
         * 否则 find(new Query(criteria)里的criteria没有任何判断条件，因此会返回所有的数据。
         */
        Criteria criteria = Criteria.where("title").is(title).and("content").regex(".*" + content + ".*");
        //定义查询器
        Query query = new Query(criteria);
        Object[] catalogs = {"Spring JPA概况", "Spring JPA入门", "Spring JPA应用与实践", "Spring JPA应用与实战性能优化"};
        Update update = new Update().pull("catalogs", catalogs);
        //更新查询返回结果集的所有
        this.mongoTemplate.updateMulti(query, update, Article.class);
        logger.info("======操作成功");
    }

    /**
     * 查询文章内容包含spring关键字，且标题是SpringCloud的文章，并删除catalogs数据中匹配到的元素
     */
    @Test
    public void deleteArticleForArrayElement() {
        String content = "Spring";
        String title = "SpringCloud";
        /**
         * 经过查看文档和源代码才发现，Criteria的where方法是一个静态工厂方法，它会返回一个实例化的criteria对象，
         * 所以就不需要自己new 一个criteria对象了。
         * 否则 find(new Query(criteria)里的criteria没有任何判断条件，因此会返回所有的数据。
         */
        Criteria criteria = Criteria.where("title").is(title).and("content").regex(".*" + content + ".*");
        //定义查询器
        Query query = new Query(criteria);
        Object[] catalogs = {"SpringCloud概况", "SpringCloud入门", "SpringCloud应用与实践", "SpringCloud应用与实战性能优化", "SpringCloud集成Docker"};
        Object[] deleteCatalogs = {"SpringCloud应用与实战性能优化", "SpringCloud集成Docker"};
        Update update = new Update().pullAll("catalogs", deleteCatalogs);
        //更新查询返回结果集的所有
        this.mongoTemplate.updateMulti(query, update, Article.class);
        logger.info("======操作成功");
    }

    /**
     * 查询文章内容包含spring关键字，且标题是SpringBoot的文章，并更新catalogs数组节点
     */
    @Test
    public void updateArticleContent() {
        String content = "Spring";
        String title = "SpringBoot";
        /**
         * 经过查看文档和源代码才发现，Criteria的where方法是一个静态工厂方法，它会返回一个实例化的criteria对象，
         * 所以就不需要自己new 一个criteria对象了。
         * 否则 find(new Query(criteria)里的criteria没有任何判断条件，因此会返回所有的数据。
         */
        Criteria criteria = Criteria.where("title").is(title).and("content").regex(".*" + content + ".*");
        //定义查询器
        Query query = new Query(criteria);
        Object[] catalogs = {"SpringBoot概况", "SpringBoot入门", "SpringBoot应用与实践", "SpringBoot高级部分", "SpringBoot集成Docker"};
        Update update = new Update().pushAll("catalogs", catalogs);
        //更新查询返回结果集的第一条
        //this.mongoTemplate.updateFirst(query, update, Article.class);
        //更新查询返回结果集的所有
        //动成给连接对象设置写策略
        WriteConcern wc = WriteConcern.W1.withJournal(true);
        this.mongoTemplate.setWriteConcern(wc);
        UpdateResult wr = this.mongoTemplate.updateMulti(query, update, "article");
        logger.info("==============" + wr.getModifiedCount() + "===" + wr.getUpsertedId());
        logger.info("==============操作成功");
    }

    /**
     * 保存文章信息（包含DBRef子集保存、经纬度位置信息）
     */
    @Test
    public void saveArticleForComments() {
        Article article = new Article();
        article.setTitle("Nginx教程");
        article.setContent("Nginx (engine x) 是一个高性能的HTTP和反向代理服务，也是一个IMAP/POP3/SMTP服务。Nginx是由伊戈尔·赛索耶夫为俄罗斯访问量第二的Rambler.ru站点（俄文：Рамблер）开发的，第一个公开版本0.1.0发布于2004年10月4日。" +
                "其将源代码以类BSD许可证的形式发布，因它的稳定性、丰富的功能集、示例配置文件和低系统资源的消耗而闻名。2011年6月1日，nginx 1.0.4发布。" +
                "Nginx是一款轻量级的Web 服务器/反向代理服务器及电子邮件（IMAP/POP3）代理服务器，并在一个BSD-like 协议下发行。其特点是占有内存少，并发能力强，事实上nginx的并发能力确实在同类型的网页服务器中表现较好，中国大陆使用nginx网站用户有：百度、京东、新浪、网易、腾讯、淘宝等。");
        article.setPublishUserId("10001");
        article.setPublishDate(new Date());
        Comments Comments = new Comments();
        Comments.setCid("c10001");
        article.setComments(Comments);
        //当前位置经纬度
        Double[] location = {114.3162001, 30.58108413};
        article.setLocation(location);
        this.mongoTemplate.save(article);
        logger.info("=========DBRef保存成功！");
    }

    /**
     * DBRef查询
     */
    @Test
    public void queryArticleForComments() {
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is("Nginx教程"));
        List<Article> articles = this.mongoTemplate.find(query, Article.class);
        for (Iterator iterator = articles.iterator(); iterator.hasNext(); ) {
            Article article = (Article) iterator.next();
            System.out.println("=======" + JSON.toJSON(article));
        }
    }

    /**
     * 保存评论信息
     */
    @Test
    public void saveComments() {
        //清空集合
        List<Comment> comments = new LinkedList<Comment>();
        comments.add(new Comment("wade1", "wade评论1", new Date(System.currentTimeMillis())));
        comments.add(new Comment("wade2", "wade评论2", new Date(System.currentTimeMillis())));
        comments.add(new Comment("wade3", "wade评论3", new Date(System.currentTimeMillis())));
        comments.add(new Comment("james", "james评论", new Date(System.currentTimeMillis())));
        comments.add(new Comment("kobe", "kobe评论", new Date(System.currentTimeMillis())));
        Comments Comments = new Comments();
        Comments.setCid("c10002");
        Comments.setLists(comments);
        this.mongoTemplate.save(Comments);
        logger.info("========评论信息保存成功！");
    }

    /**
     * 给已有文章添加评论
     */
    @Test
    public void setArticleComments() {
        String content = "Spring";
        String title = "SpringBoot";
        /**
         * 经过查看文档和源代码才发现，Criteria的where方法是一个静态工厂方法，它会返回一个实例化的criteria对象，
         * 所以就不需要自己new 一个criteria对象了。
         * 否则 find(new Query(criteria)里的criteria没有任何判断条件，因此会返回所有的数据。
         */
        Criteria criteria = Criteria.where("title").is(title).
                and("content").regex(".*" + content + ".*");
        //定义查询器
        Query query = new Query(criteria);
        List<Comment> comments = new LinkedList<Comment>();
        comments.add(new Comment("samphin1", "SpringBoot评论1", new Date(System.currentTimeMillis())));
        comments.add(new Comment("samphin2", "SpringBoot评论2", new Date(System.currentTimeMillis())));
        comments.add(new Comment("samphin3", "SpringBoot评论3", new Date(System.currentTimeMillis())));
        comments.add(new Comment("samphin4", "SpringBoot评论4", new Date(System.currentTimeMillis())));
        comments.add(new Comment("samphin5", "SpringBoot评论5", new Date(System.currentTimeMillis())));
        comments.add(new Comment("samphin6", "SpringBoot评论6", new Date(System.currentTimeMillis())));
        Update update = new Update().addToSet("comments").each(comments.toArray(new Object[]{}));
        //更新查询返回结果集的第一条
        //this.mongoTemplate.updateFirst(query, update, Article.class);
        //更新查询返回结果集的所有
        this.mongoTemplate.updateMulti(query, update, Article.class);
    }

    /**
     * 查询文章内容包含spring关键字，且标题是SpringBoot的文章，并删除其中部分节点数据
     */
    @Test
    public void deleteArticleContent() {
        String content = "Spring";
        String title = "SpringBoot";
        /**
         * 经过查看文档和源代码才发现，Criteria的where方法是一个静态工厂方法，它会返回一个实例化的criteria对象，
         * 所以就不需要自己new 一个criteria对象了。
         * 否则 find(new Query(criteria)里的criteria没有任何判断条件，因此会返回所有的数据。
         */
        Criteria criteria = Criteria.where("title").is(title).
                and("content").regex(".*" + content + ".*");
        //定义查询器
        Query query = new Query(criteria);
        Update update = new Update().unset("catalogs.fisrt");
        //更新查询返回结果集的第一条
        //this.mongoTemplate.updateFirst(query, update, Article.class);
        //更新查询返回结果集的所有
        this.mongoTemplate.updateMulti(query, update, Article.class);
    }


    /**
     * 精准匹配查询文章信息
     */
    @Test
    public void findArticleByTitle() {
        String title = "CentOS7_NAT模式网络配置";
        Article article = this.articleRepository.findByTitle(title);
        logger.info("===============文章信息：" + JSONObject.toJSONString(article));
    }

    @Test
    public void findArticle() {
        String keyword = "SpringBoot";
        int page = 1;
        int pageSize = 10;
        Criteria criteria = new Criteria();
        if (StringUtils.isNotBlank(keyword)) {
            criteria.orOperator(Criteria.where("title").regex(".*?" + keyword + ".*"));
        }
        Query query = new Query(criteria);
        query.skip((page - 1) * pageSize);
        query.limit(pageSize);
        query.with(new Sort(new Sort.Order(Direction.ASC, "title")));
        List<Article> articles = this.mongoTemplate.find(query, Article.class);
        long count = this.mongoTemplate.count(query, Article.class);
        logger.info("===查询结果：" + JSON.toJSONString(articles));
        logger.info("===查询记录数：" + count);
    }

    /**
     * 查询文章内容包含spring关键字，且标题是SpringBoot的文章
     */
    @Test
    public void findArticleContent() {
        String content = "Spring";
        String title = "SpringBoot";
        /**
         * 经过查看文档和源代码才发现，Criteria的where方法是一个静态工厂方法，它会返回一个实例化的criteria对象，
         * 所以就不需要自己new 一个criteria对象了。
         * 否则 find(new Query(criteria)里的criteria没有任何判断条件，因此会返回所有的数据。
         */
        Criteria criteria = Criteria.where("title").is(title).
                and("content").regex(".*" + content + ".*");
        //定义查询器
        Query query = new Query(criteria);
        List<Article> articles = this.mongoTemplate.find(query, Article.class);
        logger.info("===查询结果：" + JSON.toJSONString(articles));
    }

    /**
     * 模糊匹配查询文章信息
     */
    @Test
    public void findArticleForLike() {
        Article article = new Article();
        article.setTitle("SpringBoot");
        PageRequest pageRequest = new PageRequest(0, 10);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true) //改变默认大小写忽略方式：忽略大小写
                .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains()) //采用“包含匹配”的方式查询
                .withIgnorePaths("publishDate", "publishUserId");  //忽略属性，不参与查询;
        Example<Article> example = Example.of(article, matcher);
        Page<Article> datas = articleRepository.findAll(example, pageRequest);
        logger.info("===============文章信息：" + JSONObject.toJSONString(datas));
    }
}
