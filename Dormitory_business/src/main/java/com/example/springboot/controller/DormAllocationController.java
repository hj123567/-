package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import com.example.springboot.entity.AdjustRoom;
import com.example.springboot.entity.DormRoom;
import com.example.springboot.entity.Student;
import com.example.springboot.service.AdjustRoomService;
import com.example.springboot.service.DormRoomService;
import com.example.springboot.service.StudentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/dormAllocation")
public class DormAllocationController {

    @Resource
    private AdjustRoomService adjustRoomService;

    @Resource
    private DormRoomService dormRoomService;

    @Resource
    private StudentService studentService;

    @GetMapping("/getPendingApplications")
    public Result<?> getPendingApplications(@RequestParam(required = false) Integer dormBuildId) {
        QueryWrapper<AdjustRoom> qw = new QueryWrapper<>();
        qw.eq("state", "未处理");
        qw.orderByDesc("apply_time");
        if (dormBuildId != null) {
            List<AdjustRoom> allApplications = adjustRoomService.list(qw);
            List<AdjustRoom> filteredApplications = new ArrayList<>();
            Set<String> seenUsernames = new HashSet<>();
            for (AdjustRoom app : allApplications) {
                DormRoom room = dormRoomService.getById(app.getCurrentRoomId());
                if (room != null && room.getDormBuildId() == dormBuildId) {
                    if (!seenUsernames.contains(app.getUsername())) {
                        filteredApplications.add(app);
                        seenUsernames.add(app.getUsername());
                    }
                }
            }
            return Result.success(filteredApplications);
        }
        List<AdjustRoom> allApplications = adjustRoomService.list(qw);
        List<AdjustRoom> uniqueApplications = new ArrayList<>();
        Set<String> seenUsernames = new HashSet<>();
        for (AdjustRoom app : allApplications) {
            if (!seenUsernames.contains(app.getUsername())) {
                uniqueApplications.add(app);
                seenUsernames.add(app.getUsername());
            }
        }
        return Result.success(uniqueApplications);
    }

    @GetMapping("/getAvailableRooms")
    public Result<?> getAvailableRooms(@RequestParam(required = false) Integer dormBuildId) {
        QueryWrapper<DormRoom> qw = new QueryWrapper<>();
        qw.lt("current_capacity", 4);
        if (dormBuildId != null) {
            qw.eq("dormbuild_id", dormBuildId);
        }
        List<DormRoom> rooms = dormRoomService.list(qw);
        
        List<Map<String, Object>> roomInfoList = new ArrayList<>();
        for (DormRoom room : rooms) {
            Map<String, Object> roomInfo = new HashMap<>();
            roomInfo.put("dormRoomId", room.getDormRoomId());
            roomInfo.put("dormBuildId", room.getDormBuildId());
            roomInfo.put("currentCapacity", room.getCurrentCapacity());
            roomInfo.put("maxCapacity", room.getMaxCapacity());
            roomInfo.put("availableBeds", getAvailableBeds(room));
            roomInfoList.add(roomInfo);
        }
        
        return Result.success(roomInfoList);
    }

    private List<Integer> getAvailableBeds(DormRoom room) {
        List<Integer> availableBeds = new ArrayList<>();
        if (room.getFirstBed() == null) availableBeds.add(1);
        if (room.getSecondBed() == null) availableBeds.add(2);
        if (room.getThirdBed() == null) availableBeds.add(3);
        if (room.getFourthBed() == null) availableBeds.add(4);
        return availableBeds;
    }

    @PostMapping("/allocateToEmptyBed")
    public Result<?> allocateToEmptyBed(@RequestBody Map<String, Object> request) {
        Integer applicationId = (Integer) request.get("applicationId");
        Integer targetRoomId = (Integer) request.get("targetRoomId");
        Integer targetBedId = (Integer) request.get("targetBedId");

        AdjustRoom application = adjustRoomService.getById(applicationId);
        if (application == null) {
            return Result.error("-1", "申请不存在");
        }

        DormRoom targetRoom = dormRoomService.getById(targetRoomId);
        if (targetRoom == null) {
            return Result.error("-1", "目标房间不存在");
        }

        String bedName = getBedName(targetBedId);
        if (!isBedEmpty(targetRoom, bedName)) {
            return Result.error("-1", "目标床位已被占用");
        }

        Student student = studentService.getById(application.getUsername());
        if (student == null) {
            return Result.error("-1", "学生不存在");
        }

        if (student.getGender().equals("男") && targetRoom.getDormBuildId() % 2 == 0) {
            return Result.error("-1", "男生只能住在单数宿舍楼");
        }
        if (student.getGender().equals("女") && targetRoom.getDormBuildId() % 2 == 1) {
            return Result.error("-1", "女生只能住在双数宿舍楼");
        }

        DormRoom currentRoom = dormRoomService.judgeHadBed(application.getUsername());
        if (currentRoom != null) {
            String currentBedName = getBedName(application.getCurrentBedId());
            dormRoomService.deleteBedInfo(currentBedName, currentRoom.getDormRoomId(), currentRoom.getCurrentCapacity());
        }

        com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<DormRoom> uw = new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<>();
        uw.eq("dormroom_id", targetRoomId);
        uw.set(bedName, application.getUsername());
        uw.set("current_capacity", targetRoom.getCurrentCapacity() + 1);
        dormRoomService.update(null, uw);

        student.setDormBuildId(targetRoom.getDormBuildId());
        student.setDormRoomId(targetRoomId);
        studentService.updateById(student);

        application.setTowardsRoomId(targetRoomId);
        application.setTowardsBedId(targetBedId);
        application.setState("通过");
        application.setFinishTime(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        adjustRoomService.updateById(application);

        return Result.success();
    }

    @PostMapping("/swapStudents")
    public Result<?> swapStudents(@RequestBody Map<String, Object> request) {
        Integer applicationId1 = (Integer) request.get("applicationId1");
        Integer applicationId2 = (Integer) request.get("applicationId2");

        AdjustRoom application1 = adjustRoomService.getById(applicationId1);
        AdjustRoom application2 = adjustRoomService.getById(applicationId2);

        if (application1 == null || application2 == null) {
            return Result.error("-1", "申请不存在");
        }

        Student student1 = studentService.getById(application1.getUsername());
        Student student2 = studentService.getById(application2.getUsername());

        if (student1 == null || student2 == null) {
            return Result.error("-1", "学生不存在");
        }

        if (!student1.getGender().equals(student2.getGender())) {
            return Result.error("-1", "只能同性别的学生互换宿舍");
        }

        DormRoom room1 = dormRoomService.getById(application1.getCurrentRoomId());
        DormRoom room2 = dormRoomService.getById(application2.getCurrentRoomId());

        if (room1 == null || room2 == null) {
            return Result.error("-1", "房间不存在");
        }

        String bedName1 = getBedName(application1.getCurrentBedId());
        String bedName2 = getBedName(application2.getCurrentBedId());

        com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<DormRoom> uw1 = new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<>();
        uw1.eq("dormroom_id", application1.getCurrentRoomId());
        uw1.set(bedName1, application2.getUsername());
        dormRoomService.update(null, uw1);

        com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<DormRoom> uw2 = new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<>();
        uw2.eq("dormroom_id", application2.getCurrentRoomId());
        uw2.set(bedName2, application1.getUsername());
        dormRoomService.update(null, uw2);

        student1.setDormBuildId(room2.getDormBuildId());
        student1.setDormRoomId(application2.getCurrentRoomId());
        studentService.updateById(student1);

        student2.setDormBuildId(room1.getDormBuildId());
        student2.setDormRoomId(application1.getCurrentRoomId());
        studentService.updateById(student2);

        application1.setTowardsRoomId(application2.getCurrentRoomId());
        application1.setTowardsBedId(application2.getCurrentBedId());
        application1.setState("通过");
        application1.setFinishTime(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        adjustRoomService.updateById(application1);

        application2.setTowardsRoomId(application1.getCurrentRoomId());
        application2.setTowardsBedId(application1.getCurrentBedId());
        application2.setState("通过");
        application2.setFinishTime(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        adjustRoomService.updateById(application2);

        return Result.success();
    }

    private String getBedName(int bedNum) {
        switch (bedNum) {
            case 1: return "first_bed";
            case 2: return "second_bed";
            case 3: return "third_bed";
            case 4: return "fourth_bed";
            default: return "";
        }
    }

    private boolean isBedEmpty(DormRoom room, String bedName) {
        switch (bedName) {
            case "first_bed": return room.getFirstBed() == null;
            case "second_bed": return room.getSecondBed() == null;
            case "third_bed": return room.getThirdBed() == null;
            case "fourth_bed": return room.getFourthBed() == null;
            default: return false;
        }
    }
}
