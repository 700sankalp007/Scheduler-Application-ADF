package sc.common.view.backing;

import HTTPClient.HttpURLConnection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import java.math.BigDecimal;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import sc.common.view.util.ConnectionManager;
import sc.common.view.util.Constants;
import sc.common.view.util.DML;
import sc.common.view.util.ExceptionLog;
import sc.common.view.util.HttpServiceBean;
import sc.common.view.util.Util;

public class ItemCostAdjustment {


    Util util=null;
    Connection con = null;
    HashMap<String,Integer> map = new HashMap<>();
    HashMap<String, String> setupMap = null;
    private JsonElement je = null;
    private JSONObject jsonObject = null;
    private Gson gson = null;
    private JsonParser jp = null;
    StringBuffer response = null;
    //private static final String DM_ITEM_COST_ADJUSTMENTS_T = "DM_ITEM_COST_ADJUSTMENTS_T";
//    private static final String NEW_ITEM_COST_ADJUSTMENTS_T = "NEW_ITEM_COST_ADJUSTMENTS_T";
    private static final String ITEM_COST_ADJUSTMENTS_T_0804 = "ITEM_COST_ADJUSTMENTS_T_0804";
    private static final Set<String> keySet = new HashSet<String>(Arrays.asList("TRANSACTION_ID"));
    Map<String, String> exceptionMap = new HashMap<String, String>(){ {
            put("LAST_UPDATED_DATE", "SYS_EXTRACT_UTC(CURRENT_TIMESTAMP)");
        }
    };
    Map<String, Object> itemCostAdjustmentMap = null;
    private List<Map<String, Object>> itemCostAdjustmentMapList = null, requestMapList = null;    
    String url = null;
    private static final String FILE_PATH = "D:\\excel\\SCM\\Overheade Adjustment.csv";
    String prettyResponse = null;
    
    public ItemCostAdjustment(HashMap<String, String> setupMap, Connection con) {
        
        super();
        this.util = new Util();
        this.setupMap = setupMap;        
        this.con = con;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.jp = new JsonParser();
    }
    
    public int executeItemCostAdjustment() throws Exception {
        
        int recordCount = 0;
        try{
                        
            requestMapList = Util.queryForListMap(Util.getBlobData(con, "ITEM_COST_ADJUSTMENT", "SODAR_FBL_QUERY_TBL"), con);
            if(requestMapList!=null
               && !requestMapList.isEmpty()){
                
                itemCostAdjustmentMapList = new ArrayList<>();
                this.integrateItemCostAdjustment();
//                this.getItemAdjustment();
                if(itemCostAdjustmentMapList!=null
                    && !itemCostAdjustmentMapList.isEmpty())
                    this.updateItemCostAdjustmentTable();
                    
                
                recordCount = requestMapList.size();       
            }
        }catch(Exception e){
            
            e.printStackTrace();
            ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), e, null);
            throw e;
        }
        return recordCount;        
    }
    
    private void integrateItemCostAdjustment() throws Exception {
        
        int responseCode = 0, count = 0;
        String request = null;
        url = setupMap.get("HOST") + Constants.ITEM_COST_ADJUSTMENT_URL;
        for(Map<String, Object> requestMap : requestMapList){
            
            itemCostAdjustmentMap = new HashMap<>();
            request = requestMap.get("REQUEST_PAYLOAD").toString();
            itemCostAdjustmentMap.put("TRANSACTION_ID", requestMap.get("TRANSACTION_ID").toString());
            itemCostAdjustmentMap.put("REQUEST", request);
            itemCostAdjustmentMap.put("PROCESS_FLAG", "P");
            response = new StringBuffer();
            responseCode = HttpServiceBean.restHttpPost(url, request, setupMap.get("USER_NAME") + ":" + setupMap.get("PASSWORD"), Constants.HTTP_POST, HttpURLConnection.HTTP_CREATED, response);
            if(responseCode==HttpURLConnection.HTTP_CREATED){
                
                itemCostAdjustmentMap.put("ADJUSTMENT_HEADER_ID", this.getAdjustmentHeaderId(response.toString()));
                itemCostAdjustmentMap.put("ADJUSTMENT_NUMBER", this.getAdjustmentNumber(response.toString()));                
                itemCostAdjustmentMap.put("STATUS_FLAG", "S");
                itemCostAdjustmentMap.put("RESPONSE", response.toString());
            }else{
                
                itemCostAdjustmentMap.put("ADJUSTMENT_HEADER_ID", null);                
                itemCostAdjustmentMap.put("ADJUSTMENT_NUMBER", null);                
                itemCostAdjustmentMap.put("STATUS_FLAG", "F");
                itemCostAdjustmentMap.put("RESPONSE", responseCode + " : " + response);
            }
            
            if(itemCostAdjustmentMapList==null) itemCostAdjustmentMapList = new ArrayList<>();
            itemCostAdjustmentMapList.add(itemCostAdjustmentMap);
            
            count++;
            if(count>100){
                
                if(itemCostAdjustmentMapList!=null
                    && !itemCostAdjustmentMapList.isEmpty())
                    this.updateItemCostAdjustmentTable();
                count = 0;
                itemCostAdjustmentMapList = new ArrayList<>();
            }   
        }        
    }
    
    private String getAdjustmentHeaderId(String response) throws Exception {
        
        je = jp.parse(response);
        prettyResponse = gson.toJson(je);
        jsonObject = new JSONObject(prettyResponse);
        
        if(jsonObject!=null
            && jsonObject.has("AdjustmentHeaderId"))                
            return String.valueOf(jsonObject.getLong("AdjustmentHeaderId"));
        else
            return "0";
    }

    private String getAdjustmentNumber(String response) throws Exception {
        
        je = jp.parse(response);
        prettyResponse = gson.toJson(je);
        jsonObject = new JSONObject(prettyResponse);
        
        if(jsonObject!=null
            && jsonObject.has("AdjustmentNumber"))                
            return String.valueOf(jsonObject.getLong("AdjustmentNumber"));
        else
            return "0";
    }

    public void readItemCostAdjustment() throws Exception {
        
                
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
                           
                           itemCostAdjustmentMap = new HashMap<>();
                           itemCostAdjustmentMap.put("TRANSACTION_ID", csv[map.get("TRANSACTION_ID")]);
                           itemCostAdjustmentMap.put("COST_ORGANIZATION_NAME", csv[map.get("COST_ORGANIZATION_NAME")]);
//                           itemCostAdjustmentMap.put("INVENTORY_ORGANIZATION_NAME", csv[map.get("INVENTORY_ORGANIZATION_NAME")]);
//                           itemCostAdjustmentMap.put("TOTAL_UNIT_COST", csv[map.get("TOTAL_UNIT_COST")]!=null ? new BigDecimal(csv[map.get("TOTAL_UNIT_COST")]) : null);
//                           itemCostAdjustmentMap.put("TOTAL_NEW_COST", csv[map.get("TOTAL_NEW_COST")]!=null ? new BigDecimal(csv[map.get("TOTAL_NEW_COST")]) : null);
//                           itemCostAdjustmentMap.put("VALUATION_UNIT", csv[map.get("VALUATION_UNIT")]);
//                           itemCostAdjustmentMap.put("ITEM_ID", csv[map.get("COMPONENT_ITEM_ID")]);
                           itemCostAdjustmentMap.put("ITEM_NUMBER", csv[map.get("ITEM_NUMBER")]);
                           itemCostAdjustmentMap.put("DIRECT_MTL", csv[map.get("VAL_DIRECT_MTL")]!=null ? new BigDecimal(csv[map.get("VAL_DIRECT_MTL")]) : null);
                           itemCostAdjustmentMap.put("CUSTOMS", csv[map.get("VAL_CUSTOMS")]!=null ? new BigDecimal(csv[map.get("VAL_CUSTOMS")]) : null);
                           itemCostAdjustmentMap.put("FRIGHT", csv[map.get("VAL_FRIGHT")]!=null ? new BigDecimal(csv[map.get("VAL_FRIGHT")]) : null);
                           itemCostAdjustmentMap.put("VARIABLE_OVERHAED", csv[map.get("VAL_VARIABLE_OVERHAED")]!=null ? new BigDecimal(csv[map.get("VAL_VARIABLE_OVERHAED")]) : null);
                           itemCostAdjustmentMap.put("INDIRECT_MTL", csv[map.get("VAL_INDIRECT_MTL")]!=null ? new BigDecimal(csv[map.get("VAL_INDIRECT_MTL")]) : null);
                           itemCostAdjustmentMap.put("FIXED_OVERHEAD", csv[map.get("VAL_FIXED_OVERHEAD")]!=null ? new BigDecimal(csv[map.get("VAL_FIXED_OVERHEAD")]) : null);
                           itemCostAdjustmentMap.put("VAL_OTHER_HANDLING", csv[map.get("VAL_OTHER_HANDLING")]!=null ? new BigDecimal(csv[map.get("VAL_OTHER_HANDLING")]) : null);
                           itemCostAdjustmentMap.put("VAL_USEAGE", csv[map.get("VAL_USEAGE")]!=null ? new BigDecimal(csv[map.get("VAL_USEAGE")]) : null);
                           
                           
                           if(itemCostAdjustmentMapList==null) itemCostAdjustmentMapList = new  ArrayList<>();
                           itemCostAdjustmentMapList.add(itemCostAdjustmentMap);
                       }
                   }
        }
    }

    
    private void updateItemCostAdjustmentTable() throws Exception {
        
        DML dml = new DML(con);
        dml.updateData(ITEM_COST_ADJUSTMENTS_T_0804, itemCostAdjustmentMapList, keySet, exceptionMap);
        con.commit();
    }


    private void insertItemCostAdjustmentTable() throws Exception {
        
        DML dml = new DML(con);
        dml.insertData(ITEM_COST_ADJUSTMENTS_T_0804, itemCostAdjustmentMapList);
        con.commit();
    }    
    
    public void getStandardReceiptId(String transactionId) throws Exception {
        
        int responseCode = 0;                
        url = setupMap.get("HOST") + Constants.ITEM_COST_ADJUSTMENT_URL + "?q=TransactionId=" + transactionId;        
        response = new StringBuffer("");
        responseCode = HttpServiceBean.restHttpPost(url, null, setupMap.get("USER_NAME")+":"+setupMap.get("PASSWORD"), Constants.HTTP_GET, java.net.HttpURLConnection.HTTP_OK, response);        
        if(responseCode==java.net.HttpURLConnection.HTTP_OK){
            
            je = jp.parse(response.toString());
            prettyResponse = gson.toJson(je);
            jsonObject = new JSONObject(prettyResponse);
            if(jsonObject.has("count")
                && jsonObject.getInt("count")>0
                && jsonObject.has("items")
                && jsonObject.getJSONArray("items").length()>0){
                
                itemCostAdjustmentMap = new HashMap<>();
                if(jsonObject.getJSONArray("items").getJSONObject(0).has("AdjustmentHeaderId")){
                    
                    itemCostAdjustmentMap.put("ADJUSTMENT_HEADER_ID", String.valueOf(jsonObject.getJSONArray("items").getJSONObject(0).getLong("AdjustmentHeaderId")));                                    
                    itemCostAdjustmentMap.put("STATUS_FLAG", "S");
                }else{
                    
                    itemCostAdjustmentMap.put("ADJUSTMENT_HEADER_ID", null);
                    itemCostAdjustmentMap.put("STATUS_FLAG", "F");
                }
                    
                
                
                if(jsonObject.getJSONArray("items").getJSONObject(0).has("AdjustmentNumber"))                    
                    itemCostAdjustmentMap.put("ADJUSTMENT_NUMBER", jsonObject.getJSONArray("items").getJSONObject(0).getString("AdjustmentNumber"));                
                else
                    itemCostAdjustmentMap.put("ADJUSTMENT_NUMBER", null);
                
                itemCostAdjustmentMap.put("TRANSACTION_ID", transactionId);
                
                itemCostAdjustmentMapList.add(itemCostAdjustmentMap);
                   
            }
        }
    }

    public void getItemAdjustment() throws Exception {
        
        int count = 0;
        for(Map<String, Object> requestMap : requestMapList){
            
            count++;
            if(count>100){
                
                if(itemCostAdjustmentMapList!=null
                    && !itemCostAdjustmentMapList.isEmpty())
                    this.updateItemCostAdjustmentTable();
                count = 0;
                itemCostAdjustmentMapList = new ArrayList<>();
            }   
            
            this.getStandardReceiptId(requestMap.get("TRANSACTION_ID").toString());            
        }
    }

    
    public static void main(String s[]) throws Exception {
        
        try(Connection con = ConnectionManager.getConnection();){
            
            HashMap<String, String> setupMap = Util.getSetupDetails(con, Constants.SETUP_TYPE_WEBSERVICE, Constants.SUB_TYPE_PROD);
            ItemCostAdjustment ica = new ItemCostAdjustment(setupMap, con);
//                        ica.readItemCostAdjustment();
//                        if(ica.itemCostAdjustmentMapList!=null
//                            && !ica.itemCostAdjustmentMapList.isEmpty())
//                           ica.insertItemCostAdjustmentTable();
      ica.executeItemCostAdjustment();            
        }
    }
    
}
