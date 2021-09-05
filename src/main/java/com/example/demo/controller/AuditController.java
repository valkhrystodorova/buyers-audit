package com.example.demo.controller;

import com.example.demo.entity.BuyerMonthAudit;
import com.example.demo.entity.Month;
import com.example.demo.entity.Status;
import com.example.demo.service.BuyerAuditService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/audit")
public class AuditController {

    public static final Logger LOGGER = LoggerFactory.getLogger(AuditController.class);

    private final BuyerAuditService buyerAuditService;

    @Autowired
    public AuditController(BuyerAuditService buyerAuditService) {
        this.buyerAuditService = buyerAuditService;
    }

    @ApiOperation(value = "Return Buyer Month Audit")
    @GetMapping
    public Map<Status, Map<Month, List<BuyerMonthAudit>>> getBuyerAudit() {
        LOGGER.info("Get buyer Audit request accepted");
        return buyerAuditService.makeBuyerAudit();
    }
}
