package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "dorm_score")
public class DormScore {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
    @TableField("dorm_build_id")
    private Integer dormBuildId;
    
    @TableField("dorm_room_id")
    private Integer dormRoomId;
    
    @TableField("score")
    private Integer score;
    
    @TableField("comment")
    private String comment;
    
    @TableField("score_time")
    private String scoreTime;
    
    @TableField("scorer")
    private String scorer;

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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getScoreTime() {
        return scoreTime;
    }

    public void setScoreTime(String scoreTime) {
        this.scoreTime = scoreTime;
    }

    public String getScorer() {
        return scorer;
    }

    public void setScorer(String scorer) {
        this.scorer = scorer;
    }

    public DormScore() {
    }

    public DormScore(Integer id, Integer dormBuildId, Integer dormRoomId, Integer score, String comment, String scoreTime, String scorer) {
        this.id = id;
        this.dormBuildId = dormBuildId;
        this.dormRoomId = dormRoomId;
        this.score = score;
        this.comment = comment;
        this.scoreTime = scoreTime;
        this.scorer = scorer;
    }
}
