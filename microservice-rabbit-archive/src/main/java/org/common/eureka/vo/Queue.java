package org.common.eureka.vo;

import java.util.Map;

/**
 * 消息队列
 * @author samphin
 * @date 2019-1-10 16:30:32
 */
public class Queue {

	private final String name;

    private volatile boolean durable;

    private volatile boolean exclusive;

    private volatile boolean autoDelete;

    private volatile Map<String, Object> arguments;

    /**
     * 队列是持久的，非排他的和非自动删除的。
     * @param name 队列名
     */
    public Queue(String name) {
    	this.name = name;
    	this.durable = true;
    	this.exclusive = false;
    	this.autoDelete = false;
    }

	public boolean isDurable() {
		return durable;
	}

	public void setDurable(boolean durable) {
		this.durable = durable;
	}

	public boolean isExclusive() {
		return exclusive;
	}

	public void setExclusive(boolean exclusive) {
		this.exclusive = exclusive;
	}

	public boolean isAutoDelete() {
		return autoDelete;
	}

	public void setAutoDelete(boolean autoDelete) {
		this.autoDelete = autoDelete;
	}

	public Map<String, Object> getArguments() {
		return arguments;
	}

	public void setArguments(Map<String, Object> arguments) {
		this.arguments = arguments;
	}

	public String getName() {
		return name;
	}
}
