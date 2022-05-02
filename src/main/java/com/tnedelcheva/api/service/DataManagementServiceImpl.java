package com.tnedelcheva.api.service;

import com.tnedelcheva.api.exceptions.FileLoadingException;
import com.tnedelcheva.api.exceptions.NoSuchElementExistsException;
import com.tnedelcheva.api.model.Invoice;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DataManagementServiceImpl implements DataManagementService{
    public static String TYPE = "text/csv";

    @Override
    public boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            throw new FileLoadingException("Problem Loading File  " );
         }
        return true;
    }
    @Override
    public  List<Invoice> csvToInvoiceLs(InputStream is) {

        List<Invoice> invoiceList = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                Invoice invoice = new Invoice(
                        csvRecord.get("Customer"),
                        Long.parseLong(csvRecord.get("Vat number")),
                        Long.parseLong(csvRecord.get("Document number")),
                        Integer.parseInt(csvRecord.get("Type")),
                       "".equals( csvRecord.get("Parent document"))?0:Long.parseLong(csvRecord.get("Parent document")),
                        csvRecord.get("Currency"),
                        new BigDecimal(csvRecord.get("Total"))
                );

                invoiceList.add(invoice);
            }
            return invoiceList;
        } catch (Exception e) {
            throw new FileLoadingException("Problem Loading File  " );
        }

    }
    @Override
    public boolean checkParentConsistency (  List<Invoice> invoiceList){

        Set<Long> parentNumberSet = invoiceList.stream().map(invoice -> invoice.getParentDocument()).collect(Collectors.toSet());
        Set<Long> allDocumentNumberSet = invoiceList.stream().map(invoice -> invoice.getDocumentNumber()).collect(Collectors.toSet());
        parentNumberSet.remove((long)0);
        if( !  allDocumentNumberSet.containsAll(parentNumberSet)) {
            throw new NoSuchElementExistsException("Inconcictency in paretnt elements");
        }
        return true ;
    }

    @Override
    public boolean checkCurrencyConsistency (String  defaultCurrency ,Map<String, BigDecimal>  currencyMap, String outputCurrency,  List<Invoice> invoiceList){

        Set<String> invoiceCurrencySet = invoiceList.stream().map(invoice -> invoice.getCurrency()).collect(Collectors.toSet());
        Set<String> tradeCurrencySet = currencyMap.keySet().stream().collect(Collectors.toSet());
        if( ! (tradeCurrencySet.containsAll(invoiceCurrencySet) && invoiceCurrencySet.contains(outputCurrency))) {
            throw new NoSuchElementExistsException("Inconcictency with currency = " + outputCurrency);
        }
        return true;

    }


    //Add data management methods
    @Override
    public BigDecimal calculateSumAllDocumentsPerCustomer(List<Invoice> invoiceList, float vat , Map<String, BigDecimal> currencyMap, String currency)
    {
        BigDecimal sum = BigDecimal.valueOf(0);
        BigDecimal total ;
        try {
            for (int counter = 0; counter < invoiceList.size(); counter++) {
                Invoice invoice = invoiceList.get(counter);
                if (invoice.getVatNumber() == vat) {
                    total = convertToCurrency(invoice, currencyMap, currency);
                    sum = sum.add( invoice.getType() == 2 ?  total.negate() : total);
                }
            }
        }catch( Exception e)
        {
            throw new NoSuchElementExistsException("Problem with calculations! ");
        }
        return  sum ;
    }
    private BigDecimal  convertToCurrency(Invoice invoice, Map<String, BigDecimal> currencyMap, String outputCurrency ){
        BigDecimal total;
        BigDecimal invoiceTotalDefCurr ;
        BigDecimal k;
        String invoiceCurrency = invoice.getCurrency();
        BigDecimal invoiceTotal = invoice.getTotal();
        k =  currencyMap.get(invoiceCurrency);
        invoiceTotalDefCurr = invoiceTotal.multiply(k);
        k =  currencyMap.get(outputCurrency);
        total = invoiceTotalDefCurr.divide(k, 5, RoundingMode.HALF_UP);
        return total;
    }
    @Override
    public Map<String, BigDecimal> convertWithStream(String mapAsString) {
        Map<String, BigDecimal> map = Arrays.stream(mapAsString.split(","))
                .map(entry -> entry.split(":"))
                .collect(Collectors.toMap(entry -> entry[0], entry -> new BigDecimal(entry[1])));
        return map;
    }
    @Override
    public  String calcultateDefaultCurrency(Map<String, BigDecimal> currencyMap ) {
    Stream<String> defaultCurrencyStream = keys(currencyMap, new BigDecimal (1));
    try{

            return defaultCurrencyStream.findFirst().get();
        }
    catch( Exception e) {
        throw new NoSuchElementExistsException("Problem with calculations of default currency! ");
    }
    }

    public <K, V> Stream<K> keys(Map<K, V> map, V value) {
        return map
                .entrySet()
                .stream()
                .filter(entry -> value.equals(entry.getValue()))
                .map(Map.Entry::getKey);
    }

}
