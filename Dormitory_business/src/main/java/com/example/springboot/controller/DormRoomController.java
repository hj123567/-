package com.example.springboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.DormRoom;
import com.example.springboot.service.DormRoomService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@RestController
@RequestMapping("/room")
public class DormRoomController {

    @Resource
    private DormRoomService dormRoomService;

    /**
     * 添加房间
     */
    @PostMapping("/add")
    public Result<?> add(@RequestBody DormRoom dormRoom) {
        int i = dormRoomService.addNewRoom(dormRoom);
        if (i == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "添加失败");
        }
    }

    /**
     * 更新房间
     */
    @PutMapping("/update")
    public Result<?> update(@RequestBody DormRoom dormRoom) {
        int i = dormRoomService.updateNewRoom(dormRoom);
        if (i == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "更新失败");
        }
    }

    /**
     * 删除房间
     */
    @DeleteMapping("/delete/{dormRoomId}")
    public Result<?> delete(@PathVariable Integer dormRoomId) {
        int i = dormRoomService.deleteRoom(dormRoomId);
        if (i == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "删除失败");
        }
    }

    /**
     * 查找房间
     */
    @GetMapping("/find")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search,
                              @RequestParam(required = false) Integer dormBuildId) {
        Page page = dormRoomService.find(pageNum, pageSize, search, dormBuildId);
        if (page != null) {
            return Result.success(page);
        } else {
            return Result.error("-1", "查询失败");
        }
    }

    /**
     * 首页顶部：空宿舍统计
     */
    @GetMapping("/noFullRoom")
    public Result<?> noFullRoom() {
        int num = dormRoomService.notFullRoom();
        if (num >= 0) {
            return Result.success(num);
        } else {
            return Result.error("-1", "空宿舍查询失败");
        }
    }

    /**
     * 删除床位学生信息
     */
    @DeleteMapping("/delete/{bedName}/{dormRoomId}/{calCurrentNum}")
    public Result<?> deleteBedInfo(@PathVariable String bedName, @PathVariable Integer dormRoomId, @PathVariable int calCurrentNum) {
        int i = dormRoomService.deleteBedInfo(bedName, dormRoomId, calCurrentNum);
        if (i == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "删除失败");
        }
    }

    /**
     * 床位信息，查询该学生是否已有床位
     */
    @GetMapping("/judgeHadBed/{value}")
    public Result<?> judgeHadBed(@PathVariable String value) {
        DormRoom dormRoom = dormRoomService.judgeHadBed(value);
        if (dormRoom == null) {
            return Result.success();
        } else {
            return Result.error("-1", "该学生已有宿舍");
        }
    }

    /**
     * 主页 住宿人数
     */
    @GetMapping("/selectHaveRoomStuNum")
    public Result<?> selectHaveRoomStuNum() {
        Long count = dormRoomService.selectHaveRoomStuNum();
        if (count >= 0) {
            return Result.success(count);
        } else {
            return Result.error("-1", "查询首页住宿人数失败");
        }
    }

    /**
     * 住宿分布人数
     */
    @GetMapping("/getEachBuildingStuNum/{num}")
    public Result<?> getEachBuildingStuNum(@PathVariable int num) {
        ArrayList<Long> arrayList = new ArrayList();
        for (int i = 1; i <= num; i++) {
            Long eachBuildingStuNum = dormRoomService.getEachBuildingStuNum(i);
            arrayList.add(eachBuildingStuNum);
        }

        if (!arrayList.isEmpty()) {
            return Result.success(arrayList);
        } else {
            return Result.error("-1", "获取人数失败");
        }
    }

    /**
     * 学生功能： 我的宿舍
     */
    @GetMapping("/getMyRoom/{name}")
    public Result<?> getMyRoom(@PathVariable String name) {
        DormRoom dormRoom = dormRoomService.judgeHadBed(name);
        if (dormRoom != null) {
            return Result.success(dormRoom);
        } else {
            return Result.error("-1", "不存在该生");
        }
    }

    /**
     * 检查房间是否满员
     */
    @GetMapping("/checkRoomState/{dormRoomId}")
    public Result<?> checkRoomState(@PathVariable Integer dormRoomId) {
        DormRoom dormRoom = dormRoomService.checkRoomState(dormRoomId);
        if (dormRoom != null) {
            return Result.success(dormRoom);
        } else {
            return Result.error("-1", "该房间人满了");
        }
    }

    /**
     * 检查床位是否已经有人
     */
    @GetMapping("/checkBedState/{dormRoomId}/{bedNum}")
    public Result<?> getMyRoom(@PathVariable Integer dormRoomId, @PathVariable int bedNum) {
        DormRoom dormRoom = dormRoomService.checkBedState(dormRoomId, bedNum);
        if (dormRoom != null) {
            return Result.success(dormRoom);
        } else {
            return Result.error("-1", "该床位已有人");
        }
    }
    


    /**
     * 检查房间是否满员
     */
    @GetMapping("/checkRoomExist/{dormRoomId}")
    public Result<?> checkRoomExist(@PathVariable Integer dormRoomId) {
        DormRoom dormRoom = dormRoomService.checkRoomExist(dormRoomId);
        if (dormRoom != null) {
            return Result.success(dormRoom);
        } else {
            return Result.error("-1", "不存在该房间");
        }
    }

    /**
     * 查询指定房间的空床位
     */
    @GetMapping("/getEmptyBeds/{dormRoomId}")
    public Result<?> getEmptyBeds(@PathVariable Integer dormRoomId) {
        List<Map<String, Object>> emptyBeds = dormRoomService.getEmptyBeds(dormRoomId);
        if (emptyBeds != null) {
            return Result.success(emptyBeds);
        } else {
            return Result.error("-1", "查询失败");
        }
    }

    /**
     * 查询所有可换宿舍的空床位（宿管管理的楼栋）
     */
    @GetMapping("/getAllEmptyBeds")
    public Result<?> getAllEmptyBeds(@RequestParam(required = false) Integer dormBuildId,
                                      @RequestParam(required = false) Integer currentRoomId) {
        List<Map<String, Object>> emptyBeds;
        if (currentRoomId != null) {
            // 排除当前房间
            emptyBeds = dormRoomService.getAllEmptyBedsExcludeCurrent(dormBuildId, currentRoomId);
        } else {
            // 不排除
            emptyBeds = dormRoomService.getAllEmptyBeds(dormBuildId);
        }
        if (emptyBeds != null) {
            return Result.success(emptyBeds);
        } else {
            return Result.error("-1", "查询失败");
        }
    }

    /**
     * 同步床位数据：根据学生表的宿舍信息重新分配房间表的床位
     */
    @GetMapping("/syncBedData")
    public Result<?> syncBedData() {
        int result = dormRoomService.syncBedData();
        if (result >= 0) {
            return Result.success("同步成功，共处理 " + result + " 个学生");
        } else {
            return Result.error("-1", "同步失败");
        }
    }

    /**
     * 智能重新分配宿舍：确保每个宿舍最多4人，男女分开
     */
    @GetMapping("/smartAssignDorm")
    public Result<?> smartAssignDorm() {
        String result = dormRoomService.smartAssignDorm();
        return Result.success(result);
    }

    /**
     * 获取所有有空床位的房间列表（用于新生分配）
     */
    @GetMapping("/getRoomsWithEmptyBeds")
    public Result<?> getRoomsWithEmptyBeds(@RequestParam(required = false) Integer dormBuildId) {
        List<Map<String, Object>> rooms = dormRoomService.getRoomsWithEmptyBeds(dormBuildId);
        if (rooms != null) {
            return Result.success(rooms);
        } else {
            return Result.error("-1", "查询失败");
        }
    }

    /**
     * 为学生分配床位
     */
    @PostMapping("/assignBedToStudent")
    public Result<?> assignBedToStudent(@RequestParam String username,
                                        @RequestParam Integer dormBuildId,
                                        @RequestParam Integer dormRoomId,
                                        @RequestParam Integer bedNum) {
        int result = dormRoomService.assignBedToStudent(username, dormBuildId, dormRoomId, bedNum);
        if (result == 1) {
            return Result.success();
        } else if (result == -1) {
            return Result.error("-1", "房间不存在");
        } else if (result == -2) {
            return Result.error("-1", "床位号不正确");
        } else if (result == -3) {
            return Result.error("-1", "该床位已被占用");
        } else {
            return Result.error("-1", "分配失败");
        }
    }
}
