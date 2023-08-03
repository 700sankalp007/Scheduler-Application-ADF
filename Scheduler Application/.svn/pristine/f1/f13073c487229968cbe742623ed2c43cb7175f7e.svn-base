package sc.common.view.backing;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sc.common.biReport.PublicReportService;
import sc.common.view.bean.ReportDataBean;
import sc.common.view.bean.ReportSetupDetailBean;
import sc.common.view.util.ConnectionManager;
import sc.common.view.util.Constants;
import sc.common.view.util.ExceptionLog;
import sc.common.view.util.Util;

public class CycleCountItemCostIntegration {
    HashMap<String, String> setupMap = null;
    ReportDataBean bean=null;
    Util util=null;
    PublicReportService port=null;
    Connection con = null;
    HashMap<String,Integer> map1 = new HashMap<>();
    HashMap<String,Integer> map2 = new HashMap<>();
    
    public CycleCountItemCostIntegration(ReportDataBean bean, PublicReportService port, HashMap<String, String> setupMap, Connection con) {
        super();
        this.util = new Util();
        this.bean = bean;
        this.port = port;
        this.setupMap = setupMap;        
        this.con = con;
    }
    
//    public CycleCountItemCostIntegration( PublicReportService port, HashMap<String, String> setupMap, Connection con) {
//        super();
//        this.util = new Util();
//        this.port = port;
//        this.setupMap = setupMap;        
//        this.con = con;
//    }
//    
//    public static void main(String[] args){
//        try{
//            Connection con=ConnectionManager.getConnectionX(); 
//            HashMap<String, String> setupMap= Util.getSetupDetails(con, Constants.SETUP_TYPE_WEBSEVICE, Constants.SUB_TYPE_SCM);
//            PublicReportService port =ServiceBean.getPublicReportServicePort(setupMap.get("HOST"));
//            CycleCountItemCostIntegration cc = new CycleCountItemCostIntegration(port,setupMap,con);
//            cc.executeCycleCountItemCostIntegration();
//            
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }
    
    public double executeCycleCountItemCostIntegration() throws Exception {
        double recordProcessed=0;
        InputStream is = null;
        try{
            ArrayList<String> listOfCostOrgCode = getCostOrgCode();
            for(int i=0; i <listOfCostOrgCode.size(); i++){
                byte[] reportData = util.runBIReport(bean,setupMap,port);
                is = new ByteArrayInputStream(reportData);
                if(reportData.length > 0 && bean.getDataFormat().equalsIgnoreCase("xml")){
                    List<ReportSetupDetailBean> setupDtlBeanList = bean.getReportSetupDetail();
                    ReportSetupDetailBean setupDtlBean = setupDtlBeanList.get(0);  // because there is only 1 parameter
                    setupDtlBean.setDefualtVal(listOfCostOrgCode.get(i));
                    setupDtlBean.setSqlStatement("select '"+listOfCostOrgCode.get(i)+"' from dual");
                    recordProcessed = recordProcessed + util.processReport(con,bean,port,setupMap);
                }
            }
            util.updateSchedulerDate(Constants.SERVICE_TYPE_CYCLE_COUNT_ITEM_COST);
        }catch(Exception e){
            e.printStackTrace();
            ExceptionLog.CreateExceptionLog(this.getClass().getName(),"executeCycleCountIntegration",e,"");  
            throw e;
        }finally{
            if(is != null)
                is.close();
        }
        return recordProcessed;
    }
    
    
    public ArrayList<String> getCostOrgCode() throws SQLException {
        
        ArrayList<String> listOfCostOrgCode = new ArrayList<String>();
        
        final String query = "SELECT DISTINCT(COST_ORG_CODE) FROM WH360_CC_COST_ORG_CODE";                
        try(Connection con = ConnectionManager.getConnection();
            PreparedStatement ps=con.prepareStatement(query);){

            try(ResultSet rs=ps.executeQuery();){
                
                while(rs.next()){
                    listOfCostOrgCode.add(rs.getString("COST_ORG_CODE"));
                }
            }            
        }      
        return listOfCostOrgCode;
    }
}
