package com.huashui.crm.settings.dao;

import com.huashui.crm.settings.domain.DicValue;

import java.util.List;

/**
 * 华水吴彦祖
 * 2020/1/13
 */
public interface DicValueDao {

   // void deleteValueByTypeCode(String[] codes);

    void deleteValueByTypeCodes(String[] codes);

    List<DicValue> getValueList();

    void saveValue(DicValue dv);

    List<DicValue> getValueListByTypeCode(String code);

    DicValue getValueById(String id);

    void updateValue(DicValue dv);

    void deleteValue(String[] id);
}
