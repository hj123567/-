package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Repair;
import com.example.springboot.mapper.RepairMapper;
import com.example.springboot.service.RepairService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;


@Service
public class RepairServiceImpl extends ServiceImpl<RepairMapper, Repair> implements RepairService {

    /**
     * 注入DAO层对象
     */
    @Resource
    private RepairMapper repairMapper;

    /**
     * 添加订单
     */
    @Override
    public int addNewOrder(Repair repair) {
        int insert = repairMapper.insert(repair);
        return insert;
    }

    /**
     * 查找订单
     */
    @Override
    public Page find(Integer pageNum, Integer pageSize, String search, Integer dormBuildId) {
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper<Repair> qw = new QueryWrapper<>();
        qw.like("title", search);
        qw.orderByDesc("order_buildtime");
        Page orderPage = repairMapper.selectPage(page, qw);
        return orderPage;
    }

    @Override
    public Page individualFind(Integer pageNum, Integer pageSize, String search, String name, Integer dormBuildId, Integer dormRoomId) {
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper<Repair> qw = new QueryWrapper<>();
        qw.like("title", search);
        
        if (dormBuildId != null && dormRoomId != null && dormBuildId > 0 && dormRoomId > 0) {
            qw.and(wrapper -> wrapper
                .eq("dormbuild_id", dormBuildId)
                .eq("dormroom_id", dormRoomId)
            );
        } else {
            qw.eq("repairer", name);
        }
        
        qw.orderByDesc("order_buildtime");
        Page orderPage = repairMapper.selectPage(page, qw);
        return orderPage;
    }

    /**
     * 更新订单
     */
    @Override
    public int updateNewOrder(Repair repair) {
        int i = repairMapper.updateById(repair);
        Assert.notNull(i, "保修单为空");
        return i;
    }

    /**
     * 删除订单
     */
    @Override
    public int deleteOrder(Integer id) {
        int i = repairMapper.deleteById(id);
        Assert.notNull(i, "保修单为空");
        return i;
    }

    /**
     * 首页顶部：报修统计
     */
    @Override
    public int showOrderNum() {
        QueryWrapper<Repair> qw = new QueryWrapper<>();
        int orderCount = Math.toIntExact(repairMapper.selectCount(qw));
        return orderCount;
    }

    /**
     * 检查同一寝室是否有未完成的报修
     */
    @Override
    public boolean hasPendingRepair(Integer dormBuildId, Integer dormRoomId) {
        QueryWrapper<Repair> qw = new QueryWrapper<>();
        
        if (dormBuildId != null && dormRoomId != null && dormBuildId > 0 && dormRoomId > 0) {
            qw.eq("dormbuild_id", dormBuildId);
            qw.eq("dormroom_id", dormRoomId);
        }
        
        qw.and(wrapper -> wrapper
            .eq("state", "待处理")
            .or()
            .eq("state", "处理中")
        );
        
        Long count = repairMapper.selectCount(qw);
        return count != null && count > 0;
    }
}
