USE dormitory;

-- 添加缺失的字段
ALTER TABLE student ADD COLUMN student_type VARCHAR(20) DEFAULT '老生';
ALTER TABLE student ADD COLUMN registration_status VARCHAR(20) DEFAULT '已报到';
ALTER TABLE student ADD COLUMN accommodation_status VARCHAR(20) DEFAULT '已分配';
ALTER TABLE student ADD COLUMN admission_year INT;
ALTER TABLE student ADD COLUMN assigned_dorm_build_id INT;
ALTER TABLE student ADD COLUMN is_room_leader INT DEFAULT 0;

-- 更新现有学生的状态
UPDATE student SET 
    student_type = '老生',
    registration_status = '已报到',
    accommodation_status = CASE WHEN dorm_room_id IS NOT NULL THEN '已分配' ELSE '未分配' END,
    is_room_leader = 0;

-- 验证
SELECT '表结构更新完成！' as message;
SELECT COUNT(*) as total FROM student;
