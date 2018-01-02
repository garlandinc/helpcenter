package com.garland.helpcenter.utility;

/**
 * Created by lemon on 10/30/2017.
 */

public class Stock {
    private String[] stock;
    private int size;
    private String value;

    public Stock(String[] stock, String value) {
        this(stock,stock.length, value);
    }

    public Stock(String[] stock, int size, String value) {
        this.stock = stock;
        this.size = size;
        this.value = value;
    }

    public String[] getStock() {
        return stock;
    }

    public int getSize() {
        return size;
    }

    public String getValue() {
        return value;
    }
}
