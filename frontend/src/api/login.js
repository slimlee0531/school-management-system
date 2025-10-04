import request from '@/utils/request'

// 登录
export const loginApi = (data) => request.post('/login', data)

// 修改密码
export const changePasswordApi = (data) => request.post('/login/changePassword', data)
