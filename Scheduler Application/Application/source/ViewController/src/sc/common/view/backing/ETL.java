package sc.common.view.backing;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sc.common.biReport.PublicReportService;
import sc.common.view.bean.ReportDataBean;
import sc.common.view.bean.ReportDataMappingBean;
import sc.common.view.util.Constants;
import sc.common.view.util.DML;
import sc.common.view.util.ExceptionLog;
import sc.common.view.util.Util;

public class ETL implements Constants {

    Map<String,Integer> fieldMap = new HashMap<>();
    HashMap<String, String> setupMap = null;    
    Map<String, Object> map = null;
    ReportDataBean bean=null;
    Util util=null;
    PublicReportService port=null;
    Connection con = null;
    String runHisId=null;
    String startDate=null;
    String endDate=null;
    boolean isCustom=false;
    public ETL(ReportDataBean bean, PublicReportService port, HashMap<String, String> setupMap, Connection con,String runHisId,String startDate,String endDate,boolean isCustom) {


        super();
        this.util = new Util();
        this.bean = bean;
        this.port = port;
        this.setupMap = setupMap;        
        this.con = con;
        this.runHisId=runHisId;
        this.startDate=startDate;
        this.endDate=endDate;
        this.isCustom=isCustom;
    }
    
    public int executeETL() throws Exception {
       
        int recordCount = 0;        
        try{
            byte[] data = this.extract();
                List<Map<String, Object>> mapList = this.transform(data);
            if((mapList!=null
                   && !mapList.isEmpty())
                || (bean.getIsRefresh()!=null
                    && bean.getIsRefresh().equals(VALUE_YES))){
                
                this.load(mapList);
                if(mapList!=null
                   && !mapList.isEmpty())
                    recordCount = mapList.size();
            }
            
//            System.out.println("Record Processed : " + recordCount);
        }catch(Exception e){
            e.printStackTrace();
            ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), e, null);
            throw e;
        }
        return recordCount;
    }
    
    private byte[] extract() throws Exception {
        
        if(bean.getReportJobName()!=null){
            return util.scheduleReportJob(bean,setupMap,runHisId,startDate,endDate,isCustom);
        }
        else
            return util.runBIReport(bean,setupMap,port);
    }
    
    private List<Map<String, Object>> transform(byte[] data) throws Exception {
        
        List<Map<String, Object>> mapList = null;        
        try (      InputStream is = new ByteArrayInputStream(data);
                   BufferedReader reader = new BufferedReader(new InputStreamReader(is,Constants.UTF8));){

                   String line;
                   int lineNum = 0;
            
                   while ((line = reader.readLine()) != null) {
                       try{
                           lineNum ++;
                           String csv[] = util.splitLine(line, bean.getDelimiter());
                           System.out.println("new Line open");
                           System.out.println(Arrays.deepToString(csv));
                           System.out.println("new Line close");
                           
                           if(lineNum==1){
                               
                               csv[0] = csv[0].substring(1);
                               for(int i = 0; i<csv.length; i++)
                                   fieldMap.put(csv[i],i);
                           }
                           else{
                               
                               map = new HashMap<>();
                               for(ReportDataMappingBean mappingBean : bean.getReportDataMappings()){
                                   
                                   if(fieldMap.get(mappingBean.getReportColName())==null) throw new RuntimeException("Report Column Not found : " + mappingBean.getReportColName());
                                   if(mappingBean.getColumnDataType().equalsIgnoreCase(NUMBER)
                                        || mappingBean.getColumnDataType().equalsIgnoreCase(DECIMAL)
                                        || mappingBean.getColumnDataType().equalsIgnoreCase(FLOAT)
                                        || mappingBean.getColumnDataType().equalsIgnoreCase(INTEGER))                                    
                                        map.put(mappingBean.getTableColName(), csv[fieldMap.get(mappingBean.getReportColName())]!=null ? new BigDecimal(csv[fieldMap.get(mappingBean.getReportColName())]) : null);  
                                   else if(mappingBean.getColumnDataType().equalsIgnoreCase(DATE))                                   
                                       map.put(mappingBean.getTableColName(), csv[fieldMap.get(mappingBean.getReportColName())]!=null ? new Date(new SimpleDateFormat(mappingBean.getDateFormat()).parse(csv[fieldMap.get(mappingBean.getReportColName())]).getTime()) : null);
                                   else if(mappingBean.getColumnDataType().equalsIgnoreCase(TIMESTAMP))                                   
                                       map.put(mappingBean.getTableColName(), csv[fieldMap.get(mappingBean.getReportColName())]!=null ? new Timestamp(new SimpleDateFormat(mappingBean.getDateFormat()).parse(csv[fieldMap.get(mappingBean.getReportColName())]).getTime()) : null);
                                   else
                                        map.put(mappingBean.getTableColName(), csv[fieldMap.get(mappingBean.getReportColName())]);
                               }
                               if(mapList==null) mapList = new  ArrayList<>();
                               mapList.add(map);
                           }    
                       }catch(Exception e){
                           e.printStackTrace();
                       }
                       
                   }
        }
        
        return mapList;
    }
    
    private void load(List<Map<String, Object>> mapList) throws Exception {
        
        DML dml = new DML(con);
        Set<String> primaryColumnSet = util.getPrimaryColumnSet(bean);
        if(bean.getIsRefresh()!=null
            && bean.getIsRefresh().equals(VALUE_YES))            
            dml.deleteData(bean.getReportDataTable());
        
        if(mapList!=null
           && !mapList.isEmpty()){
               
               if(primaryColumnSet!=null)                        
                   dml.processData(bean.getReportDataTable(), mapList, primaryColumnSet, null, null);
               else
                   dml.insertData(bean.getReportDataTable(), mapList);
        }

        con.commit();
    }
}
