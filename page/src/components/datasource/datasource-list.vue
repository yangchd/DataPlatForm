<template>
  <div id="datasource-list">

    <!--上部菜单栏-->
    <el-col :span="24">
      <el-form :inline="true" :model="dataSourceTopForm" class="demo-form-inline" size="medium">
        <el-form-item>
          <el-input v-model="dataSourceTopForm.inputKey" placeholder="数据源模糊搜索"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getDataSourceTableList">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleAdd">新增</el-button>
        </el-form-item>
      </el-form>
    </el-col>

    <!--中部列表-->
    <el-table :data="dataSourceTable" v-loading="dataSourceTableLoading" border stripe size="medium"
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
          <el-button
            size="mini"
            @click="testConnection(scope.$index, scope.row)">测试
          </el-button>
          <el-button
            size="mini"
            @click="handleEdit(scope.$index, scope.row)">编辑
          </el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.$index, scope.row)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--新增界面-->
    <el-dialog title="新增数据源" :visible.sync="addFormVisible" :center="is_center" width="60%"
               :close-on-click-modal="false">
      <el-form ref="dataSourceForm" :model="dataSourceForm" label-width="80px" :rules="dataSourceForm">
        <el-col :span="24" v-if="true">
          <el-form-item label="id">
            <el-input v-model="dataSourceForm.id" placeholder="数据源id"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="名称">
            <el-input v-model="dataSourceForm.name" placeholder="数据源名称"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="驱动">
            <el-select v-model="valueEmpty" placeholder="请选择数据库类型" size="large">
              <el-option
                v-for="item in dataSourceForm.driver"
                :key="item.value"
                :label="item.label"
                :value="item.value">
                <span style="float: left">{{ item.label }}</span>
                <span style="float: right; color: #8492a6; font-size: 13px">{{ item.value }}</span>
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="IP">
            <el-input v-model="dataSourceForm.ip" placeholder="数据库ip地址"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="端口">
            <el-input v-model="dataSourceForm.port" placeholder="数据库端口号"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="库名称">
            <el-input v-model="dataSourceForm.basename" placeholder="数据库库名"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="参数">
            <el-input v-model="dataSourceForm.parameter" placeholder="数据库连接参数"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="连接地址">
            <el-input v-model="dataSourceForm.url" placeholder="实际连接地址" :disabled="true"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="用户名">
            <el-input v-model="dataSourceForm.username" placeholder="用户名"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="密码">
            <el-input v-model="dataSourceForm.password" placeholder="密码"></el-input>
          </el-form-item>
        </el-col>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="saveDataSource" :loading="addFormLoading">确 定</el-button>
        <el-button @click="addFormVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {getDataSourceList, saveDataSource} from "../../api/api";
  export default {
    data() {
      return {
        //上部菜单栏
        dataSourceTopForm: {
          inputKey: '',
        },

        //table相关
        dataSourceTableLoading: false,
        dataSourceTable: [],

        //新增相关
        addFormVisible: false,
        addFormLoading: false,
        is_center: true,
        valueEmpty: '',
        dataSourceForm: {},

      }
    },
    methods: {
      //获取用户列表
      getDataSourceTableList() {
        let para = {
          inputKey: this.inputKey,
        };
        this.dataSourceTableLoading = true;
        getDataSourceList(para).then((res) => {
          this.dataSourceTable = res.data;
          this.dataSourceTableLoading = false;
        });
      },
      testConnection(index, row){
//          console.log(index);
        console.log(row.id);
      },

      //打开新增按钮
      handleAdd: function () {
        this.addFormVisible = true;
        this.value6 = '';
        this.dataSourceForm = {
          id: '',
          name: '1',
          driver: [{
            value: 'com.mysql.jdbc.Driver',
            label: 'MySQL'
          }, {
            value: 'oracle.jdbc.driver.OracleDriver',
            label: 'Oracle'
          }, {
            value: 'com.microsoft.sqlserver.jdbc.SQLServerDriver',
            label: 'SQL Server'
          }, {
            value: 'com.microsoft.jdbc.sqlserver.SQLServerDriver',
            label: 'SQL Server'
          }],
          ip: '2',
          port: '3',
          basename: '4',
          parameter: '5',
          url: '6',
          username: '7',
          password: '8',
        };
      },
      //保存按钮
      saveDataSource: function () {
        this.$refs.dataSourceForm.validate((valid) => {
          if (valid) {
            this.$confirm('确认提交吗？', '提示', {}).then(() => {
              this.addFormLoading = true;
//              let para = Object.assign({}, this.dataSourceForm);
              let para = {
                id: this.dataSourceForm.id,
                name: this.dataSourceForm.name,
                driver: this.dataSourceForm.driver,
                ip: this.dataSourceForm.ip,
                port: this.dataSourceForm.port,
                basename: this.dataSourceForm.basename,
                url: this.dataSourceForm.url,
                username: this.dataSourceForm.username,
                password: this.dataSourceForm.password,
              };
              saveDataSource(para).then((res) => {
                this.addFormLoading = false;
//                this.addFormVisible = false;
                this.$message({
                  message: '提交成功',
                  type: 'success'
                });
                this.$refs['dataSourceForm'].resetFields();
                this.getDataSourceTableList();
              });
            });
          }
        });
      }
    }
  }
</script>


