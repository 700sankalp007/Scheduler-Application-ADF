package sc.common.view.bean;

import java.sql.Date;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import sc.common.view.util.Constants;

public class PoBean {
    
    private String poHeaderId;
    private String poNumber;
    private String poDate;
    private String approvedDate;
    private String style;
    private String procBu;
    private String reqBu;
    private String supplierId;
    private String supplier;
    private String supplierSiteId;
    private String supplierSite;
    private String poCurrency;
    private String buyer;
    private String paymentTerm;
    private String poStatus;
    private String poLineId;
    private String lineNum;
    private String lineStatus;
    private String lineType;
    private String itemDescription;
    private String categoryName;
    private String scheduleId;
    private String scheduleNum;
    private String location;
    private String organization;
    private String distributionId;
    private String distributionNum;
    private String segment1;
    private String segment2;
    private String segment3;
    private String segment4;
    private String segment5;
    private String segment6;
    private String segment7;
    private String segment8;
    private String segment9;
    private String segment10;
    private String projectId;
    private String projectNumber;
    private String taskId;
    private String taskNumber;
    private String expenditureItemDate;
    private String expenditureTypeName;
    private String expOrg;
    private String qty;
    private String uomCode;
    private String unitPrice;
    private String orderedAmount;
    private String exchangeRate;
    private String funcOrderedAmount;
    private String itemNumber;
    private String description;
    private String context;
    private String attribute1;
    private String retention;
    private String recoupment;
    private String supplierNumber;
    private String buId;
    private String ledgerId;
    private String projectName;
    private String taskName;
    private String accureOnReceiptFlag;
    private String contractEndDate;
    private SimpleDateFormat sdf;
    private String invoicedQuantity;
    private String remainingQuantity;
    private String invoicedAmount;
    private String remainingAmount;
    private String material;
    private String installation;
    private String engineering;
    private String testingAndCommissioning;
    private String handOver;
    private String others;
    private String paymentPhases;
    private String approvalLevel;
    private String matchOption;

    public void setApprovalLevel(String approvalLevel) {
        this.approvalLevel = approvalLevel;
    }

    public String getApprovalLevel() {
        return approvalLevel;
    }

    public void setMatchOption(String matchOption) {
        this.matchOption = matchOption;
    }

    public String getMatchOption() {
        return matchOption;
    }

    public void setPaymentPhases(String paymentPhases) {
        this.paymentPhases = paymentPhases;
    }

    public String getPaymentPhases() {
        return paymentPhases;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getMaterial() {
        return material;
    }

    public void setInstallation(String installation) {
        this.installation = installation;
    }

    public String getInstallation() {
        return installation;
    }

    public void setEngineering(String engineering) {
        this.engineering = engineering;
    }

    public String getEngineering() {
        return engineering;
    }

    public void setTestingAndCommissioning(String testingAndCommissioning) {
        this.testingAndCommissioning = testingAndCommissioning;
    }

    public String getTestingAndCommissioning() {
        return testingAndCommissioning;
    }


    public void setHandOver(String handOver) {
        this.handOver = handOver;
    }

    public String getHandOver() {
        return handOver;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getOthers() {
        return others;
    }

    public  PoBean(){
        
        sdf = new SimpleDateFormat("dd/MM/yyyy");
    }
    
    public void setContractEndDate(String contractEndDate) throws ParseException {
        
        if(contractEndDate!=null){
            this.contractEndDate=String.valueOf(new Date(sdf.parse(contractEndDate).getTime()));
        }else{
            this.contractEndDate=contractEndDate;
        }
    }

    public String getContractEndDate() {
        return contractEndDate;
    }

    public void setPoHeaderId(String poHeaderId) {
        this.poHeaderId = poHeaderId;
    }

    public String getPoHeaderId() {
        return poHeaderId;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoDate(String poDate) throws ParseException {
        
        if(poDate!=null){
            this.poDate = String.valueOf(new Date(sdf.parse(poDate).getTime()));
        }else{
            this.poDate =  poDate;
        }        
    }

    public String getPoDate() {
        return poDate;
    }

    public void setApprovedDate(String approvedDate) throws ParseException {
        
        if(approvedDate!=null){
            this.approvedDate = String.valueOf(new Date(sdf.parse(approvedDate).getTime()));
        }else{
            this.approvedDate =  approvedDate;
        }
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyle() {
        return style;
    }

    public void setProcBu(String procBu) {
        this.procBu = procBu;
    }

    public String getProcBu() {
        return procBu;
    }

    public void setReqBu(String reqBu) {
        this.reqBu = reqBu;
    }

    public String getReqBu() {
        return reqBu;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplierSiteId(String supplierSiteId) {
        this.supplierSiteId = supplierSiteId;
    }

    public String getSupplierSiteId() {
        return supplierSiteId;
    }

    public void setSupplierSite(String supplierSite) {
        this.supplierSite = supplierSite;
    }

    public String getSupplierSite() {
        return supplierSite;
    }

    public void setPoCurrency(String poCurrency) {
        this.poCurrency = poCurrency;
    }

    public String getPoCurrency() {
        return poCurrency;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setPaymentTerm(String paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    public String getPaymentTerm() {
        return paymentTerm;
    }

    public void setPoStatus(String poStatus) {
        this.poStatus = poStatus;
    }

    public String getPoStatus() {
        return poStatus;
    }

    public void setPoLineId(String poLineId) {
        this.poLineId = poLineId;
    }

    public String getPoLineId() {
        return poLineId;
    }

    public void setLineNum(String lineNum) {
        this.lineNum = lineNum;
    }

    public String getLineNum() {
        return lineNum;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    public String getLineType() {
        return lineType;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleNum(String scheduleNum) {
        this.scheduleNum = scheduleNum;
    }

    public String getScheduleNum() {
        return scheduleNum;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOrganization() {
        return organization;
    }

    public void setDistributionId(String distributionId) {
        this.distributionId = distributionId;
    }

    public String getDistributionId() {
        return distributionId;
    }

    public void setDistributionNum(String distributionNum) {
        this.distributionNum = distributionNum;
    }

    public String getDistributionNum() {
        return distributionNum;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public void setExpenditureItemDate(String expenditureItemDate) throws ParseException {
        
        if(expenditureItemDate!=null){
            this.expenditureItemDate = String.valueOf(new Date(sdf.parse(expenditureItemDate).getTime()));
        }else{
            this.expenditureItemDate =  expenditureItemDate;
        }
    }

    public String getExpenditureItemDate() {
        return expenditureItemDate;
    }

    public void setExpenditureTypeName(String expenditureTypeName) {
        this.expenditureTypeName = expenditureTypeName;
    }

    public String getExpenditureTypeName() {
        return expenditureTypeName;
    }

    public void setExpOrg(String expOrg) {
        this.expOrg = expOrg;
    }

    public String getExpOrg() {
        return expOrg;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getQty() {
        return qty;
    }

    public void setUomCode(String uomCode) {
        this.uomCode = uomCode;
    }

    public String getUomCode() {
        return uomCode;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setOrderedAmount(String orderedAmount) {
        this.orderedAmount = orderedAmount;
    }

    public String getOrderedAmount() {
        return orderedAmount;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setFuncOrderedAmount(String funcOrderedAmount) {
        this.funcOrderedAmount = funcOrderedAmount;
    }

    public String getFuncOrderedAmount() {
        return funcOrderedAmount;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getContext() {
        return context;
    }

    public void setRetention(String retention) {
        this.retention = retention;
    }

    public String getRetention() {
        return retention;
    }

    public void setRecoupment(String recoupment) {
        this.recoupment = recoupment;
    }

    public String getRecoupment() {
        return recoupment;
    }

    public void setSupplierNumber(String supplierNumber) {
        this.supplierNumber = supplierNumber;
    }

    public String getSupplierNumber() {
        return supplierNumber;
    }

    public void setBuId(String buId) {
        this.buId = buId;
    }

    public String getBuId() {
        return buId;
    }

    public void setLedgerId(String ledgerId) {
        this.ledgerId = ledgerId;
    }

    public String getLedgerId() {
        return ledgerId;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setAccureOnReceiptFlag(String accureOnReceiptFlag) {
        this.accureOnReceiptFlag = accureOnReceiptFlag;
    }

    public String getAccureOnReceiptFlag() {
        return accureOnReceiptFlag;
    }

    public void setSegment1(String segment1) {
        this.segment1 = segment1;
    }

    public String getSegment1() {
        return segment1;
    }

    public void setSegment2(String segment2) {
        this.segment2 = segment2;
    }

    public String getSegment2() {
        return segment2;
    }

    public void setSegment3(String segment3) {
        this.segment3 = segment3;
    }

    public String getSegment3() {
        return segment3;
    }

    public void setSegment4(String segment4) {
        this.segment4 = segment4;
    }

    public String getSegment4() {
        return segment4;
    }

    public void setSegment5(String segment5) {
        this.segment5 = segment5;
    }

    public String getSegment5() {
        return segment5;
    }

    public void setSegment6(String segment6) {
        this.segment6 = segment6;
    }

    public String getSegment6() {
        return segment6;
    }

    public void setSegment7(String segment7) {
        this.segment7 = segment7;
    }

    public String getSegment7() {
        return segment7;
    }

    public void setSegment8(String segment8) {
        this.segment8 = segment8;
    }

    public String getSegment8() {
        return segment8;
    }

    public void setSegment9(String segment9) {
        this.segment9 = segment9;
    }

    public String getSegment9() {
        return segment9;
    }

    public void setSegment10(String segment10) {
        this.segment10 = segment10;
    }

    public String getSegment10() {
        return segment10;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setLineStatus(String lineStatus) {
        this.lineStatus = lineStatus;
    }

    public String getLineStatus() {
        return lineStatus;
    }
    
    public String getPoKey(){
        
        return poHeaderId;
    }
    
    public String getProjectKey(){
        
        return new StringBuffer(poHeaderId).append(Constants.HYPHEN).append(projectId).append(Constants.HYPHEN).append(taskId).toString();
    }
    
    public String getPhaseKey() {
        return new StringBuffer(poHeaderId).append(Constants.HYPHEN).append(paymentPhases).toString();
    }
    
    public String getMaterialKey() {
        return  new StringBuffer(poHeaderId).append(Constants.HYPHEN).append(Constants.MATERIAL).toString();
    }
    
    public String getEngineeringKey() {
        return  new StringBuffer(poHeaderId).append(Constants.HYPHEN).append(Constants.ENGINEERING).toString();
    }
    
    public String getInstallationKey() {
        return new StringBuffer(poHeaderId).append(Constants.HYPHEN).append(Constants.INSTALLATION).toString();
    }
    
    public String gettestingAndCommissioningKey() {
        return new StringBuffer(poHeaderId).append(Constants.HYPHEN).append(Constants.TESTING_AND_COMMISSIONING).toString();
    }
    
    public String getHandOverKey() {
        return new StringBuffer(poHeaderId).append(Constants.HYPHEN).append(Constants.HANDOVER).toString();
    }
    
    public String getOthersKey() {
        return new StringBuffer(poHeaderId).append(Constants.HYPHEN).append(Constants.OTHERS).toString();
        }
    
    public String getLineKey(){
        
        return new StringBuffer(poLineId).append(Constants.HYPHEN).append(scheduleId).append(Constants.HYPHEN).append(distributionId).toString();
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    public String getAttribute1() {
        return attribute1;
    }

    public void setInvoicedQuantity(String invoicedQuantity) {
        this.invoicedQuantity = invoicedQuantity;
    }

    public String getInvoicedQuantity() {
        return invoicedQuantity;
    }

    public void setRemainingQuantity(String remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public String getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setInvoicedAmount(String invoicedAmount) {
        this.invoicedAmount = invoicedAmount;
    }

    public String getInvoicedAmount() {
        return invoicedAmount;
    }

    public void setRemainingAmount(String remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public String getRemainingAmount() {
        return remainingAmount;
    }
}
