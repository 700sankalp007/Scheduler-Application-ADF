
package sc.common.essJob;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="document" type="{http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/}DocumentDetails"/>
 *         &lt;element name="jobList" type="{http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/}EssJob" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="interfaceDetails" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="notificationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="callbackURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "document",
    "jobList",
    "interfaceDetails",
    "notificationCode",
    "callbackURL"
})
@XmlRootElement(name = "loadAndImportData")
public class LoadAndImportData {

    @XmlElement(required = true)
    protected DocumentDetails document;
    protected List<EssJob> jobList;
    @XmlElementRef(name = "interfaceDetails", namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> interfaceDetails;
    @XmlElementRef(name = "notificationCode", namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> notificationCode;
    @XmlElementRef(name = "callbackURL", namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> callbackURL;

    /**
     * Gets the value of the document property.
     *
     * @return
     * possible object is
     * {@link DocumentDetails}
     *
     */
    public DocumentDetails getDocument() {
        return document;
    }

    /**
     * Sets the value of the document property.
     *
     * @param value
     * allowed object is
     * {@link DocumentDetails}
     *
     */
    public void setDocument(DocumentDetails value) {
        this.document = value;
    }

    /**
     * Gets the value of the jobList property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the jobList property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getJobList().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EssJob}
     *
     *
     */
    public List<EssJob> getJobList() {
        if (jobList == null) {
            jobList = new ArrayList<EssJob>();
        }
        return this.jobList;
    }

    /**
     * Gets the value of the interfaceDetails property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getInterfaceDetails() {
        return interfaceDetails;
    }

    /**
     * Sets the value of the interfaceDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setInterfaceDetails(JAXBElement<String> value) {
        this.interfaceDetails = value;
    }

    /**
     * Gets the value of the notificationCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getNotificationCode() {
        return notificationCode;
    }

    /**
     * Sets the value of the notificationCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setNotificationCode(JAXBElement<String> value) {
        this.notificationCode = value;
    }

    /**
     * Gets the value of the callbackURL property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCallbackURL() {
        return callbackURL;
    }

    /**
     * Sets the value of the callbackURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCallbackURL(JAXBElement<String> value) {
        this.callbackURL = value;
    }

}
