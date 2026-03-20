package com.jmz.serveruser.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@TableName("tb_jmz_user_game_boosting")
@Data
public class UserGameBoosting {
    private Long userId;
    private Integer gameId;
    private Date createdAt;
    private Date updatedAt;
} 