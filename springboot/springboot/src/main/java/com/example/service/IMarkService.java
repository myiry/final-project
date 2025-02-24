package com.example.service;

import com.example.common.Result;
import com.example.entity.Mark;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.vo.MarkVo;
import com.example.entity.vo.PageVo;
import com.example.entity.vo.PieChartData;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2025-02-13
 */
public interface IMarkService extends IService<Mark> {

    PageInfo<Mark> marksList(MarkVo vo);

    void importMarks(MultipartFile file, String users);

    Result generateTemplate();

    List<PieChartData> getBing();
}
