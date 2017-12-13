<template>
  <div id="datasource-add">
    <el-form ref="form" :model="form" label-width="80px">
      <el-form-item label="名称" >
        <el-input v-model="form.name" placeholder="数据源名称"></el-input>
      </el-form-item>
      <el-form-item label="驱动">
        <el-select v-model="value6" placeholder="请选择数据库类型" >
          <el-option
            v-for="item in form.driver"
            :key="item.value"
            :label="item.label"
            :value="item.value">
            <span style="float: left">{{ item.label }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.value }}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="IP" >
        <el-input v-model="form.ip" placeholder="数据库ip地址"></el-input>
      </el-form-item>
      <el-form-item label="端口" >
        <el-input v-model="form.port" placeholder="数据库端口号"></el-input>
      </el-form-item>
      <el-form-item label="库名称" >
        <el-input v-model="form.basename" placeholder="数据库库名"></el-input>
      </el-form-item>
      <el-form-item label="用户名" >
        <el-input v-model="form.username" placeholder="用户名"></el-input>
      </el-form-item>
      <el-form-item label="密码" >
        <el-input v-model="form.password" placeholder="密码"></el-input>
      </el-form-item>
      <el-button type="primary">确认</el-button>
      <el-button type="primary">取消</el-button>
    </el-form>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        form: {
          name: '',
          driver:[{
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
          ip:'',
          port:'',
          basename:'',
          username:'',
          password:'',
        },
        value6:'',
      }
    },
    methods: {
    }
  }
</script>

<style scoped lang="scss">
  #datasource-add{
  }
</style>

