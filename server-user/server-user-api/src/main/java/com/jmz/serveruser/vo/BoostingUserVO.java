package com.jmz.serveruser.vo;

import lombok.Data;

@Data
public class BoostingUserVO {
    private Long userId;
    private String username;
    private String avatar;
    private Integer orderCount30d; // 近30天接单量
    private Integer finishCount30d; // 近30天完单量
    private Double managerRate30d; // 近30天客服介入率
} 