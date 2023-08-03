package sc.common.view.util;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URL;


public class HttpService {
    
    
    public HttpService() {
        super();
    }
    
    public static String soapHttpPost(String destUrl, String postData, String authStr) throws Exception {
                
        URL url = null;
        HttpURLConnection conn = null;
        InputStream in = null;
        InputStreamReader iReader = null;
        BufferedReader bReader = null;
        String response = "";
        
        try {
        
            url = new URL(destUrl);
            conn = (HttpURLConnection) url.openConnection();
            if (conn == null) 
                return null;

            conn.setRequestProperty(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_XML);
            conn.setRequestProperty(Constants.SOAP_ACTION, Constants.HTTP_POST);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setFollowRedirects(true);
            conn.setAllowUserInteraction(false);
            conn.setRequestMethod(Constants.HTTP_POST);
            
            if(authStr!=null
                && !authStr.isEmpty()){                
                byte[] authBytes = authStr.getBytes(Constants.UTF8);
                String auth = Base64.encode(authBytes);
                conn.setRequestProperty(Constants.AUTHORIZATION, Constants.BASIC + Constants.SPACE+auth);
            }

            try(OutputStream out = conn.getOutputStream();){
                out.write(postData.getBytes(Constants.UTF8));
                out.flush();
            }       

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
                iReader = new InputStreamReader(conn.getInputStream());
            else             
                iReader = new InputStreamReader(conn.getErrorStream());

            bReader = new BufferedReader(iReader); String line;
            while ((line = bReader.readLine()) != null)
                response += line;            
                        
        }catch(IOException e){
            e.getMessage();
            return response;
        }catch(IllegalArgumentException e){
             e.getMessage();
            return response;
        }finally {
            try {
                if (iReader != null)
                    iReader.close();
                if (bReader != null)
                    bReader.close();
                if (in != null)
                    in.close();
                if (conn != null)
                    conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
                
        return response;
        
    }        

    
}
