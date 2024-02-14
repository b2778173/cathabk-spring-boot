/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.example.demo.controller;

import com.example.demo.model.Bpi;
import com.example.demo.model.vo.CoinDeskRep;
import com.example.demo.service.BIPService;
import com.example.demo.service.CoinDeskService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liuzh
 * @since 2015-12-19 11:10
 */
@RestController
@RequestMapping("/bpi")
public class BIPController {

    @Autowired
    private BIPService bpiService;

    @Autowired
    private CoinDeskService coinDeskService;

    public BIPController(BIPService bpiService, CoinDeskService coinDeskService) {
        this.bpiService = bpiService;
        this.coinDeskService = coinDeskService;
    }

    @GetMapping(value = "/getAll")
    public PageInfo<Bpi> getAll() {
        List<Bpi> bpiList = bpiService.getAll(Bpi.builder().build());
        return new PageInfo<>(bpiList);
    }


    @RequestMapping(value = "/get/{code}")
    public Bpi findById(@PathVariable String code) {
        return bpiService.findByCode(code);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ModelMap delete(@PathVariable Integer id) {
        ModelMap result = new ModelMap();
        String msg = "";
        int deleteId = bpiService.deleteById(id);
        if(deleteId==0){
            msg = "資料無異動";
        }else{
            msg = "删除成功!";
        }
        result.put("msg", msg);
        result.put("id", deleteId);
        return result;
    }

    @PostMapping(value = "/save")
    public ModelMap save(@RequestBody Bpi bpi) {
        ModelMap result = new ModelMap();
        Bpi bpiExisted = bpiService.findByCode(bpi.getCode());
        if(bpiExisted !=null){
            result.put("msg", "same symbol BPI found!, insert fail");
            return result;
        }
        String msg = bpi.getId() == null ? "insert success!" : "insert fail!";
        int id = bpiService.save(bpi);
        result.put("msg", msg);
        result.put("id", id);
        return result;
    }

    @GetMapping(value = "/getCoinDesk")
    public CoinDeskRep getCoinDesk() {
        return coinDeskService.doGetCoinDesk();
    }

    @GetMapping(value = "/getBipDesk")
    public CoinDeskRep getBipDesk() {
        return coinDeskService.updateCoinDesk();
    }
}
