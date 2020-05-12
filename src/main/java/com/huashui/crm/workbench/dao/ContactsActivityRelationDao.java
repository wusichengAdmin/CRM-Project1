package com.huashui.crm.workbench.dao;

import com.huashui.crm.workbench.domain.ContactsActivityRelation;

import java.util.List;

public interface ContactsActivityRelationDao {

    void saveRelation(List<ContactsActivityRelation> contactsActivityRelationList);
}
