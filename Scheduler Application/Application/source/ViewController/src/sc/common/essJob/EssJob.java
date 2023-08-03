
package sc.common.essJob;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EssJob complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EssJob">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="JobName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ParameterList" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="JobRequestId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EssJob", namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", propOrder = {
    "jobName",
    "parameterList",
    "jobRequestId"
})
public class EssJob {

    @XmlElementRef(name = "JobName", namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> jobName;
    @XmlElementRef(name = "ParameterList", namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> parameterList;
    @XmlElementRef(name = "JobRequestId", namespace = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> jobRequestId;

    /**
     * Gets the value of the jobName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getJobName() {
        return jobName;
    }

    /**
     * Sets the value of the jobName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setJobName(JAXBElement<String> value) {
        this.jobName = value;
    }

    /**
     * Gets the value of the parameterList property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getParameterList() {
        return parameterList;
    }

    /**
     * Sets the value of the parameterList property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setParameterList(JAXBElement<String> value) {
        this.parameterList = value;
    }

    /**
     * Gets the value of the jobRequestId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getJobRequestId() {
        return jobRequestId;
    }

    /**
     * Sets the value of the jobRequestId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setJobRequestId(JAXBElement<String> value) {
        this.jobRequestId = value;
    }

}
