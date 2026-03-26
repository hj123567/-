package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "student")
public class Student {
    @TableId(value = "username")
    private String username;
    @TableField("password")
    private String password;
    @TableField("name")
    private String name;
    @TableField("age")
    private int age;
    @TableField("gender")
    private String gender;
    @TableField("phone_num")
    private String phoneNum;
    @TableField("email")
    private String email;
    @TableField("avatar")
    private String avatar;
    @TableField("college")
    private String college;
    @TableField("class_name")
    private String className;
    @TableField("dorm_build_id")
    private Integer dormBuildId;
    @TableField("dorm_room_id")
    private Integer dormRoomId;
    @TableField("is_room_leader")
    private Integer isRoomLeader;

    @TableField("student_type")
    private String studentType;

    @TableField("registration_status")
    private String registrationStatus;

    @TableField("accommodation_status")
    private String accommodationStatus;

    @TableField("admission_year")
    private Integer admissionYear;

    @TableField("assigned_dorm_build_id")
    private Integer assignedDormBuildId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getDormBuildId() {
        return dormBuildId;
    }

    public void setDormBuildId(Integer dormBuildId) {
        this.dormBuildId = dormBuildId;
    }

    public Integer getDormRoomId() {
        return dormRoomId;
    }

    public void setDormRoomId(Integer dormRoomId) {
        this.dormRoomId = dormRoomId;
    }

    public Integer getIsRoomLeader() {
        return isRoomLeader;
    }

    public void setIsRoomLeader(Integer isRoomLeader) {
        this.isRoomLeader = isRoomLeader;
    }

    public String getStudentType() {
        return studentType;
    }

    public void setStudentType(String studentType) {
        this.studentType = studentType;
    }

    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public String getAccommodationStatus() {
        return accommodationStatus;
    }

    public void setAccommodationStatus(String accommodationStatus) {
        this.accommodationStatus = accommodationStatus;
    }

    public Integer getAdmissionYear() {
        return admissionYear;
    }

    public void setAdmissionYear(Integer admissionYear) {
        this.admissionYear = admissionYear;
    }

    public Integer getAssignedDormBuildId() {
        return assignedDormBuildId;
    }

    public void setAssignedDormBuildId(Integer assignedDormBuildId) {
        this.assignedDormBuildId = assignedDormBuildId;
    }
}
