package com.example.demo.service;

import com.example.demo.dao.BuyerDAO;
import com.example.demo.entity.Buyer;
import com.example.demo.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuyerServiceImpl implements BuyerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuyerServiceImpl.class);

    private final BuyerDAO buyerDao;

    @Autowired
    public BuyerServiceImpl(BuyerDAO buyerDao) {
        this.buyerDao = buyerDao;
    }

    @Override
    public void save(Buyer buyer) {
        buyerDao.save(buyer);
    }

    @Override
    public Buyer getBuyer(int buyerId) {
        return buyerDao.findById(buyerId)
                .orElseThrow(() -> throwNotFoundException(buyerId));
    }

    private NotFoundException throwNotFoundException(int buyerId) {
        LOGGER.warn(String.format("Buyer with id = %s was not found", buyerId));
        return new NotFoundException(String.format("Buyer with id = %s was not found", buyerId));
    }
}
