package com.example.demo.controller;

import com.example.demo.entity.Buyer;
import com.example.demo.service.BuyerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/buyer")
public class BuyerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BuyerController.class);

    private final BuyerService buyerService;

    @Autowired
    public BuyerController(BuyerService buyerService) {
        this.buyerService = buyerService;
    }

    @PostMapping
    public void addBuyer(@RequestBody Buyer buyer) {
        LOGGER.info("Create buyer request accepted with buyer = {}", buyer);
        buyerService.save(buyer);
    }

    @GetMapping("/{buyerId}")
    public Buyer getBuyer(@PathVariable int buyerId) {
        LOGGER.info("Get buyer request accepted with buyerId = {}", buyerId);
        return buyerService.getBuyer(buyerId);
    }
}
