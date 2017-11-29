<template>
  <div id="navigation-top">
    <!--上部导航栏，然后通过路由加载各个页面-->
    <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" router="true"
      background-color="#545c64" text-color="#fff" active-text-color="#ffd04b">
      <el-menu-item index="1" route="/carousel-single">主页</el-menu-item>
      <el-submenu index="2">
        <template slot="title">轮播图</template>
        <el-menu-item index="2-1" route="/carousel-single">单张模式</el-menu-item>
        <el-menu-item index="2-2" route="/carousel-multiple">多张模式</el-menu-item>
      </el-submenu>
      <el-menu-item index="3" route="/utils">工具</el-menu-item>
    </el-menu>
    <router-view></router-view>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        activeIndex: '1',
      };
    },
    methods: {
    }
  }
</script>

<style>

</style>

