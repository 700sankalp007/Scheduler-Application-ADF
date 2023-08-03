package sc.common.view.backing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.data.RichTable;

import oracle.jbo.NavigatableRowIterator;
import oracle.jbo.Row;
import oracle.jbo.uicli.binding.JUCtrlHierBinding;

import org.apache.myfaces.trinidad.model.CollectionModel;

import sc.common.view.util.ConnectionManager;
import sc.common.view.util.Util;


public class GeneralSetupBean {

    private RichTable mappingTbl;

    public GeneralSetupBean() {
        super();
    }
    
    public String goToSetupDetail(){
        try{
            FacesContext context = Util.getFacesContext();
            DCIteratorBinding schedulerIter =Util.getDCBindingContainer().findIteratorBinding("SchedulerSetupEOVO1Iterator");
            if (schedulerIter != null && schedulerIter.getViewObject() != null &&
                schedulerIter.getViewObject().getCurrentRow() != null) {
                    DCIteratorBinding reportHeaderItr =Util.getDCBindingContainer().findIteratorBinding("ReportSetupHeaderEOVO1Iterator");
                    if (reportHeaderItr != null && reportHeaderItr.getViewObject() != null &&
                        reportHeaderItr.getViewObject().first() != null) {
                        String tableName=(String)reportHeaderItr.getViewObject().getCurrentRow().getAttribute("ReportDataTable");
                        DCIteratorBinding dataMappingItr =Util.getDCBindingContainer().findIteratorBinding("ReportDataMappingEOVO1Iterator");
                        if(dataMappingItr!=null && dataMappingItr.getViewObject()!=null){
                                if(dataMappingItr.getViewObject().first() != null) {
                                    Row row = dataMappingItr.getViewObject().first();
                                        do{
                                            row.setAttribute("TableName",tableName);
                                            if(dataMappingItr.getViewObject().hasNext()) {
                                                row = dataMappingItr.getViewObject().next();
                                            } else {
                                                row = null;
                                            }   
                                        }while(row!=null);
                                }
                        }
                    }
                }
            //context.getApplication().getNavigationHandler().handleNavigation(context,null,"GO_TO_DETAIL");
        }catch(Exception e){
            e.printStackTrace();
        }
        return "GO_TO_DETAIL";
    }

    public void generateServiceType(ValueChangeEvent valueChangeEvent) {
        try{
            String serviceType= null;
            DCIteratorBinding schedulerIter =Util.getDCBindingContainer().findIteratorBinding("SchedulerSetupEOVO1Iterator");
            if (schedulerIter != null && schedulerIter.getViewObject() != null &&
                schedulerIter.getViewObject().getCurrentRow() != null) {
                    String serviceName=(String)valueChangeEvent.getNewValue();
                    if(serviceName!=null) {
                        serviceType=serviceName.replace(" ","_");
                        serviceType=serviceType.toUpperCase();
                        schedulerIter.getViewObject().getCurrentRow().setAttribute("ServiceType",serviceType);
                    }
                }
            
            DCIteratorBinding schedulerDateIter =Util.getDCBindingContainer().findIteratorBinding("SchedulerDateEOVO1Iterator");
            if (schedulerDateIter != null && schedulerDateIter.getViewObject() != null &&
                schedulerDateIter.getViewObject().getCurrentRow() != null) {
                    schedulerDateIter.getViewObject().getCurrentRow().setAttribute("ServiceType",serviceType);
                }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }

    public void getTableColumnsList(ValueChangeEvent valueChangeEvent) {
        try{
            if(valueChangeEvent.getNewValue()!=null){
                DCIteratorBinding dataMappingItr =Util.getDCBindingContainer().findIteratorBinding("ReportDataMappingEOVO1Iterator");
                if(dataMappingItr!=null && dataMappingItr.getViewObject()!=null){
                    String tableName=(String)valueChangeEvent.getNewValue();
                    if(dataMappingItr.getViewObject().first() != null) {
                        Row row = dataMappingItr.getViewObject().first();
                        do{
                            row.setAttribute("TableName",tableName);
                            if(dataMappingItr.getViewObject().hasNext()) {
                                row = dataMappingItr.getViewObject().next();
                            } else {
                                row = null;
                            }   
                        }while(row!=null);
                    }
                }  
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public String createReportDataMapping() {
        try{
            CollectionModel tableModel = (CollectionModel)getMappingTbl().getValue();
            JUCtrlHierBinding adfModel = (JUCtrlHierBinding)tableModel.getWrappedData();
            DCIteratorBinding dciter = adfModel.getDCIteratorBinding();
            NavigatableRowIterator nav=dciter.getNavigatableRowIterator();
            Row newRow = nav.createRow();
            newRow.setNewRowState(Row.STATUS_INITIALIZED);
            nav.insertRowAtRangeIndex(0, newRow);        
            dciter.setCurrentRowWithKey(newRow.getKey().toStringFormat(true));
            
            DCIteratorBinding reportHeaderItr =Util.getDCBindingContainer().findIteratorBinding("ReportSetupHeaderEOVO1Iterator");
            if (reportHeaderItr != null && reportHeaderItr.getViewObject() != null &&
                reportHeaderItr.getViewObject().first() != null) {
                String tableName=(String)reportHeaderItr.getViewObject().getCurrentRow().getAttribute("ReportDataTable");
                
                DCIteratorBinding dataMappingItr =Util.getDCBindingContainer().findIteratorBinding("ReportDataMappingEOVO1Iterator");
                if(dataMappingItr!=null && dataMappingItr.getViewObject()!=null){
                    if(dataMappingItr.getViewObject().getCurrentRow() != null) {
                        dataMappingItr.getViewObject().getCurrentRow().setAttribute("TableName", tableName);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
//    public String createReportDataMapping(){
//        try{
//            DCIteratorBinding reportHeaderItr =Util.getDCBindingContainer().findIteratorBinding("ReportSetupHeaderEOVO1Iterator");
//            if (reportHeaderItr != null && reportHeaderItr.getViewObject() != null &&
//                reportHeaderItr.getViewObject().first() != null) {
//                String tableName=(String)reportHeaderItr.getViewObject().getCurrentRow().getAttribute("ReportDataTable");
//                OperationBinding op=Util.findOperation("CreateInsertReportDataMapping");
//                op.execute();
//                DCIteratorBinding dataMappingItr =Util.getDCBindingContainer().findIteratorBinding("ReportDataMappingEOVO1Iterator");
//                if(dataMappingItr!=null && dataMappingItr.getViewObject()!=null){
//                    if(dataMappingItr.getViewObject().getCurrentRow() != null) {
//                        dataMappingItr.getViewObject().getCurrentRow().setAttribute("TableName", tableName);
//                    }
//                }
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        return null;
//    }

    public void deleteTabData(ActionEvent actionEvent) {
        try{
            DCIteratorBinding reportHeaderItr =Util.getDCBindingContainer().findIteratorBinding("SchedulerSetupEOVO1Iterator");
            if (reportHeaderItr != null && reportHeaderItr.getViewObject() != null &&
                reportHeaderItr.getViewObject().first() != null) {
                String tableName=(String)reportHeaderItr.getViewObject().getCurrentRow().getAttribute("ReportDataTable");
                
                if(tableName != null){
                    deleteTableData(tableName);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void deleteTableData(String tableName) throws SQLException {
        PreparedStatement ps=null;
        Connection con=null;
        try{
            con=ConnectionManager.getConnection();
            ps=con.prepareStatement("DELETE FROM " + tableName);
            ps.executeUpdate();
            con.commit();
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }finally{
            if(ps!=null){
                 ps.close();
            }
            if(con!=null){
                 con.close();
            }
        }
    }

    public void setMappingTbl(RichTable mappingTbl) {
        this.mappingTbl = mappingTbl;
    }

    public RichTable getMappingTbl() {
        return mappingTbl;
    }
}
