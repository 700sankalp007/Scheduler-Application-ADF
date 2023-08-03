package sc.common.view.util;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DML  implements Constants {
    
    private Connection con;
    
    public DML(Connection con) {
        super();
        this.con = con;
    }
    
    public void processData(String tableName, List<Map<String, Object>> mapList, Set<String> keySet, Map<String, String> insertExceptionMap, Map<String, String> updateExceptionMap) throws Exception {
        
        String key = null;
        String[] keys = null;
        Set<String> exKeySet = null;
        List<Map<String, Object>> insertMapList = new ArrayList<>(), updateMapList = new ArrayList<>();
        if(keySet!=null){
            
            exKeySet = this.getExKeySet(tableName, keySet);
            if(!exKeySet.isEmpty()){
                
                keys = keySet.toArray(new String[keySet.size()]);
                for(Map<String, Object> m : mapList){
                    
                    key = this.getKey(m, keys);
                    
//                    System.out.println("Keys="+key);
                    if(exKeySet.contains(key)){
//                        System.out.println("In Update="+key);
                        updateMapList.add(m);
                    }
                    else{
                        exKeySet.add(key);
                        insertMapList.add(m);
                    }            
                }
            }else                
                insertMapList = mapList;            
        }else
            insertMapList = mapList;
        
        if(!insertMapList.isEmpty())
            this.insertData(tableName, insertMapList, insertExceptionMap);
        if(!updateMapList.isEmpty())
            this.updateData(tableName, updateMapList, keySet, updateExceptionMap);
    }
    
    public void insertData(String tableName, List<Map<String, Object>> insertMapList) throws Exception {
        
        String insertStatement = this.getInsertStatement(tableName, insertMapList.get(0).keySet());
//        System.out.println("Insert Statement : " + insertStatement);
        this.insertData(insertMapList, insertStatement);
    }

    public void insertData(String tableName, List<Map<String, Object>> insertMapList, Map<String, String> exceptionMap) throws Exception {
        
        String insertStatement = this.getInsertStatement(tableName, insertMapList.get(0).keySet(), exceptionMap);
//        System.out.println("Insert Statement : " + insertStatement);
        this.insertData(insertMapList, insertStatement);
    }

    private void insertData(List<Map<String, Object>> insertMapList, String insertStatement) throws Exception{
        
        int count;
        try(PreparedStatement ps = con.prepareStatement(insertStatement);){
            
            for(Map<String, Object> insertMap : insertMapList){
                
                count = 0;
                for(Map.Entry<String, Object> insertME : insertMap.entrySet()){
                    
                    count++;
                    ps.setObject(count, insertME.getValue());
                }
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
    
    public void updateData(String tableName, List<Map<String, Object>> updateMapList, Set<String> keySet) throws Exception {
        
        String updateStatement = this.getUpdateStatement(tableName, updateMapList.get(0).keySet(), keySet);
//        System.out.println("Insert Statement : " + updateStatement);
        this.updateData(updateMapList, keySet, updateStatement);
    }

    public void updateData(String tableName, List<Map<String, Object>> updateMapList, Set<String> keySet, Map<String, String> exceptionMap) throws Exception {
        
        String updateStatement = this.getUpdateStatement(tableName, updateMapList.get(0).keySet(), keySet, exceptionMap);
//        System.out.println("Update Statement : " + updateStatement);
        this.updateData(updateMapList, keySet, updateStatement);
    }

    private void updateData(List<Map<String, Object>> updateMapList, Set<String> keySet, String updateStatement) throws Exception{
        
        int count;
        try(PreparedStatement ps = con.prepareStatement(updateStatement);){
            
            for(Map<String, Object> updateMap : updateMapList){
                
                count = 0;
                for(Map.Entry<String, Object> updateME : updateMap.entrySet()){
                    
                    count++;
                    ps.setObject(count, updateME.getValue());
                }
                
                for(String key : keySet){
                    
                    count++;
                    ps.setObject(count, updateMap.get(key));
                }
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public String getInsertStatement(String tableName, Set<String> columnSet)throws Exception {
        
        return getInsertStatement(tableName, columnSet, null);
    }
    
    public String getInsertStatement(String tableName, Set<String> columnSet, Map<String, String> exceptionMap) throws Exception {
                
        boolean isFirstColumn = true;
        StringBuffer insertStatement = new StringBuffer(INSERT_INTO);
        insertStatement.append(tableName);                              
        if(columnSet!=null
           && !columnSet.isEmpty()){
                              
            for(String columnName : columnSet){
                
                if(isFirstColumn){
                    
                    insertStatement.append(OPEN_ROUND_BRACKET);
                    isFirstColumn = false;
                }else                    
                    insertStatement.append(COMMA);

                insertStatement.append(columnName);                   
            }
            
            if(exceptionMap!=null) exceptionMap.keySet().forEach(i -> insertStatement.append(COMMA).append(i));            
            insertStatement.append(CLOSE_ROUND_BRACKET);
            insertStatement.append(VALUES);
            insertStatement.append(OPEN_ROUND_BRACKET);
            for(int i=0;i<columnSet.size();i++){
                
                if(i!=0)
                    insertStatement.append(COMMA);
                                                  
                 insertStatement.append(QUESTION_MARK);
            }
            
            if(exceptionMap!=null) exceptionMap.keySet().forEach(i -> insertStatement.append(COMMA).append(exceptionMap.get(i)));
            insertStatement.append(CLOSE_ROUND_BRACKET);
        }
        
        return insertStatement.toString();
    }

    public String getUpdateStatement(String tableName, Set<String> columnSet, Set<String> keySet){
        
        return this.getUpdateStatement(tableName, columnSet, keySet, null);
    }

    public String getUpdateStatement(String tableName, Set<String> columnSet, Set<String> keySet, Map<String, String> exceptionMap){
        
        boolean isFirstColumn = true;
        StringBuffer updateStatement = new StringBuffer(UPDATE);
        updateStatement.append(tableName);
        if(columnSet!=null
           && !columnSet.isEmpty()
           && keySet!=null
           && !keySet.isEmpty()){               
               
            for(String columnName : columnSet){
                
                if(isFirstColumn){
                    
                    updateStatement.append(SET);
                    isFirstColumn = false;
                }else
                    updateStatement.append(COMMA);
                
                updateStatement.append(columnName);
                updateStatement.append(EQUAL);
                updateStatement.append(QUESTION_MARK);
            }
            
            if(exceptionMap!=null) exceptionMap.keySet().forEach(i -> updateStatement.append(COMMA).append(i).append(EQUAL).append(exceptionMap.get(i)));
            updateStatement.append(WHERE);
            isFirstColumn = true;
            for(String columnName : keySet){
                
                if(!isFirstColumn)
                    updateStatement.append(AND);

                isFirstColumn = false;
                updateStatement.append(columnName);
                updateStatement.append(EQUAL);
                updateStatement.append(QUESTION_MARK);
            }
        }
        
        return updateStatement.toString();
    }

    public Set<String> getExKeySet(String tableName, Set<String> keySet) throws Exception {
        
        final String query = this.getExKeySetQuery(tableName, keySet);
        return new Util().getExIdSet(con, query);
    }
    
    private String getKey(Map<String, Object> m, String[] k){
        
        StringBuffer s = new StringBuffer();
        s.append(m.get(k[0]));
        if(k.length>1)
            for(int i=1; i<k.length; i++) s.append(PIPE).append(m.get(k[i]));
        
        return s.toString();
    }
    
    private String getExKeySetQuery(String tableName, Set<String> keySet) throws Exception {
        
        boolean isFirstColumn = true;
        StringBuffer exKeySetQuery = new StringBuffer(SELECT);
        if(keySet!=null
           && !keySet.isEmpty()){            
            
            for(String columnName : keySet){
                
                if(!isFirstColumn)                        
                    exKeySetQuery.append(CONCAT);
                
                isFirstColumn = false;
                exKeySetQuery.append(columnName);
            }
            
            exKeySetQuery.append(FROM);
            exKeySetQuery.append(tableName);            
        }
        return exKeySetQuery.toString();
    }
    
    private String getDeleteStatement(String tableName) throws Exception {
        
        StringBuffer deleteStatement = new StringBuffer(DELETE_FROM);
        return deleteStatement.append(tableName).toString();
    }
    
    public void deleteData(String tableName) throws Exception {
        
        try(PreparedStatement ps = con.prepareStatement(this.getDeleteStatement(tableName));){
            
            ps.executeUpdate();
        }
    }
}
