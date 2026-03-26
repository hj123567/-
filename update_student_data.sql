-- 更新学生表，添加学院、班级、宿舍楼号和房间号
-- 首先检查是否存在这些字段，如果不存在则添加
ALTER TABLE `student` 
ADD COLUMN IF NOT EXISTS `college` varchar(255) NULL DEFAULT NULL COMMENT '学院' AFTER `avatar`,
ADD COLUMN IF NOT EXISTS `class_name` varchar(255) NULL DEFAULT NULL COMMENT '班级' AFTER `college`,
ADD COLUMN IF NOT EXISTS `dorm_build_id` int(11) NULL DEFAULT NULL COMMENT '宿舍楼号' AFTER `class_name`,
ADD COLUMN IF NOT EXISTS `dorm_room_id` int(11) NULL DEFAULT NULL COMMENT '房间号' AFTER `dorm_build_id`;

-- 更新现有学生数据
UPDATE `student` SET `college` = '计算机学院', `class_name` = '计科2001', `dorm_build_id` = 1, `dorm_room_id` = 1101 WHERE `username` = 'Stest';
UPDATE `student` SET `college` = '计算机学院', `class_name` = '计科2001', `dorm_build_id` = 1, `dorm_room_id` = 1101 WHERE `username` = 'stu1';
UPDATE `student` SET `college` = '外国语学院', `class_name` = '英语2002', `dorm_build_id` = 2, `dorm_room_id` = 2101 WHERE `username` = 'stu10';
UPDATE `student` SET `college` = '外国语学院', `class_name` = '英语2002', `dorm_build_id` = 2, `dorm_room_id` = 2101 WHERE `username` = 'stu11';
UPDATE `student` SET `college` = '文学院', `class_name` = '中文2001', `dorm_build_id` = 2, `dorm_room_id` = 2201 WHERE `username` = 'stu12';
UPDATE `student` SET `college` = '文学院', `class_name` = '中文2001', `dorm_build_id` = 2, `dorm_room_id` = 2201 WHERE `username` = 'stu13';
UPDATE `student` SET `college` = '理学院', `class_name` = '数学2001', `dorm_build_id` = 4, `dorm_room_id` = 4101 WHERE `username` = 'stu14';
UPDATE `student` SET `college` = '理学院', `class_name` = '物理2001', `dorm_build_id` = 3, `dorm_room_id` = 3101 WHERE `username` = 'stu15';
UPDATE `student` SET `college` = '计算机学院', `class_name` = '计科2002', `dorm_build_id` = 1, `dorm_room_id` = 1201 WHERE `username` = 'stu16';
UPDATE `student` SET `college` = '计算机学院', `class_name` = '计科2002', `dorm_build_id` = 1, `dorm_room_id` = 1201 WHERE `username` = 'stu17';
UPDATE `student` SET `college` = '外国语学院', `class_name` = '英语2001', `dorm_build_id` = 2, `dorm_room_id` = 2102 WHERE `username` = 'stu18';
UPDATE `student` SET `college` = '理学院', `class_name` = '物理2001', `dorm_build_id` = 3, `dorm_room_id` = 3102 WHERE `username` = 'stu19';
UPDATE `student` SET `college` = '计算机学院', `class_name` = '计科2001', `dorm_build_id` = 1, `dorm_room_id` = 1101 WHERE `username` = 'stu2';
UPDATE `student` SET `college` = '文学院', `class_name` = '中文2002', `dorm_build_id` = 2, `dorm_room_id` = 2202 WHERE `username` = 'stu20';
UPDATE `student` SET `college` = '理学院', `class_name` = '数学2001', `dorm_build_id` = 4, `dorm_room_id` = 4102 WHERE `username` = 'stu21';
UPDATE `student` SET `college` = '计算机学院', `class_name` = '计科2001', `dorm_build_id` = 1, `dorm_room_id` = 1101 WHERE `username` = 'stu22';
UPDATE `student` SET `college` = '外国语学院', `class_name` = '英语2001', `dorm_build_id` = 2, `dorm_room_id` = 2102 WHERE `username` = 'stu3';
UPDATE `student` SET `college` = '计算机学院', `class_name` = '计科2001', `dorm_build_id` = 1, `dorm_room_id` = 1101 WHERE `username` = 'stu4';
UPDATE `student` SET `college` = '理学院', `class_name` = '物理2002', `dorm_build_id` = 3, `dorm_room_id` = 3201 WHERE `username` = 'stu5';
UPDATE `student` SET `college` = '文学院', `class_name` = '中文2001', `dorm_build_id` = 2, `dorm_room_id` = 2201 WHERE `username` = 'stu6';

-- 同时更新新增的学生
UPDATE `student` SET `college` = '信息工程学院', `class_name` = '软件工程2024-1班', `dorm_build_id` = 1, `dorm_room_id` = 1102 WHERE `username` = 'newstu2024002';
