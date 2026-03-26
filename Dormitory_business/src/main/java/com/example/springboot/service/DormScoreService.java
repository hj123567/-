package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.entity.DormScore;

public interface DormScoreService extends IService<DormScore> {
    int addNewScore(DormScore dormScore);
    Page find(Integer pageNum, Integer pageSize, String search, Integer dormBuildId);
    Page findByRoom(Integer pageNum, Integer pageSize, Integer dormBuildId, Integer dormRoomId);
    int updateNewScore(DormScore dormScore);
    int deleteScore(Integer id);
    boolean hasTodayScore(Integer dormBuildId, Integer dormRoomId);
}
