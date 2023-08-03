
package sc.common.essJob;

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
 *         &lt;element name="requestIds" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "requestIds",
    "notificationCode",
    "callbackURL",
    "jobOptions"
})
@XmlRootElement(name = "extractAndPurge")
public class ExtractAndPurge {

    @XmlElement(required = true)
    protected String requestIds;
    @XmlElementRef(name = "notificationCode", namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> notificationCode;
    @XmlElementRef(name = "callbackURL", namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> callbackURL;
    @XmlElementRef(name = "jobOptions", namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> jobOptions;

    /**
     * Gets the value of the requestIds property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestIds() {
        return requestIds;
    }

    /**
     * Sets the value of the requestIds property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestIds(String value) {
        this.requestIds = value;
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
