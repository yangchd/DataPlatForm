<template>
  <div id="datasource-list">

    <!--上部菜单栏-->
    <el-col :span="24">
      <el-form :inline="true" :model="dataSourceTopForm" class="demo-form-inline" size="medium">
        <el-form-item>
          <el-input v-model="dataSourceTopForm.inputKey" placeholder="数据源模糊搜索" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getDataSourceListBtn">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="dataSourceAddOpenBtn">新增</el-button>
        </el-form-item>
      </el-form>
    </el-col>

    <!--中部列表-->
    <el-table :data="dataSourceTable" v-loading="dataSourceTableLoading" max-height="300" border stripe size="small"
              style="width: 100%;">
      <el-table-column prop="id" v-if="false">
      </el-table-column>
      <el-table-column prop="name" label="数据源名称" width="180">
      </el-table-column>
      <el-table-column prop="url" label="数据源地址">
      </el-table-column>
      <el-table-column prop="username" label="用户" width="180">
      </el-table-column>
      <el-table-column label="操作" width="300px">
        <template slot-scope="scope">
          <el-button size="mini" @click="testDataSourceBtn(scope.$index, scope.row)">测试</el-button>
          <el-button size="mini" @click="editDataSourceBtn(scope.$index, scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="deleteDataSourceBtn(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--新增界面-->
    <datasource-save ref="dialog" v-on:reloadTable="getDataSourceTableList"></datasource-save>
  </div>
</template>

<script>
  import DatasourceSave from './datasource-save.vue'
  import {deleteDataSource, getDataSourceList, testConnection} from "../../api/api";
  export default {
    components: {
      DatasourceSave
    },
    data() {
      return {
        //上部菜单栏
        dataSourceTopForm: {
          inputKey: '',
        },

        //table相关
        dataSourceTableLoading: false,
        dataSourceTable: [],
      }
    },
    methods: {
      /**
       * 获取数据源列表，并在页面table中显示
       */
      getDataSourceTableList: function () {
        let para = {
          key: this.dataSourceTopForm.inputKey,
        };
        this.dataSourceTableLoading = true;
        getDataSourceList(para).then((res) => {
          this.dataSourceTable = res.data;
          this.dataSourceTableLoading = false;
        }).catch((res) => {
          this.dataSourceTableLoading = false;
          this.$message.error(res);
        });
      },
      //查询按钮
      getDataSourceListBtn: function () {
        this.getDataSourceTableList();
      },
      /**
       * 新增按钮，弹出新增界面，并初始化
       */
      dataSourceAddOpenBtn: function () {
        this.$refs.dialog.dataSourceDialog();
      },
      /**
       * Table列表测试连接
       */
      testDataSourceBtn: function (index, row) {
        let para = Object.assign({}, row);
        testConnection(para).then((res) => {
          if (res.code === '0') {
            this.$message.success(res.msg);
          } else {
            this.$message.error(res.msg);
          }
        });
      },
      /**
       * Table列表编辑按钮
       */
      editDataSourceBtn: function (index, row) {
        //新建值，不直接传入地址
        let para = Object.assign({}, row);
        this.$refs.dialog.dataSourceDialog(para);
      },
      /**
       * Table列表删除功能
       */
      deleteDataSourceBtn: function (index, row) {
        this.$confirm('确认删除吗？', '提示', {}).then(() => {
          let para = Object.assign({}, row);
          deleteDataSource(para).then((res) => {
            if (res.code === '0') {
              this.$message.success(res.msg);
              this.getDataSourceTableList();
            } else {
              this.$message.error(res.msg);
            }
          });
        }).catch(() => {
          //取消操作
        });
      }
    },
    mounted(){
      this.getDataSourceTableList();
    },
  }
</script>


