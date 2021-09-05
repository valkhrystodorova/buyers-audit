package com.example.demo.controller;

import com.example.demo.entity.BuyerMonthAudit;
import com.example.demo.entity.Month;
import com.example.demo.entity.Status;
import com.example.demo.service.BuyerAuditService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuditController.class)
class AuditControllerTest {

    @MockBean
    private BuyerAuditService buyerAuditService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getBuyerAuditShouldReturnAudit() throws Exception {
        // given
        Map<Status, Map<Month, List<BuyerMonthAudit>>> auditResult = new HashMap<>();
        Map<Month, List<BuyerMonthAudit>> activeMap = new HashMap<>();
        activeMap.put(Month.JUNE, List.of(BuyerMonthAudit.builder().month(Month.JUNE).status(Status.ACTIVE)
                .totalPrice(100).firstName("Ivan").lastName("Ivanov").build()));
        auditResult.put(Status.ACTIVE, activeMap);

        given(buyerAuditService.makeBuyerAudit()).willReturn(auditResult);

        // when and then
        final String expectedResponse ="{\"ACTIVE\":{\"JUNE\":[{\"firstName\":\"Ivan\",\"lastName\":\"Ivanov\"," +
                "\"totalPrice\":100,\"phone\":null}]}}";

        mockMvc.perform(get("/audit"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }
}