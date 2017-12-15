import axios from 'axios';

axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
let base = 'http://localhost:9001';


export const requestLogin = params => { return axios.post(`${base}/login`, params).then(res => res.data); };

//获取数据列表请求
export const getDataSourceList = params => {
  return axios.get(`${base}/datasource/list`, {params:params}).then(res => res.data);
};

//测试数据源请求
export const testConnection = params => {
  return axios.get(`${base}/datasource/test`,{params:params}).then(res => res.data);
};

//保存数据源请求
export const saveDataSource = params => {
  return axios.get(`${base}/datasource/save`,{params:params}).then(res => res.data);
};

//删除数据源请求
export const deleteDataSource = params => {
  return axios.get(`${base}/datasource/delete`,{params:params}).then(res => res.data);
};
