package com.huashui.crm.settings.dao;

import com.huashui.crm.settings.domain.DicType;

import java.util.List;

/**
 * 华水吴彦祖
 * 2020/1/13
 */
public interface DicTypeDao {

    List<DicType> getTypeList();

    int getCountByCode(String code);

    void saveType(DicType dt);

    DicType getTypeByCode(String code);

    void updataType(DicType dt);

    void deleteTypeByCodes(String[] codes);
}
