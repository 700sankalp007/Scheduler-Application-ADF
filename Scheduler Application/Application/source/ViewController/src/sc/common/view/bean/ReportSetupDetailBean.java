package sc.common.view.bean;


public class ReportSetupDetailBean {
    public ReportSetupDetailBean() {
        super();
    }
    
    private String paramName;
    private String defualtVal;
    private String sqlStatement;


    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamName() {
        return paramName;
    }

    public void setDefualtVal(String defualtVal) {
        this.defualtVal = defualtVal;
    }

    public String getDefualtVal() {
        return defualtVal;
    }

    public void setSqlStatement(String sqlStatement) {
        this.sqlStatement = sqlStatement;
    }

    public String getSqlStatement() {
        return sqlStatement;
    }
}
