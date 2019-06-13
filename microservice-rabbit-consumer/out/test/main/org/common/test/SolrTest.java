package org.common.test;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.common.redis.RedisApplication;
import org.common.redis.entity.OaFjmlsl;
import org.common.redis.mapper.OaFjmlslMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = RedisApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SolrTest {
	
	@Resource
	private OaFjmlslMapper mapper;
	
	/**
	 * core中持久化的MyDataConfig.xml文件中配置好表与索引映射之后，点击上传索引数据
	 */
	@Test
	public void importIndexData(){
		try {
			//构建solrj客户端
			HttpSolrClient client = new HttpSolrClient("http://localhost:8888/solr/searchCore");
			//查询数据库获取对应表数据
			Map<String,String> params = new HashMap<String, String>();
			params.put("fjmlmc","A");
			params.put("cjrmc","舒洁");
			List<OaFjmlsl> dataList = mapper.selectAll(params);
			for (OaFjmlsl item : dataList) {
			    //创建文档对象
			    SolrInputDocument document = new SolrInputDocument();
			    //添加域
			    document.addField("id", item.getFjmlid());
			    document.addField("fjmlmc", item.getFjmlmc());
			    document.addField("cjrmc", item.getCjrmc());
			    document.addField("cjsj", item.getCjsj());
			    //写入索引库
			    client.add(document);
			}
			//提交
			client.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void emptyIndexData(){
		try {
			//构建solrj客户端
			HttpSolrClient client = new HttpSolrClient("http://localhost:8888/solr/searchCore/update");
			//封装查询对象
			ContentStreamUpdateRequest up = new ContentStreamUpdateRequest("/dataimport");	
			up.setParam("command", "full-import");  	    
			//执行查询并返回response对象
			client.request(up);
			client.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过关键字查询索引数据
	 * coreName：searchCore
	 */
	@Test
	public void solrQuery(){
		try {
			//构建solrj客户端
			HttpSolrClient client = new HttpSolrClient("http://localhost:8888/solr/searchCore");
			//封装查询对象
			SolrQuery query = new SolrQuery("fjmlmc:*A*");
			query.setStart(0);
			query.setRows(200);
			query.setSort("cjsj",ORDER.desc);//根据结果集cjsj字段排序
			//执行查询并返回response对象
			QueryResponse response = client.query(query);
			SolrDocumentList list = response.getResults();
			//遍历查询结果并输出
			for (SolrDocument solrDocument : list) {
				String fjmlmc = (String)solrDocument.getFieldValue("fjmlmc");
				String cjrmc = (String)solrDocument.getFieldValue("cjrmc");
				String fjmlid = (String)solrDocument.getFieldValue("id");
				System.out.println("编号："+fjmlid+"，名称："+fjmlmc+"，创建人："+cjrmc);
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建索引文件
	 */
	@Test
	public void solrCreateFile(){
		try {
			//构建solrj客户端
			HttpSolrClient server = new HttpSolrClient("http://localhost:8888/solr/searchCore");
			ContentStreamUpdateRequest up = new ContentStreamUpdateRequest("/update/extract");		
			up.addFile(new File("F:/广州移动端接口设计说明文档2018-9-26.doc"), "application/word");		
			up.setParam("literal.id", "doc");  	    
			up.setParam("fmap.content", "attr_content");  	    
			up.setParam("fmap.content_type", "documentFormat");	    
			up.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);	    
			server.request(up);	    
			List<String> list = new ArrayList<String>();		
			// 创建数据
			//SolrInputDocument doc = new SolrInputDocument();
			//doc.addField("id", "2");
			//doc.addField("title", "king");
			//doc.addField("author", " asdfsdfsd  asdasdsaking king king king king");
			//doc.addField("",new File(""));
			//server.add(doc);		
			server.commit();
			
			//上传完后，并查询
			SolrQuery query = new SolrQuery("*");
			query.setStart(0);
			query.setRows(200);
			//执行查询并返回response对象
			QueryResponse response = server.query(query);
			SolrDocumentList results = response.getResults();
			System.out.println(results.getNumFound());// 查询总条数
			//遍历查询结果并输出
			for (SolrDocument solrDocument : results) {
				System.out.println(solrDocument);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 全文检索
	 */
	@Test
	public void fullContextQuery(){
		try {
			//构建solrj客户端
			HttpSolrClient client = new HttpSolrClient("http://localhost:8888/solr/mysqlcore");
			//封装查询对象
			SolrQuery query = new SolrQuery("path:A");
			query.setStart(0);
			query.setRows(200);
			query.setSort("createTime",ORDER.desc);//根据结果集cjsj字段排序
			//执行查询并返回response对象
			QueryResponse response = client.query(query);
			SolrDocumentList list = response.getResults();
			//遍历查询结果并输出
			for (SolrDocument solrDocument : list) {
				String id = (String)solrDocument.getFieldValue("id");
				String name = (String)solrDocument.getFieldValue("name");
				String path = (String)solrDocument.getFieldValue("path");
				Date createTime = (Date)solrDocument.getFieldValue("createTime");
				System.out.println("编号："+id+"，名称："+name+"，路径："+path+"，创建时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
