package sc.common.view.util;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URL;

import java.nio.charset.StandardCharsets;

public class HttpServiceBean {
    
    public static final String  CONTENT_TYPE = "Content-Type",
                                CONTENT_TYPE_JSON = "application/json",                                
                                AUTHORIZATION = "Authorization",
                                BASIC = "Basic",
                                SPACE = " ",
                                CONTENT_TYPE_XML = "text/xml; charset=UTF-8",
                                SOAP_ACTION = "SOAPAction",
                                HTTP_POST = "POST",
                                HTTP_PATCH = "PATCH",
                                HTTP_GET = "GET",
                                HTTP_DELETE = "DELETE";

    
    public HttpServiceBean() {
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

            conn.setRequestProperty(CONTENT_TYPE, CONTENT_TYPE_XML);
            conn.setRequestProperty(SOAP_ACTION, HTTP_POST);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setFollowRedirects(true);
            conn.setAllowUserInteraction(false);
            conn.setRequestMethod(HTTP_POST);
            
            if(authStr!=null
                && !authStr.isEmpty()){                
                byte[] authBytes = authStr.getBytes(StandardCharsets.UTF_8);
                String auth = Base64.encode(authBytes);
                conn.setRequestProperty(AUTHORIZATION, BASIC+SPACE+auth);
            }

            try(OutputStream out = conn.getOutputStream();){
                out.write(postData.getBytes(StandardCharsets.UTF_8));
                out.flush();
            }       

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK
                 && conn.getInputStream()!=null) 
                iReader = new InputStreamReader(conn.getInputStream());
            else if (conn.getErrorStream()!=null)                   
                iReader = new InputStreamReader(conn.getErrorStream());                   

            if(iReader!=null){
                
                bReader = new BufferedReader(iReader); String line;
                while ((line = bReader.readLine()) != null)
                    response += line;            
            }
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

    
    public static void restHttpPost(String destUrl, String postData, String authStr, String requestMethod, StringBuffer response,  int responseCode) throws RuntimeException, Exception {
                   
           URL url = null;
           HttpURLConnection conn = null;
           InputStream in = null;
           InputStreamReader iReader = null;
           BufferedReader bReader = null;

           
           try {
               url = new URL(destUrl);
               conn = (HttpURLConnection) url.openConnection();
               conn.setRequestProperty(Constants.CONTENT_TYPE,Constants.CONTENT_TYPE_JSON);
            
                if(requestMethod.equals(Constants.HTTP_PATCH)){
                   conn.setRequestProperty("X-HTTP-Method-Override", Constants.HTTP_PATCH);
                }
               
               conn.setRequestMethod(Constants.HTTP_POST);
               conn.setDoOutput(true);
               conn.setDoInput(true);
               conn.setUseCaches(false);
               conn.setFollowRedirects(true);
               conn.setAllowUserInteraction(false);          
               if(authStr!=null
                   && !authStr.isEmpty()){                
                   byte[] authBytes = authStr.getBytes(StandardCharsets.UTF_8);
                   String auth = Base64.encode(authBytes);
                   conn.setRequestProperty(Constants.AUTHORIZATION,Constants.BASIC+Constants.SPACE+auth);
               }
               try(OutputStream out = conn.getOutputStream();){
                   out.write(postData.getBytes(StandardCharsets.UTF_8));
                   out.flush();
               }       
               if (conn.getResponseCode() == responseCode) {
                   iReader = new InputStreamReader(conn.getInputStream());
               }
               else             
                   iReader = new InputStreamReader(conn.getErrorStream());
               
               bReader = new BufferedReader(iReader); String line;
               while ((line = bReader.readLine()) != null)
                   response.append(line);            

               if(conn.getResponseCode() != responseCode) {
                   throw new RuntimeException(conn.getResponseCode()+" : "+conn.getResponseMessage()); 
               }
           }catch(IOException e){
               e.printStackTrace();
           }catch(IllegalArgumentException e){
               e.printStackTrace();
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
           
       }    
    
    
    
    public static void restHttpGet(String destUrl, String authStr, StringBuffer response,  int responseCode) throws RuntimeException, Exception {
                   
           URL url = null;
           HttpURLConnection conn = null;
           InputStream in = null;
           InputStreamReader iReader = null;
           BufferedReader bReader = null;
           
           try {
               url = new URL(destUrl);
               conn = (HttpURLConnection) url.openConnection();
               conn.setRequestProperty(Constants.CONTENT_TYPE,Constants.CONTENT_TYPE_JSON);
               conn.setRequestMethod(Constants.HTTP_GET);
               conn.setDoOutput(true);
               conn.setDoInput(true);
               conn.setUseCaches(false);
               conn.setFollowRedirects(true);
               conn.setAllowUserInteraction(false);          
               if(authStr!=null
                   && !authStr.isEmpty()){                
                   byte[] authBytes = authStr.getBytes(StandardCharsets.UTF_8);
                   String auth = Base64.encode(authBytes);
                   conn.setRequestProperty(Constants.AUTHORIZATION,Constants.BASIC+Constants.SPACE+auth);
               }      
               if (conn.getResponseCode() == responseCode) {
                   iReader = new InputStreamReader(conn.getInputStream());
               }
               else             
                   iReader = new InputStreamReader(conn.getErrorStream());
               
               bReader = new BufferedReader(iReader); String line;
               while ((line = bReader.readLine()) != null)
                   response.append(line);            

               if(conn.getResponseCode() != responseCode) {
                   throw new RuntimeException(conn.getResponseCode()+" : "+conn.getResponseMessage()); 
               }
           }catch(IOException e){
               e.printStackTrace();
           }catch(IllegalArgumentException e){
               e.printStackTrace();
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
           
       }
    
    
    public static int restHttpPost(String destUrl, String postData, String authStr, String requestMethod, int responseCode, StringBuffer response) throws Exception {
                   
           URL url = null;
           HttpURLConnection conn = null;
           InputStream in = null;
           InputStreamReader iReader = null;
           BufferedReader bReader = null;
           
           try {
               url = new URL(destUrl);
               conn = (HttpURLConnection) url.openConnection();
               if (conn == null) 
                   throw new RuntimeException("HttpURLConnection not established.");
                   
               conn.setRequestProperty(CONTENT_TYPE,CONTENT_TYPE_JSON);            
               if(requestMethod.equals(HTTP_PATCH)){
                   conn.setRequestProperty("X-HTTP-Method-Override", HTTP_PATCH);                
                   conn.setRequestMethod(HTTP_POST);
               }else
                   conn.setRequestMethod(requestMethod);
               
               if(requestMethod.equals(HTTP_GET))
                    conn.setDoOutput(false);
               else
                   conn.setDoOutput(true);
               
               conn.setDoInput(true);
               conn.setUseCaches(false);
               conn.setFollowRedirects(true);
               conn.setAllowUserInteraction(false);          
               if(authStr!=null
                   && !authStr.isEmpty()){                
                   byte[] authBytes = authStr.getBytes(StandardCharsets.UTF_8);
                   String auth = Base64.encode(authBytes);
                   conn.setRequestProperty(AUTHORIZATION,BASIC+SPACE+auth);
               }
               
               if(!requestMethod.equals(HTTP_GET)
                   && !requestMethod.equals(HTTP_DELETE))
                   try(OutputStream out = conn.getOutputStream();){
                       out.write(postData.getBytes(StandardCharsets.UTF_8));
                       out.flush();
                   }       
               
               if (conn.getResponseCode() == responseCode
                    && conn.getInputStream()!=null) 
                   iReader = new InputStreamReader(conn.getInputStream());
               else if (conn.getErrorStream()!=null)                   
                   iReader = new InputStreamReader(conn.getErrorStream());                   
               
               if(iReader!=null){
                   
                   bReader = new BufferedReader(iReader); String line;
                   while ((line = bReader.readLine()) != null)
                   response.append(line);            
               }

               return conn.getResponseCode();
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
           
       }

    public static int restHttpPost(String destUrl, String postData, String authStr, String requestMethod, String contentType, StringBuffer response,  int responseCode) throws Exception {
                   
           URL url = null;
           HttpURLConnection conn = null;
           InputStream in = null;
           InputStreamReader iReader = null;
           BufferedReader bReader = null;
           
           try {
               url = new URL(destUrl);
               conn = (HttpURLConnection) url.openConnection();
               if (conn == null) 
                   throw new RuntimeException("HttpURLConnection not established.");
                   
               conn.setRequestProperty(CONTENT_TYPE, contentType);            
               if(requestMethod.equals(HTTP_PATCH)){
                   conn.setRequestProperty("X-HTTP-Method-Override", HTTP_PATCH);                
                   conn.setRequestMethod(HTTP_POST);
               }else
                   conn.setRequestMethod(requestMethod);
               
               if(requestMethod.equals(HTTP_GET))
                    conn.setDoOutput(false);
               else
                   conn.setDoOutput(true);
               
               conn.setDoInput(true);
               conn.setUseCaches(false);
               conn.setFollowRedirects(true);
               conn.setAllowUserInteraction(false);          
               if(authStr!=null
                   && !authStr.isEmpty()){                
                   byte[] authBytes = authStr.getBytes(StandardCharsets.UTF_8);
                   String auth = Base64.encode(authBytes);
                   conn.setRequestProperty(AUTHORIZATION,BASIC+SPACE+auth);
               }
               
               if(!requestMethod.equals(HTTP_GET)
                   && !requestMethod.equals(HTTP_DELETE))
                   try(OutputStream out = conn.getOutputStream();){
                       out.write(postData.getBytes(StandardCharsets.UTF_8));
                       out.flush();
                   }       
               
               if (conn.getResponseCode() == responseCode) {
                   iReader = new InputStreamReader(conn.getInputStream());
               }
               else             
                   iReader = new InputStreamReader(conn.getErrorStream());
               
               bReader = new BufferedReader(iReader); String line;
               while ((line = bReader.readLine()) != null)
                   response.append(line);            

               return conn.getResponseCode();
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
           
       }

    public static int soapHttpPost(String destUrl, String postData, String authStr, StringBuffer response, int responseCode) throws Exception {
                
        URL url = null;
        HttpURLConnection conn = null;
        InputStream in = null;
        InputStreamReader iReader = null;
        BufferedReader bReader = null;
        
        try {
        
            url = new URL(destUrl);
            conn = (HttpURLConnection) url.openConnection();
            if (conn == null) 
                throw new RuntimeException("HttpURLConnection not established.");

            conn.setRequestProperty(CONTENT_TYPE, CONTENT_TYPE_XML);
            conn.setRequestProperty(SOAP_ACTION, HTTP_POST);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setFollowRedirects(true);
            conn.setAllowUserInteraction(false);
            conn.setRequestMethod(HTTP_POST);
            
            if(authStr!=null
                && !authStr.isEmpty()){                
                byte[] authBytes = authStr.getBytes(StandardCharsets.UTF_8);
                String auth = Base64.encode(authBytes);
                conn.setRequestProperty(AUTHORIZATION, BASIC+SPACE+auth);
            }

            try(OutputStream out = conn.getOutputStream();){
                out.write(postData.getBytes(StandardCharsets.UTF_8));
                out.flush();
            }       

            if (conn.getResponseCode() == responseCode)
                iReader = new InputStreamReader(conn.getInputStream());
            else             
                iReader = new InputStreamReader(conn.getErrorStream());

            bReader = new BufferedReader(iReader); String line;
            while ((line = bReader.readLine()) != null)
                response.append(line);

            return conn.getResponseCode();                        
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
    }        
    
}