package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Student;
import com.example.springboot.mapper.StudentMapper;
import com.example.springboot.service.DormRoomService;
import com.example.springboot.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    /**
     * 注入DAO层对象
     */
    @Resource
    private StudentMapper studentMapper;

    @Resource
    private DormRoomService dormRoomService;

    /**
     * 学生登陆
     */
    @Override
    public Student stuLogin(String username, String password) {
        QueryWrapper<Student> qw = new QueryWrapper<>();
        qw.eq("username", username);
        qw.eq("password", password);
        Student student = studentMapper.selectOne(qw);
        if (student != null) {
            return student;
        } else {
            return null;
        }
    }

    /**
     * 学生新增
     */
    @Override
    public int addNewStudent(Student student) {
        int insert = studentMapper.insert(student);
        return insert;
    }

    /**
     * 分页查询学生
     */
    @Override
    public Page find(Integer pageNum, Integer pageSize, String search, Integer dormBuildId) {
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper<Student> qw = new QueryWrapper<>();
        qw.like("name", search);
        if (dormBuildId != null) {
            qw.eq("dorm_build_id", dormBuildId);
        }
        Page studentPage = studentMapper.selectPage(page, qw);
        return studentPage;
    }

    /**
     * 更新学生信息
     */
    @Override
    public int updateNewStudent(Student student) {
        int i = studentMapper.updateById(student);
        return i;
    }

    /**
     * 删除学生信息
     */
    @Override
    public int deleteStudent(String username) {
        int i = studentMapper.deleteById(username);
        return i;
    }


    /**
     * 主页顶部：学生统计
     */
    @Override
    public int stuNum() {
        QueryWrapper<Student> qw = new QueryWrapper<>();
        qw.isNotNull("username");
        int stuNum = Math.toIntExact(studentMapper.selectCount(qw));
        return stuNum;
    }

    /**
     * 床位信息，查询该学生信息
     */
    @Override
    public Student stuInfo(String username) {
        QueryWrapper<Student> qw = new QueryWrapper<>();
        qw.eq("username", username);
        Student student = studentMapper.selectOne(qw);
        return student;
    }

    /**
     * 根据房间号查询所有学生
     */
    @Override
    public java.util.List<Student> findByRoom(Integer dormRoomId) {
        QueryWrapper<Student> qw = new QueryWrapper<>();
        qw.eq("dorm_room_id", dormRoomId);
        return studentMapper.selectList(qw);
    }

    /**
     * 为学生分配宿舍（含床位）
     */
    @Override
    @Transactional
    public int updateWithBed(String username, Integer dormBuildId, Integer dormRoomId, Integer bedNum) {
        QueryWrapper<Student> qw = new QueryWrapper<>();
        qw.eq("username", username);
        Student student = studentMapper.selectOne(qw);
        if (student == null) {
            return -1;
        }
        
        int result = dormRoomService.assignBedToStudent(username, dormBuildId, dormRoomId, bedNum);
        if (result != 1) {
            return result;
        }
        
        student.setDormBuildId(dormBuildId);
        student.setDormRoomId(dormRoomId);
        student.setRegistrationStatus("已报到");
        student.setAccommodationStatus("已分配");
        
        return studentMapper.updateById(student);
    }
}
