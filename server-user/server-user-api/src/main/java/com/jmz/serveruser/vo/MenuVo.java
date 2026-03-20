package com.jmz.serveruser.vo;

import lombok.Data;
import java.util.List;

@Data
public class MenuVo {
    private Integer id;
    private String name;
    private Integer parentId;
    private List<MenuVo> children;
} 