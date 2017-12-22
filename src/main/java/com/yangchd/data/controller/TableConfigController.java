package com.yangchd.data.controller;

import com.yangchd.data.dao.util.DataBaseTool;
import com.yangchd.data.service.datasource.IDataSourceService;
import com.yangchd.data.service.table.ITableConfigService;
import com.yangchd.data.synchronize.IDataSynService;
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

    private final DataBaseTool dataBaseTool;

    private final IDataSourceService dataSourceService;

    private final ITableConfigService tableConfigService;

    private final IDataSynService dataSynService;

    @Autowired
    public TableConfigController(ITableConfigService tableConfigService, IDataSourceService dataSourceService, DataBaseTool dataBaseTool, IDataSynService dataSynService) {
        this.tableConfigService = tableConfigService;
        this.dataSourceService = dataSourceService;
        this.dataBaseTool = dataBaseTool;
        this.dataSynService = dataSynService;
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
        DataSource dataSource = dataSourceService.getDataSourceByID(tableConfig.getDatato());
        try {
            tableConfig.setTablekey(dataBaseTool.getKeyByTableName(dataSource,tableConfig.getTableto()));
            int result = tableConfigService.save(tableConfig);
            if (result > 0) {
                rMap.put("code", "0");
                rMap.put("msg", "保存成功");
            } else {
                rMap.put("code", "1");
                rMap.put("msg", "保存失败");
            }
        } catch (Exception e) {
            rMap.put("code", "1");
            rMap.put("msg", e.getMessage());
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
            nameList = dataBaseTool.getTableNameList(dataSource);
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
    public Map<String, Object> getColumnByTableName(String id,String name,String type) {
        Map<String, Object> rMap = new HashMap<>(4);
        DataSource dataSource = dataSourceService.getDataSourceByID(id);
        List<Map<String, Object>> columnList = new ArrayList<>();
        try {
            String separator = ",";
            for (String tablename:name.split(separator)){
                if(null != tablename && !"".equals(tablename)){
                    columnList.addAll(dataBaseTool.getAllColumnByTableName(dataSource,tablename,type));
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

    /**
     * 测试同步
     */
    @RequestMapping(value = "/test")
    public Map<String, Object> testSynchronize(TableConfig tableConfig) {
        Map<String, Object> rMap = new HashMap<>(4);
        TableConfig config = tableConfigService.findById(tableConfig.getId());
        try {
            dataSynService.startDataSyn(config);
            rMap.put("code", "0");
            rMap.put("msg", "同步成功");
        } catch (Exception e) {
            rMap.put("code", "1");
            rMap.put("msg",e.getMessage());
        }
        return rMap;
    }
}
