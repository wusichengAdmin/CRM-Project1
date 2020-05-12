package com.huashui.crm.workbench.service.impl;

import com.huashui.crm.workbench.dao.ContactsDao;
import com.huashui.crm.workbench.domain.Contacts;
import com.huashui.crm.workbench.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 华水吴彦祖
 * 2020/2/18
 */
@Service
public class ContactsServiceImpl implements ContactsService {

    @Autowired
    private ContactsDao contactsDao;

    @Override
    public List<Contacts> getActivityListByContactsName(String searchContacts) {

        List<Contacts> cList = contactsDao.getActivityListByContactsName(searchContacts);

        for (Contacts con : cList) {

            String email = con.getEmail();

            if(email==null){

                con.setEmail("-");
            }

            String mphone = con.getMphone();

            if(mphone == null){

                con.setMphone("-");
            }

        }


        return cList;
    }
}
