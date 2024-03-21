package com.kvp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kvp.entities.OrderItem;


@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {

}