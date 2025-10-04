import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

// 创建axios实例对象
const request = axios.create({
  // baseURL: 'https://apifoxmock.com/m1/3128855-1224313-default',
  baseURL: '/api',
  timeout: 600000
})

// axios的请求request拦截器，每次请求获取LocalStorage中的token，添加到请求头中
request.interceptors.request.use(
  (config) => {
    let loginUser = JSON.parse(localStorage.getItem('loginUser'))
    console.log(localStorage.getItem('loginUser'))
    if (loginUser) {
      config.headers.token = loginUser.token
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// axios的响应Response拦截器
request.interceptors.response.use(
  (response) => { // 成功回调
    const res = response.data
    if (res.code !== 1) {
      // 业务错误：主动抛错，让catch捕获
      return Promise.reject(new Error(res.msg || '操作失败'))
    }
    return res  // 只返回成功的业务数据
  },
  (error) => { // 失败回调
    if (error.response.status === 401) {
      ElMessage.error('登录过期，请重新登录')
      router.push('/login')
    } else {
      ElMessage.error(error.message || '网络异常，请稍后重试')
    }
    return Promise.reject(error)
  }
)



export default request