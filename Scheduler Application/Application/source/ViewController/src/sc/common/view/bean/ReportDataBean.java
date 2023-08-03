package sc.common.view.bean;

import java.util.List;

//import sc.common.model.queries.ReportDataMappingEOVORowImpl;
//import sc.common.model.queries.ReportSetupDetailEOVORowImpl;


public class ReportDataBean {
    
    private String reportJobName;
    private String schedulerId;
    private String reportPath;
    private String reportDataTable;
    private String dataFormat;
    private String dataLocale;
    private String isRefresh;
    private String delimiter;
    private String serviceName;
    private String serviceType;
    private String rootNode;
//    private List<ReportSetupDetailEOVORowImpl> reportSetupDetail;
//    private List<ReportDataMappingEOVORowImpl> reportDataMappings;
//    private List<ReportDataMappingEOVORowImpl> reportColumnsMap;
//    private List<ReportDataMappingEOVORowImpl> reportPrimaryColumnMap;
    private List<ReportSetupDetailBean> reportSetupDetail;
    private List<ReportDataMappingBean> reportDataMappings;
    private List<ReportDataMappingBean> reportColumnsMap;
    private List<ReportDataMappingBean> reportPrimaryColumnMap;


    public void setReportJobName(String reportJobName) {
        this.reportJobName = reportJobName;
    }

    public String getReportJobName() {
        return reportJobName;
    }

    public void setRootNode(String rootNode) {
        this.rootNode = rootNode;
    }

    public String getRootNode() {
        return rootNode;
    }

    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }

    public String getReportPath() {
        return reportPath;
    }

    public void setReportDataTable(String reportDataTable) {
        this.reportDataTable = reportDataTable;
    }

    public String getReportDataTable() {
        return reportDataTable;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataLocale(String dataLocale) {
        this.dataLocale = dataLocale;
    }

    public String getDataLocale() {
        return dataLocale;
    }

    public void setIsRefresh(String isRefresh) {
        this.isRefresh = isRefresh;
    }

    public String getIsRefresh() {
        return isRefresh;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }

//    public void setReportSetupDetail(List<ReportSetupDetailEOVORowImpl> reportSetupDetail) {
//        this.reportSetupDetail = reportSetupDetail;
//    }
//
//    public List<ReportSetupDetailEOVORowImpl> getReportSetupDetail() {
//        return reportSetupDetail;
//    }
//
//    public void setReportDataMappings(List<ReportDataMappingEOVORowImpl> reportDataMappings) {
//        this.reportDataMappings = reportDataMappings;
//    }
//
//    public List<ReportDataMappingEOVORowImpl> getReportDataMappings() {
//        return reportDataMappings;
//    }
//
//    public void setReportColumnsMap(List<ReportDataMappingEOVORowImpl> reportColumnsMap) {
//        this.reportColumnsMap = reportColumnsMap;
//    }
//
//    public List<ReportDataMappingEOVORowImpl> getReportColumnsMap() {
//        return reportColumnsMap;
//    }
//
//    public void setReportPrimaryColumnMap(List<ReportDataMappingEOVORowImpl> reportPrimaryColumnMap) {
//        this.reportPrimaryColumnMap = reportPrimaryColumnMap;
//    }
//
//    public List<ReportDataMappingEOVORowImpl> getReportPrimaryColumnMap() {
//        return reportPrimaryColumnMap;
//    }

    public void setReportSetupDetail(List<ReportSetupDetailBean> reportSetupDetail) {
        this.reportSetupDetail = reportSetupDetail;
    }

    public List<ReportSetupDetailBean> getReportSetupDetail() {
        return reportSetupDetail;
    }

    public void setReportDataMappings(List<ReportDataMappingBean> reportDataMappings) {
        this.reportDataMappings = reportDataMappings;
    }

    public List<ReportDataMappingBean> getReportDataMappings() {
        return reportDataMappings;
    }

    public void setReportColumnsMap(List<ReportDataMappingBean> reportColumnsMap) {
        this.reportColumnsMap = reportColumnsMap;
    }

    public List<ReportDataMappingBean> getReportColumnsMap() {
        return reportColumnsMap;
    }

    public void setReportPrimaryColumnMap(List<ReportDataMappingBean> reportPrimaryColumnMap) {
        this.reportPrimaryColumnMap = reportPrimaryColumnMap;
    }

    public List<ReportDataMappingBean> getReportPrimaryColumnMap() {
        return reportPrimaryColumnMap;
    }

    public void setSchedulerId(String schedulerId) {
        this.schedulerId = schedulerId;
    }

    public String getSchedulerId() {
        return schedulerId;
    }


    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceType() {
        return serviceType;
    }

}
