package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("utility_meter_reading")
public class UtilityMeterReading implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer dormBuildId;
    private Integer dormRoomId;
    private String billingMonth;
    private BigDecimal waterReading;
    private BigDecimal electricityReading;
    private BigDecimal lastWaterReading;
    private BigDecimal lastElectricityReading;
    private BigDecimal waterUsage;
    private BigDecimal electricityUsage;
    private Integer recorderId;
    private Date recordTime;
    private Date createTime;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getBillingMonth() {
        return billingMonth;
    }

    public void setBillingMonth(String billingMonth) {
        this.billingMonth = billingMonth;
    }

    public BigDecimal getWaterReading() {
        return waterReading;
    }

    public void setWaterReading(BigDecimal waterReading) {
        this.waterReading = waterReading;
    }

    public BigDecimal getElectricityReading() {
        return electricityReading;
    }

    public void setElectricityReading(BigDecimal electricityReading) {
        this.electricityReading = electricityReading;
    }

    public BigDecimal getLastWaterReading() {
        return lastWaterReading;
    }

    public void setLastWaterReading(BigDecimal lastWaterReading) {
        this.lastWaterReading = lastWaterReading;
    }

    public BigDecimal getLastElectricityReading() {
        return lastElectricityReading;
    }

    public void setLastElectricityReading(BigDecimal lastElectricityReading) {
        this.lastElectricityReading = lastElectricityReading;
    }

    public BigDecimal getWaterUsage() {
        return waterUsage;
    }

    public void setWaterUsage(BigDecimal waterUsage) {
        this.waterUsage = waterUsage;
    }

    public BigDecimal getElectricityUsage() {
        return electricityUsage;
    }

    public void setElectricityUsage(BigDecimal electricityUsage) {
        this.electricityUsage = electricityUsage;
    }

    public Integer getRecorderId() {
        return recorderId;
    }

    public void setRecorderId(Integer recorderId) {
        this.recorderId = recorderId;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
