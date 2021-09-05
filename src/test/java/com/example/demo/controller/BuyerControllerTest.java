package com.example.demo.controller;

import com.example.demo.entity.Buyer;
import com.example.demo.entity.Month;
import com.example.demo.entity.Purchase;
import com.example.demo.exception.InternalServerErrorException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.service.BuyerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BuyerController.class)
class BuyerControllerTest {

    @MockBean
    private BuyerService buyerService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getBuyerByIdShouldReturnBuyer() throws Exception {
        // given
        Buyer buyer = Buyer.builder()
                .id(1)
                .firstName("Ivan")
                .lastName("Ivanov")
                .purchases(List.of(Purchase.builder().month(Month.JUNE).price(100).build(),
                        Purchase.builder().month(Month.JULY).price(200).build(),
                        Purchase.builder().month(Month.AUGUST).price(300).build(),
                        Purchase.builder().month(Month.AUGUST).price(300).build()))
                .build();
        given(buyerService.getBuyer(anyInt())).willReturn(buyer);

        // when and then
        final String expectedResponse = "{\"id\":1,\"firstName\":\"Ivan\",\"lastName\":\"Ivanov\",\"phone\":null," +
                "\"birthdate\":null,\"purchases\":[{\"id\":null,\"price\":100,\"month\":\"JUNE\"},{\"id\":null," +
                "\"price\":200,\"month\":\"JULY\"},{\"id\":null,\"price\":300,\"month\":\"AUGUST\"},{\"id\":null," +
                "\"price\":300,\"month\":\"AUGUST\"}]}";

        mockMvc.perform(get("/buyer/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    public void addBuyerShouldCreateBuyer() throws Exception {
        // given
        doNothing().when(buyerService).save(any());

        // when and then
        mockMvc.perform(post("/buyer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"birthdate\": \"2021-08-24T09:57:30.328Z\",\n" +
                        "  \"firstName\": \"Ivan\",\n" +
                        "  \"lastName\": \"Ivanov\",\n" +
                        "  \"phone\": \"099123456\",\n" +
                        "  \"purchases\": [\n" +
                        "    {\n" +
                        "      \"month\": \"JUNE\",\n" +
                        "      \"price\": 100\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}"))
                .andExpect(status().isOk());

        verify(buyerService).save(any());
    }

    @Test
    public void getBuyerByIdShouldReturn404WhenServiceThrowNotFoundException() throws Exception {
        // given
        given(buyerService.getBuyer(anyInt())).willThrow(new NotFoundException("id not found"));

        // when and then
        mockMvc.perform(get("/buyer/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addBuyerShouldReturn500StatusWhenInternalServerExceptionWasThrown() throws Exception {
        // given
        doNothing().when(buyerService).save(any());
        doThrow(new InternalServerErrorException("internalError")).when(buyerService).save(any());

        // when and then
        mockMvc.perform(post("/buyer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"birthdate\": \"2021-08-24T09:57:30.328Z\",\n" +
                        "  \"firstName\": \"Ivan\",\n" +
                        "  \"lastName\": \"Ivanov\",\n" +
                        "  \"phone\": \"099123456\",\n" +
                        "  \"purchases\": [\n" +
                        "    {\n" +
                        "      \"month\": \"JUNE\",\n" +
                        "      \"price\": 100\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}"))
                .andExpect(status().isInternalServerError());
    }

    class Test1 extends Thread{
        Test1(){

        }

        Test1(Runnable r){
            super(r);
        }

        public void run(){
            System.out.println("Inside Thread");
        }
    }

    class RunnableDemo implements Runnable
    {

        @Override
        public void run() {
            System.out.println("Inside Runnable");
        }
    }

    @Test
    public void test(){
        new Test1().start();
        new Test1(new RunnableDemo()).start();
    }
}