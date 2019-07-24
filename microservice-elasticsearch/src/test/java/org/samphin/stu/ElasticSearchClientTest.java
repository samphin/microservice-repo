package org.samphin.stu;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Maps;
import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@SpringBootTest(classes = ElasticSearchServerApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ElasticSearchClientTest extends ElasticSearchInsertTest {

    @Autowired
    private RestHighLevelClient client;

    /**
     * 创建一个索引
     *
     * @throws IOException
     */
    @Test
    public void createIndexTest() {
        try {
            CreateIndexRequest index = new CreateIndexRequest(INDEX_NAME);
            Map<String, Object> properties = Maps.newHashMap();
            Map<String, Object> propertie = Maps.newHashMap();
            propertie.put("type", "doc");
            propertie.put("index", true);
            propertie.put("analyzer", "ik_max_word");
            properties.put("field_name", propertie);

            XContentBuilder builder = JsonXContent.contentBuilder();
            builder.startObject()
                    .startObject("mappings")
                    .startObject(INDEX_NAME)
                    .field("properties", properties)
                    .endObject()
                    .endObject()
                    .startObject("settings")
                    .field("number_of_shards", 3)
                    .field("number_of_replicas", 1)
                    .endObject()
                    .endObject();
            index.source(builder);
            client.indices().create(index, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否存在该索引
     * @throws IOException
     */
    @Test
    public void getRequestTest() throws IOException {
        GetRequest getRequest = new GetRequest(INDEX_NAME,INDEX_NAME,"JOaM4WsBsk-Ohw-CemRY");
        boolean flag = client.exists(getRequest, RequestOptions.DEFAULT);
        System.out.println((flag==true?"存在":"不存在")+"该索引~");
    }

    /**
     * 查询食物名称为banana
     */
    @Test
    public void test1(){
        try {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

            boolQueryBuilder.must(QueryBuilders.termsQuery("name","banana","apple"));

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(boolQueryBuilder);

            SearchRequest searchRequest = new SearchRequest(INDEX_NAME).source(searchSourceBuilder).types(INDEX_NAME);

            SearchResponse searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);

            SearchHits searchHits = searchResponse.getHits();

            SearchHit[] searchHit = searchHits.getHits();

            System.out.println("dataCount = " + searchHits.getTotalHits());
            Arrays.stream(searchHit).forEach(hit->{
                System.out.println(hit.getSourceAsString());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
