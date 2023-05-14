package com.csym025_resit.Model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Invoice implements Serializable {
    public String id;
    public String customerName;
    public Cart[] cart;
    public int totalPrice;
    public int lastPrice;
    public LocalDateTime rentDate;
    public LocalDateTime returnDate;
}