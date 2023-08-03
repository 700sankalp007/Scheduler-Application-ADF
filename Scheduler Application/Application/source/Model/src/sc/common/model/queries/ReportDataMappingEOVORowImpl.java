package sc.common.model.queries;

import java.math.BigDecimal;

import oracle.jbo.RowSet;
import oracle.jbo.server.ViewRowImpl;

import sc.common.model.entities.ReportDataMappingEOImpl;
import sc.common.model.queries.common.ReportDataMappingEOVORow;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Thu Mar 14 21:32:14 IST 2019
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class ReportDataMappingEOVORowImpl extends ViewRowImpl implements ReportDataMappingEOVORow {


    public static final int ENTITY_REPORTDATAMAPPINGEO = 0;

    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. DO NOT MODIFY.
     */
    protected enum AttributesEnum {
        MapId,
        ReportColName,
        ReportSeqName,
        TableColName,
        DefaultValue,
        ReportHeaderId,
        PrimaryFlag,
        DateFormat,
        IsSeq,
        ColumnDataType,
        TableName,
        SequenceListLovVO1,
        YesNoLovVO1,
        ColumnNameLovVO1;
        private static AttributesEnum[] vals = null;
        ;
        private static final int firstIndex = 0;

        protected int index() {
            return AttributesEnum.firstIndex() + ordinal();
        }

        protected static final int firstIndex() {
            return firstIndex;
        }

        protected static int count() {
            return AttributesEnum.firstIndex() + AttributesEnum.staticValues().length;
        }

        protected static final AttributesEnum[] staticValues() {
            if (vals == null) {
                vals = AttributesEnum.values();
            }
            return vals;
        }
    }


    public static final int MAPID = AttributesEnum.MapId.index();
    public static final int REPORTCOLNAME = AttributesEnum.ReportColName.index();
    public static final int REPORTSEQNAME = AttributesEnum.ReportSeqName.index();
    public static final int TABLECOLNAME = AttributesEnum.TableColName.index();
    public static final int DEFAULTVALUE = AttributesEnum.DefaultValue.index();
    public static final int REPORTHEADERID = AttributesEnum.ReportHeaderId.index();
    public static final int PRIMARYFLAG = AttributesEnum.PrimaryFlag.index();
    public static final int DATEFORMAT = AttributesEnum.DateFormat.index();
    public static final int ISSEQ = AttributesEnum.IsSeq.index();
    public static final int COLUMNDATATYPE = AttributesEnum.ColumnDataType.index();
    public static final int TABLENAME = AttributesEnum.TableName.index();
    public static final int SEQUENCELISTLOVVO1 = AttributesEnum.SequenceListLovVO1.index();
    public static final int YESNOLOVVO1 = AttributesEnum.YesNoLovVO1.index();
    public static final int COLUMNNAMELOVVO1 = AttributesEnum.ColumnNameLovVO1.index();

    /**
     * This is the default constructor (do not remove).
     */
    public ReportDataMappingEOVORowImpl() {
    }

    /**
     * Gets ReportDataMappingEO entity object.
     * @return the ReportDataMappingEO
     */
    public ReportDataMappingEOImpl getReportDataMappingEO() {
        return (ReportDataMappingEOImpl) getEntity(ENTITY_REPORTDATAMAPPINGEO);
    }

    /**
     * Gets the attribute value for MAP_ID using the alias name MapId.
     * @return the MAP_ID
     */
    public BigDecimal getMapId() {
        return (BigDecimal) getAttributeInternal(MAPID);
    }

    /**
     * Sets <code>value</code> as attribute value for MAP_ID using the alias name MapId.
     * @param value value to set the MAP_ID
     */
    public void setMapId(BigDecimal value) {
        setAttributeInternal(MAPID, value);
    }

    /**
     * Gets the attribute value for REPORT_COL_NAME using the alias name ReportColName.
     * @return the REPORT_COL_NAME
     */
    public String getReportColName() {
        return (String) getAttributeInternal(REPORTCOLNAME);
    }

    /**
     * Sets <code>value</code> as attribute value for REPORT_COL_NAME using the alias name ReportColName.
     * @param value value to set the REPORT_COL_NAME
     */
    public void setReportColName(String value) {
        setAttributeInternal(REPORTCOLNAME, value);
    }

    /**
     * Gets the attribute value for REPORT_SEQ_NAME using the alias name ReportSeqName.
     * @return the REPORT_SEQ_NAME
     */
    public Long getReportSeqName() {
        return (Long) getAttributeInternal(REPORTSEQNAME);
    }

    /**
     * Sets <code>value</code> as attribute value for REPORT_SEQ_NAME using the alias name ReportSeqName.
     * @param value value to set the REPORT_SEQ_NAME
     */
    public void setReportSeqName(Long value) {
        setAttributeInternal(REPORTSEQNAME, value);
    }

    /**
     * Gets the attribute value for TABLE_COL_NAME using the alias name TableColName.
     * @return the TABLE_COL_NAME
     */
    public String getTableColName() {
        return (String) getAttributeInternal(TABLECOLNAME);
    }

    /**
     * Sets <code>value</code> as attribute value for TABLE_COL_NAME using the alias name TableColName.
     * @param value value to set the TABLE_COL_NAME
     */
    public void setTableColName(String value) {
        setAttributeInternal(TABLECOLNAME, value);
    }

    /**
     * Gets the attribute value for DEFAULT_VALUE using the alias name DefaultValue.
     * @return the DEFAULT_VALUE
     */
    public String getDefaultValue() {
        return (String) getAttributeInternal(DEFAULTVALUE);
    }

    /**
     * Sets <code>value</code> as attribute value for DEFAULT_VALUE using the alias name DefaultValue.
     * @param value value to set the DEFAULT_VALUE
     */
    public void setDefaultValue(String value) {
        setAttributeInternal(DEFAULTVALUE, value);
    }

    /**
     * Gets the attribute value for REPORT_HEADER_ID using the alias name ReportHeaderId.
     * @return the REPORT_HEADER_ID
     */
    public BigDecimal getReportHeaderId() {
        return (BigDecimal) getAttributeInternal(REPORTHEADERID);
    }

    /**
     * Sets <code>value</code> as attribute value for REPORT_HEADER_ID using the alias name ReportHeaderId.
     * @param value value to set the REPORT_HEADER_ID
     */
    public void setReportHeaderId(BigDecimal value) {
        setAttributeInternal(REPORTHEADERID, value);
    }

    /**
     * Gets the attribute value for PRIMARY_FLAG using the alias name PrimaryFlag.
     * @return the PRIMARY_FLAG
     */
    public String getPrimaryFlag() {
        return (String) getAttributeInternal(PRIMARYFLAG);
    }

    /**
     * Sets <code>value</code> as attribute value for PRIMARY_FLAG using the alias name PrimaryFlag.
     * @param value value to set the PRIMARY_FLAG
     */
    public void setPrimaryFlag(String value) {
        setAttributeInternal(PRIMARYFLAG, value);
    }

    /**
     * Gets the attribute value for DATE_FORMAT using the alias name DateFormat.
     * @return the DATE_FORMAT
     */
    public String getDateFormat() {
        return (String) getAttributeInternal(DATEFORMAT);
    }

    /**
     * Sets <code>value</code> as attribute value for DATE_FORMAT using the alias name DateFormat.
     * @param value value to set the DATE_FORMAT
     */
    public void setDateFormat(String value) {
        setAttributeInternal(DATEFORMAT, value);
    }

    /**
     * Gets the attribute value for IS_SEQ using the alias name IsSeq.
     * @return the IS_SEQ
     */
    public String getIsSeq() {
        return (String) getAttributeInternal(ISSEQ);
    }

    /**
     * Sets <code>value</code> as attribute value for IS_SEQ using the alias name IsSeq.
     * @param value value to set the IS_SEQ
     */
    public void setIsSeq(String value) {
        setAttributeInternal(ISSEQ, value);
    }

    /**
     * Gets the attribute value for the calculated attribute TableName.
     * @return the TableName
     */
    public String getTableName() {
        return (String) getAttributeInternal(TABLENAME);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute TableName.
     * @param value value to set the  TableName
     */
    public void setTableName(String value) {
        setAttributeInternal(TABLENAME, value);
    }

    /**
     * Gets the attribute value for COLUMN_DATA_TYPE using the alias name ColumnDataType.
     * @return the COLUMN_DATA_TYPE
     */
    public String getColumnDataType() {
        return (String) getAttributeInternal(COLUMNDATATYPE);
    }

    /**
     * Sets <code>value</code> as attribute value for COLUMN_DATA_TYPE using the alias name ColumnDataType.
     * @param value value to set the COLUMN_DATA_TYPE
     */
    public void setColumnDataType(String value) {
        setAttributeInternal(COLUMNDATATYPE, value);
    }

    /**
     * Gets the view accessor <code>RowSet</code> SequenceListLovVO1.
     */
    public RowSet getSequenceListLovVO1() {
        return (RowSet) getAttributeInternal(SEQUENCELISTLOVVO1);
    }

    /**
     * Gets the view accessor <code>RowSet</code> YesNoLovVO1.
     */
    public RowSet getYesNoLovVO1() {
        return (RowSet) getAttributeInternal(YESNOLOVVO1);
    }

    /**
     * Gets the view accessor <code>RowSet</code> ColumnNameLovVO1.
     */
    public RowSet getColumnNameLovVO1() {
        return (RowSet) getAttributeInternal(COLUMNNAMELOVVO1);
    }

}

