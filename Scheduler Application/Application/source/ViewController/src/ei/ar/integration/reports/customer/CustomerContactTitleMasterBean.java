package ei.ar.integration.reports.customer;

import java.util.ArrayList;

public class CustomerContactTitleMasterBean {
    CustomerContactTitleBean customerContactTitleBean = null;
    ArrayList<CustomerContactTitleBean> customerContactTitleList = null;

    public void setCustomerContactTitleBean(CustomerContactTitleBean customerContactTitleBean) {
        if(customerContactTitleList==null){
            customerContactTitleList = new ArrayList<CustomerContactTitleBean>();
        }
        customerContactTitleList.add(customerContactTitleBean);
        this.customerContactTitleBean = customerContactTitleBean;
    }

    public CustomerContactTitleBean getCustomerContactTitleBean() {
        return customerContactTitleBean;
    }

    public void setCustomerContactTitleList(ArrayList<CustomerContactTitleBean> customerContactTitleList) {
        this.customerContactTitleList = customerContactTitleList;
    }

    public ArrayList<CustomerContactTitleBean> getCustomerContactTitleList() {
        return customerContactTitleList;
    }
}
