package com.itheima.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClazzQueryParam {
    private Integer page = 1;
    private Integer pageSize = 10;

    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate begin;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;
//    private Integer id;
//    private String name;
//    private String room;
//    private Date beginDate;
//    private Date endDate;
//    private Integer masterId;
////    private Integer subject;
//    private LocalDateTime createTime;
//    private LocalDateTime updateTime;
//    private String masterName;
//    private String status;
}
