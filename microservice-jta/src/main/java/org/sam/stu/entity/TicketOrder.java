package org.sam.stu.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 飞机票订单表
 * 
 * @author samphin
 */
public class TicketOrder {

	private String id;

	private String orderCode;

	private String useCode;

	private Date orderTime;

	private BigDecimal price;

	public TicketOrder() {
		super();
	}

	public TicketOrder(String id, String orderCode, String useCode, Date orderTime, BigDecimal price) {
		super();
		this.id = id;
		this.orderCode = orderCode;
		this.useCode = useCode;
		this.orderTime = orderTime;
		this.price = price;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUseCode() {
		return useCode;
	}

	public void setUseCode(String useCode) {
		this.useCode = useCode;
	}

}
