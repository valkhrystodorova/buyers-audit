package com.example.demo.service;

import com.example.demo.entity.Buyer;

public interface BuyerService {
    void save (Buyer buyer);
    Buyer getBuyer (int buyerId);
}
