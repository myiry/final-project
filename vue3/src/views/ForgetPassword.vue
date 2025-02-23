<template>
  <div>
    <div class="login-container">
      <div class="login-box" style="width: 350px">
        <div class="login">忘记密码</div>
        <el-form :model="data.form" ref="formRef" :rules="rules" style="margin-top: 20px;">
          <el-form-item prop="username">
            <el-input prefix-icon="User" v-model="data.form.username" placeholder="请输入账号" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input show-password prefix-icon="Lock" v-model="data.form.password" placeholder="请输入新密码" />
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-input show-password prefix-icon="Lock" v-model="data.form.confirmPassword" placeholder="请确认新密码" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" style="width: 100%" @click="sendResetLink">重置密码</el-button>
          </el-form-item>
        </el-form>
        <div style="margin-top: 30px; text-align: right">
          <a href="/login">返回登录</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import request from "@/utils/request";
import { ElMessage } from "element-plus";
import router from "@/router";

const data = reactive({
  form: {
    username: '',
    password: '',
    confirmPassword: ''
  }
});

// 验证两次密码是否一致
const validateConfirmPassword = (rule, value, callback) => {
  if (!data.form.password) {
    callback(new Error('请输入新密码'));
  } else if (value !== data.form.password) {
    callback(new Error('两次输入的密码不一致'));
  } else {
    callback();
  }
};

// 更新表单验证规则以确保两次密码一致
const rules = reactive({
  username: [
    { required: true, message: '请输入账号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入新密码', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
});

const formRef = ref();

const sendResetLink = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/sendReset', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success("密码已重置");
          router.push("/login");
        } else {
          ElMessage.error(res.msg);
        }
      });
    }
  });
};

</script>

<style scoped>
.login-container {
  min-height: 100vh;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  background-image: url("@/assets/imgs/bj.png");
  background-size: cover;
}
.login-box {
  background-color: rgba(255, 255, 255, 0.4);
  box-shadow: 0 0 10px rgb(0, 0, 0, 0.1);
  border: 1px solid #dddddd;
  padding: 30px;
  border-radius: 10px;
}
.login {
  font-weight: bold;
  font-size: 24px;
  text-align: center;
  margin-right: 30px;
}
</style>
