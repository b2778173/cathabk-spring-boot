
package com.example.demo.service;

import com.example.demo.model.Bpi;
import com.example.demo.model.vo.CoinDeskRep;

import java.util.List;

public interface CoinDeskService {
    public CoinDeskRep doGetCoinDesk();

    public CoinDeskRep updateCoinDesk();
}
