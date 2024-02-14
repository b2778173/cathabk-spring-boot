
package com.example.demo.service;

import com.example.demo.model.Bpi;



import java.util.List;

public interface BIPService {

    public List<Bpi> getAll(Bpi bpi);

    public Bpi getById(Integer id);

    public Bpi findByCode(String symbol);

    public int deleteById(Integer id);

    public int save(Bpi bpi);
}
