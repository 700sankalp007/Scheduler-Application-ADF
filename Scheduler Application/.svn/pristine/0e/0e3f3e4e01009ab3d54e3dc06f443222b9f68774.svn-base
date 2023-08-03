package ei.ar.integration.reports.customer;

import java.util.ArrayList;

public class CustomerContactMasterBean {
    CustomerContactBean customerContactBean = null;
    ArrayList<CustomerContactBean> customerList = null;

    public void setCustomerContactBean(CustomerContactBean customerContactBean) {
        if (customerList == null) {
            customerList = new ArrayList<CustomerContactBean>();
        }
        customerList.add(customerContactBean);
        this.customerContactBean = customerContactBean;
    }

    public CustomerContactBean getCustomerContactBean() {
        return customerContactBean;
    }

    public void setCustomerContactList(ArrayList<CustomerContactBean> customerList) {
        this.customerList = customerList;
    }

    public ArrayList<CustomerContactBean> getCustomerContactList() {
        return customerList;
    }
}
