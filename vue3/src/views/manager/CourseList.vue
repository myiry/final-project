<template>
  <div class="card">
    <!-- 搜索-->
    课程名称 <el-input style="width: 260px;margin-left: 10px; margin-right: 10px" v-model="data.name" placeholder="请输入课程名称"  :prefix-icon="Search"/>
    <el-input style="width: 260px; margin-right: 10px" v-model="data.no" placeholder="请输入课程编号" :prefix-icon="Search"/>
    <el-input style="width: 260px" v-model="data.teacher" placeholder="请输入教师名称" :prefix-icon="Search"/>
    <el-button type="primary" style="margin-left: 10px" @click="load">Search</el-button>
    <el-button type="info" @click="reset">Reset</el-button>
  </div>

  <div calss="card">
    <div>
      <!-- 表格-->
      <el-table :data="data.tableData" border style="width: 100%">
        <el-table-column prop="id" label="序号" width="70" align="center"/>
        <el-table-column prop="name" label="课程名称"  align="center" />
        <el-table-column prop="no" label="课程编号" align="center" />
        <el-table-column prop="descr" label="课程描述" align="center" />
        <el-table-column prop="times" label="课时" align="center" />
        <el-table-column prop="teacher" label="任课教师" align="center" />
        <el-table-column label="操作" align="center" width="160">
          <!--          获取行的数据-->
          <template #default="scope">
            <el-button type="primary" @click="selectCourse(scope.row)">选课</el-button>
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

  </div>
</template>

<script setup>
import { Search } from '@element-plus/icons-vue'
import {reactive, ref} from "vue";
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";
// vue3的语法
const data = reactive({
  name: '',
  no: '',
  teacher: '',
  tableData: [], // data
  total: 0,
  pageSize: 1, // 当前页码
  pageNum: 5, // 每页的个数,
  dialogFormVisible: false,
  form: {},// 数据属性
  title: '',
  student: JSON.parse(localStorage.getItem('student-user') || '{}')

})

const load = () => {
  request.get('/course/selectPage', {
    params: {
      pageNum: data.pageNum, // pageNum(后端Java): data.pageNum(前端),
      pageSize: data.pageSize,
      name: data.name,
      no: data.no,
      teacher: data.teacher
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
      data.no = '',
      data.teacher = ''
  load()
}

const rules = reactive({
  name: [
    { required: true, message: '请输入课程名称', trigger: 'blur' }
  ],
  no: [
    { required: true, message: '请输入课程编号', trigger: 'blur' },
  ],
  times: [
    { required: true, message: '请输入课时', trigger: 'blur' },
  ],
  teacher: [
    { required: true, message: '请输入任课教师', trigger: 'blur' },
  ],
})

// 拿到验证结果 {}表示对象
const formRef = ref();


const selectCourse = (row) => {
 request.post('/studentCourse/add', {student: data.student.id, name: row.name, no: row.no, courseId: row.id}).then(res=>{
   if(res.code === '200') {
     ElMessage.success("操作成功")
     console.log(row.data)
   } else {
     ElMessage.error(res.msg)
   }
 })
}

</script>

<style scoped>

</style>
