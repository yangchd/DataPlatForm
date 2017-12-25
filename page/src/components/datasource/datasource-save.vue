<template>
  <div id="datasource-save">
    <!--新增dialog-->
    <el-dialog title="数据源" :visible.sync="datasourceDialogVisible" :center="is_center" width="60%"
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
        <el-button type="primary" @click="testConBtn" :loading="testConLoading">测试</el-button>
        <el-button type="primary" @click="saveDataSourceBtn" :loading="saveFormLoading">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {saveDataSource, testConnection} from "../../api/api";
  export default {
    data() {
      return {
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
        datasourceDialogVisible: false,
        testConLoading: false,
        saveFormLoading: false,
        dataSourceForm: {},
        is_center: true,
      }
    },
    methods: {
      /**
       * 新增按钮，弹出新增界面，并初始化
       */
      dataSourceDialog: function (dataSourceForm) {
        this.datasourceDialogVisible = true;
        if (!("undefined" === typeof(dataSourceForm))) {
          this.dataSourceForm = dataSourceForm;
          this.dataSourceForm.driverSelect = this.driverType;
        } else {
          this.dataSourceForm = {
            id: '',
            name: '',
            driver: '',
            driverSelect: this.driverType,
            ip: '',
            port: '',
            basename: '',
            parameter: '',
            url: '',
            username: '',
            password: '',
          };
        }
      },
      /**
       * 新增面板方法
       */
      changeValue: function () {
        //实际连接地址拼接
        if (this.dataSourceForm.driver === "com.mysql.jdbc.Driver") {
          this.dataSourceForm.url = "jdbc:mysql://" + this.dataSourceForm.ip + ":" + this.dataSourceForm.port + "/" + this.dataSourceForm.basename;
        } else if (this.dataSourceForm.driver === "oracle.jdbc.driver.OracleDriver") {
          this.dataSourceForm.url = "jdbc:oracle:thin:@" + this.dataSourceForm.ip + ":" + this.dataSourceForm.port + ":" + this.dataSourceForm.basename;
        } else if (this.dataSourceForm.driver === "com.microsoft.sqlserver.jdbc.SQLServerDriver"
          || this.dataSourceForm.driver === "com.microsoft.jdbc.sqlserver.SQLServerDriver") {
          this.dataSourceForm.url = "jdbc:sqlserver://" + this.dataSourceForm.ip + ":" + this.dataSourceForm.port + ";DatabaseName=" + this.dataSourceForm.basename;
        }
      },
      /**
       * 新增面板测试按钮
       */
      testConBtn(){
        this.changeValue();
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
        });
      },
      /**
       * 保存数据源按钮
       */
      saveDataSourceBtn: function () {
        this.changeValue();
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
                  this.datasourceDialogVisible = false;
                  this.$emit('reloadTable');
                } else {
                  this.$message.error(res.msg);
                }
              });
            }).catch(() => {
              //取消操作
            });
          }
        });
      },
    },
  }
</script>


