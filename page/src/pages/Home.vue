<template>
  <el-row class="container">

    <!--上部logo和个人信息条-->
    <el-col :span="24" class="header">
      <el-col :span="10" class="logo">
        Logo
      </el-col>
      <el-col :span="14" class="user-info">
        <el-dropdown trigger="hover">
          <span class="el-dropdown-link user-info-inner">
            <img :src="this.sysUserAvatar"/> {{sysUserName}}
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>个人</el-dropdown-item>
            <el-dropdown-item>设置</el-dropdown-item>
            <el-dropdown-item divided @click.native="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-col>
    </el-col>

    <!--控制台主题，分左侧导航栏和右侧功能区-->
    <el-col :span="24" class="main">
      <el-col :span="10" class="left">
        <el-menu :default-active="$route.path" class="el-menu-vertical-demo"
                 @open="handleOpen" @close="handleClose" router unique-opened
                 background-color="#545c64" text-color="#fff" active-text-color="#ffd04b">
          <template v-for="(item ,index) in $router.options.routes" v-if="!item.hidden">
            <el-submenu :index="index+''" v-if="!item.leaf">
              <template slot="title"><i :class="item.iconCls"></i>{{item.name}}</template>
              <el-menu-item v-for="child in item.children" :index="child.path" :key="child.path" :route="child.path"
                            v-if="!child.hidden">
                {{child.name}}
              </el-menu-item>
            </el-submenu>
            <el-menu-item v-if="item.leaf&&item.children.length>0" :index="item.children[0].path"
                          :route="item.children[0].path">
              <i :class="item.iconCls"></i>{{item.children[0].name}}
            </el-menu-item>
          </template>
        </el-menu>
      </el-col>
      <el-col :span="14" class="right">
        <el-col :span="24" class="breadcrumb-container">
          <strong class="title">{{$route.name}}</strong>
        </el-col>

        <el-col :span="24" class="content-wrapper">
          <transition name="fade" mode="out-in">
            <router-view></router-view>
          </transition>
        </el-col>
      </el-col>
    </el-col>
  </el-row>
</template>

<script>
  import logoSrc from './../assets/yangchd.png';
  export default {
    data() {
      return {
        sysUserName: '',
        sysUserAvatar: '',
        DataSourceListForm: {
          inputKey: '',
        }
      }
    },
    methods: {
      handleOpen(key, keyPath) {
        console.log(key, keyPath);
      },
      handleClose(key, keyPath) {
        console.log(key, keyPath);
      },
      logout: function () {
        let _this = this;
        this.$confirm('确认退出吗?', '提示', {
          //type: 'warning'
        }).then(() => {
          sessionStorage.removeItem('user');
          _this.$router.push('/login');
        }).catch(() => {
        });
      }
    },
    mounted() {
      let user = sessionStorage.getItem('user');
      if (user) {
        user = JSON.parse(user);
        this.sysUserName = user.name || '杨昌迪';
        this.sysUserAvatar = user.avatar || logoSrc;
      }
    },
  }

</script>

<style scoped lang="scss">

  .container {
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    width: 100%;
    .header {
      height: 60px;
      line-height: 60px;
      background: #545c64;
      .user-info {
        text-align: right;
        padding-right: 35px;
        float: right;
        .user-info-inner {
          cursor: pointer;
          color: #fff;
          img {
            width: 40px;
            height: 40px;
            border-radius: 20px;
            margin: 10px 0 10px 10px;
            float: right;
          }
        }
      }
      .logo {
        width: 230px;
        height: 60px;
        font-size: 22px;
        color: #fff;
        text-align: center;
        padding-left: 20px;
        padding-right: 20px;
        img {
          width: 40px;
          float: left;
          margin: 10px 10px 10px 18px;
        }
        .txt {
          color: #fff;
        }
      }
    }
  }

  .main {
    display: flex;
    position: absolute;
    top: 60px;
    bottom: 0;
    overflow: hidden;
    .left {
      flex: 0 0 230px;
      width: 230px;
      .el-menu {
        height: 100%;
      }
    }
    .right {
      flex: 1;
      padding: 20px;
      /*overflow-y: scroll;*/
      .breadcrumb-container {
        margin-bottom: 20px;
        .title {
          width: 200px;
          float: left;
          color: #000;
        }
      }
      .content-wrapper {
        background-color: #fff;
        box-sizing: border-box;
      }
    }
  }
</style>
