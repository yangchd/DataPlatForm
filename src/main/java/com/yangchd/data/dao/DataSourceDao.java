package com.yangchd.data.dao;

import com.yangchd.data.table.DataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * @author yangchd  2017/12/12.
 */
@Repository
public interface DataSourceDao extends JpaRepository<DataSource,Serializable> {

}
