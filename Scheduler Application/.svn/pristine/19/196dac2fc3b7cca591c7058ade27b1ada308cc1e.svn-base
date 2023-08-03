
package sc.common.essJob;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="jobPackageName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="jobDefinitionName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="paramList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
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
    "jobPackageName",
    "jobDefinitionName",
    "paramList"
})
@XmlRootElement(name = "submitESSJobRequest")
public class SubmitESSJobRequest {

    @XmlElement(required = true)
    protected String jobPackageName;
    @XmlElement(required = true)
    protected String jobDefinitionName;
    protected List<String> paramList;

    /**
     * Gets the value of the jobPackageName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobPackageName() {
        return jobPackageName;
    }

    /**
     * Sets the value of the jobPackageName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobPackageName(String value) {
        this.jobPackageName = value;
    }

    /**
     * Gets the value of the jobDefinitionName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobDefinitionName() {
        return jobDefinitionName;
    }

    /**
     * Sets the value of the jobDefinitionName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobDefinitionName(String value) {
        this.jobDefinitionName = value;
    }

    /**
     * Gets the value of the paramList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the paramList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParamList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getParamList() {
        if (paramList == null) {
            paramList = new ArrayList<String>();
        }
        return this.paramList;
    }

}
