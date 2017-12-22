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
     * 根据关键字查询数据源
     * @return 数据源列表
     */
    List<DataSource> queryByKey(String key);

    /**
     * 根据数据源id获取数据源
     * @param id    数据源id
     * @return      数据源
     */
    DataSource getDataSourceByID(String id);

    /**
     * 保存数据源    1 成功  0 失败
     * 如果有主键，则修改；没有主键，新增。
     * @param dataSource    数据源参数
     * @return              成功数量
     */
    int save(DataSource dataSource);

    /**
     * 删除数据源    1 成功  0 失败
     * spring data jpa根据主键删除
     * @param dataSource    数据源参数
     * @return              成功数量
     */
    int deleteByID(DataSource dataSource);

    /**
     * 数据源连接测试
     * @param dataSource    数据源
     * @return              连接成功或者失败
     */
    Boolean testConnection(DataSource dataSource) throws Exception;

}
