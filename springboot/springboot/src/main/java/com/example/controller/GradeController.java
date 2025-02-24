package com.example.controller;

import com.example.common.Result;
import com.example.entity.Course;
import com.example.entity.Grade;
import com.example.service.GradeService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 学生成绩
 */

// ctr + R 替换
@RestController
@RequestMapping("/grade")
public class GradeController {

    @Resource
    private GradeService gradeService;

    /**
     * 添加
     * @param grade
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Grade grade) {
        gradeService.add(grade);
        return Result.success();
    }

    /**
     * 分页加查询
     * @param pageNum
     * @param pageSize
     * @param grade
     * @return
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             Grade grade) { // ?name=xx&no=xx
        PageInfo<Grade> pageInfo = gradeService.selectPage(pageNum, pageSize, grade);
        return Result.success(pageInfo);
    }

    /**
     * 修改课程
     * @param grade
     * @return
     */
    @PutMapping("/update")
    public Result update(@RequestBody Grade grade) {
        gradeService.updateById(grade);
        return Result.success();
    }


    /**
     * 删除成绩信息
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}") // 根据路径去访问
    public Result delete(@PathVariable Integer id) {
        gradeService.deleteById(id);
        return Result.success();
    }
    // 按 alt + Enter 健生成方法
}
