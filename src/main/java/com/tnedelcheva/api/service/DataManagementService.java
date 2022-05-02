package com.tnedelcheva.api.service;

import com.tnedelcheva.api.model.APIRequestDataString;
import com.tnedelcheva.api.model.Invoice;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface DataManagementService {


    boolean hasCSVFormat(MultipartFile file);

    List<Invoice> csvToInvoiceLs(InputStream is);

    BigDecimal calculateSumAllDocumentsPerCustomer(List<Invoice> invoiceList, float VAT , Map<String, BigDecimal> currencyMap, String currency);

    Map<String, BigDecimal> convertWithStream(String mapAsString);

    String calcultateDefaultCurrency(Map<String, BigDecimal> currencyMap );

    boolean checkParentConsistency (  List<Invoice> invoiceList);

    boolean checkCurrencyConsistency (String  defaultCurrency ,Map<String, BigDecimal>  currencyMap,String outputCurrency,  List<Invoice> invoiceList);



}
