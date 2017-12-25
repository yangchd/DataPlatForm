'use strict';

import axios from 'axios'
import qs from 'qs'

const baseUrl = 'http://localhost:9001';
const timeout = 10000;

axios.interceptors.request.use(config => {
  return config
}, error => {
  return Promise.reject(error)
});

axios.interceptors.response.use(response => {
  return response
}, error => {
  return Promise.resolve(error.response)
});

function checkStatus (response) {
  // 如果http状态码正常，则直接返回数据
  if (response && (response.status === 200 || response.status === 304 || response.status === 400)) {
    // return response;
    // 如果不需要除了data之外的数据，可以直接 return response.data
    return response.data;
  }
  // 异常状态下，把错误信息返回去
  return {
    code: '404',
    msg: '网络异常'
  }
}

function checkCode (res) {
  // 这里可以截获不同code做处理
  if (res.code === '404') {
    // alert(res.msg);
  }
  //对于后台返回的code，目前不做处理，在每个方法里面单独处理逻辑
  return res
}

export default {
  post (url, data) {
    return axios({
      method: 'post',
      baseURL: baseUrl,
      url,
      data: qs.stringify(data),
      timeout: timeout,
      headers: {
        'X-Requested-With': 'XMLHttpRequest',
        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
      }
    }).then(
      (response) => {
        return checkStatus(response)
      }
    ).then(
      (res) => {
        return checkCode(res)
      }
    )
  },
  get (url, params) {
    return axios({
      method: 'get',
      baseURL: baseUrl,
      url,
      params, // get 请求时带的参数
      timeout: timeout,
      headers: {
        'X-Requested-With': 'XMLHttpRequest'
      }
    }).then(
      (response) => {
        return checkStatus(response)
      }
    ).then(
      (res) => {
        return checkCode(res)
      }
    )
  }
}
