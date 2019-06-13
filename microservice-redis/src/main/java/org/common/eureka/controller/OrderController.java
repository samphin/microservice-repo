package org.common.eureka.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.common.eureka.dto.OrderDto;
import org.common.eureka.entity.Order;
import org.common.eureka.service.IOrderService;
import org.common.eureka.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api("OrderController相关的api")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @ApiOperation(value = "保存订单信息", notes = "保存订单信息")
    @ApiImplicitParam(name = "orderDto", value = "订单对象", required = true, dataType = "OrderDto")
    @PostMapping
    public Result save(@RequestBody OrderDto orderDto) {
        boolean flag = this.orderService.save(orderDto);
        if (flag) {
            return Result.ok();
        } else {
            return Result.failure();
        }
    }

    @ApiOperation(value = "修改订单信息", notes = "修改订单信息")
    @ApiImplicitParam(name = "orderDto", value = "订单对象", required = true, dataType = "OrderDto")
    @PatchMapping
    public Result update(@RequestBody OrderDto orderDto) {
        boolean flag = this.orderService.update(orderDto);
        if (flag) {
            return Result.ok();
        } else {
            return Result.failure();
        }
    }

    @ApiOperation(value = "查询订单信息", notes = "查询数据库中某个的订单信息")
    @ApiImplicitParam(name = "orderDto", value = "订单查询对象", required = true, dataType = "OrderDto")
    @GetMapping
    public Result queryList(@ModelAttribute OrderDto orderDto) {
        List<Order> users = this.orderService.queryList(orderDto);
        return Result.ok().setData(users);
    }

    @ApiOperation(value = "根据id查询订单信息", notes = "查询数据库中某个的订单信息")
    @ApiImplicitParam(name = "id", value = "订单ID", paramType = "path", required = true, dataType = "String")
    @GetMapping("/{id}")
    public Result queryOne(@PathVariable String id) {
        Order order = this.orderService.queryOne(id);
        return Result.ok().setData(order);
    }
}
