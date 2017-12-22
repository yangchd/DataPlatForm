import axios from 'axios';

axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
let base = 'http://localhost:9001';

/**
 * 登录请求
 */
export const userLogin = params => {
  return axios.post(`${base}/login/user`, params).then(
    res => res.data
  );
};
// export const userLogin = params => { return axios.get(`${base}/login/user`, {params:params}).then(res => res.data); };

//获取数据列表请求
export const getDataSourceList = params => {return axios.get(`${base}/datasource/list`, {params:params}).then(res => res.data);};

//测试数据源请求
export const testConnection = params => {return axios.get(`${base}/datasource/test`,{params:params}).then(res => res.data);};

//保存数据源请求
export const saveDataSource = params => {return axios.get(`${base}/datasource/save`,{params:params}).then(res => res.data);};

//删除数据源请求
export const deleteDataSource = params => {return axios.get(`${base}/datasource/delete`,{params:params}).then(res => res.data);};

//获取表列表请求
export const getTableList = params => {return axios.get(`${base}/table/list`, {params:params}).then(res => res.data);};

//根据数据源获取表名称
export const getTableNameList = params => {return axios.get(`${base}/table/name`, {params:params}).then(res => res.data);};

//根据数据源获取列名称
export const getColumnList = params => {return axios.get(`${base}/table/column`, {params:params}).then(res => res.data);};

//保存同步配置
export const saveTableConfig = params => {return axios.get(`${base}/table/save`, {params:params}).then(res => res.data);};

//删除同步配置
export const deleteTableConfig = params => {return axios.get(`${base}/table/delete`, {params:params}).then(res => res.data);};

//测试同步
export const testTableSyn = params => {return axios.get(`${base}/table/test`, {params:params}).then(res => res.data);};
