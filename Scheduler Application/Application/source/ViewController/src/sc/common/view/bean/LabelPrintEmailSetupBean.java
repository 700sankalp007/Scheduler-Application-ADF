package sc.common.view.bean;


public class LabelPrintEmailSetupBean {
    String orgCode;

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgCode() {
        return orgCode;
    }
    String email;
    public LabelPrintEmailSetupBean() {
        super();
    }
    public LabelPrintEmailSetupBean(String orgCode,String email) {
        super();
        this.orgCode=orgCode;
        this.email=email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
