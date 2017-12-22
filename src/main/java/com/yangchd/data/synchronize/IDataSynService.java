package com.yangchd.data.synchronize;

import com.yangchd.data.table.TableConfig;

/**
 * @author yangchd  2017/12/21.
 */
public interface IDataSynService {
    /**
     * 根据配置开始同步
     */
    void startDataSyn(TableConfig tableConfig) throws Exception;
}
