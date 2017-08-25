package com.ebao.SpringBootShardingJdbc.dao;

import org.springframework.data.repository.CrudRepository;

import com.ebao.SpringBootShardingJdbc.domain.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {

}
