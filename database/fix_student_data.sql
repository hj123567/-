USE dormitory;

-- 1. 添加缺失的字段（如果不存在）
ALTER TABLE student ADD COLUMN IF NOT EXISTS student_type VARCHAR(20) DEFAULT '老生';
ALTER TABLE student ADD COLUMN IF NOT EXISTS registration_status VARCHAR(20) DEFAULT '已报到';
ALTER TABLE student ADD COLUMN IF NOT EXISTS accommodation_status VARCHAR(20) DEFAULT '已分配';
ALTER TABLE student ADD COLUMN IF NOT EXISTS admission_year INT;
ALTER TABLE student ADD COLUMN IF NOT EXISTS assigned_dorm_build_id INT;
ALTER TABLE student ADD COLUMN IF NOT EXISTS is_room_leader INT DEFAULT 0;

-- 2. 更新现有学生的状态
UPDATE student SET 
    student_type = '老生',
    registration_status = '已报到',
    accommodation_status = CASE WHEN dorm_room_id IS NOT NULL THEN '已分配' ELSE '未分配' END,
    is_room_leader = 0;

-- 3. 确保每个房间有一个寝室长
UPDATE student s
SET s.is_room_leader = 1
WHERE s.username IN (
    SELECT MIN(t.username) 
    FROM (
        SELECT username, dorm_build_id, dorm_room_id 
        FROM student 
        WHERE dorm_room_id IS NOT NULL
    ) t
    GROUP BY t.dorm_build_id, t.dorm_room_id
);

-- 4. 验证
SELECT '表结构更新完成！' as message;
SELECT COUNT(*) as total FROM student;
