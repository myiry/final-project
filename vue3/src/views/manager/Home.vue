<template>
  <div>
    <div class="card" style="line-height: 30px">
      <div>Welcome，{{ user.name }} Have a nice day！</div>
    </div>
    <div class="card" style="line-height: 40px">
      <div style="text-align: center; font-weight: bold">chart</div>
      <!-- 图表选择器 -->
      <div class="chart-selector">
        <span class="color-label">chart：</span>
        <el-select v-model="selectedChart" placeholder="please select chart">
          <el-option label="bar chart" value="bar"></el-option>
          <el-option label="pie chart" value="pie"></el-option>
          <el-option label="line chart" value="line"></el-option>
        </el-select>
        <span class="color-label">dataset：</span>
        <el-select v-model="analysisType" placeholder="please select">
          <el-option v-for="option in datasetOptions" :key="option.value" :label="option.label" :value="option.value"></el-option>
        </el-select>
        <span class="color-label">datatype：</span> <!-- 新增数据类型选择器 -->
        <el-select v-model="dataType" placeholder="please select dataType">
          <el-option label="average" value="average"></el-option>
          <el-option label="pass rate" value="pass"></el-option>
        </el-select>
        <span class="color-label">title：</span>
        <el-input v-model="chartTitle" placeholder="please input the chart title" class="chart-input"></el-input>
        <template v-if="selectedChart !== 'pie'">
          <span class="color-label">color：</span>
          <el-color-picker v-model="chartColor" class="color-picker" show-alpha></el-color-picker> <!-- 单个颜色选择器 -->
        </template>
        <el-button type="primary" @click="confirmChartChange" class="confirm-button">confirm</el-button>
      </div>
    </div>

    <!-- 图表容器 -->
    <div v-if="confirmedChart === 'bar'" class="card chart-container" ref="barChartContainer" id="barChartContainer">
      <div ref="barChart" class="chart"></div>
      <div class="button-group">
        <el-button type="primary" @click="toggleFullscreen('barChartContainer')" class="fullscreen-button">fullscreen</el-button>
        <el-button type="primary" @click="exportToPDF('barChartContainer')" class="export-button">downloadPDF</el-button>
      </div>
    </div>
    <div v-if="confirmedChart === 'pie'" class="card chart-container" ref="pieChartContainer" id="pieChartContainer">
      <div ref="pieChart" class="chart"></div>
      <div class="button-group">
        <el-button type="primary" @click="toggleFullscreen('pieChartContainer')" class="fullscreen-button">fullscreen</el-button>
        <el-button type="primary" @click="exportToPDF('pieChartContainer')" class="export-button">download PDF</el-button>
      </div>
    </div>
    <div v-if="confirmedChart === 'line'" class="card chart-container" ref="lineChartContainer" id="lineChartContainer">
      <div ref="lineChart" class="chart"></div>
      <div class="button-group">
        <el-button type="primary" @click="toggleFullscreen('lineChartContainer')" class="fullscreen-button">fullscreen</el-button>
        <el-button type="primary" @click="exportToPDF('lineChartContainer')" class="export-button">downloadPDF</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import request from "@/utils/request";
import * as echarts from 'echarts'; // 引入 ECharts
import { onMounted, ref, watch } from 'vue';
// 引入 html2canvas 和 jsPDF
import html2canvas from 'html2canvas';
import jsPDF from 'jspdf';

// 获取缓存在浏览器中的数据
const user = JSON.parse(localStorage.getItem('student-user') || '{}');

// 分析类型
const analysisType = ref('');
// 选择的图表类型
const selectedChart = ref('bar');
// 确认后的图表类型
const confirmedChart = ref('bar');
// 图表标题
const chartTitle = ref('');
// 图表颜色
const chartColor = ref('#5470c6');
// 图表颜色组（饼状图）
const chartColors = ref(['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de', '#3ba272', '#fc8452', '#9a60b4', '#ea7ccc']);
// 预定义颜色

// 数据类型
const dataType = ref('average'); // 新增数据类型

// 数据集选项
const datasetOptions = ref([]);

// 图表容器的引用
const pieChart = ref(null);
const barChart = ref(null);
const lineChart = ref(null);

// 实际从后端获取的数据
const chartData = ref([]);
const barChartData = ref([]);
const lineChartData = ref([]);
const userId = JSON.parse(localStorage.getItem('student-user') || '{}').userId; // 假设用户信息中包含id字段

// 获取数据集选项的函数
const fetchDatasetOptions = async () => {
  try {
    const response = await request.get('/mark/datasetOptions', { params: { userid: userId } }); // 传入userid参数
    datasetOptions.value = response;
    console.log('Fetched dataset data:', response); // 添加调试信息
    console.log('Fetched dataset options:', datasetOptions.value); // 添加调试信息
    if (datasetOptions.value.length > 0) {
      analysisType.value = datasetOptions.value[0].value; // 默认选择数据集的第一条
    }
  } catch (error) {
    console.error('获取数据集选项失败:', error);
  }
};

// 获取图表数据的函数
const fetchChartData = async () => {
  try {
    const response = await request.get('/mark/chartData', { params: { type: analysisType.value, chartType: 'pie', dataType: dataType.value, userid: userId } }); // 修改为同一个接口，并传递参数
    chartData.value = response.data;
    initChart();
  } catch (error) {
    console.error('获取图表数据失败:', error);
  }
};

const fetchBarChartData = async () => {
  try {
    const response = await request.get('/mark/chartData', { params: { type: analysisType.value, chartType: 'bar', dataType: dataType.value, userid: userId } }); // 修改为同一个接口，并传递参数
    barChartData.value = response.data;
    initBarChart();
  } catch (error) {
    console.error('获取柱状图数据失败:', error);
  }
};

const fetchLineChartData = async () => {
  try {
    const response = await request.get('/mark/chartData', { params: { type: analysisType.value, chartType: 'line', dataType: dataType.value, userid: userId } }); // 修改为同一个接口，并传递参数
    lineChartData.value = response.data;
    initLineChart();
  } catch (error) {
    console.error('获取折线图数据失败:', error);
  }
};

// 初始化图表的函数
const initChart = () => {
  const myChart = echarts.init(pieChart.value);
  const option = {
    title: {
      text: chartTitle.value,
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '成绩分析',
        type: 'pie',
        radius: '50%',
        data: chartData.value,
        itemStyle: {
          color: (params) => chartColors.value[params.dataIndex % chartColors.value.length]
        },
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  };
  myChart.setOption(option);
};

// 初始化柱状图的函数
const initBarChart = () => {
  const myChart = echarts.init(barChart.value);
  const option = {
    title: {
      text: chartTitle.value,
      left: 'center'
    },
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: barChartData.value.map(item => item.name)
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '成绩',
        type: 'bar',
        data: barChartData.value.map(item => item.value),
        itemStyle: {
          color: chartColor.value
        }
      }
    ]
  };
  myChart.setOption(option);
};

// 初始化折线图的函数
const initLineChart = () => {
  const myChart = echarts.init(lineChart.value);
  const option = {
    title: {
      text: chartTitle.value,
      left: 'center'
    },
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: lineChartData.value.map(item => item.name)
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '成绩',
        type: 'line',
        data: lineChartData.value.map(item => item.value),
        itemStyle: {
          color: chartColor.value
        }
      }
    ]
  };
  myChart.setOption(option);
};

// 确认图表类型变更的函数
const confirmChartChange = () => {
  confirmedChart.value = selectedChart.value;
  // 更新图表标题和颜色
  if (confirmedChart.value === 'pie') {
    fetchChartData(); // 传递 analysisType.value 和 dataType.value
  } else if (confirmedChart.value === 'bar') {
    fetchBarChartData(); // 传递 analysisType.value 和 dataType.value
  } else if (confirmedChart.value === 'line') {
    fetchLineChartData(); // 传递 analysisType.value 和 dataType.value
  }
};

// 监听 confirmedChart 的变化
watch(confirmedChart, (newVal) => {
  if (newVal === 'pie') {
    fetchChartData();
  } else if (newVal === 'bar') {
    fetchBarChartData();
  } else if (newVal === 'line') {
    fetchLineChartData();
  }
});

onMounted(() => {
  fetchDatasetOptions(); // 在组件挂载后获取数据集选项
  fetchChartData(); // 在组件挂载后获取数据并初始化图表
  fetchBarChartData(); // 在组件挂载后获取柱状图数据并初始化柱状图
  fetchLineChartData(); // 在组件挂载后获取折线图数据并初始化折线图
});

// 全屏状态
const isFullscreen = ref(false);

// 切换全屏的方法
const toggleFullscreen = (containerId) => {
  const container = document.getElementById(containerId);
  if (!isFullscreen.value) {
    if (container.requestFullscreen) {
      container.requestFullscreen();
    } else if (container.mozRequestFullScreen) { // Firefox
      container.mozRequestFullScreen();
    } else if (container.webkitRequestFullscreen) { // Chrome, Safari and Opera
      container.webkitRequestFullscreen();
    } else if (container.msRequestFullscreen) { // IE/Edge
      container.msRequestFullscreen();
    }
  } else {
    if (document.exitFullscreen) {
      document.exitFullscreen();
    } else if (document.mozCancelFullScreen) { // Firefox
      document.mozCancelFullScreen();
    } else if (document.webkitExitFullscreen) { // Chrome, Safari and Opera
      document.webkitExitFullscreen();
    } else if (document.msExitFullscreen) { // IE/Edge
      document.msExitFullscreen();
    }
  }
  isFullscreen.value = !isFullscreen.value;
};

// 导出PDF的函数
const exportToPDF = async (chartContainerId) => {
  const chartContainer = document.getElementById(chartContainerId);
  if (!chartContainer) {
    console.error('图表容器未找到');
    return;
  }

  // 临时移除缩放和导出PDF按钮
  const zoomButton = chartContainer.querySelector('.fullscreen-button');
  const exportButton = chartContainer.querySelector('.export-button');
  if (zoomButton) {
    zoomButton.style.display = 'none';
  }
  if (exportButton) {
    exportButton.style.display = 'none';
  }

  // 临时移除所有可能产生阴影效果的样式
  const originalBoxShadow = chartContainer.style.boxShadow;
  chartContainer.style.boxShadow = 'none';

  try {
    const canvas = await html2canvas(chartContainer);
    const imgData = canvas.toDataURL('image/png');
    const pdf = new jsPDF();
    const imgProps = pdf.getImageProperties(imgData);
    const pdfWidth = pdf.internal.pageSize.getWidth();
    const pdfHeight = (imgProps.height * pdfWidth) / imgProps.width;
    // 增加y参数的值，使图像向下移动
    pdf.addImage(imgData, 'PNG', 0, 100, pdfWidth, pdfHeight); // 将y参数从0改为20
    pdf.save('chart.pdf');
  } catch (error) {
    console.error('导出PDF失败:', error);
  } finally {
    // 恢复缩放和导出PDF按钮
    if (zoomButton) {
      zoomButton.style.display = '';
    }
    if (exportButton) {
      exportButton.style.display = '';
    }

    // 恢复原始的阴影效果
    chartContainer.style.boxShadow = originalBoxShadow;
  }
};

</script>

<style scoped>
.card {
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.chart-selector {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 10px;
}

.chart-input {
  margin-left: 10px;
  width: 200px;
}

.color-label {
  margin-left: 10px;
}

.color-picker {
  margin-left: 5px;
  width: 150px; /* 调整颜色选择器宽度 */
}

.confirm-button {
  margin-left: 10px;
}

.chart-container {
  width: 100%;
  margin-top: 10px;
  margin-bottom: 20px; /* 添加底部间距 */
  position: relative;
}

.chart {
  width: 100%;
  height: 400px;
}

.button-group {
  display: flex;
  justify-content: flex-end; /* 保持按钮靠右对齐 */
  margin-top: 10px;
  position: absolute; /* 使用绝对定位 */
  top: 10px; /* 距离顶部10px */
  right: 10px; /* 距离右侧10px */
  z-index: 10; /* 确保按钮在图表之上 */
}

.fullscreen-button {
  margin-right: 10px;
}

.export-button {
  margin-left: 10px;
}

.chart-container {
  position: relative; /* 确保按钮相对于图表容器定位 */
}

/* 全屏样式 */
.chart-container:fullscreen {
  width: 100vw;
  height: 100vh;
  margin-top: 0;
  padding: 0;
  display: flex;
  justify-content: center;
  align-items: center; /* 确保垂直居中 */
}

.chart-container:fullscreen .chart {
  width: 100%;
  height: 100%;
}
</style>
