package sc.common.view.util;

import com.oracle.ptsdemo.jwtaccelerator.vo.UserInfo;

import java.sql.Connection;

import sc.common.view.util.ConnectionManager;

import oracle.adf.share.ADFContext;

public class FusionSetupBean implements Constants {
    
    public FusionSetupBean() {
        super();
    }
   
    public String setFusionEndpoint() throws Exception {
           
           Util util = new Util();
            try(Connection cn = ConnectionManager.getConnection();){
               
                String endPoint = util.getGeneralStrings(cn, GENERAL_STRING_JWT_END_POINT);
                if(endPoint != null)
                    ADFContext.getCurrent().getPageFlowScope().put("JwtEndPoint", endPoint);
                else
                    throw new RuntimeException("Fusion endpoint not found.");
                
                if(Util.getSessionScope("jwt")==null
                    || Util.getSessionScope("jwt").toString().isEmpty())
                    throw new RuntimeException("JWT token not found.");
                
                Util.putPageFlowScope(ERROR_MESSAGE, "JWT not validated.");
                return "validateToken";
            }catch(Exception e){
                
                e.printStackTrace();
              //  ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), e);
                Util.putPageFlowScope(ERROR_MESSAGE, e.getMessage());
                return "error";
            }            
    }
    
    public String setUserDetails(){
        
        try(Connection con = ConnectionManager.getConnection()){
            
            if(Util.getSessionScope(USER_BEAN)!=null){
                
                UserInfo userInfo = (UserInfo) Util.getSessionScope(USER_BEAN);
                Util.putSessionScope(PERSON_ID, userInfo.getPersonId());                
                String role = Util.queryForString(USER_ROLE_QUERY, new Object[]{userInfo.getPersonId()}, con);
                
                if(role!=null){
                    
                    Util.putSessionScope(ROLE, role);   
                    
                    return "validToken";
                }else
                    throw new RuntimeException("User role not found.");                
            }else
                throw new RuntimeException("User details not found.");
        }catch(Exception e){
                        
            e.printStackTrace();
          //  ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), e);
            Util.putPageFlowScope(ERROR_MESSAGE, e.getMessage());
            return "error";
        }        
    }
    
    public static String getLoggedInPersonName() throws Exception {
        
        StringBuffer name = new StringBuffer("");
        if(Util.getSessionScope(USER_BEAN)!=null){
            
            UserInfo userInfo = (UserInfo) Util.getSessionScope(USER_BEAN);
            if(userInfo.getPersonId()==null) throw new RuntimeException("PERSON_ID not found.");
            if(userInfo.getFirstName()!=null)
                name.append(userInfo.getFirstName()).append(SPACE);
            else if(userInfo.getMiddleNames()!=null)
                name.append(userInfo.getMiddleNames()).append(SPACE);
            else if(userInfo.getLastName()!=null)
                name.append(userInfo.getLastName());            
        }
        return name.toString();
    }
    
    public static String getLoggedInPersonId() throws Exception {
        
        String personId=null;
        if(Util.getSessionScope(USER_BEAN)!=null){
            
            UserInfo userInfo = (UserInfo) Util.getSessionScope(USER_BEAN);
            if(userInfo.getPersonId()!=null){
                personId=userInfo.getPersonId();
            }else{
                throw new RuntimeException("PERSON_ID not found.");
            }
        }
        return personId;
    }
    
}

