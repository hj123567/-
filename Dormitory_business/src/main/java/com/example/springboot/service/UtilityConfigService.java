package com.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.UtilityConfig;
import com.example.springboot.mapper.UtilityConfigMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UtilityConfigService extends ServiceImpl<UtilityConfigMapper, UtilityConfig> {

    @Resource
    private UtilityConfigMapper utilityConfigMapper;

    public Page<UtilityConfig> findPage(Integer pageNum, Integer pageSize) {
        Page<UtilityConfig> page = new Page<>(pageNum, pageSize);
        QueryWrapper<UtilityConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("update_time");
        return utilityConfigMapper.selectPage(page, queryWrapper);
    }

    public UtilityConfig getDefaultConfig() {
        QueryWrapper<UtilityConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("dorm_build_id");
        queryWrapper.last("LIMIT 1");
        return utilityConfigMapper.selectOne(queryWrapper);
    }

    public UtilityConfig getConfigByBuildId(Integer dormBuildId) {
        QueryWrapper<UtilityConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dorm_build_id", dormBuildId);
        queryWrapper.last("LIMIT 1");
        UtilityConfig config = utilityConfigMapper.selectOne(queryWrapper);
        if (config == null) {
            return getDefaultConfig();
        }
        return config;
    }
}
