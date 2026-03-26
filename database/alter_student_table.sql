-- 修改学生表结构，添加新生入学相关字段
-- 执行前请先备份数据库！

USE dormitory;

-- 1. 添加学生类型字段（新生/老生）
ALTER TABLE student ADD COLUMN IF NOT EXISTS student_type VARCHAR(20) DEFAULT '老生' COMMENT '学生类型：新生/老生';

-- 2. 添加报到状态字段（未报到/已报到）
ALTER TABLE student ADD COLUMN IF NOT EXISTS registration_status VARCHAR(20) DEFAULT '已报到' COMMENT '报到状态：未报到/已报到';

-- 3. 添加住宿状态字段（未分配/已分配）
ALTER TABLE student ADD COLUMN IF NOT EXISTS accommodation_status VARCHAR(20) DEFAULT '已分配' COMMENT '住宿状态：未分配/已分配';

-- 4. 添加入学年份字段
ALTER TABLE student ADD COLUMN IF NOT EXISTS admission_year INT COMMENT '入学年份';

-- 5. 添加预先分配的楼栋ID
ALTER TABLE student ADD COLUMN IF NOT EXISTS assigned_dorm_build_id INT COMMENT '预先分配的楼栋ID';

-- 6. 更新现有学生的数据
UPDATE student SET 
    student_type = '老生',
    registration_status = '已报到',
    accommodation_status = CASE WHEN dorm_room_id IS NOT NULL THEN '已分配' ELSE '未分配' END
WHERE student_type IS NULL;

-- 7. 创建索引以提高查询效率
CREATE INDEX IF NOT EXISTS idx_student_type ON student(student_type);
CREATE INDEX IF NOT EXISTS idx_registration_status ON student(registration_status);
CREATE INDEX IF NOT EXISTS idx_accommodation_status ON student(accommodation_status);
CREATE INDEX IF NOT EXISTS idx_assigned_dorm_build ON student(assigned_dorm_build_id);

-- 查看表结构确认
DESC student;
