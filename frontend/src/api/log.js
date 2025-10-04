import request from '@/utils/request'

// 分页查询日志
export const queryPageApi = (page, pageSize) => 
    request.get(`/log/page?page=${page}&pageSize=${pageSize}`)
