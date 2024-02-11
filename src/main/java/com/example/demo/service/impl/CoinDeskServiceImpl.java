package com.example.demo.service.impl;

import com.example.demo.model.Bpi;
import com.example.demo.model.vo.CoinDeskRep;
import com.example.demo.service.BIPService;
import com.example.demo.service.CoinDeskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CoinDeskServiceImpl implements CoinDeskService {
    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private BIPService bpiService;

    @Override
    public CoinDeskRep doGetCoinDesk() {
        String url = "https://api.coindesk.com/v1/bpi/currentprice.json";
        String respJosn = restTemplate.getForObject(url, String.class);
        return JSON.parseObject(respJosn, CoinDeskRep.class);
    }

    @Override
    public CoinDeskRep updateCoinDesk() {
        CoinDeskRep res = doGetCoinDesk();
        CoinDeskRep.TimeInfo timeinfo = res.getTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd  hh:mm:ss");
        String now = LocalDateTime.now().format(formatter);
        String isoTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        String ukTime = LocalDateTime.now(ZoneOffset.UTC).format(formatter);

        timeinfo.setUpdated(now);
        timeinfo.setUpdatedISO(isoTime);
        timeinfo.setUpdateduk(ukTime);

        List<Bpi> bpiList = bpiService.getAll(Bpi.builder().build());
        Map<String, Bpi> bipMap = new HashMap<>();
        for (Bpi b : bpiList) {
            bipMap.put(b.getCode(), b);
        }
        res.setBpi(bipMap);
        return res;
    }
}
