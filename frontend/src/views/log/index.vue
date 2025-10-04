<script setup>
import { ElTableColumn, ElTag, ElMessage } from 'element-plus';
import { ref, onMounted } from 'vue';
import { queryPageApi } from '../../api/log';

// 列表展示数据
const tableData = ref([])

// 钩子函数
onMounted(() => {
  queryPage()
})


const currentPage4 = ref(1)
const pageSize4 = ref(10)
const background = ref(false)
const disabled = ref(false)
const total = ref(0)

const handleSizeChange = (val) => {
  console.log(`${val} items per page`)
  pageSize4.value = val
  queryPage()
}
const handleCurrentChange = (val) => {
  console.log(`current page: ${val}`)
  currentPage4.value = val
  queryPage()
}

const queryPage = async () => {
  try {
    const res = await queryPageApi(currentPage4.value, pageSize4.value)
    
    if (res.code) {
      tableData.value = res.data.rows || []
      total.value = res.data.total || 0
    } else {
      ElMessage.error('获取日志数据失败')
    }
  } catch (error) {
    console.error('Request error:', error)
    ElMessage.error('请求异常，请重试')
  }
}

</script>

<template>
  <h1>日志管理</h1>

  <!-- 列表展示 -->
  <el-table :data="tableData" border style="width: 100%" fit size="small">
    <el-table-column prop="operateEmpName" label="操作人" align="center" width="80px" />
    <el-table-column prop="operateTime" label="操作时间" align="center" width="150px" />
    <el-table-column prop="className" label="操作类名" align="center" width="300px"/>
    <el-table-column prop="methodName" label="方法名" align="center" width="100px" />
    <el-table-column prop="costTime" label="操作耗时(ms)" align="center" width="100px" />
    <el-table-column prop="methodParams" label="请求参数" align="center" width="280px">
      <template #default="scope">
        <el-popover effect="light" trigger="hover" title="Title" content="Top Center prompts info" placement="top">
          <template #default>
            <div>参数：{{ scope.row.methodParams }}</div>
          </template>
          <template #reference>
            <el-tag v-if="scope.row.methodParams.length <= 30">{{ scope.row.methodParams }}</el-tag>
            <el-tag v-else>{{ scope.row.methodParams.substring(0, 30) + '...' }}</el-tag>
          </template>
        </el-popover>
      </template>
    </el-table-column>
    <el-table-column prop="returnValue" label="返回值" align="center"></el-table-column>
 
  </el-table>

  <!-- 分页条 -->
  <div class="pagination">
    <div class="demo-pagination-block">
      <el-pagination v-model:current-page="currentPage4" v-model:page-size="pageSize4"
        :page-sizes="[5, 10, 15, 20]" :disabled="disabled" :background="background"
        layout="total, sizes, prev, pager, next, jumper" :total="total"
        @size-change="handleSizeChange" @current-change="handleCurrentChange"/>
    </div>
  </div>

</template>

<style scoped>

</style>