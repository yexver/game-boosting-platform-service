package com.jmz.serverorder.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jmz.serverorder.dto.ServersCreateDTO;
import com.jmz.serverorder.dto.ServersQueryDTO;
import com.jmz.serverorder.dto.ServersUpdateDTO;
import com.jmz.serverorder.entity.Servers;
import com.jmz.serverorder.exception.exceptions.ServersException;
import com.jmz.serverorder.mapper.ServersMapper;
import com.jmz.serverorder.service.ServersService;
import com.jmz.serverorder.vo.ServersVO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static cn.hutool.poi.excel.cell.CellUtil.getCellValue;

@Service
public class ServersServiceImpl extends ServiceImpl<ServersMapper, Servers> implements ServersService {
    @Override
    public IPage<ServersVO> getServersList(ServersQueryDTO queryDTO) {
        Page<Servers> page = new Page<>(queryDTO.getPage(), queryDTO.getPageSize());
        LambdaQueryWrapper<Servers> wrapper = new LambdaQueryWrapper<>();
        if (queryDTO.getName() != null && !queryDTO.getName().isEmpty()) {
            wrapper.like(Servers::getName, queryDTO.getName());
        }
        if (queryDTO.getGameId() != null) {
            wrapper.eq(Servers::getGameId, queryDTO.getGameId());
        }
        if (queryDTO.getSystemId() != null) {
            wrapper.eq(Servers::getSystemId, queryDTO.getSystemId());
        }
        IPage<Servers> entityPage = this.page(page, wrapper);
        IPage<ServersVO> voPage = entityPage.convert(this::toVO);
        return voPage;
    }

    @Override
    public boolean createServers(ServersCreateDTO dto) {
        Servers servers = new Servers();
        BeanUtils.copyProperties(dto, servers);
        try {
            return this.save(servers);
        } catch (DuplicateKeyException e) {
            throw new ServersException("区服名称已存在，不能重复");
        }
    }

    @Override
    public boolean updateServers(ServersUpdateDTO dto) {
        Servers servers = new Servers();
        BeanUtils.copyProperties(dto, servers);
        try {
            return this.updateById(servers);
        } catch (DuplicateKeyException e) {
            throw new ServersException("区服名称已存在，不能重复");
        }
    }

    @Override
    public boolean deleteServers(Integer id) {
        return this.removeById(id);
    }

    @Override
    public void importServers(MultipartFile file) {
        System.out.println("开始导入区服");
        System.out.println("文件名: " + file.getOriginalFilename());
        System.out.println("文件大小: " + file.getSize() + " bytes");
        System.out.println("文件类型: " + file.getContentType());
        
        try (InputStream is = file.getInputStream()) {
            // 检查文件是否为空
            if (file.getSize() == 0) {
                throw new ServersException("上传的文件为空");
            }
            
            // 尝试不同的工作簿类型
            Workbook workbook = null;
            try {
                // 先尝试XLSX格式
                workbook = new XSSFWorkbook(is);
                System.out.println("使用XLSX格式解析");
            } catch (Exception e) {
                System.out.println("XLSX解析失败，尝试XLS格式: " + e.getMessage());
                // 重新获取输入流
                try (InputStream is2 = file.getInputStream()) {
                    workbook = new HSSFWorkbook(is2);
                    System.out.println("使用XLS格式解析");
                }
            }
            
            if (workbook == null) {
                throw new ServersException("无法解析Excel文件格式");
            }
            
            System.out.println("工作表数量: " + workbook.getNumberOfSheets());
            Sheet sheet = workbook.getSheetAt(0);
            System.out.println("工作表名称: " + sheet.getSheetName());
            System.out.println("第一行行号: " + sheet.getFirstRowNum());
            System.out.println("最后一行行号: " + sheet.getLastRowNum());
            System.out.println("物理行数: " + sheet.getPhysicalNumberOfRows());
            
            // 检查前几行数据
            for (int i = 0; i <= Math.min(3, sheet.getLastRowNum()); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    System.out.println("第" + i + "行数据:");
                    for (int j = 0; j < row.getLastCellNum(); j++) {
                        Cell cell = row.getCell(j);
                        if (cell != null) {
                            System.out.print("  列" + j + ": " + getCellValue(cell));
                        }
                    }
                    System.out.println();
                }
            }
            
            List<Servers> serversList = new ArrayList<>();
            
            // 从第1行开始读取数据（跳过表头第0行）
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                try {
                    Cell gameIdCell = row.getCell(0);
                    Cell systemIdCell = row.getCell(1);
                    Cell nameCell = row.getCell(2);
                    Cell sortOrderCell = row.getCell(3);
                    
                    if (gameIdCell == null || systemIdCell == null || nameCell == null) {
                        continue; // 跳过无效行
                    }
                    
                    Integer gameId = (int) gameIdCell.getNumericCellValue();
                    Integer systemId = (int) systemIdCell.getNumericCellValue();
                    String name = nameCell.getStringCellValue();
                    Integer sortOrder = sortOrderCell != null ? 
                        (int) sortOrderCell.getNumericCellValue() : 0;
                    
                    Servers servers = new Servers();
                    servers.setGameId(gameId);
                    servers.setSystemId(systemId);
                    servers.setName(name);
                    servers.setSortOrder(sortOrder);
                    servers.setCreatedAt(new Date());
                    servers.setUpdatedAt(new Date());
                    serversList.add(servers);
                    
                } catch (Exception e) {
                    System.out.println("跳过第" + (i+1) + "行，错误: " + e.getMessage());
                }
            }
            
            System.out.println("准备保存区服数量: " + serversList.size());
            
            // 批量保存到数据库
            if (!serversList.isEmpty()) {
                boolean result = this.saveBatch(serversList);
                System.out.println("批量保存结果: " + result);
                if (result) {
                    System.out.println("成功导入区服数量: " + serversList.size());
                } else {
                    throw new ServersException("批量保存失败");
                }
            }
            
            workbook.close();
        } catch (ServersException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServersException("区服批量导入失败: " + e.getMessage());
        }
    }

    @Override
    public List<Servers> listByGameIdAndSystemId(Integer gameId, Integer systemId) {
        LambdaQueryWrapper<Servers> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Servers::getGameId, gameId).eq(Servers::getSystemId, systemId);
        return this.list(wrapper);
    }

    private ServersVO toVO(Servers servers) {
        ServersVO vo = new ServersVO();
        BeanUtils.copyProperties(servers, vo);
        return vo;
    }

    // 内部类：用于 Excel 映射，使用表头名称
    public static class ServersExcelRow {
        @ExcelProperty("gameId")
        private Integer gameId;
        @ExcelProperty("systemId")
        private Integer systemId;
        @ExcelProperty("name")
        private String name;
        @ExcelProperty("sortOrder")
        private Integer sortOrder;
        
        // getter和setter方法保持不变
        public Integer getGameId() { return gameId; }
        public void setGameId(Integer gameId) { this.gameId = gameId; }
        public Integer getSystemId() { return systemId; }
        public void setSystemId(Integer systemId) { this.systemId = systemId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Integer getSortOrder() { return sortOrder; }
        public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    }
} 
