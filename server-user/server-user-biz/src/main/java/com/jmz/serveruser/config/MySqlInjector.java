package com.jmz.serveruser.config;

import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import java.util.List;
import java.util.Collection;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;

/**
 * 自定义 SQL 注入器
 */
public class MySqlInjector extends DefaultSqlInjector {
    /**
     * 为所有的 Mapper 增加了一个“批量插入部分字段”的自定义方法（InsertBatchSomeColumn 是 MyBatis-Plus 提供的批量插入扩展方法，通常用于高效批量插入数据）。
     * 全局生效
     * @param mapperClass
     * @param tableInfo
     * @return
     */
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        methodList.add(new InsertBatchSomeColumn());
        return methodList;
    }
}