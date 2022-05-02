package com.tnedelcheva.api.model;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class APIRequestDataString {
    private MultipartFile file;
    private String currencyList;
    private String currency;
    private String vatNumber;

    public APIRequestDataString(MultipartFile file, String currencyList, String currency) {
        this.file = file;
        this.currencyList = currencyList;
        this.currency = currency;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getCurrencyList() {
        return currencyList;
    }

    public void setCurrencyList(String currencyList) {
        this.currencyList = currencyList;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }
}
