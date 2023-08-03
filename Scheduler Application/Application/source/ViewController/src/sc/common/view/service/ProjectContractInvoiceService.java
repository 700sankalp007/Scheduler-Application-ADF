package sc.common.view.service;

import java.io.IOException;

import java.util.Map;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;


public class ProjectContractInvoiceService {
    
    public static final String prefix1 = "typ";
    public static final String pubUri1 = "http://xmlns.oracle.com/apps/projects/billing/workarea/invoice/invoiceServiceV2/types/";
    public static final String prefix2 = "inv";
    public static final String pubUri2 = "http://xmlns.oracle.com/apps/projects/billing/workarea/invoice/invoiceServiceV2/";

    
    public ProjectContractInvoiceService() {
        super();
    }
    
    public SOAPMessage tieback(Map<String, String> invoiceMap) throws SOAPException, IOException, NullPointerException {
        
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage soapMessage = factory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
        soapEnvelope.addNamespaceDeclaration(prefix1,pubUri1);
        soapEnvelope.addNamespaceDeclaration(prefix2,pubUri2);
        
        SOAPBody soapBody = soapEnvelope.getBody();        
        SOAPElement tieback = soapBody.addChildElement("tieback",prefix1);        
        SOAPElement invoice = tieback.addChildElement("Invoice",prefix1);
        
        SOAPElement element = null;
        for(Map.Entry<String, String> invoiceME : invoiceMap.entrySet()){
            
            element = invoice.addChildElement(invoiceME.getKey(), prefix2);
            element.addTextNode(invoiceME.getValue());            
        }
        
        return soapMessage;        

    }

    
}
