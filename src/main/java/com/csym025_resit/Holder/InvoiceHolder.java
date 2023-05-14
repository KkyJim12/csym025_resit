package com.csym025_resit.Holder;

public class InvoiceHolder {
    private String id;
    private final static InvoiceHolder i = new InvoiceHolder();

    private InvoiceHolder() {
    }

    public static InvoiceHolder getInstance() {
        return i;
    }

    public void setInvoice(String s) {
        this.id = s;
    }

    public String getInvoice() {
        return this.id;
    }
}
