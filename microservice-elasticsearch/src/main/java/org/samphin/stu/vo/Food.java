package org.samphin.stu.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.samphin.stu.model.Base;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Food extends Base {

    private String foodId;

    private String name;

    private Double weight;

    private Integer count;

    private String color;



    public Food(String foodId, String name, Double weight, Integer count, String color) {
        this.foodId = foodId;
        this.name = name;
        this.weight = weight;
        this.count = count;
        this.color = color;
    }

    private JSONObject more_info;

    private List interest_food_agg;
}
