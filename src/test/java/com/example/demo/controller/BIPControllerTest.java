package com.example.demo.controller;

import com.example.demo.mapper.BpiMapper;
import com.example.demo.model.Bpi;
import com.example.demo.model.vo.CoinDeskRep;
import com.example.demo.service.BIPService;
import com.example.demo.service.CoinDeskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers = BIPController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMybatis
@AutoConfigureMockMvc
@MapperScan(basePackages = "com.example.demo.mapper")
class BIPControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BIPService bipService;

    @Autowired
    private BpiMapper bipMapper;


    @MockBean
    private CoinDeskService coinDeskService;

    List<Bpi> list;

    @BeforeEach
    public void init() {
        this.mvc = MockMvcBuilders.standaloneSetup(new BIPController(bipService, coinDeskService)).build();
        list = new ArrayList<>();
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
    }

    @Test
    public void givenListOfBpi_whenGetAllBpi_thenReturnBpiList() throws Exception {
        // given - precondition or setup
        given(bipService.getAll(Bpi.builder().build())).willReturn(list);

        // then - verify the output
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/bpi/getAll")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(jsonPath("$.total", is(list.size())))
//                .andExpect(jsonPath("$.list",is(content().json(objectMapper.writeValueAsString(list)))))
                .andReturn();
        System.out.println("find all :" + result.getResponse().getContentAsString());
    }

    @Test
    void givenId_whenGetBpiById_thenReturnObject() throws Exception {
        String code = "GBP";

        Bpi bpi = Bpi.builder()
                .name("英鎊")
                .code("GBP")
                .symbol("&pound;")
                .rate("35,840.185")
                .description("British Pound Sterling")
                .rateFloat(BigDecimal.valueOf(41967.25))
                .build();

        given(bipService.findByCode(code)).willReturn(bpi);


        // then - verify the output
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/bpi/get/{code}", code)).andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(bpi.getName())))
                .andExpect(jsonPath("$.code", is(bpi.getCode())))
                .andExpect(jsonPath("$.rate", is(bpi.getRate())))
                .andReturn();
        System.out.println("get GBP :" + result.getResponse().getContentAsString());
    }

    @Test
    void givenId_whenDeleteBpiById_thenReturnDeleteId() throws Exception {
        Integer id = 1;

        given(bipService.deleteById(id)).willReturn(id);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.delete("/bpi/delete/{id}", id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        System.out.println("insert id:" + result.getResponse().getContentAsString());
    }

    @Test
    void givenBpi_whenInsertBpi_thenReturnObjId() throws Exception {
        Bpi bpi = Bpi.builder()
                .name("美元")
                .symbol("&#36;")
                .code("USD")
                .rate("45,088.402")
                .description("United States Dollar")
                .rateFloat(BigDecimal.valueOf(45088.4019))
                .build();

        given(bipService.save(bpi)).willReturn(1);

        MvcResult result = mvc.perform(
                        MockMvcRequestBuilders.post("/bpi/save")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(bpi))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("insert id:" + result.getResponse().getContentAsString());
    }

    @Test
    void givenUrl_whenFetchCoinDesk_thenReturnData() throws Exception {

        String jsonStr = "{\"time\":{\"updated\":\"2024/02/14  10:08:46\",\"updatedISO\":\"2024-02-14T22:08:46.909\",\"updateduk\":\"2024/02/14  02:08:46\"},\"disclaimer\":\"This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org\",\"chartName\":\"Bitcoin\",\"bpi\":{\"GBP\":{\"id\":2,\"page\":1,\"rows\":10,\"name\":\"英鎊\",\"code\":\"GBP\",\"symbol\":\"&pound;\",\"rate\":\"35,840.185\",\"description\":\"British Pound Sterling\",\"rateFloat\":41967.25}}}";

        CoinDeskRep res = objectMapper.readValue(jsonStr, CoinDeskRep.class);

        given(coinDeskService.doGetCoinDesk()).willReturn(res);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/bpi/getCoinDesk").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        System.out.println("getCoinDesk :" + result.getResponse().getContentAsString());
    }

    @Test
    void givenUrl_whenFetchBpiDesk_thenReturnData() throws Exception {
        String jsonStr = "{\"time\":{\"updated\":\"2024/02/14  10:08:46\",\"updatedISO\":\"2024-02-14T22:08:46.909\",\"updateduk\":\"2024/02/14  02:08:46\"},\"disclaimer\":\"This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org\",\"chartName\":\"Bitcoin\",\"bpi\":{\"GBP\":{\"id\":2,\"page\":1,\"rows\":10,\"name\":\"英鎊\",\"code\":\"GBP\",\"symbol\":\"&pound;\",\"rate\":\"35,840.185\",\"description\":\"British Pound Sterling\",\"rateFloat\":41967.25}}}";

        CoinDeskRep res = objectMapper.readValue(jsonStr, CoinDeskRep.class);

        CoinDeskRep.TimeInfo timeinfo = res.getTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd  hh:mm:ss");
        String now = LocalDateTime.now().format(formatter);
        String isoTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        String ukTime = LocalDateTime.now(ZoneOffset.UTC).format(formatter);

        timeinfo.setUpdated(now);
        timeinfo.setUpdatedISO(isoTime);
        timeinfo.setUpdateduk(ukTime);
        res.setBpi(list.stream().collect(Collectors.toMap(Bpi::getName, Function.identity())));

        given(coinDeskService.updateCoinDesk()).willReturn(res);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/bpi/getBipDesk").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        System.out.println("getBipDesk :" + result.getResponse().getContentAsString());
    }
}