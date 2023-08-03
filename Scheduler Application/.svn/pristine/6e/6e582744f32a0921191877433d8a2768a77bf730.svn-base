package ei.ar.integration.reports.customer;


import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.digester.Digester;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import sc.common.view.util.ConnectionManager;
import sc.common.view.util.Constants;
import sc.common.view.util.ExceptionLogParent;
import sc.common.view.util.LogBean;
import sc.common.view.util.Util;

public class CustomerSchedulerBean implements Job {
    public CustomerSchedulerBean() {
        super();
    }
    

    public void executecustomerScheduler(String schedulerId) {
//        System.out.println("Executing Scheduler > executecustomerScheduler");
        Connection cn = null;
        try {
            cn = ConnectionManager.getConnection();
            String debugEnabled = LogBean.isDebugEnabled(cn, Constants.MODULE_OUTBOUNDING);
            LogBean.writeLog(Constants.LOG_CUSTOMER, "Inside executecustomerScheduler() metho Scheduler Id:"+schedulerId, Constants.LOG_DEBUG, debugEnabled);
           // boolean schedulerRunning = Util.isSchedulerRunning(cn, schedulerId);
//            if (schedulerRunning) {
                //boolean schedulerCurrentStatus = Util.getSchedulerCurrentStatus(cn, schedulerId);
//                if (!schedulerCurrentStatus) {
                  // Util.updateSchedulerStatus(cn, schedulerId, "Y");
                    try {
                        Util util = new Util();
                        String systemDate = Util.getCurrentDate();
                        String utcDate = Util.convertToUTCFormat(systemDate);
                        
                        //String lastRunDateStr = util.getLastRunDate(cn, schedulerId);
                        
                        String lastRunDateStr = util.getOutBoundingLastRunDate(cn, schedulerId);
                        HashMap<String, String> biHeaderMap = new HashMap<String, String>();
                        biHeaderMap = Util.getSetupDetails(cn, Constants.SETUP_TYPE_WEBSERVICE,
                                Constants.SETUP_SUB_TYPE_BI);
                        HashMap<String, ArrayList<String>> paramMap = new HashMap<String, ArrayList<String>>();
                        // ArrayList<String> paramBusinessUnit = new ArrayList<String>();
                        // paramBusinessUnit.add("300000001242930");
                        // paramMap.put("p_business_unit",paramBusinessUnit);
                        ArrayList<String> paramFromList = new ArrayList<String>();
                        paramFromList.add(lastRunDateStr);
                        paramMap.put("P_FROM_DATE", paramFromList);
                        ArrayList<String> paramToList = new ArrayList<String>();
                        paramToList.add(utcDate);
                        paramMap.put("P_TO_DATE", paramToList);
                        String CUSTOMER_REPORT_PATH = util.getBiReportPathStrings(cn, Constants.CUSTOMER_REPORT);
                        String CUSTOMER_CONTACT_REPORT_PATH = util.getBiReportPathStrings(cn,
                                Constants.CUSTOMER_CONTACT_REPORT);
                        String CUSTOMER_PROFILE_CLASS_REPORT_PATH = util.getBiReportPathStrings(cn,
                                Constants.CUSTOMER_PROFILE_CLASS_REPORT);
                        String CUSTOMER_CONTACT_TITLE_REPORT = util.getBiReportPathStrings(cn,
                                Constants.CUSTOMER_CONTACT_TITLE_REPOR);
                        // call and process customer report first
                        byte[] result = util.CallBIReport(CUSTOMER_REPORT_PATH, paramMap, biHeaderMap.get("HOST"),
                                biHeaderMap.get("USER_NAME"), biHeaderMap.get("PASSWORD"));
                        parseCustomerReport(result, cn);
                        // now call and process customer contact report
                        byte[] contactReportResult = util.CallBIReport(CUSTOMER_CONTACT_REPORT_PATH, paramMap,
                                biHeaderMap.get("HOST"), biHeaderMap.get("USER_NAME"), biHeaderMap.get("PASSWORD"));
                        CustomerContactReportProcessor.parseCustomerContactReport(contactReportResult, cn);
                        // process profile class report
                        byte[] custProfileClassResult = util.CallBIReport(CUSTOMER_PROFILE_CLASS_REPORT_PATH, null,
                                biHeaderMap.get("HOST"), biHeaderMap.get("USER_NAME"), biHeaderMap.get("PASSWORD"));
                        CustomerProfileClassProcessor.parseCustomerProfileClassReport(custProfileClassResult, cn);
                        // PROCESS CONTACT TITLE REPORT
                        byte[] custContactTitleResult = util.CallBIReport(CUSTOMER_CONTACT_TITLE_REPORT, null,
                                biHeaderMap.get("HOST"), biHeaderMap.get("USER_NAME"), biHeaderMap.get("PASSWORD"));
                        CustomerContactTitleProcessor.parseCustomerContactTitleReport(custContactTitleResult, cn);
                        util.updateOutBoundingLastRunDate(cn, schedulerId, utcDate);
                    } catch (Exception e) {
                        ExceptionLogParent exceptionLogParent = new ExceptionLogParent();
//                        exceptionLogParent.addLog(new ExceptionLog(e.getMessage(), null, "Vendor Report", null,
//                                "nep_VENDOR_MASTER", this.getClass().getName(), "executeVendorScheduler", e));
//                        exceptionLogParent.insertIntoExceptionLog(cn);
                        e.printStackTrace();
                    } finally {
                       // Util.updateSchedulerStatus(cn, schedulerId, "N");
                    }
          //      }
        //}
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.releaseConnetion(cn);
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            String schedulerId = jobExecutionContext.getJobDetail().getJobDataMap().get("SchedulerId").toString();
            executecustomerScheduler(schedulerId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseCustomerReport(byte[] bytes, Connection cn) throws Exception {
        InputStream reportStream = null;
        try {
            Digester digester = new Digester();
            digester.setValidating(false);
            digester.setUseContextClassLoader(true);
            digester.addObjectCreate("DATA_DS", CustomerMasterBean.class);
            digester.addObjectCreate("DATA_DS/G_1", CustomerBean.class);
            digester.addBeanPropertySetter("DATA_DS/G_1/CUSTOMER_NUMBER", "CUSTOMER_NUMBER");
            digester.addBeanPropertySetter("DATA_DS/G_1/CUSTOMER_NAME", "CUSTOMER_NAME");
            digester.addBeanPropertySetter("DATA_DS/G_1/PARTY_ID", "PARTY_ID");
            digester.addBeanPropertySetter("DATA_DS/G_1/ACCOUNT_NAME", "ACCOUNT_NAME");
            digester.addBeanPropertySetter("DATA_DS/G_1/ACCOUNT_NUMBER", "ACCOUNT_NUMBER");
            digester.addBeanPropertySetter("DATA_DS/G_1/CUST_ACCOUNT_ID", "CUST_ACCOUNT_ID");
            digester.addBeanPropertySetter("DATA_DS/G_1/PARTY_SITE_NUMBER", "PARTY_SITE_NUMBER");
            digester.addBeanPropertySetter("DATA_DS/G_1/CUST_ACCT_SITE_ID", "CUST_ACCT_SITE_ID");
            digester.addBeanPropertySetter("DATA_DS/G_1/PARTY_SITE_ID", "PARTY_SITE_ID");
            digester.addBeanPropertySetter("DATA_DS/G_1/ADDRESS1", "ADDRESS1");
            digester.addBeanPropertySetter("DATA_DS/G_1/ADDRESS2", "ADDRESS2");
            digester.addBeanPropertySetter("DATA_DS/G_1/ADDRESS3", "ADDRESS3");
            digester.addBeanPropertySetter("DATA_DS/G_1/ADDRESS4", "ADDRESS4");
            digester.addBeanPropertySetter("DATA_DS/G_1/CITY", "CITY");
            digester.addBeanPropertySetter("DATA_DS/G_1/COUNTRY", "COUNTRY");
            digester.addBeanPropertySetter("DATA_DS/G_1/PRIMARY_BILL_TO_SITE_FLAG", "PRIMARY_BILL_TO_SITE_FLAG");
            digester.addBeanPropertySetter("DATA_DS/G_1/PERSON_FIRST_NAME", "PERSON_FIRST_NAME");
            digester.addBeanPropertySetter("DATA_DS/G_1/PERSON_LAST_NAME", "PERSON_LAST_NAME");
            digester.addBeanPropertySetter("DATA_DS/G_1/PRIMARY_FLAG", "PRIMARY_FLAG");
            digester.addBeanPropertySetter("DATA_DS/G_1/PARTY_ORIG_REF", "PARTY_ORIG_REF");
            digester.addBeanPropertySetter("DATA_DS/G_1/ACCOUNT_ORIG_REF", "ACCOUNT_ORIG_REF");
            digester.addBeanPropertySetter("DATA_DS/G_1/SITE_ORIG_REF", "SITE_ORIG_REF");
            digester.addBeanPropertySetter("DATA_DS/G_1/POSTAL_CODE", "POSTAL_CODE");
            digester.addBeanPropertySetter("DATA_DS/G_1/LOC_ORIG_REF", "LOC_ORIG_REF");
            digester.addBeanPropertySetter("DATA_DS/G_1/CREATION_DATE", "CREATION_DATE");
            digester.addBeanPropertySetter("DATA_DS/G_1/PARTY_SITE_NAME", "PARTY_SITE_NAME");
            digester.addBeanPropertySetter("DATA_DS/G_1/PROFILE_CLASS", "PROFILE_CLASS");
            digester.addBeanPropertySetter("DATA_DS/G_1/SOURCE", "SOURCE");
            digester.addBeanPropertySetter("DATA_DS/G_1/STATE", "COUNTY");
            digester.addSetNext("DATA_DS/G_1", "setCustomerBean");
            reportStream = new ByteArrayInputStream(bytes);
            CustomerMasterBean customerMasterBean = (CustomerMasterBean) digester.parse(reportStream);
            if (customerMasterBean != null && customerMasterBean.getCustomerList() != null
                    && customerMasterBean.getCustomerList().size() > 0) {
                for (int i = 0; i < customerMasterBean.getCustomerList().size(); i++) {
                    CustomerBean customerBean = customerMasterBean.getCustomerList().get(i);
                    if (customerBean != null && customerBean.getACCOUNT_NUMBER() != null) {

                        Integer batchId = insertUpdateCustomerDetails(customerBean, cn);

                        Integer addressId = insertUpdateCustomerAddressDetails(batchId, customerBean, cn);

                        insertUpdateCustomerSiteDetails(batchId, addressId, customerBean, cn);
                    }
                }
                cn.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            cn.rollback();
            throw e;
        } finally {
            if (reportStream != null) {
                reportStream.close();
            }
        }
    }

    // process customer details
    public Integer insertUpdateCustomerDetails(CustomerBean customerBean, Connection cn) {
        String seq = null;
        Integer returningBatchId = null;
        String statement = "select batch_id FROM CUSTOMER_DETAILS_TBL WHERE CUST_ACC_ORIG_SYS_REF = ?";
        String statemetUp = "update CUSTOMER_DETAILS_TBL set ORGANIZATION_NAME=?,ACCOUNT_NAME=?,SOURCE=?,PARTY_ORIG_SYS_REF=?,ACTION=?,FUSION_LOAD_STATUS=? WHERE CUST_ACC_ORIG_SYS_REF = ?";
        String statemetIn = "insert into CUSTOMER_DETAILS_TBL (BATCH_ID,ORGANIZATION_NAME,ACCOUNT_NAME,SOURCE,PARTY_ORIG_SYS_REF,CUST_ACC_ORIG_SYS_REF,ACTION,CUST_SRC_TYPE,FUSION_LOAD_STATUS)"
                + "values(?,?,?,?,?,?,?,?,?)";
        String statementSeq = "select CUSTOMER_BATCH_ID_SEQ.nextval SEQUENCE from dual";
        PreparedStatement ps = null;
        PreparedStatement psUp = null;
        PreparedStatement psSeq = null;
        ResultSet rs = null;
        ResultSet rsSeq = null;
        try {
            ps = cn.prepareStatement(statement);
            ps.setString(1, customerBean.getACCOUNT_ORIG_REF());
            rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                returningBatchId = rs.getInt("batch_id");
                // update scene
                psUp = cn.prepareStatement(statemetUp);
                setUpdateParametersForCustomerDetails(psUp, customerBean);
                psUp.executeUpdate();
            } else {
                // insert scene
                psSeq = cn.prepareStatement(statementSeq);
                rsSeq = psSeq.executeQuery();
                if (rsSeq != null && rsSeq.next()) {
                    seq = rsSeq.getString("SEQUENCE");
                    psUp = cn.prepareStatement(statemetIn);
                    setInsertParametersForCustomerDetails(psUp, customerBean);
                    psUp.setString(1, seq);
                    returningBatchId = Integer.valueOf(seq);
                    psUp.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (psUp != null) {
                    psUp.close();
                }
                if (psSeq != null) {
                    psSeq.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (rsSeq != null) {
                    rsSeq.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return returningBatchId;
    }

    public void setUpdateParametersForCustomerDetails(PreparedStatement psUp, CustomerBean customerBean)
            throws SQLException {
        psUp.setString(1, customerBean.getCUSTOMER_NAME());
        psUp.setString(2, customerBean.getACCOUNT_NAME());
        psUp.setString(3, customerBean.getSOURCE());
        psUp.setString(4, customerBean.getPARTY_ORIG_REF());
        psUp.setString(5, "U");
        psUp.setString(6, "Loaded");
        psUp.setString(7, customerBean.getACCOUNT_ORIG_REF());
    }

    public void setInsertParametersForCustomerDetails(PreparedStatement psUp, CustomerBean customerBean)
            throws SQLException {
        // 1 for batch id
        psUp.setString(2, customerBean.getCUSTOMER_NAME());
        psUp.setString(3, customerBean.getACCOUNT_NAME());
        psUp.setString(4, customerBean.getSOURCE());
        psUp.setString(5, customerBean.getPARTY_ORIG_REF());
        psUp.setString(6, customerBean.getACCOUNT_ORIG_REF());
        psUp.setString(7, "U");
        psUp.setString(8, "Fusion");
        psUp.setString(9, "Loaded");
    }

    // process address details
    public Integer insertUpdateCustomerAddressDetails(Integer batchId, CustomerBean customerBean, Connection cn) {
        String seq = null;
        Integer returningAddressId = null;
        String statement = "select ADDRESS_ID FROM customer_address_details_tbl WHERE BATCH_ID = ?";
        String statemetUp = "update customer_address_details_tbl set COUNTRY_CODE=?,ADDRESS1=?,ADDRESS2=?,ADDRESS3=?,CITY=?,COUNTY=?,POSTAL_CODE=?,PRIMARY_FLAG=?,SET_ID=? WHERE BATCH_ID = ?";
        String statemetIn = "insert into customer_address_details_tbl (ADDRESS_ID,COUNTRY_CODE,ADDRESS1,ADDRESS2,ADDRESS3,CITY,COUNTY,POSTAL_CODE,PRIMARY_FLAG,SET_ID,BATCH_ID)values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String statementSeq = "select CUSTOMER_SITE_ID_SEQ.nextval SEQUENCE from dual";
        PreparedStatement ps = null;
        PreparedStatement psUp = null;
        PreparedStatement psSeq = null;
        ResultSet rs = null;
        ResultSet rsSeq = null;
        try {
            ps = cn.prepareStatement(statement);
            ps.setString(1, batchId.toString());
            rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                // update scene
                returningAddressId = rs.getInt("ADDRESS_ID");
                psUp = cn.prepareStatement(statemetUp);
                setUpdateParametersForAddressDetails(psUp, customerBean, batchId);
                psUp.executeUpdate();
            } else {
                // insert scene
                psSeq = cn.prepareStatement(statementSeq);
                rsSeq = psSeq.executeQuery();
                if (rsSeq != null && rsSeq.next()) {
                    seq = rsSeq.getString("SEQUENCE");
                    psUp = cn.prepareStatement(statemetIn);
                    setInsertParametersForAddressDetails(psUp, customerBean, batchId);
                    psUp.setString(1, seq);
                    returningAddressId = Integer.valueOf(seq);
                    psUp.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (psUp != null) {
                    psUp.close();
                }
                if (psSeq != null) {
                    psSeq.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (rsSeq != null) {
                    rsSeq.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return returningAddressId;
    }

    public void setUpdateParametersForAddressDetails(PreparedStatement psUp, CustomerBean customerBean, Integer batchId)
            throws SQLException {
        // country code
        psUp.setString(1, customerBean.getCOUNTRY());
        // address1
        psUp.setString(2, customerBean.getADDRESS1());
        // address 2
        psUp.setString(3, customerBean.getADDRESS2());
        // address 3
        psUp.setString(4, customerBean.getADDRESS3());
        // city
        psUp.setString(5, customerBean.getCITY());
        // county
        psUp.setString(6, customerBean.getCOUNTY());
        // postal code
        psUp.setString(7, customerBean.getPOSTAL_CODE());
        // primary flag
        psUp.setString(8, "true");
        // set
        psUp.setString(9, "SET0000"); // todo falguni to add in report
        // where batch id
        psUp.setString(10, batchId.toString());
    }

    public void setInsertParametersForAddressDetails(PreparedStatement psUp, CustomerBean customerBean, Integer batchId)
            throws SQLException {
        // 1 for ADDRESS_ID

        // COUNTRY_CODE
        psUp.setString(2, customerBean.getCOUNTRY());
        // ADDRESS1
        psUp.setString(3, customerBean.getADDRESS1());
        // ADDRESS2
        psUp.setString(4, customerBean.getADDRESS2());
        // ADDRESS3
        psUp.setString(5, customerBean.getADDRESS3());
        // CITY
        psUp.setString(6, customerBean.getCITY());
        // COUNTY
        psUp.setString(7, customerBean.getCOUNTY()); // todo falguni to add in report
        // POSTAL_CODE
        psUp.setString(8, customerBean.getPOSTAL_CODE());
        // PRIMARY_FLAG
        psUp.setString(9, "true");
        // SET_ID
        psUp.setString(10, "SET0000"); // todo falguni to add in report
        // BATCH_ID
        psUp.setString(11, batchId.toString());
    }

    // process site details
    public void insertUpdateCustomerSiteDetails(Integer batchId, Integer addressId, CustomerBean customerBean,
            Connection cn) {
        String seq = null;
        String statement = "select site_id FROM customer_site_details_tbl WHERE SITE_ORIG_SYS_REF_NO = ?";
        String statemetUp = "update customer_site_details_tbl set PRIMARY_FLAG = ?, PARTY_SITE_NAME = ?, PARTY_SITE_NUMBER = ?, SITE_PROFILE_CLASS = ?, BATCH_ID = ?, ACTION = ?, ACCOUNT_NUMBER = ? WHERE SITE_ORIG_SYS_REF_NO = ?";
        String statemetIn = "insert into customer_site_details_tbl ( SITE_ID, PRIMARY_FLAG, PARTY_SITE_NAME, PARTY_SITE_NUMBER, SITE_PROFILE_CLASS, SITE_ORIG_SYS_REF_NO, BATCH_ID, REF_ADDRESS_ID, SITE_SRC_TYPE, ACTION, ACCOUNT_NUMBER ) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String statementSeq = "select CUSTOMER_ADDRESS_ID_SEQ.nextval SEQUENCE from dual";
        PreparedStatement ps = null;
        PreparedStatement psUp = null;
        PreparedStatement psSeq = null;
        ResultSet rs = null;
        ResultSet rsSeq = null;
        try {
            ps = cn.prepareStatement(statement);
            ps.setString(1, customerBean.getSITE_ORIG_REF());
            rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                // update scene
                psUp = cn.prepareStatement(statemetUp);
                setUpdateParametersForSiteDetails(psUp, customerBean, batchId);
                psUp.executeUpdate();
            } else {
                // insert scene
                psSeq = cn.prepareStatement(statementSeq);
                rsSeq = psSeq.executeQuery();
                if (rsSeq != null && rsSeq.next()) {
                    seq = rsSeq.getString("SEQUENCE");
                    psUp = cn.prepareStatement(statemetIn);
                    setInsertParametersForSiteDetails(psUp, customerBean, batchId, addressId);
                    psUp.setString(1, seq);
                    psUp.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (psUp != null) {
                    psUp.close();
                }
                if (psSeq != null) {
                    psSeq.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (rsSeq != null) {
                    rsSeq.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setUpdateParametersForSiteDetails(PreparedStatement psUp, CustomerBean customerBean, Integer batchId)
            throws SQLException {
        // PRIMARY_FLAG
        psUp.setString(1, customerBean.getPRIMARY_BILL_TO_SITE_FLAG());
        // PARTY_SITE_NAME
        psUp.setString(2, customerBean.getPARTY_SITE_NAME());
        // PARTY_SITE_NUMBER
        psUp.setString(3, customerBean.getPARTY_SITE_NUMBER());
        // SITE_PROFILE_CLASS
        psUp.setString(4, customerBean.getPROFILE_CLASS());
        // BATCH_ID
        psUp.setString(5, batchId.toString());
        // ACTION
        psUp.setString(6, "U");
        // ACCOUNT_NUMBER
        psUp.setString(7, customerBean.getACCOUNT_NUMBER());
        // where SITE_ORIG_SYS_REF_NO
        psUp.setString(8, customerBean.getSITE_ORIG_REF());
    }

    public void setInsertParametersForSiteDetails(PreparedStatement psUp, CustomerBean customerBean, Integer batchId,
            Integer addressId) throws SQLException {
        // 1 for SITE_ID

        // PRIMARY_FLAG
        psUp.setString(2, customerBean.getPRIMARY_BILL_TO_SITE_FLAG());
        // PARTY_SITE_NAME
        psUp.setString(3, customerBean.getPARTY_SITE_NAME());
        // PARTY_SITE_NUMBER
        psUp.setString(4, customerBean.getPARTY_SITE_NUMBER());
        // SITE_PROFILE_CLASS
        psUp.setString(5, customerBean.getPROFILE_CLASS());
        // SITE_ORIG_SYS_REF_NO
        psUp.setString(6, customerBean.getSITE_ORIG_REF());
        // BATCH_ID
        psUp.setString(7, batchId.toString());
        // REF_ADDRESS_ID
        psUp.setString(8, addressId.toString());
        // SITE_SRC_TYPE
        psUp.setString(9, "Fusion");
        // ACTION
        psUp.setString(10, "U");
        // ACCOUNT_NUMBER
        psUp.setString(11, customerBean.getACCOUNT_NUMBER());
    }

}
