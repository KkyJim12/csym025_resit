package com.csym025_resit.Holder;

public class CustomerHolder {

    private String id;
    private final static CustomerHolder i = new CustomerHolder();

    private CustomerHolder() {
    }

    public static CustomerHolder getInstance() {
        return i;
    }

    public void setCustomer(String s) {
        this.id = s;
    }

    public String getCustomer() {
        return this.id;
    }
}
