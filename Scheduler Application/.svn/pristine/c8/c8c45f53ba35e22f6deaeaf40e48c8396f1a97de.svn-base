package sc.common.view.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ExceptionLog {
    
    private String errorMessage;
    private String transId;
    private String tableName;
    private String className;
    private String methodName;
    private Exception errorTrace;
    
    public ExceptionLog(String jobId,String className,String methodName,Exception errorTrace) {
        
        this.className = className;
        this.methodName = methodName;
        this.errorTrace = errorTrace;
        CreateExceptionLog(className,methodName,errorTrace,jobId);
    }


    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getTransId() {
        return transId;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setErrorTrace(Exception errorTrace) {
        this.errorTrace = errorTrace;
    }

    public Exception getErrorTrace() {
        return errorTrace;
    }


    /**To get the String representation of the exception object
     * @param e     Exception object which needs to be converted to String
     * @return      String reprensentation of the exception object
     */
    public static String getStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        if (e != null) {
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
        }
        return stringWriter.toString();
    }


    /**To insert the exception into the databse table
     * @param beanName      Name of the bean from which the exception came
     * @param methodName    Name of the bean from which the method came
     * @param exception     Actual exception object
     */
    public  static void CreateExceptionLog(String beanName, String methodName, Exception exception,String jobId) {
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConnectionManager.getConnection();
            String seqNumberStr = Util.getSequenceValue(conn, "XX_SCH_EXCEPTION_LOG_SEQ");
            StringBuffer insertStmt = new StringBuffer();
            insertStmt.append("INSERT INTO XX_SCH_EXCEPTION_LOG (EXCEPTION_ID,BEAN_NAME ,METHOD_NAME ,ERROR_MESSAGE,JOB_ID)");
            insertStmt.append(" VALUES (?,?,?,?,?)");
            pstmt = conn.prepareStatement(insertStmt.toString());
            pstmt.setString(1, seqNumberStr.toString());
            pstmt.setString(2, beanName);
            pstmt.setString(3, methodName);
            pstmt.setString(4, getStackTrace(exception));
            pstmt.setString(5, jobId);
            pstmt.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                ConnectionManager.releaseConnetion(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
