package org.samphin.stu.vo;

import lombok.Data;
import lombok.Setter;

public class Food {
    //required parameter
    private String id;

    private String name;

    private Double weight;

    private Integer count;

    //optional parameter
    private String color;

    private String size;

    public Food(FoodBuilder builder){
        this.id = builder.id;
        this.name = builder.name;
        this.weight = builder.weight;
        this.count = builder.count;
    }

    public static class FoodBuilder{
        //required parameter
        private String id;

        private String name;

        private Double weight;

        private Integer count;


        //optional parameter
        private String color;

        private String size;


        public FoodBuilder(String id,String name,Double weight,Integer count) {
            this.id = id;
            this.name = name;
            this.weight = weight;
            this.count = count;
        }

        public FoodBuilder setColor(String color) {
            this.color = color;
            return this;
        }

        public FoodBuilder setSize(String size) {
            this.size = size;
            return this;
        }

        public Food build(){
            return new Food(this);
        }
    }
}
