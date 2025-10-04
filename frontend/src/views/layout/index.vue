<script setup>
import { onMounted, ref, reactive } from 'vue';
import { ElMessage, ElMessageBox, ElForm, ElFormItem, ElInput } from 'element-plus';
import { useRouter } from 'vue-router';
import { EditPen, SwitchButton, Promotion, Menu, HomeFilled, UserFilled, Tools, 
  HelpFilled, Avatar, Histogram, InfoFilled, Share, Document 
} from '@element-plus/icons-vue'
import { changePasswordApi } from '@/api/login'

let router = useRouter()

const loginName = ref('')
// 定义钩子函数，获取登录用户名
onMounted(() => {
  // 获取用户登录名
  let loginUser = JSON.parse(localStorage.getItem('loginUser'))
  if (loginUser) {
    loginName.value = loginUser.name
  }
})

const logout = () => {
  // 弹出确认框，如果确认则退出登录
  ElMessageBox.confirm('确认退出登录吗？', '提示', {
    confirmButtonText: '确定', 
    cancelButtonText: '取消', 
    type: 'warning'
  }).then(() => { // 确认，清空登录信息
    ElMessage.success('退出登录成功')
    localStorage.removeItem('loginUser')
    router.push('/login') // 跳转到登录页面
  })
}

// 修改密码相关变量
const passwordDialogVisible = ref(false)
const passwordFormRef = ref(null)
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 表单验证规则
const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入旧密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度应在6-20个字符之间', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value === passwordForm.oldPassword) {
          callback(new Error('新密码不能与旧密码相同'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 打开修改密码对话框
const changePassword = () => {
  passwordDialogVisible.value = true
}

// 提交修改密码表单
const submitChangePassword = async (formEl) => {
  if (!formEl) return
  await formEl.validate(async (valid) => {
    if (valid) {
      try {
        // 调用修改密码API
        // 注意：confirmPassword参数已被注释，因为后端验证逻辑已经调整
        // 前端验证确保了两次输入密码一致，后端主要验证旧密码正确性和新密码强度
        const res = await changePasswordApi({
          oldPassword: passwordForm.oldPassword,
          newPassword: passwordForm.newPassword
          // 前端已验证两次密码一致，confirmPassword无需传递给后端
        })
        
        if (res.code) {
          ElMessage.success('修改密码成功，请重新登录')
          passwordDialogVisible.value = false
          // 清空表单
          resetPasswordForm()
          // 退出登录，让用户重新登录
          localStorage.removeItem('loginUser')
          router.push('/login')
        } else {
          ElMessage.error(res.msg || '修改密码失败')
        }
      } catch (error) {
        console.error('修改密码异常:', error)
        ElMessage.error(error.message || '网络异常，请稍后重试')
      }
    } else {
      console.log('表单验证失败')
      return false
    }
  })
}

// 重置修改密码表单
const resetPasswordForm = () => {
  passwordForm.oldPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
}

</script>

<template>
  <div class="common-layout">
    <el-container>
      <!-- Header 区域 -->
      <el-header class="header">
        <span class="title">教育学习辅助</span>
        <span class="right_tool">
          <a href="javascript:void(0)" @click="changePassword">
            <el-icon><EditPen /></el-icon> 修改密码 &nbsp;&nbsp;&nbsp; |  &nbsp;&nbsp;&nbsp;
          </a>
          <a href="javascript:void(0)" @click="logout">
            <el-icon><SwitchButton /></el-icon> 退出登录 【{{ loginName }}】
          </a>
        </span>
      </el-header>
  
      <!-- 修改密码对话框 -->
      <el-dialog v-model="passwordDialogVisible" title="修改密码" width="500px" center>
        <el-form ref="passwordFormRef" :model="passwordForm" :rules="passwordRules" label-width="120px">
          <el-form-item label="旧密码" prop="oldPassword">
            <el-input v-model="passwordForm.oldPassword" type="password" placeholder="请输入旧密码" show-password />
          </el-form-item>
          <el-form-item label="新密码" prop="newPassword">
            <el-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码" show-password />
          </el-form-item>
          <el-form-item label="确认新密码" prop="confirmPassword">
            <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="请确认新密码" show-password />
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="resetPasswordForm(); passwordDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="submitChangePassword($refs.passwordFormRef)">确定</el-button>
          </span>
        </template>
      </el-dialog>
  
      <el-container>
        <!-- 左侧菜单 -->
        <el-aside width="200px" class="aside">
          <!-- 左侧菜单栏 -->
          <el-menu router>
            <!-- 首页菜单 -->
            <el-menu-item index="/index">
              <el-icon><Promotion /></el-icon> 首页
            </el-menu-item>
            
            
            <!-- 班级管理菜单 -->
            <el-sub-menu index="/manage">
              <template #title>
                <el-icon><Menu /></el-icon> 班级学员管理
              </template>
              <el-menu-item index="/clazz">
                <el-icon><HomeFilled /></el-icon>班级管理
              </el-menu-item>
              <el-menu-item index="/stu">
                <el-icon><UserFilled /></el-icon>学员管理
              </el-menu-item>
            </el-sub-menu>
            
            <!-- 系统信息管理 -->
            <el-sub-menu index="/system">
              <template #title>
                <el-icon><Tools /></el-icon>系统信息管理
              </template>
              <el-menu-item index="/dept">
                <el-icon><HelpFilled /></el-icon>部门管理
              </el-menu-item>
              <el-menu-item index="/emp">
                <el-icon><Avatar /></el-icon>员工管理
              </el-menu-item>
            </el-sub-menu>

            <!-- 数据统计管理 -->
            <el-sub-menu index="/report">
              <template #title>
                <el-icon><Histogram /></el-icon>数据统计管理
              </template>
              <el-menu-item index="/empReport">
                <el-icon><InfoFilled /></el-icon>员工信息统计
              </el-menu-item>
              <el-menu-item index="/stuReport">
                <el-icon><Share /></el-icon>学员信息统计
              </el-menu-item>
              <el-menu-item index="/log">
                <el-icon><Document /></el-icon>日志信息统计
              </el-menu-item>
            </el-sub-menu>
          </el-menu>
        </el-aside>
        
        <el-main>
          <router-view>
          </router-view>
        </el-main>
      </el-container>
      
    </el-container>
  </div>
</template>

<style scoped>
.header {
  background-image: linear-gradient(to right, #00547d, #007fa4, #00aaa0, #00d072, #a8eb12);
}

.title {
  color: white;
  font-size: 40px;
  font-family: 楷体;
  line-height: 60px;
  font-weight: bolder;
}

.right_tool{
  float: right;
  line-height: 60px;
}

a {
  color: white;
  text-decoration: none;
}

.aside {
  width: 220px;
  border-right: 1px solid #ccc;
  height: 730px;
}
</style>
