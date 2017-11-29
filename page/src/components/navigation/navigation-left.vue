<template>
  <div id="navigation-left">
    <el-col :span="4">
      <el-menu class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose" router="true"
               background-color="#545c64" text-color="#fff" active-text-color="#ffd04b">
        <el-submenu index="1">
          <template slot="title">
            <i class="el-icon-location"></i>
            <span>数据源</span>
          </template>
          <el-menu-item-group>
            <!--<template slot="title">分组一</template>-->
            <el-menu-item index="1-1" route="/utils/datasource/list">数据源列表</el-menu-item>
            <el-menu-item index="1-2" route="/utils/datasource/add">新增数据源</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <el-menu-item index="2" route="/utils/datasource/list">
          <i class="el-icon-menu"></i>
          <span slot="title">同步管理</span>
        </el-menu-item>
        <el-menu-item index="3">
          <i class="el-icon-setting"></i>
          <span slot="title">定时任务</span>
        </el-menu-item>
      </el-menu>
    </el-col>
    <el-col :span="20">
      <router-view></router-view>
    </el-col>
  </div>
</template>

<script>
  export default {
    methods: {
      handleOpen(key, keyPath) {
        console.log(key, keyPath);
      },
      handleClose(key, keyPath) {
        console.log(key, keyPath);
      }
    }
  }
</script>

<style>

</style>

