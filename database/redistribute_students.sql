USE dormitory;
SET NAMES utf8mb4;

-- 重新分配学生到不同楼层，确保每层楼都有人，每栋楼都有几个房间人少

-- ==================== 楼栋 1 (计算机学院 - 122 个男生) ====================
-- 1 楼：10 个房间住满 (40 人)
UPDATE student SET dorm_room_id=101 WHERE username='s101001';
UPDATE student SET dorm_room_id=101 WHERE username='s101002';
UPDATE student SET dorm_room_id=101 WHERE username='s101003';
UPDATE student SET dorm_room_id=101 WHERE username='s101004';

UPDATE student SET dorm_room_id=102 WHERE username='s102001';
UPDATE student SET dorm_room_id=102 WHERE username='s102002';
UPDATE student SET dorm_room_id=102 WHERE username='s102003';
UPDATE student SET dorm_room_id=102 WHERE username='s102004';

UPDATE student SET dorm_room_id=103 WHERE username='s103001';
UPDATE student SET dorm_room_id=103 WHERE username='s103002';
UPDATE student SET dorm_room_id=103 WHERE username='s103003';
UPDATE student SET dorm_room_id=103 WHERE username='s103004';

UPDATE student SET dorm_room_id=104 WHERE username='s104001';
UPDATE student SET dorm_room_id=104 WHERE username='s104002';
UPDATE student SET dorm_room_id=104 WHERE username='s104003';
UPDATE student SET dorm_room_id=104 WHERE username='s104004';

UPDATE student SET dorm_room_id=105 WHERE username='s105001';
UPDATE student SET dorm_room_id=105 WHERE username='s105002';
UPDATE student SET dorm_room_id=105 WHERE username='s105003';
UPDATE student SET dorm_room_id=105 WHERE username='s105004';

UPDATE student SET dorm_room_id=106 WHERE username='s106001';
UPDATE student SET dorm_room_id=106 WHERE username='s106002';
UPDATE student SET dorm_room_id=106 WHERE username='s106003';
UPDATE student SET dorm_room_id=106 WHERE username='s106004';

UPDATE student SET dorm_room_id=107 WHERE username='s107001';
UPDATE student SET dorm_room_id=107 WHERE username='s107002';
UPDATE student SET dorm_room_id=107 WHERE username='s107003';
UPDATE student SET dorm_room_id=107 WHERE username='s107004';

UPDATE student SET dorm_room_id=108 WHERE username='s108001';
UPDATE student SET dorm_room_id=108 WHERE username='s108002';
UPDATE student SET dorm_room_id=108 WHERE username='s108003';
UPDATE student SET dorm_room_id=108 WHERE username='s108004';

UPDATE student SET dorm_room_id=109 WHERE username='s109001';
UPDATE student SET dorm_room_id=109 WHERE username='s109002';
UPDATE student SET dorm_room_id=109 WHERE username='s109003';
UPDATE student SET dorm_room_id=109 WHERE username='s109004';

UPDATE student SET dorm_room_id=110 WHERE username='s110001';
UPDATE student SET dorm_room_id=110 WHERE username='s110002';
UPDATE student SET dorm_room_id=110 WHERE username='s110003';
UPDATE student SET dorm_room_id=110 WHERE username='s110004';

-- 2 楼：8 个房间住满 (32 人)
UPDATE student SET dorm_room_id=201 WHERE username='s111001';
UPDATE student SET dorm_room_id=201 WHERE username='s111002';
UPDATE student SET dorm_room_id=201 WHERE username='s111003';
UPDATE student SET dorm_room_id=201 WHERE username='s111004';

UPDATE student SET dorm_room_id=202 WHERE username='s112001';
UPDATE student SET dorm_room_id=202 WHERE username='s112002';
UPDATE student SET dorm_room_id=202 WHERE username='s112003';
UPDATE student SET dorm_room_id=202 WHERE username='s112004';

UPDATE student SET dorm_room_id=203 WHERE username='s113001';
UPDATE student SET dorm_room_id=203 WHERE username='s113002';
UPDATE student SET dorm_room_id=203 WHERE username='s113003';
UPDATE student SET dorm_room_id=203 WHERE username='s113004';

UPDATE student SET dorm_room_id=204 WHERE username='s114001';
UPDATE student SET dorm_room_id=204 WHERE username='s114002';
UPDATE student SET dorm_room_id=204 WHERE username='s114003';
UPDATE student SET dorm_room_id=204 WHERE username='s114004';

UPDATE student SET dorm_room_id=205 WHERE username='s115001';
UPDATE student SET dorm_room_id=205 WHERE username='s115002';
UPDATE student SET dorm_room_id=205 WHERE username='s115003';
UPDATE student SET dorm_room_id=205 WHERE username='s115004';

UPDATE student SET dorm_room_id=206 WHERE username='s116001';
UPDATE student SET dorm_room_id=206 WHERE username='s116002';
UPDATE student SET dorm_room_id=206 WHERE username='s116003';
UPDATE student SET dorm_room_id=206 WHERE username='s116004';

UPDATE student SET dorm_room_id=207 WHERE username='s201001';
UPDATE student SET dorm_room_id=207 WHERE username='s201002';
UPDATE student SET dorm_room_id=207 WHERE username='s201003';
UPDATE student SET dorm_room_id=207 WHERE username='s201004';

UPDATE student SET dorm_room_id=208 WHERE username='s202001';
UPDATE student SET dorm_room_id=208 WHERE username='s202002';
UPDATE student SET dorm_room_id=208 WHERE username='s202003';
UPDATE student SET dorm_room_id=208 WHERE username='s202004';

-- 3 楼：6 个房间，部分住 3-4 人 (20 人)
UPDATE student SET dorm_room_id=301 WHERE username='s203001';
UPDATE student SET dorm_room_id=301 WHERE username='s203002';
UPDATE student SET dorm_room_id=301 WHERE username='s203003';
UPDATE student SET dorm_room_id=301 WHERE username='s203004';

UPDATE student SET dorm_room_id=302 WHERE username='s204001';
UPDATE student SET dorm_room_id=302 WHERE username='s204002';
UPDATE student SET dorm_room_id=302 WHERE username='s204003';
UPDATE student SET dorm_room_id=302 WHERE username='s204004';

UPDATE student SET dorm_room_id=303 WHERE username='s205001';
UPDATE student SET dorm_room_id=303 WHERE username='s205002';
UPDATE student SET dorm_room_id=303 WHERE username='s205003';
-- 303 房间住 3 人

UPDATE student SET dorm_room_id=304 WHERE username='s206001';
UPDATE student SET dorm_room_id=304 WHERE username='s206002';
UPDATE student SET dorm_room_id=304 WHERE username='s206003';
UPDATE student SET dorm_room_id=304 WHERE username='s206004';

UPDATE student SET dorm_room_id=305 WHERE username='s207001';
UPDATE student SET dorm_room_id=305 WHERE username='s207002';
UPDATE student SET dorm_room_id=305 WHERE username='s207003';
-- 305 房间住 3 人

UPDATE student SET dorm_room_id=306 WHERE username='s208001';
UPDATE student SET dorm_room_id=306 WHERE username='s208002';
UPDATE student SET dorm_room_id=306 WHERE username='s208003';
UPDATE student SET dorm_room_id=306 WHERE username='s208004';

-- 4 楼：4 个房间，部分住 2-3 人 (14 人)
UPDATE student SET dorm_room_id=401 WHERE username='s209001';
UPDATE student SET dorm_room_id=401 WHERE username='s209002';
UPDATE student SET dorm_room_id=401 WHERE username='s209003';
UPDATE student SET dorm_room_id=401 WHERE username='s209004';

UPDATE student SET dorm_room_id=402 WHERE username='s210001';
UPDATE student SET dorm_room_id=402 WHERE username='s210002';
UPDATE student SET dorm_room_id=402 WHERE username='s210003';
UPDATE student SET dorm_room_id=402 WHERE username='s210004';

UPDATE student SET dorm_room_id=403 WHERE username='s211001';
UPDATE student SET dorm_room_id=403 WHERE username='s211002';
UPDATE student SET dorm_room_id=403 WHERE username='s211003';
-- 403 房间住 3 人

UPDATE student SET dorm_room_id=404 WHERE username='s212001';
UPDATE student SET dorm_room_id=404 WHERE username='s212002';
-- 404 房间住 2 人

UPDATE student SET dorm_room_id=405 WHERE username='s213001';
UPDATE student SET dorm_room_id=405 WHERE username='s213002';
UPDATE student SET dorm_room_id=405 WHERE username='s213003';
-- 405 房间住 3 人

-- 5 楼：3 个房间，部分住 2-3 人 (10 人)
UPDATE student SET dorm_room_id=501 WHERE username='s214001';
UPDATE student SET dorm_room_id=501 WHERE username='s214002';
UPDATE student SET dorm_room_id=501 WHERE username='s214003';
UPDATE student SET dorm_room_id=501 WHERE username='s214004';

UPDATE student SET dorm_room_id=502 WHERE username='s215001';
UPDATE student SET dorm_room_id=502 WHERE username='s215002';
UPDATE student SET dorm_room_id=502 WHERE username='s215003';
-- 502 房间住 3 人

UPDATE student SET dorm_room_id=503 WHERE username='s216001';
UPDATE student SET dorm_room_id=503 WHERE username='s216002';
-- 503 房间住 2 人

UPDATE student SET dorm_room_id=504 WHERE username='s301001';
UPDATE student SET dorm_room_id=504 WHERE username='s301002';
UPDATE student SET dorm_room_id=504 WHERE username='s301003';
UPDATE student SET dorm_room_id=504 WHERE username='s301004';

-- 6 楼：2 个房间住满 (6 人)
UPDATE student SET dorm_room_id=601 WHERE username='s302001';
UPDATE student SET dorm_room_id=601 WHERE username='s302002';
UPDATE student SET dorm_room_id=601 WHERE username='s302003';
UPDATE student SET dorm_room_id=601 WHERE username='s302004';

UPDATE student SET dorm_room_id=602 WHERE username='s303001';
UPDATE student SET dorm_room_id=602 WHERE username='s303002';
-- 602 房间住 2 人


-- ==================== 楼栋 2 (文学院 - 20 个女生) ====================
-- 1 楼：5 个房间住满
UPDATE student SET dorm_room_id=1101 WHERE username='s1101001';
UPDATE student SET dorm_room_id=1101 WHERE username='s1101002';
UPDATE student SET dorm_room_id=1101 WHERE username='s1101003';
UPDATE student SET dorm_room_id=1101 WHERE username='s1101004';

UPDATE student SET dorm_room_id=1102 WHERE username='s1102001';
UPDATE student SET dorm_room_id=1102 WHERE username='s1102002';
UPDATE student SET dorm_room_id=1102 WHERE username='s1102003';
UPDATE student SET dorm_room_id=1102 WHERE username='s1102004';

UPDATE student SET dorm_room_id=1103 WHERE username='s1103001';
UPDATE student SET dorm_room_id=1103 WHERE username='s1103002';
UPDATE student SET dorm_room_id=1103 WHERE username='s1103003';
UPDATE student SET dorm_room_id=1103 WHERE username='s1103004';

UPDATE student SET dorm_room_id=1104 WHERE username='s1104001';
UPDATE student SET dorm_room_id=1104 WHERE username='s1104002';
UPDATE student SET dorm_room_id=1104 WHERE username='s1104003';
UPDATE student SET dorm_room_id=1104 WHERE username='s1104004';

UPDATE student SET dorm_room_id=1105 WHERE username='s1105001';
UPDATE student SET dorm_room_id=1105 WHERE username='s1105002';
UPDATE student SET dorm_room_id=1105 WHERE username='s1105003';
UPDATE student SET dorm_room_id=1105 WHERE username='s1105004';


-- ==================== 楼栋 3 (理学院 - 20 个男生) ====================
-- 1 楼：5 个房间住满
UPDATE student SET dorm_room_id=2101 WHERE username='s2101001';
UPDATE student SET dorm_room_id=2101 WHERE username='s2101002';
UPDATE student SET dorm_room_id=2101 WHERE username='s2101003';
UPDATE student SET dorm_room_id=2101 WHERE username='s2101004';

UPDATE student SET dorm_room_id=2102 WHERE username='s2102001';
UPDATE student SET dorm_room_id=2102 WHERE username='s2102002';
UPDATE student SET dorm_room_id=2102 WHERE username='s2102003';
UPDATE student SET dorm_room_id=2102 WHERE username='s2102004';

UPDATE student SET dorm_room_id=2103 WHERE username='s2103001';
UPDATE student SET dorm_room_id=2103 WHERE username='s2103002';
UPDATE student SET dorm_room_id=2103 WHERE username='s2103003';
UPDATE student SET dorm_room_id=2103 WHERE username='s2103004';

UPDATE student SET dorm_room_id=2104 WHERE username='s2104001';
UPDATE student SET dorm_room_id=2104 WHERE username='s2104002';
UPDATE student SET dorm_room_id=2104 WHERE username='s2104003';
UPDATE student SET dorm_room_id=2104 WHERE username='s2104004';

UPDATE student SET dorm_room_id=2105 WHERE username='s2105001';
UPDATE student SET dorm_room_id=2105 WHERE username='s2105002';
UPDATE student SET dorm_room_id=2105 WHERE username='s2105003';
UPDATE student SET dorm_room_id=2105 WHERE username='s2105004';

-- 验证更新结果
SELECT '学生房间重新分配完成！' AS message;
SELECT dormbuild_id AS '楼栋', floor_num AS '楼层', 
       COUNT(CASE WHEN (SELECT COUNT(*) FROM student s WHERE s.dorm_room_id=r.dormroom_id) > 0 THEN 1 END) AS '有人房间数',
       SUM((SELECT COUNT(*) FROM student s WHERE s.dorm_room_id=r.dormroom_id)) AS '总学生数' 
FROM dorm_room r 
GROUP BY dormbuild_id, floor_num 
ORDER BY dormbuild_id, floor_num;
