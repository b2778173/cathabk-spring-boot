package com.example.demo.controller;

import com.example.demo.mapper.BpiMapper;
import com.example.demo.model.Bpi;
import com.example.demo.service.BIPService;
import com.example.demo.service.CoinDeskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tk.mybatis.spring.annotation.MapperScan;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BIPController.class)
@AutoConfigureMybatis
@MapperScan(basePackages = "com.example.demo.mapper")
class BIPControllerTest {
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BIPService bipService;


    @MockBean
    private CoinDeskService coinDeskService;


    @Before
    public void init() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void givenListOfBpi_whenGetAllBpi_thenReturnBpiList() throws Exception {
        // given - precondition or setup
        List<Bpi> list = new ArrayList<>();
        list.add(
                Bpi.builder()
                        .name("美元")
                        .code("USD")
                        .symbol("&#36;")
                        .rate("45,088.402")
                        .description("United States Dollar")
                        .rateFloat(BigDecimal.valueOf(45088.40))
                        .build()
        );
        list.add(
                Bpi.builder()
                        .name("英鎊")
                        .code("GBP")
                        .symbol("&pound;")
                        .rate("35,840.185")
                        .description("British Pound Sterling")
                        .rateFloat(BigDecimal.valueOf(41967.25))
                        .build()
        );
//        given(bipService.getAll(Bpi.builder().build())).willReturn(list);

        // then - verify the output
        mvc.perform(get("/bpi/getAll")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(list)))
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(list.size())));
    }

    @Test
    void givenId_whenGetBpiById_thenReturnObject() throws Exception {
        Integer id = 2;

        Bpi bpi = Bpi.builder()
                .name("英鎊")
                .code("GBP")
                .symbol("&pound;")
                .rate("35,840.185")
                .description("British Pound Sterling")
                .rateFloat(BigDecimal.valueOf(41967.25))
                .build();
//        given(bipService.getById(id)).willReturn(bpi);


        // then - verify the output
        mvc.perform(get("/bpi/view/{id}", id)).andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(bpi.getName())))
                .andExpect(jsonPath("$.code", is(bpi.getCode())))
                .andExpect(jsonPath("$.rate", is(bpi.getRate())));
    }

    @Test
    void delete() throws Exception {
        Integer id = 1;
        mvc.perform(get("/bpi/delete/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
    }

    @Test
    void save() throws Exception {
        mvc.perform(
                        post("/bpi/save")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("name", "美元")
                                .param("code", "USD")
                                .param("symbol", "&#36;")
                                .param("rate", "45,088.402")
                                .param("description", "United States Dollar")
                                .param("rateFloat", "45088.4019")

                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getCoinDesk() throws Exception {
        mvc.perform(get("/bpi/getCoinDesk}"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getBipDesk() throws Exception {
        mvc.perform(get("/bpi/getCoinDesk"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}