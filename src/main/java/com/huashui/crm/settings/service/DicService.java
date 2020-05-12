package com.huashui.crm.settings.service;

import com.huashui.crm.settings.domain.DicType;
import com.huashui.crm.settings.domain.DicValue;

import java.util.List;
import java.util.Map;

/**
 * 华水吴彦祖
 * 2020/1/13
 */
public interface DicService {

    List<DicType> getTypeList();

    boolean checkCode(String code);

    void saveType(DicType dt);

    DicType getTypeByCode(String code);

    void updataType(DicType dt);

    void deleteType(String[] codes);

    List<DicValue> getValueList();

    void saveValue(DicValue dv);

    Map<String, List<DicValue>> getAll();

    DicValue getValueById(String id);

    void updateValue(DicValue dv);

    void deleteValue(String[] id);
}
