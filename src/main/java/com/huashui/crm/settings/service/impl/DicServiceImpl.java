package com.huashui.crm.settings.service.impl;

import com.huashui.crm.settings.dao.DicTypeDao;
import com.huashui.crm.settings.dao.DicValueDao;
import com.huashui.crm.settings.domain.DicType;
import com.huashui.crm.settings.domain.DicValue;
import com.huashui.crm.settings.service.DicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 华水吴彦祖
 * 2020/1/13
 */
@Service
public class DicServiceImpl implements DicService {


    @Autowired
    private DicTypeDao dicTypeDao;

    @Autowired
    private DicValueDao dicValueDao;


    @Override
    public boolean checkCode(String code) {
        boolean flag = true;
        int count = dicTypeDao.getCountByCode(code);
        if(count != 0){
            flag = false;
        }
        return flag;
    }

    @Override
    public void saveType(DicType dt) {

        dicTypeDao.saveType(dt);

    }

    @Override
    public DicType getTypeByCode(String code) {
        DicType dt = dicTypeDao.getTypeByCode(code);
        return dt;
    }

    @Override
    public void updataType(DicType dt) {
        dicTypeDao.updataType(dt);
    }

    @Override
    public void deleteType(String[] codes) {
        //在删除一之前，我们要先删除相关联的多
        //对于我们现在的需求
        //需要先删除字典类型关联的字典值，再删除类型
        dicValueDao.deleteValueByTypeCodes(codes);
        //删除字典类型
        dicTypeDao.deleteTypeByCodes(codes);
    }

    @Override
    public List<DicValue> getValueList() {
        List<DicValue> dvList = dicValueDao.getValueList();
        return dvList;
    }

    @Override
    public void saveValue(DicValue dv) {
        dicValueDao.saveValue(dv);
    }

    @Override
    public Map<String, List<DicValue>> getAll() {

        Map<String,List<DicValue>> map = new HashMap<>();
        //取得所有的类型
        List<DicType> dtList = dicTypeDao.getTypeList();

        //遍历所有的字典类型
        for (DicType dt : dtList) {
            String code = dt.getCode();
            //通过每一种编码查询相应的List集合
            List<DicValue> dvList = dicValueDao.getValueListByTypeCode(code);
            map.put(code,dvList);
        }
        return map;
    }

    @Override
    public DicValue getValueById(String id) {
        DicValue dv = dicValueDao.getValueById(id);
        return dv;
    }

    @Override
    public void updateValue(DicValue dv) {
        dicValueDao.updateValue(dv);
    }

    @Override
    public void deleteValue(String[] id) {
        dicValueDao.deleteValue(id);
    }

    @Override
    public List<DicType> getTypeList() {
        List<DicType> dtList = dicTypeDao.getTypeList();
        return dtList;
    }
}
