package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "repair")
public class Repair {

    @TableId(value = "id")
    private Integer id;
    @TableField("repairer")
    private String repairer;
    @TableField("dormbuild_id")
    private int dormBuildId;
    @TableField("dormroom_id")
    private int dormRoomId;
    @TableField("title")
    private String title;
    @TableField("content")
    private String content;
    @TableField("state")
    private String state;
    @TableField("order_buildtime")
    private String orderBuildTime;
    @TableField("order_finishtime")
    private String orderFinishTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRepairer() {
        return repairer;
    }

    public void setRepairer(String repairer) {
        this.repairer = repairer;
    }

    public int getDormBuildId() {
        return dormBuildId;
    }

    public void setDormBuildId(int dormBuildId) {
        this.dormBuildId = dormBuildId;
    }

    public int getDormRoomId() {
        return dormRoomId;
    }

    public void setDormRoomId(int dormRoomId) {
        this.dormRoomId = dormRoomId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOrderBuildTime() {
        return orderBuildTime;
    }

    public void setOrderBuildTime(String orderBuildTime) {
        this.orderBuildTime = orderBuildTime;
    }

    public String getOrderFinishTime() {
        return orderFinishTime;
    }

    public void setOrderFinishTime(String orderFinishTime) {
        this.orderFinishTime = orderFinishTime;
    }

    public Repair() {
    }

    public Repair(Integer id, String repairer, int dormBuildId, int dormRoomId, String title, String content, String state, String orderBuildTime, String orderFinishTime) {
        this.id = id;
        this.repairer = repairer;
        this.dormBuildId = dormBuildId;
        this.dormRoomId = dormRoomId;
        this.title = title;
        this.content = content;
        this.state = state;
        this.orderBuildTime = orderBuildTime;
        this.orderFinishTime = orderFinishTime;
    }
}
