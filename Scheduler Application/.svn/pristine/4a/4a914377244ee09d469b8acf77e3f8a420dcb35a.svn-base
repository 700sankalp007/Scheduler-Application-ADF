package sc.common.view.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.util.ArrayList;

public class ExceptionLogParent {
    public ExceptionLogParent() {
        super();
    }
    
    private ArrayList<ExceptionLog> logList = new ArrayList<ExceptionLog>();

    public void setLogList(ArrayList<ExceptionLog> logList) {
        this.logList = logList;
    }

    public ArrayList<ExceptionLog> getLogList() {
        return logList;
    }
    
    public void addLog(ExceptionLog log) {
        logList.add(log);
    }
    
    public static String getStackTrace(Exception e) throws Exception {
        try {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            return stringWriter.toString();            
        } catch(Exception e1) {
            e1.printStackTrace();
            throw e1;
        }
    }
    
    /**To insert the exception into the databse table
     * @param beanName      Name of the bean from which the exception came
     * @param methodName    Name of the bean from which the method came
     * @param exception     Actual exception object
     */
    public void insertIntoExceptionLog(Connection cn) throws Exception {
        PreparedStatement psmt = null;
        try {
            StringBuffer sqlStmt = new StringBuffer();
            sqlStmt.append("INSERT INTO XX_SCH_EXCEPTION_LOG(EXCEPTION_ID,ERROR_MESSAGE,TRANS_ID,TABLE_NAME,CLASS_NAME,METHOD_NAME,ERROR_TRACE)");
            sqlStmt.append(" VALUES(?,?,?,?,?,?,?,?,?)");
            psmt = cn.prepareStatement(sqlStmt.toString());
            for(int i=0;i<getLogList().size();i++) {
                ExceptionLog exceptionLog = getLogList().get(i);                 
                psmt.setString(1, Util.getSequenceValue(cn,"XX_SCH_EXCEPTION_LOG_SEQ"));
                psmt.setString(2,exceptionLog.getErrorMessage());                
                psmt.setString(3,exceptionLog.getTransId());
                psmt.setString(4,exceptionLog.getTableName());
                psmt.setString(5,exceptionLog.getClassName());
                psmt.setString(6,exceptionLog.getMethodName());
                psmt.setString(7,getStackTrace(exceptionLog.getErrorTrace()));
                psmt.executeUpdate();
            }
            cn.commit();
        } catch(Exception e) {
            e.printStackTrace();
            cn.rollback();
        } finally {
            if(psmt != null) {
                psmt.close();
            }
        }
    }
}
