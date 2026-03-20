package com.jmz.serverorder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.jmzfile.feign.RemoteFileService;
import com.jmz.serverorder.dto.SystemCreateDTO;
import com.jmz.serverorder.dto.SystemUpdateDTO;
import com.jmz.serverorder.entity.Systems;
import com.jmz.serverorder.exception.exceptions.SystemException;
import com.jmz.serverorder.mapper.SystemsMapper;
import com.jmz.serverorder.service.SystemsService;
import com.jmz.serverorder.vo.SystemVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.jmz.serverorder.entity.Servers;
import com.jmz.serverorder.mapper.ServersMapper;
import com.jmz.serverorder.entity.GameSystems;
import com.jmz.serverorder.mapper.GameSystemsMapper;
import org.springframework.transaction.annotation.Transactional;


@Service
public class SystemsServiceImpl implements SystemsService {
    @Autowired
    private SystemsMapper systemsMapper;
    @Autowired
    private RemoteFileService remoteFileService;
    @Autowired
    private ServersMapper serversMapper;
    @Autowired
    private GameSystemsMapper gameSystemsMapper;

    @Override
    public IPage<SystemVO> listSystems(String name, Integer page, Integer pageSize) {
        LambdaQueryWrapper<Systems> query = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            query.like(Systems::getName, name);
        }
        query.orderByAsc(Systems::getSortOrder).orderByDesc(Systems::getCreatedAt);
        Page<Systems> p = new Page<>(page, pageSize);
        IPage<Systems> result = systemsMapper.selectPage(p, query);
        List<SystemVO> voList = result.getRecords().stream().map(this::toVO).collect(Collectors.toList());
        Page<SystemVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public void createSystem(SystemCreateDTO dto, MultipartFile icon) {
        Systems sys = new Systems();
        BeanUtils.copyProperties(dto, sys);
        sys.setCreatedAt(new Date());
        sys.setUpdatedAt(new Date());
        String iconKey = null;
        if (icon != null && !icon.isEmpty()) {
            try {
                R uploadResult = remoteFileService.upload(icon);
                if (uploadResult.isSuccess()) {
                    iconKey = (String) uploadResult.get(R.DATA_TAG);
                } else {
                    throw new SystemException("图标上传失败: " + uploadResult.get(R.MSG_TAG));
                }
            } catch (Exception e) {
                throw new SystemException("图标上传异常: " + e.getMessage());
            }
        }
        sys.setIcon(iconKey);
        systemsMapper.insert(sys);
    }

    @Override
    public void updateSystem(Integer id, SystemUpdateDTO dto, MultipartFile icon) {
        Systems sys = systemsMapper.selectById(id);
        if (sys == null) throw new RuntimeException("系统不存在");
        BeanUtils.copyProperties(dto, sys);
        sys.setUpdatedAt(new Date());
        String iconKey = null;
        if (icon != null && !icon.isEmpty()) {
            try {
                R uploadResult = remoteFileService.upload(icon);
                if (uploadResult.isSuccess()) {
                    iconKey = (String) uploadResult.get(R.DATA_TAG);
                } else {
                    throw new SystemException("图标上传失败: " + uploadResult.get(R.MSG_TAG));
                }
            } catch (Exception e) {
                throw new SystemException("图标上传异常: " + e.getMessage());
            }
        }
        sys.setIcon(iconKey);
        systemsMapper.updateById(sys);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSystem(Integer id) {
        // 1. 删除所有区服
        serversMapper.delete(new LambdaQueryWrapper<Servers>().eq(Servers::getSystemId, id));

        // 2. 删除所有多对多关联
        gameSystemsMapper.delete(new LambdaQueryWrapper<GameSystems>().eq(GameSystems::getSystemId, id));

        // 3. 删除系统本身
        systemsMapper.deleteById(id);
    }

    @Override
    public List<Systems> listByGameId(Integer gameId) {
        List<Integer> systemIds = gameSystemsMapper.selectList(new LambdaQueryWrapper<GameSystems>().eq(GameSystems::getGameId, gameId))
                .stream().map(GameSystems::getSystemId).toList();
        if (systemIds.isEmpty()) return List.of();
        return systemsMapper.selectBatchIds(systemIds);
    }

    private SystemVO toVO(Systems sys) {
        SystemVO vo = new SystemVO();
        BeanUtils.copyProperties(sys, vo);
        return vo;
    }
} 