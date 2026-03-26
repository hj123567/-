package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.DormScore;
import com.example.springboot.mapper.DormScoreMapper;
import com.example.springboot.service.DormScoreService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class DormScoreServiceImpl extends ServiceImpl<DormScoreMapper, DormScore> implements DormScoreService {

    @Resource
    private DormScoreMapper dormScoreMapper;

    @Override
    public int addNewScore(DormScore dormScore) {
        return dormScoreMapper.insert(dormScore);
    }

    @Override
    public Page find(Integer pageNum, Integer pageSize, String search, Integer dormBuildId) {
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper<DormScore> qw = new QueryWrapper<>();
        qw.like("dorm_room_id", search);
        if (dormBuildId != null) {
            qw.eq("dorm_build_id", dormBuildId);
        }
        // 只显示今天的评分记录
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        qw.likeRight("score_time", today);
        qw.orderByDesc("score_time");
        return dormScoreMapper.selectPage(page, qw);
    }

    @Override
    public Page findByRoom(Integer pageNum, Integer pageSize, Integer dormBuildId, Integer dormRoomId) {
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper<DormScore> qw = new QueryWrapper<>();
        if (dormBuildId != null) {
            qw.eq("dorm_build_id", dormBuildId);
        }
        if (dormRoomId != null) {
            qw.eq("dorm_room_id", dormRoomId);
        }
        qw.orderByDesc("score_time");
        return dormScoreMapper.selectPage(page, qw);
    }

    @Override
    public int updateNewScore(DormScore dormScore) {
        return dormScoreMapper.updateById(dormScore);
    }

    @Override
    public int deleteScore(Integer id) {
        return dormScoreMapper.deleteById(id);
    }

    @Override
    public boolean hasTodayScore(Integer dormBuildId, Integer dormRoomId) {
        QueryWrapper<DormScore> qw = new QueryWrapper<>();
        qw.eq("dorm_build_id", dormBuildId);
        qw.eq("dorm_room_id", dormRoomId);
        // 检查今天的评分记录
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        qw.likeRight("score_time", today);
        Long count = dormScoreMapper.selectCount(qw);
        return count != null && count > 0;
    }
}
