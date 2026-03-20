package com.jmz.serveruser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("tb_jmz_user_accounts")
public class UserAccount {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    private BigDecimal balance;//账户余额
    private BigDecimal frozenAmount;//账户冻结金额
    private BigDecimal totalIncome;//账户总收入
    private BigDecimal totalExpense;//账户总支出
    private Date createdAt;
    private Date updatedAt;
} 