package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "dorm_manager")
public class DormManager {

    @TableId("username")
    private String username;
    @TableField("password")
    private String password;
    @TableField("dormbuild_id")
    private Integer dormBuildId;
    @TableField("name")
    private String name;
    @TableField("gender")
    private String gender;
    @TableField("age")
    private int age;
    @TableField("phone_num")
    private String phoneNum;
    @TableField("email")
    private String email;
    @TableField("avatar")
    private String avatar;

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

    public Integer getDormBuildId() {
        return dormBuildId;
    }

    public void setDormBuildId(Integer dormBuildId) {
        this.dormBuildId = dormBuildId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public DormManager() {
    }

    public DormManager(String username, String password, Integer dormBuildId, String name, String gender, int age, String phoneNum, String email, String avatar) {
        this.username = username;
        this.password = password;
        this.dormBuildId = dormBuildId;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.phoneNum = phoneNum;
        this.email = email;
        this.avatar = avatar;
    }
}
