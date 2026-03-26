USE dormitory;

-- 检查并添加缺失的字段
ALTER TABLE student ADD COLUMN IF NOT EXISTS student_type VARCHAR(20) DEFAULT '老生' COMMENT '学生类型：新生/老生';
ALTER TABLE student ADD COLUMN IF NOT EXISTS registration_status VARCHAR(20) DEFAULT '已报到' COMMENT '报到状态：未报到/已报到';
ALTER TABLE student ADD COLUMN IF NOT EXISTS accommodation_status VARCHAR(20) DEFAULT '已分配' COMMENT '住宿状态：未分配/已分配';
ALTER TABLE student ADD COLUMN IF NOT EXISTS admission_year INT COMMENT '入学年份';
ALTER TABLE student ADD COLUMN IF NOT EXISTS assigned_dorm_build_id INT COMMENT '预先分配的楼栋ID';

-- 更新现有学生的数据
UPDATE student SET 
    student_type = '老生',
    registration_status = '已报到',
    accommodation_status = CASE WHEN dorm_room_id IS NOT NULL THEN '已分配' ELSE '未分配' END;

-- 验证
DESC student;
SELECT COUNT(*) FROM student;
SELECT * FROM student LIMIT 5;