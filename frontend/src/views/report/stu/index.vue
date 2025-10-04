<script setup>
import { onMounted } from 'vue'
import * as echarts from 'echarts'
import { queryStudentCountDataApi, queryStudentDegreeDataApi } from '../../../api/report';

// 钩子函数 - 加载报表
onMounted(() => {
  loadStudentCountChart()
  loadDegreeChart()
})

// 加载职位统计报表
const loadStudentCountChart = async () => {
  let res = await queryStudentCountDataApi();
  let clazzList = res.data.clazzList;
  let dataList = res.data.dataList;

  initStudentCountChart(clazzList, dataList)
}

// 获取学历统计报表
const loadDegreeChart = async () => {
  let res = await queryStudentDegreeDataApi();
  initDegreeChart(res.data)
}

// 班级人数统计
function initStudentCountChart(clazzList, dataList) {
  var myChart = echarts.init(document.getElementById('container1'));
  // 绘制图表
  myChart.setOption({
    title: {
      text: '班级人数统计', 
      subText: '', 
      textStyle: {
        fontSize: 20
      }, 
      left: 'center'
    }, 
    grid:{
      left: '3%', 
      right: '4%', 
      bottom: '3%', 
      containerLabel:true
    }, 
    tooltip: {}, 
    xAxis: {
      data: clazzList
    }, 
    yAxis: {}, 
    series: [
      {
        name: '人数', 
        type: 'bar',
        data: dataList, 
        itemStyle: {
          // 设置柱状渐变色
          color:new echarts.graphic.LinearGradient(0, 0, 1, 1, [
            {
              offset: 0, 
              color: '#ffbf61'
            }, 
            {
              offset: 1, 
              color: '#ff9f43'
            }
          ])
        }
      }
    ]
  });
}

function initDegreeChart(degreeDataList) {
  var myChart = echarts.init(document.getElementById('container2'));
  let option = {
    title: {
      text: '学员学历统计', 
      subText: '', 
      textStyle: {
        fontSize: 20
      }, 
      left: 'center'
    }, 
    grid: {
      left: '3%', 
      right: '4%', 
      bottom: '3%', 
      containerLabel: true
    }, 
    tooltip: {
      trigger: 'item'
    }, 
    legend: {
      top: '10%', 
      left: 'center'
    }, 
    series: [
      {
        name: '学历', 
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false, 
        top: '5%', 
        itemStyle: {
          borderRadius: 5, 
          borderColor: '#fff', 
          borderWidth: 2
        }, 
        label: {
          show: false, 
          position: 'center'
        }, 
        emphasis: {
          label: {
            show: true, 
            fontSize: '30', 
            fontWeight: 'bold'
          }
        }, 
        data: degreeDataList
      }
    ]
  };
  // 绘制图表
  myChart.setOption(option);
}

</script>

<template>
  <div class="report_container" id="container1"></div>

  <div class="report_container" id="container2"></div>
</template>

<style scoped>
.report_container {
  width: 49%;
  height: 90%;
  float: left;
  margin-left: 5px;
}

#container1 {
  border-right: 1px dashed #ccc;
}
</style>