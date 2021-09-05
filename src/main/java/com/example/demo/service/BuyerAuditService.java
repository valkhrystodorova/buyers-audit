package com.example.demo.service;

import com.example.demo.entity.BuyerMonthAudit;
import com.example.demo.entity.Month;
import com.example.demo.entity.Status;

import java.util.List;
import java.util.Map;

public interface BuyerAuditService {
    Map<Status, Map<Month, List<BuyerMonthAudit>>> makeBuyerAudit();
}
