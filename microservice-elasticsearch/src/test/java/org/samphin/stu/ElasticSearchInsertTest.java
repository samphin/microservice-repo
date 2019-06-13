package org.samphin.stu;

import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.UUIDs;
import org.junit.Before;
import org.junit.Test;
import org.samphin.stu.vo.Food;

import java.io.IOException;

/**
 * ElasticSearch新增功能
 */
public class ElasticSearchInsertTest {

    private RestHighLevelClient client;

    private String indexName;

    /**
     * 初始化RestHighLevelClient对象
     */
    @Before
    public void initClient() {
        client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("localhost", 9200, "http")));

        indexName = "food_info";
    }

    /**
     * 创建一个文档
     */
    @Test
    public void createDocTest() {
        try {
            Food banana = new Food.FoodBuilder(UUIDs.randomBase64UUID(), "banana", 2.5D, 8).build();
            Food apple = new Food.FoodBuilder(UUIDs.randomBase64UUID(), "apple", 4.5D, 10).build();
            Food pear = new Food.FoodBuilder(UUIDs.randomBase64UUID(), "pear", 10D, 15).build();
            CreateIndexRequest cr = new CreateIndexRequest(indexName);
            client.indices().create(cr, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
