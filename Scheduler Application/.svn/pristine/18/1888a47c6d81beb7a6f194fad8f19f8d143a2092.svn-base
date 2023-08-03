package sc.common.view.backing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sc.common.biReport.PublicReportService;
import sc.common.view.bean.ReportDataBean;
import sc.common.view.bean.RevenueEmailBean;
import sc.common.view.mail.SendMail;
import sc.common.view.util.Constants;
import sc.common.view.util.ExceptionLog;
import sc.common.view.util.Util;

public class RevenueEmailIntegration {
     HashMap<String, String> setupMap = null;
     ReportDataBean bean=null;
     Util util=null;
     PublicReportService port=null;
     Connection con = null;
    public RevenueEmailIntegration(ReportDataBean bean, PublicReportService port, HashMap<String, String> setupMap, Connection con) {
        super();
        this.util = new Util();
        this.bean = bean;
        this.port = port;
        this.setupMap = setupMap;        
        this.con = con;
        
    }
    public int executeRevenueEmailIntegration(){
        int recordCount = 0;
        try{
            ArrayList<RevenueEmailBean> revenueEmailList = getTodaysRevenueNotification();
            if(revenueEmailList!=null && !revenueEmailList.isEmpty()){
                recordCount = revenueEmailList.size();
                for(RevenueEmailBean revenueBean : revenueEmailList){
                    List<String> emailRecList = new ArrayList<String>();
                    emailRecList.add(revenueBean.getEmailId());
                    String[] emailRecipientArr = emailRecList.toArray(new String[emailRecList.size()]);
                    final StringBuffer subject = new StringBuffer("Action Required: Create AFP -[Project Number:").append(revenueBean.getProjectNumber()).append(" Project Name:").append(revenueBean.getProjectName()).append("]"), body = new StringBuffer("<html><head><style> table { font-family: arial, sans-serif; border-collapse: collapse; width: 100%; } td, th { border: 1px solid #dddddd; text-align: left; padding: 8px; }</style>");
                    body.append("<p>Hello ");
                    body.append(revenueBean.getPersonName());
                    body.append(",</p>");
                    body.append("<p>For the below mentioned Contract/Project, Billing date is coming near. Kindly prepare the AFP.</p>");
                    body.append("<p>Ignore if already done.</p>");
                    body.append("<p>Contract Number:");
                    body.append(revenueBean.getContractNumber());
                    body.append("</p>");
                    body.append("<p>Contract Name:");
                    body.append(revenueBean.getContractName());
                    body.append("</p>");
                    body.append("<p>Project Number:"); 
                    body.append(revenueBean.getProjectNumber());
                    body.append("</p>");
                    body.append("<p>Project Name:"); 
                    body.append(revenueBean.getProjectName());
                    body.append("</p>");
                    body.append("<table><tr><th>Particulars</th><th>Start Date</th><th>End Date</th><th>Amount</th><th>Billing Notification Date</th></tr>");
                    body.append("<tr>");
                    body.append("<td>");
                    body.append(revenueBean.getDescription());
                    body.append("</td>");
                    body.append("<td>");
                    body.append(revenueBean.getStartDate());
                    body.append("</td>");
                    body.append("<td>");
                    body.append(revenueBean.getEndDate());
                    body.append("</td>");
                    body.append("<td>");
                    body.append(revenueBean.getAmount());
                    body.append("</td>");
                    body.append("<td>");
                    body.append(revenueBean.getBillingDate());
                    body.append("</td>");
                    body.append("</tr></table></head></html>");
                    SendMail.sendEmail(body.toString(), subject.toString(), emailRecipientArr); 
                }
            }
            util.updateSchedulerDate(Constants.SERVICE_TYPE_BOQ_REVENUE_NOTIF);
        
        }catch(Exception e){
            e.printStackTrace();
        }
        return recordCount;
    }
    private ArrayList<RevenueEmailBean>getTodaysRevenueNotification(){
        ArrayList<RevenueEmailBean> revenueList=new ArrayList<RevenueEmailBean>();
        try{
            String query="SELECT  \n" + 
            "    E.DISPLAY_NAME,\n" + 
            "    E.PERSON_NUMBER,\n" + 
            "    E.EMAIL_ID,\n" + 
            "    C.PROJECT_NUMBER,\n" + 
            "    C.PROJECT_NAME,\n" + 
            "    C.CONTRACT_NUMBER,\n" + 
            "    C.CONTRACT_NAME,\n" + 
            "    CM.DESCRIPTION,\n" + 
            "    TO_CHAR(CM.START_DATE,'DD-MM-YYYY')START_DATE,\n" + 
            "    TO_CHAR(CM.END_DATE,'DD-MM-YYYY')END_DATE,\n" + 
            "    TO_CHAR(CM.BILLING_NOTIFICATION_DATE,'DD-MM-YYYY')BILLING_NOTIFICATION_DATE,\n" + 
            "    CM.AMOUNT\n" + 
            "  FROM CB_CONTRACT_MAINTENANCE CM\n" + 
            "INNER JOIN CB_CONTRACT_HEADER C\n" + 
            "ON C.CONTRACT_NUMBER=CM.CONTRACT_NUMBER\n" + 
            "AND C.VERSON_NUMBER=CM.VERSON_NUMBER\n" + 
            "LEFT JOIN EMPLOYEE_MASTER E\n" + 
            "ON C.CREATED_BY=E.PERSON_ID\n" + 
            "WHERE CM.BILLING_NOTIFICATION_DATE BETWEEN (SELECT SCHEDULER_DATE FROM XX_SCH_SCHEDULER_DATE WHERE SERVICE_TYPE = 'REVENUE_NOTIFICATION') AND SYSDATE";
            
                try(PreparedStatement ps=con.prepareStatement(query)){
                    try(ResultSet rs=ps.executeQuery()){
                        while(rs.next()){
                            RevenueEmailBean revenueEmailBean=new RevenueEmailBean();
                            revenueEmailBean.setPersonNumber(rs.getString("PERSON_NUMBER")==null?"-":rs.getString("PERSON_NUMBER"));
                            revenueEmailBean.setContractNumber(rs.getString("CONTRACT_NUMBER"));
                            revenueEmailBean.setContractName(rs.getString("CONTRACT_NAME")==null?"-":rs.getString("CONTRACT_NAME"));
                            revenueEmailBean.setProjectNumber(rs.getString("PROJECT_NUMBER")==null?"-":rs.getString("PROJECT_NUMBER"));
                            revenueEmailBean.setProjectName(rs.getString("PROJECT_NAME")==null?"-":rs.getString("PROJECT_NAME"));
                            revenueEmailBean.setEmailId(rs.getString("EMAIL_ID"));
                            revenueEmailBean.setStartDate(rs.getString("START_DATE")==null?"-":rs.getString("START_DATE"));
                            revenueEmailBean.setEndDate(rs.getString("END_DATE")==null?"-":rs.getString("END_DATE"));
                            revenueEmailBean.setPersonName(rs.getString("DISPLAY_NAME")==null?"-":rs.getString("DISPLAY_NAME"));
                            revenueEmailBean.setAmount(rs.getString("AMOUNT")==null?"-":rs.getString("AMOUNT"));
                            revenueEmailBean.setBillingDate(rs.getString("BILLING_NOTIFICATION_DATE")==null?"-":rs.getString("BILLING_NOTIFICATION_DATE"));
                            revenueEmailBean.setDescription(rs.getString("DESCRIPTION")==null?"-":rs.getString("DESCRIPTION"));
                            revenueList.add(revenueEmailBean);
                        }
                    }
            }
        }catch(Exception e){
            e.printStackTrace();
            ExceptionLog.CreateExceptionLog(this.getClass().getName(), "getRevenueNotification", e,"");
        }
        return revenueList;
    }
 }
