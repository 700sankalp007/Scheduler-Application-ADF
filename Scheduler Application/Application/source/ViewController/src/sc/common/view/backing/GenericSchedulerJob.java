package sc.common.view.backing;

import java.sql.CallableStatement;
import java.sql.Connection;

import java.util.HashMap;
import java.util.Map;

import sc.common.biReport.PublicReportService;
import sc.common.view.bean.ReportDataBean;
import sc.common.view.util.ConnectionManager;
import sc.common.view.util.Constants;
import sc.common.view.util.ExceptionLog;
import sc.common.view.util.Util;

public class GenericSchedulerJob implements Runnable {
     
    ReportDataBean bean=null;
    Util util=null;
    HashMap<String,String> setupMap=null;
    PublicReportService port=null;
    String runHistId=null;
    
    public GenericSchedulerJob(ReportDataBean bean, HashMap<String, String> setupMap,PublicReportService port,String runHistId) {
        this.bean = bean;
        this.setupMap = setupMap;
        this.port=port;
        this.runHistId=runHistId;
        util=new Util();
       
    }

    @Override
    public void run() {
        
        Connection con=null;
        double recordProcessed=0;
        boolean isError=false;
        String startDate=null,dateFormat=null,endDate=null;
        Map<String,Object> freqCustDtl=null;
        long startDateMsec=0,endDateMsec=0,frequency=0;
        try {
            con = ConnectionManager.getConnection();
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
                    recordProcessed = new PhysicalCountIntegration(bean,port,setupMap,con, runHistId).executePhysicalCountIntegration();  
//                     new PhysicalCountIntegration(bean,port,setupMap,con, runHistId).executePhysicalCountIntegration();  
//                  recordProcessed = new PhysicalCountIntegration_bkp(bean,port,setupMap,con).executePhysicalCountIntegration();  
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
//                        
//                        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
//                        
//                        startDate=util.getSchLastRunDate(bean.getServiceType(),dateFormat,con);
//                        endDate=util.getDateTime(dateFormat);
//                        
//                        startDateMsec = sdf.parse(startDate).getTime();
//                        endDateMsec = sdf.parse(endDate).getTime();        
//                        
//                        util.updateSchedulerDate(bean.getServiceType());
//                        
//                        for(long i = startDateMsec; i<=endDateMsec; i += frequency){
//                           
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
//                        }
//                        
                }
                
                //BELOW CODE IS FOR SEND DAILY PRODUCTION REPORT THROUGH MAIL
                else if(bean.getServiceType().equals(Constants.LABEL_PRINT_PRODUCTION_REPORT))
                    recordProcessed=Util.labelPrintProductionReport(setupMap.get("SERVLET_CONTEXT_PATH"));
                                
                else if(bean.getServiceType().equals(Constants.SERVICE_TYPE_EXCHANGE_RATE))
                    recordProcessed=new ExchangeRateIntegration(bean,port,setupMap,con).executeExchangeRateIntegration();
                else if(bean.getDataFormat().equals(Constants.CSV)){
                    System.out.println("GenericSchedulerJob: 1");
                    recordProcessed = new ETL(bean,port,setupMap,con,runHistId,null,null,false).executeETL(); 
                }
                else{                    
                    System.out.println("GenericSchedulerJob: 2");
                    recordProcessed=util.processReport(con,bean,port,setupMap);   
                }
            }catch(Exception e){
                isError=true;
                recordProcessed=-1;
                e.printStackTrace();
                ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), e, runHistId);                
            }
    
//            if(!bean.getServiceType().equals(Constants.SERVICE_TYPE_PHYSICAL_COUNT_DETAIL)){
                util.updateRecordProcessHistory(recordProcessed,isError,runHistId,con,bean.getReportJobName());
//            }
    
        }catch(Exception e){
            
            recordProcessed=0;
            e.printStackTrace();
            try {
                
                util.updateReportRunHistory(runHistId, con, Constants.STATUS_ERRORED, recordProcessed);
                ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), e, runHistId);
            } catch (Exception f) {
                
                f.printStackTrace();
                ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), f, runHistId);
            }
        }finally{
            try {
                System.out.print("PC: GenericSchedulerJob(): updateSchedulerFlag()");
                util.updateSchedulerFlag(Constants.VALUE_NO, bean.getServiceType());
            } catch (Exception e) {
                
                e.printStackTrace();
                ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), e, runHistId);
            }
            
            if(con!=null) ConnectionManager.releaseConnetion(con);
            
        }
    }
}
