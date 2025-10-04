<script setup>

import { ref, onMounted } from 'vue';
// import axios from 'axios';
import { queryAllApi, addDeptApi, queryInfoApi, updateDeptApi, deleteDeptApi } from '../../api/dept';
import { ElMessage, ElMessageBox } from 'element-plus';

// 声明列表展示数据
let deptList = ref([])

// 动态加载数据-查询部门
const queryAll = async () => {
    // const result = await axios.get(`https://apifoxmock.com/m1/3128855-1224313-default/depts`)
    // if (result.data.code)
    //     deptList.value = result.data.data

    const result = await queryAllApi(); // 查询全部部门数据
    if (result.code) {
        deptList.value = result.data; // 这里改成deptList就显示成功了
    }
}
 
// 钩子函数
onMounted(() => {
    queryAll()
})


// 编辑部门-根据ID查询回显数据
const handleEdit = async(id) => {
    console.log(`Edit item with ID ${id}`);
    formTitle.value = '修改部门'
    showDialog.value = true
    deptForm.value = {name: ''}

    const result = await queryInfoApi(id)   // 根据ID查询
    if (result.code) {
        deptForm.value = result.data
    }
};

// 删除部门
const handleDelete = (id) => {
    console.log(`Delete item with ID ${id}`)
    // 删除功能
    // 弹出框，直接从ElementPlus复制
    ElMessageBox.confirm(
    '你确定要删除吗?',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
  .then( async () => {
    // 删除部门
    try {
        await deleteDeptApi(id);
        // 只有成功才会执行到这
        ElMessage({type: 'success', message: 'Delete completed'})
        queryAll()
    } catch (error) {
        // 捕获API调用的错误（如后端自定义的）
        ElMessage({type: 'error', message: error.message});
    }
})
    .catch(() => {
        // 只处理《用户取消删除》的情况
        ElMessage({type: 'info', message: '已取消删除'})
    });
};


const formTitle = ref('')

// 新增部门
const add = () => {
    formTitle.value = '新增部门'
    showDialog.value = true;
    deptForm.value = {name: ''}
}

// 新增部门对话框的状态
const showDialog = ref(false)
// 表单数据
const deptForm = ref({name: ''})
// 表单验证规则
const formRules = ref({
    name : [
        {required: true, message: '请输入部门名称', trigger: 'blur'}, 
        {min: 2, max: 10, message: '长度在2到10个字符', trigger: 'blur'}
    ]
})

// 表单标签宽度
const formLabelWidth = ref('120px')
// 表单引用
const deptFormRef = ref(null)

// 重置表单
const resetForm = () => {
    deptFormRef.value.resetFields()
}

// 提交表单
const save = async () => {
    // 表单校验
    await deptFormRef.value.validate(async valid => {
        if (!valid) return
        // 提交表单
        let result = null;
        if (deptForm.value.id) {
            result = await updateDeptApi(deptForm.value) // 修改
        } else {
            result = await addDeptApi(deptForm.value) // 新增
        }

        if (result.code) {
            ElMessage.success('操作成功')
            // 关闭对话框
            showDialog.value = false
            // 重置表单
            resetForm()
            // 重新加载数据
            queryAll()
        } else {
            ElMessage.error(result.msg)
        }
    })
}

</script>

<template>
    <h1>部门管理</h1>

    <!-- 新增部门按钮 -->
    <div class="container">
        <el-button type="primary" @click="add()" style="float: right;">+ 新增部门</el-button>
    </div>

    <!-- 表格 -->
    <div class="container">
        <el-table :data="deptList" stripe style="width: 100%">
            <el-table-column type="index" label="序号" width="100" align="center"/>
            <el-table-column prop="name" label="部门名称" width="300" align="center"/>
            <el-table-column prop="updateTime" label="最后操作时间" width="300" align="center"/>
            <el-table-column fixed="right" label="操作" align="center">
                <template #default="scope">
                    <el-button type="primary" size="small" @click="handleEdit(scope.row.id)"><el-icon><EditPen /></el-icon> 编辑 </el-button>
                    <el-button type="danger" size="small" @click="handleDelete(scope.row.id)"> <el-icon><Delete /></el-icon>删除 </el-button>
                </template>
            </el-table-column>

        </el-table>
    </div>

    <!-- 新增部门的dialog对话框 -->
    <el-dialog v-model="showDialog" :title="formTitle" width="30%" @close="resetForm">
        <el-form :model="deptForm" :rules="formRules" ref="deptFormRef">
            <el-form-item label="部门名称" prop="name" label-width="80px">
                <el-input v-model="deptForm.name" autocomplete="off"></el-input>
            </el-form-item>
        </el-form>

        <template #footer>
            <span class="dialog-footer">
                <el-button @click="showDialog = false">取消</el-button>
                <el-button type="primary" @click="save">确定</el-button>
            </span>
        </template>
    </el-dialog>


</template>

<style scoped>
.container {
    margin: 30px 0px;
}
</style>