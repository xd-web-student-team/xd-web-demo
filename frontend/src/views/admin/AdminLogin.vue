<template>
  <el-container>
    <el-header>
      <el-button
        @click="gotoClientLogin"
        icon="el-icon-d-arrow-left"
        style="border: none"
        >客户端登录</el-button
      >
    </el-header>
    <el-main>
      <div class="loginContainer">
        <el-form
          ref="loginForm"
          :rules="rules"
          :model="loginForm"
          label-width="80px"
        >
          <h3 class="loginTitle">管理端登录</h3>
          <el-form-item label="用户名:" prop="username">
            <el-input
              type="text"
              v-model="loginForm.username"
              auto-complete="off"
              placeholder="请输入用户名"
            ></el-input>
          </el-form-item>
          <el-form-item label="密码:" prop="password">
            <el-input
              type="password"
              v-model="loginForm.password"
              auto-complete="off"
              placeholder="请输入密码"
            ></el-input>
          </el-form-item>
          <el-button
            type="primary"
            style="width: 100%"
            @click="submitLogin"
            v-loading.fullscreen.lock="fullscreenLoading"
            >登录</el-button
          >
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
      checked: true,
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
        ],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
      },
      fullscreenLoading: false,
    };
  },
  methods: {
    submitLogin() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.fullscreenLoading = true;
          this.postKeyValueRequest("/admin/doLogin", this.loginForm).then(
            (resp) => {
              setTimeout(() => {
                this.fullscreenLoading = false;
              }, 1000);
              if (resp) {
                //alert("登录成功！");
                //保存登录用户到session中
                window.sessionStorage.setItem(
                  "admin",
                  JSON.stringify(resp.obj)
                );
                // this.$router.replace("/home");
              }
            }
          );
        } else {
          this.$message.error("用户名，密码不能为空！");
          return false;
        }
      });
    },
    gotoClientLogin() {
      this.$router.replace("/");
    },
  },
};
</script>

<style>
</style>
