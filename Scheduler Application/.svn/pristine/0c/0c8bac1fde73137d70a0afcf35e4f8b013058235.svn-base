package sc.common.view.backing;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import sc.common.biReport.PublicReportService;
import sc.common.view.bean.ExchangeRateBean;
import sc.common.view.bean.ReportDataBean;
import sc.common.view.util.Constants;
import sc.common.view.util.ExceptionLog;
import sc.common.view.util.Util;

public class ExchangeRateIntegration {

    private static final String exRateQuery = "SELECT DISTINCT CONVERSION_TYPE || '-' || FROM_CURRENCY || '-' || TO_CURRENCY || '-' || TO_CHAR(CONVERSION_DATE,'YYYY-MM-DD') FROM EEI_EXCHANGE_RATE_MASTER_T";
    HashMap<String,Integer> map1 = new HashMap<>();
    HashMap<String, String> setupMap = null;
    ReportDataBean bean=null;
    Util util=null;
    PublicReportService port=null;
    Connection con = null;
    Set<String> exRateSet = null;

    public ExchangeRateIntegration(ReportDataBean bean, PublicReportService port, HashMap<String, String> setupMap, Connection con) {
        super();
        this.util = new Util();
        this.bean = bean;
        this.port = port;
        this.setupMap = setupMap;        
        this.con = con;
    }
    
    public int executeExchangeRateIntegration() throws Exception {
        
        int recordCount = 0;
        try{
            
            ArrayList<ExchangeRateBean> exchangeRateBeanAL = this.readExchangeRateReport();
            if(exchangeRateBeanAL!=null
               && !exchangeRateBeanAL.isEmpty()){
                
               this.writeExchangeRate(exchangeRateBeanAL);
               recordCount = exchangeRateBeanAL.size();               
            }
            util.updateSchedulerDate(Constants.SERVICE_TYPE_EXCHANGE_RATE);
        }catch(Exception e){
            e.printStackTrace();
            ExceptionLog.CreateExceptionLog(this.getClass().getName(), "execute", e, null);
            throw e;
        }
        return recordCount;
    }

    public ArrayList<ExchangeRateBean> readExchangeRateReport() throws Exception {
        
        ArrayList<ExchangeRateBean> exchangeRateBeanAL = new ArrayList<ExchangeRateBean>(); 
        
        byte[] report = util.runBIReport(bean,setupMap,port);
//        System.out.println(new String(report));
        try(InputStream is = new ByteArrayInputStream(report);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, Constants.UTF8));) {
                   
                   String line;
                   int lineNum = 0;
                   while ((line = reader.readLine()) != null) {
                       lineNum ++;
                       String csv[] = util.splitLine(line, bean.getDelimiter());
                       if(lineNum==1){
                           csv[0] = csv[0].substring(1);
                           for(int i = 0; i<csv.length; i++){
                               map1.put(csv[i],i);            
                           }
                       }else{                           
                                                          
                           ExchangeRateBean erb = new ExchangeRateBean(csv[map1.get("CONVERSION_TYPE")],
                                                                       csv[map1.get("FROM_CURRENCY")],
                                                                       csv[map1.get("TO_CURRENCY")],
                                                                       csv[map1.get("CONVERSION_DATE")],
                                                                       csv[map1.get("CONVERSION_RATE")]);
                           exchangeRateBeanAL.add(erb);                               
                       }
                   }             
               }
        return exchangeRateBeanAL;
    }

    public void writeExchangeRate(ArrayList<ExchangeRateBean> exchangeRateBeanAL) throws SQLException {
        
        StringBuffer inserQuery = new StringBuffer("INSERT INTO EEI_EXCHANGE_RATE_MASTER_T");
        inserQuery.append("(CONVERSION_RATE,");
        inserQuery.append("CONVERSION_TYPE,");
        inserQuery.append("FROM_CURRENCY,");
        inserQuery.append("TO_CURRENCY,");
        inserQuery.append("CONVERSION_DATE)");          
        inserQuery.append(" VALUES(");
        for(int i=0;i<4;i++){
            inserQuery.append("?,");
        }
        inserQuery.append("TO_DATE(?,'YYYY-MM-DD'))");
        
        StringBuffer updateQuery = new StringBuffer("UPDATE EEI_EXCHANGE_RATE_MASTER_T");
        updateQuery.append(" SET CONVERSION_RATE=?");
        updateQuery.append(" WHERE CONVERSION_TYPE=?");
        updateQuery.append(" AND FROM_CURRENCY=?");
        updateQuery.append(" AND TO_CURRENCY=?");
        updateQuery.append(" AND CONVERSION_DATE = TO_DATE(?,'YYYY-MM-DD')");
        
        try(PreparedStatement insertPs = con.prepareStatement(inserQuery.toString());
            PreparedStatement updatePs = con.prepareStatement(updateQuery.toString());){
            
            exRateSet = util.getExIdSet(con,exRateQuery);
            for(ExchangeRateBean exchangeRateBean : exchangeRateBeanAL){
                                       
                StringBuffer rate = new StringBuffer(exchangeRateBean.getConversionType()+Constants.HYPHEN+exchangeRateBean.getFromCurrency()+Constants.HYPHEN+exchangeRateBean.getToCurrency()+Constants.HYPHEN+exchangeRateBean.getConversionDate()); 
                if(exRateSet.contains(rate.toString())){
                    this.exchangeRateTable(exchangeRateBean, updatePs);
                }else{
                    this.exchangeRateTable(exchangeRateBean, insertPs);
                    exRateSet.add(rate.toString());
                }                
        }
        insertPs.executeBatch();    
        updatePs.executeBatch();
        con.commit();
        }                                
    }

    
    public void exchangeRateTable(ExchangeRateBean exchangeRateBean, PreparedStatement ps) throws SQLException {
        ps.setString(1,exchangeRateBean.getConversionRate());
        ps.setString(2,exchangeRateBean.getConversionType());
        ps.setString(3,exchangeRateBean.getFromCurrency());
        ps.setString(4,exchangeRateBean.getToCurrency());
        ps.setString(5,exchangeRateBean.getConversionDate());        
        ps.addBatch(); 
    }

}
