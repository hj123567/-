package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "dorm_build")
public class DormBuild {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("dormbuild_id")
    private int dormBuildId;
    @TableField("dormbuild_name")
    private String dormBuildName;
    @TableField("dormbuild_detail")
    private String dormBuildDetail;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getDormBuildId() {
        return dormBuildId;
    }

    public void setDormBuildId(int dormBuildId) {
        this.dormBuildId = dormBuildId;
    }

    public String getDormBuildName() {
        return dormBuildName;
    }

    public void setDormBuildName(String dormBuildName) {
        this.dormBuildName = dormBuildName;
    }

    public String getDormBuildDetail() {
        return dormBuildDetail;
    }

    public void setDormBuildDetail(String dormBuildDetail) {
        this.dormBuildDetail = dormBuildDetail;
    }

    public DormBuild() {
    }

    public DormBuild(Integer id, int dormBuildId, String dormBuildName, String dormBuildDetail) {
        this.id = id;
        this.dormBuildId = dormBuildId;
        this.dormBuildName = dormBuildName;
        this.dormBuildDetail = dormBuildDetail;
    }
}
