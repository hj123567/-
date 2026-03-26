USE dormitory;
SET NAMES utf8mb4;

-- 查看当前的报修数据
SELECT * FROM repair ORDER BY id;

-- 选项1：删除所有旧的报修数据（推荐）
DELETE FROM repair;

-- 重置自增ID
ALTER TABLE repair AUTO_INCREMENT = 1;

-- 查看删除后的数据（应该为空）
SELECT * FROM repair ORDER BY id;

SELECT '报修数据已清空，可以重新开始测试！' AS message;
