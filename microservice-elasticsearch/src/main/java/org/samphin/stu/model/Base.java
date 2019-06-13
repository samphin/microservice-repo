package org.samphin.stu.model;

/**
 * Elasticsearch Base基类
 */
public class Base {
    /**
     * 索引
     */
    private String index;

    /**
     * 索引类型
     */
    private String type;

    /**
     * 索引ID
     */
    private String id;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
