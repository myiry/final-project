<template>
  <div class="card">
    <!-- 搜索-->
    <el-input style="width: 260px;margin-left: 10px; margin-right: 10px" v-model="data.moudleName"
              placeholder="please input the module name" :prefix-icon="Search"/>
    <el-button type="primary" style="margin-left: 10px" @click="load">Search</el-button>
    <el-button type="info" @click="reset">Reset</el-button>
  </div>

  <!-- 新增按钮区域 -->
  <div style="display: flex; justify-content: flex-end; margin-top: 10px;">
    <!-- 新增下载模板按钮 -->
    <el-button type="success" @click="downloadTemplate">download the format</el-button>
    <!-- 新增导入按钮 -->
    <el-upload
        style="display: inline-block; margin-left: 10px"
        :show-file-list="false"
        accept=".xlsx, .xls"
        :http-request="handleImport"
    >
      <el-button type="warning">upload</el-button>
    </el-upload>
  </div>

  <div class="card">
    <div>
      <!-- 表格-->
      <el-table :data="data.tableData" border style="width: 100%">
        <el-table-column label="No." align="center">
          <template #default="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="module" label="moduel" align="center"/>
        <el-table-column prop="score" label="score" align="center"/>
        <el-table-column prop="year" label="year" align="center"/>
        <el-table-column prop="semester" label="semester" align="center"/>
        <el-table-column prop="batchId" label="ExcelId" align="center"/>
        <el-table-column label="" align="center" width="160">
          <!--          获取行的数据-->
          <template #default="scope">
            <el-button type="danger" @click="del(scope.row.markId)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页-->
    <div class="demo-pagination-block">
      <el-pagination
          v-model:current-page="data.page"
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
import {Search} from '@element-plus/icons-vue'
import {reactive, ref} from "vue";
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
  moudleName: '',
  tableData: [], // data
  total: 0,
  page: 1, // 当前页码
  pageSize: 5, // 每页的个数,
  form: {},// 数据属性
  title: '',
  user: JSON.parse(localStorage.getItem('student-user') || '{}'),
  dialogFormVisible: false, // 弹窗显示控制
})

const load = () => {
  let params = {
    page: data.page, // pageNum(后端Java): data.pageNum(前端),
    pageSize: data.pageSize,
    moudleName: data.moudleName,
    userId: data.user.userId
  }
  request.get('/mark/marksList', {
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
  data.page = val;
  // console.log(`当前页: ${val}`);
  // 当前页
  // 重新加载
  load();
  // console.log(`current page: ${val}`)
}

const reset = () => {
  data.moudleName = '',
      load()
}

// 拿到验证结果 {}表示对象
const formRef = ref();

const handleImport = (options) => {
  const file = options.file;  // 这里要使用 options.file 来获取实际文件

  console.log("options", options);
  console.log("file", file);

  // 校验文件是否存在
  if (!file) {
    ElMessage.error('select the file');
    return;
  }

  // 文件类型校验
  const allowedTypes = ['application/vnd.ms-excel', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'];
  if (!allowedTypes.includes(file.type)) {
    ElMessage.error('invalid, only support .xlsx or .xls ');
    return;
  }

  // 文件大小校验（最大10MB）
  const maxSize = 10 * 1024 * 1024; // 10MB
  if (file.size > maxSize) {
    ElMessage.error('文件大小超过限制，请上传小于 10MB 的文件');
    return;
  }

  // 使用 FormData 上传文件
  const formData = new FormData();
  formData.append('file', file);
  formData.append('userId',  data.user.userId );

  // 提示上传中
  ElMessage.info('正在上传文件...');

  // 发送上传请求
  request.post('/mark/import', formData).then(response => {
    ElMessage.closeAll();  // 上传完成后关闭消息提示
    handleImportSuccess(response);  // 处理上传成功的响应
  }).catch(error => {
    ElMessage.closeAll();
    ElMessage.error('fail');
    console.error(error);
  });
};


const del = (markId) => {
  ElMessageBox.confirm('删除数据后无法恢复.您确认删除吗?', 'confirm', {type: 'warning',}).then(res => {
    request.delete('/mark/delete/' + markId).then(res => {
      if (res.code === '200') {
        ElMessage.success("success");
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
const handleEdit = (row) => {
  // row 表示传入当前行对象， id表示当前ID
  // 第一步 弹窗
  data.title = "反馈信息";
  data.form = JSON.parse(JSON.stringify(row)) // 进行深层次拷贝
  data.dialogFormVisible = true; // 打开弹窗
}


const save = () => {
  request.put('/grade/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success("操作成功")
      load();// 加载数据
      data.dialogFormVisible = false; // 关闭窗口
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 新增下载模板的方法
const downloadTemplate = () => {
  request({
    url: '/mark/generateTemplate',
    method: 'get',
    responseType: 'blob', // 重要：设置响应类型为blob
  }).then(response => {
    const url = window.URL.createObjectURL(new Blob([response]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', 'standard format.xlsx'); // 下载文件的名称
    document.body.appendChild(link);
    link.click();
  }).catch(() => {
    ElMessage.error('fail');
  });
}

// 新增导入成功后的处理方法
const handleImportSuccess = (response) => {
  if (response.code === '200') {
    ElMessage.success('success');
    load(); // 重新加载数据
  } else {
    ElMessage.error(response.msg);
  }
}

</script>

<style scoped>

</style>
