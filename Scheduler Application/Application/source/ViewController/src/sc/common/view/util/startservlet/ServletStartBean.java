package sc.common.view.util.startservlet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import sc.common.biReport.PublicReportService;
import sc.common.view.backing.GenericSchedulerBean;
import sc.common.view.backing.QuartzJobBean;
import sc.common.view.bean.ReportDataBean;
import sc.common.view.bean.ReportDataMappingBean;
import sc.common.view.bean.ReportSetupDetailBean;
import sc.common.view.util.ConnectionManager;
import sc.common.view.util.Constants;
import sc.common.view.util.ExceptionLog;
import sc.common.view.util.ServiceBean;
import sc.common.view.util.SoapHandler;
import sc.common.view.util.Util;

public class ServletStartBean extends HttpServlet {
    
    private static ServletContext servletContext;

    public static ServletContext getServletcontext() {
        return servletContext;
    }

    public void init(ServletConfig servletConfig) {
        servletContext = servletConfig.getServletContext();
        try(Connection conn = ConnectionManager.getConnection();            
            PreparedStatement pstmt = conn.prepareStatement(Constants.UPDATE_SCHEDULER_FLAG);){
            pstmt.executeUpdate();
            conn.commit();            
            startScheduler(conn);            
//            System.out.println("----------------------------------------------------------------");
//            System.out.println("----------------------Sevlet contextDestroyed-------------------");
//            System.out.println("----------------------------------------------------------------");
        } catch (Exception e) {
            
            e.printStackTrace();
            ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), e, null);
        }
    }
    
//Start the Scheduler
    private void startScheduler(Connection cn) throws Exception {
        
        int hour = 0, minute = 0, second = 0;
        String serviceType = null, schedulerType = null, serviceName = null, schedulerId = null, lastRunDate = null;        
        GenericSchedulerBean schedulerBean = new GenericSchedulerBean();
        HashMap<String, String> setupMap = Util.getSetupDetails(cn, Constants.SETUP_TYPE_WEBSEVICE, Constants.SUB_TYPE_FUSION);
        setupMap.put("SERVLET_CONTEXT_PATH", servletContext.getRealPath('/'+ java.io.File.separator));
        
        PublicReportService port = ServiceBean.getPublicReportServicePort(setupMap.get("HOST"));
        BindingProvider bp = (BindingProvider) port;
        
        Map<String, Object> requestContext = bp.getRequestContext();
        requestContext.put(BindingProvider.USERNAME_PROPERTY, setupMap.get("USER_NAME"));
        requestContext.put(BindingProvider.PASSWORD_PROPERTY, setupMap.get("PASSWORD"));
        requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, setupMap.get("HOST") + "/xmlpserver/services/PublicReportService");
        
        Binding binding = bp.getBinding();
        bp.getRequestContext().put("debugEnabled", "false");
        bp.getRequestContext().put("module", "LOG");
        
        List<Handler> handlerChain = binding.getHandlerChain();
        SoapHandler soapHandler = new SoapHandler();
        handlerChain.add(soapHandler);
        binding.setHandlerChain(handlerChain);

//        System.out.println("----------------------------------------------------------------");
//        System.out.println("-------------------Sevlet contextInitialized--------------------");
//        System.out.println("----------------------------------------------------------------");

        try(PreparedStatement ps = cn.prepareStatement(Constants.SCHEDULER_QUERY);
            ResultSet rs = ps.executeQuery();){
                
            while (rs.next()) {
                hour = 0;
                minute = 0;
                second = 0;
                long daySeconds = 0;
                if (rs.getString("HOUR") != null && !rs.getString("HOUR").equals("")) {
                    hour = Integer.parseInt(rs.getString("HOUR"));
                }
                if (rs.getString("MINUTE") != null && !rs.getString("MINUTE").equals("")) {
                    minute = Integer.parseInt(rs.getString("MINUTE"));
                }
                if (rs.getString("SECOND") != null && !rs.getString("SECOND").equals("")) {
                    second = Integer.parseInt(rs.getString("SECOND"));
                }
                if (rs.getString("SERVICE_TYPE") != null && !rs.getString("SERVICE_TYPE").equals("")) {
                    serviceType = rs.getString("SERVICE_TYPE");
                }
                if (rs.getString("SERVICE_NAME") != null && !rs.getString("SERVICE_NAME").equals("")) {
                    serviceName = rs.getString("SERVICE_NAME");
                }
                if (rs.getString("SCHEDULER_TYPE") != null && !rs.getString("SCHEDULER_TYPE").equals("")) {
                    schedulerType = rs.getString("SCHEDULER_TYPE");
                }
                if (rs.getString("LAST_RUN_DATE") != null && !rs.getString("LAST_RUN_DATE").equals("")) {
                    lastRunDate = rs.getString("LAST_RUN_DATE");
                }
                if (rs.getString("SCHEDULER_ID") != null && !rs.getString("SCHEDULER_ID").equals("")) {
                    schedulerId = rs.getString("SCHEDULER_ID");
                }

                ReportDataBean bean = null;
                bean = new ReportDataBean();

                bean.setSchedulerId(schedulerId);
                bean.setServiceType(serviceType);

                String repHdrId = getRerportSetupHdr(cn , bean, schedulerId);
                getRerportSetupDtl(cn, bean, repHdrId);
                getRerportDataMapp(cn, bean, repHdrId);

                Class className = QuartzJobBean.class;
                String jobName = Constants.SCHEDULER_JOB_PREFIX + schedulerId;
                String groupName = Constants.SCHEDULER_JOBGROUP_PREFIX + schedulerId;
                String trigger = Constants.SCHEDULER_TRIGGER_PREFIX + schedulerId;
                if (className != null && jobName != null && groupName != null && trigger != null && serviceType != null) {
                    if (Constants.SCH_TYPE_FREQUENCY.equals(schedulerType)) {
                        daySeconds = (second + minute * 60 + hour * 3600);
                        schedulerBean.scheduleJob(String.valueOf(daySeconds), serviceType, schedulerType, jobName, groupName, trigger, className, serviceType, bean, port, setupMap);
                    }
                    if (Constants.SCH_TYPE_TIME.equals(schedulerType)) {
                        
                        if(second==60) second = 0;
                        if(minute==60) minute = 0;
                        if(hour==24) hour = 0;                            
                        String format = String.valueOf(second) + " " + String.valueOf(minute) + " " + String.valueOf(hour) + " * * ?";
                        schedulerBean.scheduleJob(format, serviceType, schedulerType, jobName, groupName, trigger, className, serviceType, bean, port, setupMap);
                    }
                }
            }
        }
    }
        
    private String getRerportSetupHdr(Connection cn, ReportDataBean bean, String schedulerId) throws SQLException {
        
        try (PreparedStatement psHdr = cn.prepareStatement(Constants.REPORT_HEADER_QUERY);){
            
            psHdr.setString(1, schedulerId);
            try(ResultSet rsHdr = psHdr.executeQuery();){
                
                if(rsHdr.next()) {
                    
                    bean.setDataFormat(rsHdr.getString("REPORT_DATA_FORMAT"));
                    bean.setDataLocale(rsHdr.getString("REPORT_DATA_LOCALE"));
                    bean.setDelimiter(rsHdr.getString("DELIMITER"));
                    bean.setIsRefresh(rsHdr.getString("IS_REFRESH"));
                    bean.setReportDataTable(rsHdr.getString("REPORT_DATA_TABLE"));
                    bean.setReportPath(rsHdr.getString("REPORT_PATH"));
                    bean.setRootNode(rsHdr.getString("ROOT_NODE"));
                    bean.setReportJobName(rsHdr.getString("ESS_JOB_NAME"));
                    return rsHdr.getString("EI_REP_HEADER_ID");
                }else
                    return null;
            }            
        }
    }
    
    private void getRerportSetupDtl(Connection cn, ReportDataBean bean, String repHeaderId) throws SQLException {
        
        List<ReportSetupDetailBean> detailList = new ArrayList<ReportSetupDetailBean>();
        ReportSetupDetailBean setupDtlBean = null;
        try (PreparedStatement psDtl = cn.prepareStatement(Constants.REPORT_SETUP_QUERY);){
            
            psDtl.setString(1, repHeaderId);
            try(ResultSet rsDtl = psDtl.executeQuery();){
            
                while (rsDtl.next()) {
                    setupDtlBean = new ReportSetupDetailBean();

                    setupDtlBean.setSqlStatement(rsDtl.getString("SQL_STATEMENT"));
                    setupDtlBean.setDefualtVal(rsDtl.getString("DEFUALT_VAL"));
                    setupDtlBean.setParamName(rsDtl.getString("PARAM_NAME"));

                    detailList.add(setupDtlBean);
                }
                bean.setReportSetupDetail(detailList);

            }
        }
    }
    
    private void getRerportDataMapp(Connection cn, ReportDataBean bean, String repHeaderId) throws SQLException {
        
        List<ReportDataMappingBean> dataMappingList = new ArrayList<ReportDataMappingBean>();
        ReportDataMappingBean dataMappBean = null;
        try (PreparedStatement psMap = cn.prepareStatement(Constants.REPORT_DATA_MAPPING_QUERY);){
            
            psMap.setString(1, repHeaderId);
            try(ResultSet rsMap = psMap.executeQuery();){
                                
                while (rsMap.next()) {
                    
                    dataMappBean = new ReportDataMappingBean();
                    dataMappBean.setMapId(rsMap.getString("MAP_ID"));
                    dataMappBean.setReportColName(rsMap.getString("REPORT_COL_NAME"));
                    dataMappBean.setReportSeqName(Long.valueOf(rsMap.getString("REPORT_SEQ_NAME")!= null ? rsMap.getString("REPORT_SEQ_NAME") : "0"));
                    dataMappBean.setTableColName(rsMap.getString("TABLE_COL_NAME"));
                    dataMappBean.setDefaultValue(rsMap.getString("DEFAULT_VALUE"));
                    dataMappBean.setReportHdrId(rsMap.getString("REPORT_HEADER_ID"));
                    dataMappBean.setPrimaryFlag(rsMap.getString("PRIMARY_FLAG"));
                    dataMappBean.setDateFormat(rsMap.getString("DATE_FORMAT"));
                    dataMappBean.setIsSeq(rsMap.getString("IS_SEQ"));
                    dataMappBean.setColumnDataType(rsMap.getString("COLUMN_DATA_TYPE"));
                    dataMappingList.add(dataMappBean);
                }
                bean.setReportDataMappings(dataMappingList);
            }
        }
    }

}

