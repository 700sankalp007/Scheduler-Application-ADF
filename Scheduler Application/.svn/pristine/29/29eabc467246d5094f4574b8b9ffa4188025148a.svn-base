package sc.common.view.backing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import java.math.BigDecimal;

import java.net.URLEncoder;

import java.nio.charset.StandardCharsets;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import sc.common.view.bean.StandardReceiptsBean;
import sc.common.view.util.ConnectionManager;
import sc.common.view.util.Constants;
import sc.common.view.util.Constants.WSDL;
import sc.common.view.util.HttpServiceBean;
import sc.common.view.util.Util;

public class ArIntegration {
    
    Util util=null;
    Connection con = null;
    Map<String,Integer> map = new HashMap<>();
    Map<String,String> createdReceiptMap = null, customerAccountMap = null;
    List<Map<String,String>> customerAccountMapList = null;
    HashMap<String, String> setupMap = null;
    private JsonElement je = null;
    private JSONObject jsonObject = null;
    private Gson gson = null;
    private JsonParser jp = null;
    Map<String, Object> arReceiptMap = null,  arInvoiceMap = null;
    StandardReceiptsBean standardReceiptsBean = null;
    List<StandardReceiptsBean> standardReceiptsBeanList = null;
    Set<String> notCreatedReceiptSet = null,  deletedReceiptSet = null, 
                notDeletedReceiptSet = null, updatedInvoiceSet = null, 
                notFoundInvoiceSet = null, notUpdatedInvoiceSet = null, deletedInvoiceSet = null, 
                notDeletedInvoiceSet = null, notFoundReceiptSet = null,
                updatedCustomerSet = null, notUpdatedCusomerSet = null;
    private List<Map<String, Object>> arReceiptMapList = null, arInvoiceMapList = null;
    String request = null, prettyResponse = null, url = null;
    StringBuffer response = null;
    int responseCode = 0;                
    private static final String FILE_PATH = "C:\\Users\\Evosys\\Documents\\Zamil\\Migration\\Zamil_Customer_Account_profile_Update\\Zamil Cust account profile Batch 9.csv";

    public ArIntegration(HashMap<String, String> setupMap, Connection con) {
        
        super();
        this.util = new Util();
        this.setupMap = setupMap;        
        this.con = con;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.jp = new JsonParser();
    }

    
    public ArIntegration() {
        super();
    }

    public void executeDeleteReceipt(){
        
        int recordCount = 0;
        try{
            
            this.readArReceiptId();
            if(arReceiptMapList!=null
               && !arReceiptMapList.isEmpty()){
                   
                this.deleteReceipt();
                recordCount = arReceiptMapList.size();
            }                            
        }catch(Exception e){
            
            e.printStackTrace();      
        }finally{
            
//            System.out.println("File Data : " + arReceiptMapList);    
//            System.out.println("Record Count : " + recordCount);
//            System.out.println("Deleted Receipt : " + deletedReceiptSet);
//            System.out.println("Not Found Receipt : " + notFoundReceiptSet);
//            System.out.println("Not Deleted Receipt : " + notDeletedReceiptSet);
        }
    }

    public void executeArReceipt(){
        
        int recordCount = 0;
        try{
            
            this.readArReceipt();
            if(standardReceiptsBeanList!=null
               && !standardReceiptsBeanList.isEmpty()){
                   
                this.createReceipt();
                recordCount = standardReceiptsBeanList.size();
            }                            
        }catch(Exception e){
            
            e.printStackTrace();      
        }finally{
            
//            System.out.println("Record Count : " + recordCount);
//            System.out.println("Created Receipt : " + createdReceiptMap);
//            System.out.println("Not Created Receipt : " + notCreatedReceiptSet);
        }
    }

    public void executeArInvoice(){
        
        int recordCount = 0;
        try{
            
            this.readArInvoice();
            if(arInvoiceMapList!=null
               && !arInvoiceMapList.isEmpty()){
                   
//                this.getArInvoice();
                this.updateArInvoice();
                recordCount = arInvoiceMapList.size();
            }                            
        }catch(Exception e){
            
            e.printStackTrace();      
        }finally{
            
//            System.out.println("File Data : " + arInvoiceMapList);    
//            System.out.println("Record Count : " + recordCount);
//            System.out.println("Updated Invoice : " + updatedInvoiceSet);
//            System.out.println("Not Found Invoice : " + notFoundInvoiceSet);
//            System.out.println("Not Updated Invoice : " + notUpdatedInvoiceSet);
//            System.out.println("Deleted Invoice : " + deletedInvoiceSet);
//            System.out.println("Not Deleted Invoice : " + notDeletedInvoiceSet);
        }
    }

    
    public void readArReceiptId() throws Exception {
        
                
        try (      FileInputStream is = new FileInputStream(new File(FILE_PATH));
                   BufferedReader reader = new BufferedReader(new InputStreamReader(is,Constants.UTF8));){

                   String line;
                   int lineNum = 0;
                   while ((line = reader.readLine()) != null) {
                       
                       lineNum ++;
                       String csv[] = util.splitLine(line, ",");
                       if(lineNum==1){
                           
                           for(int i = 0; i<csv.length; i++)
                               map.put(csv[i],i);
                       }else{
                           
                           arReceiptMap = new HashMap<>();
                           arReceiptMap.put("STANDARD_RECEIPT_ID", csv[map.get("STANDARD_RECEIPT_ID")]);
                           arReceiptMap.put("RECEIPT_NUMBER", csv[map.get("RECEIPT_NUMBER")]);
//                           arReceiptMap.put("BUSINESS_UNIT", csv[map.get("BUSINESS_UNIT")]);
                           if(arReceiptMapList==null) arReceiptMapList = new  ArrayList<>();
                           arReceiptMapList.add(arReceiptMap);
                       }
                   }
        }
    }

    public void readArReceipt() throws Exception {
        
                
        try (      FileInputStream is = new FileInputStream(new File(FILE_PATH));
                   BufferedReader reader = new BufferedReader(new InputStreamReader(is,Constants.UTF8));){

                   String line;
                   int lineNum = 0;
                   while ((line = reader.readLine()) != null) {
                                              
                       lineNum ++;
                       String csv[] = util.splitLine(line, ",");
                       if(lineNum==1){
                           
                           for(int i = 0; i<csv.length; i++)
                               map.put(csv[i],i);
                       }else{
                           
                           standardReceiptsBean = new StandardReceiptsBean();
                           standardReceiptsBean.setReceiptNumber(csv[map.get("ReceiptNumber")]);
                           standardReceiptsBean.setBusinessUnit(csv[map.get("BusinessUnit")]);
                           standardReceiptsBean.setReceiptMethod(csv[map.get("ReceiptMethod")]);
                           standardReceiptsBean.setReceiptDate(csv[map.get("ReceiptDate")]);
                           standardReceiptsBean.setAmount(new BigDecimal(csv[map.get("Amount")]).doubleValue());
                           standardReceiptsBean.setCurrency(csv[map.get("Currency")]);
                           standardReceiptsBean.setConversionRate(new BigDecimal(csv[map.get("ConversionRate")]).doubleValue());
                           standardReceiptsBean.setConversionRateType(csv[map.get("ConversionRateType")]);
                           standardReceiptsBean.setConversionDate(csv[map.get("ConversionDate")]);
                           standardReceiptsBean.setCustomerName(csv[map.get("CustomerName")]);
                           standardReceiptsBean.setCustomerSite(csv[map.get("CustomerSite")]);
                           standardReceiptsBean.setCustomerAccountNumber(csv[map.get("CustomerAccountNumber")]);
                           standardReceiptsBean.setAccountingDate(csv[map.get("AccountingDate")]);
                           standardReceiptsBean.setRemittanceBankAccountNumber(csv[map.get("RemittanceBankAccountNumber")]);
                           if(standardReceiptsBeanList==null) standardReceiptsBeanList = new  ArrayList<>();
                           standardReceiptsBeanList.add(standardReceiptsBean);
                       }
                   }
        }
    }

    public void readArInvoice() throws Exception {
        
                
        try (      FileInputStream is = new FileInputStream(new File(FILE_PATH));
                   BufferedReader reader = new BufferedReader(new InputStreamReader(is,Constants.UTF8));){

                   String line;
                   int lineNum = 0;
                   while ((line = reader.readLine()) != null) {
                       
                       lineNum ++;
                       String csv[] = util.splitLine(line, ",");
                       if(lineNum==1){
                           
                           for(int i = 0; i<csv.length; i++)
                               map.put(csv[i],i);
                       }else{
                           
                           arInvoiceMap = new HashMap<>();
                           arInvoiceMap.put("TRX_NUMBER", csv[map.get("TRX_NUMBER")]);
                           arInvoiceMap.put("TRX_ID", csv[map.get("TRX_ID")]);
                           arInvoiceMap.put("EXCHANGE_RATE", csv[map.get("EXCHANGE_RATE")]);
                           if(arInvoiceMapList==null) arInvoiceMapList = new  ArrayList<>();
                           arInvoiceMapList.add(arInvoiceMap);
                       }
                   }
        }
    }


    public void deleteReceipt() throws Exception {
        
        for(Map<String, Object> arReceiptMap : arReceiptMapList){
            
            url = setupMap.get("HOST") + Constants.AR_RECEIPT + URLEncoder.encode(arReceiptMap.get("STANDARD_RECEIPT_ID").toString(), StandardCharsets.UTF_8.toString());
            //url = setupMap.get("HOST") + Constants.AR_RECEIPT + this.getStandardReceiptId(arReceiptMap.get("RECEIPT_NUMBER").toString(), arReceiptMap.get("BUSINESS_UNIT").toString());
            response = new StringBuffer("");
            responseCode = HttpServiceBean.restHttpPost(url, null, setupMap.get("USER_NAME")+":"+setupMap.get("PASSWORD"), Constants.HTTP_DELETE, java.net.HttpURLConnection.HTTP_NO_CONTENT, response);                
//            System.out.println("Delete Receipt : " + arReceiptMap.get("RECEIPT_NUMBER") + ", " + responseCode);            
            if(responseCode==java.net.HttpURLConnection.HTTP_NO_CONTENT){
                
                if(deletedReceiptSet==null) deletedReceiptSet = new HashSet<>();
                deletedReceiptSet.add(arReceiptMap.get("RECEIPT_NUMBER").toString());
            }else if(responseCode==java.net.HttpURLConnection.HTTP_NOT_FOUND){
            
                if(notFoundReceiptSet==null) notFoundReceiptSet = new HashSet<>();
                notFoundReceiptSet.add(arReceiptMap.get("RECEIPT_NUMBER").toString());
            }else{
                
                if(notDeletedReceiptSet==null) notDeletedReceiptSet = new HashSet<>();
                notDeletedReceiptSet.add(arReceiptMap.get("RECEIPT_NUMBER").toString());
            }
        }
    }

    public void createReceipt() throws Exception {
        
        for(StandardReceiptsBean standardReceiptsBean : standardReceiptsBeanList){
            
            url = setupMap.get("HOST") + Constants.AR_RECEIPT;
            request = gson.toJson(standardReceiptsBean);
            response = new StringBuffer("");
            responseCode = HttpServiceBean.restHttpPost(url, request, setupMap.get("USER_NAME")+":"+setupMap.get("PASSWORD"), Constants.HTTP_POST, java.net.HttpURLConnection.HTTP_CREATED, response);                
            if(responseCode==java.net.HttpURLConnection.HTTP_CREATED){
                
                if(createdReceiptMap==null) createdReceiptMap = new HashMap<>();
                je = jp.parse(response.toString());
                prettyResponse = gson.toJson(je);
                jsonObject = new JSONObject(prettyResponse);
                if(jsonObject!=null
                    && jsonObject.has("StandardReceiptId")){
                    
                    createdReceiptMap.put(standardReceiptsBean.getReceiptNumber(), String.valueOf(jsonObject.getLong("StandardReceiptId")));
//                    System.out.println(standardReceiptsBean.getReceiptNumber() + ", " +  jsonObject.getLong("StandardReceiptId"));
                }else{
                    
                    createdReceiptMap.put(standardReceiptsBean.getReceiptNumber(), null);
//                    System.out.println(standardReceiptsBean.getReceiptNumber() + ", " + null);
                }                            
            }else{
                
                if(notCreatedReceiptSet==null) notCreatedReceiptSet = new HashSet<>();
                notCreatedReceiptSet.add(standardReceiptsBean.getReceiptNumber());
//                System.out.println(standardReceiptsBean.getReceiptNumber() + " : " +  response);
            }
        }
    }


    public void getArInvoice() throws Exception {
        
        for(Map<String, Object> arInvoiceMap : arInvoiceMapList){
            
            url = setupMap.get("HOST") + Constants.AR_INVOICE + URLEncoder.encode(arInvoiceMap.get("TRX_ID").toString(), StandardCharsets.UTF_8.toString());
            response = new StringBuffer("");
            responseCode = HttpServiceBean.restHttpPost(url, null, setupMap.get("USER_NAME")+":"+setupMap.get("PASSWORD"), Constants.HTTP_GET, java.net.HttpURLConnection.HTTP_OK, response);
            if(responseCode==java.net.HttpURLConnection.HTTP_OK){
                
                if(updatedInvoiceSet==null) updatedInvoiceSet = new HashSet<>();
                updatedInvoiceSet.add(arInvoiceMap.get("TRX_NUMBER").toString());
            }else if(responseCode==java.net.HttpURLConnection.HTTP_NOT_FOUND){
            
                if(notFoundInvoiceSet==null) notFoundInvoiceSet = new HashSet<>();
                notFoundInvoiceSet.add(arInvoiceMap.get("TRX_NUMBER").toString());
            }else{
                
                if(notUpdatedInvoiceSet==null) notUpdatedInvoiceSet = new HashSet<>();
                notUpdatedInvoiceSet.add(arInvoiceMap.get("TRX_NUMBER").toString());
            }
            
//            System.out.println(arInvoiceMap.get("TRX_NUMBER") + ", " + responseCode);            
        }
    }

    public void updateArInvoice() throws Exception {
        
        for(Map<String, Object> arInvoiceMap : arInvoiceMapList){
            
            url = setupMap.get("HOST") + Constants.AR_INVOICE + URLEncoder.encode(arInvoiceMap.get("TRX_ID").toString(), StandardCharsets.UTF_8.toString());
            response = new StringBuffer("");
            request = "{ \"ConversionRate\": " +  arInvoiceMap.get("EXCHANGE_RATE") + "}";
//            request = "{ \"InvoiceStatus\": \"Incomplete\" }";
            responseCode = HttpServiceBean.restHttpPost(url, request, setupMap.get("USER_NAME")+":"+setupMap.get("PASSWORD"), Constants.HTTP_PATCH, java.net.HttpURLConnection.HTTP_OK, response);                
            if(responseCode==java.net.HttpURLConnection.HTTP_OK){
                
//                this.deleteArInvoice(arInvoiceMap.get("TRX_ID").toString(), arInvoiceMap.get("TRX_NUMBER").toString());
                if(updatedInvoiceSet==null) updatedInvoiceSet = new HashSet<>();
                updatedInvoiceSet.add(arInvoiceMap.get("TRX_NUMBER").toString());
//                System.out.println("Update Invoice : " + arInvoiceMap.get("TRX_NUMBER"));            
            }else if(responseCode==java.net.HttpURLConnection.HTTP_NOT_FOUND){
            
                if(notFoundInvoiceSet==null) notFoundInvoiceSet = new HashSet<>();
                notFoundInvoiceSet.add(arInvoiceMap.get("TRX_NUMBER").toString());
//                System.out.println("Not Found Invoice : " + arInvoiceMap.get("TRX_NUMBER"));            
            }else{
                
                if(notUpdatedInvoiceSet==null) notUpdatedInvoiceSet = new HashSet<>();
                notUpdatedInvoiceSet.add(arInvoiceMap.get("TRX_NUMBER").toString());
//                System.out.println("Not Update Invoice : " + arInvoiceMap.get("TRX_NUMBER") + " : " + responseCode + " : " + response);            
            }
        }
    }

    public void deleteArInvoice(String trxId, String trxNumber) throws Exception {
        
        url = setupMap.get("HOST") + Constants.AR_INVOICE + URLEncoder.encode(trxId, StandardCharsets.UTF_8.toString());
        //url = setupMap.get("HOST") + Constants.AR_RECEIPT + this.getStandardReceiptId(arReceiptMap.get("RECEIPT_NUMBER").toString(), arReceiptMap.get("BUSINESS_UNIT").toString());
        response = new StringBuffer("");
        responseCode = HttpServiceBean.restHttpPost(url, null, setupMap.get("USER_NAME")+":"+setupMap.get("PASSWORD"), Constants.HTTP_DELETE, java.net.HttpURLConnection.HTTP_NO_CONTENT, response);            
        if(responseCode==java.net.HttpURLConnection.HTTP_NO_CONTENT){
            
            if(deletedInvoiceSet==null) deletedInvoiceSet = new HashSet<>();
            deletedInvoiceSet.add(trxNumber);
//            System.out.println("Delete Invoice : " + trxNumber);            
        }else{
            
            if(notDeletedInvoiceSet==null) notDeletedInvoiceSet = new HashSet<>();
            notDeletedInvoiceSet.add(trxNumber);
//            System.out.println("Not Delete Invoice : " + trxNumber + " : " + responseCode + " : " + response);            

        }
    }

    public long getStandardReceiptId(String receiptNumber, String businessUnit) throws Exception {
        
        url = setupMap.get("HOST") + Constants.AR_RECEIPT + "?q=ReceiptNumber=" + receiptNumber+";BusinessUnit="+ URLEncoder.encode(businessUnit, StandardCharsets.UTF_8.toString());        
//        System.out.println("Get URL : " + url);
        response = new StringBuffer("");
        responseCode = HttpServiceBean.restHttpPost(url, null, setupMap.get("USER_NAME")+":"+setupMap.get("PASSWORD"), Constants.HTTP_GET, java.net.HttpURLConnection.HTTP_OK, response);        
//        System.out.println(responseCode + " : " + response);
        if(responseCode==java.net.HttpURLConnection.HTTP_OK){
            
            je = jp.parse(response.toString());
            prettyResponse = gson.toJson(je);
            jsonObject = new JSONObject(prettyResponse);
            if(jsonObject.has("count")
                && jsonObject.getInt("count")>0
                && jsonObject.has("items")
                && jsonObject.getJSONArray("items").length()>0
                && jsonObject.getJSONArray("items").getJSONObject(0).has("StandardReceiptId"))                
                return jsonObject.getJSONArray("items").getJSONObject(0).getLong("StandardReceiptId");
            else
                return 0l;
        }else
            return 0l;
    }

    public void executeCustomerAccount(){
        
        int recordCount = 0;
        try{
            
            this.readCustomerAccount();
            if(customerAccountMapList!=null
               && !customerAccountMapList.isEmpty()){
                   
                this.updateCustomerAccount();
                recordCount = customerAccountMapList.size();
            }                            
        }catch(Exception e){
            
            e.printStackTrace();      
        }finally{
            
//            System.out.println("File Data : " + customerAccountMapList);    
//            System.out.println("Record Count : " + recordCount);
//            System.out.println("Updated Customer : " + updatedCustomerSet);
//            System.out.println("Not Updated Customer : " + notUpdatedCusomerSet);
        }
    }

        
    public void readCustomerAccount() throws Exception {
        
                
        try (      FileInputStream is = new FileInputStream(new File(FILE_PATH));
                   BufferedReader reader = new BufferedReader(new InputStreamReader(is,Constants.UTF8));){

                   String line;
                   int lineNum = 0;
                   while ((line = reader.readLine()) != null) {
                       
                       lineNum ++;
                       String csv[] = util.splitLine(line, ",");
                       if(lineNum==1){
                           
                           for(int i = 0; i<csv.length; i++)
                               map.put(csv[i],i);
                       }else{
                           
                           customerAccountMap = new HashMap<>();
                           customerAccountMap.put("AccountNumber", csv[map.get("ACCOUNT_NUMBER")]);
                           customerAccountMap.put("CreditAnalystName", csv[map.get("CREDIT_ANALYST")]);
                           if(customerAccountMapList==null) customerAccountMapList = new  ArrayList<>();
                           customerAccountMapList.add(customerAccountMap);
                       }
                   }
        }
    }

    public void updateCustomerAccount() throws Exception {
        
        String request = null, response = null, error = null;
        String url = setupMap.get("HOST") + WSDL.CUSTOMER_PROFILE;
        ReceivablesCustomerProfileService receivablesCustomerProfileService = new ReceivablesCustomerProfileService();
        for(Map<String, String> customerAccountMap : customerAccountMapList){
                        
            request = util.prettyXmlFromSOAP(receivablesCustomerProfileService.updateCustomerProfile(customerAccountMap));
            response = util.prettyXmlFromString(HttpServiceBean.soapHttpPost(url, request, setupMap.get("USER_NAME")+":"+setupMap.get("PASSWORD")));                
            Boolean hasFault = util.hasFault(response);
            if(hasFault){
                
                error = util.getFaultString(response);
//                System.out.println("Not Update Customer : " + customerAccountMap.get("AccountNumber") + " : " + error);            
                if(notUpdatedCusomerSet==null) notUpdatedCusomerSet = new HashSet<>();
                notUpdatedCusomerSet.add(customerAccountMap.get("AccountNumber"));
            }else{
                
//                System.out.println("Update Customer : " + customerAccountMap.get("AccountNumber"));            
                if(updatedCustomerSet==null) updatedCustomerSet = new HashSet<>();
                updatedCustomerSet.add(customerAccountMap.get("AccountNumber"));
            }
        }
    }
        
        
    public static void main(String s[]) throws Exception {
        
        try(Connection con = ConnectionManager.getConnection();){
            
            HashMap<String, String> setupMap = Util.getSetupDetails(con, Constants.SETUP_TYPE_WEBSERVICE, Constants.SUB_TYPE_PROD);
            ArIntegration ai = new ArIntegration(setupMap, con);
            ai.executeCustomerAccount();           
        }
    }
}
