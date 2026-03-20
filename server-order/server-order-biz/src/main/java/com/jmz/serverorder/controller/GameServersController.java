package com.jmz.serverorder.controller;


import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.serverorder.dto.ServersCreateDTO;
import com.jmz.serverorder.dto.ServersQueryDTO;
import com.jmz.serverorder.dto.ServersUpdateDTO;
import com.jmz.serverorder.service.ServersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/servers")
public class GameServersController {
    @Autowired
    private ServersService serversService;

    /**
     * 获取区服列表
     */
    @GetMapping
    public R getServersList(ServersQueryDTO queryDTO) {
        return R.success(serversService.getServersList(queryDTO));
    }

    /**
     * 新增区服
     */
    @PostMapping
    public R createServers(@Validated @RequestBody ServersCreateDTO dto) {
        return R.success(serversService.createServers(dto));
    }

    /**
     * 更新区服
     */
    @PutMapping("/{id}")
    public R updateServers(@PathVariable Integer id, @Validated @RequestBody ServersUpdateDTO dto) {
        dto.setId(id);
        return R.success(serversService.updateServers(dto));
    }

    /**
     * 删除区服
     */
    @DeleteMapping("/{id}")
    public R deleteServers(@PathVariable Integer id) {
        return R.success(serversService.deleteServers(id));
    }

    /**
     * 批量导入区服
     */
    @PostMapping("/import")
    public R importServers(@RequestParam("file") MultipartFile file) {
        serversService.importServers(file);
        return R.success();
    }

    @GetMapping("/getAll")
    public R getAllServers(@RequestParam("gameId") Integer gameId, @RequestParam("systemId") Integer systemId) {
        return R.success(serversService.listByGameIdAndSystemId(gameId, systemId));
    }
}
