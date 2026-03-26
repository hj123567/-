package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.AdjustRoom;
import com.example.springboot.entity.DormRoom;
import com.example.springboot.mapper.AdjustRoomMapper;
import com.example.springboot.service.AdjustRoomService;
import com.example.springboot.service.DormRoomService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


@Service
public class AdjustRoomServiceImpl extends ServiceImpl<AdjustRoomMapper, AdjustRoom> implements AdjustRoomService {


    @Resource
    private AdjustRoomMapper adjustRoomMapper;

    @Resource
    private DormRoomService dormRoomService;

    /**
     * 添加调宿申请
     */
    @Override
    public int addApply(AdjustRoom adjustRoom) {
        int insert = adjustRoomMapper.insert(adjustRoom);
        return insert;
    }

    /**
     * 检查学生是否可以申请调宿
     * 返回null表示可以申请，返回错误信息表示不能申请
     */
    @Override
    public String canApply(String username) {
        QueryWrapper<AdjustRoom> qw = new QueryWrapper<>();
        qw.eq("username", username);
        qw.orderByDesc("apply_time");
        List<AdjustRoom> applications = adjustRoomMapper.selectList(qw);

        if (applications.isEmpty()) {
            return null;
        }

        AdjustRoom latestApplication = applications.get(0);

        if ("未处理".equals(latestApplication.getState())) {
            return "你已提交过调宿申请，请等待宿舍管理员处理后再申请";
        }

        if ("通过".equals(latestApplication.getState())) {
            String finishTimeStr = latestApplication.getFinishTime();
            if (finishTimeStr != null) {
                try {
                    LocalDateTime finishTime = LocalDateTime.parse(finishTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    LocalDateTime now = LocalDateTime.now();
                    long daysBetween = ChronoUnit.DAYS.between(finishTime, now);
                    if (daysBetween < 30) {
                        return "距离上次成功调宿未满30天，请" + (30 - daysBetween) + "天后再申请";
                    }
                } catch (Exception e) {
                    return null;
                }
            }
        }

        return null;
    }

    /**
     * 查找调宿申请
     */
    @Override
    public Page find(Integer pageNum, Integer pageSize, String search, Integer dormBuildId) {
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper<AdjustRoom> qw = new QueryWrapper<>();
        qw.like("username", search);
        qw.orderByDesc("apply_time");
        Page allPage = adjustRoomMapper.selectPage(page, qw);
        
        List<AdjustRoom> records = (List<AdjustRoom>) allPage.getRecords();
        
        if (dormBuildId != null) {
            List<AdjustRoom> filteredRecords = new ArrayList<>();
            for (AdjustRoom record : records) {
                DormRoom room = dormRoomService.getById(record.getCurrentRoomId());
                if (room != null && room.getDormBuildId() == dormBuildId) {
                    filteredRecords.add(record);
                }
            }
            records = filteredRecords;
        }
        
        List<AdjustRoom> uniqueRecords = new ArrayList<>();
        java.util.Set<String> seenUsernames = new java.util.HashSet<>();
        for (AdjustRoom record : records) {
            if (!seenUsernames.contains(record.getUsername())) {
                uniqueRecords.add(record);
                seenUsernames.add(record.getUsername());
            }
        }
        
        allPage.setRecords(uniqueRecords);
        allPage.setTotal(uniqueRecords.size());
        
        return allPage;
    }

    /**
     * 删除调宿申请
     */
    @Override
    public int deleteAdjustment(Integer id) {
        int i = adjustRoomMapper.deleteById(id);
        return i;
    }


    /**
     * 更新调宿申请
     */
    @Override
    public int updateApply(AdjustRoom adjustRoom) {
        int i = adjustRoomMapper.updateById(adjustRoom);
        return i;
    }

    /**
     * 学生端查询未处理的申请
     */
    @Override
    public Page findPending(Integer pageNum, Integer pageSize, String username) {
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper<AdjustRoom> qw = new QueryWrapper<>();
        qw.eq("username", username);
        qw.eq("state", "未处理");
        qw.orderByDesc("apply_time");
        return adjustRoomMapper.selectPage(page, qw);
    }

}
