package sc.common.view.backing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import sc.common.view.util.Constants;
import sc.common.view.util.HttpService;
import sc.common.view.util.Util;

public class ChangePOIntegration implements Job {
    
    private HashMap<Long,HashMap<Long,List<Long>>> changePoMap = null;
    HashMap<String, String> payloadMap = null;
    Map<String, String> poWsMap = null;
    Connection con = null;
    Util util = null;
    private static final String delimiter = "@";
    private static final String readLinesQuery = "SELECT  MASTER.PO_ID, MASTER.LINE_ID, MASTER.SCHEDULE_ID FROM EEI_IPC_LINES_MASTER_T MASTER WHERE MASTER.ACCRUE_ON_RECEIPT_FLAG='Y' OR APPROVAL_LEVEL<>'2' OR MATCH_OPTION<>'P' GROUP BY MASTER.PO_ID, MASTER.LINE_ID, MASTER.SCHEDULE_ID ORDER BY MASTER.PO_ID, MASTER.LINE_ID, MASTER.SCHEDULE_ID";
    private static final String updateAORFlagQuery = "UPDATE EEI_IPC_LINES_MASTER_T SET ACCRUE_ON_RECEIPT_FLAG='N',APPROVAL_LEVEL=?,MATCH_OPTION=? WHERE PO_ID = ?";
    private static final String getPayloadQuery = "SELECT BLOB_DATA FROM EEI_GENERAL_BLOB_T WHERE BLOB_CODE = 'AOR_PAYLOAD'";
    
    public ChangePOIntegration(HashMap<String, String> setupMap, Connection con) {
        super();
        this.poWsMap = setupMap;        
        this.con = con;
        this.util = new Util();
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // TODO Implement this method
        
        try{            
            this.readPOLines();       
            if(changePoMap!=null){
                
                payloadMap = this.getPayloadMap();
                Set<Long> poSet = changePoMap.keySet();
                for(Long poId : poSet){                   

                    String request = this.generatePayload(poId);
                    String response = HttpService.soapHttpPost(poWsMap.get("HOST") + Constants.PO_WSDL, request, poWsMap.get("USER_NAME")+":"+poWsMap.get("PASSWORD"));
                    Boolean hasFault = true;
                    if(!response.isEmpty())
                        hasFault = util.hasFault(response);                            
                    
                    if(!hasFault)
                        this.updateAORFlagByPO(poId);
                }
                
            }
                        
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void readPOLines() throws SQLException {
        Long poId = null;
        Long lineId = null;
        HashSet<Long> poSet = null;
        HashSet<Long> lineSet = null;
        HashMap<Long, List<Long>> lineMap = null;
        List<Long> schList = null;
        
        try(PreparedStatement poPs = con.prepareStatement(readLinesQuery);
            ResultSet rs = poPs.executeQuery();){
            
            Boolean create = true;
            
            while(rs.next()){
                
                if(create){
                    changePoMap = new HashMap<>(); lineMap = new HashMap<>(); schList = new ArrayList<>();
                    poSet = new HashSet<>(); lineSet = new HashSet<>();
                    poId = rs.getLong(1);
                    poSet.add(poId);
                    lineId = rs.getLong(2);
                    lineSet.add(lineId);
                    schList.add(rs.getLong(3));
                }else{                   
                    if(poSet.contains(rs.getLong(1))){                    
                        if(lineSet.contains(rs.getLong(2))){                        
                            poId = rs.getLong(1);
                            lineId = rs.getLong(2);
                            schList.add(rs.getLong(3));
                        }else{
                            poId = rs.getLong(1);
                            lineMap.put(lineId,schList);
                            schList = new ArrayList<>();
                            lineId = rs.getLong(2);
                            lineSet.add(lineId);
                            schList.add(rs.getLong(3));
                        }
                    }else{
                        lineMap.put(lineId,schList);
                        changePoMap.put(poId, lineMap);
                        lineMap = new HashMap<>();
                        schList = new ArrayList<>();
                        poId = rs.getLong(1);
                        poSet.add(poId);
                        lineId = rs.getLong(2);
                        lineSet.add(lineId);
                        schList.add(rs.getLong(3));                    
                    }                   
                }
                create = false;              
                
            }
            
            if(!create){                
                lineMap.put(lineId,schList);
                changePoMap.put(poId, lineMap);
            }
        
        }
        
    }
    
    private void updateAORFlagByPO(long poId) throws SQLException {
        try(PreparedStatement poPs = con.prepareStatement(updateAORFlagQuery);){               
                poPs.setString(1,"2");
                poPs.setString(2, "P");
                poPs.setLong(3, poId);
                poPs.executeUpdate();
                con.commit();
            }
    }
    
    private String generatePayload(Long poId){
        StringBuffer payload = new StringBuffer();
        HashMap<Long,List<Long>> lineMap = null;
        Set<Long> lineSet = null;
        List<Long> schList = null;
        
        try{
            
            payload.append(payloadMap.get("poHeader")).append(poId).append(payloadMap.get("poBody"));
            lineMap = changePoMap.get(poId);                
            lineSet = lineMap.keySet();                
            for(Long lineId : lineSet){
                
                payload.append(payloadMap.get("lineHeader")).append(lineId).append(payloadMap.get("lineBody"));
                schList =  lineMap.get(lineId);                   
                for(Long schId : schList)           
                    payload.append(payloadMap.get("schHeader")).append(schId).append(payloadMap.get("schEnd"));
                
                payload.append(payloadMap.get("lineEnd"));              
                
            }
            payload.append(payloadMap.get("poEnd"));
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return payload.toString();
    }
                
    private HashMap<String,String> getPayloadMap() throws SQLException {
        
        HashMap<String,String> map = new HashMap<>();
        try(PreparedStatement ps = con.prepareStatement(getPayloadQuery);
            ResultSet rs = ps.executeQuery()){
            
            while(rs.next()){
                String s = rs.getString(1);
                String a[] = s.split(delimiter, -1);
                map.put("poHeader",a[0]);
                map.put("poBody",a[1]);
                map.put("poEnd",a[2]);
                map.put("lineHeader",a[3]);
                map.put("lineBody",a[4]);
                map.put("lineEnd",a[5]);
                map.put("schHeader",a[6]);
                map.put("schEnd",a[7]);
            }
            
        }
               
      return map;  
    }
    
}
