package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.entity.Student;


public interface StudentService extends IService<Student> {

    //学生登陆
    Student stuLogin(String username, String password);

    //新增学生
    int addNewStudent(Student student);

    //查询学生
    Page find(Integer pageNum, Integer pageSize, String search, Integer dormBuildId);

    //更新学生信息
    int updateNewStudent(Student student);

    //删除学生信息
    int deleteStudent(String username);

    //统计学生人数
    int stuNum();

    //床位信息，查询该学生信息
    Student stuInfo(String username);

    //根据房间号查询所有学生
    java.util.List<Student> findByRoom(Integer dormRoomId);

    //为学生分配宿舍（含床位）
    int updateWithBed(String username, Integer dormBuildId, Integer dormRoomId, Integer bedNum);

}
