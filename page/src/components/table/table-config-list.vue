<template>
  <div id="table-config-list">
    <el-col :span="24">
      <el-form :inline="true" :model="tableTopForm" class="demo-form-inline" size="medium">
        <el-form-item>
          <el-input v-model="tableTopForm.inputKey" placeholder="同步模糊搜索"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="">新增</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <!--中部列表-->
    <el-table :data="table" v-loading="tableLoading" border stripe size="small"
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
          <el-button size="mini" @click="">测试</el-button>
          <el-button size="mini" @click="">编辑</el-button>
          <el-button size="mini" type="danger" @click="">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        //上部菜单栏
        tableTopForm: {
          inputKey: '',
        },
        table:[],
        tableLoading:false,
      }
    },
    methods: {
    }
  }
</script>

<style>

</style>

