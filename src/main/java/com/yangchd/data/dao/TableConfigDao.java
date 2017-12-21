package com.yangchd.data.dao;

import com.yangchd.data.table.TableConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * @author yangchd  2017/12/18.
 */
@Repository
public interface TableConfigDao extends JpaRepository<TableConfig,Serializable> {

}