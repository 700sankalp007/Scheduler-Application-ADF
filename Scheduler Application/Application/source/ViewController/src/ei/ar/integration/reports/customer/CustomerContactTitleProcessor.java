package ei.ar.integration.reports.customer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.digester.Digester;

public class CustomerContactTitleProcessor {

    public static void parseCustomerContactTitleReport(byte[] bytes, Connection cn) throws Exception {
        InputStream reportStream = null;
        try {
            Digester digester = new Digester();
            digester.setValidating(false);
            digester.setUseContextClassLoader(true);
            digester.addObjectCreate("DATA_DS", CustomerContactTitleMasterBean.class);
            digester.addObjectCreate("DATA_DS/G_TITLE", CustomerContactTitleBean.class);

            digester.addBeanPropertySetter("DATA_DS/G_TITLE/MEANING", "TITLE");
            digester.addSetNext("DATA_DS/G_TITLE", "setCustomerContactTitleBean");

            reportStream = new ByteArrayInputStream(bytes);

            CustomerContactTitleMasterBean customerContactTitleMasterBean = (CustomerContactTitleMasterBean) digester
                    .parse(reportStream);
            if (customerContactTitleMasterBean != null
                    && customerContactTitleMasterBean.getCustomerContactTitleList() != null
                    && customerContactTitleMasterBean.getCustomerContactTitleList().size() > 0) {
                emptyCustomerContactTitleTable(cn);
                for (int i = 0; i < customerContactTitleMasterBean.getCustomerContactTitleList().size(); i++) {
                    CustomerContactTitleBean customerContactTitleBean = customerContactTitleMasterBean
                            .getCustomerContactTitleList().get(i);
                    if (customerContactTitleBean != null) {
                        insertCustomerContactTitle(i + 1, customerContactTitleBean, cn);
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

    private static void insertCustomerContactTitle(Integer id, CustomerContactTitleBean customerContactTitleBean,
            Connection cn) throws SQLException {
        PreparedStatement ps = null;
        String statement = "insert into CUSTOMER_TITLE_LOV (TITLE_ID,TITLE) values(?, ?)";

        try {
            ps = cn.prepareStatement(statement);
            ps.setInt(1, id);
            ps.setString(2, customerContactTitleBean.getTITLE());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    private static void emptyCustomerContactTitleTable(Connection cn) throws SQLException {
        PreparedStatement ps = null;
        String statement = "delete from CUSTOMER_TITLE_LOV";

        try {
            ps = cn.prepareStatement(statement);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

}
