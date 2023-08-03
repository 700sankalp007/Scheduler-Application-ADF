package sc.common.view.backing.csvReport;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import sc.common.view.bean.ReportColumnBean;
import sc.common.view.bean.ReportDataBean;
import sc.common.view.util.Util;


public class CSVReport {
    Util util=null;
    public CSVReport() {
        util=new Util();
    }
    
    public  int processReport(InputStream is,ReportDataBean bean,Connection con) throws Exception{
        
        int count=1,processedRecords=0;;
        String currentLine=null;
        BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF8"));
        HashMap<Integer,String> columsMapWithIndex=new HashMap<Integer,String>();
        HashMap<Integer,HashMap<String,ReportColumnBean>> csvData=new HashMap<Integer,HashMap<String,ReportColumnBean>>();
        while((currentLine = br.readLine()) != null)
        {
            List<String> firstLine = parseCurrentLine(currentLine,bean.getDelimiter().charAt(0));
            HashMap<String,ReportColumnBean> reportCol=new HashMap<String,ReportColumnBean>();
            if(count==1){
                for(int i=0;i<firstLine.size();i++){
                    columsMapWithIndex.put(i,firstLine.get(i));
                }
            }
            else if(count>1){
            for(Map.Entry<Integer,String> entry :columsMapWithIndex.entrySet()){
                ReportColumnBean colBean=new ReportColumnBean();
                String colName= entry.getValue();
                String value=null;
                try{
                    value= firstLine.get(entry.getKey());
                }catch(Exception e){
                    e.printStackTrace();
                }
                colBean.setColumnName(colName);
                colBean.setColumnValue(value);
                reportCol.put(colName,colBean);
            }
            csvData.put(count,reportCol);
        }
        
        count++;
        }
        processedRecords=util.processRecordToDataBase(con, bean, csvData);         
        return processedRecords;
    }
    
    /**
     * @Desc parse CurrentLine
     * @param line
     * @param delimiter
     * @return
     * @throws Exception
     */
    public List<String> parseCurrentLine(String line, char delimiter) throws Exception {
        List<String> lineArray = new ArrayList<String>();
        try{
            CSVParser csvParser = CSVParser.parse(line, CSVFormat.DEFAULT.withDelimiter(delimiter));
            for (CSVRecord csvRecord : csvParser){
                    Iterator<String> itr= csvRecord.iterator();
                    lineArray =IteratorUtils.toList(itr);
                }
        }catch(Exception e){
            e.printStackTrace();
        }
        return lineArray;
    }

}
