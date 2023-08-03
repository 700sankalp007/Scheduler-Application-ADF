package sc.common.view.backing.xmlReport;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import java.sql.Connection;

import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;

//import sc.common.model.queries.ReportDataMappingEOVORowImpl;
import sc.common.view.bean.ReportColumnBean;
import sc.common.view.bean.ReportDataBean;
import sc.common.view.bean.ReportDataMappingBean;
import sc.common.view.util.Util;

public class XmlReportBean {
    Util util=null;
    public XmlReportBean() {
        util=new Util();
    }
    
    
    public  int processReport(InputStream is,ReportDataBean bean,Connection con) throws Exception{
        
        int processedRecords=0;
        try{
            /* Parse XML Data */
            HashMap<Integer,HashMap<String,ReportColumnBean>> xmlData=parseXML(is,bean.getRootNode(),bean.getReportDataMappings());
            /* Parse XML Data */
            processedRecords=util.processRecordToDataBase(con, bean, xmlData); 
        }catch(Exception e){
           e.printStackTrace();
           throw e;
        }
        return processedRecords;
    }
    
    public  String getAttrValue(Node node,String attrName) {
        Node n=null;
        try{
            if ( ! node.hasAttributes() ) return "";
            NamedNodeMap nmap = node.getAttributes();
            if ( nmap == null ) return null;
            n = nmap.getNamedItem(attrName);
            if ( n == null ) return null;
            return n.getNodeValue();
        }catch(Exception e){
            e.printStackTrace();
        }
        return n.getNodeValue();
    }
    
    public   String getTextContent(Node parentNode,String childName) {
        Node n=null;
        try{
            NodeList nlist = parentNode.getChildNodes();
            for (int i = 0 ; i < nlist.getLength() ; i++) {
                n = nlist.item(i);
                String name = n.getNodeName();
                if ( name != null && name.equals(childName) ) return n.getTextContent();
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public  HashMap<Integer,HashMap<String,ReportColumnBean>> parseXML(InputStream is,String RootNode, List<ReportDataMappingBean> reportColName ){
        HashMap<Integer,HashMap<String,ReportColumnBean>> records=null;
        try{
            Reader reader = new InputStreamReader(is,"UTF-8");
            InputSource is2 = new InputSource(reader);
            is2.setEncoding("UTF-8");
            records=new HashMap<Integer,HashMap<String,ReportColumnBean>>();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document xmlDoc = builder.parse(is2);
            XPath xpath = XPathFactory.newInstance().newXPath();
            Object res = xpath.evaluate(RootNode,xmlDoc,XPathConstants.NODESET);
            NodeList nlist = (NodeList)res;
            for (int i = 0 ; i < nlist.getLength() ; i++) {
                HashMap<String,ReportColumnBean> reportCol=new  HashMap<String,ReportColumnBean>();
                Node node = nlist.item(i);
                for(ReportDataMappingBean bean:reportColName){
                    String columnVal=null;
                    if(bean.getReportColName()!=null){
                        columnVal=getTextContent(node,bean.getReportColName());
                        reportCol.put(bean.getReportColName(),new ReportColumnBean(bean.getReportColName(), columnVal));
                    }
                }
                records.put((i+1),reportCol);
            }

        } catch(Exception e){
            e.printStackTrace();
        }
        return records;
    }
    
}
