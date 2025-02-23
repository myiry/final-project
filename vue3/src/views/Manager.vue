<template>
  <div>
    <div style="height: 60px; background-color: #fff; display: flex; align-items: center; border-bottom: 1px solid #ddd">
      <div style="flex: 1">
        <div style="padding-left: 20px; display: flex; align-items: center">
          <img src="@/assets/imgs/logo.png" alt="" style="width: 40px">
          <div style="font-weight: bold; font-size: 24px; margin-left: 5px">Exam Management System</div>
        </div>
      </div>
      <div style="width: fit-content; padding-right: 10px; display: flex; align-items: center;">
        <img :src="user.avatar || 'https://th.bing.com/th/id/OIP.3uKj8QlPMw8mCBpv3ps_LQHaHa?rs=1&pid=ImgDetMain'" alt="" style="width: 40px; height: 40px; border-radius: 50%">
        <span style="margin-left: 5px">{{user.name}}</span>
      </div>
    </div>

    <div style="display: flex">
      <div style="width: 200px; border-right: 1px solid #ddd; min-height: calc(100vh - 60px)">
        <el-menu
            router
            style="border: none"
            :default-active="$route.path"
            :default-openeds="['/home', '2', '3']"
        >
          <el-menu-item index="/home">
            <el-icon><HomeFilled /></el-icon>
            <span>Mark Analysis</span>
          </el-menu-item>
          <el-sub-menu index="2" >
            <template #title>
              <el-icon><Memo /></el-icon>
              <span>Home Page</span>
            </template>
            <el-menu-item index="/grade">
              <el-icon><Document /></el-icon>
              <span>student marks</span>
            </el-menu-item>
          </el-sub-menu>
          <el-menu-item index="/person" >
            <el-icon><User /></el-icon>
            <span>Personal Information</span>
          </el-menu-item>
          <el-menu-item index="login" @click="logout">
            <el-icon><SwitchButton /></el-icon>
            <span>Exit</span>
          </el-menu-item>
        </el-menu>
      </div>

      <div style="flex: 1; width: 0; background-color: #f8f8ff; padding: 10px">
        <router-view />
      </div>
    </div>

  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'
const $route = useRoute()
// console.log($route.path)

const user = JSON.parse(localStorage.getItem('student-user') || '{}') // 在浏览器中拿到缓存变量 然后转换了JSON字符串


const logout = () => {
  // 清空浏览器中缓存的数据
  localStorage.removeItem('student-user')
}
</script>

<style scoped>
.el-menu-item.is-active {
  background-color: #dcede9 !important;
}
.el-menu-item:hover {
  color: #11A983;
}
:deep(th)  {
  color: #333;
}
</style>
