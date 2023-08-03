package sc.common.view.backing;

import java.sql.CallableStatement;
import java.sql.Connection;

import java.util.HashMap;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import sc.common.biReport.PublicReportService;
import sc.common.view.bean.ReportDataBean;
import sc.common.view.util.ConnectionManager;
import sc.common.view.util.Constants;
import sc.common.view.util.ExceptionLog;
import sc.common.view.util.Util;

public class QuartzJobBean implements Job {
    Util  util=null;
    public QuartzJobBean() {
        super();
        util=new Util();
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        
        Connection con = null;
        String serviceType=null;        
        String runHistId=null;
        double recordProcessed = 0;
        boolean isError=false;
        String startDate=null,dateFormat=null,endDate=null;
        Map<String,Object> freqCustDtl=null;
        String custFreqFlag=null;
        long startDateMsec=0,endDateMsec=0,frequency=0;
        String errorReason=null;
        try{ 
          
            con = ConnectionManager.getConnection();
            serviceType = jobExecutionContext.getJobDetail().getJobDataMap().get("ServiceType").toString();
//            System.out.println(serviceType+" : "+new Date());            
            ReportDataBean bean = (ReportDataBean) jobExecutionContext.getJobDetail().getJobDataMap().get("reportBean");
            PublicReportService port = (PublicReportService) jobExecutionContext.getJobDetail().getJobDataMap().get("port");
            HashMap<String,String> setupMap = (HashMap <String,String>) jobExecutionContext.getJobDetail().getJobDataMap().get("setupMap");                   
            runHistId = util.addRunHistory(con, bean);
            try{
                if(bean.getServiceType().equals(Constants.SERVICE_TYPE_PO_DETAILS))                    
                    recordProcessed = new PoIntegration(bean,port,setupMap,con).executePoIntegration();
                else if(bean.getServiceType().equals(Constants.SERVICE_TYPE_AP_INV_EMAIL))
                    recordProcessed = new ApInvoiceEmailIntegration(bean,port,setupMap,con).executeApInvoiceEmailIntegration();  
//                else if(bean.getServiceType().equals(Constants.SERVICE_TYPE_LABEL_WORK_ORDER))
//                    recordProcessed = new LabelPrintingIntegration(bean,port,setupMap,con).executeLabelPrintingIntegration();
                else if(bean.getServiceType().equals(Constants.SERVICE_TYPE_CYCLE_COUNT_ITEM_COST))
                    recordProcessed = new CycleCountItemCostIntegration(bean,port,setupMap,con).executeCycleCountItemCostIntegration(); 
                else if(bean.getServiceType().equals(Constants.SERVICE_TYPE_CYCLE_COUNT_SEQUENCE_DETAIL))
                    recordProcessed = new CycleCountSeqDetIntegration(bean,port,setupMap,con).executeCycleCountSeqDetIntegration(); 
                else if(bean.getServiceType().equals(Constants.SERVICE_TYPE_PHYSICAL_COUNT_DETAIL))
//                    recordProcessed = new PhysicalCountIntegration_bkp(bean,port,setupMap,con).executePhysicalCountIntegration();  
                    recordProcessed = new PhysicalCountIntegration(bean,port,setupMap,con, runHistId).executePhysicalCountIntegration();
                else if(bean.getServiceType().equals(Constants.SERVICE_TYPE_BOQ_REVENUE_NOTIF))
                    recordProcessed=new RevenueEmailIntegration(bean,port,setupMap,con).executeRevenueEmailIntegration();
               
                else if(bean.getServiceType().equals(Constants.SERVICE_TYPE_CUTL_ITEM_STRUCTURE)){
                      
                    recordProcessed = new ETL(bean,port,setupMap,con,runHistId,null,null,false).executeETL();                                
                    try(CallableStatement cs=con.prepareCall("{call CUT_LOGIC.PROCESS_ITEM_STRCTURE}")){
                        cs.executeQuery();
                    }
//                        freqCustDtl=new HashMap<String,Object>();
//                        freqCustDtl=util.getFreqCustDtl(bean.getServiceType(), con);
//                      
//                        frequency=(Long)freqCustDtl.get(Constants.FREQUENCY);
//                        dateFormat=(String)freqCustDtl.get(Constants.DATE_FORMAT);
//                        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
//                    
//                        startDate=util.getSchLastRunDate(bean.getServiceType(),dateFormat,con);
//                        endDate=util.getDateTime(dateFormat);
//                        
//                        startDateMsec = sdf.parse(startDate).getTime();
//                        endDateMsec = sdf.parse(endDate).getTime();        
//                        
//                        util.updateSchedulerDate(bean.getServiceType());
                        
//                        for(long i = startDateMsec; i<endDateMsec; i += frequency){
//                            custFreqFlag=util.getCustFreqFlag(con, bean.getServiceType());
//                      
//                            if(custFreqFlag!=null && custFreqFlag.equals(Constants.VALUE_YES))
//                                break;
//                            try{
//                                startDate = sdf.format(new Date(i));
//                                endDate = sdf.format(new Date(i+frequency));
//                           
//                                recordProcessed = new ETL(bean,port,setupMap,con,runHistId,startDate,endDate,false).executeETL();                                
//                                try(CallableStatement cs=con.prepareCall("{call CUT_LOGIC.PROCESS_ITEM_STRCTURE}")){
//                                    cs.executeQuery();
//                                }
//                                util.updateRecordProcessHistory(recordProcessed,isError,runHistId,con,bean.getReportJobName());
//                                runHistId = util.addRunHistory(con, bean);
//                                        
//                            }catch(Exception e){
//                                isError = true;
//                                recordProcessed = -1;
//                                e.printStackTrace();
//                                ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), e, runHistId);                 
//                            }
//                            
//                       
//                        }
//                        
                }
             
               // BELOW CODE IS FOR SEND DAILY PRODUCTION REPORT THROUGH MAIL
                else if(bean.getServiceType().equals(Constants.LABEL_PRINT_PRODUCTION_REPORT))
                    recordProcessed= Util.labelPrintProductionReport(setupMap.get("SERVLET_CONTEXT_PATH"));
                
                else if(bean.getServiceType().equals(Constants.SERVICE_TYPE_EXCHANGE_RATE))
                    recordProcessed=new ExchangeRateIntegration(bean,port,setupMap,con).executeExchangeRateIntegration();
                else if(bean.getDataFormat().equals(Constants.CSV))
                    recordProcessed = new ETL(bean,port,setupMap,con,runHistId,null,null,false).executeETL();     
                else                    
                    recordProcessed = util.processReport(con,bean,port,setupMap);                    
            }catch(Exception e){
                isError = true;
                recordProcessed = -1;
                e.printStackTrace();
                errorReason=e.getMessage();
                util.sendMailForFailedScmEtl(bean.getServiceType(), runHistId, errorReason,con);
                ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), e, runHistId);                 
            }
            
            util.updateRecordProcessHistory(recordProcessed,isError,runHistId,con,bean.getReportJobName());
          
            if(isError){
          
                util.sendMailForFailedScmEtl(bean.getServiceType(), runHistId, errorReason,con);
            }
        }catch(Exception e){
            
            recordProcessed = 0;
            e.printStackTrace();
            try {
                util.updateReportRunHistory(runHistId, con, Constants.STATUS_ERRORED, recordProcessed);
                ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), e, runHistId);
            } catch (Exception f) {
                f.printStackTrace();
                ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), f, runHistId);
            }
        }finally {
            
            try {
                
                util.updateSchedulerFlag(Constants.VALUE_NO,serviceType);
            } catch (Exception e) {
                
                e.printStackTrace();
                ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), e, runHistId);
            }
            
            if(con!=null) ConnectionManager.releaseConnetion(con);
        }
    }
    
}
