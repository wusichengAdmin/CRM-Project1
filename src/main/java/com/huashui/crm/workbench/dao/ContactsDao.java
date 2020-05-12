package com.huashui.crm.workbench.dao;

import com.huashui.crm.workbench.domain.Contacts;

import java.util.List;

public interface ContactsDao {

    void saveContacts(Contacts con);

    List<Contacts> getActivityListByContactsName(String searchContacts);
}
