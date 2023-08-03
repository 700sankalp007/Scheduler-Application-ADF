package sc.common.view.bean;


public class PhysicalCountTagListMstBean {
    public PhysicalCountTagListMstBean() {
        super();
    }
    
    String orgCode;
    String ordId;
    String phyInvName;
    String phyInvId;
    String syncStatus;
    String status;
    String approvalLevel;
    String fusionStatus;
    String assignedStatus;
    String allowSubmitApproval;
    String approvalVariance;
    String createdDate;

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrdId(String ordId) {
        this.ordId = ordId;
    }

    public String getOrdId() {
        return ordId;
    }

    public void setPhyInvName(String phyInvName) {
        this.phyInvName = phyInvName;
    }

    public String getPhyInvName() {
        return phyInvName;
    }

    public void setPhyInvId(String phyInvId) {
        this.phyInvId = phyInvId;
    }

    public String getPhyInvId() {
        return phyInvId;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setApprovalLevel(String approvalLevel) {
        this.approvalLevel = approvalLevel;
    }

    public String getApprovalLevel() {
        return approvalLevel;
    }

    public void setFusionStatus(String fusionStatus) {
        this.fusionStatus = fusionStatus;
    }

    public String getFusionStatus() {
        return fusionStatus;
    }

    public void setAssignedStatus(String assignedStatus) {
        this.assignedStatus = assignedStatus;
    }

    public String getAssignedStatus() {
        return assignedStatus;
    }

    public void setAllowSubmitApproval(String allowSubmitApproval) {
        this.allowSubmitApproval = allowSubmitApproval;
    }

    public String getAllowSubmitApproval() {
        return allowSubmitApproval;
    }

    public void setApprovalVariance(String approvalVariance) {
        this.approvalVariance = approvalVariance;
    }

    public String getApprovalVariance() {
        return approvalVariance;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }
}
