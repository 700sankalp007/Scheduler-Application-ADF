package sc.common.view.util;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import java.net.HttpURLConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import java.util.regex.Matcher;

import java.util.regex.Pattern;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.share.ADFContext;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.xml.parser.v2.XMLDocument;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sc.common.biReport.ArrayOfParamNameValue;
import sc.common.biReport.ArrayOfString;
import sc.common.biReport.ParamNameValue;
import sc.common.biReport.PublicReportService;
import sc.common.biReport.ReportRequest;
import sc.common.biReport.ReportResponse;
import sc.common.essJob.ErpIntegrationService;
import sc.common.essJob.SubmitESSJobRequest;
import sc.common.view.backing.csvReport.CSVReport;
import sc.common.view.backing.xmlReport.XmlReportBean;
import sc.common.view.bean.JasperBean;
import sc.common.view.bean.LabelPrintEmailSetupBean;
import sc.common.view.bean.ReportColumnBean;
import sc.common.view.bean.ReportDataBean;
import sc.common.view.bean.ReportDataMappingBean;
import sc.common.view.bean.ReportSetupDetailBean;
import sc.common.view.mail.SendMail;
import sc.common.view.service.UCMService;

public class Util implements Constants {
    public Util() {
        super();
    }
    
    /**
     * Find an operation binding in the current binding container by name.
     *
     * @param name operation binding name
     * @return operation binding
     */
    public static OperationBinding findOperation(String name) {
        OperationBinding op = getDCBindingContainer().getOperationBinding(name);
        if (op == null) {
            throw new RuntimeException("Operation '" + name + "' not found");
        }
        return op;
    }

    public static void executeOperation (String operationName) throws Exception {
        
        OperationBinding operationBinding = getDCBindingContainer().getOperationBinding(operationName);
        if (operationBinding != null){
            
            operationBinding.execute();
            if(!operationBinding.getErrors().isEmpty())
                throw new RuntimeException("Error in Operation : "+ operationName + " : " + operationBinding.getErrors());
        }else
            throw new RuntimeException("Operation not found : "+ operationName);
    }

    /**
     * Return the Binding Container as a DCBindingContainer.
     * @return current binding container as a DCBindingContainer
     */
    public static DCBindingContainer getDCBindingContainer() {
        return (DCBindingContainer) getBindingContainer();
    }

    /**
     * Return the current page's binding container.
     * @return the current page's binding container
     */
    public static BindingContainer getBindingContainer() {
        return (BindingContainer) resolveExpression("#{bindings}");
    }

    /**
     * Method for taking a reference to a JSF binding expression and returning
     * the matching object (or creating it).
     * @param expression EL expression
     * @return Managed object
     */
    public static Object resolveExpression(String expression) {
        FacesContext facesContext = getFacesContext();
        Application app = facesContext.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        ValueExpression valueExp = elFactory.createValueExpression(elContext, expression, Object.class);
        return valueExp.getValue(elContext);
    }

    /**
     * Get FacesContext.
     * @return FacesContext
     */
    public static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }


    /**
     * Get ExternalContext
     * @return ExternalContext
     */
    public static ExternalContext getExternalContext() {
        return FacesContext.getCurrentInstance().getExternalContext();
    }


    /**
     * @Desc to sequence value
     * @param cn
     * @param sequenceName
     * @return
     * @throws Exception
     */
    public static String getSequenceValue(Connection cn, String sequenceName) throws Exception {
        
        String sql = "SELECT " + sequenceName + ".NEXTVAL FROM DUAL";        
        try(PreparedStatement pst = cn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();) {
            
            if (rs.next()) 
                return rs.getString(1);            
            return null;
        }
    }

    /**
     * @Decs get setup Detail from db
     * @return
     * @throws SQLException
     */
//    public HashMap<String,String> getSetupDetails(String setupType) throws SQLException {
//        PreparedStatement ps=null;
//        ResultSet rs=null;
//        HashMap<String,String> param=new HashMap<String,String>();
//        Connection con=null;
//        try{
//            con=ConnectionManager.getConnection();
//            ps=con.prepareStatement("SELECT 'https://ekmu-dev1.fa.em3.oraclecloud.com' HOST, ''PORT, 'Sukhgeet.singh' USER_NAME,'Welcome@123' PASSWORD FROM dual");
////            ps=con.prepareStatement("SELECT HOST,PORT,USER_NAME,PASSWORD FROM SPF_INTERFACE_SETUP  WHERE SETUP_TYPE='"+setupType+"'");
//            rs=ps.executeQuery();
//            if(rs.next()){
//                param.put("HOST",rs.getString("HOST"));
//                param.put("PORT",rs.getString("PORT"));
//                param.put("USER_NAME",rs.getString("USER_NAME"));
//                param.put("PASSWORD",rs.getString("PASSWORD"));
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//            throw e;
//        }
//        finally{
//        }if(rs!=null){
//             rs.close();
//             }
//             if(ps!=null){
//                 ps.close();
//             }if(con!=null) {
//                  ConnectionManager.releaseConnetion(con);
//              }
//        return param;
//    }
    
    public static HashMap<String, String> getSetupDetails(Connection cn, String type, String subType) throws Exception {
//            PROCEDURE GET_SETUP_DETAILS (
//                                         SETUP_TYPE_PARAM       IN     VARCHAR2,
//                                         SETUP_SUB_TYPE_PARAM   IN     VARCHAR2,
//                                         MODULE_PARAM           IN     VARCHAR2,
//                                         HOST_PARAM                OUT VARCHAR2,
//                                         PORT_PARAM                OUT VARCHAR2,
//                                         USER_NAME_PARAM           OUT VARCHAR2,
//                                         PASSWORD_PARAM            OUT VARCHAR2,
//                                         OPERATION_PARAM           OUT VARCHAR2,
//                                         SFTP_PATH_PARAM           OUT VARCHAR2,
//                                         EMAIL_ADDRESS_PARAM           OUT VARCHAR2);
//            
            CallableStatement callSt = null;
            try {
                callSt = cn.prepareCall("{ call XX_SECURITY_PKG.GET_SETUP_DETAILS(?,?,?,?,?,?,?,?,?,?) }");
                callSt.setString(1, type);
                if (subType != null) {
                    callSt.setString(2, subType);
                } else {
                    callSt.setNull(2, Types.VARCHAR);
                }
                callSt.setString(3, "COMMON");
                
                callSt.registerOutParameter(4, Types.VARCHAR);
                callSt.registerOutParameter(5, Types.VARCHAR);
                callSt.registerOutParameter(6, Types.VARCHAR);
                callSt.registerOutParameter(7, Types.VARCHAR);
                callSt.registerOutParameter(8, Types.VARCHAR);
                callSt.registerOutParameter(9, Types.VARCHAR);
                callSt.registerOutParameter(10, Types.VARCHAR);

                callSt.execute();
                HashMap<String, String> setupMap = new HashMap<String, String>();
                
                setupMap.put("HOST", callSt.getString(4));
                setupMap.put("PORT", callSt.getString(5));
                setupMap.put("USER_NAME", callSt.getString(6));
                setupMap.put("PASSWORD", callSt.getString(7));
                setupMap.put("OPERATION", callSt.getString(8));
                setupMap.put("SFTP_PATH", callSt.getString(9));
                setupMap.put("EMAIL_ADDRESS", callSt.getString(10));
                
                return setupMap;
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            } finally {
                if (callSt != null) {
                    callSt.close();
                }
            }
        }
    
    
    /**
     * @Decs checkfor scheduler wehter it is running or not
     * @param serviceType
     * @return
     * @throws SQLException
     */
    public  boolean isSchedulerRunning(String serviceType) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con=null;
        try {
            con=ConnectionManager.getConnection();
            ps = con.prepareStatement("select SCHEDULER_FLAG from XX_SCH_SCHEDULER_DATE where SERVICE_TYPE='" + serviceType + "'");
            rs = ps.executeQuery();
            if (rs != null && rs.next() && rs.getString(1) != null && rs.getString(1).equals("Y")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
                }if(con!=null){
                    ConnectionManager.releaseConnetion(con);
                }
        }
    }

    /**
     * @Des to Update Scheduler flag as Y or N
     * @param schflag
     * @param serviceType
     * @throws Exception
     */
    public  void updateSchedulerFlag(String schflag, String serviceType) throws Exception {
        Connection dbConn = null;
        PreparedStatement preparedStatement = null;
        try {
            dbConn = ConnectionManager.getConnection();
            String updateSql = "UPDATE XX_SCH_SCHEDULER_DATE SET SCHEDULER_FLAG='" + schflag + "' WHERE SERVICE_TYPE='" + serviceType + "'";

            preparedStatement = dbConn.prepareStatement(updateSql);
            preparedStatement.executeUpdate();
            dbConn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (dbConn != null)
                ConnectionManager.releaseConnetion(dbConn);
        }
    }

    /**
     * @Desc get scheduler Last run date
     * @param serviceType
     * @return
     * @throws SQLException
     */
    public String getLastUpdatedDate(String serviceType) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String date = null;
        Connection con=null;
        try {
            con=ConnectionManager.getConnection();
            ps = con.prepareStatement("SELECT SCHEDULER_DATE FROM XX_SCH_SCHEDULER_DATE WHERE SERVICE_TYPE='" + serviceType + "'");
            rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                Date dateNew = rs.getDate("SCHEDULER_DATE");
                java.util.Date utilDate = dateNew;
                date = format.format(utilDate);
            }
            return date;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
                }if(con!=null){
                    ConnectionManager.releaseConnetion(con);
                }
        }
    }

    /**
     * @Desc after successfull run update scheduler Date
     * @param serviceType
     * @throws Exception
     */
    public  void  updateSchedulerDate(String serviceType) throws Exception {
        Connection dbConn = null;
        PreparedStatement preparedStatement = null;
        try {
            dbConn = ConnectionManager.getConnection();
            String updateSql = "UPDATE XX_SCH_SCHEDULER_DATE SET SCHEDULER_DATE = SYSDATE WHERE SERVICE_TYPE= '" + serviceType + "'";
            preparedStatement = dbConn.prepareStatement(updateSql);
            preparedStatement.executeUpdate();
            dbConn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
                }
            if (dbConn != null)
                ConnectionManager.releaseConnetion(dbConn);
            }
        }
    public  void  updatePCSchedulerDate(String serviceType) throws Exception {
        Connection dbConn = null;
        PreparedStatement preparedStatement = null;
            System.out.print("PC: updateSchedulerDate" + serviceType);
        try {
            dbConn = ConnectionManager.getConnection();
            String updateSql = "UPDATE XX_SCH_SCHEDULER_DATE SET SCHEDULER_DATE = (SYSTIMESTAMP - INTERVAL '2' HOUR) WHERE SERVICE_TYPE= '" + serviceType + "'";
            preparedStatement = dbConn.prepareStatement(updateSql);
            preparedStatement.executeUpdate();
            dbConn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
                }
            if (dbConn != null)
                ConnectionManager.releaseConnetion(dbConn);
            }
        }
    
    public String excecuteQuery(String query,String commandType) throws Exception{
        
        String result=null;
        try(Connection con = ConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(query);){
                        
            if(Constants.COMMAND_SELECT.equals(commandType)){
                
                try(ResultSet rs = ps.executeQuery();){
                    
                    if(rs.next())
                        result=""+rs.getString(1);
                }
            }else if(Constants.COMMAND_DELETE.equals(commandType)){
                
                ps.executeUpdate();
                result="YES";
            }           
        }
        return result;
    }
    
    public void updateReportRunHistory(String runHistId,Connection con,String status,double Records) {
        Connection conn = ConnectionManager.getConnection();
        try(PreparedStatement ps = conn.prepareStatement("UPDATE XX_SCH_REPORT_RUN_HIS SET STATUS='"+status+"',END_TME=SYSDATE,RECORD_PROCESSED="+Records+" WHERE ID="+runHistId);){            
            System.out.print("\nPC: updateReportRunHistory() Connection:" + conn);
            System.out.print("\nPC: updateReportRunHistory() status:" + status);
            ps.executeUpdate();
            conn.commit();
        }catch(Exception e){
            
            e.printStackTrace();
            ExceptionLog.CreateExceptionLog(this.getClass().getName(), "run", e, null);
        }
        finally {
            if (conn != null){
                ConnectionManager.releaseConnetion(conn);
            }
        }
    }

    
    /**
     * @Desc use to Run BI Report
     * @param reportBean
     * @param setupMap
     * @return
     * @throws Exception
     */
    public  byte[] runBIReport(ReportDataBean reportBean,HashMap<String,String> setupMap,PublicReportService port) throws Exception {
        try {
            ArrayOfParamNameValue param = new ArrayOfParamNameValue();
            for(ReportSetupDetailBean reportParam : reportBean.getReportSetupDetail()){
                ParamNameValue paramNameValue = new ParamNameValue();
                ArrayOfString str = new ArrayOfString();
                String result=null;
                if(reportParam.getSqlStatement()!=null)
                   result = excecuteQuery(reportParam.getSqlStatement(), Constants.COMMAND_SELECT);
                
                if(result!=null)
                    str.getItem().add(result);
                else
                    str.getItem().add(reportParam.getDefualtVal());
                
                paramNameValue.setName(reportParam.getParamName());
                paramNameValue.setValues(str);
                param.getItem().add(paramNameValue);
            }
            byte[] reportData = null;
            ReportRequest req = new ReportRequest();
            String outputFormat = reportBean.getDataFormat();
            if (req != null && outputFormat != null) {
                req.setAttributeFormat(outputFormat);
                req.setAttributeLocale(reportBean.getDataLocale());
                req.setReportAbsolutePath(reportBean.getReportPath());
                req.setSizeOfDataChunkDownload(-1);
                if (param != null) {
                    req.setParameterNameValues(param);
                }
              
                updateSchedulerDate(reportBean.getServiceType());
              
                ReportResponse res = new ReportResponse();
                res = port.runReport(req,setupMap.get("USER_NAME"),setupMap.get("PASSWORD"));
                if (res != null) {
                    reportData = res.getReportBytes();
                }
            }
            
            return reportData;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
       
    public byte[] scheduleReportJob(ReportDataBean reportBean, Map<String,String> setupMap,String runHisId,String startDate,String endDate,boolean isCustom) throws Exception {
        
        Map<String, String> jobMap = this.scheduleJob(reportBean, setupMap,startDate,endDate,isCustom);  
       // if(!reportBean.getServiceType().equals(Constants.SERVICE_TYPE_CUTL_ITEM_STRUCTURE))
            updateSchedulerDate(reportBean.getServiceType());
        String jobStatus = this.getJobStatus(Long.valueOf(jobMap.get("REQUEST_ID")), setupMap);
        if(jobStatus!=null
            && (jobStatus.equalsIgnoreCase("SUCCEEDED") 
            || jobStatus.equalsIgnoreCase("Warning"))){
            
            UCMService ucmService = new UCMService();
            String url = setupMap.get("HOST") + UCM_REST_API;
            String request = this.soapMessageToString(ucmService.getFileByName(jobMap.get("UCM_ID")));
            StringBuffer response = new StringBuffer("");
            int responseCode = HttpServiceBean.restHttpPost(url, request, setupMap.get("USER_NAME")+":"+setupMap.get("PASSWORD"), HTTP_POST, HttpServiceBean.CONTENT_TYPE_XML, response, HttpURLConnection.HTTP_OK);
            if(responseCode==HttpURLConnection.HTTP_OK){
               
                updateUCMId(runHisId, jobMap.get("UCM_ID"));
                return Base64.decode(this.getTextContentByTagName(response.toString(), "fileContent"));  
            }
            else{
                throw new RuntimeException(jobMap.get("UCM_ID") + " : Unable to download file from UCM!!!");
            }
        }else if(jobStatus==null)
            throw new RuntimeException(reportBean.getReportJobName() + " : Scheduled ESS job time out!!!");
        else if(jobStatus!=null && jobStatus.equalsIgnoreCase("Canceled"))
            throw new RuntimeException(reportBean.getReportJobName() + " : Scheduled ESS job Canceled by someone !!!");
        else
            return new byte[0];        
    }
    
    public Map<String, String> scheduleJob(ReportDataBean reportBean, Map<String,String> setupMap,String startDate,String endDate,boolean isCustom) throws Exception {
        
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
        jobrequest.setJobDefinitionName(reportBean.getReportJobName());
        List<String> paramList = jobrequest.getParamList();
        
        
//        if((reportBean.getServiceType().equals(Constants.SERVICE_TYPE_CUTL_ITEM_STRUCTURE)&& !isCustom)){
//            paramList.add(Constants.ITEM_STRUCTURE+new Timestamp(System.currentTimeMillis()).getTime());
//            paramList.add(startDate);
//            paramList.add(endDate);
//        }else{
            for(ReportSetupDetailBean reportParam : reportBean.getReportSetupDetail()){
                
                String result=null;
                if(reportParam.getSqlStatement()!=null)
                   result = this.excecuteQuery(reportParam.getSqlStatement(), Constants.COMMAND_SELECT);
                
                if(result!=null)
                    paramList.add(result);
                else
                    paramList.add(reportParam.getDefualtVal());            
            }
        //}
        
        long requestId = integrationServicePort.submitESSJobRequest("/oracle/apps/ess/custom/oracle/apps/ess/custom/", reportBean.getReportJobName(), paramList);

        Map<String, String> map = new HashMap<>();
        map.put("UCM_ID", paramList.get(0));
        map.put("REQUEST_ID", String.valueOf(requestId));

        return map;
    }

  
  
    public String getJobStatus(long requestId, Map<String,String> setupMap) throws Exception {
        //long totalTime = 21600000;
        long totalTime = 36000000;
        long intervalTime = 60000;        
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
            
            jobStatus = erpPort.getESSJobStatus(requestId);
            
           if(jobStatus != null 
              && (jobStatus.equalsIgnoreCase("SUCCEEDED") 
                  || jobStatus.equalsIgnoreCase("Warning") 
                  || jobStatus.equalsIgnoreCase("Error")
                  || jobStatus.equalsIgnoreCase("Canceled")))
                break;
           else{
               
               Thread.sleep(intervalTime);
               timeMilli+=intervalTime;
           }
        }
        return jobStatus;
    }


       
    /**@Desc generic code to parse the csv report and store it into database
     * @param con
     * @param bean
     * @param port
     * @param setupMap
     * @throws Exception
     */
    public double processReport(Connection con,ReportDataBean bean,PublicReportService port,HashMap<String,String> setupMap) throws Exception{
        String dateStr = null, toDate = null;
        InputStream is = null;
        SimpleDateFormat sysDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        boolean ErrorFlag=false;
        double processedRecords=0;
        Statement stmt =null;
        
        try{ 
            stmt= con.createStatement();
//            updateSchedulerFlag(Constants.VALUE_YES, bean.getServiceName());
//            dateStr = getLastUpdatedDate(bean.getServiceName());
            updateSchedulerFlag(Constants.VALUE_YES, bean.getServiceType());
            dateStr = getLastUpdatedDate(bean.getServiceType());      
            toDate = sysDateFormat.format(cal.getTime());
            if(port!=null){
                port =ServiceBean.getPublicReportServicePort(setupMap.get("HOST"));
                BindingProvider bp = (BindingProvider) port;
                Map<String, Object> requestContext = bp.getRequestContext();
                requestContext.put(BindingProvider.USERNAME_PROPERTY,setupMap.get("USER_NAME"));
                requestContext.put(BindingProvider.PASSWORD_PROPERTY,setupMap.get("PASSWORD"));
                requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, setupMap.get("HOST") +"/xmlpserver/services/PublicReportService");
                Binding  binding= bp.getBinding();
                bp.getRequestContext().put("debugEnabled","false");
                bp.getRequestContext().put("module","LOG");
                List<Handler> handlerChain = binding.getHandlerChain();
                SoapHandler soapHandler=new SoapHandler();
                handlerChain.add(soapHandler);
                binding.setHandlerChain(handlerChain);
            }
            byte[] reportData = runBIReport(bean,setupMap,port);
            is = new ByteArrayInputStream(reportData);
            
            if (bean.getIsRefresh() != null) {
                if(Constants.VALUE_YES.equalsIgnoreCase(bean.getIsRefresh())){
                    String query=Constants.COMMAND_DELETE+" FROM " + bean.getReportDataTable();
                    excecuteQuery(query,Constants.COMMAND_DELETE);
                }
            }
            // get report columns and primary column Map //
            List<ReportDataMappingBean> reportColumnsMap =new ArrayList<ReportDataMappingBean>();
            List<ReportDataMappingBean> reportPrimaryColumnMap = new LinkedList<ReportDataMappingBean>();;
            for(ReportDataMappingBean row :bean.getReportDataMappings()){
                if(!Constants.VALUE_YES.equals(row.getIsSeq())){
                    reportColumnsMap.add(row);
                    if(row.getPrimaryFlag()!=null && Constants.VALUE_YES.equals(row.getPrimaryFlag())){
                        reportPrimaryColumnMap.add(row);
                    }
                }
            }
            bean.setReportColumnsMap(reportColumnsMap);
            bean.setReportPrimaryColumnMap(reportPrimaryColumnMap);
            // get report columns and primary column Map //
            if (reportData.length > 0 && bean.getDataFormat().equals("csv")){
                CSVReport CSV=new CSVReport();
                processedRecords=CSV.processReport(is, bean, con);
                if(!(processedRecords> 0))
                    processedRecords=-1;
            }else if(reportData.length > 0 && bean.getDataFormat().equals("xml")){
                XmlReportBean XML=new XmlReportBean();
                processedRecords= XML.processReport(is, bean,con);
                if(!(processedRecords> 0))
                    processedRecords=-1;
            }
            //updateSchedulerDate(bean.getServiceType());
//            updateSchedulerDate(bean.getServiceName());
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        return processedRecords;
    }


    /**
     * @param value
     * @param format
     * @param con
     * @return
     * @throws Exception
     */
    public java.sql.Date getDateFormat(String value,String format,Connection con)throws Exception{
        java.sql.Date date=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
            try{
                if(value==null) return date;
                ps=con.prepareStatement("SELECT TO_DATE('"+value+"','"+format+"') d from dual");
                rs=ps.executeQuery();
                if(rs.next()){
                    date=rs.getDate(1);
                }
                return date;
            }catch(Exception e){
                e.printStackTrace();
                throw e;
            }finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
                
            }
        }

    /**
     * @Desc to generate the insert and update statement of table
     * @param bean
     * @return
     */
    public Map<String,Object> generateInsertUpdateStatementforTable(ReportDataBean bean){
        Map<String,Object> dataMap=new HashMap<String,Object>();
        try{
            StringBuffer insertStatement =new StringBuffer().append(Constants.COMMAND_INSERT+" INTO "+bean.getReportDataTable());
            StringBuffer columns=new StringBuffer();
            StringBuffer values=new StringBuffer();
            HashMap<Integer,ReportDataMappingBean> insertColNameIndexMap=new HashMap<Integer,ReportDataMappingBean>();
            /* Insert Statement*/
            int minusIndex=0;
            for(int i=0;i<bean.getReportDataMappings().size();i++){
                ReportDataMappingBean obj=(ReportDataMappingBean)bean.getReportDataMappings().get(i);
                columns.append(obj.getTableColName());
                if(Constants.VALUE_YES.equals(obj.getIsSeq())){
                    values.append(obj.getDefaultValue()+".NEXTVAL");
                    minusIndex=minusIndex+1;
                }else{
                    values.append(Constants.INDEX);
                    insertColNameIndexMap.put(i+1,obj);
                    insertColNameIndexMap.put(i+1-minusIndex,obj);
                }
                if(i<(bean.getReportDataMappings().size()-1)){
                    columns.append(Constants.COMMA);
                    values.append(Constants.COMMA);
                }
            }
            /* Insert Statement*/
            /* Update Statement*/
            StringBuffer updateStatement =new StringBuffer().append(Constants.COMMAND_UPDATE+" "+bean.getReportDataTable()+" SET ");
            List<String> primaryCols=new ArrayList<String>();
            HashMap<Integer,ReportDataMappingBean> updatecolNameIndexMap=new HashMap<Integer,ReportDataMappingBean>();
            for(int i=0;i<=bean.getReportColumnsMap().size();i++){
                if(i<bean.getReportColumnsMap().size()){ 
                    ReportDataMappingBean obj=(ReportDataMappingBean)bean.getReportColumnsMap().get(i);
                    /*if(obj.getReportColName()!=null){*/
                    updateStatement.append(obj.getTableColName()+"=");
                    updateStatement.append(Constants.INDEX);
                        updatecolNameIndexMap.put(i+1,obj);
                   /* }else{
                        if(!Constants.VALUE_YES.equals(obj.getIsSeq())){
                            updateStatement.append(obj.getTableColName()+"=");
                            updateStatement.append("?");
                            updatecolNameIndexMap.put(i+1,obj);
                        }
                    }*/
                    if(Constants.VALUE_YES.equals(obj.getPrimaryFlag())){
                        primaryCols.add(obj.getTableColName());
                    }
                    if(i<bean.getReportColumnsMap().size()-1){
                        updateStatement.append(Constants.COMMA);
                    }
                }
                int index=bean.getReportColumnsMap().size();
                if(i==bean.getReportColumnsMap().size()){
                    for(int j=0;j<bean.getReportPrimaryColumnMap().size();j++){
                        index=index+1;
                        ReportDataMappingBean pc=(ReportDataMappingBean)bean.getReportPrimaryColumnMap().get(j);
                        if(j==0){
                            updateStatement.append(" WHERE ");
                            updateStatement.append(pc.getTableColName()+"=");
                            updateStatement.append(Constants.INDEX);
                            updatecolNameIndexMap.put(index,pc);
                        }else{
                            updateStatement.append(" AND ");
                            updateStatement.append(pc.getTableColName()+"=");
                            updateStatement.append(Constants.INDEX);
                            updatecolNameIndexMap.put(index,pc);
                        }
                    }
                }
            }   
            /* Update Statement*/  
            insertStatement.append(" ("+columns+") VALUES ("+values+")");
            dataMap.put("INSERT_STMT",insertStatement.toString());
            dataMap.put("UPDATE_STMT",updateStatement.toString());
            dataMap.put("INSRT_COL_INDEX_MAP",insertColNameIndexMap);
            dataMap.put("UPDATE_COL_INDEX_MAP",updatecolNameIndexMap);
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        return dataMap;
    }
    
    
    
    public void setStatementIndexValue(PreparedStatement ps,ReportDataMappingBean data,
                                       ReportColumnBean colBean,Connection con) {
        try{
            if(data.getReportSeqName()!=null){
            int colIndex=Integer.parseInt(""+data.getReportSeqName());
            if(data.getReportColName()==null ){
                colBean=new ReportColumnBean();
                colBean.setColumnValue(data.getDefaultValue());
            }else{
                
            }
            switch (data.getColumnDataType()) { 
                    case "DATE":
                        java.sql.Date date=getDateFormat(colBean.getColumnValue(),data.getDateFormat(), con);
                        ps.setDate(colIndex,date);
                        break; 
                    case "VARCHAR2": 
                        ps.setString(colIndex,colBean.getColumnValue());
                        break; 
                    case "NUMBER":
                        if(colBean.getColumnValue() == null || colBean.getColumnValue().equals(""))
                            ps.setString(colIndex,colBean.getColumnValue());
                        else{
                            ObjectConversion.convertStringToDouble(colBean.getColumnValue());
                            ps.setDouble(colIndex,ObjectConversion.convertStringToDouble(colBean.getColumnValue()));
                        }
                        break; 
                    case "DATETIME": 
                        ps.setString(colIndex,colBean.getColumnValue());
                        break; 
                    case "BLOB": 
                        ps.setString(colIndex,colBean.getColumnValue()); 
                        break; 
                    case "CLOB": 
                        ps.setString(colIndex,colBean.getColumnValue());
                        break; 
                    default: 
                        ps.setString(colIndex,colBean.getColumnValue());
                        break; 
                    } 
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * @Desc to check record is exist or not
     * @param con
     * @param bean
     * @param reportColData
     * @return
     * @throws Exception
     */
    public String checkExistingRecords(Connection con,ReportDataBean bean,
                                       HashMap<String,ReportColumnBean> reportColData) throws Exception{
        String result=null;
        ResultSet rs=null;
        PreparedStatement ps=null;
        try{
            StringBuffer existRecordCheck=new StringBuffer();
            existRecordCheck.append(Constants.COMMAND_SELECT+" DECODE(count(*),0,'N','Y') FROM "+bean.getReportDataTable());
            for(int i=0;i<bean.getReportPrimaryColumnMap().size();i++){
                ReportDataMappingBean data=(ReportDataMappingBean)bean.getReportPrimaryColumnMap().get(i);
                ReportColumnBean colBean=reportColData.get(data.getReportColName());
                if(data.getReportColName()!=null || !data.getReportColName().equals("null") || !data.getReportColName().equals(" "))
                {
                    if(i==0){
                        existRecordCheck.append(" WHERE ");
                        existRecordCheck.append(data.getTableColName()+"=?");
                    }else{
                        existRecordCheck.append(" AND ");
                        existRecordCheck.append(data.getTableColName()+"=?");
                    }
                }
            }
            //System.out.println(existRecordCheck.toString());
            ps=con.prepareStatement(existRecordCheck.toString());
            for(int i=0;i<bean.getReportPrimaryColumnMap().size();i++){
                ReportDataMappingBean data=(ReportDataMappingBean)bean.getReportPrimaryColumnMap().get(i);
                ReportColumnBean colBean=reportColData.get(data.getReportColName());
                data.setReportSeqName(Long.parseLong(""+(i+1)));
                setStatementIndexValue(ps, data, colBean, con);
            }
            rs=ps.executeQuery();
            if(rs.next()){
                result=rs.getString(1);
            }
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }finally{
            if(rs!=null){
                rs.close();
            }
            if(ps!=null){
                ps.close();
            }
        }
        return result;
    }
    
    public int processRecordToDataBase(Connection con,ReportDataBean bean,
                                       HashMap<Integer,HashMap<String,ReportColumnBean>> reportData) throws Exception{
        PreparedStatement  insert =null;
        PreparedStatement  update =null;
        int processedRecords=0;
        try{
            Map<String,Object> dataMap=generateInsertUpdateStatementforTable(bean);
            HashMap<Integer,ReportDataMappingBean> updatecolNameIndexMap =(HashMap<Integer, ReportDataMappingBean>) dataMap.get("UPDATE_COL_INDEX_MAP");
            HashMap<Integer,ReportDataMappingBean> insertColNameIndexMap =(HashMap<Integer, ReportDataMappingBean>) dataMap.get("INSRT_COL_INDEX_MAP");
//            System.out.println((String)dataMap.get("INSERT_STMT"));
//            System.out.println((String)dataMap.get("UPDATE_STMT"));
            insert=con.prepareStatement((String)dataMap.get("INSERT_STMT"));
            update=con.prepareStatement((String)dataMap.get("UPDATE_STMT"));
            for (Map.Entry<Integer,HashMap<String,ReportColumnBean>> recordEntry : reportData.entrySet()){
                HashMap<String,ReportColumnBean> reportColData=recordEntry.getValue();
                /* check existing records*/
                String isRecordExist=checkExistingRecords(con, bean, reportColData);
                /* check existing records*/
                if(Constants.VALUE_YES.equalsIgnoreCase(isRecordExist)){
                    for(Map.Entry<Integer,ReportDataMappingBean> entry : updatecolNameIndexMap.entrySet()){
                        ReportDataMappingBean data=entry.getValue();
                        data.setReportSeqName(Long.parseLong(""+entry.getKey()));
                        ReportColumnBean colBean=reportColData.get(data.getReportColName());
                       // System.out.println(data.getReportColName()+" : "+data.getReportSeqName()+" : "+colBean.getColumnValue());
                        setStatementIndexValue(update,data,colBean,con);
                    }
                    update.addBatch();
                }else{
                      for(Map.Entry<Integer,ReportDataMappingBean> entry : insertColNameIndexMap.entrySet()){
                        ReportDataMappingBean data=entry.getValue();
                        data.setReportSeqName(Long.parseLong(""+entry.getKey()));
                        ReportColumnBean colBean=reportColData.get(data.getReportColName());
                        setStatementIndexValue(insert,data,colBean,con);
                    }
                    insert.addBatch();
                  }
            }
            int[] result = insert.executeBatch();
            int [] updaterec=update.executeBatch();
//            System.out.println("The number of rows Inserted : "+ result.length);
//            System.out.println("The number of rows updated : "+ updaterec.length);
            processedRecords=result.length+updaterec.length;
            con.commit();
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        return processedRecords;
    }
    
    public byte[] CallBIReport(String reportPath, HashMap<String, ArrayList<String>> paramMap, String host,
                                  String userName, String password) throws Exception {
           try {
               byte[] binaryBytes = null;
               PublicReportService publicReportService = ServiceBean.getPublicReportServicePort(host);
               ReportRequest req = new ReportRequest();
               if (req != null) {
                   req.setAttributeFormat("xml");
                   req.setAttributeLocale("en-US");
                   req.setReportAbsolutePath(reportPath);
                   req.setSizeOfDataChunkDownload(-1); //download all
                   if (paramMap != null) {
                       Iterator it = paramMap.entrySet().iterator();
                       ArrayOfParamNameValue param = new ArrayOfParamNameValue();
                       while (it.hasNext()) {
                           Map.Entry pair = (Map.Entry) it.next();
                           ParamNameValue paramNameValue = new ParamNameValue();
                           ArrayOfString valueStr = new ArrayOfString();
                           ArrayList<String> valueList = (ArrayList<String>) pair.getValue();
                           for (int i = 0; i < valueList.size(); i++) {
                               valueStr.getItem().add(valueList.get(i));
                           }
                           paramNameValue.setName((String) pair.getKey());
                           paramNameValue.setValues(valueStr);
                           param.getItem().add(paramNameValue);
                       }
                       req.setParameterNameValues(param);
                   }

                   //get response in outputFormat using runReport
                   ReportResponse res = new ReportResponse();
                   res = publicReportService.runReport(req, userName, password);
                   if (res != null) {
                       binaryBytes = res.getReportBytes();
                   }
               }
               return binaryBytes;
           } catch (Exception e) {
               e.printStackTrace();
               throw e;
           }
       }
    
    public String getBiReportPathStrings(Connection cn, String reportName) throws Exception {
           PreparedStatement pst = null;
           ResultSet rs = null;
           try {
               String sql = "SELECT REPORT_PATH FROM EI_BIREPORT_PATH_STRINGS WHERE REPORT_NAME = ?";
               pst = cn.prepareStatement(sql);
               if (reportName != null) {
                   pst.setString(1, reportName);
               } else {
                   pst.setNull(1, Types.VARCHAR);
               }
               rs = pst.executeQuery();
               if (rs.next()) {
                   return rs.getString("REPORT_PATH");
               }
               return null;
           } catch (Exception e) {
               e.printStackTrace();
               throw e;
           } finally {
               if (rs != null) {
                   rs.close();
               }
               if (pst != null) {
                   pst.close();
               }
           }
       }
    
    

    public String getOutBoundingLastRunDate(Connection conn, String schedulerId) throws Exception {
        String lastRunDate = null;
        ResultSet rset = null;
        PreparedStatement pstmt = null;
        
        try{
            StringBuffer selectStmt = new StringBuffer();
            selectStmt.append("SELECT to_char(SCHEDULER_DATE,'MM-dd-YYYY HH24:MI:SS') FROM sc_scheduler_date WHERE SCHEDULER_ID = ?");
            pstmt = conn.prepareStatement(selectStmt.toString().toUpperCase());
            pstmt.setString(1, schedulerId);
            rset = pstmt.executeQuery();
            if (rset.next()) {
                if (rset.getString(1) != null) {
                    lastRunDate = rset.getString(1);
                }
            }
            return lastRunDate;
            
        }catch(Exception e){
            e.printStackTrace();
            throw e;
            } finally {
            try {
                if (rset != null) {
                    rset.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static String getCurrentDate() {
           try {
               SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
               String systemDate = dateFormat.format(new Date());
               return systemDate;
           } catch (Exception e) {
               e.printStackTrace();
               return null;
           }
       }
    public static String convertToUTCFormat(String date) throws Exception {
           try {
               SimpleDateFormat srcFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
               Date dateSrc = srcFormat.parse(date);
               srcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
               String destDate = srcFormat.format(dateSrc);
               return destDate;
           } catch (Exception e) {
               e.printStackTrace();
               throw e;
           }
       }
    
    public static Date getLastUpdateDateInUTC() throws Exception{
                try {
                    SimpleDateFormat formatter =new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
                    SimpleDateFormat DateFormatUTC =new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
                    DateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date currentSystemDate = formatter.parse(DateFormatUTC.format(new Date()));
                    return currentSystemDate;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
            
    
    
    public void updateOutBoundingLastRunDate(Connection conn, String schedulerId, String currDate) throws Exception {
            PreparedStatement pstmt = null;
            
            try{
                String sql =
                    "UPDATE sc_scheduler_date SET SCHEDULER_DATE = TO_DATE ('" + currDate + "'" +
                    ",'MM-dd-YYYY HH24:MI:SS') WHERE SCHEDULER_ID = ?";
                
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, schedulerId);
                pstmt.executeUpdate();
                conn.commit();
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            } finally {
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
        }

    public String[] splitLine(String line,String delimiter){
       String otherThanQuote = " [^\"] ";
       String quotedString = String.format(" \" %s* \" ", otherThanQuote);
           String regex =String.format("(?x) " + // enable comments, ignore white spaces
                                    delimiter + // match a comma
                                    "(?=                       " + // start positive look ahead
                                    "  (                       " + //   start group 1
                                    "    %s*                   " + //     match 'otherThanQuote' zero or more times
                                    "    %s                    " + //     match 'quotedString'
                                    "  )*                      " + //   end group 1 and repeat it zero or more times
                                    "  %s*                     " + //   match 'otherThanQuote'
                                    "  $                       " + // match the end of the string
                                    ")                         "
                                    , // stop positive look ahead
                                    otherThanQuote, quotedString, otherThanQuote);
           
     //   line=;
        System.out.println("after remove="+line);
        String csv[] = line.split(regex, -1); //line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        
        for(int i=0; i<csv.length; i++){
           if(!csv[i].equals("")){
               if(csv[i]!=null && csv[i].contains("\"")){
                   csv[i]=csv[i].replace("\"", "");
               }
           }else{
               csv[i] = null;
           }           
       }
     return csv;
    }

    public Set<String> getExIdSet(Connection con, String query) throws SQLException{        
        Set<String> set = new HashSet<>();
        int count = 0;
        try(PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();){           
                while(rs.next()){
                    set.add(rs.getString(1));
//                    System.out.print("set count:" + count);
//                    count++;
                }    
        }
        return set;
    }

    public SOAPMessage stringToSOAPMessage(String message) throws IOException, SOAPException {
        SOAPMessage soapMessage = null;
        
        if(message!=null){
            
            try(InputStream is = new ByteArrayInputStream(message.getBytes());){              
                soapMessage = MessageFactory.newInstance().createMessage(null, is);
            }
            
        }
        
        return soapMessage;
    }
    public Boolean hasFault(String response) throws IOException, SOAPException {      
        SOAPMessage soapMessage = stringToSOAPMessage(response);
        return soapMessage.getSOAPBody().hasFault();
    }
    
    public void updateWOItemStructure(Connection con) throws Exception {
        
        try(CallableStatement call = con.prepareCall("{CALL CUT_LOGIC.UPDATE_WO_ITEM_STRUCTURE_P}");){
            call.executeUpdate();
        }
    }


    public static List<Map<String,Object>> queryForListMap(String query) throws SQLException, Exception {
        
        try(Connection con = ConnectionManager.getConnection();){
            
            return queryForListMap(query, null, con);    
        }        
    }

    public static List<Map<String,Object>> queryForListMap(String query, Object[] placeHolders) throws Exception {

        try(Connection con = ConnectionManager.getConnection();){
            
            return queryForListMap(query, placeHolders, con);    
        }        
    }

    public static List<Map<String,Object>> queryForListMap(String query, Connection con) throws SQLException {
        
        return queryForListMap(query, null, con);
    }

    public static List<Map<String,Object>> queryForListMap(String query, Object[] placeHolders, Connection con) throws SQLException {       
        
        Map<String,Object> map = null;
        List<Map<String,Object>> list = null;
        try(PreparedStatement ps = con.prepareStatement(query);){
            
            if(placeHolders!=null)
                for(int i=1; i<=placeHolders.length; i++)
                    ps.setObject(i, placeHolders[i-1]);
            
            try(ResultSet rs = ps.executeQuery();){
                
                ResultSetMetaData rsmd = rs.getMetaData();
                while(rs.next()){
                    
                    if(list==null) list = new ArrayList<>();
                    map = new HashMap<>();
                    for(int i=1; i<=rsmd.getColumnCount(); i++)
                        map.put(rsmd.getColumnName(i), rs.getObject(i));
                    list.add(map);
                }
            }        
        }
        return list;
    }

    public static Map<String,Object> queryForMap(String query) throws SQLException, Exception {
        
        try(Connection con = ConnectionManager.getConnection();){
            
            return queryForMap(query, null, con);    
        }        
    }

    public static Map<String,Object> queryForMap(String query, Object[] placeHolders) throws SQLException, Exception {

        try(Connection con = ConnectionManager.getConnection();){
            
            return queryForMap(query, placeHolders, con);    
        }        
    }

    public static Map<String,Object> queryForMap(String query, Connection con) throws SQLException {
        
        return queryForMap(query, null, con);
    }

    public static Map<String,Object> queryForMap(String query, Object[] placeHolders, Connection con) throws SQLException {       
        
        Map<String,Object> map = null;
        try(PreparedStatement ps = con.prepareStatement(query);){
            
            if(placeHolders!=null)
                for(int i=1; i<=placeHolders.length; i++)
                    ps.setObject(i, placeHolders[i-1]);
            
            try(ResultSet rs = ps.executeQuery();){
                
                ResultSetMetaData rsmd = rs.getMetaData();
                if(rs.next()){
                    
                    map = new HashMap<>();
                    for(int i=1; i<=rsmd.getColumnCount(); i++)
                        map.put(rsmd.getColumnName(i), rs.getObject(i));
                }
            }        
        }
        return map;
    }

    public Set<String> getPrimaryColumnSet(ReportDataBean bean) throws Exception {
        
        Set<String> primaryColumnSet = null;
        for(ReportDataMappingBean row : bean.getReportDataMappings()){
            
            if(!Constants.VALUE_YES.equals(row.getIsSeq())){
                                
                if(row.getPrimaryFlag()!=null 
                   && Constants.VALUE_YES.equals(row.getPrimaryFlag())){
                    
                    if(primaryColumnSet==null) primaryColumnSet = new HashSet<>();
                    primaryColumnSet.add(row.getTableColName());
                }
            }
        }
        return primaryColumnSet;
    }
    
    public String addRunHistory(Connection con, ReportDataBean bean) throws Exception {
        
        String runHistoryId = null;                
        Map<String, Object> runHistoryMap = new HashMap<>();
        runHistoryId = Util.getSequenceValue(con, "XX_SCH_REPORT_RUN_HIST_SEQ");
        runHistoryMap.put("ID", runHistoryId);
        runHistoryMap.put("STATUS", STATUS_RUNNING);
        runHistoryMap.put("SERVICE_TYPE", bean.getServiceType());
        runHistoryMap.put("RUN_TYPE", SCHEDULE_RUN);
        runHistoryMap.put("SCHEDULER_ID", bean.getSchedulerId());        
        DML dml = new DML(con);
        dml.insertData(XX_SCH_REPORT_RUN_HIS, Arrays.asList(runHistoryMap), null);
        con.commit();
        return runHistoryId;
    }

    public static String getBlobData(Connection con, String blobCode, String tableName) throws Exception {
        
        String query = "SELECT BLOB_DATA  FROM " + tableName + " WHERE BLOB_CODE = ?";
        try(PreparedStatement ps = con.prepareStatement(query);){
            ps.setString(1, blobCode);
            try(ResultSet rs = ps.executeQuery();){
                while(rs.next())
                    return rs.getString(1);
                return null;
            }            
        }
    }

    public String soapMessageToString(SOAPMessage message) throws Exception {
        
        String result = null;
        if (message != null) {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();){
                message.writeTo(baos);
                result = baos.toString();
    }
        
        }
        return result;
    }

    public String getStackTrace(Exception e, int length) {
    
        if (e != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String error = sw.toString();
            if(error.length()>length)
                return error.substring(0,length);
            else
                return error;
        }else
            return null;
    }    
    
    public String getFaultString(String response) throws IOException, SOAPException {
        SOAPMessage soapMessage = stringToSOAPMessage(response);
        return soapMessage.getSOAPBody().getFault().getFaultString();
    }

    public String getTextContentByTagName(String response, String tag) throws IOException, SOAPException {
        
        SOAPMessage soapMessage =  stringToSOAPMessage(response);
        SOAPBody soapBody = soapMessage.getSOAPBody();
        NodeList nodes = soapBody.getElementsByTagName(tag);
        Node node = nodes.item(0);
        
        return node != null ? node.getTextContent() : null;
        
    }


    public String prettyXmlFromSOAP(SOAPMessage msg) {
        
        if (msg == null) {
            return null;
        }
        try {
            final StringWriter sw = new StringWriter();
            final Element msgDocElem = msg.getSOAPPart().getDocumentElement();
            final XMLDocument xdoc = new XMLDocument();
            xdoc.appendChild(xdoc.importNode(msgDocElem, true));
            xdoc.print(sw);
            return sw.toString();
        } catch (Exception e) {
            return "error pretty-printing XML: " + e.getMessage();
        }
    }

    public String prettyXmlFromString(String input) {
        
            if (input == null) {
                return null;
            }
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            try {
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
                    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                    transformer.transform(xmlInput, new StreamResult(stringWriter));

                    return stringWriter.toString().trim();
            } catch (Exception e) {
                    throw new RuntimeException(e);
            }
    }

    
    public static void main(String s[]) throws Exception {
        
        Util util = new Util();
       try(Connection con = ConnectionManager.getConnection()){
        //    util.updateWOItemStructure(con);   
           util.deleteFileFromUCM("ITEM_19062021071826");
        }
    }
    public String getUCMId(String reportRunHisId,Connection con){
        String ucmId=null;
        try{
            try(PreparedStatement ps=con.prepareStatement(Constants.GET_UCM_ID)){
                ps.setString(1, reportRunHisId);
                try(ResultSet rs=ps.executeQuery()){
                    if(rs.next()){
                        ucmId=rs.getString(1);
                        return ucmId; 
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public void updateUCMId(String reportRunHisId,String ucmId){
        try{
            try(Connection con = ConnectionManager.getConnection();
             
                PreparedStatement ps= con.prepareStatement(Constants.UPDATE_UCM_ID);){
                ps.setString(1, ucmId);
                ps.setString(2, reportRunHisId);
                ps.executeUpdate();
                con.commit();
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public  void deleteFileFromUCM(String ucmId){
        try{
            try(Connection con=ConnectionManager.getConnection()){
               
                HashMap<String, String> setupMap = Util.getSetupDetails(con, Constants.SETUP_TYPE_WEBSEVICE, Constants.SUB_TYPE_FUSION);
                String url=setupMap.get("HOST")+Constants.GENERIC_SOAP_WSDL;
                String request="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ucm=\"http://www.oracle.com/UCM\">\n" + 
                    "<soapenv:Header/>\n" + 
                    "<soapenv:Body>\n" + 
                    "<ucm:GenericRequest webKey=\"cs\">\n" + 
                    "<ucm:Service IdcService=\"DELETE_DOC\">\n" + 
                    "<ucm:Document>\n" + 
                    "<ucm:Field name=\"dDocName\">"+ucmId+"</ucm:Field>\n" + 
                    "</ucm:Document>\n" + 
                    "</ucm:Service>\n" + 
                    "</ucm:GenericRequest>\n" + 
                    "</soapenv:Body>\n" + 
                    "</soapenv:Envelope>";
                HttpService.soapHttpPost(url, request, setupMap.get("USER_NAME")+":"+setupMap.get("PASSWORD"));
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static int labelPrintProductionReport(String context){
        int count=0;
        List<String> emailRecList=null;
        Map<String, String> hostMap = new HashMap<>();
        Map<String,List<LabelPrintEmailSetupBean>> emailByOrg = new HashMap<>();
        
      //  String body=null;
        StringBuilder body=new StringBuilder("");
        try{
            body.append("<html>");
            body.append("<body>");
            body.append("<p>Hi,</p>");
            body.append("<p>");
            body.append("Please find attached production report for ");
            body.append(getDateOnly());
            body.append(".</p>");
            body.append("<br><p>Thanks,</p>");
            body.append("</body>");
            body.append("</html>");
            
            String fileName= Constants.LABEL_PRINT_PRODUCTION_REPORT+"_"+getDateOnly()+Constants.PDF;
            byte[] byteArray=null;
           
            try(Connection con=ConnectionManager.getConnection()){
                
                emailByOrg=getLabelPrintRecList(con);
                for (String orgCode : emailByOrg.keySet()) {
                    Map<String,Object> reportParameters=new HashMap<String,Object>();
                    reportParameters.put("OrgCode", orgCode);
                    List emails = emailByOrg.get(orgCode);
                    if(!emails.isEmpty()){
                       
                        hostMap=getEmailSetupDetails(con);
                        JasperBean jasperBean=new JasperBean(context);
                        byteArray=jasperBean.runReport(Constants.LABEL_PRINT_PRODUCTION_REPORT_PATH,reportParameters,fileName, null, Constants.CONTENT_TYPE_PDF);
                       
                        if (byteArray != null && byteArray.length > 0) {
                            count++;
                            SendMail.sendMail(byteArray, hostMap, hostMap.get("from"), emails, null, Constants.LABEL_PRINT_PRODUTION_REPORT_SUBJECT, body.toString(),fileName);
                        }             
                    }
                    
                }
               
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return count;
    }
    public static String getDateOnly(){
        Date date = new Date();
        DateFormat dateOnly = new SimpleDateFormat("dd-MMM-yyyy");
        dateOnly.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateOnly.format(date).toString();
    }
    public static Map getLabelPrintRecList(Connection con){
        List<LabelPrintEmailSetupBean> emailSetup = new ArrayList<>();
        Map<String,List> emailByOrg = new HashMap<>();
        try{
           
            final String query="SELECT EMP.EMAIL_ID,LP.ORG_CODE FROM LABEL_PRINT_DAILY_REPORT_SETUP LP JOIN EMPLOYEE_MASTER EMP  \n" + 
            "            ON LP.PERSON_ID=EMP.PERSON_ID\n" + 
            "            GROUP BY EMP.EMAIL_ID,LP.ORG_CODE";
            try(PreparedStatement ps=con.prepareStatement(query)){
                try(ResultSet rs=ps.executeQuery()){
                    while(rs.next()){
                        emailSetup.add(new LabelPrintEmailSetupBean(rs.getString("ORG_CODE"),rs.getString("EMAIL_ID")));
                    }
                }
            }
            if(!emailSetup.isEmpty()){
                for(LabelPrintEmailSetupBean e:emailSetup){
                    if(!emailByOrg.containsKey(e.getOrgCode())){
                        emailByOrg.put(e.getOrgCode(), new ArrayList<>());                
                    }
                    emailByOrg.get(e.getOrgCode()).add(e.getEmail());
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return emailByOrg;
    }
    public  List getSchFailedRecList(Connection con){
        List<String> emailList=new ArrayList<String>();
        try{
            final String query="SELECT EMAIL_ID FROM XX_SCH_REPORT_ERROR_MAIL_LIST WHERE IS_ENABLE='Y' AND SCH_NAME='scm'";
            try(PreparedStatement ps=con.prepareStatement(query)){
                try(ResultSet rs=ps.executeQuery()){
                    while(rs.next()){
                        emailList.add(rs.getString(1));
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return emailList;
    }
    public static Map<String, String> getEmailSetupDetails(Connection con) {
        Map<String, String> hostMap = new HashMap<>();
        final String query="select HOST,EMAIL_ADDRESS, USER_NAME, PASSWORD, PORT from XX_PAAS_SETUP_DTL where setup_type='MAIL' and sub_type='MAIL' AND EMAIL_ADDRESS IS NOT NULL";
        
        try {
             
             try(PreparedStatement ps=con.prepareStatement(query)){
        
                 try(ResultSet rs=ps.executeQuery()){
        
                     if (rs.next()) {
        
                         hostMap.put("HOST", rs.getString(1));
                         hostMap.put("username", rs.getString(3));
                         hostMap.put("password", rs.getString(4));
                         hostMap.put("port", rs.getString(5));
                         hostMap.put("from", rs.getString(2));
                     }
                 }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hostMap;
    }
    
    public static Map<String, String> getErrorSchPersonMail(Connection con) {
        Map<String, String> hostMap = new HashMap<>();
        final String query="select HOST,EMAIL_ADDRESS, USER_NAME, PASSWORD, PORT from XX_PAAS_SETUP_DTL where setup_type='MAIL' and sub_type='MAIL' AND EMAIL_ADDRESS IS NOT NULL";
        
        try {
             
             try(PreparedStatement ps=con.prepareStatement(query)){
        
                 try(ResultSet rs=ps.executeQuery()){
        
                     if (rs.next()) {
        
                         hostMap.put("HOST", rs.getString(1));
                         hostMap.put("username", rs.getString(3));
                         hostMap.put("password", rs.getString(4));
                         hostMap.put("port", rs.getString(5));
                         hostMap.put("from", rs.getString(2));
                     }
                 }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hostMap;
    }
    public  Map<String,Object> getFreqCustDtl(String serviceType,Connection con){
        Map<String,Object> data=new HashMap<String,Object>();
        
        try{
            String dateFormat=null;
            long frequency=0;
            long milisecond=60000;
            final String query="SELECT DATE_FORMAT,FREQUENCY_IN_MIN FROM XX_SCH_FREQUENCY_CUST WHERE SERVICE_TYPE=?";
            try(PreparedStatement ps=con.prepareStatement(query)){
                ps.setString(1, serviceType);
                try(ResultSet rs=ps.executeQuery()){
                    if(rs.next()){
                        dateFormat=rs.getString(1);
                        frequency=rs.getLong(2);
                    }
                }
                frequency=frequency*milisecond;
                data.put(Constants.DATE_FORMAT, dateFormat);
                data.put(Constants.FREQUENCY, frequency);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return data;
    }
    
    public String getSchLastRunDate(String serviceType,String dateFormat,Connection con){
        String lastRunDate=null;
        final String query="select to_char(scheduler_date,'DD-MM-YYYY HH24:MI:SS')LAST_RUN_DATE from xx_sch_scheduler_date where service_type=?";
        try{
            try(PreparedStatement ps=con.prepareStatement(query)){
                ps.setString(1, serviceType);
                try(ResultSet rs=ps.executeQuery()){
                    if(rs.next()){
                        lastRunDate=rs.getString(1);
                    }
                }
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return lastRunDate;
    }
    public  String getDateTime(String dateFormat){
        Date date = new Date();
        DateFormat dateTime = new SimpleDateFormat(dateFormat);
        dateTime.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateTime.format(date).toString();
        
    }
    public void deleteUCMFile(String runHisId,Connection con){
        String ucmId=null;
        try{
            ucmId=getUCMId(runHisId, con);
            
            if(ucmId!=null){
                    
                deleteFileFromUCM(ucmId);
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public String getCustFreqFlag(Connection con,String serviceType){
        final String query="SELECT STOP_FLAG FROM XX_SCH_FREQUENCY_CUST WHERE SERVICE_TYPE=?";
        String flag="N";
        try{
            try(PreparedStatement ps=con.prepareStatement(query)){
                ps.setString(1, serviceType);
                try(ResultSet rs=ps.executeQuery()){
                    if(rs.next()){
                        flag=rs.getString(1);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return flag;
    }
    public void updateRecordProcessHistory(double recordProcessed,boolean isError,String runHistId,Connection con,String jobName){
        try{
            if(recordProcessed>=0){
                
                updateReportRunHistory(runHistId, con,Constants.STATUS_COMPLETED,recordProcessed);
                
                if(jobName!=null){
                    deleteUCMFile(runHistId,con);
                }
            }
             else{
                 
                recordProcessed = 0;
                if(isError)
                         updateReportRunHistory(runHistId, con, Constants.STATUS_ERRORED,recordProcessed);
                else
                     updateReportRunHistory(runHistId, con, Constants.STATUS_COMPLETED,recordProcessed);
            }   
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     *Below Method is use to check  the current scheduler is scm scheduler or not
     * @param serviceType
     * @param con
     * @return
     */
    public int isSCMReport(String serviceType,Connection con){
        final String query="SELECT COUNT(*) FROM XX_SCH_SCHEDULER_SETUP WHERE APP_NAME='scm' AND SERVICE_TYPE=?";
        int count=0;
        try{
            try(PreparedStatement ps=con.prepareStatement(query)){
                ps.setString(1, serviceType);
                try(ResultSet rs=ps.executeQuery()){
                    if(rs.next()){
                        count=rs.getInt(1);    
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return count;
    }
    /**
     *Below Method is used to get Notification body from notification table.
     * @param schHisId
     * @param con
     * @return
     */
    public String getFailedETLEmailBody(String schHisId,String errorReason,Connection con){
        final String query="SELECT NOTIF_BODY FROM NOTIFICATION_BODY WHERE REQUEST_ID='1000' AND ACTION_TYPE='SCH_MAIL'";
        String notifBodyQuery=null,notificationBody=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{
            ps=con.prepareStatement(query);
            rs=ps.executeQuery();
            if(rs.next()){
                notifBodyQuery=rs.getString(1);
            }
            if(notifBodyQuery!=null){
                
                ps=null;
                rs=null;
                ps=con.prepareStatement(notifBodyQuery);
                ps.setString(1, schHisId);
                rs=ps.executeQuery();
            
                if(rs.next()){
            
                    notificationBody=rs.getString(1);
                }
                if(notificationBody!=null){
                    notificationBody=notificationBody.replace("$error$", errorReason);
                }
            }
        }catch(Exception e){;
            e.printStackTrace();
        }finally{
            try {
                if(rs!=null){
                        rs.close();
                } 
                if(ps!=null){
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return notificationBody;
    }
    
    /**
     *Below Method is used to send an email notification to concern person if etl has failed during execution.
     * @param serviceType
     * @param runHistId
     * @param con
     */
    public void sendMailForFailedScmEtl(String serviceType,String runHistId,String errorReason,Connection con){
      
        List<String> emailList=new ArrayList<String>();
        Map<String, String> hostMap = new HashMap<>();
        String mailBody=null;
        int count=0;
        try{
            hostMap=getEmailSetupDetails(con);
            if(serviceType!=null 
                && !serviceType.equals(Constants.SERVICE_TYPE_CUTL_ITEM_STRUCTURE)){
                
                count=isSCMReport(serviceType,con);  
                if(count>0){
                
                    mailBody= getFailedETLEmailBody(runHistId,errorReason,con);
                    emailList= getSchFailedRecList(con);
                    SendMail.sendMail(hostMap, hostMap.get("from"), emailList, null, Constants.REPORT_FAILED_SUBJECT,mailBody);
                }
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void putSessionScope(String key, Object value){
        
        ADFContext.getCurrent().getSessionScope().put(key, value);
    }

    public static void putPageFlowScope(String key, Object value){
        
        ADFContext.getCurrent().getPageFlowScope().put(key, value);
    }


    public static Object getSessionScope(String key){
        
        return ADFContext.getCurrent().getSessionScope().get(key);
    }

    public static Object getPageFlowScope(String key){
        
        return ADFContext.getCurrent().getPageFlowScope().get(key);
    }
    public String getGeneralStrings(Connection cn, String type) throws Exception {
           PreparedStatement pst = null;
           ResultSet rs = null;
           try {
               String sql = "SELECT HOST FROM XX_PAAS_SETUP_DTL WHERE SETUP_TYPE = ?";
               pst = cn.prepareStatement(sql);
               if (type != null) {
                   pst.setString(1, type);
               } else {
                   pst.setNull(1, Types.VARCHAR);
               }
               rs = pst.executeQuery();
               if (rs.next()) {
                   return rs.getString("HOST");
               }
               return null;
           } catch (Exception e) {
               e.printStackTrace();
               throw e;
           } finally {
               if (rs != null) {
                   rs.close();
               }
               if (pst != null) {
                   pst.close();
               }
           }
       }
    public static String queryForString(String query, Object[] placeHolders, Connection con) throws SQLException {       
        
        try(PreparedStatement ps = con.prepareStatement(query);){
            
            if(placeHolders!=null)
                for(int i=1; i<=placeHolders.length; i++)
                    ps.setObject(i, placeHolders[i-1]);
            
            try(ResultSet rs = ps.executeQuery();){
                
                if(rs.next())                    
                    return rs.getString(1);                
            }        
        }
        return null;
    }
    
    public HashMap<String,String> getPCCountDuration(Connection cn) throws Exception {
        HashMap<String, String> pcCountDurationMap = new HashMap<String, String>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT * from wh360_general_strings WHERE TYPE = 'PHYSICAL_COUNT'");
            pst = cn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                pcCountDurationMap.put(rs.getString("CODE"), rs.getString("VALUE"));
            }
            return pcCountDurationMap;
        } catch (Exception e) {
            System.out.println("Util" + ": getPCCountDuration(): " + e.getLocalizedMessage());
            e.printStackTrace();
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
        }
    } 
    
}