package ei.ar.integration.reports.customer;

import java.util.ArrayList;

public class CustomerProfileClassMasterBean {
    CustomerProfileClassBean customerProfileClassBean = null;
    ArrayList<CustomerProfileClassBean> customerProfileClassList = null;

    public void setCustomerProfileClassBean(CustomerProfileClassBean customerProfileClassBean) {
        if(customerProfileClassList==null){
            customerProfileClassList = new ArrayList<CustomerProfileClassBean>();
        }
        customerProfileClassList.add(customerProfileClassBean);
        this.customerProfileClassBean = customerProfileClassBean;
    }

    public CustomerProfileClassBean getCustomerProfileClassBean() {
        return customerProfileClassBean;
    }

    public void setCustomerProfileClassList(ArrayList<CustomerProfileClassBean> customerProfileClassList) {
        this.customerProfileClassList = customerProfileClassList;
    }

    public ArrayList<CustomerProfileClassBean> getCustomerProfileClassList() {
        return customerProfileClassList;
    }
}
