package org.common.eureka.service;

import org.common.eureka.dto.OrderDto;
import org.common.eureka.entity.Order;

import java.util.List;

public interface IOrderService {

    /**
     * 保存订单信息
     *
     * @param orderDto
     * @author samphin
     * @date 2019年4月9日 下午5:17:33 @GitConfig：
     */
    public boolean save(OrderDto orderDto);

    /**
     * 修改
     * @param orderDto
     * @return
     */
    public boolean update(OrderDto orderDto);

    /**
     * 删除
     *
     * @param orderId
     * @author samphin
     * @date 2019年4月9日 下午5:18:30 @GitConfig：
     */
    public boolean delete(String orderId);

    /**
     * 查询
     *
     * @param orderId
     * @author samphin
     * @date 2019年4月9日 下午5:18:36 @GitConfig：
     */
    public Order queryOne(String orderId);

    /**
     * 查询列表
     *
     * @param orderDto
     * @return
     */
    public List<Order> queryList(OrderDto orderDto);
}
