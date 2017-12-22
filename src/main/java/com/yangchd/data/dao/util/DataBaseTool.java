package com.yangchd.data.dao.util;

import com.yangchd.data.table.DataSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author yangchd
 * @date 2017/8/28
 * 数据更新Dao工具
 * 获取数据库下的表，获取表的主键等
 */
@Component
public class DataBaseTool {

    private static String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    private static String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static String SQLSERVER_DRIVER_OLD = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static String SQLSERVER_DRIVER_NEW = "com.microsoft.jdbc.sqlserver.SQLServerDriver";

    /**
     * 获取数据源下所有表
     */
    public List<Map<String, Object>> getTableNameList(DataSource dataSource) throws Exception {
        List<Map<String, Object>> list;
        OnceDataBase dao = new OnceDataBase(dataSource);
        StringBuilder sb = new StringBuilder();

        if (MYSQL_DRIVER.equals(dataSource.getDriver())) {
            sb.append(" select table_name from information_schema.tables where table_type='base table' ");
            if (dataSource.getBasename() != null && !"".equals(dataSource.getBasename())) {
                sb.append(" and table_schema='").append(dataSource.getBasename()).append("' ");
            }
        } else if (ORACLE_DRIVER.equals(dataSource.getDriver())) {
            sb.append(" select table_name from user_tables");
        } else if (SQLSERVER_DRIVER_OLD.equals(dataSource.getDriver())
                || SQLSERVER_DRIVER_NEW.equals(dataSource.getDriver())) {
            sb.append(" select name as table_name from SysObjects where XType = 'U' ");
        } else {
            throw new Exception("暂时未对该数据库进行适配!");
        }
        sb.append(" order by table_name ");
        list = dao.execQuery(sb.toString(), null);
        return list;
    }

    /**
     * 获取表的主键
     */
    public String getKeyByTableName(DataSource dataSource, String tablename) throws Exception {
        OnceDataBase dao = new OnceDataBase(dataSource);
        List<Map<String, Object>> list;
        String pk;
        StringBuilder sb = new StringBuilder();
        if (MYSQL_DRIVER.equals(dataSource.getDriver())) {
            sb.append(" SELECT " +
                    "  t.TABLE_NAME," +
                    "  t.CONSTRAINT_TYPE," +
                    "  c.COLUMN_NAME," +
                    "  c.ORDINAL_POSITION " +
                    " FROM   INFORMATION_SCHEMA.TABLE_CONSTRAINTS t LEFT JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE c on t.TABLE_NAME = c.TABLE_NAME " +
                    " WHERE " +
                    "  t.CONSTRAINT_TYPE = 'PRIMARY KEY'  " +
                    " and t.table_name = '").append(tablename).append("' ");
            if (dataSource.getBasename() != null && !"".equals(dataSource.getBasename())) {
                sb.append(" and t.table_schema='").append(dataSource.getBasename()).append("' ");
                sb.append(" and c.CONSTRAINT_SCHEMA='").append(dataSource.getBasename()).append("' ");
            }
        } else if (ORACLE_DRIVER.equals(dataSource.getDriver())) {
            sb.append(" select cu.column_name as column_name from user_cons_columns cu, user_constraints au  "
                    + "where cu.constraint_name = au.constraint_name and au.table_name = '")
                    .append(tablename.toUpperCase()).append("' ")
                    .append("and ( au.constraint_type = 'U' or au.constraint_type = 'P' ) ");
        } else if (SQLSERVER_DRIVER_OLD.equals(dataSource.getDriver())
                || SQLSERVER_DRIVER_NEW.equals(dataSource.getDriver())) {
            sb.append(" select table_name,table_schema,column_name " +
                    "from INFORMATION_SCHEMA.KEY_COLUMN_USAGE where table_name = '")
                    .append(tablename).append("' ");
        } else {
            throw new Exception("暂时未对该数据库进行适配");
        }
        list = dao.execQuery(sb.toString(), null);
        if (list != null && list.size() > 0) {
            sb.setLength(0);
            for (Map<String, Object> pkmap : list) {
                sb.append(pkmap.get("column_name").toString());
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        pk = sb.toString();
        return pk;
    }

    /**
     * 根据数据源和表名称获取表的所有列
     */
    public List<Map<String, Object>> getAllColumnByTableName(DataSource dataSource, String tablename, String type) throws Exception {
        OnceDataBase dao = new OnceDataBase(dataSource);
        List<Map<String, Object>> list;
        StringBuilder sb = new StringBuilder();
        if (MYSQL_DRIVER.equals(dataSource.getDriver())) {
            sb.append(" select COLUMN_NAME from information_schema.COLUMNS ");
            sb.append(" where table_name = '").append(tablename).append("' ");
            if (dataSource.getBasename() != null && !"".equals(dataSource.getBasename())) {
                sb.append(" and table_schema='").append(dataSource.getBasename()).append("' ");
            }
        } else if (ORACLE_DRIVER.equals(dataSource.getDriver())) {
            sb.append(" select t.column_name from user_col_comments t where t.table_name = '").append(tablename.toUpperCase()).append("'");
        } else if (SQLSERVER_DRIVER_OLD.equals(dataSource.getDriver())
                || SQLSERVER_DRIVER_NEW.equals(dataSource.getDriver())) {
            sb.append(" select column_name from INFORMATION_SCHEMA.COLUMNS where table_name = '").append(tablename).append("' ");
        } else {
            throw new Exception("暂时未对这种类型的数据库进行适配");
        }
        list = dao.execQuery(sb.toString(), null);
        if (null != list && list.size() > 0) {
            for (Map<String, Object> cMap : list) {
                if (null != cMap.get("column_name")) {
                    if ("from".equals(type)) {
                        cMap.put("column_name", tablename + "." + cMap.get("column_name").toString());
                    } else {
                        cMap.put("column_name", cMap.get("column_name").toString());
                    }
                }
            }
        }
        return list;
    }

    /**
     * 根据数据源的连接地址拼接连接参数
     */
    public String getRealConUrl(DataSource dataSource) throws Exception {
        String realUrl = null;
        if (MYSQL_DRIVER.equals(dataSource.getDriver())) {
            if (null != dataSource.getParameter() && !"".equals(dataSource.getParameter())) {
                realUrl = dataSource.getUrl() + "?" + dataSource.getParameter();
            } else {
                realUrl = dataSource.getUrl();
            }
        } else if (ORACLE_DRIVER.equals(dataSource.getDriver())) {
            //oracle参数拼接
        } else if (SQLSERVER_DRIVER_OLD.equals(dataSource.getDriver())
                || SQLSERVER_DRIVER_NEW.equals(dataSource.getDriver())) {
            //sql server参数拼接
        } else {
            throw new Exception("暂时未对这种类型的数据库进行适配");
        }
        return realUrl;
    }

    public static String getMysqlDriver() {
        return MYSQL_DRIVER;
    }

    public static String getOracleDriver() {
        return ORACLE_DRIVER;
    }

    public static String getSqlserverDriverOld() {
        return SQLSERVER_DRIVER_OLD;
    }

    public static String getSqlserverDriverNew() {
        return SQLSERVER_DRIVER_NEW;
    }
}
