package com.yangchd.data.dao;

import com.yangchd.data.table.DataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * @author yangchd  2017/12/12.
 */
@Repository
public interface DataSourceDao extends JpaRepository<DataSource,Serializable> {

    /**
     * 根据数据源id获取数据源全部信息
     */
    DataSource findById(String id);


    /**
     * 数据源列表模糊查询
     */
    List<DataSource> findByNameContainingOrUrlContainingOrUsernameContaining(String name,String url,String username);

}
