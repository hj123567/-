package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "dorm_room")
public class DormRoom {

    @TableId(value = "dormroom_id")
    private Integer dormRoomId;
    @TableField("dormbuild_id")
    private int dormBuildId;
    @TableField("floor_num")
    private int floorNum;
    @TableField("max_capacity")
    private int maxCapacity;
    @TableField("current_capacity")
    private int currentCapacity;
    @TableField("first_bed")
    private String firstBed;
    @TableField("second_bed")
    private String secondBed;
    @TableField("third_bed")
    private String thirdBed;
    @TableField("fourth_bed")
    private String fourthBed;

    public Integer getDormRoomId() {
        return dormRoomId;
    }

    public void setDormRoomId(Integer dormRoomId) {
        this.dormRoomId = dormRoomId;
    }

    public int getDormBuildId() {
        return dormBuildId;
    }

    public void setDormBuildId(int dormBuildId) {
        this.dormBuildId = dormBuildId;
    }

    public int getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(int floorNum) {
        this.floorNum = floorNum;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public String getFirstBed() {
        return firstBed;
    }

    public void setFirstBed(String firstBed) {
        this.firstBed = firstBed;
    }

    public String getSecondBed() {
        return secondBed;
    }

    public void setSecondBed(String secondBed) {
        this.secondBed = secondBed;
    }

    public String getThirdBed() {
        return thirdBed;
    }

    public void setThirdBed(String thirdBed) {
        this.thirdBed = thirdBed;
    }

    public String getFourthBed() {
        return fourthBed;
    }

    public void setFourthBed(String fourthBed) {
        this.fourthBed = fourthBed;
    }
}
