package com.ebao.SpringBootShardingJdbc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebao.SpringBootShardingJdbc.dao.OrderRepository;
import com.ebao.SpringBootShardingJdbc.domain.Order;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderRepository orderRepository;

	@RequestMapping("/add")
	public String add() {
		for (int i = 1; i <= 16; i++) {
			Order order = new Order();
			order.setOrderId(new Long(i));
			order.setUserId(new Long(i));
			order.setStatus("test");

			orderRepository.save(order);
		}

		return "success add";
	}

	@RequestMapping("query")
	private Object queryAll() {
		return orderRepository.findAll();
	}

	@RequestMapping("/add1")
	public String add1(@RequestParam Long orderId, @RequestParam Long userId) {
		Order order = new Order();
		order.setOrderId(new Long(orderId));
		order.setUserId(new Long(userId));
		order.setStatus("test");

		orderRepository.save(order);

		return "success add1";
	}

}
