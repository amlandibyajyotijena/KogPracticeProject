package com.kvp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kvp.entities.Payment;


@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long>{

}