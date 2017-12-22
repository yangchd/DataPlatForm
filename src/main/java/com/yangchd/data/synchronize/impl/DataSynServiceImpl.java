package com.yangchd.data.synchronize.impl;

import com.yangchd.data.dao.TableConfigDao;
import com.yangchd.data.synchronize.IDataSynService;
import com.yangchd.data.table.TableConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yangchd  2017/12/21.
 */
@Service("dataSynService")
public class DataSynServiceImpl implements IDataSynService {

    private final TableConfigDao tableConfigDao;

    private final DataSynTool dataSynTool;

    @Autowired
    public DataSynServiceImpl(TableConfigDao tableConfigDao, DataSynTool dataSynTool) {
        this.tableConfigDao = tableConfigDao;
        this.dataSynTool = dataSynTool;
    }

    @Override
    public void startDataSyn(TableConfig tableConfig) throws Exception {

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date begin = new Date();
        String msg = "";

        try {
            dataSynTool.startDataSynByTableConfig(tableConfig);
        } catch (Exception e) {
            msg = "同步失败！" + e.getMessage();
            throw new Exception(e);
        } finally {
            Date end = new Date();
            int timecost = (int)(end.getTime()-begin.getTime());
            tableConfig.setTimecost(String.valueOf(timecost) + "ms");
            tableConfig.setLasttime(sf.format(begin));
            if ("".equals(msg)) {
                tableConfig.setSynmsg("同步成功！");
            }else {
                tableConfig.setSynmsg(msg);
            }
            tableConfigDao.save(tableConfig);
        }
    }
}