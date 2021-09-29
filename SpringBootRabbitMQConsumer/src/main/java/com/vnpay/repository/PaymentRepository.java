package com.vnpay.repository;

import org.springframework.data.repository.CrudRepository;

import com.vnpay.model.Payment;

public interface PaymentRepository extends CrudRepository<Payment, Long>{

}
