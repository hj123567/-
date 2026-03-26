package com.example.springboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.UtilityConfig;
import com.example.springboot.service.UtilityConfigService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/utilityConfig")
public class UtilityConfigController {

    @Resource
    private UtilityConfigService utilityConfigService;

    @PostMapping("/add")
    public Result<?> add(@RequestBody UtilityConfig utilityConfig) {
        utilityConfigService.save(utilityConfig);
        return Result.success();
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody UtilityConfig utilityConfig) {
        utilityConfigService.updateById(utilityConfig);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        utilityConfigService.removeById(id);
        return Result.success();
    }

    @GetMapping("/find")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<UtilityConfig> page = utilityConfigService.findPage(pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/getDefault")
    public Result<?> getDefaultConfig() {
        UtilityConfig config = utilityConfigService.getDefaultConfig();
        return Result.success(config);
    }

    @GetMapping("/getByBuildId")
    public Result<?> getConfigByBuildId(@RequestParam Integer dormBuildId) {
        UtilityConfig config = utilityConfigService.getConfigByBuildId(dormBuildId);
        return Result.success(config);
    }
}
