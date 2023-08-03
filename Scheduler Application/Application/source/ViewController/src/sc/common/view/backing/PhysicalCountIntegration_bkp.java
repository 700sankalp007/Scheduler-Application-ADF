package sc.common.view.backing;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.SOAPException;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import oracle.dms.http.Base64Encoder;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

import org.w3c.dom.Document;

import org.xml.sax.InputSource;

import sc.common.biReport.PublicReportService;
import sc.common.essJob.ErpIntegrationService;
import sc.common.essJob.SubmitESSJobRequest;
import sc.common.view.bean.ReportDataBean;
import sc.common.view.service.UCMService;
import sc.common.view.util.ConnectionManager;
import sc.common.view.util.Constants;
import sc.common.view.util.ExceptionLog;
import sc.common.view.util.HttpServiceBean;
import sc.common.view.util.ServiceBean;
import sc.common.view.util.SoapHandler;
import sc.common.view.util.Util;


public class PhysicalCountIntegration_bkp {
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
    Map<String,Integer> fieldMap = new HashMap<>();
    Map<String, Object> map = null;
//    PhysicalCountIntegration_bkp(){
//        try {
//            util = new Util();
//            this.con = ConnectionManager.P2TgetConnection();
//            this.setupMap = util.getSetupDetails(con, Constants.SETUP_TYPE_WEBSEVICE, Constants.SUB_TYPE_FUSION);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    
    public PhysicalCountIntegration_bkp(ReportDataBean bean, PublicReportService port, HashMap<String, String> setupMap, Connection con) {
        super();
        this.util = new Util();
        this.bean = bean;
        this.port = port;
        this.setupMap = setupMap;
//        System.out.println("setupMap :" + setupMap);
        this.con = con;
    }
    
//    public static void main(String[] arg){
//        PhysicalCountIntegration_bkp pc = new PhysicalCountIntegration_bkp();
//        try {
//    //            pc.getFilefromUCM("16_11_2021_16_43_24");
//            byte[] data = pc.getUCMFileData("16_11_2021_16_43_24");
////            byte[] data = pc.getUCMFileData("ITEM_STRUC_01092021191033");
//            
////            System.out.print("PC: data[]" + data);
////                List<Map<String, Object>> mapList = pc.transform(data);
//            String  outputString = pc.transform(data);
////            String outputString = "";
//            outputString = "60527,FG,\"ZAMIL RAC-17096436\",00,,,,HSC151-00339187,,300000086424927,Each,HSC151,300000004819523,Z.H.D.18.C.A.E.F.I.N.N.W,ConsumerFGTest_Parvez,300000184908237,520072,348217,Each,Each,1,,1,16111353,300000004761369,,\"Finished Good\",753.24225,753.24225,1,2021-11-14T13:25:23.000+00:00,66406"+
//                           "\n60528,FG,\"ZAMIL RAC-17096436\",00,,,,HSC151-00339188,,300000086424927,Each,HSC151,300000004819523,Z.H.D.18.C.A.E.F.I.N.N.W,ConsumerFGTest_Parvez,300000184908237,520073,348218,Each,Each,1,,1,16111353,300000004761369,,\"Finished Good\",753.24225,753.24225,1,2021-11-14T13:25:23.000+00:00,66406";
//            int recordProcessed = pc.insertIntoDatabase(outputString);
//            System.out.print("PC: recordProcessed" + recordProcessed);
//           
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
    
//    public static void main(String args[]){
//        Calendar now = Calendar.getInstance();
//        System.out.println("Date = "+now.get(Calendar.DAY_OF_MONTH) + "_" + (now.get(Calendar.MONTH) + 1) + "_" +
//                        now.get(Calendar.YEAR) + "_" + now.get(Calendar.HOUR_OF_DAY) + "_" + now.get(Calendar.MINUTE) +
//                        "_" + now.get(Calendar.SECOND));
//    }
    

    public double executePhysicalCountIntegration() throws Exception {
        int recordProcessed = 0;
        try{
            
            String currentDate=getcurrentDate();
            long reqId = ScheduleJob(currentDate);
            String jobStatus = getJobStatus(reqId);
//            System.out.println("PHYSICAL_COUNT: JOB_STATUS: " + jobStatus);
            if(jobStatus.equalsIgnoreCase("SUCCEEDED") || jobStatus.equalsIgnoreCase("Warning")){
//                String responseString = getFilefromUCM(currentDate);
                byte[] data = getUCMFileData(currentDate);
                String responseString = transform(data);
                recordProcessed = insertIntoDatabase(responseString);
            }
            util.updateSchedulerDate(Constants.SERVICE_TYPE_PHYSICAL_COUNT_DETAIL);
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
//            System.out.println("paramList1 : " + paramList);
            long reqId = integrationServicePort.submitESSJobRequest("/oracle/apps/ess/custom/oracle/apps/ess/custom/", "PINVJOBINV", paramList);
//            System.out.println("request ID"+reqId);
            return reqId;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    
    public String getFilefromUCM(String docName)throws Exception{
        String responseString = "";
        String outputString = "";
        BufferedReader in = null;
        InputStreamReader isr = null;
        try{
            System.out.println("PHYSICAL_COUNT: START getFilefromUCM() ");
            String wsURL = setupMap.get("HOST")+Constants.GENERIC_SOAP_WSDL;
            URL url = new URL(wsURL);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection)connection;
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            String auth = setupMap.get("USER_NAME") + ":" + setupMap.get("PASSWORD");
            byte[] encodedAuth = Base64Encoder.encode(auth).getBytes(); //.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
            String authHeaderValue = "Basic " + new String(encodedAuth);
            httpConn.setRequestProperty("Authorization", authHeaderValue);
            String xmlInput =
            " <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ucm=\"http://www.oracle.com/UCM\">\n" + 
            "   <soapenv:Header/>\n" + 
            "   <soapenv:Body>\n" + 
            "      <ucm:GenericRequest webKey=\"cs\">\n" + 
            "         <ucm:Service IdcService=\"GET_FILE\">\n" + 
            "            <ucm:Document>\n" + 
            "               <ucm:Field name=\"dDocName\">"+docName+"</ucm:Field>\n" + 
            "               <ucm:Field name=\"RevisionSelectionMethod\">Latest</ucm:Field>\n" + 
            "               <ucm:Field name=\"Rendition\">Primary</ucm:Field>\n" + 
            "            </ucm:Document>\n" + 
            "         </ucm:Service>\n" + 
            "      </ucm:GenericRequest>\n" + 
            "   </soapenv:Body>\n" + 
            "</soapenv:Envelope>";
            byte[] buffer = new byte[xmlInput.length()];
            buffer = xmlInput.getBytes();
            bout.write(buffer);
            byte[] b = bout.toByteArray();
            httpConn.setRequestProperty("Content-Length",
            String.valueOf(b.length));
            httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            OutputStream out = httpConn.getOutputStream();
            //Write the content of the request to the outputstream of the HTTP Connection.
            out.write(b);
            out.close();
            
//            isr = new InputStreamReader(httpConn.getInputStream());
//            in = new BufferedReader(isr);
//    
//            //Write the SOAP message response to a String.
//            int i=0;
//            if(in == null){
//                System.out.println("PHYSICAL_COUNT: IN NULL ");
//            }else{
//                System.out.println("PHYSICAL_COUNT: IN NOT NULL ");
//            }
            
            int count = 0;
//            responseString = in.readLine();
                           
//            System.out.println("\nPHYSICAL_COUNT: COUNT " + count);
//            System.out.println("\nPHYSICAL_COUNT: RESPONSE STRING: " + responseString);
//            while ((responseString = in.readLine()) != null) {
            
            try (Scanner scanner = new Scanner(httpConn.getInputStream())) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    outputString = outputString + line+"\n"; 
                    count ++;
                        System.out.println("\nPHYSICAL_COUNT: COUNT " + count);
                        System.out.println("\nPHYSICAL_COUNT: RESPONSE STRING: " + line);
                }
                       
            }
            
//            while (responseString != null) {
//                count ++;
////                if(i<12){
////                    i++;
////                    continue;
////                }
//            outputString = outputString + responseString+"\n"; 
//            responseString =  in.readLine();
//                System.out.println("\nPHYSICAL_COUNT: COUNT " + count);
//                System.out.println("\nPHYSICAL_COUNT: RESPONSE STRING: " + responseString);
//    
//            }
//            responseString="";
//            String strArray[]=outputString.split("\n");
//            for(i=0;i<strArray.length-2;i++){
//                responseString+=strArray[i]+"\n";
//            }  
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(in != null){
                in.close();
            }
            if(isr != null){
                isr.close();
            }
        }
//        System.out.println("PHYSICAL_COUNT: ResponseString Length" + responseString.length());
        return responseString;
    }
    
    public int insertIntoDatabase(String records)throws Exception{
        String insertSQL="INSERT INTO WH360_PHY_INV_TAG_LIST  \n" + 
                        "        (TAG_NUMBER,  \n" + 
                        "        SUBINVENTORY,  \n" + 
                        "        ITEM_NUMBER,  \n" + 
                        "        LOCATOR_ID,  \n" + 
                        "        LOCATOR,  \n" + 
                        "        LOT_NUMBER,  \n" + 
                        "        QUANTITY,  \n" + 
                        "        ITEM_ID,  \n" + 
                        "        ITEM_UOM,  \n" + 
                        "        ORGANIZATION_CODE,  \n" + 
                        "        ORGANIZATION_ID,  \n" + 
                        "        ITEM_DESCRIPTION,  \n" + 
                        "        PHYSICAL_INVENTORY_NAME,  \n" + 
                        "        PHYSICAL_INVENTORY_ID,  \n" + 
                        "        TAG_ID,  \n" + 
                        "        ADJUSTMENT_ID,  \n" + 
                        "        TAG_UOM,  \n" + 
                        "        TAG_UOM_CODE,  \n" + 
                        "        TAG_TYPE_CODE,  \n" + 
                        "        KEY_VALUE,  \n" + 
                        "        UNIQUE_SEQUENCE,  \n" + 
                        "        CROSS_REFERENCE,STATUS, SERIAL_NUMBER, REVISION, ITEM_TYPE, UNIT_COST_FD, UNIT_COST_CURRENT, SNAPSHOT_QTY, MOBILE_QUANTITY, PC_ASSIGNED_USER_ID, PC_ASSIGNED_USER_NAME, LAST_UPDATE_DATE)\n" + 
                        "        VALUES  \n" + 
                        "        (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        String updateSQL="UPDATE WH360_PHY_INV_TAG_LIST  \n" + 
                        "        SET TAG_NUMBER=?,  \n" + 
                        "        SUBINVENTORY=?,  \n" + 
                        "        ITEM_NUMBER=?,  \n" + 
                        "        LOCATOR_ID=?,  \n" + 
                        "        LOCATOR=?,  \n" + 
                        "        LOT_NUMBER=?,  \n" + 
                        "        QUANTITY=?,  \n" + 
                        "        ITEM_ID=?,  \n" + 
                        "        ITEM_UOM=?,  \n" + 
                        "        ORGANIZATION_CODE=?,  \n" + 
                        "        ORGANIZATION_ID=?,  \n" + 
                        "        ITEM_DESCRIPTION=?,  \n" + 
                        "        PHYSICAL_INVENTORY_NAME=?,  \n" + 
                        "        PHYSICAL_INVENTORY_ID=?,  \n" + 
                        "        TAG_ID=?,  \n" + 
                        "        ADJUSTMENT_ID=?,  \n" + 
                        "        TAG_UOM=?,  \n" + 
                        "        TAG_UOM_CODE=?,  \n" + 
                        "        TAG_TYPE_CODE=?,  \n" + 
                        "        KEY_VALUE=?,  \n" + 
                        "        UNIQUE_SEQUENCE=?,  \n" + 
                        "        CROSS_REFERENCE=?, \n" +
                        "        SERIAL_NUMBER=?, \n" + 
                        "        REVISION=?, \n" + 
                        "        ITEM_TYPE=?, \n" + 
                        "        UNIT_COST_FD=?, \n" + 
                        "        UNIT_COST_CURRENT=?, \n" + 
                        "        SNAPSHOT_QTY=?, \n" + 
                        "        MOBILE_QUANTITY=?, \n" + 
                        "        PC_ASSIGNED_USER_ID=?, \n" + 
                        "        STATUS=?, \n" + 
                        "        PC_ASSIGNED_USER_NAME=?, \n" +  
                        "        LAST_UPDATE_DATE=? \n" + 
                        "        WHERE TAG_ID=?";
        
        String insMstSQL="INSERT INTO WH360_PHY_INV_TAG_LIST_MST\n" + 
                         "(ORGANIZATION_CODE,\n" + 
                         "ORGANIZATION_ID,\n" + 
                         "PHYSICAL_INVENTORY_NAME,\n" + 
                         "PHYSICAL_INVENTORY_ID,\n" + 
                         "SYNC_STATUS,\n" + 
                         "STATUS)VALUES(?,?,?,?,?,?)";
        
//        masterSet = util.getExIdSet(con, selectSQL);
//        lineSet = util.getExIdSet(con, selectMstSQL);

//        System.out.println("PHYSICAL_COUNT: insertSQL: " + insertSQL.toString());
//        System.out.println("PHYSICAL_COUNT: updateSQL: " + updateSQL.toString());
//        System.out.println("PHYSICAL_COUNT: insMstSQL: " + insMstSQL.toString());
//        util = new Util();
        masterSet = util.getExIdSet(con, selectMstSQL);
        lineSet = util.getExIdSet(con, selectSQL);        
//        System.out.println("PHYSICAL_COUNT: MASTER SET SIZE(): " + masterSet.size());
//        System.out.println("PHYSICAL_COUNT: LINE SET SIZE(): " + lineSet.size());
        
//        System.out.println("PHYSICAL_COUNT: before prepare statement");
        
        try(PreparedStatement insPST=con.prepareStatement(insertSQL);
            PreparedStatement udPST=con.prepareStatement(updateSQL);
            PreparedStatement insMstPST=con.prepareStatement(insMstSQL);){
            
            String recordArray[]=records.split("\n");
            boolean flag=true;
            
            for(int i=0;i<recordArray.length;i++){
                if(flag){
                    flag=false;
                    continue;
                }
                
//                System.out.println(1+"\t"+recordArray[i]);
                String delimiter=",";
                String otherThanQuote = " [^\"] ";
                String quotedString = String.format(" \" %s* \" ", otherThanQuote);
                String regex = String.format("(?x)"+delimiter+"(?=(%s*%s)*%s*$)",otherThanQuote, quotedString, otherThanQuote);
                
                String[] data=recordArray[i].split(regex,-1);
                
//                System.out.println("PHYSICAL_COUNT: data[]: " + data.toString());
//                System.out.println("PHYSICAL_COUNT: masterSet[]: " + masterSet.toString());
                if(data.length>10){
                    if(!masterSet.contains(data[15])){
//                        System.out.println("Insert Header Records");
//                        System.out.println("PHYSICAL_COUNT: INSERT MASTER: " + data[15]);
                        insMstPST.setString(1, data[11]!=null?data[11]:null);
                        insMstPST.setString(2, data[12]!=null?data[12]:null);
                        insMstPST.setString(3, data[14]!=null?data[14].replaceAll("^\"|\"$", ""):null);
                        insMstPST.setString(4, data[15]!=null?data[15]:null);
                        insMstPST.setObject(5, null);
                        insMstPST.setObject(6, null);
                        insMstPST.addBatch();
                        masterSet.add(data[15]);
                    }
                    int userId= 0;
//                    System.out.println("PHYSICAL_COUNT: lineset[]: " + lineSet.toString());
                    if(lineSet.contains(data[16])){
//                        System.out.println("PHYSICAL_COUNT: Update Line: " + data[16]);
//                        System.out.println("Update Line Records");
                        udPST.setString(1, data[0]!=null?data[0]:null); // TAG_NUMBER
                        udPST.setString(2, data[1]!=null?data[1]:null); //SUB_INV
                        udPST.setString(3, data[2]!=null?data[2]:null); //ITEM_NUMBER
                        udPST.setString(4, data[4]!=null?data[4]:null); //LOC_ID
                        udPST.setString(5, data[5]!=null?data[5]:null); //LOCATOR
                        udPST.setString(6, data[6]!=null?data[6]:null); //LOT_NUMBER
                        udPST.setString(7, data[8]!=null?data[8]:null); //QUANTITY
                        udPST.setString(8, data[9]!=null?data[9]:null); //ITEM_ID
                        udPST.setString(9, data[10]!=null?data[10]:null); //ITEM_UOM
                        udPST.setString(10, data[11]!=null?data[11]:null); //ORGANIZATION_CODE
                        udPST.setString(11, data[12]!=null?data[12]:null); //ORGANIZATION_ID
                        udPST.setString(12, data[13]!=null?data[13].replaceAll("^\"|\"$", ""):null); //ITEM_DESCRIPTION
                        udPST.setString(13, data[14]!=null?data[14].replaceAll("^\"|\"$", ""):null); //PHYSICAL_INVENTORY_NAME
                        udPST.setString(14, data[15]!=null?data[15]:null); //PHYSICAL_INVENTORY_ID
                        udPST.setString(15, data[16]!=null?data[16]:null); //TAG_ID
                        udPST.setString(16, data[17]!=null?data[17]:null); //ADJUSTMENT_ID
                        udPST.setString(17, data[18]!=null?data[18]:null); //TAG_UOM
                        udPST.setString(18, data[19]!=null?data[19]:null); //TAG_UOM_CODE
                        udPST.setString(19, data[20]!=null?data[20]:null); //TAG_TYPE_CODE
                        udPST.setString(20, data[22]!=null?data[22]:null); //KEY
                        if(data.length>23&&data[23]!=null) //UNIQUE_SEQUENCE
                            udPST.setString(21, data[23]);
                        else
                            udPST.setObject(21, null);
                        udPST.setString(22, data[21]!=null?data[21]:null); //CROSS_REFERENCE
                        udPST.setString(34, data[16]!=null?data[16]:null); //TAG_ID unique KEY
                        udPST.setString(23, data[7]!=null?data[7]:null); //SERIAL_NUMBER
                        udPST.setString(24, data[3]!=null?data[3]:null); //REVISION
                        udPST.setString(25, data[26]!=null?data[26]:null); //ITEM_TYPE
                        udPST.setObject(26, data[27]!=null?data[27]:null); //UNIT_COST_FD
                        udPST.setObject(27, data[28]!=null?data[28]:null); //UNIT_COST_CURRENT
                        
                        if(data[20] != null && data[20].equals("3")){ //TAG_TYPE_CODE
                        udPST.setObject(28, 0); //SNAPSHOT_QTY
                            if(data[25] != null){ //COUNTED_BY
                                String userName = data[25];
                                //Get User id from db
                                userId = getUserID(userName.toUpperCase());
                                udPST.setString(29, data[8]!=null?data[8]:null); //INSERT QUANTITY from REPORT into MOBILE_QUANTITY of Database field
                                if(userId == 0){
                                    udPST.setObject(30, null); //PC_ASSIGNED_USER_ID
                                    udPST.setObject(32, null); //PC_ASSIGNED_USER_NAME
                                }else{
                                    udPST.setObject(30, userId); //PC_ASSIGNED_USER_ID
                                    udPST.setObject(32, userName.toUpperCase()); //PC_ASSIGNED_USER_NAME - COUNTED_BY
                                }
                            }else{
                                udPST.setString(29, data[8]!=null?data[8]:null); // MOBILE_QUANTITY - what about here if TAG_TYPE_CODE -3 and COUNTEDby is null?
                                udPST.setObject(30, null); //PC_ASSIGNED_USER_ID
                                udPST.setObject(32, null); //PC_ASSIGNED_USER_NAME
                            }
                            udPST.setString(31, "COUNTED"); // STATUS
                            Date lsatUpdateDate = Util.getLastUpdateDateInUTC();
                            udPST.setTimestamp(33, new Timestamp(lsatUpdateDate.getTime())); //LAST_UPDATE_DATE
                            
                        } else{
                            udPST.setObject(28, data[29]!=null?data[29]:null); //SNAPSHOT_QTY
                            udPST.setString(29, null); //MOBILE_QUANTITY
                            udPST.setObject(30, null); //PC_ASSIGNED_USER_ID
                            udPST.setString(31, "PENDING"); // STATUS
                            udPST.setObject(32, null); //PC_ASSIGNED_USER_NAME
                            udPST.setTimestamp(33, null); //LAST_UPDATE_DATE
                        }
                        
                        udPST.addBatch();
                    }else{
//                        System.out.println("Insert Line Records");
//                        System.out.println("PHYSICAL_COUNT: INSERT Line: " + data[16]);
                        insPST.setString(1, data[0]!=null?data[0]:null); // TAG_NUMBER
                        insPST.setString(2, data[1]!=null?data[1]:null); //SUB_INV
                        insPST.setString(3, data[2]!=null?data[2]:null); //ITEM_NUMBER
                        insPST.setString(4, data[4]!=null?data[4]:null); //LOC_ID
                        insPST.setString(5, data[5]!=null?data[5]:null); //LOCATOR
                        insPST.setString(6, data[6]!=null?data[6]:null); //LOT_NUMBER
                        insPST.setString(7, data[8]!=null?data[8]:null); //QUANTITY
                        insPST.setString(8, data[9]!=null?data[9]:null); //ITEM_ID
                        insPST.setString(9, data[10]!=null?data[10]:null); //ITEM_UOM
                        insPST.setString(10, data[11]!=null?data[11]:null); //ORGANIZATION_CODE
                        insPST.setString(11, data[12]!=null?data[12]:null); //ORGANIZATION_ID
                        insPST.setString(12, data[13]!=null?data[13].replaceAll("^\"|\"$", ""):null); //ITEM_DESCRIPTION
                        insPST.setString(13, data[14]!=null?data[14].replaceAll("^\"|\"$", ""):null); //PHYSICAL_INVENTORY_NAME
                        insPST.setString(14, data[15]!=null?data[15]:null); //PHYSICAL_INVENTORY_ID
                        insPST.setString(15, data[16]!=null?data[16]:null); //TAG_ID
                        insPST.setString(16, data[17]!=null?data[17]:null); //ADJUSTMENT_ID
                        insPST.setString(17, data[18]!=null?data[18]:null); //TAG_UOM
                        insPST.setString(18, data[19]!=null?data[19]:null); //TAG_UOM_CODE
                        insPST.setString(19, data[20]!=null?data[20]:null); //TAG_TYPE_CODE
                        insPST.setString(20, data[22]!=null?data[22]:null); //KEY
                        if(data.length>23&&data[23]!=null)
                            insPST.setString(21, data[23]); //UNIQUE_SEQUENCE
                        else
                            insPST.setObject(21, null);
                        insPST.setString(22, data[21]!=null?data[21]:null); //CROSS_REFERENCE
                        
//                        insPST.setString(23, "PENDING");
                        insPST.setString(24, data[7]!=null?data[7]:null); // SERIAL_NUMBER
                        insPST.setString(25, data[3]!=null?data[3]:null); // REVISION
                        insPST.setString(26, data[26]!=null?data[26]:null); // ITEM_TYPE
                        insPST.setString(27, data[27]!=null?data[27]:null); // UNIT_COST_FD 
                        insPST.setString(28, data[28]!=null?data[28]:null); // UNIT_COST_CURRENT
                        
                        if(data[20] != null && data[20].equals("3")){ //TAG_TYPE_CODE
                        insPST.setObject(29, 0); // SNAPSHOT_QTY
                            // update status as COUNTED
                            insPST.setString(23, "COUNTED"); // STATUS
                            if(data[25] != null){ //COUNTED_BY
                                String userName = data[25];
                                //Get User id from db
                                userId = getUserID(userName.toUpperCase());
                                insPST.setString(30, data[8]!=null?data[8]:null); //INSERT QUANTITY from REPORT into MOBILE_QUANTITY of Database field
                                if(userId == 0){
                                    insPST.setObject(31, null); //PC_ASSIGNED_USER_ID
                                    insPST.setObject(32, null); //PC_ASSIGNED_USER_NAME
                                    //also update status
                                }else{
                                    insPST.setObject(31, userId); //PC_ASSIGNED_USER_ID
                                    insPST.setObject(32, userName.toUpperCase()); //PC_ASSIGNED_USER_NAME - COUNTED_BY
                                }
                            }else{
                                insPST.setString(30, data[8]!=null?data[8]:null); // MOBILE_QUANTITY - what about here if TAG_TYPE_CODE-3 and COUNTEDby is null?
                                insPST.setObject(31, null); //PC_ASSIGNED_USER_ID
                                insPST.setObject(32, null); //PC_ASSIGNED_USER_NAME
                            }
                            Date lsatUpdateDate = Util.getLastUpdateDateInUTC();
                            insPST.setTimestamp(33, new Timestamp(lsatUpdateDate.getTime())); //LAST_UPDATE_DATE
                        } else{
                            insPST.setString(23, "PENDING");
                            insPST.setString(29, data[29]!=null?data[29]:null); // SNAPSHOT_QTY
                            insPST.setString(30, null); //MOBILE_QUANTITY
                            insPST.setObject(31, null); //PC_ASSIGNED_USER_ID
                            insPST.setObject(32, null); //PC_ASSIGNED_USER_NAME
                            insPST.setTimestamp(33, null); //LAST_UPDATE_DATE
                        }
                        
                        insPST.addBatch();
                        lineSet.add(data[16]);
                    }
                }
            }
            
//            System.out.println("PHYSICAL_COUNT: START BATCH INSERT");
//            System.out.println("PHYSICAL_COUNT: START BATCH INSERT");
//            System.out.println("\nPHYSICAL_COUNT: MASTER INSERT: " + masterSet.size());
            int[] hdrInsert = insMstPST.executeBatch();
//            System.out.println("\nPHYSICAL_COUNT: Execute: insMstPST.executeBatch()" + hdrInsert);
            int[] lineInsert = insPST.executeBatch();
//            System.out.println("\nPHYSICAL_COUNT: Execute: insPST.executeBatch()" + lineInsert);
            int[] lineUpdtInsert = udPST.executeBatch();
//            System.out.println("\nPHYSICAL_COUNT: Execute: udPST.executeBatch()" + lineUpdtInsert);
            
            con.commit();
//            System.out.println("\nPHYSICAL_COUNT: commit()");
            
            return hdrInsert.length + lineInsert.length + lineUpdtInsert.length;
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    
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
    
    public byte[] getUCMFileData(String UCM_ID) throws Base64DecodingException, IOException, Exception, SOAPException {
        try{
            UCMService ucmService = new UCMService();
            String url = setupMap.get("HOST") + Constants.UCM_REST_API;
            String request = util.soapMessageToString(ucmService.getFileByName(UCM_ID));
            StringBuffer response = new StringBuffer("");
            int responseCode = HttpServiceBean.restHttpPost(url, request, setupMap.get("USER_NAME")+":"+setupMap.get("PASSWORD"), Constants.HTTP_POST, HttpServiceBean.CONTENT_TYPE_XML, response, HttpURLConnection.HTTP_OK);
            if(responseCode==HttpURLConnection.HTTP_OK){
                System.out.print("response OK");
                return Base64.decode(util.getTextContentByTagName(response.toString(), "fileContent"));  
            }
            else{
                throw new RuntimeException("Unable to download file from UCM!!!");
            }
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }    
    }    
    
//    private List<Map<String, Object>> transform(byte[] data) throws Exception {
    private String transform(byte[] data) throws Exception {
        String outputString = null;
        try (InputStream is = new ByteArrayInputStream(data);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is,Constants.UTF8));){

           String line;
           int lineNum = 0;
           while ((line = reader.readLine()) != null) {
               System.out.print("\nline#:" + lineNum + ": "+ line);
               lineNum ++;
               outputString = outputString + line+"\n"; 
           }
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        return outputString;
    }
}
