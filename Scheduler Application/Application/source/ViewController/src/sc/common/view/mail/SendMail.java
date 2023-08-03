package sc.common.view.mail;

import com.oracle.webservices.impl.nonanonresponseendpointsupport.tube.BindingUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import oracle.jbo.server.DBTransaction;

import sc.common.view.util.ConnectionManager;
import sc.common.view.util.ExceptionLog;

public class SendMail {
    public SendMail() {
        super();
    }
    
    public static void sendEmail(String content, String subject, String... recieverEmails) throws Exception {
        Connection con = null;
        ResultSet rs = null;
        try {
            con=ConnectionManager.getConnection();
            PreparedStatement ptst =
                con.prepareStatement("select HOST,EMAIL_ADDRESS, USER_NAME, PASSWORD, PORT from XX_PAAS_SETUP_DTL " +
                                     " where setup_type='MAIL' and sub_type='MAIL'");
            ptst.execute();
            rs = ptst.getResultSet();
            rs.next();
            Map<String, String> hostMap = new HashMap<>();
            hostMap.put("HOST", rs.getString(1));
            hostMap.put("username", rs.getString(3));
            hostMap.put("password", rs.getString(4));
            hostMap.put("port", rs.getString(5));
            hostMap.put("from", rs.getString(2));

            Properties props = new Properties();
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.ssl.trust", hostMap.get("HOST"));
            props.setProperty("mail.smtp.host", hostMap.get("HOST"));
            props.setProperty("mail.smtp.port", hostMap.get("port"));
            props.put("mail.smtp.EnableSSL.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(hostMap.get("username"), hostMap.get("password"));
                }
            });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(hostMap.get("from")));

            message.setSubject(subject);
            message.setContent(content, "text/html; charset=utf-8");
            for (String address : recieverEmails) {
                if (address != null)
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(address));

            }
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionLog.CreateExceptionLog("SendMail", "sendEmail", e,"");
            throw e;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                ExceptionLog.CreateExceptionLog("SendMail", "sendEmail", e,"");
            }
        }
    }
    public static void sendMail(byte[] attachment,Map<String, String> hostMap,String from, List<String> toList,
                    List<String> ccList,String subject,String body,String filename){
        try{            Properties props = new Properties();
                    props.put("mail.smtp.host", hostMap.get("HOST"));
                    props.put("mail.smtp.port", hostMap.get("port"));
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.starttls.enable", "true");
            
                
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(hostMap.get("username"), hostMap.get("password"));
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));

            message.setSubject(subject);

            Multipart multipart = new MimeMultipart();

            BodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(body==null?" ":body, "text/html; charset=utf-8");
            htmlPart.setDisposition(BodyPart.INLINE);
            multipart.addBodyPart(htmlPart);

                    MimeBodyPart messageBodyOutputPart = new MimeBodyPart();
                  //  BlobDomain blob = atm.getFileData();
                   // byte[] data = blob.getBytes(1l, (int) blob.getLength());
                    if (attachment == null) {
                        attachment = "".getBytes();
                    }
                    ByteArrayDataSource attachmentOutputSource = new ByteArrayDataSource(attachment, "application/pdf");
                    messageBodyOutputPart.setDataHandler(new DataHandler(attachmentOutputSource));
                    messageBodyOutputPart.setFileName(filename);
                    multipart.addBodyPart(messageBodyOutputPart);
                
    
            message.setContent(multipart);
            if (toList != null && !toList.isEmpty()) {
                for (String s : toList) {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(s.toString()));
                }
            }
            if (ccList != null && !ccList.isEmpty()) {
                for (String s : ccList) {
                    message.addRecipient(Message.RecipientType.CC, new InternetAddress(s.toString()));
                }
            }
            Transport.send(message);
        }catch(Exception e){
            ExceptionLog.CreateExceptionLog("SendMail", "sendEmail", e,"");
        }
    }
    public static void sendMail(Map<String, String> hostMap,String from, List<String> toList,
                    List<String> ccList,String subject,String body){
        try{            
            Properties props = new Properties();
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.ssl.trust", hostMap.get("HOST"));
            props.setProperty("mail.smtp.host", hostMap.get("HOST"));
            props.setProperty("mail.smtp.port", hostMap.get("port"));
            props.put("mail.smtp.EnableSSL.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(hostMap.get("username"), hostMap.get("password"));
                }
            });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(hostMap.get("from")));

            message.setSubject(subject);
            message.setContent(body, "text/html; charset=utf-8");
            List<Address> receipents = new ArrayList<>();
            for (String address : toList) {
                if (address != null)
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(address));

            }
            Transport.send(message);
        }catch(Exception e){
            ExceptionLog.CreateExceptionLog("SendMail", "sendEmail", e,"");
        }
    
    }
    
}
