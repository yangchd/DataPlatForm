package com.yangchd.data.dao.util;

import com.yangchd.data.table.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yangchd
 * @date 2017/8/28
 * 获取不会自动关闭的数据源连接，用作数据更新时，防止主从改变等意外情况
 */
public class NoCloseDataBase {

    /**
     * 连接对象
     */
    private Connection con = null;
    /**
     * 语句对象
     */
    private PreparedStatement pstmt = null;
    /**
     * 结果集对象
     */
    private ResultSet rs = null;
    /**
     * 数据源
     */
    private String driver = null;

    public NoCloseDataBase(DataSource dataSource) throws Exception {
        this.driver = dataSource.getDriver();
        try {
            Class.forName(dataSource.getDriver());
            if(null != dataSource.getRealurl() && !"".equals(dataSource.getRealurl())){
                this.con = DriverManager.getConnection(dataSource.getRealurl(), dataSource.getUsername(), dataSource.getPassword());
            }else {
                this.con = DriverManager.getConnection(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
            }
        } catch (Exception e) {
            destory();
            throw new Exception(e);
        }
    }
    /**
     * 连接销毁
     */
    public void destory() {
        this.close(rs, pstmt, con);
    }
    /**
     * 关闭 连接对象、语句对象、结果集对象
     */
    private void close(ResultSet rs, PreparedStatement pstmt, Connection con) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                rs = null;
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                pstmt = null;
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                con = null;
            }
        }
    }

    /**
     * 执行更新
     */
    public int execUpdate(String sql, Object[] params) throws Exception {
        // 获得预设语句对象
        this.pstmt = this.con.prepareStatement(sql);
        if (params != null) {
            // 设置参数列表
            for (int i = 0; i < params.length; i++) {
                this.pstmt.setObject(i + 1, params[i]);
            }
        }
        // 执行更新，并返回影响行数
        return this.pstmt.executeUpdate();
    }

    //批量操作，设置自动提交为false
    /**
     * 批量执行操作，设置自动提交为false，执行完成以后提交
     */
    public int execUpdateBatch(String sql, List<Object[]> plist) throws Exception {
        int num = 0;
        // 获得预设语句对象
        this.pstmt = this.con.prepareStatement(sql);
        if (plist != null) {
            try {
                con.setAutoCommit(false);
                for (Object[] params : plist) {
                    // 设置参数列表
                    for (int j = 0; j < params.length; j++) {
                        this.pstmt.setObject(j + 1, params[j]);
                    }
                    this.pstmt.addBatch();
                }
                int[] numbers = this.pstmt.executeBatch();
                for (int number : numbers) {
                    num += number;
                }
                con.commit();
                con.setAutoCommit(true);
            } catch (SQLException e) {
                con.rollback();
                throw new Exception(e);
            }
        } else {
            // 执行更新，并返回影响行数
            num = this.pstmt.executeUpdate();
        }
        return num;
    }
    /**
     * 执行查询
     */
    public List<Map<String, Object>> execQuery(String sql, Object[] params) throws Exception {
        // 获得预设语句对象
        this.pstmt = this.con.prepareStatement(sql);
        if (params != null) {
            // 设置参数列表
            for (int i = 0; i < params.length; i++) {
                this.pstmt.setObject(i + 1, params[i]);
            }
        }
        // 执行查询
        ResultSet rs = pstmt.executeQuery();

        // 获得结果集元数据（元数据就是描述数据的数据，比如把表的列类型列名等作为数据）
        ResultSetMetaData rsmd = rs.getMetaData();

        // 获得列的总数
        int columnCount = rsmd.getColumnCount();

        // 遍历结果集
        List<Map<String, Object>> al = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> rm = new HashMap<>();
            for (int i = 0; i < columnCount; i++) {
                String columnName = rsmd.getColumnLabel(i + 1).toLowerCase();
                Object columnValue = rs.getObject(columnName);
                rm.put(columnName, columnValue);
            }
            // 将每个 hm添加到al中, al相当于是整个表，每个 hm是里面的一条记录
            al.add(rm);
        }
        return al;
    }

    public String getDriver() {
        return driver;
    }
}