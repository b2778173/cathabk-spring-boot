
package com.example.demo.service;

import com.example.demo.model.Bpi;



import java.util.List;

public interface BIPService {

    public List<Bpi> getAll(Bpi bpi);

    public Bpi getById(Integer id);

    public Bpi findByCode(String symbol);

    public void deleteById(Integer id);

    public void save(Bpi bpi);
}
