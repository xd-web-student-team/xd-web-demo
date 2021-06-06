<template>
  <el-container>
    <!-- <el-header>
      <el-button
        @click="gotoLogin"
        icon="el-icon-d-arrow-left"
        style="border: none"
        >客户端登录</el-button
      >
    </el-header>
    <el-main>
      <div class="loginContainer">
        <el-form
          :model="registerForm"
          status-icon
          :rules="rules"
          ref="registerForm"
        >
          <h3 class="loginTitle">XDChat-用户注册</h3>
          <el-form-item label="登录用户名：" prop="username">
            <el-input
              type="text"
              v-model="registerForm.username"
              autocomplete="off"
              placeholder="请输入登录用户名"
            ></el-input>
          </el-form-item>
          <el-form-item label="用户昵称：" prop="nickname">
            <el-input
              type="text"
              v-model="registerForm.nickname"
              autocomplete="off"
              placeholder="请输入昵称"
            ></el-input>
          </el-form-item>
          <el-form-item label="密码：" prop="password">
            <el-input
              type="password"
              v-model="registerForm.password"
              autocomplete="off"
              placeholder="请输入密码"
            ></el-input>
          </el-form-item>
          <el-form-item label="确认密码：" prop="checkPass">
            <el-input
              type="password"
              v-model="registerForm.checkPass"
              autocomplete="off"
              placeholder="请再次输入密码"
            ></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitRegister" style="width: 100%"
            >注册</el-button
          >
        </div>
      </div>
    </el-main> -->
  </el-container>
</template>

<script>
export default {
  name: "Register",
  data() {
    var validateUsername = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入用户名"));
      }
      //检查用户名是否重复
      this.getRequest("/user/checkUsername?username=" + value).then((resp) => {
        if (resp != 0) {
          callback(new Error("该用户名已被注册"));
        } else {
          callback();
        }
      });
    };
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (this.registerForm.checkPass !== "") {
          this.$refs.registerForm.validateField("checkPass");
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.registerForm.password) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    return {
      fullscreenLoading: false,
      //注册表单相关
      formLabelWidth: "120px",
      registerForm: {
        username: "",
        nickname: "",
        password: "",
        checkPass: "",
        userProfile: "",
      },
      formLabelWidth: "120px",
      rules: {
        username: [
          { validator: validateUsername, trigger: "blur" },
          { required: true, max: 20, message: "用户名过长", trigger: "blur" },
        ],
        nickname: [
          { required: true, message: "昵称为空", trigger: "blur" },
          { max: 20, message: "昵称过长", trigger: "blur" },
        ],
        password: [
          { validator: validatePass, trigger: "blur" },
          { required: true, max: 20, message: "密码过长", trigger: "blur" },
        ],
        checkPass: [
          { required: true, validator: validatePass2, trigger: "blur" },
        ],
      },
      uploadDisabled: false,
      //上传的文件信息列表
      fileList: [],
    };
  },
  methods: {
    gotoLogin() {
      this.$router.replace("/");
    },
    //提交注册操作
    submitRegister() {
      this.$refs.registerForm.validate((valid) => {
        if (valid) {
          this.fullscreenLoading = true;
          this.postRequest("/user/register", this.registerForm).then((resp) => {
            setTimeout(() => {
              this.fullscreenLoading = false;
            }, 1000);
            if (resp) {
              this.$router.push("/");
            }
          });
        } else {
          this.$message.error("请正确填写信息！");
          console.log("error submit!!");
          return false;
        }
      });
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
</style>