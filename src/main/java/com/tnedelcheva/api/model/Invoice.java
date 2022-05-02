package com.tnedelcheva.api.model;


import java.math.BigDecimal;

public class Invoice {

    private String customer;
    private long vatNumber;
    private long documentNumber;
    private int type;
    private long parentDocument;
    private String currency;
    private BigDecimal total; //? type?


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

    public long getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(long documentNumber) {
        this.documentNumber = documentNumber;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getParentDocument() {
        return parentDocument;
    }

    public void setParentDocument(long parentDocument) {
        this.parentDocument = parentDocument;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Invoice(String customer, long vatNumber, long documentNumber, int type, long parentDocument, String currency, BigDecimal total) {
        this.customer = customer;
        this.vatNumber = vatNumber;
        this.documentNumber = documentNumber;
        this.type = type;
        this.parentDocument = parentDocument;
        this.currency = currency;
        this.total = total;


    }

}
