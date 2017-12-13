package com.yangchd.data.controller;

import com.yangchd.data.service.datasource.IDataSourceService;
import com.yangchd.data.table.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping(value = "/list")
    public Map<String,Object> getDataSourceList(){
        Map<String,Object> rMap = new HashMap<>(4);
        List<DataSource> list = dataSourceService.queryAll();
        rMap.put("code","0");
        rMap.put("data",list);
        rMap.put("msg","查询成功");
        return rMap;
    }

    @RequestMapping(value = "/add")
    public Map<String,Object> addDataSource(HttpServletRequest request, DataSource dataSource, @RequestBody String id){
        String a = request.getParameter("id");
        Map<String,Object> rMap = new HashMap<>(4);
        int result = dataSourceService.add(dataSource);
        if(result > 0){
            rMap.put("code","0");
            rMap.put("msg","新增成功");
        }else {
            rMap.put("code","1");
            rMap.put("msg","新增失败");
        }
        return rMap;
    }

}
