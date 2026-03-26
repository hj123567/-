package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

public class AdjustRoom {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField(value = "username")
    private String username;
    @TableField(value = "name")
    private String name;
    @TableField(value = "currentroom_id")
    private int currentRoomId;
    @TableField(value = "currentbed_id")
    private int currentBedId;
    @TableField(value = "towardsroom_id")
    private int towardsRoomId;
    @TableField(value = "towardsbed_id")
    private int towardsBedId;
    @TableField("state")
    private String state;
    @TableField("apply_time")
    private String applyTime;
    @TableField("finish_time")
    private String finishTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrentRoomId() {
        return currentRoomId;
    }

    public void setCurrentRoomId(int currentRoomId) {
        this.currentRoomId = currentRoomId;
    }

    public int getCurrentBedId() {
        return currentBedId;
    }

    public void setCurrentBedId(int currentBedId) {
        this.currentBedId = currentBedId;
    }

    public int getTowardsRoomId() {
        return towardsRoomId;
    }

    public void setTowardsRoomId(int towardsRoomId) {
        this.towardsRoomId = towardsRoomId;
    }

    public int getTowardsBedId() {
        return towardsBedId;
    }

    public void setTowardsBedId(int towardsBedId) {
        this.towardsBedId = towardsBedId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }
}
