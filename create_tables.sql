-- 创建卫生评分表
CREATE TABLE IF NOT EXISTS dorm_score (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dorm_build_id INT NOT NULL COMMENT '楼栋号',
    dorm_room_id INT NOT NULL COMMENT '房间号',
    score INT NOT NULL COMMENT '评分(0-100)',
    comment TEXT COMMENT '评语',
    score_time VARCHAR(50) COMMENT '评分时间',
    scorer VARCHAR(50) COMMENT '评分人',
    INDEX idx_dorm (dorm_build_id, dorm_room_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='卫生评分表';

-- 创建水电费表
CREATE TABLE IF NOT EXISTS utility_bill (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dorm_build_id INT NOT NULL COMMENT '楼栋号',
    dorm_room_id INT NOT NULL COMMENT '房间号',
    electricity_fee DECIMAL(10,2) DEFAULT 0 COMMENT '电费',
    water_fee DECIMAL(10,2) DEFAULT 0 COMMENT '水费',
    total_fee DECIMAL(10,2) DEFAULT 0 COMMENT '总计',
    month VARCHAR(10) COMMENT '月份(YYYY-MM)',
    status VARCHAR(20) DEFAULT '未缴纳' COMMENT '状态:未缴纳/已缴纳',
    notify_time VARCHAR(50) COMMENT '通知时间',
    pay_time VARCHAR(50) COMMENT '缴纳时间',
    INDEX idx_dorm (dorm_build_id, dorm_room_id),
    INDEX idx_month (month),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='水电费表';
