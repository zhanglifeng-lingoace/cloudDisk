package com.edu.cloudDisk.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.edu.cloudDisk.entity.CoursePackageTreeRelation;
import com.edu.cloudDisk.service.CoursePackageTreeRelationService;
import com.edu.common.response.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 课程包和课包树关系表 前端控制器
 * </p>
 *
 * @author zhanglf
 * @since 2022-05-24
 */
@RestController
@RequestMapping("/coursePackageTreeRelation")
public class CoursePackageTreeRelationController {

    @Autowired
    private CoursePackageTreeRelationService coursePackageTreeRelationServiceImpl;

    @ResponseBody
    @GetMapping("/selectById/{id}")
    public CommonResult selectById(@PathVariable("id")Long id) {
        CoursePackageTreeRelation coursePackageTreeRelation = coursePackageTreeRelationServiceImpl.getOne(new QueryWrapper<CoursePackageTreeRelation>().eq("id", id));
        return CommonResult.success(coursePackageTreeRelation);
    }
}

