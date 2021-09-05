package com.example.demo.service;

import com.example.demo.dao.BuyerDAO;
import com.example.demo.entity.*;
import com.example.demo.exception.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BuyerAuditServiceImpl implements BuyerAuditService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuyerAuditServiceImpl.class);

    private final BuyerDAO buyerDAO;

    @Autowired
    public BuyerAuditServiceImpl(BuyerDAO buyerDAO) {
        this.buyerDAO = buyerDAO;
    }

    @Override
    public Map<Status, Map<Month, List<BuyerMonthAudit>>> makeBuyerAudit() {
        Iterable<Buyer> buyers;
        try {
            buyers = buyerDAO.findAll();
        } catch (DataAccessException e) {
            throw new InternalServerErrorException("Couldn't connect to database");
        }

        return StreamSupport.stream(buyers.spliterator(), false)
                .flatMap(buyer -> makeBuyerMonthAudits(buyer)
                        .stream())
                .collect(Collectors.groupingBy(BuyerMonthAudit::getStatus,
                        Collectors.groupingBy(BuyerMonthAudit::getMonth)));

    }

    private List<BuyerMonthAudit> makeBuyerMonthAudits(Buyer buyer) {
        Status buyerStatus = findBuyerStatus(buyer.getPurchases());
        return Arrays.stream(Month.values())
                .map(month -> makeBuyerMonthAudit(buyer, buyerStatus, month))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Status findBuyerStatus(List<Purchase> buyerPurchases) {
        return buyerHasPurchasesFor(buyerPurchases, Month.AUGUST) ? Status.ACTIVE : Status.INACTIVE;
    }
    
    private BuyerMonthAudit makeBuyerMonthAudit(Buyer buyer, Status status, Month month) {

        if (!buyerHasPurchasesFor(buyer.getPurchases(), month)) {
            LOGGER.warn("Buyer with id {} does not have purchases", buyer.getId());
            return null;
        }
        int totalPrice = buyer.getPurchases().stream()
                .filter(purchase -> purchase.getMonth().equals(month))
                .map(Purchase::getPrice)
                .mapToInt(value -> value)
                .sum();
        return BuyerMonthAudit.builder()
                .firstName(buyer.getFirstName())
                .lastName(buyer.getLastName())
                .status(status)
                .totalPrice(totalPrice)
                .phone(buyer.getPhone())
                .month(month)
                .build();
    }

    private boolean buyerHasPurchasesFor(List<Purchase> purchases, Month givenMonth) {
        return purchases.stream()
                .map(Purchase::getMonth)
                .anyMatch(month -> month.equals(givenMonth));
    }
}
