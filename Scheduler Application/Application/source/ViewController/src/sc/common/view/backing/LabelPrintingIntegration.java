package sc.common.view.backing;

import HTTPClient.HttpURLConnection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import sc.common.biReport.PublicReportService;
import sc.common.view.bean.LPWorkOrderBean;
import sc.common.view.bean.LPWorkOrderSerialNoBean;
import sc.common.view.bean.ReportDataBean;
import sc.common.view.bean.ReportDataMappingBean;
import sc.common.view.bean.ReportSetupDetailBean;
import sc.common.view.util.Constants;
import sc.common.view.util.ExceptionLog;
import sc.common.view.util.HttpServiceBean;
import sc.common.view.util.Util;

public class LabelPrintingIntegration {
    
    HashMap<String, String> setupMap = null;
    ReportDataBean bean=null;
    Util util=null;
    PublicReportService port=null;
    Connection con = null;
    HashMap<String,Integer> map1 = new HashMap<>();
    HashMap<String,Integer> map2 = new HashMap<>();
    
    private static final String UPDATE_GENERATE_SERIAL_SUCCESS_QUERY = "UPDATE LABEL_PRINT_GEN_SERIAL SET START_SERIAL_NUMBER =?,GEN_SERIAL_WS_STATUS = ?,WS_ERROR_MSG=? WHERE WORK_ORDER_ID =?";
    private static final String UPDATE_ASSOCIATE_SERIAL_SUCCESS_QUERY = "UPDATE LABEL_PRINT_GEN_SERIAL SET START_SERIAL_NUMBER =?,ASSOC_SERIAL_WS_STATUS = ?,WS_ERROR_MSG=? WHERE WORK_ORDER_ID =?";
    private static final String getSerialReportHeaderData = "SELECT REPORT_PATH, REPORT_DATA_TABLE, REPORT_DATA_FORMAT, REPORT_DATA_LOCALE, SERVICE_TYPE, IS_REFRESH, DELIMITER, ROOT_NODE FROM XX_SCH_REPORT_SETUP_HDR WHERE  EI_REP_HEADER_ID = 9263";
    private static final String getSerialReportlineData = "SELECT PARAM_NAME, PARAM_SQL_TYPE, DEFUALT_VAL, LAST_VAL_LOOKUP_TYPE FROM XX_SCH_REPORT_SETUP_DTL WHERE  EI_REP_HEADER_ID = 9263";
        
    public LabelPrintingIntegration(ReportDataBean bean, PublicReportService port, HashMap<String, String> setupMap, Connection con) {
        super();
        this.util = new Util();
        this.bean = bean;
        this.port = port;
        this.setupMap = setupMap;        
        this.con = con;
    }
    
//    public LabelPrintingIntegration( PublicReportService port, HashMap<String, String> setupMap, Connection con) {
//        super();
//        this.util = new Util();
//        this.port = port;
//        this.setupMap = setupMap;        
//        this.con = con;
//    }
    
    
//        public static void main(String[] args){
//            try{
//                Connection con=ConnectionManager.getConnectionX(); 
//                HashMap<String, String> setupMap= Util.getSetupDetails(con, Constants.SETUP_TYPE_WEBSEVICE, Constants.SUB_TYPE_SCM);
//                PublicReportService port =ServiceBean.getPublicReportServicePort(setupMap.get("HOST"));
//                LabelPrintingIntegration ln = new LabelPrintingIntegration(port,setupMap,con);
//                ln.executeLabelPrintingIntegration();
//                
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//        }
        
        
    public int executeLabelPrintingIntegration() throws Exception {
        int recordCount = 0;
        try{
            setupMap= Util.getSetupDetails(con, Constants.SETUP_TYPE_WEBSEVICE, Constants.SUB_TYPE_SCM);
            
            ArrayList<LPWorkOrderBean> workOrderAL = callGetWorkOrderReport();
            recordCount = workOrderAL.size();
            if(workOrderAL.size() > 0){
                writeWorkOrderDtl(workOrderAL);
                for(LPWorkOrderBean woBean : workOrderAL ){
                    String startSerialNumber = callgenerateSerialAPI(woBean);
                    if(startSerialNumber != null){
                        String associateApiStatus = callAssociateSerialAPI(startSerialNumber,woBean.getWorkOrderId());
                        if("ASSOCIATED".equals(associateApiStatus)){
                            ArrayList<LPWorkOrderSerialNoBean> workOrderSerialAL = callGetSerialReport(woBean);
                            if(workOrderSerialAL.size() > 0){
                                writeWordOrderSerialDtl(workOrderSerialAL,woBean.getSerialId());
                            }
                        }
                    }
                }
                util.updateSchedulerDate(Constants.SERVICE_TYPE_LABEL_WORK_ORDER);
            }
        }catch(Exception e){
            e.printStackTrace();
            ExceptionLog.CreateExceptionLog(this.getClass().getName(),"executeLabelPrintingIntegration",e,"");  
            throw e;
        }
        return recordCount;
    }


    private String callgenerateSerialAPI(LPWorkOrderBean woBean) throws Exception {
        StringBuffer response = new StringBuffer("");
        String startSerialNumber = null;
        String noOfSerials = "1";
        try{
            String url = setupMap.get("HOST") + Constants.GENERATE_SERIAL_REST_URL;
            String request = "{\n" + 
                             "  \"OrganizationCode\": "+woBean.getInventoryOrgCode()+",\n" + 
                             "  \"ItemNumber\":\""+woBean.getEpnNumber()+"\",\n" +
                             "  \"NumberOfSerials\": "+noOfSerials+"\n" + 
                             "}";
            
            HttpServiceBean.restHttpPost(url, request,
                                         setupMap.get("USER_NAME") + ":" + setupMap.get("PASSWORD"),
                                         Constants.HTTP_POST, response, HttpURLConnection.HTTP_CREATED);
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(response.toString());
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String prettyResponse = gson.toJson(je);
//            System.out.println(prettyResponse);
            JSONObject jsonObject = new JSONObject(prettyResponse);
            if (jsonObject.has("StartSerialNumber")) {
                startSerialNumber = jsonObject.getString("StartSerialNumber");
                this.updateTable(con, UPDATE_GENERATE_SERIAL_SUCCESS_QUERY, "SUCCESS",startSerialNumber, null,woBean.getWorkOrderId());
                return startSerialNumber;
            }
        }catch(Exception e){
            e.printStackTrace();
            this.updateTable(con, UPDATE_GENERATE_SERIAL_SUCCESS_QUERY, "ERROR",startSerialNumber, response.toString(),woBean.getWorkOrderId());
            throw e;
        }
        return startSerialNumber;
    }

    private String callAssociateSerialAPI(String startSerialNumber, String workOrderId)throws Exception {
        StringBuffer response = new StringBuffer("");
        String serialStatus = null;
        try{
            String url = setupMap.get("HOST") + Constants.ASSOCIATE_SERIAL_REST_URL_1 + workOrderId + Constants.ASSOCIATE_SERIAL_REST_URL_2;
            String request = "{\"SerialNumber\":\""+startSerialNumber+"\"}";
            
//            System.out.println("url for Associate = "+url);
//            System.out.println("request = ");
//            System.out.println(request);
            
            HttpServiceBean.restHttpPost(url, request,
                                         setupMap.get("USER_NAME") + ":" + setupMap.get("PASSWORD"),
                                         Constants.HTTP_POST, response, HttpURLConnection.HTTP_CREATED);
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(response.toString());
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String prettyResponse = gson.toJson(je);
//            System.out.println(prettyResponse);
            JSONObject jsonObject = new JSONObject(prettyResponse);
            if (jsonObject.has("SerialStatus")) {
                serialStatus = jsonObject.getString("SerialStatus");
//                System.out.println("serialStatus = "+serialStatus);
                if("ASSOCIATED".equals(serialStatus)){
                    this.updateTable(con, UPDATE_ASSOCIATE_SERIAL_SUCCESS_QUERY, "SUCCESS",startSerialNumber, null,workOrderId);    
                }else{
                    this.updateTable(con, UPDATE_ASSOCIATE_SERIAL_SUCCESS_QUERY, "ERROR",startSerialNumber, response.toString(),workOrderId);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            this.updateTable(con, UPDATE_ASSOCIATE_SERIAL_SUCCESS_QUERY, "ERROR",startSerialNumber, response.toString(),workOrderId);
            throw e;
        }
        return serialStatus;
    }
    
    public void updateTable(Connection con, String query, String status, String value, String error, String workOrderId) throws SQLException {

        try (PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, value);
            ps.setString(2, status);
            ps.setString(3, error);
            ps.setString(4, workOrderId);
            ps.executeUpdate();
            con.commit();
        }
    }
    
    
    private ArrayList<LPWorkOrderBean> callGetWorkOrderReport()throws Exception{
        ArrayList<LPWorkOrderBean> workOrderAL = new ArrayList<LPWorkOrderBean>();
        byte[] reportData = util.runBIReport(bean,setupMap,port);
        try (      InputStream is = new ByteArrayInputStream(reportData);
                   BufferedReader reader = new BufferedReader(new InputStreamReader(is,Constants.UTF8));){
                String line;
                int lineNum = 0;
                while ((line = reader.readLine()) != null) {
                    
                    lineNum ++;
                    String csv[] = util.splitLine(line, bean.getDelimiter());
                    if(lineNum==1){
                        
                        csv[0] = csv[0].substring(1);
                        for(int i = 0; i<csv.length; i++)
                            map1.put(csv[i],i);
                    }else{
                        LPWorkOrderBean woBean = new LPWorkOrderBean();
                        woBean.setInventoryOrgCode(csv[map1.get("INVENTORY_ORGANIZATION")]);
                        woBean.setInventoryOrgId(csv[map1.get("INVENTORY_ORGANIZATION_ID")]);
                        woBean.setInventoryOrgName(csv[map1.get("INVENTORY_ORGANIZATION_NAME")]);
                        woBean.setWorkOrderNumber(csv[map1.get("WORK_ORDER_NUMBER")]);
                        woBean.setWorkOrderId(csv[map1.get("WORK_ORDER_ID")]);
                        woBean.setEpnNumber(csv[map1.get("EPN_NUMBER")]);
                        woBean.setScheduledQty(csv[map1.get("SCHEDULED_QTY")]);
                        woBean.setStatus(csv[map1.get("TO_STATUS")]);
                        
                        workOrderAL.add(woBean);
                    }
                }
            }
        return workOrderAL;
    }
    
    
    private ArrayList<LPWorkOrderSerialNoBean> callGetSerialReport(LPWorkOrderBean woBean)throws Exception{
        ArrayList<LPWorkOrderSerialNoBean> workOrderSerialAL = new ArrayList<LPWorkOrderSerialNoBean>();
        ReportDataBean beanforSerial =new ReportDataBean();
        setReportDataForSerialNumbers(beanforSerial,woBean);
        
        byte[] reportData = util.runBIReport(beanforSerial,setupMap,port);
        try (      InputStream is = new ByteArrayInputStream(reportData);
                   BufferedReader reader = new BufferedReader(new InputStreamReader(is,Constants.UTF8));){
                String line;
                int lineNum = 0;
                while ((line = reader.readLine()) != null) {
                    
                    lineNum ++;
                    String csv[] = util.splitLine(line, bean.getDelimiter());
                    if(lineNum==1){
                        
                        csv[0] = csv[0].substring(1);
                        for(int i = 0; i<csv.length; i++)
                            map2.put(csv[i],i);
                    }else{
                        LPWorkOrderSerialNoBean woSerialBean = new LPWorkOrderSerialNoBean();
                        woSerialBean.setInventoryOrgCode(csv[map2.get("INVENTORY_ORGANIZATION")]);
                        woSerialBean.setInventoryOrgId(csv[map2.get("INVENTORY_ORGANIZATION_ID")]);
                        woSerialBean.setInventoryOrgName(csv[map2.get("INVENTORY_ORGANIZATION_NAME")]);
                        woSerialBean.setWorkOrderNumber(csv[map2.get("WORK_ORDER_NUMBER")]);
                        woSerialBean.setWorkOrderId(csv[map2.get("WORK_ORDER_ID")]);
                        woSerialBean.setEpnNumber(csv[map2.get("EPN_NUMBER")]);
                        woSerialBean.setScheduledQty(csv[map2.get("SCHEDULED_QTY")]);
                        woSerialBean.setAssociatedQty(csv[map2.get("ASSOCIATED_QUANTITY")]);
                        woSerialBean.setWorkOrderSerialNumber(csv[map2.get("WORK_ORDER_SERIAL_NUMBER")]);
                        
                        workOrderSerialAL.add(woSerialBean);
                    }
                }
            }
        return workOrderSerialAL;
    }

    private void writeWordOrderSerialDtl(ArrayList<LPWorkOrderSerialNoBean> workOrderSerialAL, String serialId)throws Exception {
        try(PreparedStatement insertPstmt = con.prepareStatement(this.getInsertWorkOrderDtlQuery())){
            for(LPWorkOrderSerialNoBean woBean : workOrderSerialAL ){
                workOrderSerialTable(woBean,insertPstmt,serialId);    
            }
            insertPstmt.executeBatch();
            con.commit();
        }
    }
    
    
    private String getInsertWorkOrderDtlQuery(){
        
        StringBuffer insertSerialQuery = new StringBuffer("INSERT INTO LABEL_PRINT_GEN_SERIAL_DTL");
        insertSerialQuery.append("(SERIAL_DTL_ID,");
        insertSerialQuery.append("SERIAL_ID,"); int x=0;
        insertSerialQuery.append("WORK_ORDER_NUMBER,"); x++;
        insertSerialQuery.append("EPN_NUMBER,"); x++;
        insertSerialQuery.append("SERIAL_NUMBER,"); x++;
        insertSerialQuery.append("PRINTED )"); x++;      
        insertSerialQuery.append(" VALUES("); 
        insertSerialQuery.append("LABEL_PRINT_GEN_SERIAL_DTL_SEQ.NEXTVAL,");
        for(int i=0;i<x;i++){
            insertSerialQuery.append("?,");
        }
        insertSerialQuery.append("?)");
        return insertSerialQuery.toString();
    }

    private void workOrderSerialTable(LPWorkOrderSerialNoBean woBean, PreparedStatement insertPstmt, String serialId) throws Exception {
        insertPstmt.setString(1,serialId);
        insertPstmt.setString(2,woBean.getWorkOrderNumber());
        insertPstmt.setString(3,woBean.getEpnNumber());
        insertPstmt.setString(4,woBean.getWorkOrderSerialNumber());
        insertPstmt.setString(5,"NO");
        insertPstmt.addBatch(); 
    }

    private void writeWorkOrderDtl(ArrayList<LPWorkOrderBean> workOrderAL) throws Exception {
        try(PreparedStatement insertPstmt = con.prepareStatement(this.getInsertWorkOrderQuery())){
            for(LPWorkOrderBean woBean : workOrderAL ){
                workOrderTable(woBean,insertPstmt);    
            }
            insertPstmt.executeBatch();
            con.commit();
        }
    }
    
    private String getInsertWorkOrderQuery()throws Exception{
        
        StringBuffer insertWOQuery = new StringBuffer("INSERT INTO LABEL_PRINT_GEN_SERIAL");
        insertWOQuery.append("(SERIAL_ID,");int x=0;
        insertWOQuery.append("INV_ORG_CODE,"); x++;
        insertWOQuery.append("INV_ORG_ID,"); x++;
        insertWOQuery.append("INV_ORG_NAME,"); x++;
        insertWOQuery.append("WORK_ORDER_ID,"); x++;
        insertWOQuery.append("WORK_ORDER_NUMBER,"); x++;
        insertWOQuery.append("EPN_NUMBER,"); x++;
        insertWOQuery.append("SCHEDULED_QTY,"); x++;
        insertWOQuery.append("WORK_ORDER_STATUS )"); x++;      
        insertWOQuery.append(" VALUES("); 
        for(int i=0;i<x;i++){
                insertWOQuery.append("?,");
        }
        insertWOQuery.append("?)");
        return insertWOQuery.toString();
    }
    
    private void workOrderTable(LPWorkOrderBean woBean, PreparedStatement insertPstmt)throws Exception {
        woBean.setSerialId(util.getSequenceValue(con, Constants.LABEL_PRINTING_WO_SEQ));
        
        insertPstmt.setString(1,woBean.getSerialId());
        insertPstmt.setString(2,woBean.getInventoryOrgCode());
        insertPstmt.setString(3,woBean.getInventoryOrgId());
        insertPstmt.setString(4,woBean.getInventoryOrgName());
        insertPstmt.setString(5,woBean.getWorkOrderId());
        insertPstmt.setString(6,woBean.getWorkOrderNumber());
        insertPstmt.setString(7,woBean.getEpnNumber());
        insertPstmt.setString(8,woBean.getScheduledQty());
        insertPstmt.setString(9,woBean.getStatus());
        insertPstmt.addBatch(); 
    }

    private void setReportDataForSerialNumbers(ReportDataBean beanforSerial,LPWorkOrderBean woBean) throws Exception {
        String userName =setupMap.get("USER_NAME");
        String password =setupMap.get("PASSWORD");
        try(PreparedStatement selectHdrPstmt = con.prepareStatement(getSerialReportHeaderData);
            ResultSet rsHdr = selectHdrPstmt.executeQuery();
            PreparedStatement selectLinePstmt = con.prepareStatement(getSerialReportlineData);
            ResultSet rsLine = selectLinePstmt.executeQuery();){
            
            if(rsHdr.next()){
                beanforSerial.setDataFormat(rsHdr.getString("REPORT_DATA_FORMAT"));
                beanforSerial.setDataLocale(rsHdr.getString("REPORT_DATA_LOCALE"));
                beanforSerial.setDelimiter(rsHdr.getString("DELIMITER"));
                beanforSerial.setIsRefresh(rsHdr.getString("IS_REFRESH"));
                beanforSerial.setReportDataTable(rsHdr.getString("REPORT_DATA_TABLE"));
                beanforSerial.setReportPath(rsHdr.getString("REPORT_PATH"));
                beanforSerial.setServiceType(rsHdr.getString("SERVICE_TYPE"));
                beanforSerial.setRootNode(rsHdr.getString("ROOT_NODE"));
                if(beanforSerial.getReportPath()!=null){
                        boolean isAvailable=port.isReportExist(beanforSerial.getReportPath(),userName, password);
                        if(!isAvailable){
                        throw new Exception("Invalid Report Path : "+beanforSerial.getReportPath());
                        }
                }
                
                List<ReportSetupDetailBean> detailList=new ArrayList<ReportSetupDetailBean>();
                ReportSetupDetailBean setupDtlBean = null;
                while(rsLine.next()){
                    setupDtlBean = new ReportSetupDetailBean();
                    setupDtlBean.setSqlStatement(null);
                    setupDtlBean.setDefualtVal(woBean.getWorkOrderNumber());
                    setupDtlBean.setParamName(rsLine.getString("PARAM_NAME"));
                    detailList.add(setupDtlBean);
                }
                beanforSerial.setReportSetupDetail(detailList);
                
                List<ReportDataMappingBean> dataMappingList=new ArrayList<ReportDataMappingBean>();
                beanforSerial.setReportDataMappings(dataMappingList);
            }
        }
    }
}
