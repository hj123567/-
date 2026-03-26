package com.example.springboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.AdjustRoom;
import com.example.springboot.service.AdjustRoomService;
import com.example.springboot.service.DormRoomService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/adjustRoom")
public class AdjustRoomController {

    @Resource
    private AdjustRoomService adjustRoomService;

    @Resource
    private DormRoomService dormRoomService;


    /**
     * 检查是否可以申请调宿
     */
    @GetMapping("/canApply/{username}")
    public Result<?> canApply(@PathVariable String username) {
        String errorMsg = adjustRoomService.canApply(username);
        if (errorMsg == null) {
            return Result.success();
        } else {
            return Result.error("-1", errorMsg);
        }
    }

    /**
     * 添加订单
     */
    @PostMapping("/add")
    public Result<?> add(@RequestBody AdjustRoom adjustRoom) {
        String errorMsg = adjustRoomService.canApply(adjustRoom.getUsername());
        if (errorMsg != null) {
            return Result.error("-1", errorMsg);
        }
        int result = adjustRoomService.addApply(adjustRoom);
        if (result == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "查询失败");
        }
    }


    /**
     * 更新订单
     */
    @PutMapping("/update/{state}")
    public Result<?> update(@RequestBody AdjustRoom adjustRoom, @PathVariable Boolean state) {
        if (state) {
            // 更新房间表信息
            int i = dormRoomService.adjustRoomUpdate(adjustRoom);
            if (i == -1) {
                return Result.error("-1", "重复操作");
            } else if (i == -2) {
                return Result.error("-1", "原床位学生信息不存在");
            } else if (i == -3) {
                return Result.error("-1", "目标床位已被占用，请选择其他床位");
            }
        }
        //更新调宿表信息
        int i = adjustRoomService.updateApply(adjustRoom);
        if (i == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "更新失败");
        }
    }

    /**
     * 删除订单
     */
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        int i = adjustRoomService.deleteAdjustment(id);
        if (i == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "删除失败");
        }
    }

    /**
     * 查找订单（管理员端）
     */
    @GetMapping("/find")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search,
                              @RequestParam(required = false) Integer dormBuildId) {
        Page page = adjustRoomService.find(pageNum, pageSize, search, dormBuildId);
        if (page != null) {
            return Result.success(page);
        } else {
            return Result.error("-1", "查询失败");
        }
    }

    /**
     * 学生端查找未处理的申请
     */
    @GetMapping("/findPending/{username}")
    public Result<?> findPending(@PathVariable String username,
                                  @RequestParam(defaultValue = "1") Integer pageNum,
                                  @RequestParam(defaultValue = "10") Integer pageSize) {
        Page page = adjustRoomService.findPending(pageNum, pageSize, username);
        if (page != null) {
            return Result.success(page);
        } else {
            return Result.error("-1", "查询失败");
        }
    }
}
