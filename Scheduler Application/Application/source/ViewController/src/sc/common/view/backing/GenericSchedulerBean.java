package sc.common.view.backing;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.math.BigDecimal;

import java.net.URL;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.servlet.ServletContext;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.binding.OperationBinding;

import oracle.jbo.Row;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.quartz.impl.StdSchedulerFactory;

import sc.common.biReport.PublicReportService;
import sc.common.view.bean.ReportDataBean;
import sc.common.view.bean.ReportDataMappingBean;
import sc.common.view.bean.ReportSetupDetailBean;
import sc.common.view.util.ConnectionManager;
import sc.common.view.util.Constants;
import sc.common.view.util.ExceptionLog;
import sc.common.view.util.ServiceBean;
import sc.common.view.util.SoapHandler;
import sc.common.view.util.Util;
import sc.common.view.util.startservlet.ServletStartBean;

import weblogic.net.http.HttpsURLConnection;

public class GenericSchedulerBean implements Constants {
    
    Util util=null;
    private RichPopup schedulePopUp;
    
    public GenericSchedulerBean() {
        util=new Util();
    }
    
    public void setSchedulePopUp(RichPopup schedulePopUp) {
        this.schedulePopUp = schedulePopUp;
    }

    public RichPopup getSchedulePopUp() {
        return schedulePopUp;
    }

    /**
     * @param actionEvent
     */
    public void AdHocRun(ActionEvent actionEvent) throws Exception {
        Connection con=null;
        String serviceType= null;
        String runHistId=null; 
        
        try {
            ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            FacesContext facesContext=FacesContext.getCurrentInstance();
            con=ConnectionManager.getConnection();
            actionEvent.getSource();
            DCIteratorBinding schedulerIter =Util.getDCBindingContainer().findIteratorBinding("SchedulerSetupEOVO1Iterator");
            boolean isError = false;
            if (schedulerIter != null && schedulerIter.getViewObject() != null &&
                schedulerIter.getViewObject().getCurrentRow() != null) {
                serviceType=(String)schedulerIter.getViewObject().getCurrentRow().getAttribute("ServiceType");
                if(serviceType!= null && serviceType.equals("SSHR_TICKET_BOOKING")){
                    isError = util.isSchedulerRunning(serviceType);
                    if(!isError){
                        TicketBookingBean tbean = new TicketBookingBean();
                        tbean.TicketBookingMain();
                    }
                }else{
                        isError = util.isSchedulerRunning(serviceType);
                        if (!isError) {
                            HashMap<String,String> setupMap= util.getSetupDetails(con, Constants.SETUP_TYPE_WEBSEVICE, Constants.SUB_TYPE_FUSION);
                            setupMap.put("SERVLET_CONTEXT_PATH",context.getRealPath('/'+ java.io.File.separator));
                            
                        //                    System.out.println("SERVLET_CONTEXT_PATH = "+context.getRealPath('/'+ java.io.File.separator));
                            
                            PublicReportService port =ServiceBean.getPublicReportServicePort(setupMap.get("HOST"));
                            BindingProvider bp = (BindingProvider) port;
                            Map<String, Object> requestContext = bp.getRequestContext();
                            requestContext.put(BindingProvider.USERNAME_PROPERTY,setupMap.get("USER_NAME"));
                            requestContext.put(BindingProvider.PASSWORD_PROPERTY,setupMap.get("PASSWORD"));
                            requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, setupMap.get("HOST") +"/xmlpserver/services/PublicReportService");
                            String userName =setupMap.get("USER_NAME");
                            String password =setupMap.get("PASSWORD");
                            Binding  binding= bp.getBinding();
                            bp.getRequestContext().put("debugEnabled","false");
                            bp.getRequestContext().put("module","LOG");
                            List<Handler> handlerChain = binding.getHandlerChain();
                            SoapHandler soapHandler=new SoapHandler();
                            handlerChain.add(soapHandler);
                            binding.setHandlerChain(handlerChain);
                            
                            DCIteratorBinding setupHeaderItr =Util.getDCBindingContainer().findIteratorBinding("ReportSetupHeaderEOVO1Iterator");
                            if(setupHeaderItr!=null && setupHeaderItr.getViewObject()!=null){
                                if(setupHeaderItr.getViewObject().first() != null) {
                                    Row row = setupHeaderItr.getViewObject().first();
                                    do{
                                        ReportDataBean bean = new ReportDataBean();
                                        bean.setDataFormat((String)row.getAttribute("ReportDataFormat"));
                                        bean.setDataLocale((String)row.getAttribute("ReportDataLocale"));
                                        bean.setDelimiter((String)row.getAttribute("Delimiter"));
                                        bean.setIsRefresh((String)row.getAttribute("IsRefresh"));
                                        bean.setReportDataTable((String)row.getAttribute("ReportDataTable"));
                                        bean.setReportPath((String)row.getAttribute("ReportPath"));
                                        bean.setServiceType(serviceType);
                                        bean.setRootNode((String)row.getAttribute("RootNode"));
                                        bean.setReportJobName((String) row.getAttribute("EssJobName"));
                                        if(bean.getReportPath()!=null){
                                            boolean isAvailable=port.isReportExist(bean.getReportPath(),userName, password);
                                            if(!isAvailable){
                                            throw new Exception("Invalid Report Path : "+bean.getReportPath());
                                            }
                                        }
                                        DCIteratorBinding setupDtlItr=Util.getDCBindingContainer().findIteratorBinding("ReportSetupDetailEOVO1Iterator");
                                        List<ReportSetupDetailBean> detailList=new ArrayList<ReportSetupDetailBean>();
                                        ReportSetupDetailBean setupDtlBean = null;
                                        if(setupDtlItr.getViewObject()!=null){
                                            if(setupDtlItr.getViewObject().first()!=null)
                                            {
                                                Row dtlRow = setupDtlItr.getViewObject().first();
                                                    do{
                                                        setupDtlBean = new ReportSetupDetailBean();
                                                        setupDtlBean.setSqlStatement((String)dtlRow.getAttribute("SqlStatement"));
                                                        setupDtlBean.setDefualtVal((String)dtlRow.getAttribute("DefualtVal"));
                                                        setupDtlBean.setParamName((String)dtlRow.getAttribute("ParamName"));
                                                        detailList.add(setupDtlBean);
                                                        if(setupDtlItr.getViewObject().hasNext()) {
                                                            dtlRow = setupDtlItr.getViewObject().next();
                                                        } else {
                                                            dtlRow = null;
                                                        }        
                                                    }while(dtlRow!=null);
                                                }
                                            }
                                            bean.setReportSetupDetail(detailList);
                                            DCIteratorBinding dataMappingItr=Util.getDCBindingContainer().findIteratorBinding("ReportDataMappingEOVO1Iterator");
                                            List<ReportDataMappingBean> dataMappingList=new ArrayList<ReportDataMappingBean>();
                                            ReportDataMappingBean dataMappBean = null;
                                            if(dataMappingItr.getViewObject()!=null){
                                                if(dataMappingItr.getViewObject().first()!=null)
                                                {
                                                        Row mappingRow = dataMappingItr.getViewObject().first();
                                                    do{
                                                        dataMappBean = new ReportDataMappingBean();
                                                                                                        
                                                        dataMappBean.setMapId(mappingRow.getAttribute("MapId").toString());
                                                        dataMappBean.setReportColName((String)mappingRow.getAttribute("ReportColName"));
                                                        dataMappBean.setReportSeqName(Long.valueOf((mappingRow.getAttribute("ReportSeqName")!= null ? mappingRow.getAttribute("ReportSeqName").toString() : "0")));
                                                        dataMappBean.setTableColName((String)mappingRow.getAttribute("TableColName"));
                                                        dataMappBean.setDefaultValue((String)mappingRow.getAttribute("DefaultValue"));
                                                        dataMappBean.setReportHdrId(mappingRow.getAttribute("ReportHeaderId").toString());
                                                        dataMappBean.setPrimaryFlag((String)mappingRow.getAttribute("PrimaryFlag"));
                                                        dataMappBean.setDateFormat((String)mappingRow.getAttribute("DateFormat"));
                                                        dataMappBean.setIsSeq((String)mappingRow.getAttribute("IsSeq"));
                                                        dataMappBean.setColumnDataType((String)mappingRow.getAttribute("ColumnDataType"));
                                                                                                        
                                                        dataMappingList.add(dataMappBean);
                                                        
                                                        if(dataMappingItr.getViewObject().hasNext()) {
                                                            mappingRow = dataMappingItr.getViewObject().next();
                                                        } else {
                                                            mappingRow = null;
                                                        }        
                                                    }while(mappingRow!=null);
                                                }
                                            }
                                            bean.setReportDataMappings(dataMappingList);
                                        
                                            Util.executeOperation("CreateInsertRunHistory");
                                            DCIteratorBinding runHistItr=Util.getDCBindingContainer().findIteratorBinding("ReportRunHistoryEOVO1Iterator");
                                            if(runHistItr!=null && runHistItr.getViewObject()!=null && runHistItr.getViewObject().getCurrentRow()!=null){
                                                runHistItr.getViewObject().getCurrentRow().setAttribute("Status",Constants.STATUS_RUNNING);
                                                runHistItr.getViewObject().getCurrentRow().setAttribute("ServiceType",serviceType);
                                                runHistItr.getViewObject().getCurrentRow().setAttribute("RunType",Constants.AD_HOC_RUN);
                                                //runHistItr.getViewObject().getCurrentRow().setAttribute("StartTime",new Timestamp(new java.util.Date().getTime()));
                                                runHistId=runHistItr.getViewObject().getCurrentRow().getAttribute("Id").toString();
                                            }
                                        
                                            Util.executeOperation("Commit");
                                            ExecutorService thread = Executors.newFixedThreadPool(1);
                                            Runnable worker = new GenericSchedulerJob(bean,setupMap,port,runHistId);
                                            thread.execute(worker);
                                            thread.shutdown();
                                            if(setupHeaderItr.getViewObject().hasNext()) {
                                                row = setupHeaderItr.getViewObject().next();
                                            } else {
                                                row = null;
                                            }                       
                                        }while(row!=null);
                                    }
                                }
                                
                            
                        } else{
                            ErrorMessage("scheduler is currently running, please check logs.");
                        }
                    }
//                isError = util.isSchedulerRunning(serviceType);
//                if (!isError) {
//                    HashMap<String,String> setupMap= util.getSetupDetails(con, Constants.SETUP_TYPE_WEBSEVICE, Constants.SUB_TYPE_FUSION);
//                    setupMap.put("SERVLET_CONTEXT_PATH",context.getRealPath('/'+ java.io.File.separator));
//                    
////                    System.out.println("SERVLET_CONTEXT_PATH = "+context.getRealPath('/'+ java.io.File.separator));
//                    
//                    PublicReportService port =ServiceBean.getPublicReportServicePort(setupMap.get("HOST"));
//                    BindingProvider bp = (BindingProvider) port;
//                    Map<String, Object> requestContext = bp.getRequestContext();
//                    requestContext.put(BindingProvider.USERNAME_PROPERTY,setupMap.get("USER_NAME"));
//                    requestContext.put(BindingProvider.PASSWORD_PROPERTY,setupMap.get("PASSWORD"));
//                    requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, setupMap.get("HOST") +"/xmlpserver/services/PublicReportService");
//                    String userName =setupMap.get("USER_NAME");
//                    String password =setupMap.get("PASSWORD");
//                    Binding  binding= bp.getBinding();
//                    bp.getRequestContext().put("debugEnabled","false");
//                    bp.getRequestContext().put("module","LOG");
//                    List<Handler> handlerChain = binding.getHandlerChain();
//                    SoapHandler soapHandler=new SoapHandler();
//                    handlerChain.add(soapHandler);
//                    binding.setHandlerChain(handlerChain);
//                    
//                    DCIteratorBinding setupHeaderItr =Util.getDCBindingContainer().findIteratorBinding("ReportSetupHeaderEOVO1Iterator");
//                    if(setupHeaderItr!=null && setupHeaderItr.getViewObject()!=null){
//                        if(setupHeaderItr.getViewObject().first() != null) {
//                            Row row = setupHeaderItr.getViewObject().first();
//                            do{
//                                ReportDataBean bean =new ReportDataBean();
//                                bean.setDataFormat((String)row.getAttribute("ReportDataFormat"));
//                                bean.setDataLocale((String)row.getAttribute("ReportDataLocale"));
//                                bean.setDelimiter((String)row.getAttribute("Delimiter"));
//                                bean.setIsRefresh((String)row.getAttribute("IsRefresh"));
//                                bean.setReportDataTable((String)row.getAttribute("ReportDataTable"));
//                                bean.setReportPath((String)row.getAttribute("ReportPath"));
//                                bean.setServiceType(serviceType);
//                                bean.setRootNode((String)row.getAttribute("RootNode"));
//                                if(bean.getReportPath()!=null){
//                                    boolean isAvailable=port.isReportExist(bean.getReportPath(),userName, password);
//                                    if(!isAvailable){
//                                    throw new Exception("Invalid Report Path : "+bean.getReportPath());
//                                    }
//                                }
//                                DCIteratorBinding setupDtlItr=Util.getDCBindingContainer().findIteratorBinding("ReportSetupDetailEOVO1Iterator");
//                                List<ReportSetupDetailBean> detailList=new ArrayList<ReportSetupDetailBean>();
//                                ReportSetupDetailBean setupDtlBean = null;
//                                if(setupDtlItr.getViewObject()!=null){
//                                    if(setupDtlItr.getViewObject().first()!=null)
//                                    {
//                                        Row dtlRow = setupDtlItr.getViewObject().first();
//                                            do{
//                                                setupDtlBean = new ReportSetupDetailBean();
//                                                setupDtlBean.setSqlStatement((String)dtlRow.getAttribute("SqlStatement"));
//                                                setupDtlBean.setDefualtVal((String)dtlRow.getAttribute("DefualtVal"));
//                                                setupDtlBean.setParamName((String)dtlRow.getAttribute("ParamName"));
//                                                detailList.add(setupDtlBean);
//                                                if(setupDtlItr.getViewObject().hasNext()) {
//                                                    dtlRow = setupDtlItr.getViewObject().next();
//                                                } else {
//                                                    dtlRow = null;
//                                                }        
//                                            }while(dtlRow!=null);
//                                        }
//                                    }
//                                    bean.setReportSetupDetail(detailList);
//                                    DCIteratorBinding dataMappingItr=Util.getDCBindingContainer().findIteratorBinding("ReportDataMappingEOVO1Iterator");
//                                    List<ReportDataMappingBean> dataMappingList=new ArrayList<ReportDataMappingBean>();
//                                    ReportDataMappingBean dataMappBean = null;
//                                    if(dataMappingItr.getViewObject()!=null){
//                                        if(dataMappingItr.getViewObject().first()!=null)
//                                        {
//                                                Row mappingRow = dataMappingItr.getViewObject().first();
//                                            do{
//                                                dataMappBean = new ReportDataMappingBean();
//                                                                                                
//                                                dataMappBean.setMapId(mappingRow.getAttribute("MapId").toString());
//                                                dataMappBean.setReportColName((String)mappingRow.getAttribute("ReportColName"));
//                                                dataMappBean.setReportSeqName(Long.valueOf((mappingRow.getAttribute("ReportSeqName")!= null ? mappingRow.getAttribute("ReportSeqName").toString() : "0")));
//                                                dataMappBean.setTableColName((String)mappingRow.getAttribute("TableColName"));
//                                                dataMappBean.setDefaultValue((String)mappingRow.getAttribute("DefaultValue"));
//                                                dataMappBean.setReportHdrId(mappingRow.getAttribute("ReportHeaderId").toString());
//                                                dataMappBean.setPrimaryFlag((String)mappingRow.getAttribute("PrimaryFlag"));
//                                                dataMappBean.setDateFormat((String)mappingRow.getAttribute("DateFormat"));
//                                                dataMappBean.setIsSeq((String)mappingRow.getAttribute("IsSeq"));
//                                                dataMappBean.setColumnDataType((String)mappingRow.getAttribute("ColumnDataType"));
//                                                                                                
//                                                dataMappingList.add(dataMappBean);
//                                                
//                                                if(dataMappingItr.getViewObject().hasNext()) {
//                                                    mappingRow = dataMappingItr.getViewObject().next();
//                                                } else {
//                                                    mappingRow = null;
//                                                }        
//                                            }while(mappingRow!=null);
//                                        }
//                                    }
//                                    bean.setReportDataMappings(dataMappingList);
//                                
//                                    Util.executeOperation("CreateInsertRunHistory");
//                                    DCIteratorBinding runHistItr=Util.getDCBindingContainer().findIteratorBinding("ReportRunHistoryEOVO1Iterator");
//                                    if(runHistItr!=null && runHistItr.getViewObject()!=null && runHistItr.getViewObject().getCurrentRow()!=null){
//                                        runHistItr.getViewObject().getCurrentRow().setAttribute("Status",Constants.STATUS_RUNNING);
//                                        runHistItr.getViewObject().getCurrentRow().setAttribute("ServiceType",serviceType);
//                                        runHistItr.getViewObject().getCurrentRow().setAttribute("RunType",Constants.AD_HOC_RUN);
//                                        //runHistItr.getViewObject().getCurrentRow().setAttribute("StartTime",new Timestamp(new java.util.Date().getTime()));
//                                        runHistId=runHistItr.getViewObject().getCurrentRow().getAttribute("Id").toString();
//                                    }
//                                
//                                    Util.executeOperation("Commit");
//                                    ExecutorService thread = Executors.newFixedThreadPool(1);
//                                    Runnable worker = new GenericSchedulerJob(bean,setupMap,port,runHistId);
//                                    thread.execute(worker);
//                                    thread.shutdown();
//                                    if(setupHeaderItr.getViewObject().hasNext()) {
//                                        row = setupHeaderItr.getViewObject().next();
//                                    } else {
//                                        row = null;
//                                    }                       
//                                }while(row!=null);
//                            }
//                        }
//                        
//                    
//                } else{
//                    ErrorMessage("scheduler is currently running, please check logs.");
//                }
              
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            ErrorMessage(e.getMessage());
            ExceptionLog.CreateExceptionLog(this.getClass().getName(),"AdHocRun",e,runHistId);
        } finally{
           ConnectionManager.releaseConnetion(con);
        }
    }

    public void customRun(ActionEvent actionEvent) throws Exception {
        
        Connection con=null;        
        try {
            ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            
            con=ConnectionManager.getConnection();
            actionEvent.getSource();
            DCIteratorBinding schedulerIter =Util.getDCBindingContainer().findIteratorBinding("SchedulerSetupEOVO1Iterator");
            boolean isError = false;
            if (schedulerIter != null && schedulerIter.getViewObject() != null &&
                schedulerIter.getViewObject().getCurrentRow() != null) {
                String schedulerId = schedulerIter.getViewObject().getCurrentRow().getAttribute("SchedulerId").toString();
                String serviceType = schedulerIter.getViewObject().getCurrentRow().getAttribute("ServiceType").toString();
                
                isError = util.isSchedulerRunning(serviceType);
                if (!isError) {
                    HashMap<String,String> setupMap= util.getSetupDetails(con, Constants.SETUP_TYPE_WEBSEVICE, Constants.SUB_TYPE_FUSION);                    
                    setupMap.put("SERVLET_CONTEXT_PATH", context.getRealPath('/'+ java.io.File.separator));
                    
//                        System.out.println("SERVLET_CONTEXT_PATH = "+context.getRealPath('/'+ java.io.File.separator));
                    
                    PublicReportService port =ServiceBean.getPublicReportServicePort(setupMap.get("HOST"));
                    BindingProvider bp = (BindingProvider) port;
                    Map<String, Object> requestContext = bp.getRequestContext();
                    requestContext.put(BindingProvider.USERNAME_PROPERTY,setupMap.get("USER_NAME"));
                    requestContext.put(BindingProvider.PASSWORD_PROPERTY,setupMap.get("PASSWORD"));
                    requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, setupMap.get("HOST") +"/xmlpserver/services/PublicReportService");
                    String userName =setupMap.get("USER_NAME");
                    String password =setupMap.get("PASSWORD");
                    Binding  binding= bp.getBinding();
                    bp.getRequestContext().put("debugEnabled","false");
                    bp.getRequestContext().put("module","LOG");
                    List<Handler> handlerChain = binding.getHandlerChain();
                    SoapHandler soapHandler=new SoapHandler();
                    handlerChain.add(soapHandler);
                    binding.setHandlerChain(handlerChain);
                    
                    DCIteratorBinding setupHeaderItr =Util.getDCBindingContainer().findIteratorBinding("ReportSetupHeaderEOVO1Iterator");
                    if(setupHeaderItr!=null && setupHeaderItr.getViewObject()!=null){
                        if(setupHeaderItr.getViewObject().first() != null) {
                            Row row = setupHeaderItr.getViewObject().first();
                            do{
                                ReportDataBean bean = new ReportDataBean();
                                bean.setDataFormat((String)row.getAttribute("ReportDataFormat"));
                                bean.setDataLocale((String)row.getAttribute("ReportDataLocale"));
                                bean.setDelimiter((String)row.getAttribute("Delimiter"));
                                bean.setIsRefresh((String)row.getAttribute("IsRefresh"));
                                bean.setReportDataTable((String)row.getAttribute("ReportDataTable"));
                                bean.setReportPath((String)row.getAttribute("ReportPath"));
                                bean.setServiceType(serviceType);
                                bean.setSchedulerId(schedulerId);
                                bean.setRootNode((String)row.getAttribute("RootNode"));
                                bean.setReportJobName((String) row.getAttribute("EssJobName"));
                                if(bean.getReportPath()!=null){
                                    boolean isAvailable=port.isReportExist(bean.getReportPath(),userName, password);
                                    if(!isAvailable){
                                    throw new Exception("Invalid Report Path : "+bean.getReportPath());
                                    }
                                }
                                DCIteratorBinding setupDtlItr=Util.getDCBindingContainer().findIteratorBinding("ReportSetupDetailEOVO1Iterator");
                                List<ReportSetupDetailBean> detailList=new ArrayList<ReportSetupDetailBean>();
                                ReportSetupDetailBean setupDtlBean = null;
                                if(setupDtlItr.getViewObject()!=null){
                                    if(setupDtlItr.getViewObject().first()!=null)
                                    {
                                        Row dtlRow = setupDtlItr.getViewObject().first();
                                            do{
                                                setupDtlBean = new ReportSetupDetailBean();
                                                setupDtlBean.setSqlStatement((String)dtlRow.getAttribute("SqlStatement"));
                                                setupDtlBean.setDefualtVal((String)dtlRow.getAttribute("DefualtVal"));
                                                setupDtlBean.setParamName((String)dtlRow.getAttribute("ParamName"));
                                                detailList.add(setupDtlBean);
                                                if(setupDtlItr.getViewObject().hasNext()) {
                                                    dtlRow = setupDtlItr.getViewObject().next();
                                                } else {
                                                    dtlRow = null;
                                                }        
                                            }while(dtlRow!=null);
                                        }
                                    }
                                    bean.setReportSetupDetail(detailList);
                                    DCIteratorBinding dataMappingItr=Util.getDCBindingContainer().findIteratorBinding("ReportDataMappingEOVO1Iterator");
                                    List<ReportDataMappingBean> dataMappingList=new ArrayList<ReportDataMappingBean>();
                                    ReportDataMappingBean dataMappBean = null;
                                    if(dataMappingItr.getViewObject()!=null){
                                        if(dataMappingItr.getViewObject().first()!=null)
                                        {
                                                Row mappingRow = dataMappingItr.getViewObject().first();
                                            do{
                                                dataMappBean = new ReportDataMappingBean();
                                                                                                
                                                dataMappBean.setMapId(mappingRow.getAttribute("MapId").toString());
                                                dataMappBean.setReportColName((String)mappingRow.getAttribute("ReportColName"));
                                                dataMappBean.setReportSeqName(Long.valueOf((mappingRow.getAttribute("ReportSeqName")!= null ? mappingRow.getAttribute("ReportSeqName").toString() : "0")));
                                                dataMappBean.setTableColName((String)mappingRow.getAttribute("TableColName"));
                                                dataMappBean.setDefaultValue((String)mappingRow.getAttribute("DefaultValue"));
                                                dataMappBean.setReportHdrId(mappingRow.getAttribute("ReportHeaderId").toString());
                                                dataMappBean.setPrimaryFlag((String)mappingRow.getAttribute("PrimaryFlag"));
                                                dataMappBean.setDateFormat((String)mappingRow.getAttribute("DateFormat"));
                                                dataMappBean.setIsSeq((String)mappingRow.getAttribute("IsSeq"));
                                                dataMappBean.setColumnDataType((String)mappingRow.getAttribute("ColumnDataType"));
                                                                                                
                                                dataMappingList.add(dataMappBean);
                                                
                                                if(dataMappingItr.getViewObject().hasNext()) {
                                                    mappingRow = dataMappingItr.getViewObject().next();
                                                } else {
                                                    mappingRow = null;
                                                }        
                                            }while(mappingRow!=null);
                                        }
                                    }
                                    bean.setReportDataMappings(dataMappingList);
                                
                                
                                    ExecutorService es = Executors.newFixedThreadPool(2);
                                    es.execute(() -> new CustomSchedulerBean(bean, port, setupMap).runCustomReport());
                                    es.shutdown();
                                
                                    if(setupHeaderItr.getViewObject().hasNext()) {
                                        row = setupHeaderItr.getViewObject().next();
                                    } else {
                                        row = null;
                                    }                       
                                }while(row!=null);
                            }
                        }                                            
                } else {
                    ErrorMessage("scheduler is currently running, please check logs.");
                }              
            }            
        } catch (Exception e) {
            
            e.printStackTrace();
            ErrorMessage(e.getMessage());
            ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), e, null);
        } finally{
           
            ConnectionManager.releaseConnetion(con);
        }
    }
    
    private static ArrayList<String> httpPost(String destUrl, String postData) throws Exception {
           URL url = null;
           HttpsURLConnection conn = null;
           //        SOAPHttpsURLConnection conn = null;
           InputStream in = null;
           InputStreamReader iReader = null;
           BufferedReader bReader = null;
           ArrayList<String> responseList = new ArrayList<String>();
           String response = "";
           try {
               url = new URL(destUrl);
               conn = (HttpsURLConnection) url.openConnection();
               //            conn = (SOAPHttpsURLConnection) url.openConnection();
               if (conn == null) {
                   return null;
               }
               conn.setRequestProperty("Content-Type", "application/json");
               conn.setDoOutput(true);
               conn.setDoInput(true);
               conn.setUseCaches(false);
               conn.setFollowRedirects(true);
               conn.setAllowUserInteraction(false);
               conn.setRequestMethod("POST");


               int statusCode = conn.getResponseCode();
//               System.out.println("connection status: " + conn.getResponseCode() + "; connection response: " +
//                                  conn.getResponseMessage());
               if (statusCode != 200) {
                   in = conn.getErrorStream();
               } else {
                   in = conn.getInputStream();
               }

               iReader = new InputStreamReader(in);
               bReader = new BufferedReader(iReader);

               String line;
               while ((line = bReader.readLine()) != null) {
//                   System.out.println(line);
                   response += line;
               }
           } catch (IOException e) {
               throw e;
           } catch (Exception e) {
               throw e;
           } finally {
               try {
                   if (iReader != null)
                       iReader.close();
                   if (bReader != null)
                       bReader.close();
                   if (in != null)
                       in.close();
                   if (conn != null)
                       conn.disconnect();
               } catch (IOException e) {
                   throw e;
               }
           }
           responseList.add(response);
           return responseList;
       }

    /**
     * @decs to get error message
     * @param Message
     */
    public void ErrorMessage(String Message) {
        try {
            FacesMessage msg = new FacesMessage(Message);
            msg.setSeverity(FacesMessage.SEVERITY_FATAL);
            FacesContext fctx = FacesContext.getCurrentInstance();
            ExtendedRenderKitService service = org.apache.myfaces.trinidad.util.Service.getRenderKitService(fctx, ExtendedRenderKitService.class);
            service.addScript(fctx, "var popup = AdfPage.PAGE.findComponent('d1_msgDlg');");
            fctx.addMessage(null, msg);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    
    public void ScheduleReport() {
        
        BigDecimal hourValue = null, minuteValue = null, secondValue = null;
        String serviceType = null, schedulerType = null, serviceName = null;
        String schedulerId=null;
        boolean isError = false;        
        try (Connection con = ConnectionManager.getConnection();){
            ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();            
            DCIteratorBinding schedulerIter = Util.getDCBindingContainer().findIteratorBinding("SchedulerSetupEOVO1Iterator");
            if (null != schedulerIter && null != schedulerIter.getViewObject() && null != schedulerIter.getViewObject().getCurrentRow()) {
                if (schedulerIter.getViewObject().getCurrentRow().getAttribute("ServiceName") != null) {
                    serviceName = schedulerIter.getViewObject().getCurrentRow().getAttribute("ServiceName").toString();
                    schedulerId=schedulerIter.getViewObject().getCurrentRow().getAttribute("SchedulerId").toString();
                }
                if (schedulerIter.getViewObject().getCurrentRow().getAttribute("ServiceType") != null) {
                    serviceType = schedulerIter.getViewObject().getCurrentRow().getAttribute("ServiceType").toString();
                    isError = util.isSchedulerRunning(serviceType);
                }
                if (!isError) {
                    if (schedulerIter.getViewObject().getCurrentRow().getAttribute("SchedulerType") != null) {
                        schedulerType =(String)schedulerIter.getViewObject().getCurrentRow().getAttribute("SchedulerType");
                    }
                    if (schedulerIter.getViewObject().getCurrentRow().getAttribute("Hour") != null) {
                        hourValue = (BigDecimal) schedulerIter.getViewObject().getCurrentRow().getAttribute("Hour");
                    }
                    if (schedulerIter.getViewObject().getCurrentRow().getAttribute("Minute") != null) {
                        minuteValue = (BigDecimal) schedulerIter.getViewObject().getCurrentRow().getAttribute("Minute");
                    }
                    if (schedulerIter.getViewObject().getCurrentRow().getAttribute("Second") != null) {
                        secondValue = (BigDecimal) schedulerIter.getViewObject().getCurrentRow().getAttribute("Second");
                    }
                    schedulerIter.getViewObject().getCurrentRow().setAttribute("ScheduleFlag",Constants.VALUE_YES);
                    long daySeconds = 0;
                    OperationBinding ob = Util.findOperation("Commit");
                    ob.execute();
                    if(serviceType!= null 
                       && serviceType.equals("SSHR_TICKET_BOOKING")){
                        
                        JobKey jobKey = new JobKey(SCHEDULER_JOB_PREFIX + schedulerId, SCHEDULER_JOBGROUP_PREFIX + schedulerId);
                        JobDetail jobDetail = setSchedulerClass(jobKey,schedulerId,TicketBookingBean.class);
                        Trigger trigger = null;
                        if(schedulerType != null 
                           && schedulerType.equals(Constants.SCH_TYPE_TIME)) {
                            
                            if(secondValue.compareTo(new BigDecimal(60))==0) secondValue = BigDecimal.ZERO;
                            if(minuteValue.compareTo(new BigDecimal(60))==0) minuteValue = BigDecimal.ZERO;
                            if(hourValue.compareTo(new BigDecimal(24))==0) hourValue = BigDecimal.ZERO;                            
                            String format = secondValue + " " + minuteValue + " " + hourValue + " * * ?";
                            trigger = TriggerBuilder.newTrigger().withIdentity(SCHEDULER_TRIGGER_PREFIX + schedulerId, SCHEDULER_JOBGROUP_PREFIX + schedulerId).withSchedule(CronScheduleBuilder.cronSchedule(format)).build();                
                        } else if(schedulerType != null 
                                  && schedulerType.equals(Constants.SCH_TYPE_FREQUENCY)){
                            
                            int duration =(secondValue.intValue() + minuteValue.intValue() * 60 +hourValue.intValue() * 3600);
                            trigger = TriggerBuilder.newTrigger().withIdentity(SCHEDULER_TRIGGER_PREFIX + schedulerId, SCHEDULER_JOBGROUP_PREFIX + schedulerId).withSchedule(
                            SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(new Integer(duration)).repeatForever()).build();             
                        }
                        /* Scheduler Bug Fix start code */
                        Scheduler scheduler = new StdSchedulerFactory().getScheduler();           
                       // Scheduler scheduler = ((StdSchedulerFactory) ServletStartBean.servletContext.getAttribute(QuartzInitializerListener.QUARTZ_FACTORY_KEY)).getScheduler();
                        /* End of code */
                        scheduler.start();                      
                        scheduler.scheduleJob(jobDetail, trigger); 
                }else{
                        
                    HashMap<String,String> setupMap = Util.getSetupDetails(con, Constants.SETUP_TYPE_WEBSEVICE, Constants.SUB_TYPE_FUSION);
                    setupMap.put("SERVLET_CONTEXT_PATH", context.getRealPath('/'+ java.io.File.separator));  
                    PublicReportService port =ServiceBean.getPublicReportServicePort(setupMap.get("HOST"));
                    BindingProvider bp = (BindingProvider) port;
                    Map<String, Object> requestContext = bp.getRequestContext();
                    requestContext.put(BindingProvider.USERNAME_PROPERTY,setupMap.get("USER_NAME"));
                    requestContext.put(BindingProvider.PASSWORD_PROPERTY,setupMap.get("PASSWORD"));
                    requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, setupMap.get("HOST") +"/xmlpserver/services/PublicReportService");
                    String userName = setupMap.get("USER_NAME");
                    String password = setupMap.get("PASSWORD");
                    Binding  binding= bp.getBinding();
                    bp.getRequestContext().put("debugEnabled","false");
                    bp.getRequestContext().put("module","LOG");
                    List<Handler> handlerChain = binding.getHandlerChain();
                    SoapHandler soapHandler = new SoapHandler();
                    handlerChain.add(soapHandler);
                    binding.setHandlerChain(handlerChain);
                    DCIteratorBinding setupHeaderItr = Util.getDCBindingContainer().findIteratorBinding("ReportSetupHeaderEOVO1Iterator");
                    ReportDataBean bean = null;
                    if(setupHeaderItr != null 
                        && setupHeaderItr.getViewObject() != null){
                        if(setupHeaderItr.getViewObject().first() != null) {
                            Row row = setupHeaderItr.getViewObject().first();
                           
                            do{
                                bean = new ReportDataBean();
                                bean.setDataFormat((String)row.getAttribute("ReportDataFormat"));
                                bean.setDataLocale((String)row.getAttribute("ReportDataLocale"));
                                bean.setDelimiter((String)row.getAttribute("Delimiter"));
                                bean.setIsRefresh((String)row.getAttribute("IsRefresh"));
                                bean.setReportDataTable((String)row.getAttribute("ReportDataTable"));
                                bean.setReportPath((String)row.getAttribute("ReportPath"));
                                bean.setServiceType(serviceType);
                                bean.setSchedulerId(schedulerId);
                                bean.setRootNode((String)row.getAttribute("RootNode"));
                                bean.setReportJobName((String) row.getAttribute("EssJobName"));
                                if(bean.getReportPath()!=null){
                                   boolean isAvailable=port.isReportExist(bean.getReportPath(),userName, password);
                                    if(!isAvailable){
                                        throw new Exception("Invalid Report Path : "+bean.getReportPath());
                                    }
                                }
                                DCIteratorBinding setupDtlItr=Util.getDCBindingContainer().findIteratorBinding("ReportSetupDetailEOVO1Iterator");
                                List<ReportSetupDetailBean> detailList=new ArrayList<ReportSetupDetailBean>();
                                ReportSetupDetailBean setupDtlBean = null;
                                if(setupDtlItr.getViewObject()!=null){
                                    if(setupDtlItr.getViewObject().first()!=null)
                                    {
                                        Row dtlRow = setupDtlItr.getViewObject().first();
                                        do{
                                            
                                            setupDtlBean = new ReportSetupDetailBean();
                                            
                                            setupDtlBean.setSqlStatement((String)dtlRow.getAttribute("SqlStatement"));
                                            setupDtlBean.setDefualtVal((String)dtlRow.getAttribute("DefualtVal"));
                                            setupDtlBean.setParamName((String)dtlRow.getAttribute("ParamName"));
                                            
                                            detailList.add(setupDtlBean);
                                            
                                            if(setupDtlItr.getViewObject().hasNext()) {
                                                dtlRow = setupDtlItr.getViewObject().next();
                                            } else {
                                                dtlRow = null;
                                            }        
                                        }while(dtlRow!=null);
                                    }
                                }
                                bean.setReportSetupDetail(detailList);
                                DCIteratorBinding dataMappingItr=Util.getDCBindingContainer().findIteratorBinding("ReportDataMappingEOVO1Iterator");
                                List<ReportDataMappingBean> dataMappingList=new ArrayList<ReportDataMappingBean>();
                                ReportDataMappingBean dataMappBean = null;
                                if(dataMappingItr.getViewObject()!=null){
                                    if(dataMappingItr.getViewObject().first()!=null)
                                    {
                                        Row mappingRow = dataMappingItr.getViewObject().first();
                                        do{
                                            dataMappBean = new ReportDataMappingBean();
                                            
                                            dataMappBean.setMapId(mappingRow.getAttribute("MapId").toString());
                                            dataMappBean.setReportColName((String)mappingRow.getAttribute("ReportColName"));
                                            dataMappBean.setReportSeqName(Long.valueOf((mappingRow.getAttribute("ReportSeqName") != null ? mappingRow.getAttribute("ReportSeqName").toString() : "0")));
                                            dataMappBean.setTableColName((String)mappingRow.getAttribute("TableColName"));
                                            dataMappBean.setDefaultValue((String)mappingRow.getAttribute("DefaultValue"));
                                            dataMappBean.setReportHdrId(mappingRow.getAttribute("ReportHeaderId").toString());
                                            dataMappBean.setPrimaryFlag((String)mappingRow.getAttribute("PrimaryFlag"));
                                            dataMappBean.setDateFormat((String)mappingRow.getAttribute("DateFormat"));
                                            dataMappBean.setIsSeq((String)mappingRow.getAttribute("IsSeq"));
                                            dataMappBean.setColumnDataType((String)mappingRow.getAttribute("ColumnDataType"));
                                            
                                            dataMappingList.add(dataMappBean);
                                            if(dataMappingItr.getViewObject().hasNext()) {
                                                mappingRow = dataMappingItr.getViewObject().next();
                                            } else {
                                                mappingRow = null;
                                            }        
                                        }while(mappingRow!=null);
                                    }
                                }
                                bean.setReportDataMappings(dataMappingList);
                               if(setupHeaderItr.getViewObject().hasNext()) {
                                    row = setupHeaderItr.getViewObject().next();
                                } else {
                                    row = null;
                                }                       
                            }while(row!=null);
                        }
                        
                        schedulerIter.getViewObject().executeQuery();
                        Class cla = QuartzJobBean.class;
                        String jobName = SCHEDULER_JOB_PREFIX + schedulerId;
                        String groupName = SCHEDULER_JOBGROUP_PREFIX + schedulerId;
                        String trigger = SCHEDULER_TRIGGER_PREFIX + schedulerId;
                        if (Constants.SCH_TYPE_FREQUENCY.equals(schedulerType)) {
                            
                            daySeconds = (secondValue.intValue() + minuteValue.intValue() * 60 +hourValue.intValue() * 3600);
                            scheduleJob(String.valueOf(daySeconds), serviceType, schedulerType, jobName, groupName, trigger, cla, serviceType, bean, port, setupMap);
                        }else if (Constants.SCH_TYPE_TIME.equals(schedulerType)) {
                            
                            if(secondValue.compareTo(new BigDecimal(60))==0) secondValue = BigDecimal.ZERO;
                            if(minuteValue.compareTo(new BigDecimal(60))==0) minuteValue = BigDecimal.ZERO;
                            if(hourValue.compareTo(new BigDecimal(24))==0) hourValue = BigDecimal.ZERO;                            
                            String format = secondValue + " " + minuteValue + " " + hourValue + " * * ?";
                            scheduleJob(format, serviceType, schedulerType, jobName, groupName , trigger, cla, serviceType, bean, port, setupMap);
                        }
                    }
                 }
                }else{
                    ErrorMessage("Scheduler is already running, Please try after some time.");
                }
            }
        } catch (Exception e) {
            
            e.printStackTrace();
            ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), e, null);           
        }
    }


    /**
     * @desc stop scheduler button event
     * @param actionEvent
     */
    public void stopScheduler(ActionEvent actionEvent) {
        try {
           DCIteratorBinding SchedulerVO1Iterator = Util.getDCBindingContainer().findIteratorBinding("SchedulerSetupEOVO1Iterator");
           String schedulerId = null;
           if (null != SchedulerVO1Iterator && null != SchedulerVO1Iterator.getViewObject() &&
               null != SchedulerVO1Iterator.getViewObject().getCurrentRow()) {
               SchedulerVO1Iterator.getViewObject().getCurrentRow().setAttribute("ScheduleFlag", "N");
               if(SchedulerVO1Iterator.getViewObject().getCurrentRow().getAttribute("SchedulerId") != null){
                   schedulerId = SchedulerVO1Iterator.getViewObject().getCurrentRow().getAttribute("SchedulerId").toString();
               }
           }
        
            Util.executeOperation("Commit");
            SchedulerVO1Iterator.getViewObject().executeQuery();
            schedulerStop(schedulerId);
        } catch (Exception e) {
            
            e.printStackTrace();
            ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), e, null);
        }
    }


    /**
     * @Desc to stop the scheduler 
     * @throws Exception
     */
    public void schedulerStop(String schedulerId) throws Exception {
        try {
            
            Scheduler scheduler = ((StdSchedulerFactory) ServletStartBean.getServletcontext().getAttribute(QuartzInitializerListener.QUARTZ_FACTORY_KEY)).getScheduler();
            //Scheduler scheduler = new StdSchedulerFactory().getScheduler();           
            if (scheduler != null
                && schedulerId!=null) {
                
                String jobName = Constants.SCHEDULER_JOB_PREFIX + schedulerId;
                String groupName = Constants.SCHEDULER_JOBGROUP_PREFIX + schedulerId;
                String trigger = Constants.SCHEDULER_TRIGGER_PREFIX + schedulerId;
                scheduler.unscheduleJob(TriggerKey.triggerKey(trigger, groupName));                         
                scheduler.deleteJob(JobKey.jobKey(jobName, groupName));
            }
        } catch (Exception e) {
            
            ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), e, null);
            e.printStackTrace();
            throw e;
        }
    }
//    public void schedulerStop(String serviceName) throws Exception {
//        try {
//            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
//            if (serviceName != null && scheduler != null) {
//                scheduler.deleteJob(new JobKey(serviceName + "Job",serviceName + "Group"));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }


    
    public  void scheduleJob(String time, String type, String freqType, String jobName, String jobGroup, String triggerName, Class className,String serviceType, ReportDataBean reportBean,PublicReportService port,HashMap<String,String> setupMap) throws Exception {
        
        if (type != null) {
            
            JobKey jobKey = new JobKey(jobName, jobGroup);
            JobDetail jobDetail = JobBuilder.newJob(className).withIdentity(jobKey).build();
            JobDataMap dataNow = jobDetail.getJobDataMap();
            dataNow.put("ServiceType", serviceType);
            dataNow.put("reportBean", reportBean);
            dataNow.put("port", port);
            dataNow.put("setupMap", setupMap);
            Trigger trigger = null;
            if (freqType != null 
                && freqType.equals(Constants.SCH_TYPE_FREQUENCY)) 
                trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, jobGroup).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(Integer.parseInt(time)).repeatForever()).build();
            else if (freqType != null 
                     && freqType.equals(Constants.SCH_TYPE_TIME)) 
                
                trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, jobGroup).withSchedule(CronScheduleBuilder.cronSchedule(time)).build();
//            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            Scheduler scheduler = ((StdSchedulerFactory) ServletStartBean.getServletcontext().getAttribute(QuartzInitializerListener.QUARTZ_FACTORY_KEY)).getScheduler();
            scheduler.getContext().put("ServiceType", serviceType);

            if (!scheduler.checkExists(jobKey)) {
                if (!scheduler.isStarted()) {
                    scheduler.start();
                }
                scheduler.scheduleJob(jobDetail, trigger);
            }
        }
    }
    
    public String  reFreshScheduler(){
        try{
            OperationBinding op=util.findOperation("ExecuteScheduler");
            op.execute();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public String  reFreshRunHis(){
        try{
            OperationBinding op=util.findOperation("ExecuteRunHis");
            op.execute();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Desc startScheduler buttion event
     * @param actionEvent
     * @throws Exception
     */
    public void startScheduler(ActionEvent actionEvent) throws Exception {
        try {
            schedulePopUp.show(new RichPopup.PopupHints());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    

    public void scheduleDailog(DialogEvent dialogEvent) {
        try{
            ScheduleReport();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void openErrorPopUp(ActionEvent actionEvent) {
        try{
            DCIteratorBinding runHistItr = Util.getDCBindingContainer().findIteratorBinding("ReportRunHistoryEOVO1Iterator");
                if(runHistItr!=null && runHistItr.getViewObject()!=null
                && runHistItr.getViewObject().getCurrentRow()!=null){
                    BigDecimal runHistId=(BigDecimal)runHistItr.getViewObject().getCurrentRow().getAttribute("Id");
                    DCIteratorBinding exceptionItr = Util.getDCBindingContainer().findIteratorBinding("ExceptionLogVO1Iterator");
                    if(exceptionItr!=null && exceptionItr.getViewObject()!=null){
                        exceptionItr.getViewObject().setWhereClause(null);
                        exceptionItr.getViewObject().setWhereClause("JOB_ID='"+runHistId+"'");
                        exceptionItr.getViewObject().executeQuery();
                    }
                }
           
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public JobDetail setSchedulerClass(JobKey jobKey,String schedulerId, Class<? extends Job> schedulerClass) {
           JobDetail jobDetailNow = null;
           jobDetailNow=JobBuilder.newJob(schedulerClass).withIdentity(jobKey).build();
           
           JobDataMap dataNow = jobDetailNow.getJobDataMap();
           dataNow.put("SchedulerId", schedulerId);
           ServletContext context = (ServletContext) FacesContext.getCurrentInstance()
                                                                 .getExternalContext()
                                                                 .getContext();
//           System.out.println(context.getRealPath('/' + File.separator));
           dataNow.put("contextRootPath", context.getRealPath('/'+File.separator));
           return jobDetailNow;
       }
}
