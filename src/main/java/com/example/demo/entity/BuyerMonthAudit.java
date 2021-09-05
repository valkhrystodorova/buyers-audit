package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuyerMonthAudit {
    private String firstName;
    private String lastName;
    @JsonIgnore
    private Status status;
    private int totalPrice;
    private String phone;
    @JsonIgnore
    private Month month;
}
