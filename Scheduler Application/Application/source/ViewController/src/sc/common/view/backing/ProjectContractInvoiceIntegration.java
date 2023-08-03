package sc.common.view.backing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sc.common.view.service.ProjectContractInvoiceService;
import sc.common.view.util.ConnectionManager;
import sc.common.view.util.Constants;
import sc.common.view.util.HttpServiceBean;
import sc.common.view.util.Util;

public class ProjectContractInvoiceIntegration implements Constants {

    Util util=null;
    Connection con = null;
    HashMap<String,Integer> map = new HashMap<>();
    HashMap<String, String> setupMap = null;
    Map<String, String> contractInvoiceMap = null;
    private List<Map<String, String>> contractInvoiceMapList = null;
    Set<String> updatedInvoiceSet = null, notUpdatedInvoiceSet = null;
    private static final String FILE_PATH = "C:\\Users\\Evosys\\Documents\\Zamil\\Migration\\Contract Invoice\\Contract Invoice 3.csv";    

    public ProjectContractInvoiceIntegration(HashMap<String, String> setupMap, Connection con) {
        
        super();
        this.util = new Util();
        this.setupMap = setupMap;        
        this.con = con;
    }

    
    public ProjectContractInvoiceIntegration() {
        super();
    }

    public void executeContractInvoice(){
        
        int recordCount = 0;
        try{
            
            this.readContractInvoice();
            if(contractInvoiceMapList!=null
               && !contractInvoiceMapList.isEmpty()){
                   
                this.updateContractInvoice();
                recordCount = contractInvoiceMapList.size();
            }                            
        }catch(Exception e){
            
            e.printStackTrace();      
        }finally{
            
//            System.out.println("File Data : " + contractInvoiceMapList);    
//            System.out.println("Record Count : " + recordCount);
//            System.out.println("Updated Invoice : " + updatedInvoiceSet);
//            System.out.println("Not Updated Invoice : " + notUpdatedInvoiceSet);
        }
    }


    public void readContractInvoice() throws Exception {
        
                
        try (      FileInputStream is = new FileInputStream(new File(FILE_PATH));
                   BufferedReader reader = new BufferedReader(new InputStreamReader(is,Constants.UTF8));){

                   String line;
                   int lineNum = 0;
                   while ((line = reader.readLine()) != null) {
                       
                       lineNum ++;
                       String csv[] = util.splitLine(line, ",");
                       if(lineNum==1){
                           
                           for(int i = 0; i<csv.length; i++)
                               map.put(csv[i],i);
                       }else{
                           
                           contractInvoiceMap = new HashMap<>();
                           contractInvoiceMap.put("ContractTypeName", csv[map.get("CONTRACT_TYPE_NAME")]);
                           contractInvoiceMap.put("InvoiceNumber", csv[map.get("INVOICE_DRAFT_NUMBE")]);
                           contractInvoiceMap.put("RaInvoiceNumber", csv[map.get("INVOICE_RA_NUMB")]);
                           contractInvoiceMap.put("SystemReference", csv[map.get("SYS_REF_NUM")]);
                           contractInvoiceMap.put("ContractNumber", csv[map.get("CONTRACT_NUM")]);
                           contractInvoiceMap.put("TransferStatusCode", "A");
                           if(contractInvoiceMapList==null) contractInvoiceMapList = new  ArrayList<>();
                           contractInvoiceMapList.add(contractInvoiceMap);
                       }
                   }
        }
    }

    public void updateContractInvoice() throws Exception {
        
        String request = null, response = null, error = null;
        String url = setupMap.get("HOST") + WSDL.PROJECT_CONTRACT;
        ProjectContractInvoiceService projectContractInvoiceService = new ProjectContractInvoiceService();
        for(Map<String, String> contractInvoiceMap : contractInvoiceMapList){
                        
            request = util.prettyXmlFromSOAP(projectContractInvoiceService.tieback(contractInvoiceMap));
            response = util.prettyXmlFromString(HttpServiceBean.soapHttpPost(url, request, setupMap.get("USER_NAME")+":"+setupMap.get("PASSWORD")));                
            Boolean hasFault = util.hasFault(response);
            if(hasFault){
                
                error = util.getFaultString(response);
//                System.out.println("Not Update Invoice : " + contractInvoiceMap.get("ContractNumber") + " : " + error);            
                if(notUpdatedInvoiceSet==null) notUpdatedInvoiceSet = new HashSet<>();
                notUpdatedInvoiceSet.add(contractInvoiceMap.get("ContractNumber") + "|" + contractInvoiceMap.get("InvoiceNumber"));
            }else{
                
//                System.out.println("Update Invoice : " + contractInvoiceMap.get("ContractNumber"));            
                if(updatedInvoiceSet==null) updatedInvoiceSet = new HashSet<>();
                updatedInvoiceSet.add(contractInvoiceMap.get("ContractNumber") + "|" + contractInvoiceMap.get("InvoiceNumber"));
            }
        }
    }

    public static void main(String s[]) throws Exception {
        
        try(Connection con = ConnectionManager.getConnection();){
            
            HashMap<String, String> setupMap = Util.getSetupDetails(con, Constants.SETUP_TYPE_WEBSERVICE, Constants.SUB_TYPE_PROD);
            ProjectContractInvoiceIntegration pcii = new ProjectContractInvoiceIntegration(setupMap, con);
            pcii.executeContractInvoice();           
        }

    }

}
