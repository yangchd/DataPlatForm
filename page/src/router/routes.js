import Login from '../pages/Login.vue'
import Home from '../pages/Home.vue'
import ERROR from '../pages/404.vue'

import DataSource from '../components/datasource/datasource-list.vue'
import TableConfig from '../components/table/table-config-list.vue'
import Developing from '../pages/Developing.vue'

// leaf:true    单节点控制
//hidden: true  隐藏控制

let routes = [
  {
    path: '/login',
    component: Login,
    name: '',
    hidden: true
  },{
    path: '/404',
    component: ERROR,
    name: '',
    hidden: true
  },{
    path: '/',
    name: '数据同步',
    iconCls: 'el-icon-refresh',//图标样式class
    component: Home,
    children: [
      {path: '/datasource',name:'数据源管理', component: DataSource},
      {path: '/table',name:'同步信息', component: TableConfig},
      {path: '/query',name:'数据查询', component: Developing},
      {path: '/export',name:'表结构导出', component: Developing},
      {path: '/statistics',name:'同步统计', component: Developing},
    ],
  },{
    path: '/',
    name: '定时任务',
    iconCls: 'el-icon-time',//图标样式class
    component: Home,
    // hidden:true,
    children: [
      {path: '/quartz',name:'任务列表', component: Developing},
    ],
  },{
    path: '/',
    name: '云盘',
    iconCls: 'el-icon-upload',//图标样式class
    component: Home,
    children: [
      {path: '/disk',name:'云盘', component: Developing},
    ],
  },{
    path: '/',
    name: '应用管理',
    iconCls: 'el-icon-mobile-phone',//图标样式class
    component: Home,
    children: [
      {path: '/app',name:'应用列表', component: Developing},
    ],
  },{
    path: '/',
    name: '控制台',
    leaf:true,      //单节点控制
    iconCls: 'el-icon-document',//图标样式class
    component: Home,
    children: [
      {path: '/console',name:'控制台', component: Developing},
    ],
  },
];
export default routes;
