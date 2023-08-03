package ei.ar.integration.reports.customer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.digester.Digester;

public class CustomerProfileClassProcessor {

    public static void parseCustomerProfileClassReport(byte[] bytes, Connection cn) throws Exception {
        InputStream reportStream = null;
        try {
            Digester digester = new Digester();
            digester.setValidating(false);
            digester.setUseContextClassLoader(true);
            digester.addObjectCreate("DATA_DS", CustomerProfileClassMasterBean.class);
            digester.addObjectCreate("DATA_DS/G_PROFILE_CLASS", CustomerProfileClassBean.class);

            digester.addBeanPropertySetter("DATA_DS/G_PROFILE_CLASS/NAME", "CLASS_NAME");
            digester.addSetNext("DATA_DS/G_PROFILE_CLASS", "setCustomerProfileClassBean");

            reportStream = new ByteArrayInputStream(bytes);

            CustomerProfileClassMasterBean customerProfileClassMasterBean = (CustomerProfileClassMasterBean) digester
                    .parse(reportStream);

            if (customerProfileClassMasterBean != null
                    && customerProfileClassMasterBean.getCustomerProfileClassList() != null
                    && customerProfileClassMasterBean.getCustomerProfileClassList().size() > 0) {
                emptyCustomerProfileClassTable(cn);
                for (int i = 0; i < customerProfileClassMasterBean.getCustomerProfileClassList().size(); i++) {
                    CustomerProfileClassBean customerProfileClassBean = customerProfileClassMasterBean
                            .getCustomerProfileClassList().get(i);
                    if (customerProfileClassBean != null) {

                        insertCustomerProfileClass(i + 1, customerProfileClassBean, cn);
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

    private static void insertCustomerProfileClass(Integer id, CustomerProfileClassBean customerProfileClassBean,
            Connection cn) throws SQLException {
        PreparedStatement ps = null;
        String statement = "insert into CUSTOMER_PROFILE_CLASS_LOV (CLASS_ID,CLASS_NAME) values(?, ?)";

        try {
            ps = cn.prepareStatement(statement);
            ps.setInt(1, id);
            ps.setString(2, customerProfileClassBean.getCLASS_NAME());
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

    private static void emptyCustomerProfileClassTable(Connection cn) throws SQLException {
        PreparedStatement ps = null;
        String statement = "delete from CUSTOMER_PROFILE_CLASS_LOV";

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
