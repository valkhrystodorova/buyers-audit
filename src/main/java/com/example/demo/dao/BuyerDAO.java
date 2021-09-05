package com.example.demo.dao;

import com.example.demo.entity.Buyer;
import org.springframework.data.repository.CrudRepository;

public interface BuyerDAO extends CrudRepository<Buyer, Integer> {
}
