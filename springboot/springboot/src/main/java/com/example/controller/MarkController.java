package com.example.controller;

import com.alibaba.excel.EasyExcel;
import com.example.common.Result;
import com.example.entity.Mark;
import com.example.entity.Users;
import com.example.entity.vo.MarkVo;
import com.example.entity.vo.PieChartData;
import com.example.service.IMarkService;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2025-02-13
 */
@RestController
@RequestMapping("/mark")
public class MarkController {
    private static final Logger logger = LoggerFactory.getLogger(MarkController.class);

    @Autowired
    private IMarkService markService;

    @GetMapping("/marksList")
    public Result marksList(MarkVo vo){
        return Result.success(markService.marksList(vo));
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result importMarks(@RequestParam("file") MultipartFile file,
                              @RequestParam("userId") String user) {
        try {
            markService.importMarks(file,user);
            return Result.success("导入成功");
        } catch (Exception e) {
            logger.error("Failed to import marks", e);
            return Result.error("导入失败: " + e.getMessage());
        }
    }

    @GetMapping("/generateTemplate")
    public void downloadTemplate(HttpServletResponse response) throws IOException {

        // 设置响应头
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("学生成绩", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        // 创建一个空的用户列表
        List<Mark> userList = new ArrayList<>();

        // 使用 EasyExcel 写入 Excel 文件并输出到响应流
        EasyExcel.write(response.getOutputStream(), Mark.class)
                .sheet("学生成绩")
                .doWrite(userList);
    }

    @GetMapping("/chartData")
    public Result  getChartData() {
        List<PieChartData> bing = markService.getBing();
        return Result.success(bing);
    }

}