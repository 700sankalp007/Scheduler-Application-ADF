package sc.common.view.bean;


public class ApInvoiceEmailBean {
    
    private String invoiceStatus;
    private String invoiceNumber;
    private String personEmail;
    private String personNumber;
    
    public ApInvoiceEmailBean() {
        super();
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public void setPersonNumber(String personNumber) {
        this.personNumber = personNumber;
    }

    public String getPersonNumber() {
        return personNumber;
    }
}
