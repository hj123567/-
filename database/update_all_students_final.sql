USE dormitory;
SET NAMES utf8mb4;

-- 楼栋 1（计算机学院 - 男生）- 1 楼房间 101-116
UPDATE student SET name='张伟', gender='男', college='计算机学院', class_name='计科 1 班' WHERE username='s20241001';
UPDATE student SET name='李强', gender='男', college='计算机学院', class_name='计科 1 班' WHERE username='s20241002';
UPDATE student SET name='王磊', gender='男', college='计算机学院', class_name='计科 1 班' WHERE username='s20241003';
UPDATE student SET name='赵杰', gender='男', college='计算机学院', class_name='计科 1 班' WHERE username='s20241004';

UPDATE student SET name='刘洋', gender='男', college='计算机学院', class_name='计科 1 班' WHERE username='s20241005';
UPDATE student SET name='陈明', gender='男', college='计算机学院', class_name='计科 1 班' WHERE username='s20241006';
UPDATE student SET name='杨帆', gender='男', college='计算机学院', class_name='计科 1 班' WHERE username='s20241007';
UPDATE student SET name='黄涛', gender='男', college='计算机学院', class_name='计科 1 班' WHERE username='s20241008';

UPDATE student SET name='周军', gender='男', college='计算机学院', class_name='计科 1 班' WHERE username='s20241009';
UPDATE student SET name='吴斌', gender='男', college='计算机学院', class_name='计科 1 班' WHERE username='s20241010';
UPDATE student SET name='徐亮', gender='男', college='计算机学院', class_name='计科 1 班' WHERE username='s20241011';
UPDATE student SET name='孙飞', gender='男', college='计算机学院', class_name='计科 1 班' WHERE username='s20241012';

UPDATE student SET name='马超', gender='男', college='计算机学院', class_name='计科 1 班' WHERE username='s20241013';
UPDATE student SET name='朱勇', gender='男', college='计算机学院', class_name='计科 1 班' WHERE username='s20241014';
UPDATE student SET name='胡鹏', gender='男', college='计算机学院', class_name='计科 1 班' WHERE username='s20241015';
UPDATE student SET name='郭帅', gender='男', college='计算机学院', class_name='计科 1 班' WHERE username='s20241016';

SELECT 'Updated building 1, floor 1' as result;
