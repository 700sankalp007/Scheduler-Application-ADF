package sc.common.view.backing;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import java.net.HttpURLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.SOAPException;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

import org.w3c.dom.Document;

import org.xml.sax.InputSource;

import sc.common.biReport.PublicReportService;
import sc.common.essJob.ErpIntegrationService;
import sc.common.essJob.SubmitESSJobRequest;
import sc.common.view.bean.PhysicalCountTagListBean;
import sc.common.view.bean.PhysicalCountTagListMstBean;
import sc.common.view.bean.ReportDataBean;
import sc.common.view.service.UCMService;
import sc.common.view.util.ConnectionManager;
import sc.common.view.util.Constants;
import sc.common.view.util.ExceptionLog;
import sc.common.view.util.HttpServiceBean;
import sc.common.view.util.ServiceBean;
import sc.common.view.util.SoapHandler;
import sc.common.view.util.Util;


public class PhysicalCountIntegration {
    HashMap<String, String> setupMap = null;
    ReportDataBean bean=null;
    Util util=null;
    PublicReportService port=null;
    Connection con = null;
    
    private long totalTime = (1800000*8);//4 hrs
    private long intervalTime = 120000;//2 min 
    
    String selectSQL="SELECT DISTINCT TAG_ID FROM WH360_PHY_INV_TAG_LIST";
    String selectMstSQL="SELECT DISTINCT PHYSICAL_INVENTORY_ID FROM WH360_PHY_INV_TAG_LIST_MST";
    
    Set<String> masterSet = null;
    Set<String> lineSet = null;
    String runHisId = null;
    List<Future<?>> futures = new ArrayList<Future<?>>();
    ExecutorService executorService = null;
    public static final String TAG = "PhysicalCountIntegration";
    
//    PhysicalCountIntegration(){
//            try {
//                this.util = new Util();
//                this.con = ConnectionManager.P2TgetConnection();
//                this.setupMap = Util.getSetupDetails(con, Constants.SETUP_TYPE_WEBSEVICE, Constants.SUB_TYPE_FUSION);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    
    public PhysicalCountIntegration(ReportDataBean bean, PublicReportService port, HashMap<String, String> setupMap, Connection con, String runHisId) {
        super();
        this.util = new Util();
        this.bean = bean;
        this.port = port;
        this.setupMap = setupMap;
//        System.out.println("setupMap :" + setupMap);
        this.con = con;
        this.runHisId = runHisId;
    }
    
//    public static void main(String[] arg){
//            PhysicalCountIntegration pc = new PhysicalCountIntegration();
//            Util util1 = new Util();
//            try {
//                byte[] data = pc.getUCMFileData("Test123");
//                int  recordProcessed = pc.transform(data);
//    //            String outputString = "";
//                System.out.print("PC: recordProcessed" + recordProcessed + "");
////                util1.updateSchedulerDate(Constants.SERVICE_TYPE_PHYSICAL_COUNT_DETAIL);
////                System.out.print("PC: recordProcessed" + recordProcessed + "\nrunHisId: " + runHisId + "\nben.getReportJobName()" + bean.getReportJobName());
////                con = ConnectionManager.P2TgetConnection();
////                util1.updateRecordProcessHistory(recordProcessed,false,runHisId,con,bean.getReportJobName());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }finally{
//                if(con != null){
//                try {
//                    con.close();
//                } catch (SQLException e) {
//                }
//            }
//            }
//    
//        }
    
//    public static void main(String args[]){
//        Calendar now = Calendar.getInstance();
//        System.out.println("Date = "+now.get(Calendar.DAY_OF_MONTH) + "_" + (now.get(Calendar.MONTH) + 1) + "_" +
//                        now.get(Calendar.YEAR) + "_" + now.get(Calendar.HOUR_OF_DAY) + "_" + now.get(Calendar.MINUTE) +
//                        "_" + now.get(Calendar.SECOND));
//    }
    

    public double executePhysicalCountIntegration() throws Exception {
        int recordProcessed = 0;
        try{
            //TODO: Implement purge data logic before start report data integration.
            PurgePhysicalCountData(con);
            
            String currentDate=getcurrentDate();
            long reqId = ScheduleJob(currentDate);
            String jobStatus = getJobStatus(reqId);
            System.out.println("PHYSICAL_COUNT: JOB_STATUS: " + jobStatus);
            if(jobStatus.equalsIgnoreCase("SUCCEEDED") || jobStatus.equalsIgnoreCase("Warning")){
//                getFilefromUCM(currentDate);
//                String responseString = getFilefromUCM(currentDate);
//                recordProcessed = insertIntoDatabase(responseString);
                byte[] data = getUCMFileData(currentDate);
                recordProcessed = transform(data);
            }
//            util.updateSchedulerDate(Constants.SERVICE_TYPE_PHYSICAL_COUNT_DETAIL);
            util.updatePCSchedulerDate(Constants.SERVICE_TYPE_PHYSICAL_COUNT_DETAIL);
        }catch(Exception e){
            e.printStackTrace();
            ExceptionLog.CreateExceptionLog(this.getClass().getName(),"executePhysicalCountIntegration",e,"");  
            throw e;
        }
        return recordProcessed;
    }
    
    public String getcurrentDate(){
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.DAY_OF_MONTH) + "_" + (now.get(Calendar.MONTH) + 1) + "_" +
                        now.get(Calendar.YEAR) + "_" + now.get(Calendar.HOUR_OF_DAY) + "_" + now.get(Calendar.MINUTE) +
                        "_" + now.get(Calendar.SECOND);
    }
    
    public String getJobStatus(long requestId) throws Exception {
        try{
            String jobStatus = null;
            ErpIntegrationService erpPort = ServiceBean.getIntegrationServicePort(setupMap.get("HOST"));
            BindingProvider bindingProvider = (BindingProvider)erpPort;
            Binding binding = bindingProvider.getBinding();
            List<Handler> handlerChain = binding.getHandlerChain();
            handlerChain.add(new SoapHandler());
            binding.setHandlerChain(handlerChain);
            Map<String, Object> requestContext = bindingProvider.getRequestContext();  
            requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, setupMap.get("HOST") + Constants.ERP_INTEGRATION_WSDL);
            requestContext.put(BindingProvider.USERNAME_PROPERTY, setupMap.get("USER_NAME"));
            requestContext.put(BindingProvider.PASSWORD_PROPERTY, setupMap.get("PASSWORD"));
            
            long timeMilli=0;
            while(true){
               if(timeMilli > totalTime){
                    jobStatus = null;
                   break;
                }
                try {
                    jobStatus = erpPort.getESSJobStatus(requestId);
//                    System.out.println("Job Status="+jobStatus);
//                    System.out.println("Request Id="+requestId);
                } catch(Exception e) {
                    e.printStackTrace();
                }
               if(jobStatus != null && (jobStatus.equalsIgnoreCase("SUCCEEDED") || jobStatus.equalsIgnoreCase("Warning") || jobStatus.equalsIgnoreCase("Error"))){
                   break;
               }
               else{
                   Thread.sleep(intervalTime);
                   timeMilli+=intervalTime;
               }
            }
            
            return jobStatus;
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    
    public long ScheduleJob(String currentDate) throws Exception {
        try {
            ErpIntegrationService integrationServicePort = ServiceBean.getIntegrationServicePort(setupMap.get("SERVLET_CONTEXT_PATH") + Constants.LOCAL_ERP_INTEGRATION_WSDL);
//            ErpIntegrationService integrationServicePort = ServiceBean.getIntegrationServicePort(setupMap.get("HOST"));
            BindingProvider bp = (BindingProvider) integrationServicePort;
            Binding binding = bp.getBinding();
            List<Handler> handlerChain = binding.getHandlerChain();
            handlerChain.add(new SoapHandler());
            binding.setHandlerChain(handlerChain);
            Map<String, Object> requestContext = bp.getRequestContext();
            requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, setupMap.get("HOST") + Constants.ERP_INTEGRATION_WSDL);
            requestContext.put(BindingProvider.USERNAME_PROPERTY, setupMap.get("USER_NAME"));
            requestContext.put(BindingProvider.PASSWORD_PROPERTY, setupMap.get("PASSWORD"));
            
            SubmitESSJobRequest jobrequest = new SubmitESSJobRequest();
            jobrequest.setJobPackageName("/oracle/apps/ess/custom/oracle/apps/ess/custom/");
            jobrequest.setJobDefinitionName("PINVJOBINV");
            List<String> paramList = jobrequest.getParamList();
            
            String FromDate = util.getLastUpdatedDate(Constants.SERVICE_TYPE_PHYSICAL_COUNT_DETAIL);
            String toDate = util.getCurrentDate();
            paramList.add(currentDate);
            paramList.add(FromDate);
            paramList.add(toDate);
            System.out.println("paramList1 : " + paramList);
            long reqId = integrationServicePort.submitESSJobRequest("/oracle/apps/ess/custom/oracle/apps/ess/custom/", "PINVJOBINV", paramList);
//            System.out.println("request ID"+reqId);
            return reqId;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    
//    public void getFilefromUCM(String docName)throws Exception{
//        String responseString = "";
//        String outputString = "";
//        int count = 0;
//        try{
////            System.out.println("PHYSICAL_COUNT: START getFilefromUCM() ");
//            String wsURL = setupMap.get("HOST")+Constants.GENERIC_SOAP_WSDL;
//            URL url = new URL(wsURL);
//            URLConnection connection = url.openConnection();
//            HttpURLConnection httpConn = (HttpURLConnection)connection;
//            ByteArrayOutputStream bout = new ByteArrayOutputStream();
//            String auth = setupMap.get("USER_NAME") + ":" + setupMap.get("PASSWORD");
//            byte[] encodedAuth = Base64Encoder.encode(auth).getBytes(); //.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
//            String authHeaderValue = "Basic " + new String(encodedAuth);
//            httpConn.setRequestProperty("Authorization", authHeaderValue);
//            String xmlInput =
//            " <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ucm=\"http://www.oracle.com/UCM\">\n" + 
//            "   <soapenv:Header/>\n" + 
//            "   <soapenv:Body>\n" + 
//            "      <ucm:GenericRequest webKey=\"cs\">\n" + 
//            "         <ucm:Service IdcService=\"GET_FILE\">\n" + 
//            "            <ucm:Document>\n" + 
//            "               <ucm:Field name=\"dDocName\">"+docName+"</ucm:Field>\n" + 
//            "               <ucm:Field name=\"RevisionSelectionMethod\">Latest</ucm:Field>\n" + 
//            "               <ucm:Field name=\"Rendition\">Primary</ucm:Field>\n" + 
//            "            </ucm:Document>\n" + 
//            "         </ucm:Service>\n" + 
//            "      </ucm:GenericRequest>\n" + 
//            "   </soapenv:Body>\n" + 
//            "</soapenv:Envelope>";
//            byte[] buffer = new byte[xmlInput.length()];
//            buffer = xmlInput.getBytes();
//            bout.write(buffer);
//            byte[] b = bout.toByteArray();
//            httpConn.setRequestProperty("Content-Length",
//            String.valueOf(b.length));
//            httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
//            httpConn.setRequestMethod("POST");
//            httpConn.setDoOutput(true);
//            httpConn.setDoInput(true);
//            OutputStream out = httpConn.getOutputStream();
//            //Write the content of the request to the outputstream of the HTTP Connection.
//            out.write(b);
//            out.close();
//            InputStreamReader isr =
//            new InputStreamReader(httpConn.getInputStream());
//            BufferedReader in = new BufferedReader(isr);
//    
//            //Write the SOAP message response to a String.
//            int i=0;
//            if(in == null){
////                System.out.println("PHYSICAL_COUNT: IN NULL ");
//            }else{
////            System.out.println("PHYSICAL_COUNT: IN NOT NULL ");
//            }
//
//            
//            executorService = Executors.newFixedThreadPool(5);
//            while ((responseString = in.readLine()) != null) {
//                count ++;
//                System.out.println("\nPHYSICAL_COUNT 1: COUNT " + count);
//                System.out.println("\nPHYSICAL_COUNT 2: responseString " + responseString);
//                
////                ExecutorService executorLog = null;
////                executorLog = Executors.newFixedThreadPool(1);
////                Runnable worker = new SetLogExecuterService(responseString);
////                executorLog.execute(worker);
//                
//                if(i<12){
//                    i++;
//                    continue;
//                }
//                
//            outputString = outputString + responseString+"\n";
////            System.out.println("\nPHYSICAL_COUNT 3: outputString " + outputString);
////                if(count == 10){
////                    break;
////                }
//            }
//            responseString="";
//            String strArray[]=outputString.split("\n");
//            for(i=0;i<strArray.length-2;i++){
////                System.out.println("\nPHYSICAL_COUNT 4: strArray[] " + strArray[i]);
//                responseString+=strArray[i]+"\n";
//            }  
//            
//        }catch(Exception e){
//            e.printStackTrace();
//            throw e;
//        }
//        finally{
//            // check for execureservice is running, then shutdown and update records and runHistory
//            
////            if(executorLog != null)
////            {
////                    executorLog.shutdown();
////                            while (!executorLog.isTerminated()) {
////                    }
////
////            }
//            util.updateSchedulerDate(Constants.SERVICE_TYPE_PHYSICAL_COUNT_DETAIL);
//            util.updateRecordProcessHistory(count,false,runHisId,con,bean.getReportJobName());
//            
//        }
////        System.out.println("PHYSICAL_COUNT: ResponseString Length" + responseString.length());
//        System.out.println("\nPHYSICAL_COUNT 5: final responseString" + responseString);
////        return responseString;
//    }
//    
//    public int insertIntoDatabase(String records)throws Exception{
//        String insertSQL="INSERT INTO WH360_PHY_INV_TAG_LIST  \n" + 
//                        "        (TAG_NUMBER,  \n" + 
//                        "        SUBINVENTORY,  \n" + 
//                        "        ITEM_NUMBER,  \n" + 
//                        "        LOCATOR_ID,  \n" + 
//                        "        LOCATOR,  \n" + 
//                        "        LOT_NUMBER,  \n" + 
//                        "        QUANTITY,  \n" + 
//                        "        ITEM_ID,  \n" + 
//                        "        ITEM_UOM,  \n" + 
//                        "        ORGANIZATION_CODE,  \n" + 
//                        "        ORGANIZATION_ID,  \n" + 
//                        "        ITEM_DESCRIPTION,  \n" + 
//                        "        PHYSICAL_INVENTORY_NAME,  \n" + 
//                        "        PHYSICAL_INVENTORY_ID,  \n" + 
//                        "        TAG_ID,  \n" + 
//                        "        ADJUSTMENT_ID,  \n" + 
//                        "        TAG_UOM,  \n" + 
//                        "        TAG_UOM_CODE,  \n" + 
//                        "        TAG_TYPE_CODE,  \n" + 
//                        "        KEY_VALUE,  \n" + 
//                        "        UNIQUE_SEQUENCE,  \n" + 
//                        "        CROSS_REFERENCE,STATUS, SERIAL_NUMBER, REVISION, ITEM_TYPE, UNIT_COST_FD, UNIT_COST_CURRENT, SNAPSHOT_QTY, MOBILE_QUANTITY, PC_ASSIGNED_USER_ID, PC_ASSIGNED_USER_NAME, LAST_UPDATE_DATE)\n" + 
//                        "        VALUES  \n" + 
//                        "        (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//        
//        String updateSQL="UPDATE WH360_PHY_INV_TAG_LIST  \n" + 
//                        "        SET TAG_NUMBER=?,  \n" + 
//                        "        SUBINVENTORY=?,  \n" + 
//                        "        ITEM_NUMBER=?,  \n" + 
//                        "        LOCATOR_ID=?,  \n" + 
//                        "        LOCATOR=?,  \n" + 
//                        "        LOT_NUMBER=?,  \n" + 
//                        "        QUANTITY=?,  \n" + 
//                        "        ITEM_ID=?,  \n" + 
//                        "        ITEM_UOM=?,  \n" + 
//                        "        ORGANIZATION_CODE=?,  \n" + 
//                        "        ORGANIZATION_ID=?,  \n" + 
//                        "        ITEM_DESCRIPTION=?,  \n" + 
//                        "        PHYSICAL_INVENTORY_NAME=?,  \n" + 
//                        "        PHYSICAL_INVENTORY_ID=?,  \n" + 
//                        "        TAG_ID=?,  \n" + 
//                        "        ADJUSTMENT_ID=?,  \n" + 
//                        "        TAG_UOM=?,  \n" + 
//                        "        TAG_UOM_CODE=?,  \n" + 
//                        "        TAG_TYPE_CODE=?,  \n" + 
//                        "        KEY_VALUE=?,  \n" + 
//                        "        UNIQUE_SEQUENCE=?,  \n" + 
//                        "        CROSS_REFERENCE=?, \n" +
//                        "        SERIAL_NUMBER=?, \n" + 
//                        "        REVISION=?, \n" + 
//                        "        ITEM_TYPE=?, \n" + 
//                        "        UNIT_COST_FD=?, \n" + 
//                        "        UNIT_COST_CURRENT=?, \n" + 
//                        "        SNAPSHOT_QTY=?, \n" + 
//                        "        MOBILE_QUANTITY=?, \n" + 
//                        "        PC_ASSIGNED_USER_ID=?, \n" + 
//                        "        STATUS=?, \n" + 
//                        "        PC_ASSIGNED_USER_NAME=?, \n" +  
//                        "        LAST_UPDATE_DATE=? \n" + 
//                        "        WHERE TAG_ID=?";
//        
//        String insMstSQL="INSERT INTO WH360_PHY_INV_TAG_LIST_MST\n" + 
//                         "(ORGANIZATION_CODE,\n" + 
//                         "ORGANIZATION_ID,\n" + 
//                         "PHYSICAL_INVENTORY_NAME,\n" + 
//                         "PHYSICAL_INVENTORY_ID,\n" + 
//                         "SYNC_STATUS,\n" + 
//                         "STATUS)VALUES(?,?,?,?,?,?)";
//        
////        masterSet = util.getExIdSet(con, selectSQL);
////        lineSet = util.getExIdSet(con, selectMstSQL);
//
////        System.out.println("PHYSICAL_COUNT: insertSQL: " + insertSQL.toString());
////        System.out.println("PHYSICAL_COUNT: updateSQL: " + updateSQL.toString());
////        System.out.println("PHYSICAL_COUNT: insMstSQL: " + insMstSQL.toString());
//        masterSet = util.getExIdSet(con, selectMstSQL);
//        lineSet = util.getExIdSet(con, selectSQL);        
////        System.out.println("PHYSICAL_COUNT: MASTER SET SIZE(): " + masterSet.size());
////        System.out.println("PHYSICAL_COUNT: LINE SET SIZE(): " + lineSet.size());
//        
////        System.out.println("PHYSICAL_COUNT: before prepare statement");
//        
//        try(PreparedStatement insPST=con.prepareStatement(insertSQL);
//            PreparedStatement udPST=con.prepareStatement(updateSQL);
//            PreparedStatement insMstPST=con.prepareStatement(insMstSQL);){
//            
//            String recordArray[]=records.split("\n");
//            boolean flag=true;
//            
//            for(int i=0;i<recordArray.length;i++){
//                if(flag){
//                    flag=false;
//                    continue;
//                }
//                
////                System.out.println(1+"\t"+recordArray[i]);
//                String delimiter=",";
//                String otherThanQuote = " [^\"] ";
//                String quotedString = String.format(" \" %s* \" ", otherThanQuote);
//                String regex = String.format("(?x)"+delimiter+"(?=(%s*%s)*%s*$)",otherThanQuote, quotedString, otherThanQuote);
//                
//                String[] data=recordArray[i].split(regex,-1);
//                
////                System.out.println("PHYSICAL_COUNT: data[]: " + data.toString());
////                System.out.println("PHYSICAL_COUNT: masterSet[]: " + masterSet.toString());
//                if(data.length>10){
//                    if(!masterSet.contains(data[15])){
////                        System.out.println("Insert Header Records");
////                        System.out.println("PHYSICAL_COUNT: INSERT MASTER: " + data[15]);
//                        insMstPST.setString(1, data[11]!=null?data[11]:null);
//                        insMstPST.setString(2, data[12]!=null?data[12]:null);
//                        insMstPST.setString(3, data[14]!=null?data[14].replaceAll("^\"|\"$", ""):null);
//                        insMstPST.setString(4, data[15]!=null?data[15]:null);
//                        insMstPST.setObject(5, null);
//                        insMstPST.setObject(6, null);
//                        insMstPST.addBatch();
//                        masterSet.add(data[15]);
//                    }
//                    int userId= 0;
////                    System.out.println("PHYSICAL_COUNT: lineset[]: " + lineSet.toString());
//                    if(lineSet.contains(data[16])){
////                        System.out.println("PHYSICAL_COUNT: Update Line: " + data[16]);
////                        System.out.println("Update Line Records");
//                        udPST.setString(1, data[0]!=null?data[0]:null); // TAG_NUMBER
//                        udPST.setString(2, data[1]!=null?data[1]:null); //SUB_INV
//                        udPST.setString(3, data[2]!=null?data[2]:null); //ITEM_NUMBER
//                        udPST.setString(4, data[4]!=null?data[4]:null); //LOC_ID
//                        udPST.setString(5, data[5]!=null?data[5]:null); //LOCATOR
//                        udPST.setString(6, data[6]!=null?data[6]:null); //LOT_NUMBER
//                        udPST.setString(7, data[8]!=null?data[8]:null); //QUANTITY
//                        udPST.setString(8, data[9]!=null?data[9]:null); //ITEM_ID
//                        udPST.setString(9, data[10]!=null?data[10]:null); //ITEM_UOM
//                        udPST.setString(10, data[11]!=null?data[11]:null); //ORGANIZATION_CODE
//                        udPST.setString(11, data[12]!=null?data[12]:null); //ORGANIZATION_ID
//                        udPST.setString(12, data[13]!=null?data[13].replaceAll("^\"|\"$", ""):null); //ITEM_DESCRIPTION
//                        udPST.setString(13, data[14]!=null?data[14].replaceAll("^\"|\"$", ""):null); //PHYSICAL_INVENTORY_NAME
//                        udPST.setString(14, data[15]!=null?data[15]:null); //PHYSICAL_INVENTORY_ID
//                        udPST.setString(15, data[16]!=null?data[16]:null); //TAG_ID
//                        udPST.setString(16, data[17]!=null?data[17]:null); //ADJUSTMENT_ID
//                        udPST.setString(17, data[18]!=null?data[18]:null); //TAG_UOM
//                        udPST.setString(18, data[19]!=null?data[19]:null); //TAG_UOM_CODE
//                        udPST.setString(19, data[20]!=null?data[20]:null); //TAG_TYPE_CODE
//                        udPST.setString(20, data[22]!=null?data[22]:null); //KEY
//                        if(data.length>23&&data[23]!=null) //UNIQUE_SEQUENCE
//                            udPST.setString(21, data[23]);
//                        else
//                            udPST.setObject(21, null);
//                        udPST.setString(22, data[21]!=null?data[21]:null); //CROSS_REFERENCE
//                        udPST.setString(34, data[16]!=null?data[16]:null); //TAG_ID unique KEY
//                        udPST.setString(23, data[7]!=null?data[7]:null); //SERIAL_NUMBER
//                        udPST.setString(24, data[3]!=null?data[3]:null); //REVISION
//                        udPST.setString(25, data[26]!=null?data[26]:null); //ITEM_TYPE
//                        udPST.setObject(26, data[27]!=null?data[27]:null); //UNIT_COST_FD
//                        udPST.setObject(27, data[28]!=null?data[28]:null); //UNIT_COST_CURRENT
//                        udPST.setObject(28, data[29]!=null?data[29]:null); //SNAPSHOT_QTY
//                        if(data[20] != null && data[20].equals("3")){ //TAG_TYPE_CODE
//                            if(data[25] != null){ //COUNTED_BY
//                                String userName = data[25];
//                                //Get User id from db
//                                userId = getUserID(userName.toUpperCase());
//                                udPST.setString(29, data[8]!=null?data[8]:null); //INSERT QUANTITY from REPORT into MOBILE_QUANTITY of Database field
//                                if(userId == 0){
//                                    udPST.setObject(30, null); //PC_ASSIGNED_USER_ID
//                                    udPST.setObject(32, null); //PC_ASSIGNED_USER_NAME
//                                }else{
//                                    udPST.setObject(30, userId); //PC_ASSIGNED_USER_ID
//                                    udPST.setObject(32, userName.toUpperCase()); //PC_ASSIGNED_USER_NAME - COUNTED_BY
//                                }
//                            }else{
//                                udPST.setString(29, data[8]!=null?data[8]:null); // MOBILE_QUANTITY - what about here if TAG_TYPE_CODE -3 and COUNTEDby is null?
//                                udPST.setObject(30, null); //PC_ASSIGNED_USER_ID
//                                udPST.setObject(32, null); //PC_ASSIGNED_USER_NAME
//                            }
//                            udPST.setString(31, "COUNTED"); // STATUS
//                            Date lsatUpdateDate = Util.getLastUpdateDateInUTC();
//                            udPST.setTimestamp(33, new Timestamp(lsatUpdateDate.getTime())); //LAST_UPDATE_DATE
//                            
//                        } else{
//                            udPST.setString(29, null); //MOBILE_QUANTITY
//                            udPST.setObject(30, null); //PC_ASSIGNED_USER_ID
//                            udPST.setString(31, "PENDING"); // STATUS
//                            udPST.setObject(32, null); //PC_ASSIGNED_USER_NAME
//                            udPST.setTimestamp(33, null); //LAST_UPDATE_DATE
//                        }
//                        
//                        udPST.addBatch();
//                    }else{
////                        System.out.println("Insert Line Records");
////                        System.out.println("PHYSICAL_COUNT: INSERT Line: " + data[16]);
//                        insPST.setString(1, data[0]!=null?data[0]:null); // TAG_NUMBER
//                        insPST.setString(2, data[1]!=null?data[1]:null); //SUB_INV
//                        insPST.setString(3, data[2]!=null?data[2]:null); //ITEM_NUMBER
//                        insPST.setString(4, data[4]!=null?data[4]:null); //LOC_ID
//                        insPST.setString(5, data[5]!=null?data[5]:null); //LOCATOR
//                        insPST.setString(6, data[6]!=null?data[6]:null); //LOT_NUMBER
//                        insPST.setString(7, data[8]!=null?data[8]:null); //QUANTITY
//                        insPST.setString(8, data[9]!=null?data[9]:null); //ITEM_ID
//                        insPST.setString(9, data[10]!=null?data[10]:null); //ITEM_UOM
//                        insPST.setString(10, data[11]!=null?data[11]:null); //ORGANIZATION_CODE
//                        insPST.setString(11, data[12]!=null?data[12]:null); //ORGANIZATION_ID
//                        insPST.setString(12, data[13]!=null?data[13].replaceAll("^\"|\"$", ""):null); //ITEM_DESCRIPTION
//                        insPST.setString(13, data[14]!=null?data[14].replaceAll("^\"|\"$", ""):null); //PHYSICAL_INVENTORY_NAME
//                        insPST.setString(14, data[15]!=null?data[15]:null); //PHYSICAL_INVENTORY_ID
//                        insPST.setString(15, data[16]!=null?data[16]:null); //TAG_ID
//                        insPST.setString(16, data[17]!=null?data[17]:null); //ADJUSTMENT_ID
//                        insPST.setString(17, data[18]!=null?data[18]:null); //TAG_UOM
//                        insPST.setString(18, data[19]!=null?data[19]:null); //TAG_UOM_CODE
//                        insPST.setString(19, data[20]!=null?data[20]:null); //TAG_TYPE_CODE
//                        insPST.setString(20, data[22]!=null?data[22]:null); //KEY
//                        if(data.length>23&&data[23]!=null)
//                            insPST.setString(21, data[23]); //UNIQUE_SEQUENCE
//                        else
//                            insPST.setObject(21, null);
//                        insPST.setString(22, data[21]!=null?data[21]:null); //CROSS_REFERENCE
//                        
////                        insPST.setString(23, "PENDING");
//                        insPST.setString(24, data[7]!=null?data[7]:null); // SERIAL_NUMBER
//                        insPST.setString(25, data[3]!=null?data[3]:null); // REVISION
//                        insPST.setString(26, data[26]!=null?data[26]:null); // ITEM_TYPE
//                        insPST.setString(27, data[27]!=null?data[27]:null); // UNIT_COST_FD 
//                        insPST.setString(28, data[28]!=null?data[28]:null); // UNIT_COST_CURRENT
//                        insPST.setString(29, data[29]!=null?data[29]:null); // SNAPSHOT_QTY
//                        if(data[20] != null && data[20].equals("3")){ //TAG_TYPE_CODE
//                            // update status as COUNTED
//                            insPST.setString(23, "COUNTED"); // STATUS
//                            if(data[25] != null){ //COUNTED_BY
//                                String userName = data[25];
//                                //Get User id from db
//                                userId = getUserID(userName.toUpperCase());
//                                insPST.setString(30, data[8]!=null?data[8]:null); //INSERT QUANTITY from REPORT into MOBILE_QUANTITY of Database field
//                                if(userId == 0){
//                                    insPST.setObject(31, null); //PC_ASSIGNED_USER_ID
//                                    insPST.setObject(32, null); //PC_ASSIGNED_USER_NAME
//                                    //also update status
//                                }else{
//                                    insPST.setObject(31, userId); //PC_ASSIGNED_USER_ID
//                                    insPST.setObject(32, userName.toUpperCase()); //PC_ASSIGNED_USER_NAME - COUNTED_BY
//                                }
//                            }else{
//                                insPST.setString(30, data[8]!=null?data[8]:null); // MOBILE_QUANTITY - what about here if TAG_TYPE_CODE-3 and COUNTEDby is null?
//                                insPST.setObject(31, null); //PC_ASSIGNED_USER_ID
//                                insPST.setObject(32, null); //PC_ASSIGNED_USER_NAME
//                            }
//                            Date lsatUpdateDate = Util.getLastUpdateDateInUTC();
//                            insPST.setTimestamp(33, new Timestamp(lsatUpdateDate.getTime())); //LAST_UPDATE_DATE
//                        } else{
//                            insPST.setString(23, "PENDING");
//                            insPST.setString(30, null); //MOBILE_QUANTITY
//                            insPST.setObject(31, null); //PC_ASSIGNED_USER_ID
//                            insPST.setObject(32, null); //PC_ASSIGNED_USER_NAME
//                            insPST.setTimestamp(33, null); //LAST_UPDATE_DATE
//                        }
//                        
//                        insPST.addBatch();
//                        lineSet.add(data[16]);
//                    }
//                }
//            }
//            
////            System.out.println("PHYSICAL_COUNT: START BATCH INSERT");
////            System.out.println("PHYSICAL_COUNT: START BATCH INSERT");
////            System.out.println("PHYSICAL_COUNT: MASTER INSERT: " + masterSet.size());
//            int[] hdrInsert = insMstPST.executeBatch();
////            System.out.println("PHYSICAL_COUNT: Execute: insMstPST.executeBatch()" + hdrInsert);
//            int[] lineInsert = insPST.executeBatch();
////            System.out.println("PHYSICAL_COUNT: Execute: insPST.executeBatch()" + lineInsert);
//            int[] lineUpdtInsert = udPST.executeBatch();
////            System.out.println("PHYSICAL_COUNT: Execute: udPST.executeBatch()" + lineUpdtInsert);
//            
//            con.commit();
////            System.out.println("PHYSICAL_COUNT: commit()");
//            
//            return hdrInsert.length + lineInsert.length + lineUpdtInsert.length;
//        }catch(Exception e){
//            e.printStackTrace();
//            throw e;
//        }
//    }
    
    // Get MobileUserID of Mobile UserName
    public int getUserID(String userName) {
        
        int mobileUserId = 0;
        String statement = "SELECT MOBILE_USER_ID FROM WH360_MOBILE_USER_MANAGEMENT WHERE UPPER(MOBILE_USER_NAME) = ?";
        try(Connection cn  = ConnectionManager.getConnection();
            PreparedStatement ps =  cn.prepareStatement(statement);) {
            
            ps.setString(1, userName);
            try( ResultSet rs = ps.executeQuery();){
                if (rs != null && rs.next()) {
                    mobileUserId = rs.getInt("MOBILE_USER_ID");
                }
            } catch (SQLException sqle) {
                ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), sqle, null);
                sqle.printStackTrace();
            } 
            
        } catch (SQLException sqle) {
            ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), sqle, null);
            sqle.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mobileUserId;
    }
    
    public String formatXML(String unformattedXml) {
        try {
            Document document = parseXmlFile(unformattedXml);
            OutputFormat format = new OutputFormat(document);
            format.setIndenting(true);
            format.setIndent(3);
            format.setOmitXMLDeclaration(true);
            Writer out = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(document);
            return out.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private Document parseXmlFile(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
//    private void callExecutorService(String resString){
//        ExecutorService executorService = null;
//        try{ 
//            Runnable worker = new PhysicalCountExecuterService(resString, runHisId);
//            executorService.execute(worker);
////            Future<?> f = executorService.submit(worker);
////            futures.add(f);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            throw e;
//        } 
//    }
    
    public byte[] getUCMFileData(String UCM_ID) throws Base64DecodingException, IOException, Exception, SOAPException {
        try{
            UCMService ucmService = new UCMService();
            String url = setupMap.get("HOST") + Constants.UCM_REST_API;
            String request = util.soapMessageToString(ucmService.getFileByName(UCM_ID));
            StringBuffer response = new StringBuffer("");
            int responseCode = HttpServiceBean.restHttpPost(url, request, setupMap.get("USER_NAME")+":"+setupMap.get("PASSWORD"), Constants.HTTP_POST, HttpServiceBean.CONTENT_TYPE_XML, response, HttpURLConnection.HTTP_OK);
            if(responseCode==HttpURLConnection.HTTP_OK){
                return Base64.decode(util.getTextContentByTagName(response.toString(), "fileContent"));  
            }
            else{
                throw new RuntimeException("Unable to download file from UCM!!!");
            }
        }catch(Exception e){
            System.out.println(TAG + ": getUCMFileData(): " + e.getLocalizedMessage());
            e.printStackTrace();
            throw e;
        }    
    } 
    
    private int transform(byte[] data) throws Exception {
        String outputString = null;
        int lineNum = 0;
        try (InputStream is = new ByteArrayInputStream(data);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is,Constants.UTF8));){
           String line;
           executorService = Executors.newFixedThreadPool(5);
           while ((line = reader.readLine()) != null) {
//               System.out.print("\nline#:" + lineNum + ": "+ line);
               if(lineNum != 0){
                   Runnable worker = new PhysicalCountExecuterService(line, runHisId);
                   executorService.execute(worker);
               }
               lineNum ++;
           }
            executorService.shutdown();
            while (!executorService.isTerminated()) {
//                System.out.println("executorService status:: " + executorService.isShutdown());
                Thread.currentThread().sleep(20000);                         
            }
            lineNum = (lineNum > 0 ? (lineNum -1) : lineNum);
//            System.out.print("Count:" + lineNum);
        }catch(Exception e){
            System.out.println(TAG + ": transform(): " + e.getLocalizedMessage());
            e.printStackTrace();
            throw e;
        }
        return lineNum;
    }
    
    public void PurgePhysicalCountData(Connection cn){
        try{
            //Get PC duration from WH360_GENERAL_STRINGS
            HashMap<String,String> pcduration = util.getPCCountDuration(cn);
            String unitOfTime = pcduration.get("UNIT_OF_TIME");
            String duration = pcduration.get("DURATION");
            System.out.println("UNIT_OF_TIME: " + unitOfTime + ", DURATION: " + duration);
            
            //get PhyInvTagListMst data
             List<PhysicalCountTagListMstBean> pcTagListMst = getPhyInvtagListMstData(cn,unitOfTime,duration);
            //Insert PhyInvTagListMst into PURGE table
            //            System.out.print("pcTagListMst size: " + pcTagListMst.size());
            if(pcTagListMst != null && pcTagListMst.size() > 0){
                
                insertIntoPhyInvtagListmstPurgeTable(cn,pcTagListMst);
            }
            //Delete from PhyInvTagListMst
            deletePhyInvTagListMstData(cn, duration, unitOfTime);
            
            // Get the data from WH360_PHY_INV_TAG_LIST of defined duration.
            List<PhysicalCountTagListBean> pcTagList = getPhyInvtagListData(cn, unitOfTime, duration);
//            System.out.print("pcTagList size: " + pcTagList.size());
            //Insert data into WH360_PHY_INV_TAG_LIST_PURGE table
            if(pcTagList != null && pcTagList.size() > 0){
                insertIntoPhyInvtagListPurgeTable(cn, pcTagList);
            }
            //Delete TAG list data from the table
            deletePhyInvTagListData(cn, duration, unitOfTime);
            
            
            
        }catch(Exception e){
            e.printStackTrace();
             
        }
    }
    
    public List<PhysicalCountTagListBean> getPhyInvtagListData(Connection cn, String unitOfTime, String duration) throws Exception {
        List<PhysicalCountTagListBean> phyInvList = new ArrayList<PhysicalCountTagListBean>();
        PreparedStatement pstmt = null;
        ResultSet result = null;

        try {
            StringBuffer selectStmt = new StringBuffer();
//            selectStmt.append("Select * From wh360_phy_inv_tag_list WHERE CREATED_DATE IS NOT NULL AND CREATED_DATE >= add_months(sysdate, -"+month+")");
            selectStmt.append("Select * from WH360_PHY_INV_TAG_LIST WHERE CREATED_DATE IS NOT NULL AND CREATED_DATE <= (SYSTIMESTAMP - INTERVAL '"+duration+"'"+ unitOfTime +") order by CREATED_DATE desc");
            pstmt = cn.prepareStatement(selectStmt.toString().toUpperCase());
            result = pstmt.executeQuery();
            if (result != null) {
                while (result.next()) {
                    PhysicalCountTagListBean bean = new PhysicalCountTagListBean();
                    bean.setTagNumber(result.getString("TAG_NUMBER"));
                    bean.setSubInventory(result.getString("SUBINVENTORY"));
                    bean.setItemNumber(result.getString("ITEM_NUMBER"));
                    bean.setLocatorId(result.getString("LOCATOR_ID"));
                    bean.setLocator(result.getString("LOCATOR"));
                    bean.setLotNumber(result.getString("LOT_NUMBER"));
                    bean.setQuantity(result.getString("QUANTITY"));
                    bean.setItemId(result.getString("ITEM_ID"));
                    bean.setItemUOM(result.getString("ITEM_UOM"));
                    bean.setOrgCode(result.getString("ORGANIZATION_CODE"));
                    bean.setOrgId(result.getString("ORGANIZATION_ID"));
                    bean.setItemDesc(result.getString("ITEM_DESCRIPTION"));
                    bean.setPhyInvName(result.getString("PHYSICAL_INVENTORY_NAME"));
                    bean.setPhyInvid(result.getString("PHYSICAL_INVENTORY_ID"));
                    bean.setTagId(result.getString("TAG_ID"));
                    bean.setAdjustmentId(result.getString("ADJUSTMENT_ID"));
                    bean.setTagUOM(result.getString("TAG_UOM"));
                    bean.setTagUOMCode(result.getString("TAG_UOM_CODE"));
                    bean.setTagTypeCode(result.getString("TAG_TYPE_CODE"));
                    bean.setKeyValue(result.getString("KEY_VALUE"));
                    bean.setUniqueSequence(result.getString("UNIQUE_SEQUENCE"));
                    bean.setCrossReference(result.getString("CROSS_REFERENCE"));
                    bean.setSerialNumber(result.getString("SERIAL_NUMBER"));
                    bean.setStatus(result.getString("STATUS"));
                    bean.setMobileQuantity(result.getString("MOBILE_QUANTITY"));
                    bean.setTagVoid(result.getString("TAG_VOID"));
                    bean.setPcReject(result.getString("PC_REJECT"));
                    bean.setPcApprove(result.getString("PC_APPROVE"));
                    bean.setPcAssignedUserName(result.getString("PC_ASSIGNED_USER_NAME"));
                    bean.setPcAssignedUserId(result.getString("PC_ASSIGNED_USER_ID"));
                    bean.setLastUpdateDate(result.getString("LAST_UPDATE_DATE"));
                    bean.setPcComment(result.getString("PC_COMMENTS"));
                    bean.setRevision(result.getString("REVISION"));
                    bean.setItemType(result.getString("ITEM_TYPE"));
                    bean.setUnitCostFD(result.getInt("UNIT_COST_FD"));
                    bean.setUnitCostCurrent(result.getInt("UNIT_COST_CURRENT"));
                    bean.setSnapshotQty(result.getInt("SNAPSHOT_QTY"));
                    bean.setCreatedDate(result.getString("CREATED_DATE"));
                    phyInvList.add(bean);
                }
                return phyInvList;
            }
            
        } catch (Exception e) {
            System.out.println(TAG + ": getPhyInvtagListData(): " + e.getLocalizedMessage());
            e.printStackTrace();
//            throw e;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                System.out.println(TAG + ": getPhyInvtagListData(): " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
        return phyInvList;
    }
    
    public void insertIntoPhyInvtagListPurgeTable(Connection cn, List<PhysicalCountTagListBean> phyInvTagList) throws Exception {
        PreparedStatement pstmt = null;
        
        try {
            StringBuffer insertStrmt = new StringBuffer();
            insertStrmt.append("INSERT INTO WH360_PHY_INV_TAG_LIST_PURGE" +
                "(TAG_NUMBER,SUBINVENTORY,ITEM_NUMBER,LOCATOR_ID,LOCATOR,LOT_NUMBER,QUANTITY,ITEM_ID,ITEM_UOM,ORGANIZATION_CODE," +
                      "ORGANIZATION_ID,ITEM_DESCRIPTION,PHYSICAL_INVENTORY_NAME,PHYSICAL_INVENTORY_ID,TAG_ID,ADJUSTMENT_ID," +
                      "TAG_UOM,TAG_UOM_CODE,TAG_TYPE_CODE,KEY_VALUE,UNIQUE_SEQUENCE,CROSS_REFERENCE,SERIAL_NUMBER,STATUS," +
                      "MOBILE_QUANTITY,TAG_VOID,PC_REJECT,PC_APPROVE,PC_ASSIGNED_USER_NAME,PC_ASSIGNED_USER_ID,LAST_UPDATE_DATE," +
                      "PC_COMMENTS,REVISION,ITEM_TYPE,UNIT_COST_FD,UNIT_COST_CURRENT,SNAPSHOT_QTY,CREATED_DATE) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pstmt = cn.prepareStatement(insertStrmt.toString().toUpperCase());
            if (phyInvTagList != null) {
                for (int i = 0; i < phyInvTagList.size(); i++) {
                    PhysicalCountTagListBean bean = phyInvTagList.get(i);
                    pstmt.setString(1, bean.getTagNumber());
                    pstmt.setString(2, bean.getSubInventory());
                    pstmt.setString(3, bean.getItemNumber());
                    pstmt.setString(4, bean.getLocatorId());
                    pstmt.setString(5, bean.getLocator());
                    pstmt.setString(6, bean.getLotNumber());
                    pstmt.setString(7, bean.getQuantity());
                    pstmt.setString(8, bean.getItemId());
                    pstmt.setString(9, bean.getItemUOM());
                    pstmt.setString(10, bean.getOrgCode());
                    pstmt.setString(11, bean.getOrgId());
                    pstmt.setString(12, bean.getItemDesc());
                    pstmt.setString(13, bean.getPhyInvName());
                    pstmt.setString(14, bean.getPhyInvid());
                    pstmt.setString(15, bean.getTagId());
                    pstmt.setString(16, bean.getAdjustmentId());
                    pstmt.setString(17, bean.getTagUOM());
                    pstmt.setString(18, bean.getTagUOMCode());
                    pstmt.setString(19, bean.getTagTypeCode());
                    pstmt.setString(20, bean.getKeyValue());
                    pstmt.setString(21, bean.getUniqueSequence());
                    pstmt.setString(22, bean.getCrossReference());
                    pstmt.setString(23, bean.getSerialNumber());
                    pstmt.setString(24, bean.getStatus());
                    pstmt.setString(25, bean.getMobileQuantity());
                    pstmt.setString(26, bean.getTagVoid());
                    pstmt.setString(27, bean.getPcReject());
                    pstmt.setString(28, bean.getPcApprove());
                    pstmt.setString(29, bean.getPcAssignedUserName());
                    pstmt.setString(30, bean.getPcAssignedUserId());
                    pstmt.setString(31, bean.getLastUpdateDate());
                    pstmt.setString(32, bean.getPcComment());
                    pstmt.setString(33, bean.getRevision());
                    pstmt.setString(34, bean.getItemType());
                    pstmt.setInt(35, bean.getUnitCostFD());
                    pstmt.setInt(36, bean.getUnitCostCurrent());
                    pstmt.setInt(37, bean.getSnapshotQty());
                    pstmt.setString(38, bean.getCreatedDate());
                    pstmt.addBatch();
                    if (i % 1000 == 0) {
                        //System.out.println("Batch "+i);
                        pstmt.executeBatch();
                    }
                }
                pstmt.executeBatch();
            }
            cn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(TAG + ": insertIntoPhyInvtagListPurgeTable(): " + e.getLocalizedMessage());
//            throw e;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
//                throw e;
            }
        }
    }
    
    public void deletePhyInvTagListData(Connection cn, String duration, String unitOfTime) throws SQLException {
        PreparedStatement pstmt = null;
        try{
            String deleteStmt = "DELETE FROM WH360_PHY_INV_TAG_LIST where CREATED_DATE IS NOT NULL AND CREATED_DATE <= (SYSTIMESTAMP - INTERVAL '"+duration+"'"+ unitOfTime +")";
            pstmt = cn.prepareStatement(deleteStmt);
            int deletedRecords = pstmt.executeUpdate();
            cn.commit();
        }catch(Exception e){
            System.out.println(TAG + ": deletePhyInvTagListData(): " + e.getLocalizedMessage());
            e.printStackTrace();
//            throw e;
        }finally {
            try {
                //ConnectionManager.releaseConnetion(cn);
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
//                throw e;
            }
        }
    }
    
    public List<PhysicalCountTagListMstBean> getPhyInvtagListMstData(Connection cn, String unitOfTime, String duration) throws Exception {
        List<PhysicalCountTagListMstBean> phyInvMstList = new ArrayList<PhysicalCountTagListMstBean>();
        PreparedStatement pstmt = null;
        ResultSet result = null;

        try {
            StringBuffer selectStmt = new StringBuffer();
            selectStmt.append("Select * from WH360_PHY_INV_TAG_LIST_MST WHERE CREATED_DATE IS NOT NULL AND CREATED_DATE <= (SYSTIMESTAMP - INTERVAL '"+duration+"'"+ unitOfTime +")");
            pstmt = cn.prepareStatement(selectStmt.toString().toUpperCase());
            result = pstmt.executeQuery();
            if (result != null) {
                while (result.next()) {
                    PhysicalCountTagListMstBean bean = new PhysicalCountTagListMstBean();
                    bean.setOrgCode(result.getString("ORGANIZATION_CODE"));
                    bean.setOrdId(result.getString("ORGANIZATION_ID"));
                    bean.setPhyInvName(result.getString("PHYSICAL_INVENTORY_NAME"));
                    bean.setPhyInvId(result.getString("PHYSICAL_INVENTORY_ID"));
                    bean.setSyncStatus(result.getString("SYNC_STATUS"));
                    bean.setStatus(result.getString("STATUS"));
                    bean.setApprovalLevel(result.getString("APPROVAL_LEVEL"));
                    bean.setFusionStatus(result.getString("FUSION_STATUS"));
                    bean.setAssignedStatus(result.getString("ASSIGNED_STATUS"));
                    bean.setAllowSubmitApproval(result.getString("ALLOW_SUBMIT_APRVL"));
                    bean.setApprovalVariance(result.getString("APPROVAL_VARIANCE"));
                    bean.setCreatedDate(result.getString("CREATED_DATE"));
                    phyInvMstList.add(bean);
                }
                return phyInvMstList;
            }
            
        } catch (Exception e) {
            System.out.println(TAG + ": getPhyInvtagListMstData(): " + e.getLocalizedMessage());
            e.printStackTrace();
//            throw e;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
//                throw e;
            }
        }
        return phyInvMstList;
    }
    
    public void insertIntoPhyInvtagListmstPurgeTable(Connection cn, List<PhysicalCountTagListMstBean> phyInvTagListMst) throws Exception {
        PreparedStatement pstmt = null;
        
        try {
            StringBuffer insertStrmt = new StringBuffer();
            insertStrmt.append("INSERT INTO WH360_PHY_INV_TAG_LIST_MST_PURGE" +
                "(ORGANIZATION_CODE,ORGANIZATION_ID,PHYSICAL_INVENTORY_NAME,PHYSICAL_INVENTORY_ID,SYNC_STATUS," +
                       "STATUS,APPROVAL_LEVEL,FUSION_STATUS,ASSIGNED_STATUS,ALLOW_SUBMIT_APRVL," +
                       "APPROVAL_VARIANCE,CREATED_DATE) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
            pstmt = cn.prepareStatement(insertStrmt.toString().toUpperCase());
            if (phyInvTagListMst != null) {
                for (int i = 0; i < phyInvTagListMst.size(); i++) {
                    PhysicalCountTagListMstBean bean = phyInvTagListMst.get(i);
                    pstmt.setString(1, bean.getOrgCode());
                    pstmt.setString(2, bean.getOrdId());
                    pstmt.setString(3, bean.getPhyInvName());
                    pstmt.setString(4, bean.getPhyInvId());
                    pstmt.setString(5, bean.getSyncStatus());
                    pstmt.setString(6, bean.getStatus());
                    pstmt.setString(7, bean.getApprovalLevel());
                    pstmt.setString(8, bean.getFusionStatus());
                    pstmt.setString(9, bean.getAssignedStatus());
                    pstmt.setString(10, bean.getAllowSubmitApproval());
                    pstmt.setString(11, bean.getApprovalVariance());
                    pstmt.setString(12, bean.getCreatedDate());
                    pstmt.addBatch();
                    if (i % 1000 == 0) {
                        //System.out.println("Batch "+i);
                        pstmt.executeBatch();
                    }
                }
                pstmt.executeBatch();
            }
            cn.commit();
        } catch (Exception e) {
            System.out.println(TAG + ": insertIntoPhyInvtagListmstPurgeTable(): " + e.getLocalizedMessage());
            e.printStackTrace();
//            throw e;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }
    
    public void deletePhyInvTagListMstData(Connection cn, String duration, String unitOfTime) throws SQLException {
        PreparedStatement pstmt = null;
        
        try{
            String deleteStmt = "DELETE FROM WH360_PHY_INV_TAG_LIST_MST where CREATED_DATE IS NOT NULL AND CREATED_DATE <= (SYSTIMESTAMP - INTERVAL '"+duration+"'"+ unitOfTime +")";
            pstmt = cn.prepareStatement(deleteStmt);
            int deletedRecords = pstmt.executeUpdate();
            cn.commit();
            
        }catch(Exception e){
            System.out.println(TAG + ": deletePhyInvTagListMstData(): " + e.getLocalizedMessage());
            e.printStackTrace();
//            throw e;
        }finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
//                throw e;
            }
        }
    }
}

