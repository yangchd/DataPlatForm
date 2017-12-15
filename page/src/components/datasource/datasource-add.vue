<template>
  <div id="datasource-add">
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
          <el-select v-model="valueEmpty" placeholder="请选择数据库类型" size="large" @change="changeValue">
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
      <el-button type="primary" @click="saveDataSourceBtn" :loading="addFormLoading">确 定</el-button>
      <el-button @click="addFormVisible = false">取 消</el-button>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        addFormLoading: false,
        is_center: true,
        valueEmpty: '',
        dataSourceForm: {},

      }
    },
    methods: {
      //选择框变化时
      changeValue(value) {
        this.dataSourceForm.url=value;
        console.log(value);
      },
      //保存按钮
      saveDataSourceBtn: function () {
        this.$refs.dataSourceForm.validate((valid) => {
          if (valid) {
            this.$confirm('确认提交吗？', '提示', {}).then(() => {
              this.addFormLoading = true;
              let para = Object.assign({}, this.dataSourceForm);
              para.driver = para.driver.options.value;
//              console.log(para);
              console.log(para.driver);
              saveDataSource(para).then((res) => {
                this.addFormLoading = false;
                if (res.code === '0') {
                  this.$message.success(res.msg);
                  this.$refs['dataSourceForm'].resetFields();
                  this.getDataSourceTableList();
                  this.addFormVisible = false;
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
    }
  }
</script>

<style scoped lang="scss">
  #datasource-add{
  }
</style>

