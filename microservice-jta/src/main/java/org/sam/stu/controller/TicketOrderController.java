package org.sam.stu.controller;

import org.sam.stu.service.ITicketOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@Api("TicketOrderController相关的api")
public class TicketOrderController {

	@Autowired
	private ITicketOrderService ticketOrderService;

	@ApiOperation(value = "保存用户机票订单信息", notes = "存用户机票订单信息")
	@ApiImplicitParam(name = "idCard", value = "用户ID", paramType = "path", required = true, dataType = "Integer")
	@GetMapping("/save/{idCard}")
	public void save(@PathVariable String idCard) {
		this.ticketOrderService.save(null);
	}

}
