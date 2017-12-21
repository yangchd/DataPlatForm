<template>
  <div id="datasource-list">

    <!--上部菜单栏-->
    <el-col :span="24">
      <el-form :inline="true" :model="dataSourceTopForm" class="demo-form-inline" size="medium">
        <el-form-item>
          <el-input v-model="dataSourceTopForm.inputKey" placeholder="数据源模糊搜索"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getDataSourceListBtn">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleAddBtn">新增</el-button>
        </el-form-item>
      </el-form>
    </el-col>

    <!--中部列表-->
    <el-table :data="dataSourceTable" v-loading="dataSourceTableLoading" border stripe size="small"
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
    <el-dialog title="新增数据源" :visible.sync="addFormVisible" :center="is_center" width="60%"
               :close-on-click-modal="false">
      <el-form ref="dataSourceForm" :model="dataSourceForm" label-width="80px" :rules="dataSourceForm">
        <el-col :span="24" v-if="false">
          <el-form-item label="id">
            <el-input v-model="dataSourceForm.id" placeholder="数据源id"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="名称">
            <el-input v-model="dataSourceForm.name" placeholder="数据源别名"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="驱动">
            <el-select v-model="dataSourceForm.driver" placeholder="请选择数据库类型" size="large" @change="changeValue">
              <el-option
                v-for="item in dataSourceForm.driverSelect"
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
            <el-input v-model="dataSourceForm.parameter" placeholder="数据库连接参数&分割"></el-input>
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
        <el-col :span="24">
          <el-form-item label="连接地址">
            <el-input v-model="dataSourceForm.url" placeholder="实际连接地址" :disabled="true"></el-input>
          </el-form-item>
        </el-col>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <!--<el-button @click="addFormVisible = false">取 消</el-button>-->
        <el-button type="primary" @click="testConBtn" :loading="testConLoading">测试</el-button>
        <el-button type="primary" @click="saveDataSourceBtn" :loading="saveFormLoading">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {deleteDataSource, getDataSourceList, saveDataSource, testConnection} from "../../api/api";
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
        driverType: [{
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
        addFormVisible: false,
        testConLoading: false,
        saveFormLoading: false,
        is_center: true,
        dataSourceForm: {},
      }
    },
    methods: {
      //获取用户列表
      getDataSourceTableList: function () {
        let para = {
          inputKey: this.inputKey,
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
       * 新增按钮初始化
       */
      handleAddBtn: function () {
        this.addFormVisible = true;
        this.dataSourceForm = {
          id: '',
          name: '数据库测试',
          driver: '',
          driverSelect: this.driverType,
          ip: 'localhost',
          port: '3306',
          basename: 'idea',
          parameter: '',
          url: '',
          username: 'ycd',
          password: '111111',
        };
      },

      /**
       * 新增面板方法
       */
      changeValue(value) {
        //实际数据源地址
        this.dataSourceForm.driver = value;
        //实际连接地址拼接
        let prefixUrl = "";
        if (this.dataSourceForm.driver === "com.mysql.jdbc.Driver") {
          prefixUrl = "jdbc:mysql://" + this.dataSourceForm.ip + ":" + this.dataSourceForm.port + "/" + this.dataSourceForm.basename;
        } else if (this.dataSourceForm.driver === "oracle.jdbc.driver.OracleDriver") {
          prefixUrl = "jdbc:oracle:thin:@" + this.dataSourceForm.ip + ":" + this.dataSourceForm.port + ":" + this.dataSourceForm.basename;
        } else if (this.dataSourceForm.driver === "com.microsoft.sqlserver.jdbc.SQLServerDriver"
          || driver === "com.microsoft.jdbc.sqlserver.SQLServerDriver") {
          prefixUrl = "jdbc:sqlserver://" + this.dataSourceForm.ip + ":" + this.dataSourceForm.port + ";DatabaseName=" + this.dataSourceForm.basename;
        }
        this.dataSourceForm.url = prefixUrl;
      },
      /**
       * 新增面板测试按钮
       */
      testConBtn(){
        this.testConLoading = true;
        let para = Object.assign({}, this.dataSourceForm);
        para.driverSelect = para.driver;
        testConnection(para).then((res) => {
          this.testConLoading = false;
          if (res.code === '0') {
            this.$message.success(res.msg);
          } else {
            this.$message.error(res.msg);
          }
        }).catch(() => {
          this.testConLoading = false;
        });
      },
      /**
       * 保存数据源按钮
       */
      saveDataSourceBtn: function () {
        this.$refs.dataSourceForm.validate((valid) => {
          if (valid) {
            this.$confirm('确认提交吗？', '提示', {}).then(() => {
              this.saveFormLoading = true;
              let para = Object.assign({}, this.dataSourceForm);
              para.driverSelect = para.driver;
              saveDataSource(para).then((res) => {
                this.saveFormLoading = false;
                if (res.code === '0') {
                  this.$message.success(res.msg);
                  this.getDataSourceTableList();
                  this.addFormVisible = false;
                } else {
                  this.$message.error(res.msg);
                }
              }).catch(() => {
                this.saveFormLoading = false;
              });
            }).catch(() => {
              //取消操作
            });
          }
        });
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
        this.addFormVisible = true;
        this.dataSourceForm = row;
        this.dataSourceForm.driverSelect = this.driverType;
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

    }
  }
</script>


