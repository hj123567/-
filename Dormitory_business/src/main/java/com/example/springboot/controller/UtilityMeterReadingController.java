package com.example.springboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.UtilityMeterReading;
import com.example.springboot.service.UtilityMeterReadingService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/utilityMeterReading")
public class UtilityMeterReadingController {

    @Resource
    private UtilityMeterReadingService utilityMeterReadingService;

    @PostMapping("/add")
    public Result<?> add(@RequestBody UtilityMeterReading reading) {
        UtilityMeterReading saved = utilityMeterReadingService.saveReading(reading);
        return Result.success(saved);
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody UtilityMeterReading reading) {
        UtilityMeterReading saved = utilityMeterReadingService.saveReading(reading);
        return Result.success(saved);
    }

    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        utilityMeterReadingService.removeById(id);
        return Result.success();
    }

    @GetMapping("/find")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(required = false) Integer dormBuildId,
                              @RequestParam(required = false) String billingMonth) {
        Page<UtilityMeterReading> page = utilityMeterReadingService.findPage(pageNum, pageSize, dormBuildId, billingMonth);
        return Result.success(page);
    }

    @GetMapping("/getLastReading")
    public Result<?> getLastReading(@RequestParam Integer dormBuildId,
                                     @RequestParam Integer dormRoomId) {
        UtilityMeterReading reading = utilityMeterReadingService.getLastReading(dormBuildId, dormRoomId);
        return Result.success(reading);
    }
}
