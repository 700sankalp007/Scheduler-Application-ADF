package sc.common.view.backing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sc.common.biReport.PublicReportService;
import sc.common.view.bean.ApInvoiceEmailBean;
import sc.common.view.bean.ReportDataBean;
import sc.common.view.mail.SendMail;
import sc.common.view.util.Constants;
import sc.common.view.util.ExceptionLog;
import sc.common.view.util.Util;

public class ApInvoiceEmailIntegration {
    
    HashMap<String, String> setupMap = null;
    ReportDataBean bean=null;
    Util util=null;
    PublicReportService port=null;
    Connection con = null;
    
    public ApInvoiceEmailIntegration(ReportDataBean bean, PublicReportService port, HashMap<String, String> setupMap, Connection con) {
        super();
        this.util = new Util();
        this.bean = bean;
        this.port = port;
        this.setupMap = setupMap;        
        this.con = con;
    }
    
//    public static void main(String[] args){
//        try{
//            
//            Class.forName("oracle.jdbc.driver.OracleDriver");  
//            con=DriverManager.getConnection("jdbc:oracle:thin:@//140.238.69.50:1521/pdb1.sub04060645130.paassaasvcn.oraclevcn.com","C##ZAMIL_TST","Zamil_Ad_123"); 
//            util=new Util();
//            setupMap= util.getSetupDetails(con, Constants.SETUP_TYPE_WEBSEVICE, Constants.SUB_TYPE_FUSION);
//            port =ServiceBean.getPublicReportServicePort(setupMap.get("HOST"));
//            
//            ArrayList<ApInvoiceEmailBean> apInvoiceEmailList = getCancelledRecords();
//            if(apInvoiceEmailList!=null && !apInvoiceEmailList.isEmpty()){
//                for(ApInvoiceEmailBean invoiceBean : apInvoiceEmailList){
//                    
//                    List<String> emailRecList = new ArrayList<String>();
//                    emailRecList.add(invoiceBean.getPersonEmail());
//                    String[] emailRecipientArr = emailRecList.toArray(new String[emailRecList.size()]);
//                    
//                    String body = "Invoice :"+invoiceBean.getInvoiceNumber()+ " has been Cancelled";
//                    SendMail.sendEmail(body, body, emailRecipientArr); 
//                }
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        
//    }
    
    public int executeApInvoiceEmailIntegration() throws Exception {
        int recordCount = 0;
        try{
            ArrayList<ApInvoiceEmailBean> apInvoiceEmailList = getCancelledRecords();
            if(apInvoiceEmailList!=null && !apInvoiceEmailList.isEmpty()){
                recordCount = apInvoiceEmailList.size();
                
                for(ApInvoiceEmailBean invoiceBean : apInvoiceEmailList){
                    
                    List<String> emailRecList = new ArrayList<String>();
                    emailRecList.add(invoiceBean.getPersonEmail());
                    String[] emailRecipientArr = emailRecList.toArray(new String[emailRecList.size()]);
                    
                    String body = "Invoice : "+invoiceBean.getInvoiceNumber()+ " has been Cancelled";
                    SendMail.sendEmail(body, body, emailRecipientArr); 
                }
            }
            util.updateSchedulerDate(Constants.SERVICE_TYPE_AP_INV_EMAIL);
            
        }catch(Exception e){
            e.printStackTrace();
            ExceptionLog.CreateExceptionLog(this.getClass().getName(),"executeApInvoiceEmailIntegration",e,"");  
            throw e;
        }
        return recordCount;
    }

    private ArrayList<ApInvoiceEmailBean> getCancelledRecords()throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<ApInvoiceEmailBean> beanList = new ArrayList<ApInvoiceEmailBean>();
        String selectQuery = "SELECT APINVM.INVOICE_STATUS,       " +
                             "       APINVM.INVOICE_NUMBER,       " +
                             "       SEM.EMAIL_ID,                " +
                             "       SEM.PERSON_NUMBER            " +
                             "FROM EEI_AP_INVOICE_MASTER_T APINVM," +
                             "     EEI_IPC_HEADER_T IPCM,         " +
                             "     EMPLOYEE_MASTER SEM " +
                             "WHERE APINVM.IPC_ID = IPCM.WC_HEADER_ID    " +
                             "AND   IPCM.CREATED_BY = SEM.PERSON_ID      " +
                             "AND   APINVM.INVOICE_STATUS = 'Cancelled'  " +
                             "AND   APINVM.LAST_UPDATE_DATE BETWEEN (SELECT SCHEDULER_DATE FROM XX_SCH_SCHEDULER_DATE WHERE SERVICE_TYPE = 'AP_INVOICE_EMAIL') AND SYSDATE";
        try{
            ps = con.prepareStatement(selectQuery);
            rs = ps.executeQuery();
            if (rs.next()) {
                ApInvoiceEmailBean invoiceBean = new ApInvoiceEmailBean();
                invoiceBean.setInvoiceStatus(rs.getString("INVOICE_STATUS"));
                invoiceBean.setInvoiceNumber(rs.getString("INVOICE_NUMBER"));
                invoiceBean.setPersonEmail(rs.getString("EMAIL_ID"));
                invoiceBean.setPersonNumber(rs.getString("PERSON_NUMBER"));
                beanList.add(invoiceBean);
            }
        }catch(Exception e){
            e.printStackTrace();
            ExceptionLog.CreateExceptionLog(this.getClass().getName(), "getCancelledRecords", e,"");
        }finally{
            if (rs != null) {
                rs.close(); 
            }
            if (ps != null) {
                ps.close();
            }
        }
        return beanList;
    }
    
}
