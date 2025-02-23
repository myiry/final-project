<template>
  <div class="card">
<!-- 搜索-->
    <el-input style="width: 260px;margin-left: 10px; margin-right: 10px" v-model="data.username" placeholder="请输入账号查询"  :prefix-icon="Search"/>
    <el-input style="width: 260px; margin-right: 10px" v-model="data.name" placeholder="请输入名称查询" :prefix-icon="Search"/>
<!--    <el-input style="width: 260px" v-model="data.sex" placeholder="请输入性别查询" :prefix-icon="Search"/>-->
    <el-button type="primary" style="margin-left: 10px" @click="load">查询</el-button>
    <el-button type="info" @click="reset">重置</el-button>
  </div>

  <div calss="card">
    <div style="margin-bottom: 10px">
      <el-button type="primary" @click="handleAdd">新增</el-button>
    </div>
    <div>
<!-- 表格-->
      <el-table :data="data.tableData" border style="width: 100%">
        <el-table-column prop="id" label="序号" width="70" align="center"/>
        <el-table-column prop="username" label="学生账号"  align="center" />
        <el-table-column prop="name" label="学生名称"  align="center" />
        <el-table-column prop="phone" label="学生手机" align="center" />
        <el-table-column prop="email" label="学生邮箱" align="center" />
        <el-table-column prop="sex" label="性别" align="center" />
        <el-table-column prop="birth" label="生日" align="center" />
        <el-table-column prop="avatar" label="头像" align="center">
         <template v-slot="scope">
<!--         preview-src-list是否放大  -->
           <el-image v-if="scope.row.avatar" :src="scope.row.avatar" :preview-src-list="[scope.row.avatar]" style="width: 40px; height: 40px; border-radius: 5px"></el-image>
         </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="160">
<!--          获取行的数据-->
          <template #default="scope">
<!--            <template slot-scope="scope">-->
<!--      空心  plain-->
<!--      按钮大小      size="small"-->
            <el-button type="primary" size="default" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" plain @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

<!-- 分页-->
    <div class="demo-pagination-block">
      <el-pagination
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          :page-sizes="[5, 10, 15, 20]"
          :small="small"
          :disabled="disabled"
          :background="background"
          layout="total, sizes, prev, pager, next, jumper"
          :total="data.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
    </div>

<!--弹窗-->
<!--    destroy-on-close关闭弹窗的时候清空上次残留的缓存-->
    <el-dialog width="35%" v-model="data.dialogFormVisible" :title="data.title" destroy-on-close>
<!--      padding-right 右边空一点距离-->
      <el-form :model="data.form" ref="formRef" :rules="rules"  label-width="100px" label-position="right" style="padding-right: 40px">
        <el-form-item label="学生账号" prop="username">
<!--          进行验证-->
          <el-input v-model="data.form.username" autocomplete="off" />
        </el-form-item>
        <el-form-item label="学生密码" prop="password">
          <el-input v-model="data.form.password" autocomplete="off" />
        </el-form-item>
        <el-form-item label="学生名称" >
          <el-input v-model="data.form.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="学生手机">
          <el-input v-model="data.form.phone" autocomplete="off" />
        </el-form-item>
        <el-form-item label="学生邮箱" >
          <el-input v-model="data.form.email" autocomplete="off" />
        </el-form-item>
        <el-form-item label="性别">
         <el-radio-group v-model="data.form.sex">
           <el-radio label="男"></el-radio>
           <el-radio label="女"></el-radio>
         </el-radio-group>
        </el-form-item>
        <el-form-item label="生日">
          <el-date-picker style="width: 100%;" format="YYYY-MM-DD" value-format="YYYY-MM-DD" v-model="data.form.birth"></el-date-picker>
        </el-form-item>
        <el-form-item label="学生头像">
<!--          :show-file-list="false" 是否显示文件上传的结果-->
<!--          list-type="picture" 显示图片的效果-->
          <el-upload action="http://localhost:9090/files/upload" list-type="picture"  :on-success="handleImgUploadSuccess">
            <el-button type="primary">上传头像</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="data.dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </span>
      </template>
   </el-dialog>
  </div>
</template>

<script setup>
import { Search } from '@element-plus/icons-vue'
import {ref,reactive} from "vue";
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";
// 拿到验证结果 {}表示对象

const baseUrl = '/student';
// vue3的语法
const data = reactive({
  name: '',
  // sex: '',
  username: '',
  tableData: [], // data
  total: 0,
  pageSize: 1, // 当前页码
  pageNum: 10, // 每页的个数,
  dialogFormVisible: false,
  form: {},// 数据属性
  title: ''

})

const rules = reactive({
  username: [
    { required: true, message: '请输入账号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
  ],
})

const formRef = ref();

const load = () => {
  request.get(baseUrl + '/selectPage', {
    params: {
      pageNum: data.pageNum, // pageNum(后端Java): data.pageNum(前端),
      pageSize: data.pageSize,
      name: data.name,
      sex: data.sex,
      username: data.username
    }
  }).then(res => { // 传两个数据一个list 一个 total
    // : 表示数据绑定或者模型绑定
    console.log(res);
    data.tableData = res.data?.list || [] // 或者空
    data.total = res.data?.total || 0 // 或者0
  })
}
// 调用方法获取数据
load()
// 分页
const handleSizeChange = (val) => {
  data.pageSize = val;
  // console.log(`每页 ${val} 条`);
  // 每页
  load();
  // console.log(`${val} items per page`)
}
const handleCurrentChange = (val) => {
  data.pageNum = val;
  // console.log(`当前页: ${val}`);
  // 当前页
  // 重新加载
  load();
  // console.log(`current page: ${val}`)
}

const reset = () => {
      data.name = '',
      data.sex = '',
      data.username = ''
      load()
}

// 添加
const handleAdd = () => {
  data.title = "添加学生信息"
  /*两步走*/
  // 清空数据
  data.form = {},
      // 打开弹出
  data.dialogFormVisible = true;
}

 // 编辑 (需要传入行数据)
const handleEdit = (row) => {
  // 进行深度拷贝 拷贝到data.form里面去
  data.form = JSON.parse(JSON.stringify(row)) // 类似于  this.deprtment01 = row; // 拿到回显数据 这样子  data.form = row;
  data.title = "修改学生信息";
  data.dialogFormVisible = true;
}

const save = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.request({
        url: data.form.id ? baseUrl + '/update' : baseUrl + '/add',
        method: data.form.id ? 'put' : 'post',
        data: data.form
      }).then(res => {
        if(res.code === '200') {
          load(); // 从新获取数据
          data.dialogFormVisible = false;
          ElMessage.success("操作成功")
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
// 删除

const del = (id) => {
  ElMessageBox.confirm('删除数据后无法恢复.您确认删除吗?','确认',{ type: 'warning',}).then(res => {
    request.delete(baseUrl + "/delete/"+id).then(res => {
      if (res.code === '200') {
        ElMessage.success("操作成功");
        // 从新加载一下数据
        load();
      } else {
        ElMessage.error(res.msg);
      }
    })
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '取消删除',
    })
  })
}

// 文件上传之后处理的函数
const handleImgUploadSuccess = (res) => {
  // res 表示回调参数
  console.log(res);
  data.form.avatar = res.data;
}
</script>

<style scoped>

</style>
