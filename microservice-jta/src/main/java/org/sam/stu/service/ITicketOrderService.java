package org.sam.stu.service;

import org.sam.stu.entity.TicketOrder;

public interface ITicketOrderService {

	/**
	 * 保存订单信息
	 * 
	 * @param order
	 * @author samphin
	 * @date 2019年4月9日 下午5:17:33 @GitConfig：
	 */
	public void save(TicketOrder order);

	/**
	 * 删除
	 * 
	 * @param orderId
	 * @author samphin
	 * @date 2019年4月9日 下午5:18:30 @GitConfig：
	 */
	public void delete(String orderId);

	/**
	 * 查询
	 * 
	 * @param orderId
	 * @author samphin
	 * @date 2019年4月9日 下午5:18:36 @GitConfig：
	 */
	public TicketOrder query(String orderId);
}
