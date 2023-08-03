package sc.common.view.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.sql.DataSource;

public class ConnectionManager {
    public ConnectionManager() {
        super();
    }
    
    public static Connection getConnection() {
        Connection conn = null;
        Context ctx;
        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup("jdbc/MobDS");

            if (ds != null) {
                conn = ds.getConnection();
                conn.setAutoCommit(false);
            }
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void releaseConnetion(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized Connection XgetConnection(){      
            
        Connection con = null;  
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@132.233.55.37:1521:UATDB", "", "");
            con.setAutoCommit(false);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return con;            
    }
    
    public static synchronized Connection P2TgetConnection(){      
            
        Connection con = null;  
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@132.145.55:1521/uatdb_pdb1..paassaasvcn.oraclevcn.com", "", "");
            con.setAutoCommit(false);
            System.out.print("Connection: " + con);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return con;            
    }
    
    public static void main(String[] arg){
        ConnectionManager cn = new ConnectionManager();
        cn.P2TgetConnection();
        
    }
    
}
