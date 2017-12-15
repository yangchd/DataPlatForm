package com.yangchd.data.controller;

import com.yangchd.config.DataPlatformLogger;
import com.yangchd.data.service.datasource.IDataSourceService;
import com.yangchd.data.table.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yangchd  2017/12/12.
 */
@RestController
@RequestMapping("/datasource")
public class DataSourceController {

    private final IDataSourceService dataSourceService;

    @Autowired
    public DataSourceController(IDataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    @RequestMapping(value = "/test")
    public Map<String, Object> testConnection(DataSource dataSource) {
        Map<String, Object> rMap = new HashMap<>(4);
        Boolean flag;
        try {
            flag = dataSourceService.testConnection(dataSource);
            if (flag) {
                rMap.put("code", "0");
                rMap.put("msg", "测试成功");
            } else {
                rMap.put("code", "1");
                rMap.put("msg", "测试失败");
            }
        } catch (Exception e) {
            DataPlatformLogger.error("测试连接请求异常",e);
            rMap.put("code", "1");
            rMap.put("msg", e.getMessage());
        }
        return rMap;
    }

    @RequestMapping(value = "/list")
    public Map<String, Object> getDataSourceList() {
        Map<String, Object> rMap = new HashMap<>(4);
        List<DataSource> list = dataSourceService.queryAll();
        rMap.put("code", "0");
        rMap.put("data", list);
        rMap.put("msg", "查询成功");
        return rMap;
    }

    @RequestMapping(value = "/save")
    public Map<String, Object> saveDataSource(DataSource dataSource) {
        Map<String, Object> rMap = new HashMap<>(4);
        int result = dataSourceService.save(dataSource);
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
    public Map<String, Object> deleteDataSource(DataSource dataSource) {
        Map<String, Object> rMap = new HashMap<>(4);
        int result = dataSourceService.deleteByID(dataSource);
        if (result > 0) {
            rMap.put("code", "0");
            rMap.put("msg", "删除成功");
        } else {
            rMap.put("code", "1");
            rMap.put("msg", "删除失败");
        }
        return rMap;
    }

}
