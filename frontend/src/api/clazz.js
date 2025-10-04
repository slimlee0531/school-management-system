import request from '@/utils/request'

// 查询员工列表数据
export const queryPageApi = (name, begin, end, page, pageSize) => {
    return request.get(`/clazzs?name=${name}&begin=${begin}&end=${end}&page=${page}&pageSize=${pageSize}`)
}

// 新增班级
export const addApi = (clazz) => request.post('/clazzs', clazz);

// 查询所有班级
export const queryAllApi = () => request.get('/clazzs/list');

// 根据ID查询班级
export const queryByIdApi = (id) => request.get(`/clazzs/${id}`);

// 更新班级
export const updateApi = (clazz) => request.put(`/clazzs`, clazz);

// 删除班级
export const deleteApi = (id) => request.delete(`/clazzs/${id}`);