package com.payconiq.controller;

import com.payconiq.domain.Stock;
import com.payconiq.storage.StockStorageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
    public class StockController {

    @Autowired
    private  StockStorageRepo stockStorageRepo;

    @GetMapping("/api/stocks")
    public List<Stock> listStocks(Model model){
        return stockStorageRepo.getStocks();
    }


    @GetMapping("/api/stock/names")
    public Set<String> getStockNames(Model model){
        Set<String> res =new HashSet<String>();
        for(Stock stk:stockStorageRepo.getStocks()){
            res.add(stk.getName());
        }
        return res;
    }

    @GetMapping("/api/stock/{name}")
    public List<Stock> getStockNames(@PathVariable("name") String name, Model model){
        return stockStorageRepo.findStockByName(name);
    }


    @GetMapping("/api/stocks/{id}")
    public Object stocks(@PathVariable("id") int id, Model model){
        Stock stock=stockStorageRepo.findById(id);
        if(stock!=null){
            return  stock;
        }else{
            Map<String,String> res=new HashMap<String,String>();
            res.put("error","No Match Found");
            return res;
        }
    }

    @PostMapping("/api/stocks")
    public Map<String, Object> addStock(@RequestBody Stock stock){
        Stock stock1=stockStorageRepo.findStockByTimeAndName(stock.getName(),stock.getLastUpdate());
        Map<String,Object> result=new HashMap<String, Object>();
        if(stock1!=null){
            stock.setId(stock1.getId());
            stock.setIndex(stock1.getIndex());
            stockStorageRepo.updateStock(stock);
            result.put("stock",stock);
            result.put("result","updated");
        }else{
            int stockId=stockStorageRepo.addStock(stock);
            result.put("id",stockId);
            result.put("result","created");
        }
        return result;
    }

    @PutMapping("/api/stocks/{id}")
    public Map<String,Object> updatePrice(@PathVariable("id") int id,@RequestBody Stock stock){
        Map<String,Object> result=new HashMap<String, Object>();
        Stock stock1=stockStorageRepo.findById(id);
        if(stock1!=null){
            stock1.setCurrentPrice(stock.getCurrentPrice());
            stockStorageRepo.updateStock(stock1);
            result.put("result","priceUpdated");
            result.put("stock",stock1);
        }else{
            result.put("error","No Match Found");
        }
        return  result;
    }

    @DeleteMapping("/api/stocks/{id}")
    public Map<String,Object> removeStock(@PathVariable("id") int id){
        Map<String,Object> result=new HashMap<String, Object>();
        Stock stock1=stockStorageRepo.findById(id);
        if(stock1!=null){
            stockStorageRepo.deleteStock(stock1);
            result.put("result","deleted");
        }else{
            result.put("error","No Match Found");
        }

        return  result;
    }
}
