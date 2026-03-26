chcp 65001

$mysql = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
$cmd = "& `"$mysql`" -u root -p123456 --default-character-set=utf8mb4 dormitory -e `"UPDATE student SET name='张伟', gender='男', college='计算机学院', class_name='计科 2024-1 班' WHERE username='s101001'; UPDATE student SET name='李强', gender='男', college='计算机学院', class_name='计科 2024-1 班' WHERE username='s101002'; UPDATE student SET name='王磊', gender='男', college='计算机学院', class_name='计科 2024-1 班' WHERE username='s101003'; UPDATE student SET name='赵杰', gender='男', college='计算机学院', class_name='计科 2024-1 班' WHERE username='s101004'; SELECT 'Updated 4 students' as result;`""
Invoke-Expression $cmd
