package sc.common.view.backing;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.Set;

import org.apache.commons.digester.Digester;

import sc.common.biReport.PublicReportService;
import sc.common.view.bean.CycleCountSeqDetailBean;
import sc.common.view.bean.CycleCountSeqDetailMasterBean;
import sc.common.view.bean.ReportDataBean;
import sc.common.view.util.Constants;
import sc.common.view.util.ExceptionLog;
import sc.common.view.util.Util;

public class CycleCountSeqDetIntegration {
    HashMap<String, String> setupMap = null;
    ReportDataBean bean=null;
    Util util=null;
    PublicReportService port=null;
    Connection con = null;
    
    String selectStmtHeaderMaster = "SELECT * FROM WH360_CC_HEADER_MASTER WHERE CC_HEADER_ID=?";
    String selectStmtLineMaster = "SELECT * FROM WH360_CC_LINES_MASTER WHERE CC_HEADER_ID=? AND CC_CYCLE_COUNT_SEQUENCE_NO=?";

    String statemetHMUp = "UPDATE WH360_CC_HEADER_MASTER SET CC_NAME = ?,CC_ORG_ID =? WHERE CC_HEADER_ID = ?";
    String statemetHMIn = "INSERT INTO WH360_CC_HEADER_MASTER(CC_HEADER_ID,CC_NAME,CC_ORG_ID) VALUES (?,?,?)";

    String statemetLMUp = "UPDATE WH360_CC_LINES_MASTER SET CC_ITEM_DESC=?, CC_ITEM_NO=?, CC_SUB_INV=?, CC_LOCATOR=?, CC_LOT=?, CC_SERIAL=?, CC_SYSTEM_QTY=?, CC_RETAIL_P=?, CC_UNIT_COST=?, CC_LOT_EXP_DATE=?, CYCLE_COUNT_ENTRY_ID=?, CC_UOM=?, CC_LOT_ORIGINATION_DATE=?,CYCLE_COUNT_HEADER_NAME=?, ORGANIZATION_CODE=?, INVENTORY_ITEM_ID=?, ORGANIZATION_ID=?, BUSINESS_UNIT_ID=?, COST_ID=?, RETAIL_PRICE_ID=?, CROSS_REFERENCE = ?, CC_LOCATOR_ID = ? WHERE CC_CYCLE_COUNT_SEQUENCE_NO=? AND CC_HEADER_ID=?";
    String statemetLMIn = "INSERT INTO WH360_CC_LINES_MASTER(CC_LINE_ID,CC_CYCLE_COUNT_SEQUENCE_NO, CC_HEADER_ID,CC_ITEM_DESC,CC_ITEM_NO,CC_SUB_INV,CC_LOCATOR,CC_LOT,CC_SERIAL, CC_SYSTEM_QTY, CC_RETAIL_P, CC_UNIT_COST, CC_LOT_EXP_DATE, CYCLE_COUNT_ENTRY_ID,CC_UOM,CC_LOT_ORIGINATION_DATE,CYCLE_COUNT_HEADER_NAME,ORGANIZATION_CODE,INVENTORY_ITEM_ID,ORGANIZATION_ID,BUSINESS_UNIT_ID,COST_ID,RETAIL_PRICE_ID,CROSS_REFERENCE,CC_LOCATOR_ID) VALUES ("+Constants.WH360_CC_LINES_MASTER_SEQ+".nextVal,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    HashMap<String,Integer> map1 = new HashMap<>();
    HashMap<String,Integer> map2 = new HashMap<>();
    
    Set<String> headerSet = null;
    Set<String> LineSet = null;
    
    String selectHeaderSetQuery = "SELECT distinct CC_HEADER_ID FROM WH360_CC_HEADER_MASTER";
    String selectLineSetQuery = "SELECT CC_HEADER_ID || '-' || CC_CYCLE_COUNT_SEQUENCE_NO FROM WH360_CC_LINES_MASTER group by CC_HEADER_ID,CC_CYCLE_COUNT_SEQUENCE_NO ";
    
    public CycleCountSeqDetIntegration(ReportDataBean bean, PublicReportService port, HashMap<String, String> setupMap, Connection con) {
        super();
        this.util = new Util();
        this.bean = bean;
        this.port = port;
        this.setupMap = setupMap;        
        this.con = con;
    }
    
//    public CycleCountSeqDetIntegration( PublicReportService port, HashMap<String, String> setupMap, Connection con) {
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
//            CycleCountSeqDetIntegration cc = new CycleCountSeqDetIntegration(port,setupMap,con);
//            cc.executeCycleCountSeqDetIntegration();
//            
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }
    
    public double executeCycleCountSeqDetIntegration() throws Exception {
        double recordProcessed=0;
        InputStream is = null;
        try{
            byte[] reportData = util.runBIReport(bean,setupMap,port);
            recordProcessed = parseCustomerReport(reportData, con);
            util.updateSchedulerDate(Constants.SERVICE_TYPE_CYCLE_COUNT_SEQUENCE_DETAIL);
        }catch(Exception e){
            e.printStackTrace();
            ExceptionLog.CreateExceptionLog(this.getClass().getName(),"executeCycleCountSeqDetIntegration",e,"");  
            throw e;
        }finally{
            if(is != null)
                is.close();
        }
        return recordProcessed;
    }
    
    
    public int parseCustomerReport(byte[] bytes, Connection cn) throws Exception {
        int processedRecords = 0;
        InputStream reportStream = null;
        try {
            Digester digester = new Digester();
            digester.setValidating(false);
            digester.setUseContextClassLoader(true);
            digester.addObjectCreate("DATA_DS", CycleCountSeqDetailMasterBean.class);
            digester.addObjectCreate("DATA_DS/G_1", CycleCountSeqDetailBean.class);
            digester.addBeanPropertySetter("DATA_DS/G_1/CYCLE_COUNT_HEADER_NAME", "CYCLE_COUNT_HEADER_NAME");
            digester.addBeanPropertySetter("DATA_DS/G_1/CYCLE_COUNT_HEADER_ID", "CYCLE_COUNT_HEADER_ID");
            digester.addBeanPropertySetter("DATA_DS/G_1/EXPIRATION_DATE", "EXPIRATION_DATE");
            digester.addBeanPropertySetter("DATA_DS/G_1/ORGANIZATION_CODE", "ORGANIZATION_CODE");
            digester.addBeanPropertySetter("DATA_DS/G_1/SEQUENCE_NUMBER", "SEQUENCE_NUMBER");
            digester.addBeanPropertySetter("DATA_DS/G_1/CYCLE_COUNT_ENTRY_ID", "CYCLE_COUNT_ENTRY_ID");
            digester.addBeanPropertySetter("DATA_DS/G_1/ITEM_CODE", "ITEM_CODE");
            digester.addBeanPropertySetter("DATA_DS/G_1/ITEM_DESCRIPTION", "ITEM_DESCRIPTION");
            digester.addBeanPropertySetter("DATA_DS/G_1/SUBINVENTORY", "SUBINVENTORY");
            digester.addBeanPropertySetter("DATA_DS/G_1/LOCATOR", "LOCATOR");
            digester.addBeanPropertySetter("DATA_DS/G_1/LOTNUMBER", "LOTNUMBER");
            digester.addBeanPropertySetter("DATA_DS/G_1/LOT_EXPIRATION_DATE", "LOT_EXPIRATION_DATE");
            digester.addBeanPropertySetter("DATA_DS/G_1/SERIAL_NUMBER", "SERIAL_NUMBER");
            digester.addBeanPropertySetter("DATA_DS/G_1/ON_HAND_QUANTITY", "ON_HAND_QUANTITY");
            digester.addBeanPropertySetter("DATA_DS/G_1/ITEM_COST", "ITEM_COST");
            digester.addBeanPropertySetter("DATA_DS/G_1/RETAIL_PRICE", "RETAIL_PRICE");
            digester.addBeanPropertySetter("DATA_DS/G_1/COUNTUOMCODE", "COUNTUOMCODE");
            digester.addBeanPropertySetter("DATA_DS/G_1/LOTORIGINATIONDATE", "LOTORIGINATIONDATE");
            digester.addBeanPropertySetter("DATA_DS/G_1/INVENTORY_ITEM_ID", "INVENTORY_ITEM_ID");
            digester.addBeanPropertySetter("DATA_DS/G_1/ORGANIZATION_ID", "ORGANIZATION_ID");
            digester.addBeanPropertySetter("DATA_DS/G_1/BUSINESS_UNIT_ID", "BUSINESS_UNIT_ID");
            digester.addBeanPropertySetter("DATA_DS/G_1/COST_ID", "COST_ID");
            digester.addBeanPropertySetter("DATA_DS/G_1/RETAIL_PRICE_ID", "RETAIL_PRICE_ID");
            digester.addBeanPropertySetter("DATA_DS/G_1/CF_SYSQTY", "CF_SYSQTY");
            digester.addBeanPropertySetter("DATA_DS/G_1/CROSS_REFERENCE", "CROSS_REFERENCE");
            digester.addBeanPropertySetter("DATA_DS/G_1/LOCATOR_ID", "LOCATOR_ID");

            digester.addSetNext("DATA_DS/G_1", "setCycleCountBean");
            reportStream = new ByteArrayInputStream(bytes);
            CycleCountSeqDetailMasterBean cycleCountMasterBean = (CycleCountSeqDetailMasterBean) digester.parse(reportStream);
            

            
            if (cycleCountMasterBean != null && cycleCountMasterBean.getCycleCountList() != null &&
                cycleCountMasterBean.getCycleCountList().size() > 0) {
                
                headerSet = util.getExIdSet(con, selectHeaderSetQuery);
                LineSet = util.getExIdSet(con, selectLineSetQuery);
                
                try(PreparedStatement statementHdrInsert = cn.prepareStatement(statemetHMIn);
                    PreparedStatement statementHdrUpdate = cn.prepareStatement(statemetHMUp);
                    PreparedStatement statementLineInsert = cn.prepareStatement(statemetLMIn);
                    PreparedStatement statementLineUpdate = cn.prepareStatement(statemetLMUp);){
                    
                    for (int i = 0; i < cycleCountMasterBean.getCycleCountList().size(); i++) {
                        CycleCountSeqDetailBean cycleCountBean = cycleCountMasterBean.getCycleCountList().get(i);
                        if (cycleCountBean != null && cycleCountBean.getCYCLE_COUNT_HEADER_NAME() != null) {
                            insertUpdateCustomer(cycleCountBean, cn,statementHdrInsert,statementHdrUpdate,
                                                 statementLineInsert,statementLineUpdate);
                        }
                    }
                    
                    int[] hdrInsert = statementHdrInsert.executeBatch();
                    int[] LineInsert = statementLineInsert.executeBatch();
                    int[] hdrUpdate = statementHdrUpdate.executeBatch();
                    int[] LineUpdate = statementLineUpdate.executeBatch();
                    cn.commit();
                    
//                    System.out.println("The number of rows at Header Inserted : " + hdrInsert.length);
//                    System.out.println("The number of rows at Line Inserted : " + LineInsert.length);
//                    System.out.println("The number of rows at Header Updated : " + hdrUpdate.length);
//                    System.out.println("The number of rows at Line Update : " + LineUpdate.length);
                    
                    processedRecords = hdrInsert.length + LineInsert.length + hdrUpdate.length + LineUpdate.length;
                }
                
            }

        } catch (Exception e) {
            e.printStackTrace();
            cn.rollback();
            throw e;
        } finally {
            if (reportStream != null) {
                reportStream.close();
            }
        }
        return processedRecords;
    }
    
    
    public void insertUpdateCustomer(CycleCountSeqDetailBean cycleCountBean, Connection cn,
                                     PreparedStatement statementHdrInsert,PreparedStatement statementHdrUpdate,
                                     PreparedStatement statementLineInsert, PreparedStatement statementLineUpdate) throws Exception {
        
        if(headerSet.contains(cycleCountBean.getCYCLE_COUNT_HEADER_ID())){
            setUpdateHMParameters(statementHdrUpdate, cycleCountBean);
            statementHdrUpdate.addBatch();
        } else{
            headerSet.add(cycleCountBean.getCYCLE_COUNT_HEADER_ID());
            setInsertHMParameters(statementHdrInsert, cycleCountBean);
            statementHdrInsert.addBatch();
        }
        
        
        if (LineSet.contains(cycleCountBean.getCYCLE_COUNT_HEADER_ID().concat(Constants.HYPHEN).concat(cycleCountBean.getSEQUENCE_NUMBER()))) {
            setUpdateLMParameters(statementLineUpdate, cycleCountBean);
            statementLineUpdate.addBatch();
        } else {
            LineSet.add(cycleCountBean.getCYCLE_COUNT_HEADER_ID().concat(Constants.HYPHEN).concat(cycleCountBean.getSEQUENCE_NUMBER()));
            setInsertLMParameters(statementLineInsert, cycleCountBean);
            statementLineInsert.addBatch();
        }
    }

    public void setUpdateHMParameters(PreparedStatement psUp, CycleCountSeqDetailBean cycleCountBean) throws SQLException {
        psUp.setString(1, cycleCountBean.getCYCLE_COUNT_HEADER_NAME());
        psUp.setString(2, cycleCountBean.getORGANIZATION_CODE());
        psUp.setString(3, cycleCountBean.getCYCLE_COUNT_HEADER_ID());

    }

    public void setInsertHMParameters(PreparedStatement psUp, CycleCountSeqDetailBean cycleCountBean) throws SQLException {
        psUp.setString(1, cycleCountBean.getCYCLE_COUNT_HEADER_ID());
        psUp.setString(2, cycleCountBean.getCYCLE_COUNT_HEADER_NAME());
        psUp.setString(3, cycleCountBean.getORGANIZATION_CODE());
    }

    public void setUpdateLMParameters(PreparedStatement psIn, CycleCountSeqDetailBean cycleCountBean) throws SQLException {
        psIn.setString(1, cycleCountBean.getITEM_DESCRIPTION());
        psIn.setString(2, cycleCountBean.getITEM_CODE());
        psIn.setString(3, cycleCountBean.getSUBINVENTORY());

        psIn.setString(4, cycleCountBean.getLOCATOR());
        psIn.setString(5, cycleCountBean.getLOTNUMBER());
        psIn.setString(6, cycleCountBean.getSERIAL_NUMBER());
        psIn.setString(7, cycleCountBean.getCF_SYSQTY());

        psIn.setString(8, cycleCountBean.getRETAIL_PRICE());
        psIn.setString(9, cycleCountBean.getITEM_COST());
        psIn.setString(10, cycleCountBean.getLOT_EXPIRATION_DATE());
        psIn.setString(11, cycleCountBean.getCYCLE_COUNT_ENTRY_ID());

        psIn.setString(12, cycleCountBean.getCOUNTUOMCODE());
        psIn.setString(13, cycleCountBean.getLOTORIGINATIONDATE());

        psIn.setString(14, cycleCountBean.getCYCLE_COUNT_HEADER_NAME());
        psIn.setString(15, cycleCountBean.getORGANIZATION_CODE());
        psIn.setString(16, cycleCountBean.getINVENTORY_ITEM_ID());
        psIn.setString(17, cycleCountBean.getORGANIZATION_ID());
        psIn.setString(18, cycleCountBean.getBUSINESS_UNIT_ID());
        psIn.setString(19, cycleCountBean.getCOST_ID());
        psIn.setString(20, cycleCountBean.getRETAIL_PRICE_ID());
        psIn.setString(21, cycleCountBean.getCROSS_REFERENCE());
        psIn.setString(22, cycleCountBean.getLOCATOR_ID());
        
        psIn.setString(23, cycleCountBean.getSEQUENCE_NUMBER());
        psIn.setString(24, cycleCountBean.getCYCLE_COUNT_HEADER_ID());

    }

    public void setInsertLMParameters(PreparedStatement psIn, CycleCountSeqDetailBean cycleCountBean) throws SQLException {
        psIn.setString(1, cycleCountBean.getSEQUENCE_NUMBER());
        psIn.setString(2, cycleCountBean.getCYCLE_COUNT_HEADER_ID());
        psIn.setString(3, cycleCountBean.getITEM_DESCRIPTION());
        psIn.setString(4, cycleCountBean.getITEM_CODE());

        psIn.setString(5, cycleCountBean.getSUBINVENTORY());

        psIn.setString(6, cycleCountBean.getLOCATOR());
        psIn.setString(7, cycleCountBean.getLOTNUMBER());
        psIn.setString(8, cycleCountBean.getSERIAL_NUMBER());
        psIn.setString(9, cycleCountBean.getCF_SYSQTY());
        psIn.setString(10, cycleCountBean.getRETAIL_PRICE());
        psIn.setString(11, cycleCountBean.getITEM_COST());
        psIn.setString(12, cycleCountBean.getLOT_EXPIRATION_DATE());
        psIn.setString(13, cycleCountBean.getCYCLE_COUNT_ENTRY_ID());
        psIn.setString(14, cycleCountBean.getCOUNTUOMCODE());
        psIn.setString(15, cycleCountBean.getLOTORIGINATIONDATE());
        psIn.setString(16, cycleCountBean.getCYCLE_COUNT_HEADER_NAME());
        psIn.setString(17, cycleCountBean.getORGANIZATION_CODE());
        psIn.setString(18, cycleCountBean.getINVENTORY_ITEM_ID());
        psIn.setString(19, cycleCountBean.getORGANIZATION_ID());
        psIn.setString(20, cycleCountBean.getBUSINESS_UNIT_ID());
        psIn.setString(21, cycleCountBean.getCOST_ID());
        psIn.setString(22, cycleCountBean.getRETAIL_PRICE_ID());
        psIn.setString(23, cycleCountBean.getCROSS_REFERENCE());
        psIn.setString(24, cycleCountBean.getLOCATOR_ID());

    }
    
    
}
