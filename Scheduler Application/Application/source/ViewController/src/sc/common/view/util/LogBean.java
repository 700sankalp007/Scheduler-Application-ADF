package sc.common.view.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

public class LogBean {
    
    public static String isDebugEnabled(Connection cn,String module) throws Exception{
        String debugEnabled = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT DEBUG_ENABLED FROM EI_LOGGING WHERE MODULE = ?";
            pst = cn.prepareStatement(sql);
            pst.setString(1,module);
            rs = pst.executeQuery();
            if(rs.next()) {
                debugEnabled = rs.getString("DEBUG_ENABLED");
            }            
            return debugEnabled;                
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
        }

    }
    
    public static void writeLog(String logName,String logMessage,String level,String debugEnabledParam){
        try {
            Logger logger=Logger.getLogger(logName);
            if(debugEnabledParam != null && debugEnabledParam.equals("Y") && level.equals(Constants.LOG_DEBUG)) {
                logger.debug(logMessage);
            } else if(level.equals(Constants.LOG_ERROR)) {
                logger.error(logMessage);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
