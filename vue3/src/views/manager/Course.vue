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
    <div style="margin-bottom: 10px">
      <el-button type="primary" @click="handleAdd">Add</el-button>
    </div>
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
<!--            <template slot-scope="scope">-->
<!--      空心  plain-->
<!--      按钮大小      size="small"-->
            <el-button type="primary" size="default" @click="handleEdit(scope.row)">edit</el-button>
            <el-button type="danger" plain @click="del(scope.row.id)">delete</el-button>
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

    <el-dialog width="35%" v-model="data.dialogFormVisible" :title="data.title">
<!--      padding-right 右边空一点距离-->
      <el-form :model="data.form"  ref="formRef" :rules="rules" label-width="100px" label-position="right" style="padding-right: 40px">
        <el-form-item label="课程名称" prop="name">
          <el-input v-model="data.form.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="课程编号" prop="no" >
          <el-input v-model="data.form.no" autocomplete="off" />
        </el-form-item>
        <el-form-item label="课程描述">
          <el-input v-model="data.form.descr" autocomplete="off" />
        </el-form-item>
        <el-form-item label="课时" prop="times">
          <el-input v-model="data.form.times" autocomplete="off" />
        </el-form-item>
        <el-form-item label="任课教师" prop="teacher">
          <el-input v-model="data.form.teacher" autocomplete="off" />
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
  title: ''

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

// 添加
const handleAdd = () => {
  data.title = "添加课程信息"
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
  data.title = "修改课程信息";
  data.dialogFormVisible = true;
}
// 判断新增和修改看没有ID 修改有ID
/*const save = () => {
 request.post("/course/add", data.form).then(res => {
    if(res.code === '200') {
       ElMessage.success("操作成功");
       // 关闭弹窗
      data.dialogFormVisible = false;
       load(); // 重新加载一下数据
    } else {
        ElMessage.error(res.msg);
    }
 })
}*/

const save = () => {
   formRef.value.validate((valid)=>{
     if(valid) {
       request.request({
         url: data.form.id ? '/course/update' : '/course/add',
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
    request.delete("/course/delete/"+id).then(res => {
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
</script>

<style scoped>

</style>
