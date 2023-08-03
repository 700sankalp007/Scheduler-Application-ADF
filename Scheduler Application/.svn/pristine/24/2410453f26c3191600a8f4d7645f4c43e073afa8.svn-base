package ei.ar.integration.reports.customer;

import java.util.ArrayList;

public class CustomerMasterBean {
    CustomerBean customerBean = null;
    ArrayList<CustomerBean> customerList = null;
    public CustomerMasterBean() {
        super();
    }
    public void setCustomerBean(CustomerBean customerBean) {
        if(customerList==null){
            customerList = new ArrayList<CustomerBean>();
        }
        customerList.add(customerBean);
        this.customerBean = customerBean;
    }

    public CustomerBean getCustomerBean() {
        return customerBean;
    }

    public void setCustomerList(ArrayList<CustomerBean> customerList) {
        this.customerList = customerList;
    }

    public ArrayList<CustomerBean> getCustomerList() {
        return customerList;
    }
}
