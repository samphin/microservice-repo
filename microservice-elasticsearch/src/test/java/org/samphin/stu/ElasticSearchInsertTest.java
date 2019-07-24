package org.samphin.stu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.UUIDs;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Before;
import org.junit.Test;
import org.samphin.stu.vo.Food;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ElasticSearch新增功能
 */
public class ElasticSearchInsertTest {

    private RestHighLevelClient client;

    private List<Food> foodList;

    protected static final String INDEX_NAME = "food_info";
    /**
     * 初始化RestHighLevelClient对象
     */
    @Before
    public void initClient() {
        Food banana = new Food(UUIDs.randomBase64UUID(), "banana", 2.5D, 8, "yellow");
        Food apple = new Food(UUIDs.randomBase64UUID(), "apple", 4.5D, 10, "red");
        Food pear = new Food(UUIDs.randomBase64UUID(), "pear", 10D, 15, "brown");
        foodList = Stream.of(banana, apple, pear).collect(Collectors.toList());

        //初始化客户端
        client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("localhost", 9200, "http")));
    }

    /**
     * 创建一个索引
     */
    @Test
    public void createIndexTest() {
        try {
            CreateIndexRequest cr = new CreateIndexRequest(INDEX_NAME);
            client.indices().create(cr, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除一个索引
     */
    @Test
    public void deleteIndexTest() {
        try {
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(INDEX_NAME);
            client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向索引中添加文档数据
     */
    @Test
    public void createDocTest() {
        try {
            IndexRequest indexRequest = new IndexRequest(INDEX_NAME, "food_info");
            indexRequest.source(JSON.toJSONString(foodList.get(0)), XContentType.JSON);
            BulkRequest bulkRequest = new BulkRequest();
            bulkRequest.add(indexRequest);
            BulkResponse bulkItemResponses = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向索引批量插入数据
     */
    @Test
    public void batchSaveDataToIndexTest() {
        BulkRequest bulkRequest = new BulkRequest();
        foodList.forEach(food -> {
            IndexRequest indexRequest = new IndexRequest(INDEX_NAME, "food_info");
            indexRequest.source(JSON.toJSONString(food), XContentType.JSON);
            bulkRequest.add(indexRequest);
        });
        try {
            BulkResponse bulkItemResponses = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            BulkItemResponse[] items = bulkItemResponses.getItems();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改索引文档数据
     */
    @Test
    public void updateIndexDataTest() {
        try {
            UpdateRequest updateRequest = new UpdateRequest(INDEX_NAME,INDEX_NAME,"JuaM4WsBsk-Ohw-CemRY");
            Food banana = new Food();
            banana.setFoodId("VbrBvyBKTD-DEpF86dhYlQ");
            List<JSONObject> foodNameAgg = new ArrayList<>();
            JSONObject json = new JSONObject();
            json.put("name","banana2");
            json.put("amount",30);
            banana.setInterest_food_agg(foodNameAgg);
            updateRequest.doc(JSONObject.toJSONString(banana), XContentType.JSON);
            UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
