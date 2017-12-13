package com.yangchd.data.service.datasource;

import com.yangchd.data.table.DataSource;

import java.util.List;

/**
 * @author yangchd  2017/12/12.
 */
public interface IDataSourceService {

    /**
     * 获取所有数据源列表
     * @return 数据源列表
     */
    List<DataSource> queryAll();

    /**
     * 新增数据源
     * @param dataSource    数据源参数
     * @return              成功数量
     */
    int add(DataSource dataSource);
}
