package com.yangchd.data.service.table.impl;

import com.yangchd.data.dao.TableConfigDao;
import com.yangchd.data.service.table.ITableConfigService;
import com.yangchd.data.table.TableConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yangchd  2017/12/18.
 */
@Service("tableConfigService")
public class TableConfigServiceImpl implements ITableConfigService {

    private final TableConfigDao tableConfigDao;

    @Autowired
    public TableConfigServiceImpl(TableConfigDao tableConfigDao) {
        this.tableConfigDao = tableConfigDao;
    }

    @Override
    public List<TableConfig> queryAll() {
        return tableConfigDao.findAll();
    }

    @Override
    public int save(TableConfig tableConfig) {
        return tableConfigDao.save(tableConfig)==null?0:1;
    }

    @Override
    public int deleteByID(TableConfig tableConfig) {
        if(null != tableConfig.getId()){
            tableConfigDao.delete(tableConfig);
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public TableConfig findById(String id) {
        return tableConfigDao.findById(id);
    }
}
