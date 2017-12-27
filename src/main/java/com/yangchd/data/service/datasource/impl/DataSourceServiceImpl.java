package com.yangchd.data.service.datasource.impl;

import com.yangchd.data.dao.DataSourceDao;
import com.yangchd.data.service.datasource.IDataSourceService;
import com.yangchd.data.table.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
    public List<DataSource> queryByKey(String key) {
        return dataSourceDao.findByNameContainingOrUrlContainingOrUsernameContaining(key,key,key);
    }
    @Override
    public DataSource getDataSourceByID(String id) {
        return dataSourceDao.findById(id);
    }


    @Override
    public int save(DataSource dataSource) {
        return dataSourceDao.save(dataSource)==null?0:1;
    }

    @Override
    public int deleteByID(DataSource dataSource) {
        if(null != dataSource.getId()){
            dataSourceDao.delete(dataSource);
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public Boolean testConnection(DataSource dataSource) throws Exception {
        Boolean flag = false;
        Connection con;
        try {
            Class.forName(dataSource.getDriver());
            if(null != dataSource.getRealurl() && !"".equals(dataSource.getRealurl())){
                con = DriverManager.getConnection(dataSource.getRealurl(), dataSource.getUsername(), dataSource.getPassword());
            }else {
                con = DriverManager.getConnection(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
            }
            if (null!= con && !con.isClosed()) {
                flag = true;
                con.close();
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new Exception(e);
        }
        return flag;
    }

}
