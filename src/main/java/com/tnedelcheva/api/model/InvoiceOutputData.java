package com.tnedelcheva.api.model;

import java.math.BigDecimal;

public class InvoiceOutputData {

    private String customer;
    private long vatNumber;
    private BigDecimal total;
    private String currency;

    public InvoiceOutputData(String customer, long vatNumber, BigDecimal total, String currency) {
        this.customer = customer;
        this.vatNumber = vatNumber;
        this.total = total;
        this.currency = currency;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public long getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(long vatNumber) {
        this.vatNumber = vatNumber;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
