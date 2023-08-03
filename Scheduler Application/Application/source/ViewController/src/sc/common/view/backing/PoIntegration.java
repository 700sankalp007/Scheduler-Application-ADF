package sc.common.view.backing;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import sc.common.biReport.PublicReportService;
import sc.common.view.bean.PoBean;
import sc.common.view.bean.ReportDataBean;
import sc.common.view.util.Constants;
import sc.common.view.util.ExceptionLog;
import sc.common.view.util.Util;

public class PoIntegration {
    
    public static final String exPoQuery = "SELECT PO_ID FROM EEI_PO_MASTER_T";
    public static final String exProjectQuery = "SELECT PO_ID || '-' || PROJECT_ID || '-' || TASK_ID  FROM EEI_PROJECT_MASTER_T GROUP BY PO_ID, PROJECT_ID, TASK_ID";
    public static final String exPhaseQuery = "SELECT PO_ID || '-' || PAYMENT_PHASES FROM EEI_PHASES_MASTER_T GROUP BY PO_ID, PAYMENT_PHASES";
    public static final String exLineQuery = "SELECT LINE_ID || '-' || SCHEDULE_ID || '-' || DISTRIBUTION_ID FROM EEI_IPC_LINES_MASTER_T GROUP BY LINE_ID, SCHEDULE_ID, DISTRIBUTION_ID";
    String module="GL";
    String serviceName="COA Value Set Integration";
    String email=null;
    String filePath;    
    String serviceType;    
    String erpIntegrationWsdl=null;
    HashMap<String, String> setupMap = null;
    ArrayList<String> poIdAL = new ArrayList<String>();
    ArrayList<String> projectAL = new ArrayList<String>();
    ArrayList<String> phaseAL = new ArrayList<String>();
    ArrayList<String> lineAL = new ArrayList<String>();
    ArrayList<String> uPoIdAL = new ArrayList<String>();
    ArrayList<String> uProjectAL = new ArrayList<String>();
    ArrayList<String> uPhaseAL = new ArrayList<String>();
    ArrayList<String> uLineAL = new ArrayList<String>();
    Set<String> exProjectSet = null;
    Set<String> exPhaseSet = null;
    Set<String> exPoIdSet = null;
    Set<String> exLineSet = null;
    HashMap<String,Integer> map1 = new HashMap<>();
    ReportDataBean bean=null;
    Util util=null;
    PublicReportService port=null;
    Connection con = null;

    public PoIntegration(ReportDataBean bean, PublicReportService port, HashMap<String, String> setupMap, Connection con) {
        super();
        this.util = new Util();
        this.bean = bean;
        this.port = port;
        this.setupMap = setupMap;        
        this.con = con;
    }
    
    public int executePoIntegration() throws Exception {
        
        int recordCount = 0;
        try{
            
            ArrayList<PoBean> poBeanAL= this.poReport();
            if(poBeanAL!=null 
               && !poBeanAL.isEmpty()){
                
                   this.writePo(poBeanAL);
                   recordCount = poBeanAL.size();
               }
                
            util.updateSchedulerDate(Constants.SERVICE_TYPE_PO_DETAILS);
//            new ChangePOIntegration(setupMap, con).execute(null);            
        }catch(Exception e){
            e.printStackTrace();
            ExceptionLog.CreateExceptionLog(this.getClass().getName(), "execute", e, null);
            throw e;
        }
        return recordCount;
    }

    
    
    private ArrayList<PoBean> poReport() throws Exception {

        ArrayList<PoBean> poBeanAL = new ArrayList<PoBean>();
        byte[] reportData = util.runBIReport(bean,setupMap,port);
//         System.out.println(new String(reportData));
        
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
                               map1.put(csv[i],i);
//                           System.out.println("Map is="+map1);
                       }else{
                           
                           PoBean po = new PoBean();
                           po.setPoHeaderId(csv[map1.get("PO_HEADER_ID")]);
                           po.setPoNumber(csv[map1.get("PO_NUMBER")]);
                           po.setPoDate(csv[map1.get("PO_DATE")]);
                           po.setApprovedDate(csv[map1.get("APPROVED_DATE")]);
                           po.setStyle(csv[map1.get("STYLE")]);
                           po.setLedgerId(csv[map1.get("LEDGER_ID")]);
                           po.setBuId(csv[map1.get("PRC_BU_ID")]);
                           po.setProcBu(csv[map1.get("PROC_BU")]);
                           po.setReqBu(csv[map1.get("REQ_BU")]);
                           po.setSupplierId(csv[map1.get("SUPPLIER_ID")]);
                           po.setSupplier(csv[map1.get("SUPPLIER")]);
                           po.setSupplierNumber(csv[map1.get("SUPPLIER_NUM")]);
                           po.setSupplierSiteId(csv[map1.get("SUPPLIER_SITE_ID")]);
                           po.setSupplierSite(csv[map1.get("SUPPLIER_SITE")]);
                           po.setPoCurrency(csv[map1.get("PO_CURRENCY")]);
                           po.setBuyer(csv[map1.get("BUYER")]);
                           po.setDescription(csv[map1.get("DESCRIPTION")]);
                           po.setPaymentTerm(csv[map1.get("PAYMENT_TERM")]);
                           po.setPoStatus(csv[map1.get("PO_STATUS")]);
                           po.setPoLineId(csv[map1.get("PO_LINE_ID")]);
                           po.setLineNum(csv[map1.get("LINE_NUM")]);
                           po.setLineStatus(csv[map1.get("LINE_STATUS")]);
                           po.setLineType(csv[map1.get("LINE_TYPE")]);
                           po.setItemNumber(csv[map1.get("ITEM_NUMBER")]);
                           po.setItemDescription(csv[map1.get("ITEM_DESCRIPTION")]);
                           po.setCategoryName(csv[map1.get("CATEGORY_NAME")]);
                           po.setScheduleId(csv[map1.get("SCHEDULE_ID")]);
                           po.setScheduleNum(csv[map1.get("SCHEDULE_NUM")]);
                           po.setLocation(csv[map1.get("LOCATION")]);
                           po.setOrganization(csv[map1.get("ORGANIZATION")]);
                           po.setDistributionId(csv[map1.get("PO_DISTRIBUTION_ID")]);
                           po.setDistributionNum(csv[map1.get("DISTRIBUTION_NUM")]);
                           po.setSegment1(csv[map1.get("SEGMENT1")]);
                           po.setSegment2(csv[map1.get("SEGMENT2")]);
                           po.setSegment3(csv[map1.get("SEGMENT3")]);
                           po.setSegment4(csv[map1.get("SEGMENT4")]);
                           po.setSegment5(csv[map1.get("SEGMENT5")]);
                           po.setSegment6(csv[map1.get("SEGMENT6")]);
                           po.setSegment7(csv[map1.get("SEGMENT7")]);
                           po.setSegment8(csv[map1.get("SEGMENT8")]);
                           po.setSegment9(csv[map1.get("SEGMENT9")]);
                           po.setSegment10(csv[map1.get("SEGMENT10")]);
                           po.setQty(csv[map1.get("QTY")]);
                           po.setUomCode(csv[map1.get("UOM_CODE")]);
                           po.setUnitPrice(csv[map1.get("UNIT_PRICE")]);
                           po.setOrderedAmount(csv[map1.get("ORDERED_AMT")]);
                           po.setExchangeRate(csv[map1.get("RATE")]);
                           po.setFuncOrderedAmount(csv[map1.get("FUNC_ORDERED_AMT")]);
                           po.setProjectId(csv[map1.get("PROJECT_ID")]);
                           po.setProjectNumber(csv[map1.get("PRJCT_NUM")]);
                           po.setProjectName(csv[map1.get("PROJECT_NAME")]);
                           po.setTaskId(csv[map1.get("TASK_ID")]);
                           po.setTaskNumber(csv[map1.get("TASK_NUMBER")]);
                           po.setTaskName(csv[map1.get("TASK_NAME")]);
                           po.setExpenditureItemDate(csv[map1.get("EXPENDITURE_ITEM_DATE")]);
                           po.setExpenditureTypeName(csv[map1.get("EXPENDITURE_TYPE_NAME")]);
                           po.setExpOrg(csv[map1.get("EXP_ORG")]);
                           po.setContext(csv[map1.get("CONTEXT")]);
                           po.setAttribute1(csv[map1.get("ATTRIBUTE1")]);
                           po.setRetention(csv[map1.get("RETENTION_RATE")]);
                           po.setRecoupment(csv[map1.get("RECOUPMENT_RATE")]);
                           po.setAccureOnReceiptFlag(csv[map1.get("ACCRUE_ON_RECEIPT_FLAG")]);
                           po.setContractEndDate(csv[map1.get("CONTRACT_END_DATE")]);
                           po.setInvoicedAmount(csv[map1.get("INVOICED_AMOUNT")]);
                           po.setInvoicedQuantity(csv[map1.get("INVOICED_QUANTITY")]);
                           po.setRemainingAmount(csv[map1.get("REMAINING_AMOUNT")]);
                           po.setRemainingQuantity(csv[map1.get("REMAINING_QUANTITY")]);
                           po.setMaterial(csv[map1.get("MATERIAL")]);
                           po.setInstallation(csv[map1.get("INSTALLATION")]);
                           po.setEngineering(csv[map1.get("ENGINEERING")]);
                           po.setTestingAndCommissioning(csv[map1.get("TESTING_AND_COMMISSIONING")]);
                           po.setHandOver(csv[map1.get("HANDOVER")]);
                           po.setOthers(csv[map1.get("OTHERS")]);
                           po.setPaymentPhases(csv[map1.get("PAYMENT_PHASES")]);
                           po.setApprovalLevel(csv[map1.get("APPROVAL_LEVEL")]);
                           po.setMatchOption(csv[map1.get("MATCH_OPTION")]);
                           poBeanAL.add(po);                               
                       }
                   }             
               }
        return poBeanAL;
    }


    private void writePo(ArrayList<PoBean> poBeanAL) throws Exception { 
                                                        
        try(      PreparedStatement poPs = con.prepareStatement(this.getInsertPoQuery());
                  PreparedStatement uPoPs = con.prepareStatement(this.getUpdatePoQuery());
                  PreparedStatement proPs = con.prepareStatement(this.getInsertProjectQuery());
                  PreparedStatement uProPs = con.prepareStatement(this.getUpdateProjectQuery());
                  PreparedStatement phsPs = con.prepareStatement(this.getInsertPhaseQuery());
                  PreparedStatement uPhsPs = con.prepareStatement(this.getUpdatePhaseQuery());
                  PreparedStatement linePs = con.prepareStatement(this.getInsertLineQuery());
                  PreparedStatement uLinePs = con.prepareStatement(this.getUpdateLineQuery());){
            
            exPoIdSet = util.getExIdSet(con, exPoQuery);
            exProjectSet = util.getExIdSet(con, exProjectQuery);
            exPhaseSet = util.getExIdSet(con, exPhaseQuery);
            exLineSet = util.getExIdSet(con, exLineQuery) ;            
            for(PoBean poBean : poBeanAL){            

                    if(exPoIdSet.contains(poBean.getPoKey())){
                        this.poTable(poBean, uPoPs);
                        
                        if(exProjectSet.contains(poBean.getProjectKey()))                            
                            this.projectTable(poBean, uProPs);                            
                        else{
                            
                            exProjectSet.add(poBean.getProjectKey());
                            this.projectTable(poBean, proPs);
                        }

                        if(exLineSet.contains(poBean.getLineKey()))
                            this.lineTable(poBean, uLinePs);
                        else{
                            
                            exLineSet.add(poBean.getLineKey());
                            this.lineTable(poBean, linePs);
                        }
                    }else{
    
                        exPoIdSet.add(poBean.getPoHeaderId());
                        exProjectSet.add(poBean.getProjectKey());
                        exLineSet.add(poBean.getLineKey());
                        this.poTable(poBean, poPs); 
                        this.projectTable(poBean, proPs);
                        this.lineTable(poBean, linePs);
                    } 
                this.integratePhase(poBean, phsPs, uPhsPs);
            }
            
            poPs.executeBatch();
            uPoPs.executeBatch();
            proPs.executeBatch();
            uProPs.executeBatch();
            phsPs.executeBatch();
            uPhsPs.executeBatch();
            linePs.executeBatch();
            uLinePs.executeBatch();
            con.commit();
        }
    }
    
    
    private void poTable(PoBean poBean, PreparedStatement poPs) throws Exception {
        
        poPs.setString(1,poBean.getPoNumber());
        poPs.setString(2,poBean.getSupplierId());
        poPs.setString(3,poBean.getSupplier());
        poPs.setString(4,poBean.getSupplierSiteId());
        poPs.setString(5,poBean.getSupplierSite());
        poPs.setString(6,poBean.getProcBu());
        poPs.setString(7,poBean.getReqBu());
        poPs.setString(8,poBean.getStyle());
        poPs.setString(9,poBean.getPoDate());
        poPs.setString(10,poBean.getApprovedDate());
        poPs.setString(11,poBean.getBuyer());
        poPs.setString(12,poBean.getDescription());
        poPs.setString(13,poBean.getPaymentTerm());
        poPs.setString(14,poBean.getPoCurrency());
        poPs.setString(15,poBean.getAttribute1());
        poPs.setString(16,poBean.getRetention());
        poPs.setString(17,poBean.getRecoupment());
        poPs.setString(18,poBean.getPoStatus());
        poPs.setString(19,poBean.getContext());
        poPs.setString(20,poBean.getSupplierNumber());
        poPs.setString(21,poBean.getBuId());
        poPs.setString(22,poBean.getLedgerId());  
        poPs.setString(23,poBean.getContractEndDate());  
        poPs.setString(24,poBean.getProjectId());
        poPs.setString(25,poBean.getProjectNumber());
        poPs.setString(26,poBean.getPoHeaderId());     
        poPs.addBatch();                   
    }
    
    private void integratePhase(PoBean poBean, PreparedStatement phsPs, PreparedStatement uPhsPs) throws Exception {
    
        if(exPhaseSet.contains(poBean.getMaterialKey())){
            this.phaseTable(uPhsPs, Constants.MATERIAL,poBean.getMaterial(),poBean.getPoHeaderId());
          
        }else {
           this.phaseTable(phsPs, Constants.MATERIAL,poBean.getMaterial(),poBean.getPoHeaderId());
            exPhaseSet.add(poBean.getMaterialKey());
        }
        
        
        if(exPhaseSet.contains(poBean.getEngineeringKey())) {
            this.phaseTable(uPhsPs, Constants.ENGINEERING,poBean.getEngineering(),poBean.getPoHeaderId());
        }else {
            this.phaseTable(phsPs, Constants.ENGINEERING,poBean.getEngineering(),poBean.getPoHeaderId());
            exPhaseSet.add(poBean.getEngineeringKey());
        }
        
        
        if(exPhaseSet.contains(poBean.getInstallationKey())) {
            this.phaseTable(uPhsPs, Constants.INSTALLATION,poBean.getInstallation(),poBean.getPoHeaderId());
        }else {
            this.phaseTable(phsPs, Constants.INSTALLATION,poBean.getInstallation(),poBean.getPoHeaderId());
            exPhaseSet.add(poBean.getInstallationKey());
        }
        
        
        if(exPhaseSet.contains(poBean.gettestingAndCommissioningKey())) {
            this.phaseTable(uPhsPs, Constants.TESTING_AND_COMMISSIONING,poBean.getTestingAndCommissioning(),poBean.getPoHeaderId());
        }else {
            this.phaseTable(phsPs, Constants.TESTING_AND_COMMISSIONING,poBean.getTestingAndCommissioning(),poBean.getPoHeaderId());
            exPhaseSet.add(poBean.gettestingAndCommissioningKey());
        }
        
        
        if(exPhaseSet.contains(poBean.getHandOverKey())) {
            this.phaseTable(uPhsPs, Constants.HANDOVER,poBean.getHandOver(),poBean.getPoHeaderId());
        }else {
            this.phaseTable(phsPs, Constants.HANDOVER,poBean.getHandOver(),poBean.getPoHeaderId());
            exPhaseSet.add(poBean.getHandOverKey());
        }
        
        
        if(exPhaseSet.contains(poBean.getOthersKey())) {
            this.phaseTable(uPhsPs, Constants.OTHERS,poBean.getOthers(),poBean.getPoHeaderId());
        }else {
            this.phaseTable(phsPs, Constants.OTHERS,poBean.getOthers(),poBean.getPoHeaderId());
            exPhaseSet.add(poBean.getOthersKey());
        }
        
    }
    
    private void phaseTable(PreparedStatement phsPs, String phase,String value,String headerId) throws Exception {
        
        phsPs.setString(1, value);
        phsPs.setString(2, headerId);
        phsPs.setString(3, phase);
        phsPs.addBatch();
        
    }
    
    private void projectTable(PoBean poBean, PreparedStatement proPs) throws Exception {
        
        proPs.setString(1,poBean.getPoNumber());
        proPs.setString(2,poBean.getProjectNumber());
        proPs.setString(3,poBean.getProjectName());
        proPs.setString(4,poBean.getTaskNumber());
        proPs.setString(5,poBean.getTaskName());
        proPs.setString(6,poBean.getPoHeaderId());
        proPs.setString(7,poBean.getProjectId());
        proPs.setString(8,poBean.getTaskId());
        proPs.addBatch();   
    }
    
    private void lineTable(PoBean poBean, PreparedStatement linePs) throws Exception {
        
        linePs.setString(1,poBean.getLineNum());
        linePs.setString(2,poBean.getLineStatus());
        linePs.setString(3,poBean.getLineType());
        linePs.setString(4,poBean.getItemNumber());
        linePs.setString(5,poBean.getCategoryName());
        linePs.setString(6,poBean.getItemDescription());
        linePs.setString(7,poBean.getScheduleNum());
        linePs.setString(8,poBean.getLocation());
        linePs.setString(9, poBean.getOrganization());
        linePs.setString(10,poBean.getDistributionNum());
        linePs.setString(11,poBean.getSegment1());
        linePs.setString(12,poBean.getSegment2());
        linePs.setString(13,poBean.getSegment3());
        linePs.setString(14,poBean.getSegment4());
        linePs.setString(15,poBean.getSegment5());
        linePs.setString(16,poBean.getSegment6());
        linePs.setString(17,poBean.getSegment7());
        linePs.setString(18,poBean.getSegment8());
        linePs.setString(19,poBean.getSegment9());
        linePs.setString(20,poBean.getSegment10());
        linePs.setString(21, poBean.getQty());
        linePs.setString(22,poBean.getUomCode());
        linePs.setString(23,poBean.getUnitPrice());
        linePs.setString(24,poBean.getOrderedAmount());
        linePs.setString(25,poBean.getFuncOrderedAmount());
        linePs.setString(26,poBean.getExpenditureItemDate());
        linePs.setString(27,poBean.getExpenditureTypeName());
        linePs.setString(28,poBean.getExpOrg());
        linePs.setString(29,poBean.getExchangeRate());
        linePs.setString(30,poBean.getAccureOnReceiptFlag());
        linePs.setString(31,poBean.getInvoicedQuantity());
        linePs.setString(32,poBean.getInvoicedAmount());
        linePs.setString(33,poBean.getRemainingQuantity());
        linePs.setString(34,poBean.getRemainingAmount());
        linePs.setString(35,poBean.getApprovalLevel());
        linePs.setString(36,poBean.getMatchOption());
        linePs.setString(37,poBean.getPoHeaderId());
        linePs.setString(38,poBean.getProjectId());
        linePs.setString(39,poBean.getProjectNumber());
        linePs.setString(40,poBean.getTaskId());
        linePs.setString(41,poBean.getTaskNumber());
        linePs.setString(42,poBean.getPaymentPhases());
        linePs.setString(43,poBean.getPoLineId());
        linePs.setString(44,poBean.getScheduleId());
        linePs.setString(45,poBean.getDistributionId());
        linePs.addBatch();
    }
    
    private String getUpdatePoQuery(){
        
        StringBuffer updatePoQuery = new StringBuffer("UPDATE EEI_PO_MASTER_T");
        updatePoQuery.append(" SET PO_NUMBER=?,");
        updatePoQuery.append("SUPPLIER_ID=?,");
        updatePoQuery.append("SUPPLIER_NAME=?,");
        updatePoQuery.append("SUPPLIER_SITE_ID=?,");
        updatePoQuery.append("SUPPLIER_SITE=?,");
        updatePoQuery.append("PROCUREMENT_BU=?,");
        updatePoQuery.append("REQUISITIONING_BU=?,");
        updatePoQuery.append("STYLE=?,");
        updatePoQuery.append("PO_DATE = TO_DATE(?,'YYYY-MM-DD'),");
        updatePoQuery.append("PO_APPROVED_DATE = TO_DATE(?,'YYYY-MM-DD'),");
        updatePoQuery.append("BUYER_NAME=?,");
        updatePoQuery.append("DESCRIPTION=?,");
        updatePoQuery.append("PAYMENT_TERMS=?,");
        updatePoQuery.append("CURRENCY=?,");
        updatePoQuery.append("ATTRIBUTE1=?,");
        updatePoQuery.append("RETENTION=?,");
        updatePoQuery.append("RECOUPMENT=?,");
        updatePoQuery.append("PO_STATUS=?,");
        updatePoQuery.append("CONTEXT=?,");
        updatePoQuery.append("SUPPLIER_NO=?,");
        updatePoQuery.append("BU_ID=?,");
        updatePoQuery.append("LEDGER_ID=?,");
        updatePoQuery.append("CONTRACT_END_DATE= TO_DATE(?,'YYYY-MM-DD'),");
        updatePoQuery.append("PROJECT_ID=?,");
        updatePoQuery.append("PROJECT_NUMBER=?");
        updatePoQuery.append(" WHERE PO_ID=?");
        return updatePoQuery.toString();
    }
    
    private String getInsertPoQuery(){
        
        StringBuffer insertPoQuery = new StringBuffer("INSERT INTO EEI_PO_MASTER_T");
        insertPoQuery.append("(PO_NUMBER,"); int x=0;
        insertPoQuery.append("SUPPLIER_ID,"); x++;
        insertPoQuery.append("SUPPLIER_NAME,"); x++;
        insertPoQuery.append("SUPPLIER_SITE_ID,"); x++;
        insertPoQuery.append("SUPPLIER_SITE,"); x++;
        insertPoQuery.append("PROCUREMENT_BU,"); x++;
        insertPoQuery.append("REQUISITIONING_BU,"); x++;
        insertPoQuery.append("STYLE,"); x++;
        insertPoQuery.append("PO_DATE,"); x++;
        insertPoQuery.append("PO_APPROVED_DATE,"); x++;
        insertPoQuery.append("BUYER_NAME,"); x++;
        insertPoQuery.append("DESCRIPTION,"); x++;
        insertPoQuery.append("PAYMENT_TERMS,"); x++;
        insertPoQuery.append("CURRENCY,"); x++;
        insertPoQuery.append("ATTRIBUTE1,"); x++;
        insertPoQuery.append("RETENTION,"); x++;
        insertPoQuery.append("RECOUPMENT,"); x++;
        insertPoQuery.append("PO_STATUS,"); x++;
        insertPoQuery.append("CONTEXT,"); x++;
        insertPoQuery.append("SUPPLIER_NO,"); x++;
        insertPoQuery.append("BU_ID,"); x++;
        insertPoQuery.append("LEDGER_ID,"); x++;
        insertPoQuery.append("CONTRACT_END_DATE,"); x++;
        insertPoQuery.append("PROJECT_ID,"); x++;
        insertPoQuery.append("PROJECT_NUMBER,"); x++;
        insertPoQuery.append("PO_ID)"); x++;     
        insertPoQuery.append(" VALUES(");
        for(int i=0;i<x;i++){
            
            if(i==8 || i==9 || i==22)
                insertPoQuery.append("TO_DATE(?,'YYYY-MM-DD'),");
            else
                insertPoQuery.append("?,");
        }
        insertPoQuery.append("?)");
        return insertPoQuery.toString();
    }
    
    private String getInsertProjectQuery(){
        
        StringBuffer insertProjectQuery = new StringBuffer("INSERT INTO EEI_PROJECT_MASTER_T");
        insertProjectQuery.append(" (PO_NUMBER,"); int x=0;
        insertProjectQuery.append("PROJECT_NUMBER,"); x++;        
        insertProjectQuery.append("PROJECT_NAME,"); x++;
        insertProjectQuery.append("TASK_NUMBER,"); x++;
        insertProjectQuery.append("TASK_NAME,"); x++;
        insertProjectQuery.append("PO_ID,"); x++;
        insertProjectQuery.append("PROJECT_ID,"); x++;        
        insertProjectQuery.append("TASK_ID)"); x++;
        insertProjectQuery.append(" VALUES("); 
        for(int i=0;i<x;i++){
            insertProjectQuery.append("?,");
        }
        insertProjectQuery.append("?)");
        return insertProjectQuery.toString();
    }
    
    private String getUpdateProjectQuery(){
        
        StringBuffer updateProjectQuery = new StringBuffer("UPDATE EEI_PROJECT_MASTER_T");
        updateProjectQuery.append(" SET PO_NUMBER = ?,");
        updateProjectQuery.append("PROJECT_NUMBER=?,");
        updateProjectQuery.append("PROJECT_NAME=?,");
        updateProjectQuery.append("TASK_NUMBER=?,");
        updateProjectQuery.append("TASK_NAME=?");
        updateProjectQuery.append(" WHERE PO_ID = ?");
        updateProjectQuery.append(" AND PROJECT_ID = ?");
        updateProjectQuery.append(" AND TASK_ID = ?");
        return updateProjectQuery.toString();
    }
    
    private String getInsertLineQuery(){
        
        StringBuffer insertLineQuery = new StringBuffer("INSERT INTO EEI_IPC_LINES_MASTER_T");
        insertLineQuery.append(" (LINES_ID,");
        insertLineQuery.append("LINE_NUMBER,"); int x=0;
        insertLineQuery.append("LINE_STATUS,"); x++;
        insertLineQuery.append("LINE_TYPE,"); x++;
        insertLineQuery.append("ITEM,");  x++;
        insertLineQuery.append("CATEGORY_NAME,");  x++;
        insertLineQuery.append("BBQ_LINES,");  x++;
        insertLineQuery.append("SCHEDULE_NUMBER,");  x++;
        insertLineQuery.append("LOCATION,");  x++;
        insertLineQuery.append("ORGANIZATION,");  x++;
        insertLineQuery.append("DISTRIBUTION_NUMBER,");  x++;
        insertLineQuery.append("SEGMENT1,");  x++;
        insertLineQuery.append("SEGMENT2,");  x++;
        insertLineQuery.append("SEGMENT3,");  x++;
        insertLineQuery.append("SEGMENT4,");  x++;
        insertLineQuery.append("SEGMENT5,");  x++;
        insertLineQuery.append("SEGMENT6,");  x++;
        insertLineQuery.append("SEGMENT7,");  x++;
        insertLineQuery.append("SEGMENT8,");  x++;
        insertLineQuery.append("SEGMENT9,");  x++;
        insertLineQuery.append("SEGMENT10,");  x++;
        insertLineQuery.append("ORDERED_QUANTITY,");  x++;
        insertLineQuery.append("UOM,");  x++;
        insertLineQuery.append("RATE,");  x++;
        insertLineQuery.append("ORDERED_AMOUNT,");  x++;
        insertLineQuery.append("ORDERED_AMOUNT_FUN,");  x++;
        insertLineQuery.append("EXP_ITEM_DATE,");  x++;
        insertLineQuery.append("EXP_TYPE,");  x++;
        insertLineQuery.append("EXP_ORG,");  x++;
        insertLineQuery.append("EXCHANGE_RATE,");  x++;
        insertLineQuery.append("ACCRUE_ON_RECEIPT_FLAG,");  x++;
        insertLineQuery.append("INVOICED_QUANTITY,");  x++;
        insertLineQuery.append("INVOICED_AMOUNT,");  x++;
        insertLineQuery.append("REMAINING_QUANTITY,");  x++;
        insertLineQuery.append("REMAINING_AMOUNT,");  x++;
        insertLineQuery.append("APPROVAL_LEVEL,");x++;
        insertLineQuery.append("MATCH_OPTION,");x++;
        insertLineQuery.append("PO_ID,");  x++;
        insertLineQuery.append("PROJECT_ID,");  x++;
        insertLineQuery.append("PROJECT_NUMBER,");  x++;
        insertLineQuery.append("TASK_ID,");  x++;
        insertLineQuery.append("TASK_NUMBER,");  x++;
        insertLineQuery.append("PAYMENT_PHASES,"); x++;
        insertLineQuery.append("LINE_ID,");  x++;
        insertLineQuery.append("SCHEDULE_ID,");  x++;
        insertLineQuery.append("DISTRIBUTION_ID)");  x++;        
        insertLineQuery.append(" VALUES(");
        insertLineQuery.append("IPC_LINES_MASTER_SEQUENCE.NEXTVAL,");
      
        for(int i=0;i<x;i++){
            
            if(i==25)
                insertLineQuery.append("TO_DATE(?,'YYYY-MM-DD'),");
            else
                insertLineQuery.append("?,");
        }
        insertLineQuery.append("?)");
        return insertLineQuery.toString();
    }
    
    private String getUpdateLineQuery(){
        
        StringBuffer updateLineQuery = new StringBuffer("UPDATE EEI_IPC_LINES_MASTER_T");
        updateLineQuery.append(" SET LINE_NUMBER=?,");
        updateLineQuery.append("LINE_STATUS=?,");
        updateLineQuery.append("LINE_TYPE=?,");
        updateLineQuery.append("ITEM=?,");
        updateLineQuery.append("CATEGORY_NAME=?,");
        updateLineQuery.append("BBQ_LINES=?,");
        updateLineQuery.append("SCHEDULE_NUMBER=?,");
        updateLineQuery.append("LOCATION=?,");
        updateLineQuery.append("ORGANIZATION=?,");
        updateLineQuery.append("DISTRIBUTION_NUMBER=?,");
        updateLineQuery.append("SEGMENT1=?,");
        updateLineQuery.append("SEGMENT2=?,");
        updateLineQuery.append("SEGMENT3=?,");
        updateLineQuery.append("SEGMENT4=?,");
        updateLineQuery.append("SEGMENT5=?,");
        updateLineQuery.append("SEGMENT6=?,");
        updateLineQuery.append("SEGMENT7=?,");
        updateLineQuery.append("SEGMENT8=?,");
        updateLineQuery.append("SEGMENT9=?,");
        updateLineQuery.append("SEGMENT10=?,");
        updateLineQuery.append("ORDERED_QUANTITY=?,");
        updateLineQuery.append("UOM=?,");
        updateLineQuery.append("RATE=?,");
        updateLineQuery.append("ORDERED_AMOUNT=?,");
        updateLineQuery.append("ORDERED_AMOUNT_FUN=?,");
        updateLineQuery.append("EXP_ITEM_DATE = TO_DATE(?,'YYYY-MM-DD'),");
        updateLineQuery.append("EXP_TYPE=?,");
        updateLineQuery.append("EXP_ORG=?,");
        updateLineQuery.append("EXCHANGE_RATE=?,");
        updateLineQuery.append("ACCRUE_ON_RECEIPT_FLAG=?,");
        updateLineQuery.append("INVOICED_QUANTITY=?,");
        updateLineQuery.append("INVOICED_AMOUNT=?,");
        updateLineQuery.append("REMAINING_QUANTITY=?,");
        updateLineQuery.append("REMAINING_AMOUNT=?,");
        updateLineQuery.append("APPROVAL_LEVEL=?,");
        updateLineQuery.append("MATCH_OPTION=?,");
        updateLineQuery.append("PO_ID=?,");
        updateLineQuery.append("PROJECT_ID=?,");
        updateLineQuery.append("PROJECT_NUMBER=?,");
        updateLineQuery.append("TASK_ID=?,");
        updateLineQuery.append("TASK_NUMBER=?,");
        updateLineQuery.append("PAYMENT_PHASES=?");
        updateLineQuery.append(" WHERE LINE_ID=?");
        updateLineQuery.append(" AND SCHEDULE_ID=?");
        updateLineQuery.append(" AND DISTRIBUTION_ID=?");
        return updateLineQuery.toString();
    }

    private String getInsertPhaseQuery() {
        StringBuffer insertPhaseQuery = new StringBuffer("INSERT INTO EEI_PHASES_MASTER_T");
        insertPhaseQuery.append("(PAYMENT_PERCENT,"); int x=0;
        insertPhaseQuery.append("PO_ID,");x++;
        insertPhaseQuery.append("PAYMENT_PHASES)");x++;
        insertPhaseQuery.append(" VALUES("); 
         for(int i=0;i<x;i++){
             insertPhaseQuery.append("?,");
         }
        insertPhaseQuery.append("?)");
                return insertPhaseQuery.toString();
        
    }
    
    private String getUpdatePhaseQuery(){
        StringBuffer updatePhaseQuery = new StringBuffer("UPDATE EEI_PHASES_MASTER_T");           
        updatePhaseQuery.append(" SET PAYMENT_PERCENT= ?");
        updatePhaseQuery.append(" WHERE PO_ID =?");
        updatePhaseQuery.append(" AND PAYMENT_PHASES=?");
        return updatePhaseQuery.toString();
        
    }
}
