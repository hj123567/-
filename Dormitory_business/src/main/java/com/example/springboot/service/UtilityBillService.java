package com.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.UtilityBill;
import com.example.springboot.entity.UtilityConfig;
import com.example.springboot.entity.UtilityMeterReading;
import com.example.springboot.mapper.UtilityBillMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class UtilityBillService extends ServiceImpl<UtilityBillMapper, UtilityBill> {

    @Resource
    private UtilityBillMapper utilityBillMapper;
    
    @Resource
    private UtilityConfigService utilityConfigService;
    
    @Resource
    private UtilityMeterReadingService utilityMeterReadingService;

    public Page<UtilityBill> findPage(Integer pageNum, Integer pageSize, Integer dormBuildId, String billingMonth, String paymentStatus) {
        Page<UtilityBill> page = new Page<>(pageNum, pageSize);
        QueryWrapper<UtilityBill> queryWrapper = new QueryWrapper<>();
        if (dormBuildId != null) {
            queryWrapper.eq("dorm_build_id", dormBuildId);
        }
        if (billingMonth != null && !billingMonth.isEmpty()) {
            queryWrapper.eq("billing_month", billingMonth);
        }
        if (paymentStatus != null && !paymentStatus.isEmpty()) {
            queryWrapper.eq("payment_status", paymentStatus);
        }
        queryWrapper.orderByDesc("create_time");
        return utilityBillMapper.selectPage(page, queryWrapper);
    }

    public Page<UtilityBill> findPageByRoom(Integer pageNum, Integer pageSize, Integer dormBuildId, Integer dormRoomId) {
        Page<UtilityBill> page = new Page<>(pageNum, pageSize);
        QueryWrapper<UtilityBill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dorm_build_id", dormBuildId);
        queryWrapper.eq("dorm_room_id", dormRoomId);
        queryWrapper.orderByDesc("billing_month");
        return utilityBillMapper.selectPage(page, queryWrapper);
    }

    @Transactional
    public UtilityBill generateBill(UtilityMeterReading reading) {
        QueryWrapper<UtilityBill> checkWrapper = new QueryWrapper<>();
        checkWrapper.eq("dorm_build_id", reading.getDormBuildId());
        checkWrapper.eq("dorm_room_id", reading.getDormRoomId());
        checkWrapper.eq("billing_month", reading.getBillingMonth());
        UtilityBill existingBill = utilityBillMapper.selectOne(checkWrapper);
        if (existingBill != null) {
            throw new RuntimeException(reading.getDormBuildId() + "号楼" + reading.getDormRoomId() + "号房 " + reading.getBillingMonth() + " 的账单已存在，不能重复生成");
        }
        
        UtilityConfig config = utilityConfigService.getConfigByBuildId(reading.getDormBuildId());
        
        UtilityBill bill = new UtilityBill();
        bill.setDormBuildId(reading.getDormBuildId());
        bill.setDormRoomId(reading.getDormRoomId());
        bill.setBillingMonth(reading.getBillingMonth());
        bill.setWaterUsage(reading.getWaterUsage());
        bill.setElectricityUsage(reading.getElectricityUsage());
        bill.setWaterPrice(config.getWaterPrice());
        bill.setElectricityPrice(config.getElectricityPrice());
        
        if (reading.getWaterUsage() != null) {
            bill.setWaterAmount(reading.getWaterUsage().multiply(config.getWaterPrice()));
        }
        if (reading.getElectricityUsage() != null) {
            bill.setElectricityAmount(reading.getElectricityUsage().multiply(config.getElectricityPrice()));
        }
        
        BigDecimal total = BigDecimal.ZERO;
        if (bill.getWaterAmount() != null) {
            total = total.add(bill.getWaterAmount());
        }
        if (bill.getElectricityAmount() != null) {
            total = total.add(bill.getElectricityAmount());
        }
        bill.setTotalAmount(total);
        bill.setPaymentStatus("未缴费");
        
        utilityBillMapper.insert(bill);
        return bill;
    }

    @Transactional
    public UtilityBill payBill(Integer billId, Integer payerId, String payerName) {
        UtilityBill bill = utilityBillMapper.selectById(billId);
        if (bill == null) {
            throw new RuntimeException("账单不存在");
        }
        if ("已缴费".equals(bill.getPaymentStatus())) {
            throw new RuntimeException("账单已缴费");
        }
        
        bill.setPaymentStatus("已缴费");
        bill.setPaymentTime(new Date());
        bill.setPayerId(payerId);
        utilityBillMapper.updateById(bill);
        
        return bill;
    }
}
