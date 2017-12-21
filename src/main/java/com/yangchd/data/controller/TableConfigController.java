package com.yangchd.data.controller;

import com.yangchd.data.dao.util.DaoTool;
import com.yangchd.data.service.datasource.IDataSourceService;
import com.yangchd.data.service.table.ITableConfigService;
import com.yangchd.data.table.DataSource;
import com.yangchd.data.table.TableConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yangchd  2017/12/18.
 */
@RestController
@RequestMapping("/table")
public class TableConfigController {

    private final DaoTool daoTool;

    private final IDataSourceService dataSourceService;

    private final ITableConfigService tableConfigService;

    @Autowired
    public TableConfigController(ITableConfigService tableConfigService, IDataSourceService dataSourceService, DaoTool daoTool) {
        this.tableConfigService = tableConfigService;
        this.dataSourceService = dataSourceService;
        this.daoTool = daoTool;
    }

    @RequestMapping(value = "/list")
    public Map<String, Object> getDataSourceList() {
        Map<String, Object> rMap = new HashMap<>(4);
        List<TableConfig> list = tableConfigService.queryAll();
        rMap.put("code", "0");
        rMap.put("data", list);
        rMap.put("msg", "查询成功");
        return rMap;
    }

    @RequestMapping(value = "/save")
    public Map<String, Object> saveDataSource(TableConfig tableConfig) {
        Map<String, Object> rMap = new HashMap<>(4);
        int result = tableConfigService.save(tableConfig);
        if (result > 0) {
            rMap.put("code", "0");
            rMap.put("msg", "保存成功");
        } else {
            rMap.put("code", "1");
            rMap.put("msg", "保存失败");
        }
        return rMap;
    }

    @RequestMapping(value = "/delete")
    public Map<String, Object> deleteDataSource(TableConfig tableConfig) {
        Map<String, Object> rMap = new HashMap<>(4);
        int result = tableConfigService.deleteByID(tableConfig);
        if (result > 0) {
            rMap.put("code", "0");
            rMap.put("msg", "删除成功");
        } else {
            rMap.put("code", "1");
            rMap.put("msg", "删除失败");
        }
        return rMap;
    }

    /**
     * 根据数据源id获取当前库中所有的表
     */
    @RequestMapping(value = "/name")
    public Map<String, Object> getTableNamesByDataSourceID(String id) {
        Map<String, Object> rMap = new HashMap<>(4);
        DataSource dataSource = dataSourceService.getDataSourceByID(id);
        List<Map<String, Object>> nameList;
        try {
            nameList = daoTool.getTableNameList(dataSource);
            rMap.put("code", "0");
            rMap.put("msg", "查询成功");
            rMap.put("data", nameList);
        } catch (Exception e) {
            rMap.put("code", "1");
            rMap.put("msg", e.getMessage());
        }
        return rMap;
    }

    /**
     * 根据数据源和表名称获取所有列
     */
    @RequestMapping(value = "/column")
    public Map<String, Object> getColumnByTableName(String id,String name) {
        Map<String, Object> rMap = new HashMap<>(4);
        DataSource dataSource = dataSourceService.getDataSourceByID(id);
        List<Map<String, Object>> columnList = new ArrayList<>();
        try {
            String separator = ",";
            for (String tablename:name.split(separator)){
                if(null != tablename && !"".equals(tablename)){
                    columnList.addAll(daoTool.getAllColumnByTableName(dataSource,tablename));
                }
            }
            rMap.put("code", "0");
            rMap.put("msg", "查询成功");
            rMap.put("data", columnList);
        } catch (Exception e) {
            rMap.put("code", "1");
            rMap.put("msg", e.getMessage());
        }
        return rMap;
    }
}
