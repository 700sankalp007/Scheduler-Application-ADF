package sc.common.view.backing;

import java.sql.CallableStatement;
import java.sql.Connection;

import java.text.SimpleDateFormat;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sc.common.biReport.PublicReportService;
import sc.common.view.bean.ReportDataBean;
import sc.common.view.bean.ReportSetupDetailBean;
import sc.common.view.util.ConnectionManager;
import sc.common.view.util.Constants;
import sc.common.view.util.DML;
import sc.common.view.util.ExceptionLog;
import sc.common.view.util.Util;

public class CustomSchedulerBean implements Constants {
    
    ReportDataBean bean = null;
    HashMap<String,String> setupMap = null;    
    Map<String, Object> runHistoryMap = null, customSetupMap = null;
    PublicReportService port = null;
    Util util;

    public CustomSchedulerBean(ReportDataBean bean, PublicReportService port, HashMap<String, String> setupMap) {

        super();
        this.util = new Util();
        this.bean = bean;
        this.port = port;
        this.setupMap = setupMap;
    }
        
    public void runCustomReport() {
        
        try(Connection con = ConnectionManager.getConnection()){
            
            customSetupMap = Util.queryForMap(CUSTOM_SETUP_QUERY, new Object[]{bean.getServiceType()}, con);
            if(customSetupMap!=null){
                
                long frequency = Long.valueOf(customSetupMap.get("FREQUENCY").toString());
                SimpleDateFormat sdf = new SimpleDateFormat(customSetupMap.get("DATE_FORMAT").toString());
                long startDateMsec = sdf.parse(customSetupMap.get("START_DATE").toString()).getTime();
                long endDateMsec = sdf.parse(customSetupMap.get("END_DATE").toString()).getTime();        
                for(long i = startDateMsec; i<endDateMsec; i += frequency){
                    
                    customSetupMap = Util.queryForMap(CUSTOM_SETUP_QUERY, new Object[]{bean.getServiceType()}, con);
                    if(customSetupMap!=null
                        && customSetupMap.get("STOP_FLAG")!=null
                        && customSetupMap.get("STOP_FLAG").toString().equals(VALUE_YES))
                        break;
                    
                    String fromDate = sdf.format(new Date(i));
                    String toDate = sdf.format(new Date(i+frequency));
                    
                    if(bean.getReportSetupDetail()!=null
                       && !bean.getReportSetupDetail().isEmpty()){
                           
                        for(ReportSetupDetailBean rsdb : bean.getReportSetupDetail()){
                            
                            if(rsdb.getParamName().equals(customSetupMap.get("FROM_DATE_PARAM").toString())){
                                
                                rsdb.setDefualtVal(fromDate);
                                rsdb.setSqlStatement(null);
                            }else if(rsdb.getParamName().equals(customSetupMap.get("TO_DATE_PARAM").toString())){
                                
                                rsdb.setDefualtVal(toDate);
                                rsdb.setSqlStatement(null);
                            }
                        }
                    }

//                    List<ReportSetupDetailBean> rsdbList = new ArrayList<>();
//                    ReportSetupDetailBean rsdb = new ReportSetupDetailBean();
//                    rsdb.setParamName(customSetupMap.get("FROM_DATE_PARAM").toString());
//                    rsdb.setDefualtVal(fromDate);
//                    rsdbList.add(rsdb);
//                    rsdb = new ReportSetupDetailBean();
//                    rsdb.setParamName(customSetupMap.get("TO_DATE_PARAM").toString());
//                    rsdb.setDefualtVal(toDate);
//                    rsdbList.add(rsdb);                    
//                    bean.setReportSetupDetail(rsdbList);
                    
                    this.runReport(con);
                }
            }
        }catch(Exception e){
            
            e.printStackTrace();
            ExceptionLog.CreateExceptionLog(this.getClass().getName(), "runCustomReport", e, null);
        }
    }
    
    private String addRunHistory(Connection con) throws Exception {
        
        String runHistoryId = null;        
        runHistoryMap = new HashMap<>();
        runHistoryId = Util.getSequenceValue(con, "XX_SCH_REPORT_RUN_HIST_SEQ");
        runHistoryMap.put("ID", runHistoryId);
        runHistoryMap.put("STATUS", STATUS_RUNNING);
        runHistoryMap.put("SERVICE_TYPE", bean.getServiceType());
        runHistoryMap.put("RUN_TYPE", CUSTOM_RUN);
        runHistoryMap.put("SCHEDULER_ID", bean.getSchedulerId());        
        if(bean.getReportSetupDetail()!=null
            && !bean.getReportSetupDetail().isEmpty())
            for(ReportSetupDetailBean rsdb : bean.getReportSetupDetail()){
                
                if(rsdb.getParamName().equals(customSetupMap.get("FROM_DATE_PARAM").toString()))
                    runHistoryMap.put("PARAM1", rsdb.getDefualtVal());        
                else if(rsdb.getParamName().equals(customSetupMap.get("TO_DATE_PARAM").toString()))                    
                    runHistoryMap.put("PARAM2", rsdb.getDefualtVal());                        
            }
        
        DML dml = new DML(con);
        dml.insertData(XX_SCH_REPORT_RUN_HIS, Arrays.asList(runHistoryMap), null);
        con.commit();
        return runHistoryId;
    }
    
    private double runReport(Connection con) throws Exception {
                
        double recordProcessed = 0;
        String runHistoryId = null;
        try{
            
            runHistoryId = this.addRunHistory(con);
            if(bean.getServiceType().equals(Constants.SERVICE_TYPE_PO_DETAILS))                    
                recordProcessed = new PoIntegration(bean,port,setupMap,con).executePoIntegration();
            else if(bean.getServiceType().equals(Constants.SERVICE_TYPE_AP_INV_EMAIL))
                recordProcessed = new ApInvoiceEmailIntegration(bean,port,setupMap,con).executeApInvoiceEmailIntegration();  
            else if(bean.getServiceType().equals(Constants.SERVICE_TYPE_CYCLE_COUNT_ITEM_COST))
                recordProcessed = new CycleCountItemCostIntegration(bean,port,setupMap,con).executeCycleCountItemCostIntegration(); 
            else if(bean.getServiceType().equals(Constants.SERVICE_TYPE_CYCLE_COUNT_SEQUENCE_DETAIL))
                recordProcessed = new CycleCountSeqDetIntegration(bean,port,setupMap,con).executeCycleCountSeqDetIntegration(); 
            else if(bean.getServiceType().equals(Constants.SERVICE_TYPE_PHYSICAL_COUNT_DETAIL))
//                recordProcessed = new PhysicalCountIntegration_bkp(bean,port,setupMap,con).executePhysicalCountIntegration();  
                recordProcessed = new PhysicalCountIntegration(bean,port,setupMap,con, runHistoryId).executePhysicalCountIntegration();
            else if(bean.getServiceType().equals(Constants.SERVICE_TYPE_BOQ_REVENUE_NOTIF))
                recordProcessed=new RevenueEmailIntegration(bean,port,setupMap,con).executeRevenueEmailIntegration();
            else if(bean.getServiceType().equals(Constants.SERVICE_TYPE_CUTL_ITEM_STRUCTURE)){
                    recordProcessed = new ETL(bean,port,setupMap,con,runHistoryId,null,null,true).executeETL();                                
                    try(CallableStatement cs=con.prepareCall("{call CUT_LOGIC.PROCESS_ITEM_STRCTURE}")){
                        cs.executeQuery();
                    }
                //if(recordProcessed>0) 
                  //  util.updateWOItemStructure(con);
            }else if(bean.getServiceType().equals(Constants.SERVICE_TYPE_EXCHANGE_RATE))
                recordProcessed=new ExchangeRateIntegration(bean,port,setupMap,con).executeExchangeRateIntegration();
            else if(bean.getDataFormat().equals(Constants.CSV))
                recordProcessed = new ETL(bean,port,setupMap,con,runHistoryId,null,null,true).executeETL();                                
            else                
                recordProcessed=util.processReport(con,bean,port,setupMap);                
            
            if(recordProcessed<0) recordProcessed=0;
           
            util.updateReportRunHistory(runHistoryId,con,Constants.STATUS_COMPLETED,recordProcessed);
           
            if(bean.getReportJobName()!=null
                && recordProcessed>0){
           
                util.deleteUCMFile(runHistoryId, con);
            }
        }catch(Exception e){
            
            util.updateReportRunHistory(runHistoryId, con, Constants.STATUS_ERRORED, recordProcessed);            
            ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), e, runHistoryId);
            throw e;
        }
        
        return recordProcessed;
    }
        
}
