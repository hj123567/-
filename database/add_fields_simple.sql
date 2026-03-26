USE dormitory;

-- 尝试添加学生类型字段（如果已存在会报错，忽略即可）
ALTER TABLE student ADD COLUMN student_type VARCHAR(20) DEFAULT '老生' COMMENT '学生类型：新生/老生';

-- 尝试添加报到状态字段
ALTER TABLE student ADD COLUMN registration_status VARCHAR(20) DEFAULT '已报到' COMMENT '报到状态：未报到/已报到';

-- 尝试添加住宿状态字段
ALTER TABLE student ADD COLUMN accommodation_status VARCHAR(20) DEFAULT '已分配' COMMENT '住宿状态：未分配/已分配';

-- 尝试添加入学年份字段
ALTER TABLE student ADD COLUMN admission_year INT COMMENT '入学年份';

-- 尝试添加预先分配楼栋ID字段
ALTER TABLE student ADD COLUMN assigned_dorm_build_id INT COMMENT '预先分配的楼栋ID';

-- 尝试添加寝室长字段
ALTER TABLE student ADD COLUMN is_room_leader INT DEFAULT 0 COMMENT '是否为寝室长：0否，1是';

-- 更新现有学生的数据
UPDATE student SET 
    student_type = '老生',
    registration_status = '已报到',
    accommodation_status = CASE WHEN dorm_room_id IS NOT NULL THEN '已分配' ELSE '未分配' END
WHERE student_type IS NULL OR student_type = '';

-- 查看表结构确认
DESC student;
