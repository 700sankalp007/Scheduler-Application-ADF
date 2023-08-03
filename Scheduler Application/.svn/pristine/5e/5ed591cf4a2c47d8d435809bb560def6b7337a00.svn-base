package sc.common.view.backing;

import java.io.IOException;

import java.util.Map;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

public class ReceivablesCustomerProfileService {

    public static final String prefix1 = "typ";
    public static final String pubUri1 = "http://xmlns.oracle.com/apps/financials/receivables/customers/customerProfileService/types/";
    public static final String prefix2 = "cus";
    public static final String pubUri2 = "http://xmlns.oracle.com/apps/financials/receivables/customers/customerProfileService/";

    
    public ReceivablesCustomerProfileService() {
        super();
    }
    
    public SOAPMessage updateCustomerProfile(Map<String, String> accountMap) throws SOAPException, IOException, NullPointerException {
        
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage soapMessage = factory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
        soapEnvelope.addNamespaceDeclaration(prefix1,pubUri1);
        soapEnvelope.addNamespaceDeclaration(prefix2,pubUri2);
        
        SOAPBody soapBody = soapEnvelope.getBody();        
        SOAPElement tieback = soapBody.addChildElement("updateCustomerProfile",prefix1);        
        SOAPElement invoice = tieback.addChildElement("customerProfile",prefix1);
        
        SOAPElement element = null;
        for(Map.Entry<String, String> accountME : accountMap.entrySet()){
            
            element = invoice.addChildElement(accountME.getKey(), prefix2);
            element.addTextNode(accountME.getValue());            
        }
        
        return soapMessage;        

    }


}
