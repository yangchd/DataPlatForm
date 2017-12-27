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
 * 手动连接数据库
 */
public class OnceDataBase {
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
    private DataSource dataSource = null;

    public OnceDataBase(DataSource dataSource){
        this.dataSource = dataSource;
    }
    /**
     * 获得连接对象
     */
    private Connection getConnection(){
        try{
            Class.forName(dataSource.getDriver());
            if(null != dataSource.getRealurl() && !"".equals(dataSource.getRealurl())){
                this.con = DriverManager.getConnection(dataSource.getRealurl(), dataSource.getUsername(), dataSource.getPassword());
            }else {
                this.con = DriverManager.getConnection(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
            }
        }catch(Exception e){
            if(con != null) {
                try {
                    con.close();
                } catch (SQLException e1) {
                    con = null;
                }
            }
        }
        return con;
    }

    /**
     * 关闭 连接对象、语句对象、结果集对象
     */
    private void close(ResultSet rs, PreparedStatement pstmt, Connection con) {
        if(rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                rs = null;
            }
        }
        if(pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                pstmt = null;
            }
        }
        if(con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                con=null;
            }
        }
    }



    /**
     * 执行查询
     */
    public List<Map<String, Object>> execQuery(String sql, Object[] params) throws Exception {
        try {
            this.getConnection();
            // 获得预设语句对象
            this.pstmt = this.con.prepareStatement(sql);
            if (params != null) {
                // 设置参数列表
                for (int i = 0; i < params.length; i++) {
                    // 因为问号参数的索引是从1开始，所以是i+1
                    this.pstmt.setObject(i + 1, params[i]);
                }
            }
            // 执行查询
            ResultSet rs = pstmt.executeQuery();

            List<Map<String, Object>> al = new ArrayList<Map<String, Object>>();

            // 获得结果集元数据（元数据就是描述数据的数据，比如把表的列类型列名等作为数据）
            ResultSetMetaData rsmd = rs.getMetaData();

            // 获得列的总数
            int columnCount = rsmd.getColumnCount();

            // 遍历结果集
            while (rs.next()) {
                Map<String, Object> hm = new HashMap<>();
                for (int i = 0; i < columnCount; i++) {
                    String columnName = rsmd.getColumnName(i + 1).toLowerCase();
                    Object columnValue = rs.getObject(columnName);
                    hm.put(columnName, columnValue);
                }
                // 将每个 hm添加到al中, al相当于是整个表，每个 hm是里面的一条记录
                al.add(hm);
            }
            return al;
        } finally {
            this.close(this.rs, this.pstmt, this.con);
        }
    }
}