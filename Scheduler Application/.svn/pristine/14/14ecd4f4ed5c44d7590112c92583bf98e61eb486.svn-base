package sc.common.view.bean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import java.sql.Connection;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.engine.util.JRLoader;

import sc.common.view.util.ConnectionManager;
import sc.common.view.util.ExceptionLog;


public class JasperBean {
    String context;
    public JasperBean(String context){
        this.context=context;
    }
    
    @SuppressWarnings("unchecked")
    public byte[] runReport(String repPath, java.util.Map param, String reportName, OutputStream op,
                            String contentType) throws Exception {
        Connection conn = null;
        byte[] reportContent = null;
        try {
//            HttpServletResponse response = getResponse();
            String FileName = reportName;
            if (FileName.contains(" "))
                FileName = FileName.replace(" ", "_");
            if (contentType.equals("pdf")) {
                FileName = FileName + ".pdf";
            } else {
                FileName = FileName + ".xls";
            }
          //  ServletContext context = getContext();
            param.put("CONTEXT", context+"/reports/");
//            response.setHeader("Content-Disposition", "attachment; filename=" + FileName);
//            if (contentType.equals("pdf")) {
//                response.setContentType("application/pdf");
//            } else {
//                response.setContentType("application/vnd.ms-excel");
            
//            }
            File file=new File(context+"/reports/"+repPath);
            InputStream stream=new FileInputStream(file);
           JasperReport template = (JasperReport) JRLoader.loadObject(stream);
           template.setWhenNoDataType(WhenNoDataTypeEnum.NO_DATA_SECTION);

           conn = ConnectionManager.getConnection();

           JasperPrint print = JasperFillManager.fillReport(template, param, conn);
           ByteArrayOutputStream baos = new ByteArrayOutputStream();
           if (contentType.equals("pdf")) {
               JasperExportManager.exportReportToPdfStream(print, baos);
            } else {
                print.setProperty("net.sf.jasperreports.export.xls.remove.empty.space.between.rows", "true");
                print.setProperty("net.sf.jasperreports.export.xls.remove.empty.space.between.columns", "true");
                print.setProperty("net.sf.jasperreports.export.xls.detect.cell.type", "true");
                print.setProperty("net.sf.jasperreports.export.xls.collapse.row.span", "true");

                print.getPropertiesMap().setProperty("net.sf.jasperreports.print.keep.full.text", "true");
                print.getPropertiesMap().setProperty("net.sf.jasperreports.export.xls.auto.fit.row", "true");
                print.getPropertiesMap().setProperty("net.sf.jasperreports.export.xls.auto.fit.column", "true");

                JRXlsExporter exporterXLS = new JRXlsExporter();
                exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, print);
                exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                // to avoid cell merging
                exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
                exporterXLS.exportReport();
            }
            if(baos.size()>0){
                
                reportContent = baos.toByteArray();
                
            }else{
                reportContent=null;
            }
            if (op != null) {
                if(baos.size()>0){
                    op.write(baos.toByteArray());
                }
                op.flush();
                op.close();
                FacesContext.getCurrentInstance().responseComplete();
            }
            //ExceptionLog.CreateExceptionLog("JasperBean", "reportContent : " + reportName, new Exception(reportContent.toString()));
//            if (print.getPages().size() == 0) {
//                reportContent = null;
//            }
        } catch (Exception jex) {
            ExceptionLog.CreateExceptionLog(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName(), jex, null);
            jex.printStackTrace();
        } finally {
            ConnectionManager.releaseConnetion(conn);
        }
        return reportContent;
    }
}
