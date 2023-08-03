package sc.common.view.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sc.common.view.bean.ReportColumnBean;

public class Test {
    public Test() {
        super();
    }
   


    public static void main(String[] args) throws IOException {
        try {
            int count=1;
            String currentLine=null;
            BufferedReader br = null;
            Util util=new Util();
            HashMap<Integer,String> columsMapWithIndex=new HashMap<Integer,String>();
            File file = new File("D:\\employee.csv"); 
            HashMap<Integer,HashMap<String,ReportColumnBean>> records=new HashMap<Integer,HashMap<String,ReportColumnBean>>();
             br = new BufferedReader(new FileReader(file)); 
            while((currentLine = br.readLine()) != null)
            {
                List<String> firstLine = null;//util.parseCurrentLine(currentLine,",".charAt(0));
                HashMap<String,ReportColumnBean> reportCol=new HashMap<String,ReportColumnBean>();
                if(count==1){
                    for(int i=0;i<firstLine.size();i++){
                        columsMapWithIndex.put(i,firstLine.get(i));
                    }
                    for(Map.Entry<Integer,String> entry :columsMapWithIndex.entrySet()){
//                        System.out.println(entry.getValue()+" -------> "+entry.getKey());
                        }
                }
                
                else if(count>1){
                    for(Map.Entry<Integer,String> entry :columsMapWithIndex.entrySet()){
                        ReportColumnBean bean=new ReportColumnBean();
                        String colName= entry.getValue();
                        String value=null;
                        try{
                            value= firstLine.get(entry.getKey());
                        }catch(Exception e){
                         
                            e.printStackTrace();
                        }
                        bean.setColumnName(colName);
                        bean.setColumnValue(value);
                        reportCol.put(colName,bean);
//                        System.out.println(colName +" : "+value);
                        
                    }
                        
                }
                records.put(count,reportCol);
                count++;
                
            }
              
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
