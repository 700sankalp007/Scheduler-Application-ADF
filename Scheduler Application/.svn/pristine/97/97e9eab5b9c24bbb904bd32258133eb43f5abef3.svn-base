package sc.common.view.service;

import java.io.IOException;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.namespace.QName;

public class UCMService {
    

    public static final String prefix1 = "get";
    public static final String pubUri1 = "http://www.stellent.com/GetFile/";
    public static final String prefix2 = "ucm";
    public static final String pubUri2 = "http://www.oracle.com/UCM";


    public UCMService() {
        super();
    }
    
    public SOAPMessage getFileByName(String docName) throws SOAPException, IOException, NullPointerException {
        
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage soapMessage = factory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
        soapEnvelope.addNamespaceDeclaration(prefix1,pubUri1);
        
        SOAPBody soapBody = soapEnvelope.getBody();        
        SOAPElement getFileByName = soapBody.addChildElement("GetFileByName",prefix1);        
        
        SOAPElement dDocName = getFileByName.addChildElement("dDocName", prefix1);
        dDocName.addTextNode(docName);            

        SOAPElement revisionSelectionMethod = getFileByName.addChildElement("revisionSelectionMethod", prefix1);
        revisionSelectionMethod.addTextNode("Latest");            

        SOAPElement rendition = getFileByName.addChildElement("rendition", prefix1);
        rendition.addTextNode("Primary");            
        
        return soapMessage;        

    }

    public SOAPMessage genericRequest(String docName) throws SOAPException, IOException, NullPointerException {
        
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage soapMessage = factory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
        soapEnvelope.addNamespaceDeclaration(prefix2,pubUri2);
        
        SOAPBody soapBody = soapEnvelope.getBody();        
        SOAPElement genericRequest = soapBody.addChildElement("GenericRequest",prefix2);        
        genericRequest.addAttribute(new QName("webKey"), "cs");
        
        SOAPElement service = genericRequest.addChildElement("Service",prefix2);        
        service.addAttribute(new QName("IdcService"), "GET_FILE");

        SOAPElement document = service.addChildElement("Document",prefix2);        
        
        SOAPElement dDocName = document.addChildElement("Field",prefix2);        
        dDocName.addAttribute(new QName("name"), "dDocName");
        dDocName.addTextNode(docName);
        
        SOAPElement revisionSelectionMethod = document.addChildElement("Field",prefix2);        
        revisionSelectionMethod.addAttribute(new QName("name"), "RevisionSelectionMethod");
        revisionSelectionMethod.addTextNode("Latest");

        SOAPElement rendition = document.addChildElement("Field",prefix2);        
        rendition.addAttribute(new QName("name"), "Rendition");
        rendition.addTextNode("Primary");

        return soapMessage;        

    }

    
}
