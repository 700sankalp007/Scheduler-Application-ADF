
package sc.common.essJob;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="document" type="{http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/}DocumentDetail" minOccurs="0"/>
 *         &lt;element name="jobDetails" type="{http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/}EssJob" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="notificationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="callbackURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="jobOptions" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "jobDetails",
    "notificationCode",
    "callbackURL",
    "jobOptions"
})
@XmlRootElement(name = "importBulkDataAsync")
public class ImportBulkDataAsync {

    @XmlElementRef(name = "document", namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", type = JAXBElement.class, required = false)
    protected JAXBElement<DocumentDetail> document;
    protected List<EssJob> jobDetails;
    @XmlElementRef(name = "notificationCode", namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> notificationCode;
    @XmlElementRef(name = "callbackURL", namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> callbackURL;
    @XmlElementRef(name = "jobOptions", namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> jobOptions;

    /**
     * Gets the value of the document property.
     *
     * @return
     * possible object is
     * {@link JAXBElement} {@code <} {@link DocumentDetail} {@code >}
     *
     */
    public JAXBElement<DocumentDetail> getDocument() {
        return document;
    }

    /**
     * Sets the value of the document property.
     *
     * @param value
     * allowed object is
     * {@link JAXBElement} {@code <} {@link DocumentDetail} {@code >}
     *
     */
    public void setDocument(JAXBElement<DocumentDetail> value) {
        this.document = value;
    }

    /**
     * Gets the value of the jobDetails property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the jobDetails property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getJobDetails().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EssJob}
     *
     *
     */
    public List<EssJob> getJobDetails() {
        if (jobDetails == null) {
            jobDetails = new ArrayList<EssJob>();
        }
        return this.jobDetails;
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

    /**
     * Gets the value of the jobOptions property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getJobOptions() {
        return jobOptions;
    }

    /**
     * Sets the value of the jobOptions property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setJobOptions(JAXBElement<String> value) {
        this.jobOptions = value;
    }

}
