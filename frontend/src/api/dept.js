import request from "../utils/request";

// 查询全部部门数据
export const queryAllApi = () => request.get('/depts');

// 新增
export const addDeptApi = (data) => request.post('/depts', data)

// 根据ID查询
export const queryInfoApi = (id) => request.get(`/depts/${id}`)

// 修改
export const updateDeptApi = (data) => request.put('/depts', data)

// 删除
export const deleteDeptApi = (id) => request.delete(`/depts?id=${id}`)
