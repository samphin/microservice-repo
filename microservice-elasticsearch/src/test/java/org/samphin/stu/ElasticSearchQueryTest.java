package org.samphin.stu;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Maps;
import org.apache.http.HttpHost;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/**
 * ElasticSearch查询详解
 */
public class ElasticSearchQueryTest extends ElasticSearchInsertTest{

    private RestHighLevelClient client;

    /**
     * 初始化RestHighLevelClient对象
     */
    @Before
    public void initClient(){
        client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("localhost",9200,"http")));
    }

    /**
     * 创建一个索引
     * @throws IOException
     */
    @Test
    public void createIndex()throws IOException{
        CreateIndexRequest index = new CreateIndexRequest(INDEX_NAME);
        Map<String,Object> properties = Maps.newHashMap();
        Map<String,Object> propertie = Maps.newHashMap();
        propertie.put("type","text");
        propertie.put("index",true);
        propertie.put("analyzer","ik_max_word");
        properties.put("field_name",propertie);

        XContentBuilder builder = JsonXContent.contentBuilder();
        builder.startObject()
                .startObject("mappings")
                .startObject("index_name")
                .field("properties",properties)
                .endObject()
                .endObject()
                .startObject("settings")
                .field("number_of_shards",3)
                .field("number_of_replicas",1)
                .endObject()
                .endObject();
        index.source(builder);
        client.indices().create(index,RequestOptions.DEFAULT);
    }

    /**
     * Es嵌套查询
     * @throws IOException
     */
    @Test
    public void nestedQueryTest()throws IOException{
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //精准匹配名字=apple水果
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name","apple");

        //内嵌字段精准匹配
        MatchPhraseQueryBuilder match = QueryBuilders.matchPhraseQuery("interest_food_agg.name", "apple");
        //内嵌查询对象
        /*NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery("interest_food_agg",match, ScoreMode.None);
        nestedQueryBuilder.ignoreUnmapped(true);*/

        boolQueryBuilder.must(termQueryBuilder).must(match);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);

        SearchRequest searchRequest = new SearchRequest(INDEX_NAME);

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = this.client.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = searchResponse.getHits();

        System.out.println("hits = " + JSONArray.toJSONString(hits.getHits()));
    }



    /**
     * 创建一个文档
     */
    @Test
    public void createDocTest(){
        String document = "{" +
                "\"user\":\"samphin\"," +
                "\"birthday\":\"1992-12-13\"," +
                "\"sign\":\"trying out Elasticsearch\"" +
                "}";
    }

    @Test
    public void getRequestTest()throws IOException {
        GetRequest getRequest = new GetRequest("user_info");
        boolean flag = client.exists(getRequest, RequestOptions.DEFAULT);
        System.out.println("flag = " + flag);
    }
}
