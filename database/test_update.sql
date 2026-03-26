USE dormitory;

-- 直接更新几个学生验证
UPDATE student SET name='张伟', gender='男', college='计算机学院', class_name='计科 2024-1 班' WHERE username='s101001';
UPDATE student SET name='李强', gender='男', college='计算机学院', class_name='计科 2024-1 班' WHERE username='s101002';
UPDATE student SET name='王磊', gender='男', college='计算机学院', class_name='计科 2024-1 班' WHERE username='s101003';
UPDATE student SET name='赵杰', gender='男', college='计算机学院', class_name='计科 2024-1 班' WHERE username='s101004';

SELECT username, name, HEX(name) as hex FROM student WHERE username='s101001';
