package com.jmz.serverorder.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jmz.serverorder.dto.SystemCreateDTO;
import com.jmz.serverorder.dto.SystemUpdateDTO;
import com.jmz.serverorder.vo.SystemVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import com.jmz.serverorder.entity.Systems;

public interface SystemsService {
    IPage<SystemVO> listSystems(String name, Integer page, Integer pageSize);
    void createSystem(SystemCreateDTO dto, MultipartFile icon);
    void updateSystem(Integer id, SystemUpdateDTO dto, MultipartFile icon);
    void deleteSystem(Integer id);
    List<Systems> listByGameId(Integer gameId);
} 