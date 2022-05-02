package com.tnedelcheva.api.controller;

import com.tnedelcheva.api.model.APIRequestDataString;
import com.tnedelcheva.api.model.Invoice;
import com.tnedelcheva.api.model.InvoiceOutputData;
import com.tnedelcheva.api.service.DataManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static jdk.nashorn.internal.objects.NativeMath.round;


@RestController
public class APIController {
    String defaultCurrency;
    Map<String, BigDecimal> currencyMap;


    @Autowired
    DataManagementService dataManagementService;


   @PostMapping(value = "/upload")
   @ResponseBody
   public List<InvoiceOutputData> uploadFile(final  APIRequestDataString apiRequestData ) throws IOException {

       String message = "";
       List<Invoice> invoiceList = null;
       MultipartFile file = apiRequestData.getFile();
       ArrayList<InvoiceOutputData> invoiceOutputDataList = new ArrayList<>();

       dataManagementService.hasCSVFormat(file);

       invoiceList = dataManagementService.csvToInvoiceLs(file.getInputStream());

       //get currency list
       currencyMap = dataManagementService.convertWithStream(apiRequestData.getCurrencyList());

        //Get output currency
       String outputCurrency = apiRequestData.getCurrency();
       defaultCurrency = dataManagementService.calcultateDefaultCurrency(currencyMap);
       dataManagementService.checkCurrencyConsistency(defaultCurrency ,currencyMap,outputCurrency, invoiceList  );
       dataManagementService.checkParentConsistency ( invoiceList);

       BigDecimal sum;
        if (apiRequestData.getVatNumber() != null) {
               sum = dataManagementService.calculateSumAllDocumentsPerCustomer(invoiceList, Long.parseLong(apiRequestData.getVatNumber()), currencyMap, apiRequestData.getCurrency());

               Invoice clientInvoice = invoiceList.stream().filter(invoice -> Long.parseLong(apiRequestData.getVatNumber())==(invoice.getVatNumber()))
                       .findAny()
                       .orElse(null);

               InvoiceOutputData invoiceOutputData = new InvoiceOutputData(clientInvoice.getCustomer(), Long.parseLong(apiRequestData.getVatNumber()), sum.setScale(3, BigDecimal.ROUND_HALF_EVEN), outputCurrency);
               invoiceOutputDataList.add(invoiceOutputData);
        } else {
               Set<Long> vatNumberSet = invoiceList.stream().map(invoice -> invoice.getVatNumber()).collect(Collectors.toSet());
               Iterator iterator = vatNumberSet.iterator();
               while (iterator.hasNext()) {
                   Long vatNumber = (Long) iterator.next();
                   sum = dataManagementService.calculateSumAllDocumentsPerCustomer(invoiceList, vatNumber, currencyMap, apiRequestData.getCurrency());

                   Invoice clientInvoice = invoiceList.stream().filter(invoice -> vatNumber==(invoice.getVatNumber()))
                           .findAny()
                           .orElse(null);

                   InvoiceOutputData invoiceOutputData = new InvoiceOutputData(clientInvoice.getCustomer(), vatNumber,sum.setScale(3, BigDecimal.ROUND_HALF_EVEN), outputCurrency);
                   invoiceOutputDataList.add(invoiceOutputData);
               }
        }

     return invoiceOutputDataList;

   }



}

