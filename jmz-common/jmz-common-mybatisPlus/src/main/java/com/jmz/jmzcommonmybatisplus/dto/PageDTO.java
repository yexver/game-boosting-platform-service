package com.jmz.jmzcommonmybatisplus.dto;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class PageDTO<T>{
    private long pages;
    private long size; // 每页显示条数
    private long current;// 当前页码
    private long total;// 数据总量
    private List<T> records;// 当前页数据记录列表
    public static <PO,VO> PageDTO<VO> of(Page<PO>  page, Class<VO> clazz){
        PageDTO<VO> pageDTO = new PageDTO<>();
        //1.页数
        pageDTO.setPages(page.getPages());
        //2.分页大小
        pageDTO.setSize(page.getSize());
        //3.当前页码
        pageDTO.setCurrent(page.getCurrent());
        //4.数据总量
        pageDTO.setTotal(page.getTotal());
         List<PO> records = page.getRecords();
        if(CollectionUtils.isEmpty(records)){
            //设置空集合
            pageDTO.setRecords(Collections.emptyList());
            return pageDTO;
        }
        //5.数据转换
        pageDTO.setRecords(BeanUtil.copyToList(records,clazz));
        return pageDTO;
    }
    public static <PO,VO> PageDTO<VO> of(Page<PO>  page, Function<PO,VO> function){
        PageDTO<VO> pageDTO = new PageDTO<>();
        //1.页数
        pageDTO.setPages(page.getPages());
        //2.分页大小
        pageDTO.setSize(page.getSize());
        //3.当前页码
        pageDTO.setCurrent(page.getCurrent());
        //4.数据总量
        pageDTO.setTotal(page.getTotal());
        List<PO> records = page.getRecords();
        if(CollectionUtils.isEmpty(records)){
            //设置空集合
            pageDTO.setRecords(Collections.emptyList());
            return pageDTO;
        }

        pageDTO.setRecords(records.stream().map(function).collect(Collectors.toList()));
        return pageDTO;
    }
}
