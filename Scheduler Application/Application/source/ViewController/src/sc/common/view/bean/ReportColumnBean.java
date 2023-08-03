package sc.common.view.bean;


public class ReportColumnBean {
    private String columnName;
    private String columnValue;
   

    public ReportColumnBean(String columnName, String columnValue) {
        this.columnName = columnName;
        this.columnValue = columnValue;
        
    }

  

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnValue(String columnValue) {
        this.columnValue = columnValue;
    }

    public String getColumnValue() {
        return columnValue;
    }

    public ReportColumnBean() {
        super();
    }
    
   
}
