package com.huashui.crm.workbench.dao;

import com.huashui.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    void saveCustomer(Customer cus);

    List<String> getCustomerNamesByName(String name);
}
