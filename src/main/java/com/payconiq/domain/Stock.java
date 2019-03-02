package com.payconiq.domain;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.payconiq.util.CustomStockDeserializer;
import com.payconiq.util.CustomStockSerializer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Currency;


@ToString
@EqualsAndHashCode
@JsonDeserialize(using = CustomStockDeserializer.class)
@JsonSerialize(using = CustomStockSerializer.class)
public class Stock {

    private int id;

    private String name;

    private Amount currentPrice;

    private Long lastUpdate;

    private int index;

    public Stock(Amount currentPrice,Long time){
        this.currentPrice=currentPrice;
        this.lastUpdate=time;
    }

    public Stock(String name,Amount currentPrice,Long time){
        this.name=name;
        this.currentPrice=currentPrice;
        this.lastUpdate=time;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Amount getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Amount currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
