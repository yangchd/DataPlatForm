package com.yangchd.data.service.datasource.impl;

import com.yangchd.data.dao.DataSourceDao;
import com.yangchd.data.service.datasource.IDataSourceService;
import com.yangchd.data.table.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yangchd  2017/12/12.
 */
@Service("dataSourceService")
public class DataSourceServiceImpl implements IDataSourceService {

    private final DataSourceDao dataSourceDao;

    @Autowired
    public DataSourceServiceImpl(DataSourceDao dataSourceDao) {
        this.dataSourceDao = dataSourceDao;
    }

    @Override
    public List<DataSource> queryAll() {
        return dataSourceDao.findAll();
    }

    @Override
    public int add(DataSource dataSource) {
        return dataSourceDao.save(dataSource)==null?0:1;
    }
}
