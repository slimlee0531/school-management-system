package com.itheima.controller;

import com.itheima.anno.LogOperation;
import com.itheima.pojo.Dept;
import com.itheima.pojo.Result;
import com.itheima.service.DeptService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/depts")
@RestController
public class DeptController {

    //private static final Logger logger = LoggerFactory.getLogger(DeptController.class);

    @Autowired
    private DeptService deptService;

    @GetMapping
    public Result list() {
//        System.out.println("查询全部部门的数据：");
        log.debug("查询全部部门的数据:");
        List<Dept> deptList = deptService.findAll();
        return Result.success(deptList);
    }

    /**
     * 删除部门：方式一：HttpServletRequest 获取请求参数
     */
//    @DeleteMapping("/depts")
//    public Result delete(HttpServletRequest request) {
//        String idStr = request.getParameter("id");
//        int id = Integer.parseInt(idStr);
//        System.out.println(id);
//        return Result.success();
//    }

    /**
     * 删除部门：方式二：@RequestParam
     * 注意事项：一旦声明了@RequestParam, 该参数在请求时必须传递，如果不传递就会报错(默认：required = true)
     */
//    @DeleteMapping("/depts")
//    public Result delete(@RequestParam(value = "id", required = false) Integer deptId) {
//        System.out.println(deptId);
//        return Result.success();
//    }

    /**
     * 删除部门-方式三：省略@RequestParam（前端传递的请求参数名与服务端方法形参名一致）
     */
    @DeleteMapping
    public Result delete(Integer id) {
//        System.out.println(id);
        log.info("根据ID删除部门：{}", id);
        deptService.deleteById(id);
        return Result.success();
    }

    /**
     * 新增部门
     */
    @LogOperation
    @PostMapping
    public Result add(@RequestBody Dept dept) {
//        System.out.println(dept);
        log.info("新增部门：{}", dept);
        deptService.add(dept);
        return Result.success();
    }

    /**
     * 根据ID查询部门
     */
//    @GetMapping("/depts/{id}")
//    public Result getInfo(@PathVariable("id") Integer deptId) {
//        System.out.println("根据ID查询部门:" + deptId);
//        return Result.success();
//    }

    /**
     * 根据ID查询部门
     * 这样就可以省略掉 ("id")
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id) {
//        System.out.println(id);
        log.info("根据ID查询部门：{}", id);
        Dept dept = deptService.getById(id);
        return Result.success(dept);
    }

    /**
     * 修改部门
     */
    @LogOperation
    @PutMapping
    public Result update(@RequestBody Dept dept) {
//        System.out.println("修改部门" + dept);
        log.info("修改部门: {}", dept);
        deptService.update(dept);
        return Result.success();
    }

}
