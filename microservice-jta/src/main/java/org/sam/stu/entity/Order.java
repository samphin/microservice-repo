package org.sam.stu.entity;

import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "t_order")
public class Order {

	// 如果是JPA 用import
	// javax.persistence.Id;MongoDB则用@org.springframework.data.annotation.Id
	@Id
	private String id;

	@Column(name = "order_code")
	private String orderCode;

	@Column(name = "use_code")
	private String useCode;

	@Column(name = "order_time")
	private Date orderTime;

	private BigDecimal price;

	@Transient
	private String[] auditors;

	public Order() {
		super();
	}

	public Order(String id, String orderCode, String useCode, Date orderTime, BigDecimal price) {
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

	public String[] getAuditors() {
		return auditors;
	}

	public void setAuditors(String[] auditors) {
		this.auditors = auditors;
	}
}
