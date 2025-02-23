<template>
  <div class="card">
    <!-- 搜索-->
    课程名称 <el-input style="width: 260px;margin-left: 10px; margin-right: 10px" v-model="data.name" placeholder="请输入课程名称"  :prefix-icon="Search"/>
    <el-input style="width: 260px; margin-right: 10px" v-model="data.no" placeholder="请输入课程编号" :prefix-icon="Search"/>
    <el-button type="primary" style="margin-left: 10px" @click="load">查询</el-button>
    <el-button type="info" @click="reset">重置</el-button>
  </div>

  <div calss="card">
    <div>
      <!-- 表格-->
      <el-table :data="data.tableData" border style="width: 100%">
        <el-table-column prop="id" label="序号" width="70" align="center"/>
        <el-table-column prop="name" label="课程名称"  align="center" />
        <el-table-column prop="no" label="课程编号" align="center" />
        <el-table-column prop="studentName" label="学生名称" align="center" />
        <el-table-column label="操作" align="center" width="160">
          <!--          获取行的数据-->
          <template #default="scope">
            <el-button type="danger" @click="del(scope.row.id)">删除</el-button>
            <el-button type="primary" @click="addGrade(scope.row)" v-if="data.user.role ==='ADMIN'">打分</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

<!--弹窗-->
    <el-dialog width="35%" v-model="data.dialogFormVisible" :title="data.title">
      <!--      padding-right 右边空一点距离-->
      <el-form :model="data.gradeFrom"  ref="formRef" :rules="rules" label-width="100px" label-position="right" style="padding-right: 40px">
        <el-form-item label="课程名称" prop="name">
          <el-input v-model="data.gradeFrom.name" autocomplete="off" disabled/>
        </el-form-item>
        <el-form-item label="分数" prop="score">
          <el-input v-model="data.gradeFrom.score" autocomplete="off" />
        </el-form-item>
        <el-form-item label="评语" prop="comment">
          <el-input type="textarea" v-model="data.gradeFrom.comment" autocomplete="off"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.dialogFormVisible = false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </span>
      </template>
    </el-dialog>
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
  tableData: [], // data
  total: 0,
  pageSize: 1, // 当前页码
  pageNum: 5, // 每页的个数,
  dialogFormVisible: false, // 默认弹窗关闭
  form: {},// 数据属性
  title: '',
  user: JSON.parse(localStorage.getItem('student-user') || '{}'),
  gradeFrom: {}

})

const load = () => {
  let params = {
    pageNum: data.pageNum, // pageNum(后端Java): data.pageNum(前端),
        pageSize: data.pageSize,
        name: data.name,
        no: data.no
  }
  if(data.user.role === 'STUDENT') { // 如果登录的是学生，那就查询自己的选课记录
    // 进行参数筛选  管理员的话就不用了筛选了（也就是说不用拼接）
    params.studentId = data.user.id
  }
  request.get('/studentCourse/selectPage', {
    params: params // 的参数拿出去了
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
  load()
}

const rules = reactive({
  name: [
    { required: true, message: '请输入课程名称', trigger: 'blur' }
  ],
  no: [
    { required: true, message: '请输入课程编号', trigger: 'blur' },
  ],
})

// 拿到验证结果 {}表示对象
const formRef = ref();


const del = (id) => {
  ElMessageBox.confirm('删除数据后无法恢复.您确认删除吗?','确认',{ type: 'warning',}).then(res => {
    request.delete('studentCourse/delete/'+id).then(res=> {
      if(res.code === '200') {
        ElMessage.success("操作成功");
        // 更新一下数据
        load();
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '取消删除',
    })
  })
}

// 打分
const addGrade = (row) => {
  // row 表示传入当前行对象， id表示当前ID
  // 第一步 弹窗
  data.title = "打分";
  data.dialogFormVisible = true; // 打开弹窗
  data.gradeFrom.name = row.name;
  data.gradeFrom.courseId = row.courseId; // 当我们打开弹窗的时候就赋值
  data.gradeFrom.studentId = row.studentId;
}


const save = () => {
  request.post('/grade/add',data.gradeFrom).then(res=> {
       if(res.code === '200') {
         ElMessage.success("操作成功")
         load();// 加载数据
         data.dialogFormVisible = false; // 关闭窗口
       } else {
         ElMessage.error(res.msg)
       }
  })
}
</script>

<style scoped>

</style>
