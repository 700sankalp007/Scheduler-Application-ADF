package sc.common.view.backing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import sc.common.view.util.ConnectionManager;
import sc.common.view.util.ExceptionLog;

public class TicketBookingBean implements Job {
    public TicketBookingBean() {
      super();
      
    }
    /**
       * contextRootPath , Path to root folder of the application
       * which is used to get reference of a template to generate PDF report.
       **/
      private String contextRootPath;
    /**
     * Parameterized constructor which initializes object with contextRootPath.
     * @param contextRootPath Root path of the application
     **/
    public TicketBookingBean(String contextRootPath){
        super();
        this.contextRootPath = contextRootPath; 
    }

    /**
         * Overridden method of JOB, which executes the task.
         * @param jobExecutionContext, object which holds job information and context
         * @throws JobExecutionException
         **/
        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            try {
                String schedulerId = jobExecutionContext.getJobDetail().getJobDataMap().get("SchedulerId").toString();
                this.contextRootPath = jobExecutionContext.getJobDetail().getJobDataMap().get("contextRootPath").toString();
                //System.out.println("contextRootPath: "+contextRootPath);
                TicketBookingMain();
            } catch (Exception e) {
                ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), e, null);
                e.printStackTrace();
            }
        }
    public void TicketBookingMain(){
        Statement stmt = null;
        ResultSet rs = null;
        try(Connection con  = ConnectionManager.getConnection();) {
           stmt = con.createStatement();
           String sql =
           "select * from (select APPROVAL_HOURS,REQ_MASTER_ID,FROM_USER,OPTION_STATUS,SYSDATE,FROM_USER_NAME,PERSON_ID,TO_USER,CREATED_DATE,(trunc((SYSDATE - CREATED_DATE) * 24,2)) as diff_hours,TO_USER_NAME,scheduler_flag,OPTION_DTL_ID from sshr_travel_ticket_section) tbl where tbl.OPTION_STATUS='Ticket Options Sent' and tbl.diff_hours > tbl.APPROVAL_HOURS and tbl.SCHEDULER_FLAG='N'";
           rs = stmt.executeQuery(sql);
           Map<String, String> reqMasterDetail = new HashMap<String, String>();
           Map<String, String> notifmaster = new HashMap<String, String>();
           while (rs.next()){
                   reqMasterDetail.clear(); 
                   Date createdDate =rs.getDate("CREATED_DATE");
                   String personId = rs.getString("PERSON_ID");
                   String optionId =   rs.getString("OPTION_DTL_ID");
                   String reqMasterId = rs.getString("REQ_MASTER_ID");
                   reqMasterDetail.put("personId", rs.getString("PERSON_ID"));
                   reqMasterDetail.put("travelAssis", rs.getString("FROM_USER"));
                   reqMasterDetail.put("reqMasterId", rs.getString("REQ_MASTER_ID"));
                   reqMasterDetail.put("emp", rs.getString("TO_USER"));
                   reqMasterDetail.put("fromName", rs.getString("FROM_USER_NAME"));
                   reqMasterDetail.put("userName", rs.getString("TO_USER_NAME"));
                   reqMasterDetail.put("currentDate",createdDate.toString());
                   if(reqMasterDetail.get("reqMasterId") != null){
                    createTravelTicketEntry(reqMasterDetail);
                    updateSchedulerFlag(optionId, reqMasterId);
                    notifmaster = getNotificationMasterId(reqMasterId);
                    createNotification(reqMasterDetail);
                   }
                      con.commit();
                }
            } catch (Exception e) {
                    ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(),e, null);
                    e.printStackTrace();
                }finally{
                       try {
                           if (stmt != null) {
                               stmt.close();
                           }
                           if (rs != null) {
                               rs.close();
                           }
                       } catch (SQLException sqle) {
                           ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), sqle, null);
                           sqle.printStackTrace();
                       }
                    }
                 }
    
    public void createNotification(Map<String, String> reqMasterDetail) {
        PreparedStatement notificationPreparedStatement = null;
        try(Connection con = ConnectionManager.getConnection();) {
            String reqMasterId = reqMasterDetail.get("reqMasterId");
            String personName = reqMasterDetail.get("userName");
            String personId = reqMasterDetail.get("personId");
            String getNotificationMstId = getNextIdFromSequence("NotifMaster");
            String travelAssId = reqMasterDetail.get("travelAssis");
            String notifSubject =
                "Ticket Booking for " + personName + " has been autoapproved";
            String notifMstSql =
            "Insert into SSHR_NOTIFICATION_MASTER (NOTIF_MASTER_ID,OBJECT_ID,CREATED_DATE) values (?,?,?)";
            notificationPreparedStatement = con.prepareStatement(notifMstSql);
            notificationPreparedStatement.setString(1, getNotificationMstId);
            notificationPreparedStatement.setString(2, reqMasterId);
            notificationPreparedStatement.setDate(3,java.sql.Date.valueOf(java.time.LocalDate.now()));
            int notifMasterInsert = notificationPreparedStatement.executeUpdate();
            if (notifMasterInsert == 0){
//                System.out.println("Notification master not inserted");
            }else{
//                System.out.println("Notification masetr inserted ");
            }
            notificationPreparedStatement.close();
            createNotificationDtl(getNotificationMstId,travelAssId,notifSubject,personId);
        } catch (SQLException e) {
            ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), e, null);
            e.printStackTrace();
        }finally{
            try {
                if (notificationPreparedStatement != null) {
                    notificationPreparedStatement.close();
                }
            } catch (SQLException sqle) {
                ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), sqle, null);
                sqle.printStackTrace();
            }
        }
    }
    
    public String getNextIdFromSequence(String type){
        String masterId = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try(Connection con = ConnectionManager.getConnection()) {
            String sql = null;
            if (type.equals("NotifMaster")) {
                sql = "select SSHR_NOTIF_MASTER_SEQ.nextval from dual";
            } else if (type.equals("optionDetailsId")) {
                sql = "select SSHR_TRAVEL_SECT_SEQ.nextval from dual";
            } else if (type.equals("NotifDtl")){
                sql = "select SSHR_NOTIF_DETAIL_SEQ.nextval from dual";
            } 
            preparedStatement = con.prepareStatement(sql);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                masterId = rs.getString(1);
            }
            preparedStatement.close();
        } catch (SQLException e) {
            ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), e, null);
            e.printStackTrace();
        }finally{
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException sqle) {
                ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), sqle, null);
                sqle.printStackTrace();
            }
        }
        return masterId;
    }
    public void createTravelTicketEntry(Map<String, String> ticketDetails){
        PreparedStatement preparedStatement = null;
        try(Connection con = ConnectionManager.getConnection();){
            String optionDetailsId = getNextIdFromSequence("optionDetailsId");
            String reqMasterId = ticketDetails.get("reqMasterId");
            String fromUser = ticketDetails.get("emp");
            String toUser = ticketDetails.get("travelAssis");
            String toName = ticketDetails.get("fromName");
            String fromUserName = ticketDetails.get("userName");
            String currentDateSting  = ticketDetails.get("currentDate");
            Date now = new SimpleDateFormat ("dd-MM-yyyy").parse(currentDateSting);
            String sql =
            "INSERT INTO SSHR_TRAVEL_TICKET_SECTION(OPTION_DTL_ID, OPTION_DETAILS, OPTION_COMMENTS, CREATED_DATE, REQ_MASTER_ID, OPTION_STATUS, FROM_USER, FROM_USER_NAME, TO_USER, TO_USER_NAME,PERSON_ID,SCHEDULER_FLAG) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
           preparedStatement = con.prepareStatement(sql);
           preparedStatement.setString(1, optionDetailsId);
           preparedStatement.setString(2, "AutoApproved");
           preparedStatement.setString(3, "Default Option Selected");
           preparedStatement.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now()));
           preparedStatement.setString(5, reqMasterId);
           preparedStatement.setString(6, "Ticket Options Accepted" );
           preparedStatement.setString(7,fromUser);
           preparedStatement.setString(8, fromUserName);
           preparedStatement.setString(9, toUser);
           preparedStatement.setString(10, toName);
           preparedStatement.setString(11, fromUser);
           preparedStatement.setString(12, "Y");
           int ticketInsert = preparedStatement.executeUpdate();
//           System.out.println("Ticket Count"+ticketInsert);
            if (ticketInsert == 0) {
//                System.out.println("Ticket Options inserted failed");
            } else {
//                System.out.println("Ticket Options inserted");
            }
       } catch (Exception e) {
            ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), e, null);
            e.printStackTrace();
        }finally{
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException sqle) {
                ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), sqle, null);
                sqle.printStackTrace();
            }
        }
       
    }
    
   public void createNotificationDtl(String notificationMstId, String approverId, String notifSubject,
                                      String personId){
           PreparedStatement notifDtlPS = null;
           try(Connection con = ConnectionManager.getConnection();) {
              String getNotifiactionDtlId = getNextIdFromSequence("NotifDtl");
              String notifDtlSql =
            "Insert into SSHR_NOTIFICATION_DTL(NOTIF_DTL_ID,NOTIF_MASTER_ID,PERSON_ID,STATUS,NOTIF_SUBJECT,NOTIF_TYPE,FROM_USER) values (?,?,?,?,?,?,?)";
              notifDtlPS = con.prepareStatement(notifDtlSql);
              notifDtlPS.setString(1, getNotifiactionDtlId);
              notifDtlPS.setString(2, notificationMstId);
              notifDtlPS.setString(3, approverId);
              notifDtlPS.setString(4, "O");
              notifDtlPS.setString(5, notifSubject);
              notifDtlPS.setString(6, "A");
              notifDtlPS.setString(7, personId);
              int notifDtlInsert = notifDtlPS.executeUpdate();
//              System.out.println("NotificDetail Count"+notifDtlInsert);
              if (notifDtlInsert == 0) {
//                  System.out.println("Notification detail not inserted ");
              } else {
//                  System.out.println("Notification detail is inserted");
              }
                  notifDtlPS.close();
                  } catch (SQLException sqle) {
                      ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), sqle, null);
                      sqle.printStackTrace();
                  }finally{
                      try {
                          if (notifDtlPS != null) {
                              notifDtlPS.close();
                          }
                      } catch (SQLException sqle) {
                          ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), sqle, null);
                          sqle.printStackTrace();
                      }
                }
         }   
    public void updateSchedulerFlag(String optionID, String reqMasterId){
            PreparedStatement ps = null;   
               try(Connection con = ConnectionManager.getConnection()){
                String  sql ="UPDATE SSHR_TRAVEL_TICKET_SECTION SET SCHEDULER_FLAG= 'Y' WHERE OPTION_DTL_ID=? and REQ_MASTER_ID=? and OPTION_STATUS='Ticket Options Sent'";                                   
                ps =  con.prepareStatement(sql) ;
                ps.setString(1, optionID);
                ps.setString(2,reqMasterId);
                int rows =  ps.executeUpdate();
                if(rows == 0){
//                    System.out.println("Scheduler flag not updated");
                }else{
//                     System.out.println("Scheduler flag  updated");           
                 }  
                ps.close();  
               }catch(SQLException e){
                ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), e, null);
                e.printStackTrace();
            }finally{
                      try {
                          if (ps != null) {
                              ps.close();
                          } 
                      } catch (SQLException sqle){
                          ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), sqle, null);
                          sqle.printStackTrace();
                      }
                } 
        }
    public void closeNotofication(String notifMasterId){
     PreparedStatement ps = null;   
        try(Connection con = ConnectionManager.getConnection()){
         String  sql ="UPDATE SSHR_NOTIFICATION_DTL SET STATUS= 'C' WHERE notif_master_id=? ";                                   
         ps =  con.prepareStatement(sql) ;
         ps.setString(1, notifMasterId);
         int rows =  ps.executeUpdate();
         con.commit();
         if(rows == 0){
//             System.out.println("Notification status not updated");
         }else{
//              System.out.println("Notification status updated");           
          }  
        
        }catch(SQLException e){
         e.printStackTrace();
         ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), e, null);
     }finally{
               try {
                   if (ps != null) {
                       ps.close();
                   } 
               } catch (SQLException sqle){
                   ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), sqle, null);
                   sqle.printStackTrace();
               }
         } 
    }
    public HashMap<String,String> getNotificationMasterId(String reqId) throws SQLException {
        PreparedStatement ps = null;
        HashMap<String,String> notifMaster = null;
        try(Connection con  = ConnectionManager.getConnection();){
            ResultSet rs = null;
            String sql =
           "select notif_master_id from sshr_notification_master where object_id=?";
            ps = con.prepareStatement(sql);
            ps.setString(1, reqId);
            rs = ps.executeQuery();
            notifMaster = new HashMap<String,String>();
            while (rs.next()){
               notifMaster.clear(); 
               String notifmasterID = rs.getString("NOTIF_MASTER_ID");
//               System.out.println(notifmasterID);
               closeNotofication(notifmasterID);
            }
        }catch(SQLException sqle){
          ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), sqle, null);
          sqle.printStackTrace();   
        }finally{
               try {
                   if (ps != null) {
                       ps.close();
                   } 
               } catch (SQLException sqle){
                   ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), sqle, null);
                   sqle.printStackTrace();
               }
         } 
        return notifMaster;
    }
            
}
