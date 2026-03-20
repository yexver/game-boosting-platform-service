package com.jmz.serverorder.controller;

import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.serverorder.dto.SystemCreateDTO;
import com.jmz.serverorder.dto.SystemUpdateDTO;
import com.jmz.serverorder.service.SystemsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/systems")
public class SystemsController {

    @Autowired
    private SystemsService systemsService;

    // 1. 获取系统列表
    @GetMapping
    public R list(
        @RequestParam(value = "name", required = false) String name,
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        return R.success(systemsService.listSystems(name, page, pageSize));
    }

    // 2. 新增系统
    @PostMapping
    public R create(
        @RequestPart("systemData") SystemCreateDTO systemData,
        @RequestPart(value = "icon", required = false) MultipartFile icon
    ) {
        systemsService.createSystem(systemData, icon);
        return R.success();
    }

    // 3. 更新系统
    @PutMapping("/{id}")
    public R update(
        @PathVariable Integer id,
        @RequestPart("systemData") SystemUpdateDTO systemData,
        @RequestPart(value = "icon", required = false) MultipartFile icon
    ) {
        systemsService.updateSystem(id, systemData, icon);
        return R.success();
    }

    // 4. 删除系统
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Integer id) {
        systemsService.deleteSystem(id);
        return R.success();
    }

    @GetMapping("/getAll")
    public R getAllSystems(@RequestParam("gameId") Integer gameId) {
        return R.success(systemsService.listByGameId(gameId));
    }
}
