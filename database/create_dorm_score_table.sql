-- 创建卫生评分表
USE dormitory;

DROP TABLE IF EXISTS dorm_score;

CREATE TABLE dorm_score (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键 ID',
    dorm_build_id INT NOT NULL COMMENT '楼栋号',
    dorm_room_id INT NOT NULL COMMENT '房间号',
    score INT NOT NULL COMMENT '评分 (0-100)',
    comment VARCHAR(500) COMMENT '评语',
    score_time VARCHAR(50) COMMENT '评分时间',
    scorer VARCHAR(50) COMMENT '评分人',
    INDEX idx_build_id (dorm_build_id),
    INDEX idx_room_id (dorm_room_id),
    INDEX idx_score_time (score_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='卫生评分表';

-- 插入一些测试数据
INSERT INTO dorm_score (dorm_build_id, dorm_room_id, score, comment, score_time, scorer) VALUES
(1, 101, 95, '宿舍非常干净', '2024-01-15 10:30:00', '张三'),
(1, 102, 88, '卫生良好', '2024-01-15 10:35:00', '张三'),
(1, 103, 92, '整洁有序', '2024-01-15 10:40:00', '张三'),
(2, 201, 90, '干净卫生', '2024-01-15 11:00:00', '李四'),
(2, 202, 85, '整体不错', '2024-01-15 11:05:00', '李四');
