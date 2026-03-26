package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.entity.AdjustRoom;

public interface AdjustRoomService extends IService<AdjustRoom> {

    //查询调宿申请
    Page find(Integer pageNum, Integer pageSize, String search, Integer dormBuildId);

    //删除调宿申请
    int deleteAdjustment(Integer id);

    //更新
    int updateApply(AdjustRoom adjustRoom);

    // 添加
    int addApply(AdjustRoom adjustRoom);

    // 检查学生是否可以申请调宿
    String canApply(String username);

    // 学生端查询未处理的申请
    Page findPending(Integer pageNum, Integer pageSize, String username);

}
