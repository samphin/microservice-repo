package org.common.eureka.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.common.eureka.dto.OrderDto;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "t_order")
@Data
public class Order implements Serializable {

    @Id
    private String id;

    @Column(name = "order_code")
    private String orderCode;

    @Column(name = "use_code")
    private String useCode;

    @Column(name = "order_time")
    private Date orderTime;

    private BigDecimal price;

    public Order(String id, String orderCode, String useCode, Date orderTime, BigDecimal price) {
        this.id = id;
        this.orderCode = orderCode;
        this.useCode = useCode;
        this.orderTime = orderTime;
        this.price = price;
    }

    @Transient
    private String[] auditors;

    public Order() {
        super();
    }

    public Order buildOrder(OrderDto orderDto) {
        BeanUtils.copyProperties(orderDto, this);
        return this;
    }
}
