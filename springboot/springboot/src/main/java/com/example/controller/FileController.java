package com.example.controller;

/**
 * @date 2024/1/25 17:51
 */

import cn.hutool.core.io.FileUtil;
import com.example.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * 文件上传下载功能
 * 读入和写出
 */

@RestController
@RequestMapping("/files")
public class FileController {

    // ip
   @Value("${ip}")
   private String ip;


   // 端口
   @Value("${server.port}")
   private String port;

    private static final String ROOT_PATH = System.getProperty("user.dir") + "/files"; // 当前的文件路径


    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();  // 获取到原始的文件名   aaa.png   aaa.png
        long flag = System.currentTimeMillis();  // 唯一标识
        String fileName = flag + "_" + originalFilename;    //  1561231313132_aaa.png

        File finalFile = new File(ROOT_PATH + "/" + fileName);  // 最终存到磁盘的文件对象
        if (!finalFile.getParentFile().exists()) {  // 如果父级目录不存在 就得创建
            finalFile.getParentFile().mkdirs();
        }
        file.transferTo(finalFile);
        // 返回文件的url
        /**
         * 首先在存入磁盘
         * 然后生成一个url给前端用
         */
//        String url = "http://localhost:9090/files/download?fileName=" + fileName;
        String url = "http://" + ip+":" + port + "/files/download?fileName=" + fileName;
        return Result.success(url);
    }


    /**
     * 文件下载
     */
    @GetMapping("/download")
    public void download(String fileName, HttpServletResponse response) throws IOException {
        File file = new File(ROOT_PATH + "/" + fileName);  // 文件在存盘存储的对象
        ServletOutputStream os = response.getOutputStream();
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        response.setContentType("application/octet-stream");
//        os.write(FileUtil.readBytes(file));
        FileUtil.writeToStream(file, os); // 写出
        os.flush();
        os.close();
    }
}
