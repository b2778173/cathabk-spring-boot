package com.example.demo.model.vo;

import com.example.demo.model.Bpi;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class CoinDeskRep {
    TimeInfo time;
    String disclaimer;
    String chartName;
    Map<String, Bpi> bpi;

    @Data
    public class TimeInfo {
        String updated;
        String updatedISO;
        String updateduk;
    }
}
