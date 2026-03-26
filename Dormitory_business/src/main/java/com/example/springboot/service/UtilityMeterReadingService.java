package com.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.UtilityMeterReading;
import com.example.springboot.mapper.UtilityMeterReadingMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class UtilityMeterReadingService extends ServiceImpl<UtilityMeterReadingMapper, UtilityMeterReading> {

    @Resource
    private UtilityMeterReadingMapper utilityMeterReadingMapper;

    public Page<UtilityMeterReading> findPage(Integer pageNum, Integer pageSize, Integer dormBuildId, String billingMonth) {
        Page<UtilityMeterReading> page = new Page<>(pageNum, pageSize);
        QueryWrapper<UtilityMeterReading> queryWrapper = new QueryWrapper<>();
        if (dormBuildId != null) {
            queryWrapper.eq("dorm_build_id", dormBuildId);
        }
        if (billingMonth != null && !billingMonth.isEmpty()) {
            queryWrapper.eq("billing_month", billingMonth);
        }
        queryWrapper.orderByDesc("create_time");
        return utilityMeterReadingMapper.selectPage(page, queryWrapper);
    }

    public UtilityMeterReading getLastReading(Integer dormBuildId, Integer dormRoomId) {
        QueryWrapper<UtilityMeterReading> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dorm_build_id", dormBuildId);
        queryWrapper.eq("dorm_room_id", dormRoomId);
        queryWrapper.orderByDesc("billing_month");
        queryWrapper.last("LIMIT 1");
        return utilityMeterReadingMapper.selectOne(queryWrapper);
    }

    @Transactional
    public UtilityMeterReading saveReading(UtilityMeterReading reading) {
        UtilityMeterReading lastReading = getLastReading(reading.getDormBuildId(), reading.getDormRoomId());
        
        if (lastReading != null) {
            reading.setLastWaterReading(lastReading.getWaterReading());
            reading.setLastElectricityReading(lastReading.getElectricityReading());
            
            if (reading.getWaterReading() != null && lastReading.getWaterReading() != null) {
                reading.setWaterUsage(reading.getWaterReading().subtract(lastReading.getWaterReading()));
            }
            if (reading.getElectricityReading() != null && lastReading.getElectricityReading() != null) {
                reading.setElectricityUsage(reading.getElectricityReading().subtract(lastReading.getElectricityReading()));
            }
        } else {
            reading.setLastWaterReading(BigDecimal.ZERO);
            reading.setLastElectricityReading(BigDecimal.ZERO);
            if (reading.getWaterReading() != null) {
                reading.setWaterUsage(reading.getWaterReading());
            }
            if (reading.getElectricityReading() != null) {
                reading.setElectricityUsage(reading.getElectricityReading());
            }
        }
        
        if (reading.getId() == null) {
            utilityMeterReadingMapper.insert(reading);
        } else {
            utilityMeterReadingMapper.updateById(reading);
        }
        
        return reading;
    }
}
