package com.itheima.controller;

import com.itheima.pojo.Result;
import com.itheima.utils.AliyunOSSOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
public class UploadController {

    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;

    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        log.info("上传文件：", file);

        if (!file.isEmpty()) {
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString().replace("-", "") + "." + extName;
            // 上传文件
            String url = aliyunOSSOperator.upload(file.getBytes(), fileName);
            return Result.success(url);
        }
        return Result.error("上传失败");
//        // 获取原始文件名
//        String originalFilename = file.getOriginalFilename();
//
//        // 新的文件名
//        String extention = originalFilename.substring(originalFilename.lastIndexOf("."));
//        String newFileName = UUID.randomUUID().toString() + "." + extention;
//
//        // 保存文件
//        file.transferTo(new File("D:/Code/" +  newFileName));
//
//        return Result.success();
    }

}
