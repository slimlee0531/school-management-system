<script setup>

import { queryPageApi, updateApi as updateStuApi, addApi as addStuApi, queryInfoApi, deleteApi, handleViolationApi } from '../../api/stu'
import { queryAllApi as queryAllClazzApi} from '../../api/clazz'
import { ref, onMounted } from 'vue'
import { ElFormItem, ElMessage, ElMessageBox } from 'element-plus'

// 表格展示数据
let tableData = ref([])
// 搜索表单对象
let searchStu = ref({
  name: '',
  degree: '',
  clazzId: '',
})

// 分页相关变量
const pagination = ref({
  currentPage4: 1,
  pageSize4: 10,
  total: 0,
})

const multipleTableRef = ref(null)

//学历列表数据
const degrees = ref([{ name: '初中', value: 1 },{ name: '高中', value: 2 },{ name: '大专', value: 3 },{ name: '本科', value: 4 },{ name: '硕士', value: 5 },{ name: '博士', value: 6 }])
//性别列表数据
const genders = ref([{ name: '男', value: 1 }, { name: '女', value: 2 }])
// 是否是院校学员列表数据
const whethers = ref([{ name: '是', value: 1 },{ name: '否', value: 0 }])

onMounted(() => {
  queryAllClazz()
  queryPage()
})

// 查询所有的班级
let clazzs = ref([])
// 加载所有班级
const queryAllClazz = async () => {
  const res = await queryAllClazzApi()
  if (res.code) {
    clazzs.value = res.data
  }
}

// 搜索按钮
const submitSearch = () => {
  queryPage()
}

// 清空搜索栏
const clearSearch = () => {
  searchStu.value.name = ''
  searchStu.value.degree = ''
  searchStu.value.clazzId = ''
  queryPage()
}

// 切换选择
const toggleSelection = () => {
  if (multipleTableRef.value) {
    multipleTableRef.value.clearSelection()
  }
}

// 选择变化
const multipleSelection = ref([])
const handleSelectionChange = (selection) => {
  multipleSelection.value = selection.map(item => item.id)
}

// 可选判断
const selectable = (row) => {
  return true
}

// 编辑学员
const updateStu = async (id) => {
  console.log('编辑学员:', id)

  clearStuDialog()
  customDraggingVisible.value = true
  dialogTitle.value = '修改学员'
  
  try {
    let res = await queryInfoApi(id)
    if (res.code) {
      stu.value = res.data
    } else {
      ElMessage.error('获取学员详情失败')
    }
  } catch (error) {
    console.error('获取学员详情异常:', error)
    ElMessage.error('获取学员详情失败，请稍后重试')
  }
}

// 表单重置函数
const resetForm = (formEl) => {
  if (!formEl) return
  formEl.resetFields()
}

// 违纪记录
const openVialation = (id) => {
  console.log('违纪记录:', id)
  ElMessageBox.prompt('请输入违纪扣分', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPattern: /^\d+$/,
    inputErrorMessage: '请输入一个整数'
  }).then(async (value) => {
    let result = await handleViolationApi(id, value.value)
    if (result.code) {
      ElMessage.success('处理成功')
      queryPage()
    } else {
      ElMessage.error(result.msg || '更新失败')
    }
  })
}

const background = ref(false)
const disabled = ref(false)

// 分页组件不需要额外的对象，直接使用ref变量即可
// 当前页码发生变化时触发
const handleSizeChange = (pageSize) => {
  pagination.value.pageSize4 = pageSize
  queryPage()
  console.log(`current pageSize: ${pageSize}`)
}

// 每页条数发生变化时触发
const handleCurrentChange = (page) => {
  pagination.value.currentPage4 = page
  queryPage()
  console.log(`current page: ${page}`)
}
// 分页条件查询
const queryPage = async () => {
  try {
    // 根据API定义，参数应该是独立的，不是一个对象
    const res = await queryPageApi(
      searchStu.value.clazzId, 
      searchStu.value.degree, 
      searchStu.value.name, 
      pagination.value.currentPage4, 
      pagination.value.pageSize4
    )
    
    console.log('API返回结果:', res); // 添加调试日志
    
    // 更灵活的状态判断，适应不同的后端返回格式
    if (res.code) {
      // 优先检查常用的数据字段格式
      tableData.value = res.data.rows || []
      pagination.value.total = res.data.total || 0
    } else {
      console.error('获取学员数据失败，状态码:', res.code, '消息:', res.msg);
      // 即使失败也尝试设置数据，以防某些特殊情况
      tableData.value = res.data?.records || res.data?.rows || res.data || [];
    }
  } catch (error) {
    console.error('获取学员数据异常:', error);
    // 异常情况下也确保表格有数据（空数组），避免显示错误
    tableData.value = [];
  }
}

// ----------------新增/修改学员----------------
const customDraggingVisible = ref(false)
const labelWidth = ref(100)
let dialogTitle = ref('')
let stu = ref({
  name: '', 
  no: '', 
  gender:'', 
  phone:'', 
  degree:'', 
  clazzId:'', 
  idCard:'', 
  isCollege:'', 
  address:'', 
  graduationDate:''
})
const clearStuDialog = () => {
  stu.value = {
    name: '', 
    no: '', 
    gender:'', 
    phone:'', 
    degree:'', 
    clazzId:'', 
    idCard:'', 
    isCollege:'', 
    address:'', 
    graduationDate:''    
  }
}
const addStu = async () => {
  console.log('新增学员');
  customDraggingVisible.value = true
  dialogTitle.value = '新增学员'
  clearStuDialog();
  queryPage()
}

// 表单校验规则
const stuFormRef = ref()
const rules = ref({
  name: [
    { required: true, message: '姓名为必填项', trigger: 'blur' },
    { min: 2, max: 10, message: '姓名长度为2-20个字', trigger: 'blur' }
  ],
  no: [
    { required: true, message: '学号为必填项', trigger: 'blur' },
    { min: 10, max: 10, message: '学号长度为10个字', trigger: 'blur' }
  ],
  gender: [{ required: true, message: '性别为必填项', trigger: 'change' }],
  phone: [
    { required: false, message: '手机号为必填项', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/g, message: '请输入合法的手机号', trigger: 'blur' }
  ],
  isCollege: [
    { required: true, message: '是否院校学员为必填项', trigger: 'change' }
  ]
})

// -----------------保存学生信息------------------
const saveStu = (formInstance) => {
  // 表单校验
  if (!formInstance) return
  formInstance.validate(async (valid) => {
    if (valid) {
      console.log('表单校验通过');
      // 这里可以添加提交逻辑
      let api
      if (stu.value.id) {
        api = updateStuApi(stu.value)
      } else {
        api = addStuApi(stu.value)
      }
      
      try {
        let res = await api
        if (res.code) {
          ElMessage.success('操作成功')
          customDraggingVisible.value = false
          resetForm(formInstance) // 使用传入的表单实例，而不是全局引用
          queryPage() // 刷新页面数据
        } else {
          ElMessage.error(res.msg || '操作失败')
        }
      } catch (error) {
        console.error('保存学生信息异常:', error);
        ElMessage.error('保存失败，请稍后重试')
      }
    } else {
      console.log('表单校验不通过');
      return false
    }
  })
}

const delById = async (id) => {
  ElMessageBox.confirm('您确认删除此数据吗？', '删除学生', {confirmButtonText:'确定', cancelButtonText:'取消', type:'warning'})
    .then(async () => {
      let res = await deleteApi(`${id}`)
      if (res.code) {
        ElMessage.success('删除成功')
        queryPage()
      } else {
        ElMessage.error(res.msg || '删除失败')
      }
    }).catch(() => {
      ElMessage.info('取消删除')
    })
}

// 批量删除学员
const delByIds = async () => {
  if (!multipleSelection.value || multipleSelection.value.length === 0) {
    ElMessage.warning('请选择要删除的学员')
    return
  }
  
  console.log('删除学员:', multipleSelection.value)
  
  ElMessageBox.confirm('您确定删除选中的数据吗？', '删除学生', {confirmButtonText:'确定', cancelButtonText:'取消', type:'warning'})
    .then(async () => {
      try {
        // 获取选中的学员ID列表
        let res = await deleteApi(multipleSelection.value.join(','))
        if (res.code) {
          ElMessage.success('删除成功')
          queryPage()
          // toggleSelection() // 清空选择
        } else {
          ElMessage.error(res.msg || '删除失败')
        }
      } catch (error) {
        console.error('删除学员失败:', error)
        ElMessage.error('网络错误，请稍后重试')
      }
    })
}

</script>

<template>
  <h1>学员管理</h1> <br>

  <!-- 搜索栏 -->
  <el-form :inline="true" :model="searchStu" class="demo-form-inline">
    <el-form-item label="姓名">
      <el-input v-model="searchStu.name" placeholder="请输入学生姓名" clearable />
    </el-form-item>

    <el-form-item label="学历">
      <el-select v-model="searchStu.degree" placeholder="请选择" clearable>
        <el-option v-for="degree in degrees" :label="degree.name" :value="degree.value" />
      </el-select>
    </el-form-item>

    <el-form-item label="班级">
      <el-select v-model="searchStu.clazzId" placeholder="请选择班级" clearable>
        <el-option v-for="clazz in clazzs" :label="clazz.name" :value="clazz.id" />
      </el-select>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="submitSearch">查询</el-button>
      <el-button type="info" @click="clearSearch">清空</el-button>
    </el-form-item>
  </el-form>

  <!-- 新增/删除按钮 -->
   <div class="buttons">
     <el-button type="success" size="large" @click="addStu();resetForm(stuFormRef)">+ 新增学员</el-button>
     <el-button type="danger" size="large" @click="delByIds()">- 批量删除</el-button>
      <!-- 取消多选 -->
     <el-button type="info" @click="toggleSelection()">取消多选</el-button>
   </div> <br>

   <!-- 表格 -->
    <!-- <pre>{{ tableData }}</pre> -->
     <!-- <pre>{{ clazzs }}</pre> -->
   <div class="table">
      <el-table ref="multipleTableRef" :data="tableData" row-key="id" style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" :selectable="selectable" width="55" />

        <el-table-column prop="name" label="姓名" width="80" align="center"/>

        <el-table-column prop="no" label="学号" width="120" align="center"/>

        <el-table-column label="班级" width="150" align="center">
          <template #default="scope">{{ clazzs.find(item => item.id === scope.row.clazzId)?.name || '未分配班级' }}</template>
        </el-table-column>

        <el-table-column prop="gender" label="性别" width="70" align="center">
          <template #default="scope">{{ scope.row.gender === 1 ? '男' : '女' }}</template>
        </el-table-column>

        <el-table-column prop="phone" label="手机号" width="120" align="center"/>
        
        <el-table-column prop="degree" label="最高学历" width="100" align="center">
          <template #default="scope">{{ degrees.find(item => item.value === scope.row.degree)?.name || '其他' }}</template>
        </el-table-column>

        <el-table-column prop="violationCount" label="违纪次数" width="100" align="center"/>

        <el-table-column prop="violationScore" label="违纪扣分" width="100" align="center"/>

        <el-table-column prop="updateTime" label="最后修改时间" width="110" align="center"/>

        <el-table-column label="操作" width="200" align="center">
          <template #default="scope">
            <el-button type="primary" size="small" @click="updateStu(scope.row.id)">编辑</el-button>
            <el-button type="warning" size="small" @click="openVialation(scope.row.id)">违纪</el-button>
            <el-button type="danger" size="small" @click="delById(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
   </div>

   <!-- 分页条 -->
    <div class="demonstration">
      <el-pagination
        v-model:current-page="pagination.currentPage4"
        v-model:page-size="pagination.pageSize4"
        :page-sizes="[5, 10, 20, 30]"
        :size="size"
        :disabled="disabled"
        :background="background"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    
    <!-- 新增/编辑学员对话框 -->
    <div class="stuDialog">
      <el-dialog v-model="customDraggingVisible" class="custom-dragging-style"
        :title="dialogTitle" width="50%" draggable>
        <!-- <span>This dialog has custom dragging styles. Try dragging it to see the effects!</span> -->

        <el-form :model="stu" ref="stuFormRef" :rules="rules">
          <!-- 第一行 -->
          <el-row>
            <el-col :span="12">
              <el-form-item label="姓名" :label-width="labelWidth" prop="name">
                <el-input v-model="stu.name" placeholder="请输入姓名" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="学号" :label-width="labelWidth" prop="no">
                <el-input v-model="stu.no" placeholder="请输入学号" clearable />
              </el-form-item>
            </el-col>
          </el-row>

          <!-- 第二行 -->
          <el-row>
            <el-col :span="12">
              <el-form-item label="性别" :label-width="labelWidth"  prop="gender">
                <el-select v-model="stu.gender" placeholder="请选择" style="width: 100%;">
                  <el-option v-for="gender in genders" :label="gender.name" :value="gender.value" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="手机号" :label-width="labelWidth"  prop="phone">
                <el-input v-model="stu.phone" placeholder="请输入手机号"/>
              </el-form-item>
            </el-col>
          </el-row>

          <!-- 第三行 -->
          <el-row>
            <el-col :span="12">
              <el-form-item label="身份证号" :label-width="labelWidth"  prop="idCard">
                <el-input v-model="stu.idCard" placeholder="请输入身份证号"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="是否院校" :label-width="labelWidth" prop="isCollege">
                <el-select v-model="stu.isCollege" placeholder="请选择" style="width: 100%;">
                  <el-option v-for="w in whethers" :label="w.name" :value="w.value" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <!-- 第四行 -->
          <el-row>
            <el-col :span="12">
              <el-form-item label="联系地址" :label-width="labelWidth" prop="address">
                <el-input v-model="stu.address" placeholder="请输入联系地址"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="最高学历" :label-width="labelWidth" prop="degree">
                <el-select v-model="stu.degree" placeholder="请选择" style="width: 100%;">
                  <el-option v-for="degree in degrees" :label="degree.name" :value="degree.value" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <!-- 第五行 -->
          <el-row>
            <el-col :span="12">
              <el-form-item label="毕业时间" :label-width="labelWidth" prop="graduationDate">
                <el-date-picker v-model="stu.graduationDate" type="date" placeholder="请选择毕业时间" value-format="YYYY-MM-DD" style="width: 100%;"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="所属班级" :label-width="labelWidth" prop="clazzId">
                <el-select v-model="stu.clazzId" placeholder="请选择" style="width: 100%;">
                  <el-option v-for="clazz in clazzs" :label="clazz.name" :value="clazz.id" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        

        <template #footer>
          <div class="dialog-footer">
            <el-button type="info" @click="customDraggingVisible = false; resetForm(stuFormRef)">取消</el-button>
            <el-button type="primary" @click="saveStu(stuFormRef)">保存</el-button>
          </div>
        </template>
      </el-dialog>
    </div>

</template>

<style scoped>
.buttons {
  margin: 20px 0;
}

:global(.custom-dragging-style.is-dragging) {
  border: 2px dashed var(--el-color-primary);
  opacity: 0.65;
}
</style>