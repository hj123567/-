package com.example.springboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.UtilityBill;
import com.example.springboot.entity.UtilityMeterReading;
import com.example.springboot.service.UtilityBillService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/utilityBill")
public class UtilityBillController {

    @Resource
    private UtilityBillService utilityBillService;

    @PostMapping("/generate")
    public Result<?> generateBill(@RequestBody UtilityMeterReading reading) {
        UtilityBill bill = utilityBillService.generateBill(reading);
        return Result.success(bill);
    }

    @PostMapping("/pay")
    public Result<?> payBill(@RequestParam Integer billId,
                             @RequestParam Integer payerId,
                             @RequestParam String payerName) {
        try {
            UtilityBill bill = utilityBillService.payBill(billId, payerId, payerName);
            return Result.success(bill);
        } catch (Exception e) {
            return Result.error("-1", e.getMessage());
        }
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody UtilityBill utilityBill) {
        utilityBillService.updateById(utilityBill);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        utilityBillService.removeById(id);
        return Result.success();
    }

    @GetMapping("/find")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(required = false) Integer dormBuildId,
                              @RequestParam(required = false) String billingMonth,
                              @RequestParam(required = false) String paymentStatus) {
        Page<UtilityBill> page = utilityBillService.findPage(pageNum, pageSize, dormBuildId, billingMonth, paymentStatus);
        return Result.success(page);
    }

    @GetMapping("/findByRoom")
    public Result<?> findPageByRoom(@RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam Integer dormBuildId,
                                    @RequestParam Integer dormRoomId) {
        Page<UtilityBill> page = utilityBillService.findPageByRoom(pageNum, pageSize, dormBuildId, dormRoomId);
        return Result.success(page);
    }
}
