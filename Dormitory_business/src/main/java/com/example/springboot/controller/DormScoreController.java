package com.example.springboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.DormScore;
import com.example.springboot.service.DormScoreService;
import com.example.springboot.service.DormRoomService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/dormScore")
public class DormScoreController {

    @Resource
    private DormScoreService dormScoreService;
    
    @Resource
    private DormRoomService dormRoomService;

    @PostMapping("/add")
    public Result<?> add(@RequestBody DormScore dormScore,
                        @RequestParam(required = false) Integer managerDormBuildId) {
        System.out.println("=== 接收到添加请求 ===");
        System.out.println("dormBuildId: " + dormScore.getDormBuildId());
        System.out.println("dormRoomId: " + dormScore.getDormRoomId());
        System.out.println("score: " + dormScore.getScore());
        System.out.println("comment: " + dormScore.getComment());
        System.out.println("scorer: " + dormScore.getScorer());
        System.out.println("scoreTime: " + dormScore.getScoreTime());
        System.out.println("managerDormBuildId: " + managerDormBuildId);
        
        // 数据验证
        if (dormScore.getDormBuildId() == null || dormScore.getDormBuildId() <= 0) {
            System.out.println("验证失败：楼栋号为空");
            return Result.error("-1", "楼栋号不能为空");
        }
        if (dormScore.getDormRoomId() == null || dormScore.getDormRoomId() <= 0) {
            System.out.println("验证失败：房间号为空");
            return Result.error("-1", "房间号不能为空");
        }
        // 验证房间是否存在
        Object room = dormRoomService.getById(dormScore.getDormRoomId());
        if (room == null) {
            System.out.println("验证失败：房间不存在，房间 ID: " + dormScore.getDormRoomId());
            return Result.error("-1", "房间不存在，请选择正确的房间");
        }
        if (dormScore.getScore() == null || dormScore.getScore() < 0 || dormScore.getScore() > 100) {
            System.out.println("验证失败：评分不合法");
            return Result.error("-1", "评分必须在 0-100 之间");
        }
        
        // 权限验证
        if (managerDormBuildId != null && !managerDormBuildId.equals(dormScore.getDormBuildId())) {
            System.out.println("权限验证失败");
            return Result.error("-1", "您只能管理自己楼栋的评分！");
        }
        
        // 检查今天是否已经对该房间评过分
        boolean hasTodayScore = dormScoreService.hasTodayScore(
            dormScore.getDormBuildId(), 
            dormScore.getDormRoomId()
        );
        if (hasTodayScore) {
            return Result.error("-1", "该房间今天已经评过分了！每天只能评分一次");
        }
        
        try {
            System.out.println("开始调用 service 添加数据...");
            int result = dormScoreService.addNewScore(dormScore);
            System.out.println("添加结果：" + result);
            if (result == 1) {
                return Result.success();
            } else {
                return Result.error("-1", "添加失败");
            }
        } catch (Exception e) {
            System.out.println("添加数据时发生异常：" + e.getMessage());
            e.printStackTrace();
            return Result.error("-1", "添加失败：" + e.getMessage());
        }
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody DormScore dormScore,
                           @RequestParam(required = false) Integer managerDormBuildId) {
        if (managerDormBuildId != null) {
            DormScore oldScore = dormScoreService.getById(dormScore.getId());
            if (oldScore != null && !managerDormBuildId.equals(oldScore.getDormBuildId())) {
                return Result.error("-1", "您只能管理自己楼栋的评分！");
            }
            if (!managerDormBuildId.equals(dormScore.getDormBuildId())) {
                return Result.error("-1", "您不能修改评分的楼栋号！");
            }
        }
        // 验证房间是否存在
        if (dormScore.getDormRoomId() != null) {
            Object room = dormRoomService.getById(dormScore.getDormRoomId());
            if (room == null) {
                return Result.error("-1", "房间不存在，请选择正确的房间");
            }
        }
        int result = dormScoreService.updateNewScore(dormScore);
        if (result == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "更新失败");
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Integer id,
                           @RequestParam(required = false) Integer managerDormBuildId) {
        if (managerDormBuildId != null) {
            DormScore oldScore = dormScoreService.getById(id);
            if (oldScore != null && !managerDormBuildId.equals(oldScore.getDormBuildId())) {
                return Result.error("-1", "您只能删除自己楼栋的评分！");
            }
        }
        int result = dormScoreService.deleteScore(id);
        if (result == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "删除失败");
        }
    }

    @GetMapping("/find")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search,
                              @RequestParam(required = false) Integer dormBuildId) {
        Page page = dormScoreService.find(pageNum, pageSize, search, dormBuildId);
        return Result.success(page);
    }

    @GetMapping("/findByRoom")
    public Result<?> findByRoom(@RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "10") Integer pageSize,
                               @RequestParam(required = false) Integer dormBuildId,
                               @RequestParam(required = false) Integer dormRoomId) {
        Page page = dormScoreService.findByRoom(pageNum, pageSize, dormBuildId, dormRoomId);
        return Result.success(page);
    }

    /**
     * 快速添加测试评分数据
     */
    @GetMapping("/addTestData")
    public Result<?> addTestData() {
        DormScore score1 = new DormScore();
        score1.setDormBuildId(1);
        score1.setDormRoomId(1104);
        score1.setScore(85);
        score1.setComment("整体整洁，桌面物品摆放整齐");
        score1.setScoreTime("2026-03-25 20:30:00");
        score1.setScorer("张宿管");
        dormScoreService.addNewScore(score1);

        DormScore score2 = new DormScore();
        score2.setDormBuildId(1);
        score2.setDormRoomId(1104);
        score2.setScore(92);
        score2.setComment("非常干净，被子叠放整齐");
        score2.setScoreTime("2026-03-24 19:00:00");
        score2.setScorer("李宿管");
        dormScoreService.addNewScore(score2);

        return Result.success("测试数据添加成功！已添加2条评分记录到1号楼1104房间");
    }
}
