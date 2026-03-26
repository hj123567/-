USE dormitory;
SET NAMES utf8mb4;

-- 为每个寝室设置第一个学生为寝室长
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

-- 验证结果
SELECT dorm_build_id, dorm_room_id, username, name, is_room_leader 
FROM student 
WHERE is_room_leader = 1
ORDER BY dorm_build_id, dorm_room_id;

SELECT '寝室长设置完成！' AS message;
