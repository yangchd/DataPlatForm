import axios from 'axios';

axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
let base = 'http://localhost:9001';

export const requestLogin = params => { return axios.post(`${base}/login`, params).then(res => res.data); };

export const getDataSourceList = params => { return axios.post(`${base}/datasource/list`, params).then(res => res.data); };

// export const saveDataSource = params => {
//   console.log(params);
//   params.driver=1;
//   return axios.get(`${base}/datasource/add`,{params:params}).then(res => res.data);
// };
export const saveDataSource = params => {
  console.log(params);
  params.driver=1;
  return axios.post(`${base}/datasource/add`,params).then(res => res.data);
};
