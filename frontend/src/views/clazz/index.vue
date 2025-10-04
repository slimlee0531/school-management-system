<script setup>

// 输入框
import { ref, onMounted, watch } from 'vue'
// 班级查询API
import { queryPageApi, addApi, deleteApi, queryByIdApi, updateApi } from '../../api/clazz'
// 员工查询API
import { queryAllEmpApi } from '../../api/emp'
import { ElMessage, ElMessageBox } from 'element-plus'

// 班级展示数据
let classList = ref([])
// 搜索表单数据
const searchClass = ref({
    name: '', date: [], begin: '', end: ''
})

// 钩子函数
onMounted( async () => {
    queryPage();
    // 加载员工数据
    await queryAllEmp();
})

// 分页
const currentPage4 = ref(1)
const pageSize4 = ref(10)
const total = ref(0)
const disabled = ref(false)
const background = ref(true)

const handleSizeChange = (val) => {
  console.log(`${val} items per page`)
  pageSize4.value = val
  queryPage();
}
const handleCurrentChange = (val) => {
  console.log(`current page: ${val}`)
  currentPage4.value = val
  queryPage();
}

// 分页查询班级列表
const queryPage = async () => {
  console.log('Search:' , searchClass.value);
  // 确保begin和end的正确值
  const { name, begin, end } = searchClass.value;
  const result = await queryPageApi(name, begin, end, currentPage4.value, pageSize4.value);
  
  if (result.code) {
    classList.value = result.data.rows
    total.value = result.data.total
  }
}

// 清空搜索栏
const clear = () => {
  searchClass.value = {name: '', date: [], begin: '', end: ''};
  queryPage();
}

// 监听searchClass中date属性变化
watch(() => searchClass.value.date, (newVal, oldVal) => {
  console.log('newVal:', newVal, 'oldVal:', oldVal);
  if (newVal.length > 0) {
    searchClass.value.begin = newVal[0]
    searchClass.value.end = newVal[1]
  } else {
    searchClass.value.begin = ''
    searchClass.value.end = ''
  }
})

// ---------------新增 / 修改----------------
let dialogFormVisible = ref(false) // 控制新增/修改的对话框的显示与隐藏
let labelWidth = ref(80) // form表单label的宽度
let formTitle = ref('') // 对话框标题
// 班级表单数据
let classForm = ref({
  id: '', 
  name: '',
  room: '',
  beginDate: '',
  endDate: '',
  subject: '',
  masterId: ''
})

// 清空表单
const clearClass = () => {
  classForm.value = {
    id: '', 
    name: '',
    room: '',
    beginDate: '',
    endDate: '',
    subject: '',
    masterId: ''
  }
}

// 新增班级
const addClass = async () => {
  dialogFormVisible.value = true
  formTitle.value = '新增班级'
  clearClass()
}

// 修改班级
const updateClass = async (id) => {
  clearClass()
  dialogFormVisible.value = true
  formTitle.value = '修改班级'  // 设置表单标题为修改班级
  let result = await queryByIdApi(id)
  if (result.code) {
    classForm.value = result.data
  }
}

// 表单校验规则
const dialogFormRef = ref()
const rules = ref({
  name: [
    { required: true, message: '请输入班级名称', trigger: 'blur' }, 
    { min: 3, max: 10, message: '班级名称长度在 3 到 10 个字符', trigger: 'blur' }
  ],
  room: [{ required: true, message: '请输入班级教室', trigger: 'blur' }],
  beginDate: [{ required: true, message: '请选择开课时间', trigger: 'blur' }],
  endDate: [{ required: true, message: '请选择结课时间', trigger: 'blur' }],
  subject: [{ required: true, message: '请选择课程', trigger: 'blur' }],
  masterId: [{ required: true, message: '请选择班主任', trigger: 'blur' }],
})

// 重置表单
const resetForm = (classForm) => {
  if (!classForm) return
  classForm.resetFields()
}

// -----------保存班级信息
const save = (formRef) => {
  console.log('classForm:', classForm.value);
  dialogFormRef.value.validate((valid) => {
    if (valid) {
      // 校验通过，提交表单数据
      if (formTitle.value === '新增班级') {
        addApi(classForm.value).then((res) => {
          if (res.code) {
            // 新增成功后，刷新班级列表
            queryPage();
            // 关闭对话框
            dialogFormVisible.value = false
            // 保存成功后显示提示信息
            ElMessage.success('保存成功');
          } else {
            ElMessage.error(res.msg || '新增失败');
          }
        }).catch(error => {
          console.error('新增班级出错:', error);
          ElMessage.error('网络错误，请稍后重试');
        })
      } else if (formTitle.value === '修改班级') {
        updateApi(classForm.value).then((res) => {
          if (res.code) {
            // 修改成功后，刷新班级列表
            queryPage();
            // 关闭对话框
            dialogFormVisible.value = false
            // 保存成功后显示提示信息
            ElMessage.success('修改成功');
          } else {
            ElMessage.error(res.msg || '修改失败');
          }
        }).catch(error => {
          console.error('修改班级出错:', error);
          ElMessage.error('网络错误，请稍后重试');
        })
      }
    } else {
      console.log('校验失败')
    }
  })
}

// ----------------删除班级
const deleteClass = async (id) => {
  ElMessageBox.confirm('确定删除该班级吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then( async () => {
    let result = await deleteApi(id)
    if (result.code) {
      ElMessage.success('删除成功')
      queryPage();
    } else {
      ElMessage.error(result.msg)
    }
    }).catch(() => {
      ElMessage.info('已取消删除')
    })
} 

// 所有员工数据
let emps = ref([])
// 所有学科数据
const subjects = ref([{ name: 'Java', id: 1 },{ name: '前端', id: 2 },{ name: '大数据', id: 3 },{ name: 'Python', id: 4 },{ name: 'Go', id: 5 },{ name: '嵌入式', id: 6 }])

// 加载所有员工
const queryAllEmp = async () => {
  try {
    let result = await queryAllEmpApi()
    if (result.code) {
      emps.value = result.data
    } else {
      console.error('加载员工数据失败:', result.msg)
      ElMessage.warning('加载员工数据失败，使用默认数据')
      // 提供默认数据，确保功能可用
      emps.value = [
        { id: '1', name: '张老师' },
        { id: '2', name: '李老师' },
        { id: '3', name: '王老师' }
      ]
    }
  } catch (error) {
    console.error('请求员工数据时发生错误:', error)
    ElMessage.error('网络错误，请稍后重试')
    // 提供默认数据，确保功能可用
    emps.value = [
      { id: '1', name: '张老师' },
      { id: '2', name: '李老师' },
      { id: '3', name: '王老师' }
    ]
  }
}


</script>

<template>
  <h1>班级管理</h1><br>
  
  <!-- 搜索栏 -->  
  <div class="searchForm">
    <el-form :inline="true" :model="searchClass" class="demo-form-inline">
      <el-form-item label="班级名称：">
        <el-input v-model="searchClass.name" placeholder="请输入班级名称" clearable />
      </el-form-item>

      <el-form-item label="课程时间：">
        <el-date-picker
          v-model="searchClass.date"
          type="daterange"
          value-format="YYYY-MM-DD"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          clearable
        />
      </el-form-item>

      <!-- 按钮 -->
      <el-form-item>
        <el-button type="primary" @click="queryPage">查询</el-button>
        <el-button type="info" @click="clear">清空</el-button>
      </el-form-item>
    </el-form>
  </div>

  <!-- 新增班级按钮 -->
   <div class="container">
      <el-button type="success" plain size="large" @click="addClass" round>新增班级</el-button> 
   </div><br>

  <!-- 班级表格 -->
  <div class="tableForm" >
    <el-table :data="classList" border style="width: 100%" >
      <el-table-column type="index" label="班级ID" width="80" align="center" />
      <el-table-column prop="name" label="班级名称" width="180" align="center" />
      <el-table-column prop="room" label="班级教室" width="120" align="center" />
      <el-table-column prop="masterName" label="班主任" width="120" align="center" />
      <el-table-column prop="beginDate" label="开课时间" width="120" align="center" />
      <el-table-column prop="endDate" label="结课时间" width="120" align="center" />
      <el-table-column prop="updateTime" label="最后修改时间" width="200" align="center" />
      <el-table-column prop="status" label="状态" width="120" align="center" />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button type="primary" size="small" @click="updateClass(scope.row.id)">编辑</el-button>
          <el-button type="danger" size="small" @click="deleteClass(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table> 
  </div><br>

  <!-- 分页 -->
  <div class="pagination">
    <el-pagination
      v-model:current-page="currentPage4"
      v-model:page-size="pageSize4"
      :page-sizes="[5, 10, 20, 30]"
      :disabled="disabled"
      :background="background"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
  
  <!-- 新增/修改班级对话框 -->
  <el-dialog v-model="dialogFormVisible" :title="formTitle" width="35%">
    <el-form :model="classForm" :rules="rules" ref="dialogFormRef" labelWidth="labelWidth">
      <el-form-item label="班级名称" prop="name">
        <el-input v-model="classForm.name" placeholder="请输入班级名称" clearable />
      </el-form-item>

      <el-form-item label="班级教室" prop="room">
        <el-input v-model="classForm.room" placeholder="请输入班级教室" clearable />
      </el-form-item>

      <el-form-item label="班主任" prop="masterId">
        <el-select v-model="classForm.masterId" placeholder="请选择班主任" style="width: 100%;">
          <el-option v-for="emp in emps" :key="emp.id" :label="emp.name" :value="emp.id" />
        </el-select>
      </el-form-item>

      <el-form-item label="开课时间" prop="beginDate">
        <el-date-picker
          v-model="classForm.beginDate"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择开课时间"
          clearable
        />
      </el-form-item>

      <el-form-item label="结课时间" prop="endDate">
        <el-date-picker
          v-model="classForm.endDate"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择结课时间"
          clearable
        />
      </el-form-item>

      <el-form-item label="学科" prop="subject">
        <el-select v-model="classForm.subject" placeholder="请选择学科" style="width: 100%;">
          <el-option v-for="subject in subjects" :key="subject.id" :label="subject.name" :value="subject.id" />
        </el-select>
      </el-form-item>

    </el-form>
    
    <!-- 俩按钮 -->
    <template #footer>
      <el-button type="primary" @click="save('dialogFormRef')">保存</el-button>
      <el-button type="info" @click="dialogFormVisible = false; resetForm('dialogFormRef')">取消</el-button>
    </template>
  </el-dialog>

</template>

<style scoped>

</style>