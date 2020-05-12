package com.huashui.crm.workbench.service;

import com.huashui.crm.workbench.domain.Contacts;

import java.util.List;

/**
 * 华水吴彦祖
 * 2020/2/18
 */
public interface ContactsService {


    List<Contacts> getActivityListByContactsName(String searchContacts);
}
