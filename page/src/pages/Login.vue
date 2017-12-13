<template>
    <el-form :model="loginForm" :rules="rules" ref="loginForm"
             label-position="right" label-width="40px" class="demo-ruleForm login-container">
        <h3 class="title">系统登录</h3>
        <el-form-item prop="username" label="账号">
            <el-input type="text" v-model="loginForm.username" auto-complete="off" placeholder="账号"></el-input>
        </el-form-item>
        <el-form-item prop="password" label="密码">
            <el-input type="password" v-model="loginForm.password" auto-complete="off" placeholder="密码"></el-input>
        </el-form-item>
        <el-checkbox v-model="checked" checked class="remember">记住密码</el-checkbox>
        <el-form-item style="width:100%;" label-width="0px">
            <el-button type="primary" style="width:100%;" @click="submitForm('loginForm')" :loading="logining">登录
            </el-button>
        </el-form-item>
    </el-form>
</template>

<script>
    import {requestLogin} from '../api/api';
    export default {
        data() {
            let validateName = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入用户'));
                } else {
                    callback();
                }
            };
            let validatePass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入密码'));
                } else {
                    callback();
                }
            };
            return {
                logining: false,
                loginForm: {
                    username: 'admin',
                    password: '123456'
                },
                rules: {
                    username: [
                        //自定义校验方法
                        {validator: validateName, trigger: 'blur'},
//                        {required: true, message: '请输入账号111', trigger: 'blur'}
                    ],
                    password: [
                        {validator: validatePass, trigger: 'blur'},
                    ]
                },
                checked: true
            };
        },
        methods: {
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.logining = true;
                        let loginParams = {
                            username: this.loginForm.username, password: this.loginForm.password
                        };
                      sessionStorage.setItem('user', JSON.stringify("{'name':'yangchd'}"));
                      this.logining = false;
                      this.$router.push({path: '/'});
//                        requestLogin(loginParams).then(data => {
//                            this.logining = false;
//                            let {msg, code, user} = data;
//                            if (code !== 200) {
//                                this.$message({
//                                    message: msg,
//                                    type: 'error'
//                                });
//                            } else {
//                                sessionStorage.setItem('user', JSON.stringify(user));
//                                this.$router.push({path: '/table'});
//                            }
//                        });
                    } else {
                        return false;
                    }
                });
            }
        }
    }

</script>

<style lang="scss" scoped>
    .login-container {
        /*box-shadow: 0 0px 8px 0 rgba(0, 0, 0, 0.06), 0 1px 0px 0 rgba(0, 0, 0, 0.02);*/
        -webkit-border-radius: 5px;
        border-radius: 5px;
        -moz-border-radius: 5px;
        background-clip: padding-box;
        margin: 180px auto;
        width: 350px;
        padding: 35px 35px 15px 35px;
        background: #fff;
        border: 1px solid #eaeaea;
        box-shadow: 0 0 25px #cac6c6;
        .title {
            margin: 0 auto 40px auto;
            text-align: center;
            color: #505458;
        }
        .remember {
            margin: 0 0 25px 0;
            float: right;
        }
        .forgetPass {
            margin: 0;
        }
    }
</style>
