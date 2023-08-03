package ei.ar.integration.reports.customer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.digester.Digester;

public class CustomerContactReportProcessor {

    public static void parseCustomerContactReport(byte[] bytes, Connection cn) throws Exception {
        InputStream reportStream = null;
        try {
            Digester digester = new Digester();
            digester.setValidating(false);
            digester.setUseContextClassLoader(true);
            digester.addObjectCreate("DATA_DS", CustomerContactMasterBean.class);
            digester.addObjectCreate("DATA_DS/G_1", CustomerContactBean.class);

            digester.addBeanPropertySetter("DATA_DS/G_1/TITLE", "PERSON_TITLE");
            digester.addBeanPropertySetter("DATA_DS/G_1/PERSON_FIRST_NAME", "PERSON_FIRST_NAME");
            digester.addBeanPropertySetter("DATA_DS/G_1/PERSON_LAST_NAME", "PERSON_LAST_NAME");
            digester.addBeanPropertySetter("DATA_DS/G_1/DUNNING_FLAG", "DUNNING_LETTERS");
            digester.addBeanPropertySetter("DATA_DS/G_1/STMNT_FLAG", "STATEMENT_FLAG");
            digester.addBeanPropertySetter("DATA_DS/G_1/BILL_TO_FLAG", "BILL_TO_FLAG");
            digester.addBeanPropertySetter("DATA_DS/G_1/CONT_ROLE_ORIG_SYS_REF", "CONTACT_ROLE_ORIG_SYS_REF_NO");
            digester.addBeanPropertySetter("DATA_DS/G_1/CUST_ACC_ORIG_SYS_REF", "CUST_ACC_ORIG_SYS_REF");
            digester.addBeanPropertySetter("DATA_DS/G_1/REL_ORIG_SYS_REF_NO", "REL_ORIG_SYSTEM_REF_NO");
            // digester.addBeanPropertySetter("DATA_DS/G_1/PS_ORIG_SYS_REF_NO", "");
            // probably wrong, take call with falguni and discuss
            digester.addBeanPropertySetter("DATA_DS/G_1/OBJ_ORIG_SYS_REF", "OBJ_ORIG_SYS_REF");
            // digester.addBeanPropertySetter("DATA_DS/G_1/SUB_ORIG_SYS_REF", ""); probably
            // wrong, take call with falguni and discuss
            // digester.addBeanPropertySetter("DATA_DS/G_1/ROLE_RESP_ORIG_SYS_REF", "");
            // need to be corrected on both app and report level
            digester.addBeanPropertySetter("DATA_DS/G_1/LOC_ORIG_SYS_REF", "LOC_ORIG_SYS_REF"); // this is parent site
                                                                                                // for contact
            digester.addBeanPropertySetter("DATA_DS/G_1/CUS_CONTACT_ORIG_SYS_REF", "CUST_CONTACT_ORIG_SYS_REF_NO");

            digester.addSetNext("DATA_DS/G_1", "setCustomerContactBean");

            reportStream = new ByteArrayInputStream(bytes);
            CustomerContactMasterBean customerContactMasterBean = (CustomerContactMasterBean) digester
                    .parse(reportStream);
            if (customerContactMasterBean != null && customerContactMasterBean.getCustomerContactList() != null
                    && customerContactMasterBean.getCustomerContactList().size() > 0) {
                for (int i = 0; i < customerContactMasterBean.getCustomerContactList().size(); i++) {
                    CustomerContactBean customerContactBean = customerContactMasterBean.getCustomerContactList().get(i);
                    if (customerContactBean != null) {
//                        System.out.println("********* processing customer contact details...");
                        Map<String, Integer> siteDetails = getSiteDetails(customerContactBean.getLOC_ORIG_SYS_REF(),
                                cn);
                        if (!siteDetails.isEmpty()) {
                            insertUpdateCustomerContactDetails(siteDetails.get("batchId"), siteDetails.get("siteId"),
                                    customerContactBean, cn);
                        }

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

    // process address details
    public static void insertUpdateCustomerContactDetails(Integer batchId, Integer refSiteId,
            CustomerContactBean customerContactBean, Connection cn) {
        String seq = null;
        String statement = "select contact_id FROM customer_contact_details_tbl WHERE CUST_CONTACT_ORIG_SYS_REF_NO = ?";
        String statemetUp = "update customer_contact_details_tbl set PERSON_FIRST_NAME = ?, PERSON_LAST_NAME = ?, BILL_TO_FLAG = ?, STATEMENT_FLAG = ?, DUNNING_LETTERS = ?, EMAIL_ADDRESS = ?, PHONE_NUMBER = ?, CONTACT_ROLE_ORIG_SYS_REF_NO = ?, CUST_ACC_ORIG_SYS_REF = ?, REL_ORIG_SYSTEM_REF_NO = ?, OBJ_ORIG_SYS_REF = ?, REF_SITE_ID = ?, BATCH_ID = ? WHERE CUST_CONTACT_ORIG_SYS_REF_NO = ?";
        String statemetIn = "insert into customer_contact_details_tbl ( CONTACT_ID, PERSON_FIRST_NAME, PERSON_LAST_NAME, BILL_TO_FLAG, STATEMENT_FLAG, DUNNING_LETTERS, EMAIL_ADDRESS, PHONE_NUMBER, CONTACT_ROLE_ORIG_SYS_REF_NO, CUST_ACC_ORIG_SYS_REF, REL_ORIG_SYSTEM_REF_NO, OBJ_ORIG_SYS_REF, REF_SITE_ID, BATCH_ID, CUST_CONTACT_ORIG_SYS_REF_NO ) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String statementSeq = "select CUSTOMER_CONTACT_ID_SEQ.nextval SEQUENCE from dual";
        PreparedStatement ps = null;
        PreparedStatement psUp = null;
        PreparedStatement psSeq = null;
        ResultSet rs = null;
        ResultSet rsSeq = null;
        try {
            ps = cn.prepareStatement(statement);
            ps.setString(1, customerContactBean.getCUST_CONTACT_ORIG_SYS_REF_NO());
            rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                // update scene
//                System.out.println("customer contact update scene");
//                System.out.println(rs.getString(1));
//                System.out.println("----------------------------------");
                psUp = cn.prepareStatement(statemetUp);
                setUpdateParametersForContactDetails(psUp, customerContactBean, batchId, refSiteId);
                psUp.executeUpdate();
            } else {
                // insert scene
//                System.out.println("customer contact insert scene");
                psSeq = cn.prepareStatement(statementSeq);
                rsSeq = psSeq.executeQuery();
                if (rsSeq != null && rsSeq.next()) {
                    seq = rsSeq.getString("SEQUENCE");
                    psUp = cn.prepareStatement(statemetIn);
                    setInsertParametersForContactDetails(psUp, customerContactBean, batchId, refSiteId);
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

    public static void setUpdateParametersForContactDetails(PreparedStatement psUp,
            CustomerContactBean customerContactBean, Integer batchId, Integer refSiteID) throws SQLException {
        // PERSON_FIRST_NAME
        psUp.setString(1, customerContactBean.getPERSON_FIRST_NAME());

        // PERSON_LAST_NAME
        psUp.setString(2, customerContactBean.getPERSON_LAST_NAME());

        // BILL_TO_FLAG
        psUp.setString(3, customerContactBean.getBILL_TO_FLAG());

        // STATEMENT_FLAG
        psUp.setString(4, customerContactBean.getSTATEMENT_FLAG());

        // DUNNING_LETTERS
        psUp.setString(5, customerContactBean.getDUNNING_LETTERS());

        // EMAIL_ADDRESS
        psUp.setString(6, customerContactBean.getEMAIL_ADDRESS());

        // PHONE_NUMBER
        psUp.setString(7, customerContactBean.getPHONE_NUMBER());

        // CONTACT_ROLE_ORIG_SYS_REF_NO
        psUp.setString(8, customerContactBean.getCONTACT_ROLE_ORIG_SYS_REF_NO());

        // CUST_ACC_ORIG_SYS_REF
        psUp.setString(9, customerContactBean.getCUST_ACC_ORIG_SYS_REF());

        // REL_ORIG_SYSTEM_REF_NO
        psUp.setString(10, customerContactBean.getREL_ORIG_SYSTEM_REF_NO());

        // OBJ_ORIG_SYS_REF
        psUp.setString(11, customerContactBean.getOBJ_ORIG_SYS_REF());

        // REF_SITE_ID
        psUp.setString(12, refSiteID.toString());

        // BATCH_ID
        psUp.setString(13, batchId.toString());

        // where CUST_CONTACT_ORIG_SYS_REF_NO
        psUp.setString(14, customerContactBean.getCUST_CONTACT_ORIG_SYS_REF_NO());
    }

    public static void setInsertParametersForContactDetails(PreparedStatement psUp,
            CustomerContactBean customerContactBean, Integer batchId, Integer refSiteId) throws SQLException {
        // 1 for CONTACT_ID

        // PERSON_FIRST_NAME
        psUp.setString(2, customerContactBean.getPERSON_FIRST_NAME());

        // PERSON_LAST_NAME
        psUp.setString(3, customerContactBean.getPERSON_LAST_NAME());

        // BILL_TO_FLAG
        psUp.setString(4, customerContactBean.getBILL_TO_FLAG());

        // STATEMENT_FLAG
        psUp.setString(5, customerContactBean.getSTATEMENT_FLAG());

        // DUNNING_LETTERS
        psUp.setString(6, customerContactBean.getDUNNING_LETTERS());

        // EMAIL_ADDRESS
        psUp.setString(7, customerContactBean.getEMAIL_ADDRESS());

        // PHONE_NUMBER
        psUp.setString(8, customerContactBean.getPHONE_NUMBER());

        // CONTACT_ROLE_ORIG_SYS_REF_NO
        psUp.setString(9, customerContactBean.getCONTACT_ROLE_ORIG_SYS_REF_NO());

        // CUST_ACC_ORIG_SYS_REF
        psUp.setString(10, customerContactBean.getCUST_ACC_ORIG_SYS_REF());

        // REL_ORIG_SYSTEM_REF_NO
        psUp.setString(11, customerContactBean.getREL_ORIG_SYSTEM_REF_NO());

        // OBJ_ORIG_SYS_REF
        psUp.setString(12, customerContactBean.getOBJ_ORIG_SYS_REF());

        // REF_SITE_ID
        psUp.setString(13, refSiteId.toString());

        // BATCH_ID
        psUp.setString(14, batchId.toString());

        // CUST_CONTACT_ORIG_SYS_REF_NO
        psUp.setString(15, customerContactBean.getCUST_CONTACT_ORIG_SYS_REF_NO());
    }

    public static Map<String, Integer> getSiteDetails(String siteOrigSysRef, Connection cn) throws SQLException {

        Map<String, Integer> returningMap = new HashMap<>();

        String statement = "select site_id, batch_id FROM customer_site_details_tbl WHERE site_orig_sys_ref_no = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = cn.prepareStatement(statement);
            ps.setString(1, siteOrigSysRef);
            rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                returningMap.put("siteId", rs.getInt("SITE_ID"));
                returningMap.put("batchId", rs.getInt("BATCH_ID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }

        return returningMap;
    }

}