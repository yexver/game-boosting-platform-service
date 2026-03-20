package com.jmz.jmzcommonmybatisplus.dto;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class PageQuery <T>{
    private long size = 10L; // 每页显示条数
    private long current = 1L;// 当前页码
    private List<OrderItem> orders;// 排序字段信息
    public Page<T> toMpPage(){
        //根据当前分页条件，创建分页对象
        Page<T> page = new Page<>(current, size);
        if(orders != null){
            page.addOrder(orders);
        }
        return page;
    }

}
