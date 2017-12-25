package com.yangchd.data.synchronize.impl;

import com.yangchd.data.dao.util.DataBaseTool;
import com.yangchd.data.dao.util.NoCloseDataBase;
import com.yangchd.data.table.TableConfig;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.*;


/**
 * @author yangchd
 * @date 2017/8/28
 * 无中间表同步模式
 */
@Component
public class TableSynNormal {

    /**
     * 先获取数据差异组成sql，然后执行sql
     */
    void tableSynchronize(TableConfig tableConfig, NoCloseDataBase fromdao, NoCloseDataBase todao)
            throws Exception {
        List<Map<String, Object>> sqlList = getSQL(tableConfig, fromdao, todao);

        Date sqlRunBeginTime = new Date();
        if (sqlList != null && sqlList.size() > 0) {
            for (Map<String, Object> sqlMap : sqlList) {
                String sql = sqlMap.get("sql") == null ? "" : sqlMap.get("sql").toString();
                List<Object[]> plist = null;
                if (sqlMap.get("plist") != null) {
                    plist = (List<Object[]>) sqlMap.get("plist");
                }
                if (!"".equals(sql)) {
                    todao.execUpdateBatch(sql, plist);
                }
            }
        }
        Date sqlRunEndTime = new Date();
        tableConfig.setSqlruntime(String.valueOf(sqlRunEndTime.getTime() - sqlRunBeginTime.getTime()) + "ms");
    }

    /**
     * 获取所有更新sql
     */
    private List<Map<String, Object>> getSQL(TableConfig tableConfig, NoCloseDataBase fromdao, NoCloseDataBase todao)
            throws Exception {
        Date sqlQueryBeginTime = new Date();

        //来源表多表联查SQL组装
        String fromtable = tableConfig.getTablefrom();
        JSONArray joins = JSONArray.fromObject(tableConfig.getJointable());
        StringBuffer leftJoin = new StringBuffer();
        for (Object join1 : joins) {
            JSONObject join = JSONObject.fromObject(join1);
            leftJoin.append(" left join ").append(join.getString("name"))
                    .append(" on ").append(join.getString("on"))
                    .append(" = ").append(join.getString("dengyu"));
        }
        fromtable = fromtable + leftJoin.toString();

        //来源表和目标表主键确认
        String[] topk = tableConfig.getTablekey().split(",");
        String[] frompk = new String[topk.length];
        JSONArray columnRelation = JSONArray.fromObject(tableConfig.getRelation());
        for (int i = 0; i < topk.length; i++) {
            for (Object aColumnRelation : columnRelation) {
                JSONObject cjson = JSONObject.fromObject(aColumnRelation);
                if (topk[i].equals(cjson.getString("columnto"))) {
                    frompk[i] = cjson.getString("columnfrom");
                    break;
                }
            }
        }
        //来源表SQL组装
        StringBuffer fromsql = new StringBuffer();
        fromsql.append("select DISTINCT ").append(getColumnSQL(tableConfig, "from", fromdao))
                .append(" from ").append(fromtable);
        if (null != tableConfig.getWherefrom() && !"".equals(tableConfig.getWherefrom())) {
            fromsql.append(" where 1=1 and (").append(tableConfig.getWherefrom()).append(") ");
        }
        fromsql.append(" order by ");
        for (String pkfrom : frompk) {
            fromsql.append(addSymbol(pkfrom, fromdao)).append(" asc,");
        }
        fromsql.deleteCharAt(fromsql.length() - 1);
        //目标表SQL组装
        StringBuffer tosql = new StringBuffer();
        tosql.append(" select DISTINCT ").append(getColumnSQL(tableConfig, "to", todao))
                .append(" from ").append(tableConfig.getTableto());
        if (null != tableConfig.getWhereto() && !"".equals(tableConfig.getWhereto())) {
            tosql.append(" where 1=1 and (").append(tableConfig.getWhereto()).append(") ");
        }
        tosql.append(" order by ");
        for (String pkto : topk) {
            tosql.append(addSymbol(pkto, todao)).append(" asc,");
        }
        tosql.deleteCharAt(tosql.length() - 1);

        //查询出需要同步的数据，然后进入比较
        List<Map<String, Object>> fromList = fromdao.execQuery(fromsql.toString(), null);
        List<Map<String, Object>> toList = todao.execQuery(tosql.toString(), null);
        Date sqlQueryEndTime = new Date();
        //第一部分时间记录，从建立SQL到成功查询出所有数据
        tableConfig.setSqlquerytime(String.valueOf(sqlQueryEndTime.getTime() - sqlQueryBeginTime.getTime()) + "ms");


        //分析比较数据差异，组成执行SQL
        Object[] frompkvalue = new Object[frompk.length];
        Object[] topkvalue = new Object[topk.length];
        List<Map<String, Object>> insert = new ArrayList<>();
        List<Map<String, Object>> delete = new ArrayList<>();
        List<Map<String, Object>> update = new ArrayList<>();

        Date computeBeginTime = new Date();
        String column = getColumnSQL(tableConfig, "all", null);
        for (int i = 0; i < fromList.size(); i++) {
            //先获取主键值
            for (int m = 0; m < frompkvalue.length; m++) {
                frompkvalue[m] = fromList.get(i).get(topk[m]);
            }
            for (int j = 0; j < toList.size(); j++) {
                for (int n = 0; n < topkvalue.length; n++) {
                    topkvalue[n] = toList.get(j).get(topk[n]);
                }
                if (isPkEquals(frompkvalue, topkvalue)) {
                    //找到对应list,判断是否相等
                    if (!isUpdate(fromList.get(i), toList.get(j), column)) {
                        //如果不同
                        update.add(fromList.get(i));
                    }
                    fromList.remove(i--);
                    toList.remove(j--);
                    break;
                }
            }
        }
        insert.addAll(fromList);
        delete.addAll(toList);
        Map<String, Object> insertSQL;
        Map<String, Object> deleteSQL;
        Map<String, Object> updateSQL;
        //在这里进行同步高级选项配置，是否新增、插入、删除
        String[] synValue = new String[]{"insert", "delete", "update"};
        if (tableConfig.getSynvalue().contains(synValue[0])) {
            insertSQL = getInsertSQL(insert, tableConfig, todao);
        } else {
            insertSQL = null;
        }
        if (tableConfig.getSynvalue().contains(synValue[1])) {
            deleteSQL = getDeleteSQL(delete, tableConfig, todao);
        } else {
            deleteSQL = null;
        }
        if (tableConfig.getSynvalue().contains(synValue[2])) {
            updateSQL = getUpdateSQL(update, tableConfig, todao);
        } else {
            updateSQL = null;
        }
        Date computeEndTime = new Date();
        //第二部分时间记录，分析数据时间
        tableConfig.setComputetime(String.valueOf(computeEndTime.getTime() - computeBeginTime.getTime()) + "ms");
        List<Map<String, Object>> sqlList = new ArrayList<>();
        if (insertSQL != null) {
            sqlList.add(insertSQL);
        }
        if (deleteSQL != null) {
            sqlList.add(deleteSQL);
        }
        if (updateSQL != null) {
            sqlList.add(updateSQL);
        }
        return sqlList;
    }


    /**
     * 根据数据库获取sql语句的列名称，防止出现命名问题
     * defaulttype  0 没有默认值
     * defaulttype  1 当值为null时使用默认值
     * defaulttype  2 强制使用默认值
     * type         to 目标表
     * type         from 来源表
     */
    private static String getColumnSQL(TableConfig tableConfig, String type, NoCloseDataBase dao) {
        JSONArray relations = JSONArray.fromObject(tableConfig.getRelation());
        StringBuffer sb = new StringBuffer();
        for (Object relation1 : relations) {
            JSONObject relation = JSONObject.fromObject(relation1);
            if ("0".equals(relation.getString("defaulttype"))) {
                String columnfrom = relation.getString("columnfrom");
                String columnto = relation.getString("columnto");
                if (null != columnfrom && !"".equals(columnfrom)) {
                    if ("from".equals(type)) {
                        //在这里对columnto做一下处理，as后面不允许带表名称
                        sb.append(addSymbol(columnfrom, dao)).append(" as ")
                                .append(addSymbol(columnto.split("\\.")[0], dao)).append(",");
                    } else if ("to".equals(type)) {
                        sb.append(addSymbol(columnto, dao)).append(",");
                    } else {
                        sb.append(columnto).append(",");
                    }
                }
            } else if ("1".equals(relation.getString("defaulttype"))) {

            } else if ("2".equals(relation.getString("defaulttype"))) {
                String columnto = relation.getString("columnto");
                if ("from".equals(type)) {
                    //在这里对columnto做一下处理，as后面不允许带表名称
                    sb.append(relation.getString("defaultvalue")).append(" as ")
                            .append(addSymbol(columnto.split("\\.")[0], dao)).append(",");
                } else if ("to".equals(type)) {
                    sb.append(addSymbol(columnto, dao)).append(",");
                } else {
                    sb.append(columnto).append(",");
                }
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 根据数据库，改变列格式,避免命名冲突问题
     */
    private static String addSymbol(String column, NoCloseDataBase dao) {
        String symbolLeft = "";
        String symbolRight = "";
        if (DataBaseTool.getMysqlDriver().equals(dao.getDriver())) {
            symbolLeft = "`";
            symbolRight = "`";
        }
        if (DataBaseTool.getOracleDriver().equals(dao.getDriver())) {
            symbolLeft = "";
            symbolRight = "";
        }
        if (DataBaseTool.getSqlserverDriverNew().equals(dao.getDriver())
                || DataBaseTool.getSqlserverDriverOld().equals(dao.getDriver())) {
            symbolLeft = "[";
            symbolRight = "]";
        }
        StringBuffer re = new StringBuffer();
        if (null != column) {
            String[] columns = column.split("\\.");
            for (String a : columns) {
                a = symbolLeft + a + symbolRight;
                re.append(a).append(".");
            }
            if (re.length() > 0) {
                re.deleteCharAt(re.length() - 1);
            }
            return re.toString();
        } else {
            return null;
        }
    }

    /**
     * 判断主键是否完全相等
     */
    private static boolean isPkEquals(Object[] str1, Object[] str2) {
        if (str1.length != str2.length) {
            return false;
        }
        boolean flag = true;
        Object a, b;
        for (int i = 0; i < str1.length; i++) {
            a = str1[i];
            b = str2[i];
            if (null != a && null != b) {
                if (!a.toString().trim().equals(b.toString().trim())) {
                    flag = false;
                }
            } else if (a == null && b == null) {
                //相等
            } else {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * 有不同，返回false null也加入判断
     */
    private static boolean isUpdate(Map<String, Object> fromMap, Map<String, Object> toMap, String column) throws Exception {
        boolean flag = true;
        String[] tos = column.split(",");
        Object a, b;
        for (String to : tos) {
            a = fromMap.get(to);
            b = toMap.get(to);
            if (null != a && null != b) {
                if (!a.toString().trim().equals(b.toString().trim())) {
                    flag = false;
                }
            } else if (a == null && b == null) {
                //相等
            } else {
                flag = false;
            }
        }
        return flag;
    }

    private static Map<String, Object> getInsertSQL(List<Map<String, Object>> insert, TableConfig tableConfig, NoCloseDataBase dao) {
        Map<String, Object> rMap = new HashMap<>(4);
        List<Object[]> plist = new ArrayList<>();
        if (insert != null && insert.size() > 0) {
            //先准备sql
            StringBuffer insertSql = new StringBuffer();
            String column = getColumnSQL(tableConfig, "to", dao);
            insertSql.append("insert into ").append(tableConfig.getTableto()).append(" (").append(column).append(") values (");
            for (int i = 0; i < column.split(",").length; i++) {
                insertSql.append("?,");
            }
            insertSql.deleteCharAt(insertSql.length() - 1);
            insertSql.append(")");
            //准备参数

            for (Map<String, Object> anInsert : insert) {
                plist.add(getObjectValue(anInsert, getColumnSQL(tableConfig, "all", null)));
            }
            rMap.put("sql", insertSql.toString());
            rMap.put("plist", plist);
            return rMap;
        } else {
            return null;
        }
    }

    private static Map<String, Object> getDeleteSQL(List<Map<String, Object>> delete, TableConfig tableConfig, NoCloseDataBase dao) {
        Map<String, Object> rMap = new HashMap<>(4);
        String[] topk = tableConfig.getTablekey().split(",");
        if (delete != null && delete.size() > 0) {
            List<Object[]> plist = new ArrayList<>();
            StringBuffer deleteSql = new StringBuffer();
            deleteSql.append(" delete from ").append(tableConfig.getTableto()).append(" where 1=1 ");
            for (String aTopk : topk) {
                deleteSql.append(" and ").append(addSymbol(aTopk, dao)).append("=?,");
            }
            if (deleteSql.length() > 0) {
                deleteSql.deleteCharAt(deleteSql.length() - 1);
            }
            for (Map<String, Object> aDelete : delete) {
                Object[] para = new Object[topk.length];
                for (int i = 0; i < topk.length; i++) {
                    para[i] = aDelete.get(topk[i]).toString();
                }
                plist.add(para);
            }
            rMap.put("sql", deleteSql.toString());
            rMap.put("plist", plist);
            return rMap;
        } else {
            return null;
        }
    }

    private static Map<String, Object> getUpdateSQL(List<Map<String, Object>> update, TableConfig tableConfig, NoCloseDataBase dao) {
        Map<String, Object> rMap = new HashMap<>(4);
        List<Object[]> plist = new ArrayList<>();
        if (update != null && update.size() > 0) {
            StringBuffer updateSql = new StringBuffer();
            String[] columns = getColumnSQL(tableConfig, "all", null).split(",");
            String[] typeColumns = getColumnSQL(tableConfig, "to", dao).split(",");
            String[] topk = tableConfig.getTablekey().split(",");

            updateSql.append(" update ").append(tableConfig.getTableto()).append(" set ");
            for (String typeColumn : typeColumns) {
                updateSql.append(typeColumn).append(" = ?,");
            }
            updateSql.deleteCharAt(updateSql.length() - 1);
            updateSql.append(" where 1=1");
            for (String aTopk : topk) {
                updateSql.append(" and ").append(addSymbol(aTopk, dao)).append(" = ?");
            }

            Object colValue;
            for (Map<String, Object> anUpdate : update) {
                Object[] para = new Object[typeColumns.length + topk.length];
                for (int n = 0; n < typeColumns.length; n++) {
                    colValue = anUpdate.get(columns[n]);
                    para[n] = colValue;
                }
                for (int k = typeColumns.length; k < typeColumns.length + topk.length; k++) {
                    colValue = anUpdate.get(topk[k - typeColumns.length]);
                    para[k] = colValue;
                }
                plist.add(para);
            }
            rMap.put("sql", updateSql.toString());
            rMap.put("plist", plist);
            return rMap;
        } else {
            return null;
        }
    }

    /**
     * 获取插入的参数
     */
    private static Object[] getObjectValue(Map<String, Object> rMap, String column) {
        String[] str = column.split(",");
        Object[] obj = new Object[str.length];
        for (int i = 0; i < str.length; i++) {
            if (str[i].indexOf(".") > 0) {
                str[i] = str[i].substring(str[i].lastIndexOf(".") + 1, str[i].length());
            }
            obj[i] = rMap.get(str[i]);
        }
        return obj;
    }

}
