package com.yangchd.data.service.table;

import com.yangchd.data.table.TableConfig;

import java.util.List;

/**
 * @author yangchd  2017/12/18.
 */
public interface ITableConfigService {

    /**
     * 获取所有同步配置
     * @return 数据源列表
     */
    List<TableConfig> queryAll();


    /**
     * 保存同步配置    1 成功  0 失败
     * 如果有主键，则修改；没有主键，新增。
     * @param tableConfig    数据源参数
     * @return              成功数量
     */
    int save(TableConfig tableConfig);

    /**
     * 删除数据源    1 成功  0 失败
     * @param tableConfig    数据源参数
     * @return              成功数量
     */
    int deleteByID(TableConfig tableConfig);


    TableConfig findById(String id);
}
