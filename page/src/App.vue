<template>
  <div id="app">
    <navigation-top></navigation-top>
    <!--<el-row>-->
      <!--<el-col :span="4">-->
        <!--<navigation-left></navigation-left>-->
      <!--</el-col>-->
      <!--<el-col :span="20">-->
        <!--&lt;!&ndash;<router-view></router-view>&ndash;&gt;-->
      <!--</el-col>-->
    <!--</el-row>-->
  </div>
</template>

<script>
  import NavigationTop from './components/navigation/navigation-top.vue'
  import NavigationLeft from './components/navigation/navigation-left.vue'

  export default {
    name: 'app',
    components: {  NavigationTop , NavigationLeft }
  }
</script>

<style>

</style>
