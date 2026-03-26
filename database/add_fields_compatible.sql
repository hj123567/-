USE dormitory;

-- 1. 添加学生类型字段（新生/老生）
-- 先检查字段是否存在，如果不存在则添加
SET @col_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = 'dormitory' 
    AND TABLE_NAME = 'student' 
    AND COLUMN_NAME = 'student_type'
);
SET @sql = IF(@col_exists = 0, 
    'ALTER TABLE student ADD COLUMN student_type VARCHAR(20) DEFAULT ''老生'' COMMENT ''学生类型：新生/老生''', 
    'SELECT ''Field student_type already exists''');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 2. 添加报到状态字段（未报到/已报到）
SET @col_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = 'dormitory' 
    AND TABLE_NAME = 'student' 
    AND COLUMN_NAME = 'registration_status'
);
SET @sql = IF(@col_exists = 0, 
    'ALTER TABLE student ADD COLUMN registration_status VARCHAR(20) DEFAULT ''已报到'' COMMENT ''报到状态：未报到/已报到''', 
    'SELECT ''Field registration_status already exists''');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 3. 添加住宿状态字段（未分配/已分配）
SET @col_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = 'dormitory' 
    AND TABLE_NAME = 'student' 
    AND COLUMN_NAME = 'accommodation_status'
);
SET @sql = IF(@col_exists = 0, 
    'ALTER TABLE student ADD COLUMN accommodation_status VARCHAR(20) DEFAULT ''已分配'' COMMENT ''住宿状态：未分配/已分配''', 
    'SELECT ''Field accommodation_status already exists''');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 4. 添加入学年份字段
SET @col_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = 'dormitory' 
    AND TABLE_NAME = 'student' 
    AND COLUMN_NAME = 'admission_year'
);
SET @sql = IF(@col_exists = 0, 
    'ALTER TABLE student ADD COLUMN admission_year INT COMMENT ''入学年份''', 
    'SELECT ''Field admission_year already exists''');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 5. 添加预先分配的楼栋ID
SET @col_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = 'dormitory' 
    AND TABLE_NAME = 'student' 
    AND COLUMN_NAME = 'assigned_dorm_build_id'
);
SET @sql = IF(@col_exists = 0, 
    'ALTER TABLE student ADD COLUMN assigned_dorm_build_id INT COMMENT ''预先分配的楼栋ID''', 
    'SELECT ''Field assigned_dorm_build_id already exists''');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 6. 添加寝室长字段
SET @col_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = 'dormitory' 
    AND TABLE_NAME = 'student' 
    AND COLUMN_NAME = 'is_room_leader'
);
SET @sql = IF(@col_exists = 0, 
    'ALTER TABLE student ADD COLUMN is_room_leader INT DEFAULT 0 COMMENT ''是否为寝室长：0否，1是''', 
    'SELECT ''Field is_room_leader already exists''');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 更新现有学生的数据
UPDATE student SET 
    student_type = '老生',
    registration_status = '已报到',
    accommodation_status = CASE WHEN dorm_room_id IS NOT NULL THEN '已分配' ELSE '未分配' END
WHERE student_type IS NULL OR student_type = '';

-- 查看表结构确认
DESC student;
