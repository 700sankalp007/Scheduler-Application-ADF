
package sc.common.essJob;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DocumentDetails complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DocumentDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Content" type="{http://xmlns.oracle.com/adf/svc/types/}base64Binary-DataHandler"/>
 *         &lt;element name="FileName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ContentType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DocumentTitle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DocumentAuthor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DocumentSecurityGroup" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DocumentAccount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DocumentName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DocumentId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentDetails", namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", propOrder = {
    "content",
    "fileName",
    "contentType",
    "documentTitle",
    "documentAuthor",
    "documentSecurityGroup",
    "documentAccount",
    "documentName",
    "documentId"
})
public class DocumentDetails {

    @XmlElement(name = "Content", required = true, nillable = true)
    protected byte[] content;
    @XmlElement(name = "FileName", required = true, nillable = true)
    protected String fileName;
    @XmlElementRef(name = "ContentType", namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> contentType;
    @XmlElementRef(name = "DocumentTitle", namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> documentTitle;
    @XmlElementRef(name = "DocumentAuthor", namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> documentAuthor;
    @XmlElementRef(name = "DocumentSecurityGroup", namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> documentSecurityGroup;
    @XmlElementRef(name = "DocumentAccount", namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> documentAccount;
    @XmlElementRef(name = "DocumentName", namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> documentName;
    @XmlElementRef(name = "DocumentId", namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> documentId;

    /**
     * Gets the value of the content property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setContent(byte[] value) {
        this.content = value;
    }

    /**
     * Gets the value of the fileName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the value of the fileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileName(String value) {
        this.fileName = value;
    }

    /**
     * Gets the value of the contentType property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getContentType() {
        return contentType;
    }

    /**
     * Sets the value of the contentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setContentType(JAXBElement<String> value) {
        this.contentType = value;
    }

    /**
     * Gets the value of the documentTitle property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDocumentTitle() {
        return documentTitle;
    }

    /**
     * Sets the value of the documentTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDocumentTitle(JAXBElement<String> value) {
        this.documentTitle = value;
    }

    /**
     * Gets the value of the documentAuthor property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDocumentAuthor() {
        return documentAuthor;
    }

    /**
     * Sets the value of the documentAuthor property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDocumentAuthor(JAXBElement<String> value) {
        this.documentAuthor = value;
    }

    /**
     * Gets the value of the documentSecurityGroup property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDocumentSecurityGroup() {
        return documentSecurityGroup;
    }

    /**
     * Sets the value of the documentSecurityGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDocumentSecurityGroup(JAXBElement<String> value) {
        this.documentSecurityGroup = value;
    }

    /**
     * Gets the value of the documentAccount property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDocumentAccount() {
        return documentAccount;
    }

    /**
     * Sets the value of the documentAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDocumentAccount(JAXBElement<String> value) {
        this.documentAccount = value;
    }

    /**
     * Gets the value of the documentName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDocumentName() {
        return documentName;
    }

    /**
     * Sets the value of the documentName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDocumentName(JAXBElement<String> value) {
        this.documentName = value;
    }

    /**
     * Gets the value of the documentId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDocumentId() {
        return documentId;
    }

    /**
     * Sets the value of the documentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDocumentId(JAXBElement<String> value) {
        this.documentId = value;
    }

}
