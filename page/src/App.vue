<template>
  <div id="app">
    <!--App为index加载的唯一一个页面，这里添加各个组件-->
    <navigation-top></navigation-top>
  </div>
</template>

<script>
  import NavigationTop from './components/navigation/navigation-top.vue'

  export default {
    name: 'app',
    components: {  NavigationTop  }
  }
</script>

<style>

</style>
