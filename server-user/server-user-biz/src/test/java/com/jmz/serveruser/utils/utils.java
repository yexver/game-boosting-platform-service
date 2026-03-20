package com.jmz.serveruser.utils;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.junit.jupiter.api.Test;

public class utils {
    //雪花算法获取id
    @Test
    public void getId() {
        System.out.println(IdWorker.getId());
    }
}
