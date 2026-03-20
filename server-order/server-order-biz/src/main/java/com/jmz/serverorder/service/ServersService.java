package com.jmz.serverorder.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jmz.serverorder.dto.ServersCreateDTO;
import com.jmz.serverorder.dto.ServersQueryDTO;
import com.jmz.serverorder.dto.ServersUpdateDTO;
import com.jmz.serverorder.entity.Servers;
import com.jmz.serverorder.vo.ServersVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ServersService extends IService<Servers> {
    IPage<ServersVO> getServersList(ServersQueryDTO queryDTO);
    boolean createServers(ServersCreateDTO dto);
    boolean updateServers(ServersUpdateDTO dto);
    boolean deleteServers(Integer id);
    void importServers(MultipartFile file);
    List<Servers> listByGameIdAndSystemId(Integer gameId, Integer systemId);
} 