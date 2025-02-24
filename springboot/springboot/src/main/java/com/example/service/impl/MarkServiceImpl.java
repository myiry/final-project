package com.example.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.MarkDataListener;
import com.example.common.Result; // 假设 Result 类在 com.example.common 包下
import com.example.entity.Mark;
import com.example.entity.vo.MarkVo;
import com.example.entity.vo.PieChartData;
import com.example.mapper.MarkMapper;
import com.example.service.IMarkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-02-13
 */
@Service
public class MarkServiceImpl extends ServiceImpl<MarkMapper, Mark> implements IMarkService {
    private static final Logger logger = LoggerFactory.getLogger(MarkServiceImpl.class);

    @Override
    public PageInfo<Mark> marksList(MarkVo vo) {
        PageHelper.startPage(vo.getPage(), vo.getPageSize());
        List<Mark> list = baseMapper.selectList(new LambdaQueryWrapper<Mark>()
                .eq(ObjectUtil.isNotEmpty(vo.getMoudleName()), Mark::getModule, vo.getMoudleName())
                .orderByDesc(Mark::getCreateTime));
        return new PageInfo<>(list);
    }

    @Override
    public void importMarks(MultipartFile file,String userId) {
        try (InputStream inputStream = file.getInputStream()) {
            List<Mark> marks = EasyExcel.read(inputStream, Mark.class, new MarkDataListener()).sheet().doReadSync();
            marks.forEach(mark -> {
                mark.setUserId(Long.valueOf(userId));
                mark.setCreateTime(new Date());
            });
            saveBatch(marks);
        } catch (IOException e) {
            logger.error("Failed to import marks", e);
        }
    }

    @Override
    public Result generateTemplate() {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // 创建 WriteSheet 对象
            WriteSheet writeSheet = EasyExcel.writerSheet("Marks Template").head(Mark.class).build();

            // 使用 writeSheet 写入数据
//            EasyExcel.write(outputStream, Mark.class).sheet(writeSheet).doWrite(new ArrayList<>());

            byte[] bytes = outputStream.toByteArray();
            InputStream inputStream = new ByteArrayInputStream(bytes);
            return Result.success(inputStream);
        } catch (IOException e) {
            logger.error("Failed to generate template", e);
            return Result.error("生成模板失败: " + e.getMessage());
        }
    }

    @Override
    public List<PieChartData> getBing() {

        List<PieChartData> data = new ArrayList<>();
        List<Mark> marks = baseMapper.selectList(new LambdaQueryWrapper<Mark>());
        for (Mark mark : marks) {
            PieChartData pieChartData = new PieChartData();
            pieChartData.setValue(mark.getScore());
            pieChartData.setName(mark.getModule());
            data.add(pieChartData);
        }
        return data;
    }

}