
import http from "./http";

/**登录**/
export const userLogin = params => {return http.post('/login/user',params).then(res => res);};


/**获取数据源列表**/
export const getDataSourceList = params => {return http.get('/datasource/list', params).then(res => res);};
/**数据源连接测试**/
export const testConnection = params => {return http.post('/datasource/test',params).then(res => res);};
/**保存数据源**/
export const saveDataSource = params => {return http.post('/datasource/save',params).then(res => res);};
/**删除数据源**/
export const deleteDataSource = params => {return http.post('/datasource/delete',params).then(res => res);};

/**获取表配置列表**/
export const getTableList = params => {return http.get('/table/list', params).then(res => res);};
/**获取数据源下所有表**/
export const getTableNameList = params => {return http.get('/table/name', params).then(res => res);};
/**获取表中所有列名称**/
export const getColumnList = params => {return http.get('/table/column', params).then(res => res);};
/**保存表配置**/
export const saveTableConfig = params => {return http.post('/table/save', params).then(res => res);};
/**删除表配置**/
export const deleteTableConfig = params => {return http.post('/table/delete', params).then(res => res);};
/**同步测试**/
export const testTableSyn = params => {return http.post('/table/test', params).then(res => res);};
