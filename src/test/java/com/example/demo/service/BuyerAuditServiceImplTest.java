package com.example.demo.service;

import com.example.demo.dao.BuyerDAO;
import com.example.demo.entity.*;
import com.example.demo.exception.InternalServerErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class BuyerAuditServiceImplTest {

    private BuyerAuditService buyerAuditService;

    @Mock
    private BuyerDAO buyerDAO;

    @BeforeEach
    public void setUp() {
        buyerAuditService = new BuyerAuditServiceImpl(buyerDAO);
    }

    @Test
    public void makeBuyerAuditShouldReturnCorrectAuditForActiveAndNotActiveUser() {
        // given
        List<Buyer> returnedBuyers = List.of(Buyer.builder()
                        .firstName("Ivan")
                        .lastName("Ivanov")
                        .purchases(List.of(Purchase.builder().month(Month.JUNE).price(100).build(),
                                Purchase.builder().month(Month.JULY).price(200).build(),
                                Purchase.builder().month(Month.AUGUST).price(300).build(),
                                Purchase.builder().month(Month.AUGUST).price(300).build()))
                        .build(),
                Buyer.builder()
                        .firstName("Petr")
                        .lastName("Petrov")
                        .purchases(List.of(Purchase.builder().month(Month.JUNE).price(100).build()))
                        .build());

        given(buyerDAO.findAll()).willReturn(returnedBuyers);

        // when
        var result = buyerAuditService.makeBuyerAudit();

        // then
        Map<Status, Map<Month, List<BuyerMonthAudit>>> expectedResult = new HashMap<>();
        Map<Month, List<BuyerMonthAudit>> activeMap = new HashMap<>();
        activeMap.put(Month.JUNE, List.of(BuyerMonthAudit.builder().month(Month.JUNE).status(Status.ACTIVE)
                .totalPrice(100).firstName("Ivan").lastName("Ivanov").build()));
        activeMap.put(Month.JULY, List.of(BuyerMonthAudit.builder().month(Month.JULY).status(Status.ACTIVE)
                .totalPrice(200).firstName("Ivan").lastName("Ivanov").build()));
        activeMap.put(Month.AUGUST, List.of(BuyerMonthAudit.builder().month(Month.AUGUST).status(Status.ACTIVE)
                .totalPrice(600).firstName("Ivan").lastName("Ivanov").build()));

        Map<Month, List<BuyerMonthAudit>> inactiveMap = new HashMap<>();
        inactiveMap.put(Month.JUNE, List.of(BuyerMonthAudit.builder().month(Month.JUNE).status(Status.INACTIVE)
                .totalPrice(100).firstName("Petr").lastName("Petrov").build()));

        expectedResult.put(Status.ACTIVE, activeMap);
        expectedResult.put(Status.INACTIVE, inactiveMap);

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    public void makeBuyerAuditShouldReturnEmptyResult() {
        // given
        List<Buyer> returnedBuyers = new ArrayList<>();

        given(buyerDAO.findAll()).willReturn(returnedBuyers);
        // when
        var result = buyerAuditService.makeBuyerAudit();

        // then
        Map<Status, Map<Month, List<BuyerMonthAudit>>> expectedResult = new HashMap<>();

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    public void makeBuyerAuditShouldReturnAuditForActiveUsers() {
        // given
        List<Buyer> returnedBuyers = List.of(Buyer.builder()
                        .firstName("Ivan")
                        .lastName("Ivanov")
                        .purchases(List.of(Purchase.builder().month(Month.JUNE).price(100).build(),
                                Purchase.builder().month(Month.JULY).price(200).build(),
                                Purchase.builder().month(Month.AUGUST).price(300).build(),
                                Purchase.builder().month(Month.AUGUST).price(300).build()))
                        .build(),
                Buyer.builder()
                        .firstName("Petr")
                        .lastName("Petrov")
                        .purchases(List.of(Purchase.builder().month(Month.AUGUST).price(100).build()))
                        .build());

        given(buyerDAO.findAll()).willReturn(returnedBuyers);

        // when
        var result = buyerAuditService.makeBuyerAudit();

        // then
        Map<Status, Map<Month, List<BuyerMonthAudit>>> expectedResult = new HashMap<>();
        Map<Month, List<BuyerMonthAudit>> activeMap = new HashMap<>();
        activeMap.put(Month.JUNE, List.of(BuyerMonthAudit.builder().month(Month.JUNE).status(Status.ACTIVE)
                .totalPrice(100).firstName("Ivan").lastName("Ivanov").build()));
        activeMap.put(Month.JULY, List.of(BuyerMonthAudit.builder().month(Month.JULY).status(Status.ACTIVE)
                .totalPrice(200).firstName("Ivan").lastName("Ivanov").build()));
        activeMap.put(Month.AUGUST, List.of(BuyerMonthAudit.builder().month(Month.AUGUST).status(Status.ACTIVE)
                        .totalPrice(600).firstName("Ivan").lastName("Ivanov").build(),
                BuyerMonthAudit.builder().month(Month.AUGUST).status(Status.ACTIVE)
                        .totalPrice(100).firstName("Petr").lastName("Petrov").build()));

        expectedResult.put(Status.ACTIVE, activeMap);


        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    public void makeBuyerAuditShouldThrowInternalServerErrorExceptionWhenDaoThrowsDataAccessException() {
        // given
        given(buyerDAO.findAll()).willThrow(new DataAccessResourceFailureException("error"));

        // when and then
        assertThatThrownBy(() -> buyerAuditService.makeBuyerAudit())
                .isInstanceOf(InternalServerErrorException.class)
                .hasMessage("Couldn't connect to database");
    }
}