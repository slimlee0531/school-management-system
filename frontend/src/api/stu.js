import request from '@/utils/request'

// 分页条件查询
export const queryPageApi = (clazzId, degree, name, page, pageSize) => {
    return request.get(`/students?clazzId=${clazzId}&degree=${degree}&name=${name}&page=${page}&pageSize=${pageSize}`)
}

// 修改学生
export const updateApi = (stu) => request.put(`/students`, stu)

// 增加学生
export const addApi = (stu) => request.post('/students', stu)

// 根据ID查询学生
export const queryInfoApi = (id) => request.get(`/students/${id}`)

// 删除学生
export const deleteApi = (ids) => request.delete(`/students/${ids}`)

// 违纪
export const handleViolationApi = (id, score) => request.put(`/students/violation/${id}/${score}`)
