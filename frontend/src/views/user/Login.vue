<template>
  <el-container>
    <el-header>
      <el-button
        @click="gotoAdminLogin"
        icon="el-icon-d-arrow-right"
        style="float: right; border: none"
        >管理端登录</el-button
      >
    </el-header>
    <el-main>
      <div class="loginContainer">
        <el-form
          :model="loginForm"
          ref="loginForm"
          :rules="rules"
          label-width="80px"
        >
          <h3 class="loginTitle">XDChat</h3>
          <el-form-item label="用户名" prop="username">
            <el-input
              type="text"
              v-model="loginForm.username"
              auto-complete="off"
              placeholder="请输入用户名"
            ></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input
              type="password"
              v-model="loginForm.password"
              auto-complete="off"
              placeholder="请输入密码"
            ></el-input>
          </el-form-item>
          <div>
            <el-button
              @click="gotoRegister"
              style="width: 45%; margin-right: 10px"
              >注册</el-button
            >
            <el-button
              type="primary"
              style="width: 45%"
              @click="submitLogin"
              v-loading.fullscreen.lock="fullscreenLoading"
              >登录</el-button
            >
          </div>
        </el-form>
      </div>
    </el-main>
  </el-container>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      loginForm: {
        username: "",
        password: "",
      },
      rules: {
        username: [
          { required: true, message: "用户名为空", trigger: "blur" },
          { max: 20, message: "用户名过长", trigger: "blur" },
        ],
        password: [
          { required: true, message: "密码为空", trigger: "blur" },
          { max: 20, message: "密码过长", trigger: "blur" },
        ],
      },
      fullscreenLoading: false,
    };
  },
  methods: {
    submitLogin() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.fullscreenLoading = true;
          this.postKeyValueRequest("/doLogin", this.loginForm).then((resp) => {
            setTimeout(() => {
              this.fullscreenLoading = false;
            }, 1000);
            if (resp) {
              //保存当前用户到vuex
              this.$store.state.currentUser = resp.obj;
              //保存登录用户到窗口的sessionStorage中
              window.sessionStorage.setItem("user", JSON.stringify(resp.obj));
              let path = this.$router.query.redirect;
              this.$router.push(
                path == "/" || path == undefined ? "/chatroom" : path
              );
            }
          });
        } else {
          this.$message.error("用户名和密码都不能为空!");
          return false;
        }
      });
    },
    gotoAdminLogin() {
      this.$router.push("/adminlogin");
    },
    gotoRegister() {
      this.$router.push("/register");
    },
  },
};
</script>

<style>
.disabled .el-upload--picture-card {
  display: none;
}
.loginContainer {
  width: 350px;
  margin: 100px auto;
  border-radius: 15px;
  border: 1px solid #eaeaea;
  /*添加阴影 h-shadow(水平阴影位置)，v-shadow(垂直阴影位置)，blur(阴影大小)，color(颜色)*/
  box-shadow: 10px 10px 35px #cac6c6;
  background: #fff;
  /*background-clip——规定背景的绘制区域*/
  /*border-box：背景被裁剪到边框盒*/
  /*padding-box：背景被裁剪到内边距框*/
  /*content-box：背景被裁剪到内容框*/
  background-clip: padding-box;
  padding: 25px 35px 25px 35px;
}
.loginTitle {
  margin: 10px auto 30px auto;
  text-align: center;
  color: #505458;
}
.loginRemember {
  margin: 5px auto 35px 80px;
}
/*.el-form-item__content{*/
/* display: flex;*/
/*  align-items: center*/
/*}*/
</style>