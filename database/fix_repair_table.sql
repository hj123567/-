USE dormitory;

-- 修改 repair 表的 state 字段类型为 varchar
ALTER TABLE repair MODIFY COLUMN state VARCHAR(50) NOT NULL DEFAULT '待处理';

-- 查看修改后的表结构
DESCRIBE repair;
