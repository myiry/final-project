<template>
  <div>
<!--    align-items: center 垂直居中; justify-content: center 水平居中-->
    <div class="login-container">
      <div  class="login-box" style="width: 350px">
        <div class="login">Login</div>
<!--   label-width="120px" 左边空120像素-->
        <el-form :model="data.form" ref="formRef" :rules="rules" style="margin-top: 20px;">
          <el-form-item prop="username">
<!--  <el-form-item label="账号">-->
            <el-input prefix-icon="User" v-model="data.form.username" placeholder="Please input your username" />
          </el-form-item>
          <el-form-item prop="password">
<!--show-password进行加密-->
            <el-input show-password prefix-icon="Lock" v-model="data.form.password" placeholder="Please input your password" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" style="width: 100%" @click="login">login</el-button>
          </el-form-item>

        </el-form>
        <div style="margin-top: 30px; text-align: right">
<!--        这是一个页面转路径  -->
          Don't have an account? please <a href="/register">register</a>
          <!-- 添加忘记密码链接 -->
          <br>
          <a href="/forget-password">forget the password</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
//ctr + alt + l 进行格式化
// vue3
import {reactive, ref} from "vue";
import request from "@/utils/request";
import {ElMessage} from "element-plus";
// 引入路由
import router  from "@/router";
const data = reactive({
   form: {role: ''}
 })



const rules = reactive({
  username: [
    { required: true, message: 'Please input your username', trigger: 'blur' }
  ],
  password: [
    { required: true, message: 'Please input your password', trigger: 'blur' },
    { min: 3, max: 20, message: 'Invalid', trigger: 'blur' },
  ],
})

// 拿到验证结果 {}表示对象
const formRef = ref();

const login = () => {
  // 先验证
  formRef.value.validate((valid) => {
    // console.log(valid) // 打印校验结果
    if (valid) { // 如果校验通过了 则去调用后台 data.form传的参数
      request.post('/login',data.form).then(res => {
        if(res.code === '200') { // 验证成功
          localStorage.setItem('student-user',JSON.stringify(res.data)) // 将缓存放到浏览器中用户信息 转为JSON格式的字符串
          ElMessage.success("success")
          console.log(res.data)
          // 跳转到主页
          router.push("/home") // 跳转到首页(通过路由去跳转)
        } else { // 验证失败
          ElMessage.error(res.msg) // 导入ElMessage
        }
      })
    }
  })
  // 调用后台

}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  overflow: hidden;  /* 超出部分部分隐藏*/
  display: flex; /* flex 值表示该元素应作为弹性容器显示 */
  align-items: center; /* 垂直居中 */
  justify-content: center; /* 水平居中 */
  background-image: url("@/assets/imgs/bj.png"); /*背景图片*/
  background-size: cover; /*背景图片铺满*/
}
.login-box {

  /*background-color: #ffffff; !*透明背景*!*/
  background-color: rgba(255,255,255,0.4); /*透明背景*/
  box-shadow: 0 0 10px rgb(0,0,0,0.1); /*阴影效果*/
  border: 1px solid #dddddd;  /*边框  solid：这是边框的样式，表示边框是实线*/
  padding: 30px; /*设置元素的内边距（padding）*/
  border-radius: 10px; /*圆角*/
}
.login {
  font-weight: bold; /*字体加粗*/
  font-size: 24px; /*字体大小*/
  text-align: center; /*文本居中*/
  margin-right: 30px /*margin-right 是CSS中的一个属性，用于设置元素右边的外边距*/
}
</style>
