package com.example.springboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Repair;
import com.example.springboot.service.RepairService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/repair")
public class RepairController {

    @Resource
    private RepairService repairService;

    /**
     * 检查同一寝室是否有未完成的报修
     */
    @GetMapping("/checkPending/{dormBuildId}/{dormRoomId}")
    public Result<?> checkPendingRepair(@PathVariable Integer dormBuildId,
                                          @PathVariable Integer dormRoomId) {
        boolean hasPending = repairService.hasPendingRepair(dormBuildId, dormRoomId);
        if (hasPending) {
            return Result.error("-1", "该寝室已有未完成的报修，请等待处理完成后再申请");
        } else {
            return Result.success();
        }
    }

    /**
     * 添加订单
     */
    @PostMapping("/add")
    public Result<?> add(@RequestBody Repair repair) {
        boolean hasPending = repairService.hasPendingRepair(repair.getDormBuildId(), repair.getDormRoomId());
        if (hasPending) {
            return Result.error("-1", "该寝室已有未完成的报修，请等待处理完成后再申请");
        }
        int i = repairService.addNewOrder(repair);
        if (i == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "添加失败");
        }
    }

    /**
     * 更新订单
     */
    @PutMapping("/update")
    public Result<?> update(@RequestBody Repair repair) {
        int i = repairService.updateNewOrder(repair);
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
        int i = repairService.deleteOrder(id);
        if (i == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "删除失败");
        }
    }

    /**
     * 查找订单
     */
    @GetMapping("/find")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search,
                              @RequestParam(required = false) Integer dormBuildId) {
        Page page = repairService.find(pageNum, pageSize, search, dormBuildId);
        if (page != null) {
            return Result.success(page);
        } else {
            return Result.error("-1", "查询失败");
        }
    }

    /**
     * 个人申报报修 分页查询（查询同一寝室的所有报修）
     */
    @GetMapping("/find/{name}")
    public Result<?> individualFind(@RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(defaultValue = "") String search,
                                    @PathVariable String name,
                                    @RequestParam(required = false) Integer dormBuildId,
                                    @RequestParam(required = false) Integer dormRoomId) {
        System.out.println(name);
        Page page = repairService.individualFind(pageNum, pageSize, search, name, dormBuildId, dormRoomId);
        if (page != null) {
            return Result.success(page);
        } else {
            return Result.error("-1", "查询失败");
        }
    }

    /**
     * 首页顶部：报修统计
     */
    @GetMapping("/orderNum")
    public Result<?> orderNum() {
        int num = repairService.showOrderNum();
        if (num >= 0) {
            return Result.success(num);
        } else {
            return Result.error("-1", "报修统计查询失败");
        }
    }
}
