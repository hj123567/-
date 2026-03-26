package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.common.JudgeBedName;
import com.example.springboot.entity.AdjustRoom;
import com.example.springboot.entity.DormRoom;
import com.example.springboot.entity.Student;
import com.example.springboot.mapper.DormRoomMapper;
import com.example.springboot.mapper.StudentMapper;
import com.example.springboot.service.DormRoomService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.springboot.common.CalPeopleNum.calNum;


@Service
public class DormRoomImpl extends ServiceImpl<DormRoomMapper, DormRoom> implements DormRoomService {

    @Resource
    private DormRoomMapper dormRoomMapper;

    @Resource
    private StudentMapper studentMapper;

    /**
     * 首页顶部：空宿舍统计
     */
    @Override
    public int notFullRoom() {
        QueryWrapper<DormRoom> qw = new QueryWrapper<>();
        qw.lt("current_capacity", 4);
        int notFullRoomNum = Math.toIntExact(dormRoomMapper.selectCount(qw));
        return notFullRoomNum;
    }

    /**
     * 添加房间
     */
    @Override
    public int addNewRoom(DormRoom dormRoom) {
        int insert = dormRoomMapper.insert(dormRoom);
        return insert;
    }

    /**
     * 查找房间
     */
    @Override
    public Page find(Integer pageNum, Integer pageSize, String search, Integer dormBuildId) {
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper<DormRoom> qw = new QueryWrapper<>();
        qw.like("Dormroom_id", search);
        if (dormBuildId != null) {
            qw.eq("dormbuild_id", dormBuildId);
        }
        Page roomPage = dormRoomMapper.selectPage(page, qw);
        return roomPage;
    }

    /**
     * 更新房间
     */
    @Override
    public int updateNewRoom(DormRoom dormRoom) {
        int i = dormRoomMapper.updateById(dormRoom);
        return i;
    }

    /**
     * 删除房间
     */
    @Override
    public int deleteRoom(Integer dormRoomId) {
        int i = dormRoomMapper.deleteById(dormRoomId);
        return i;
    }

    /**
     * 删除床位上的学生信息
     */
    @Override
    public int deleteBedInfo(String bedName, Integer dormRoomId, int calCurrentNum) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("dormroom_id", dormRoomId);
        updateWrapper.set(bedName, null);
        updateWrapper.set("current_capacity", calCurrentNum - 1);
        int update = dormRoomMapper.update(null, updateWrapper);
        return update;

    }

    /**
     * 床位信息，查询该学生是否已由床位
     */
    @Override
    public DormRoom judgeHadBed(String username) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("first_bed", username);
        qw.or();
        qw.eq("second_bed", username);
        qw.or();
        qw.eq("third_bed", username);
        qw.or();
        qw.eq("fourth_bed", username);
        qw.last("LIMIT 1");
        DormRoom dormRoom = dormRoomMapper.selectOne(qw);
        return dormRoom;
    }

    /**
     * 主页 住宿人数
     */
    @Override
    public Long selectHaveRoomStuNum() {
        QueryWrapper<DormRoom> qw1 = new QueryWrapper<>();
        qw1.isNotNull("first_bed");
        Long first_bed = dormRoomMapper.selectCount(qw1);

        QueryWrapper<DormRoom> qw2 = new QueryWrapper<>();
        qw2.isNotNull("second_bed");
        Long second_bed = dormRoomMapper.selectCount(qw2);

        QueryWrapper<DormRoom> qw3 = new QueryWrapper<>();
        qw3.isNotNull("third_bed");
        Long third_bed = dormRoomMapper.selectCount(qw3);

        QueryWrapper<DormRoom> qw4 = new QueryWrapper<>();
        qw4.isNotNull("fourth_bed");
        Long fourth_bed = dormRoomMapper.selectCount(qw4);

        Long count = first_bed + second_bed + third_bed + fourth_bed;
        return count;
    }

    /**
     * 获取每栋宿舍学生总人数
     */
    @Override
    public Long getEachBuildingStuNum(int dormBuildId) {

        QueryWrapper<DormRoom> qw1 = new QueryWrapper<>();
        qw1.eq("dormbuild_id", dormBuildId);
        qw1.isNotNull("first_bed");
        Long first_bed = dormRoomMapper.selectCount(qw1);

        QueryWrapper<DormRoom> qw2 = new QueryWrapper<>();
        qw2.eq("dormbuild_id", dormBuildId);
        qw2.isNotNull("second_bed");
        Long second_bed = dormRoomMapper.selectCount(qw2);

        QueryWrapper<DormRoom> qw3 = new QueryWrapper<>();
        qw3.eq("dormbuild_id", dormBuildId);
        qw3.isNotNull("third_bed");
        Long third_bed = dormRoomMapper.selectCount(qw3);

        QueryWrapper<DormRoom> qw4 = new QueryWrapper<>();
        qw4.eq("dormbuild_id", dormBuildId);
        qw4.isNotNull("fourth_bed");
        Long fourth_bed = dormRoomMapper.selectCount(qw4);

        Long count = first_bed + second_bed + third_bed + fourth_bed;

        return count;
    }

    /**
     * 根据调宿申请表对房间表内的学生床位进行调整
     */
    @Override
    public int adjustRoomUpdate(AdjustRoom adjustRoom) {
        //调宿人
        String username = adjustRoom.getUsername();
        //当前房间号
        int currentRoomId = adjustRoom.getCurrentRoomId();
        //当前床位名称
        String currentBedName = JudgeBedName.getBedName(adjustRoom.getCurrentBedId());
        //目标房间号
        int towardsRoomId = adjustRoom.getTowardsRoomId();
        //目标床位号
        int towardsBedId = adjustRoom.getTowardsBedId();
        //目标床位名称
        String towardsBedName = JudgeBedName.getBedName(towardsBedId);
        
        // 检查原房间和床位是否存在该学生
        QueryWrapper qw = new QueryWrapper();
        qw.eq("dormroom_id", currentRoomId);
        qw.isNotNull(currentBedName);
        DormRoom dormRoom1 = dormRoomMapper.selectOne(qw);
        if (dormRoom1 == null) {
            return -2;
        }
        
        // 检查目标床位是否为空
        QueryWrapper qw2 = new QueryWrapper();
        qw2.eq("dormroom_id", towardsRoomId);
        qw2.isNull(towardsBedName);
        DormRoom dormRoom2 = dormRoomMapper.selectOne(qw2);
        if (dormRoom2 == null) {
            return -3; // 目标床位已被占用
        }
        
        // 获取目标房间信息，用于更新学生表的宿舍楼号
        DormRoom towardsRoom = dormRoomMapper.selectById(towardsRoomId);
        if (towardsRoom == null) {
            return -4; // 目标房间不存在
        }
        
        int currentCapacity1 = calNum(dormRoom1);
        UpdateWrapper uw1 = new UpdateWrapper();
        uw1.eq("dormroom_id", currentRoomId);
        uw1.set(currentBedName, null);
        uw1.set("current_capacity", currentCapacity1 - 1);
        int result1 = dormRoomMapper.update(null, uw1);
        
        int currentCapacity2 = calNum(towardsRoom);
        if (result1 == 1) {
            UpdateWrapper uw2 = new UpdateWrapper();
            uw2.eq("dormroom_id", towardsRoomId);
            uw2.set(towardsBedName, username);
            uw2.set("current_capacity", currentCapacity2 + 1);
            int result2 = dormRoomMapper.update(null, uw2);
            
            if (result2 == 1) {
                UpdateWrapper uw3 = new UpdateWrapper();
                uw3.eq("username", username);
                uw3.set("dorm_build_id", towardsRoom.getDormBuildId());
                uw3.set("dorm_room_id", towardsRoomId);
                studentMapper.update(null, uw3);
            }
            return result2;
        }
        return -1;
    }


    /**
     * 检查该房间是否满了
     */
    @Override
    public DormRoom checkRoomState(Integer dormRoomId) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("dormroom_id", dormRoomId);
        qw.lt("current_capacity", 4);
        DormRoom dormRoom = dormRoomMapper.selectOne(qw);
        return dormRoom;
    }

    /**
     * 检查该房间是否存在
     */
    @Override
    public DormRoom checkRoomExist(Integer dormRoomId) {
        DormRoom dormRoom = dormRoomMapper.selectById(dormRoomId);
        return dormRoom;
    }


    /**
     * 检查床位是否有人
     */
    @Override
    public DormRoom checkBedState(Integer dormRoomId, int bedNum) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("dormroom_id", dormRoomId);
        qw.isNull(JudgeBedName.getBedName(bedNum));
        DormRoom dormRoom = dormRoomMapper.selectOne(qw);
        return dormRoom;
    }

    /**
     * 查询指定房间的空床位
     */
    @Override
    public List<Map<String, Object>> getEmptyBeds(Integer dormRoomId) {
        // 先查询房间信息
        DormRoom dormRoom = dormRoomMapper.selectById(dormRoomId);
        if (dormRoom == null) {
            return new ArrayList<>();
        }
        
        List<Map<String, Object>> emptyBeds = new ArrayList<>();
        
        // 检查四个床位
        String[] bedNames = {"first_bed", "second_bed", "third_bed", "fourth_bed"};
        String[] bedLabels = {"1 号床", "2 号床", "3 号床", "4 号床"};
        Integer[] bedIds = {1, 2, 3, 4};
        
        for (int i = 0; i < 4; i++) {
            // 如果该床位为 null，说明是空床位
            String bedValue = getBedValueByName(dormRoom, bedNames[i]);
            if (bedValue == null) {
                Map<String, Object> bedInfo = new HashMap<>();
                bedInfo.put("roomId", dormRoom.getDormRoomId());
                bedInfo.put("bedId", bedIds[i]);
                bedInfo.put("bedLabel", bedLabels[i]);
                emptyBeds.add(bedInfo);
            }
        }
        
        return emptyBeds;
    }

    /**
     * 查询所有可换宿舍的空床位
     */
    @Override
    public List<Map<String, Object>> getAllEmptyBeds(Integer dormBuildId) {
        // 查询所有有空床位的房间
        QueryWrapper<DormRoom> qw = new QueryWrapper<>();
        if (dormBuildId != null) {
            qw.eq("dormbuild_id", dormBuildId);
        }
        qw.lt("current_capacity", 4); // 当前人数小于 4，说明有空床位
        
        List<DormRoom> rooms = dormRoomMapper.selectList(qw);
        List<Map<String, Object>> allEmptyBeds = new ArrayList<>();
        
        for (DormRoom room : rooms) {
            // 检查四个床位
            if (room.getFirstBed() == null) {
                Map<String, Object> bedInfo = new HashMap<>();
                bedInfo.put("roomId", room.getDormRoomId());
                bedInfo.put("bedId", 1);
                bedInfo.put("bedLabel", "1 号床");
                allEmptyBeds.add(bedInfo);
            }
            if (room.getSecondBed() == null) {
                Map<String, Object> bedInfo = new HashMap<>();
                bedInfo.put("roomId", room.getDormRoomId());
                bedInfo.put("bedId", 2);
                bedInfo.put("bedLabel", "2 号床");
                allEmptyBeds.add(bedInfo);
            }
            if (room.getThirdBed() == null) {
                Map<String, Object> bedInfo = new HashMap<>();
                bedInfo.put("roomId", room.getDormRoomId());
                bedInfo.put("bedId", 3);
                bedInfo.put("bedLabel", "3 号床");
                allEmptyBeds.add(bedInfo);
            }
            if (room.getFourthBed() == null) {
                Map<String, Object> bedInfo = new HashMap<>();
                bedInfo.put("roomId", room.getDormRoomId());
                bedInfo.put("bedId", 4);
                bedInfo.put("bedLabel", "4 号床");
                allEmptyBeds.add(bedInfo);
            }
        }
        
        return allEmptyBeds;
    }

    /**
     * 查询所有可换宿舍的空床位（排除当前房间）
     */
    @Override
    public List<Map<String, Object>> getAllEmptyBedsExcludeCurrent(Integer dormBuildId, Integer currentRoomId) {
        // 查询所有有空床位的房间，排除当前房间
        QueryWrapper<DormRoom> qw = new QueryWrapper<>();
        if (dormBuildId != null) {
            qw.eq("dormbuild_id", dormBuildId);
        }
        qw.ne("dormroom_id", currentRoomId); // 排除当前房间
        qw.lt("current_capacity", 4); // 当前人数小于 4，说明有空床位
        
        List<DormRoom> rooms = dormRoomMapper.selectList(qw);
        List<Map<String, Object>> allEmptyBeds = new ArrayList<>();
        
        for (DormRoom room : rooms) {
            // 检查四个床位
            if (room.getFirstBed() == null) {
                Map<String, Object> bedInfo = new HashMap<>();
                bedInfo.put("roomId", room.getDormRoomId());
                bedInfo.put("bedId", 1);
                bedInfo.put("bedLabel", "1 号床");
                allEmptyBeds.add(bedInfo);
            }
            if (room.getSecondBed() == null) {
                Map<String, Object> bedInfo = new HashMap<>();
                bedInfo.put("roomId", room.getDormRoomId());
                bedInfo.put("bedId", 2);
                bedInfo.put("bedLabel", "2 号床");
                allEmptyBeds.add(bedInfo);
            }
            if (room.getThirdBed() == null) {
                Map<String, Object> bedInfo = new HashMap<>();
                bedInfo.put("roomId", room.getDormRoomId());
                bedInfo.put("bedId", 3);
                bedInfo.put("bedLabel", "3 号床");
                allEmptyBeds.add(bedInfo);
            }
            if (room.getFourthBed() == null) {
                Map<String, Object> bedInfo = new HashMap<>();
                bedInfo.put("roomId", room.getDormRoomId());
                bedInfo.put("bedId", 4);
                bedInfo.put("bedLabel", "4 号床");
                allEmptyBeds.add(bedInfo);
            }
        }
        
        return allEmptyBeds;
    }
    
    private String getBedValueByName(DormRoom dormRoom, String bedName) {
        switch (bedName) {
            case "first_bed":
                return dormRoom.getFirstBed();
            case "second_bed":
                return dormRoom.getSecondBed();
            case "third_bed":
                return dormRoom.getThirdBed();
            case "fourth_bed":
                return dormRoom.getFourthBed();
            default:
                return null;
        }
    }

    @Override
    public int syncBedData() {
        int totalCount = 0;

        QueryWrapper<DormRoom> clearQw = new QueryWrapper<>();
        UpdateWrapper<DormRoom> clearUw = new UpdateWrapper<>();
        clearUw.set("first_bed", null);
        clearUw.set("second_bed", null);
        clearUw.set("third_bed", null);
        clearUw.set("fourth_bed", null);
        clearUw.set("current_capacity", 0);
        dormRoomMapper.update(null, clearUw);

        QueryWrapper<Student> studentQw = new QueryWrapper<>();
        studentQw.isNotNull("dorm_room_id");
        List<Student> students = studentMapper.selectList(studentQw);

        Map<Integer, List<Student>> roomStudentMap = new HashMap<>();
        for (Student student : students) {
            Integer roomId = student.getDormRoomId();
            if (!roomStudentMap.containsKey(roomId)) {
                roomStudentMap.put(roomId, new ArrayList<>());
            }
            roomStudentMap.get(roomId).add(student);
        }

        for (Map.Entry<Integer, List<Student>> entry : roomStudentMap.entrySet()) {
            Integer roomId = entry.getKey();
            List<Student> roomStudents = entry.getValue();

            DormRoom room = dormRoomMapper.selectById(roomId);
            if (room == null) {
                continue;
            }

            UpdateWrapper<DormRoom> uw = new UpdateWrapper<>();
            uw.eq("dormroom_id", roomId);

            String firstBed = roomStudents.size() > 0 ? roomStudents.get(0).getUsername() : null;
            String secondBed = roomStudents.size() > 1 ? roomStudents.get(1).getUsername() : null;
            String thirdBed = roomStudents.size() > 2 ? roomStudents.get(2).getUsername() : null;
            String fourthBed = roomStudents.size() > 3 ? roomStudents.get(3).getUsername() : null;

            uw.set("first_bed", firstBed);
            uw.set("second_bed", secondBed);
            uw.set("third_bed", thirdBed);
            uw.set("fourth_bed", fourthBed);
            uw.set("current_capacity", Math.min(roomStudents.size(), 4));

            dormRoomMapper.update(null, uw);
            totalCount += roomStudents.size();
        }

        return totalCount;
    }

    @Override
    public String smartAssignDorm() {
        StringBuilder result = new StringBuilder();

        QueryWrapper<DormRoom> clearRoomQw = new QueryWrapper<>();
        UpdateWrapper<DormRoom> clearRoomUw = new UpdateWrapper<>();
        clearRoomUw.set("first_bed", null);
        clearRoomUw.set("second_bed", null);
        clearRoomUw.set("third_bed", null);
        clearRoomUw.set("fourth_bed", null);
        clearRoomUw.set("current_capacity", 0);
        dormRoomMapper.update(null, clearRoomUw);

        QueryWrapper<Student> clearStudentQw = new QueryWrapper<>();
        UpdateWrapper<Student> clearStudentUw = new UpdateWrapper<>();
        clearStudentUw.set("dorm_build_id", null);
        clearStudentUw.set("dorm_room_id", null);
        studentMapper.update(null, clearStudentUw);

        QueryWrapper<Student> studentQw = new QueryWrapper<>();
        studentQw.isNotNull("gender");
        List<Student> allStudents = studentMapper.selectList(studentQw);

        List<Student> maleStudents = new ArrayList<>();
        List<Student> femaleStudents = new ArrayList<>();

        for (Student student : allStudents) {
            if ("男".equals(student.getGender())) {
                maleStudents.add(student);
            } else if ("女".equals(student.getGender())) {
                femaleStudents.add(student);
            }
        }

        QueryWrapper<DormRoom> roomQw = new QueryWrapper<>();
        roomQw.orderByAsc("dormbuild_id", "dormroom_id");
        List<DormRoom> allRooms = dormRoomMapper.selectList(roomQw);

        List<DormRoom> maleRooms = new ArrayList<>();
        List<DormRoom> femaleRooms = new ArrayList<>();

        for (DormRoom room : allRooms) {
            if (room.getDormBuildId() % 2 == 1) {
                maleRooms.add(room);
            } else {
                femaleRooms.add(room);
            }
        }

        int maleAssigned = assignStudentsToRooms(maleStudents, maleRooms, "男");
        int femaleAssigned = assignStudentsToRooms(femaleStudents, femaleRooms, "女");

        result.append("智能分配完成！\n");
        result.append("男生：分配 ").append(maleAssigned).append(" 人\n");
        result.append("女生：分配 ").append(femaleAssigned).append(" 人\n");
        result.append("总计：").append(maleAssigned + femaleAssigned).append(" 人\n");
        result.append("规则：奇数号楼住男生，偶数号楼住女生，每间宿舍最多4人");

        return result.toString();
    }

    private int assignStudentsToRooms(List<Student> students, List<DormRoom> rooms, String gender) {
        int assignedCount = 0;
        int studentIndex = 0;

        for (DormRoom room : rooms) {
            if (studentIndex >= students.size()) {
                break;
            }

            List<Student> roomStudents = new ArrayList<>();
            for (int i = 0; i < 4 && studentIndex < students.size(); i++) {
                roomStudents.add(students.get(studentIndex++));
            }

            String firstBed = roomStudents.size() > 0 ? roomStudents.get(0).getUsername() : null;
            String secondBed = roomStudents.size() > 1 ? roomStudents.get(1).getUsername() : null;
            String thirdBed = roomStudents.size() > 2 ? roomStudents.get(2).getUsername() : null;
            String fourthBed = roomStudents.size() > 3 ? roomStudents.get(3).getUsername() : null;

            UpdateWrapper<DormRoom> roomUw = new UpdateWrapper<>();
            roomUw.eq("dormroom_id", room.getDormRoomId());
            roomUw.set("first_bed", firstBed);
            roomUw.set("second_bed", secondBed);
            roomUw.set("third_bed", thirdBed);
            roomUw.set("fourth_bed", fourthBed);
            roomUw.set("current_capacity", roomStudents.size());
            dormRoomMapper.update(null, roomUw);

            for (Student student : roomStudents) {
                UpdateWrapper<Student> studentUw = new UpdateWrapper<>();
                studentUw.eq("username", student.getUsername());
                studentUw.set("dorm_build_id", room.getDormBuildId());
                studentUw.set("dorm_room_id", room.getDormRoomId());
                studentMapper.update(null, studentUw);
                assignedCount++;
            }
        }

        return assignedCount;
    }

    /**
     * 获取所有有空床位的房间列表（用于新生分配）
     */
    @Override
    public List<Map<String, Object>> getRoomsWithEmptyBeds(Integer dormBuildId) {
        QueryWrapper<DormRoom> qw = new QueryWrapper<>();
        if (dormBuildId != null) {
            qw.eq("dormbuild_id", dormBuildId);
        }
        qw.lt("current_capacity", 4);
        qw.orderByAsc("dormbuild_id", "dormroom_id");
        
        List<DormRoom> rooms = dormRoomMapper.selectList(qw);
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (DormRoom room : rooms) {
            Map<String, Object> roomInfo = new HashMap<>();
            roomInfo.put("dormRoomId", room.getDormRoomId());
            roomInfo.put("dormBuildId", room.getDormBuildId());
            roomInfo.put("floorNum", room.getFloorNum());
            roomInfo.put("currentCapacity", room.getCurrentCapacity());
            roomInfo.put("maxCapacity", room.getMaxCapacity());
            
            List<Integer> availableBeds = new ArrayList<>();
            if (room.getFirstBed() == null) availableBeds.add(1);
            if (room.getSecondBed() == null) availableBeds.add(2);
            if (room.getThirdBed() == null) availableBeds.add(3);
            if (room.getFourthBed() == null) availableBeds.add(4);
            
            roomInfo.put("availableBeds", availableBeds);
            result.add(roomInfo);
        }
        
        return result;
    }

    /**
     * 为学生分配床位
     */
    @Override
    public int assignBedToStudent(String username, Integer dormBuildId, Integer dormRoomId, Integer bedNum) {
        DormRoom room = dormRoomMapper.selectById(dormRoomId);
        if (room == null) {
            return -1;
        }
        
        String bedField = "";
        switch (bedNum) {
            case 1: bedField = "first_bed"; break;
            case 2: bedField = "second_bed"; break;
            case 3: bedField = "third_bed"; break;
            case 4: bedField = "fourth_bed"; break;
            default: return -2;
        }
        
        QueryWrapper<DormRoom> checkQw = new QueryWrapper<>();
        checkQw.eq("dormroom_id", dormRoomId);
        checkQw.isNull(bedField);
        DormRoom checkRoom = dormRoomMapper.selectOne(checkQw);
        if (checkRoom == null) {
            return -3;
        }
        
        UpdateWrapper<DormRoom> uw = new UpdateWrapper<>();
        uw.eq("dormroom_id", dormRoomId);
        uw.set(bedField, username);
        uw.set("current_capacity", room.getCurrentCapacity() + 1);
        
        return dormRoomMapper.update(null, uw);
    }
}
