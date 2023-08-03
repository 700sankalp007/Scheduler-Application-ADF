package sc.common.view.backing;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.math.BigDecimal;

import java.sql.Connection;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sc.common.biReport.PublicReportService;
import sc.common.view.bean.ReportDataBean;
import sc.common.view.util.Constants;
import sc.common.view.util.DML;
import sc.common.view.util.ExceptionLog;
import sc.common.view.util.Util;

public class ItemStructureIntegration {
    
        HashMap<String,Integer> map = new HashMap<>();
    HashMap<String, String> setupMap = null;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static final Set<String> updateItemStructureSet = new HashSet<String>(Arrays.asList("PARENT_ITEM_NUMBER_KEY", "COMPONENT_ITEM_ID", "COMPONENT_LEVEL", "PARENT_END_FLAG", "OBJECT_VERSION_NUMBER"));
    private static final String SCM_ITEM_STRUCTURE_DTL = "SCM_ITEM_STRUCTURE_DTL";
    private List<Map<String, Object>> itemStructureMapList = null;
    Map<String, Object> itemStructureMap = null;
    ReportDataBean bean=null;
    Util util=null;
    PublicReportService port=null;
    Connection con = null;
    
    public ItemStructureIntegration(ReportDataBean bean, PublicReportService port, HashMap<String, String> setupMap, Connection con) {

        super();
        this.util = new Util();
        this.bean = bean;
        this.port = port;
        this.setupMap = setupMap;        
        this.con = con;
    }
    
    public int executeItemStructureIntegration() throws Exception {
       
        int recordCount = 0;
        try{
            
            this.readItemStructure();
            if(itemStructureMapList!=null
               && !itemStructureMapList.isEmpty()){
                
                this.writeItemStructure();
                recordCount = itemStructureMapList.size();
            }
            util.updateSchedulerDate(Constants.SERVICE_TYPE_CUTL_ITEM_STRUCTURE);
        }catch(Exception e){
            e.printStackTrace();
            ExceptionLog.CreateExceptionLog(this.getClass().getName(), "execute", e, null);
            throw e;
        }
        return recordCount;
    }
    
    
    public void readItemStructure() throws Exception {
        
        byte[] reportData = util.runBIReport(bean,setupMap,port);
        try (      InputStream is = new ByteArrayInputStream(reportData);
                   BufferedReader reader = new BufferedReader(new InputStreamReader(is,Constants.UTF8));){

                   String line;
                   int lineNum = 0;
                   while ((line = reader.readLine()) != null) {
                       
                       lineNum ++;
                       String csv[] = util.splitLine(line, bean.getDelimiter());
                       if(lineNum==1){
                           
                           csv[0] = csv[0].substring(1);
                           for(int i = 0; i<csv.length; i++)
                               map.put(csv[i],i);
                       }else{
                           
                           itemStructureMap = new HashMap<>();
                           itemStructureMap.put("COMPONENT_ITEM_TYPE", csv[map.get("COMPONENT_ITEM_TYPE")]);
                           itemStructureMap.put("PATH", csv[map.get("PATH")]);
                           itemStructureMap.put("COMPONENT_ITEM_NAME", csv[map.get("COMPONENT_ITEM_NAME")]);
                           itemStructureMap.put("PARENT_UOM_NAME", csv[map.get("PARENT_UOM_NAME")]);
                           itemStructureMap.put("COMPONENT_UOM_NAME", csv[map.get("COMPONENT_UOM_NAME")]);                           
                           itemStructureMap.put("PARENT_ITEM_NUMBER_KEY", csv[map.get("PARENT_ITEM_NUMBER_KEY")]);
                           itemStructureMap.put("COMPONENT_ITEM_ID", csv[map.get("COMPONENT_ITEM_ID")]);
                           itemStructureMap.put("UOM", csv[map.get("UOM")]);
                           itemStructureMap.put("SUPPLY_TYPE", csv[map.get("SUPPLY_TYPE")]);
                           itemStructureMap.put("WIP_SUPPLY_TYPE", csv[map.get("WIP_SUPPLY_TYPE")]);
                           itemStructureMap.put("COMPONENT_ITEM_DESC", csv[map.get("COMPONENT_ITEM_DESC")]);
                           itemStructureMap.put("STRUCTURE_TYPE_NAME", csv[map.get("STRUCTURE_TYPE_NAME")]);
                           itemStructureMap.put("STRUCTURE_TYPE_ID", csv[map.get("STRUCTURE_TYPE_ID")]);
                           itemStructureMap.put("STRUCTURE_NAME", csv[map.get("STRUCTURE_NAME")]);
                           itemStructureMap.put("ITEM_TYPE", csv[map.get("ITEM_TYPE")]);
                           itemStructureMap.put("PARENT_ITEM_REV", csv[map.get("PARENT_ITEM_REV")]);
                           itemStructureMap.put("IMM_PARENT_ITEM_UOM", csv[map.get("IMM_PARENT_ITEM_UOM")]);
                           itemStructureMap.put("IMM_PARENT_ITEM_ID", csv[map.get("IMM_PARENT_ITEM_ID")]);
                           itemStructureMap.put("IMM_PARENT_ITEM_DESC", csv[map.get("IMM_PARENT_ITEM_DESC")]);
                           itemStructureMap.put("IMM_PARENT_ITEM_NUMBER", csv[map.get("IMM_PARENT_ITEM_NUMBER")]);
                           itemStructureMap.put("COMPONENT_LEVEL", csv[map.get("COMPONENT_LEVEL")]);
                           itemStructureMap.put("COMPONENT_SEQUENCE_ID", csv[map.get("COMPONENT_SEQUENCE_ID")]);
                           itemStructureMap.put("BILL_SEQUENCE_ID", csv[map.get("BILL_SEQUENCE_ID")]);
                           itemStructureMap.put("ORGANIZATION_CODE", csv[map.get("ORGANIZATION_CODE")]);
                           itemStructureMap.put("ORGANIZATION_ID", csv[map.get("ORGANIZATION_ID")]);
                           itemStructureMap.put("MAIN_ITEM_NUMBER", csv[map.get("MAIN_ITEM_NUMBER")]);
                           itemStructureMap.put("PARENT_END_FLAG", csv[map.get("PARENT_END_FLAG")]);                           
                           itemStructureMap.put("COMPONENT_QUANTITY", csv[map.get("COMPONENT_QUANTITY")]!=null ? new BigDecimal(csv[map.get("COMPONENT_QUANTITY")]) : null);
                           itemStructureMap.put("COMPONENT_SEQUENCE", csv[map.get("COMPONENT_SEQUENCE")]!=null ? new BigDecimal(csv[map.get("COMPONENT_SEQUENCE")]) : null);
                           itemStructureMap.put("COMPONENT_REVISION", csv[map.get("COMPONENT_REVISION")]!=null ? new BigDecimal(csv[map.get("COMPONENT_REVISION")]) : null);
                           itemStructureMap.put("OBJECT_VERSION_NUMBER", csv[map.get("OBJECT_VERSION_NUMBER")]!=null ? new BigDecimal(csv[map.get("OBJECT_VERSION_NUMBER")]) : null);
                           itemStructureMap.put("MAIN_ITEM_ID", csv[map.get("MAIN_ITEM_ID")]!=null ? new BigDecimal(csv[map.get("MAIN_ITEM_ID")]) : null);
                           itemStructureMap.put("PARENT_INDENTED_QTY", csv[map.get("PARENT_INDENTED_QTY")]!=null ? new BigDecimal(csv[map.get("PARENT_INDENTED_QTY")]) : null);
                           itemStructureMap.put("COMP_INDENTED_QTY", csv[map.get("COMP_INDENTED_QTY")]!=null ? new BigDecimal(csv[map.get("COMP_INDENTED_QTY")]) : null);
                           itemStructureMap.put("START_DATE", csv[map.get("START_DATE")]!=null ? new java.sql.Date(sdf.parse(csv[map.get("START_DATE")]).getTime()) : null);
                           itemStructureMap.put("END_DATE", csv[map.get("END_DATE")]!=null ? new java.sql.Date(sdf.parse(csv[map.get("END_DATE")]).getTime()) : null);                           
                           if(itemStructureMapList==null) itemStructureMapList = new  ArrayList<>();
                           itemStructureMapList.add(itemStructureMap);
                       }
                   }
        }
    }
    
    public void writeItemStructure() throws Exception {
        
        DML dml = new DML(con);
        dml.processData(SCM_ITEM_STRUCTURE_DTL, itemStructureMapList, updateItemStructureSet, null, null);
        con.commit();
    }
}
