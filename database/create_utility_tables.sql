-- ============================================
-- 水电费管理系统 - 数据库表结构
-- ============================================

USE dormitory;

-- 1. 水电费参数配置表
CREATE TABLE IF NOT EXISTS utility_config (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    water_price DECIMAL(10, 2) DEFAULT 5.00 COMMENT '水费单价(元/吨)',
    electricity_price DECIMAL(10, 2) DEFAULT 0.80 COMMENT '电费单价(元/度)',
    billing_cycle_day INT DEFAULT 1 COMMENT '计费周期日(每月几号)',
    dorm_build_id INT COMMENT '适用楼栋ID，NULL表示全部楼栋',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='水电费参数配置表';

-- 2. 水电表读数记录表
CREATE TABLE IF NOT EXISTS utility_meter_reading (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    dorm_build_id INT NOT NULL COMMENT '楼栋ID',
    dorm_room_id INT NOT NULL COMMENT '房间ID',
    billing_month VARCHAR(7) NOT NULL COMMENT '计费月份(YYYY-MM)',
    water_reading DECIMAL(10, 2) COMMENT '本期水表读数(吨)',
    electricity_reading DECIMAL(10, 2) COMMENT '本期电表读数(度)',
    last_water_reading DECIMAL(10, 2) COMMENT '上期水表读数(吨)',
    last_electricity_reading DECIMAL(10, 2) COMMENT '上期电表读数(度)',
    water_usage DECIMAL(10, 2) COMMENT '用水量(吨)',
    electricity_usage DECIMAL(10, 2) COMMENT '用电量(度)',
    recorder_id INT COMMENT '录入人ID(宿管)',
    record_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '录入时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_dorm_room (dorm_build_id, dorm_room_id),
    INDEX idx_billing_month (billing_month)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='水电表读数记录表';

-- 3. 水电费账单表
CREATE TABLE IF NOT EXISTS utility_bill (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    dorm_build_id INT NOT NULL COMMENT '楼栋ID',
    dorm_room_id INT NOT NULL COMMENT '房间ID',
    billing_month VARCHAR(7) NOT NULL COMMENT '计费月份(YYYY-MM)',
    water_usage DECIMAL(10, 2) COMMENT '用水量(吨)',
    electricity_usage DECIMAL(10, 2) COMMENT '用电量(度)',
    water_price DECIMAL(10, 2) COMMENT '水费单价(元/吨)',
    electricity_price DECIMAL(10, 2) COMMENT '电费单价(元/度)',
    water_amount DECIMAL(10, 2) COMMENT '水费金额(元)',
    electricity_amount DECIMAL(10, 2) COMMENT '电费金额(元)',
    total_amount DECIMAL(10, 2) COMMENT '总金额(元)',
    payment_status VARCHAR(20) DEFAULT '未缴费' COMMENT '缴费状态：未缴费/已缴费',
    payment_time DATETIME COMMENT '缴费时间',
    payer_id INT COMMENT '缴费人ID(学生)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_dorm_room (dorm_build_id, dorm_room_id),
    INDEX idx_billing_month (billing_month),
    INDEX idx_payment_status (payment_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='水电费账单表';

-- 4. 缴费记录表
CREATE TABLE IF NOT EXISTS utility_payment_record (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    bill_id INT NOT NULL COMMENT '账单ID',
    dorm_build_id INT NOT NULL COMMENT '楼栋ID',
    dorm_room_id INT NOT NULL COMMENT '房间ID',
    billing_month VARCHAR(7) NOT NULL COMMENT '计费月份(YYYY-MM)',
    payment_amount DECIMAL(10, 2) NOT NULL COMMENT '缴费金额(元)',
    payment_method VARCHAR(50) DEFAULT '在线支付' COMMENT '支付方式',
    payer_id INT COMMENT '缴费人ID(学生)',
    payer_name VARCHAR(50) COMMENT '缴费人姓名',
    transaction_id VARCHAR(100) COMMENT '交易流水号',
    payment_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '缴费时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_bill_id (bill_id),
    INDEX idx_dorm_room (dorm_build_id, dorm_room_id),
    INDEX idx_payer (payer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='缴费记录表';

-- 5. 催缴提醒表
CREATE TABLE IF NOT EXISTS utility_reminder (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    bill_id INT NOT NULL COMMENT '账单ID',
    dorm_build_id INT NOT NULL COMMENT '楼栋ID',
    dorm_room_id INT NOT NULL COMMENT '房间ID',
    reminder_type VARCHAR(50) DEFAULT '短信' COMMENT '提醒方式：短信/系统消息',
    reminder_content TEXT COMMENT '提醒内容',
    reminder_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '提醒时间',
    operator_id INT COMMENT '操作人ID',
    operator_name VARCHAR(50) COMMENT '操作人姓名',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_bill_id (bill_id),
    INDEX idx_dorm_room (dorm_build_id, dorm_room_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='催缴提醒表';

-- ============================================
-- 插入初始数据
-- ============================================

-- 插入默认水电费参数
INSERT INTO utility_config (water_price, electricity_price, billing_cycle_day) VALUES
(5.00, 0.80, 1);

-- 插入测试读数记录（可选）
-- INSERT INTO utility_meter_reading (dorm_build_id, dorm_room_id, billing_month, water_reading, electricity_reading, last_water_reading, last_electricity_reading, water_usage, electricity_usage) VALUES
-- (1, 101, '2026-03', 150.5, 320.0, 120.0, 280.0, 30.5, 40.0),
-- (1, 102, '2026-03', 145.0, 300.0, 115.0, 260.0, 30.0, 40.0);

-- 插入测试账单（可选）
-- INSERT INTO utility_bill (dorm_build_id, dorm_room_id, billing_month, water_usage, electricity_usage, water_price, electricity_price, water_amount, electricity_amount, total_amount, payment_status) VALUES
-- (1, 101, '2026-03', 30.5, 40.0, 5.00, 0.80, 152.50, 32.00, 184.50, '未缴费'),
-- (1, 102, '2026-03', 30.0, 40.0, 5.00, 0.80, 150.00, 32.00, 182.00, '未缴费');

-- ============================================
-- 查看表结构
-- ============================================

DESC utility_config;
DESC utility_meter_reading;
DESC utility_bill;
DESC utility_payment_record;
DESC utility_reminder;

SELECT '水电费管理系统数据库表创建完成！' AS message;
