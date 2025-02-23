<template>
  <div>
    <div claas="card" style="width: 50%; padding: 40px" >
      <!--      padding-right 右边空一点距离-->
      <el-form :model="data.form" ref="formRef"   label-width="100px" label-position="right" style="padding-right: 40px">
        <el-form-item label="username" prop="username">
          <!--          进行验证-->
          <el-input v-model="data.form.username" autocomplete="off" disabled />
        </el-form-item>
        <el-form-item label="password" prop="password">
          <el-input v-model="data.form.password" autocomplete="off" />
        </el-form-item>

        <el-form-item label="E-mail" prop="email" >
          <el-input v-model="data.form.email" autocomplete="off" />
        </el-form-item>
        <el-form-item label="gender" prop="gender">
          <el-radio-group v-model="data.form.sex">
            <el-radio label="male"></el-radio>
            <el-radio label="female"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="birthday" prop="birthday">
          <el-date-picker style="width: 100%;" format="YYYY-MM-DD" value-format="YYYY-MM-DD" v-model="data.form.birth"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="update">save</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import {ElMessage} from "element-plus";
import router from "@/router";
import request from "@/utils/request";

const data = reactive({
  form: JSON.parse(localStorage.getItem('student-user') || "{}")
//  获取登录的信息
})

const update = () => {
  request.put('/update', data.form).then(res => {
    if (res.code === '200') {
      // console.log(res)
      ElMessage.success("操作成功")
      router.push('/login')
    } else {
      ElMessage.error(res.msg)
    }
  })
}

</script>

<style scoped>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
}
.avatar {
  width: 100px;
  height: 100px;
  display: block;
}
</style>
