package sc.common.view.bean;


public class ReportDataMappingBean {
    public ReportDataMappingBean() {
        super();
    }
    private String mapId;
    private String reportColName;
    private Long reportSeqName;
    private String TableColName;
    private String defaultValue;
    private String reportHdrId;
    private String primaryFlag;
    private String dateFormat;
    private String isSeq;
    private String columnDataType;

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public String getMapId() {
        return mapId;
    }

    public void setReportColName(String reportColName) {
        this.reportColName = reportColName;
    }

    public String getReportColName() {
        return reportColName;
    }

    public void setReportSeqName(Long reportSeqName) {
        this.reportSeqName = reportSeqName;
    }

    public Long getReportSeqName() {
        return reportSeqName;
    }

    public void setTableColName(String TableColName) {
        this.TableColName = TableColName;
    }

    public String getTableColName() {
        return TableColName;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setReportHdrId(String reportHdrId) {
        this.reportHdrId = reportHdrId;
    }

    public String getReportHdrId() {
        return reportHdrId;
    }

    public void setPrimaryFlag(String primaryFlag) {
        this.primaryFlag = primaryFlag;
    }

    public String getPrimaryFlag() {
        return primaryFlag;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setIsSeq(String isSeq) {
        this.isSeq = isSeq;
    }

    public String getIsSeq() {
        return isSeq;
    }

    public void setColumnDataType(String columnDataType) {
        this.columnDataType = columnDataType;
    }

    public String getColumnDataType() {
        return columnDataType;
    }
}
