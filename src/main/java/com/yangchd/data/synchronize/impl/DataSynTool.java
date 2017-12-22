package com.yangchd.data.synchronize.impl;

import com.yangchd.data.dao.DataSourceDao;
import com.yangchd.data.dao.util.NoCloseDataBase;
import com.yangchd.data.table.DataSource;
import com.yangchd.data.table.TableConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yangchd
 * @date 2017/8/3
 * 数据同步工具类
 */
@Component
public class DataSynTool {

    private final DataSourceDao dataSourceDao;

    private final TableSynNormal tableSynNormal;

    private static String SYN_TYPE_NORMAL = "normal";
    private static String SYN_TYPE_MIDDLE = "middle";

    @Autowired
    public DataSynTool(DataSourceDao dataSourceDao, TableSynNormal tableSynNormal) {
        this.dataSourceDao = dataSourceDao;
        this.tableSynNormal = tableSynNormal;
    }

    /**
     * 开始进行数据同步，根据数据同步配置信息
     * flag 是否使用同步表
     * by yangchd 2017-08-28
     */
    void startDataSynByTableConfig(TableConfig tableConfig) throws Exception {

        //获取目标库和来源源库的连接
        DataSource datato = dataSourceDao.findById(tableConfig.getDatato());
        DataSource datafrom = dataSourceDao.findById(tableConfig.getDatafrom());
        NoCloseDataBase todao = new NoCloseDataBase(datato);
        NoCloseDataBase fromdao = new NoCloseDataBase(datafrom);

        try {
            //先判断同步模式
            if (null == tableConfig.getSyntype() || "".equals(tableConfig.getSyntype())) {
                tableConfig.setSyntype(SYN_TYPE_NORMAL);
            }
            if (SYN_TYPE_NORMAL.equals(tableConfig.getSyntype())) {
                tableSynNormal.tableSynchronize(tableConfig, fromdao, todao);
            } else if (SYN_TYPE_MIDDLE.equals(tableConfig.getSyntype())) {
                //其他模式暂未开发
                throw new Exception("其他模式暂未开发,请选择普通模式");
            }else {
                throw new Exception("未找到该同步模式，请确认同步配置");
            }
        } finally {
            fromdao.destory();
            todao.destory();
        }
    }
}
