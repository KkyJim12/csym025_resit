package com.csym025_resit.Holder;

public class StockHolder {

    private String id;
    private final static StockHolder i = new StockHolder();

    private StockHolder() {
    }

    public static StockHolder getInstance() {
        return i;
    }

    public void setStock(String s) {
        this.id = s;
    }

    public String getStock() {
        return this.id;
    }
}
