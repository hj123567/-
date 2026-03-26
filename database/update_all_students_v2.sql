USE dormitory;
SET NAMES utf8mb4;

-- ==================== 楼栋 1 (计算机学院 - 男生) ====================
-- 共 122 个学生，分布在 1-6 楼，每层楼约 20 个学生

-- 1 楼房间 101-105 (住满 4 人)
UPDATE student SET name='张伟', gender='男', college='计算机学院', class_name='计科 2024-1 班' WHERE username='s101001';
UPDATE student SET name='李强', gender='男', college='计算机学院', class_name='计科 2024-1 班' WHERE username='s101002';
UPDATE student SET name='王磊', gender='男', college='计算机学院', class_name='计科 2024-1 班' WHERE username='s101003';
UPDATE student SET name='赵杰', gender='男', college='计算机学院', class_name='计科 2024-1 班' WHERE username='s101004';

UPDATE student SET name='刘洋', gender='男', college='计算机学院', class_name='计科 2024-1 班' WHERE username='s102001';
UPDATE student SET name='陈明', gender='男', college='计算机学院', class_name='计科 2024-1 班' WHERE username='s102002';
UPDATE student SET name='杨帆', gender='男', college='计算机学院', class_name='计科 2024-1 班' WHERE username='s102003';
UPDATE student SET name='黄涛', gender='男', college='计算机学院', class_name='计科 2024-1 班' WHERE username='s102004';

UPDATE student SET name='周军', gender='男', college='计算机学院', class_name='计科 2024-1 班' WHERE username='s103001';
UPDATE student SET name='吴斌', gender='男', college='计算机学院', class_name='计科 2024-1 班' WHERE username='s103002';
UPDATE student SET name='徐亮', gender='男', college='计算机学院', class_name='计科 2024-1 班' WHERE username='s103003';
UPDATE student SET name='孙飞', gender='男', college='计算机学院', class_name='计科 2024-1 班' WHERE username='s103004';

UPDATE student SET name='马超', gender='男', college='计算机学院', class_name='计科 2024-1 班' WHERE username='s104001';
UPDATE student SET name='朱勇', gender='男', college='计算机学院', class_name='计科 2024-1 班' WHERE username='s104002';
UPDATE student SET name='胡鹏', gender='男', college='计算机学院', class_name='计科 2024-1 班' WHERE username='s104003';
UPDATE student SET name='郭帅', gender='男', college='计算机学院', class_name='计科 2024-1 班' WHERE username='s104004';

UPDATE student SET name='何辉', gender='男', college='计算机学院', class_name='计科 2024-1 班' WHERE username='s105001';
UPDATE student SET name='高翔', gender='男', college='计算机学院', class_name='计科 2024-1 班' WHERE username='s105002';
UPDATE student SET name='林峰', gender='男', college='计算机学院', class_name='计科 2024-1 班' WHERE username='s105003';
UPDATE student SET name='梁波', gender='男', college='计算机学院', class_name='计科 2024-1 班' WHERE username='s105004';

-- 2 楼房间 201-204 (住满 4 人)
UPDATE student SET name='宋杰', gender='男', college='计算机学院', class_name='计科 2024-2 班' WHERE username='s201001';
UPDATE student SET name='谢磊', gender='男', college='计算机学院', class_name='计科 2024-2 班' WHERE username='s201002';
UPDATE student SET name='韩涛', gender='男', college='计算机学院', class_name='计科 2024-2 班' WHERE username='s201003';
UPDATE student SET name='冯强', gender='男', college='计算机学院', class_name='计科 2024-2 班' WHERE username='s201004';

UPDATE student SET name='邓明', gender='男', college='计算机学院', class_name='计科 2024-2 班' WHERE username='s202001';
UPDATE student SET name='曹军', gender='男', college='计算机学院', class_name='计科 2024-2 班' WHERE username='s202002';
UPDATE student SET name='彭飞', gender='男', college='计算机学院', class_name='计科 2024-2 班' WHERE username='s202003';
UPDATE student SET name='曾斌', gender='男', college='计算机学院', class_name='计科 2024-2 班' WHERE username='s202004';

UPDATE student SET name='萧亮', gender='男', college='计算机学院', class_name='计科 2024-2 班' WHERE username='s203001';
UPDATE student SET name='田勇', gender='男', college='计算机学院', class_name='计科 2024-2 班' WHERE username='s203002';
UPDATE student SET name='董帅', gender='男', college='计算机学院', class_name='计科 2024-2 班' WHERE username='s203003';
UPDATE student SET name='袁辉', gender='男', college='计算机学院', class_name='计科 2024-2 班' WHERE username='s203004';

UPDATE student SET name='潘翔', gender='男', college='计算机学院', class_name='计科 2024-2 班' WHERE username='s204001';
UPDATE student SET name='于峰', gender='男', college='计算机学院', class_name='计科 2024-2 班' WHERE username='s204002';
UPDATE student SET name='蒋波', gender='男', college='计算机学院', class_name='计科 2024-2 班' WHERE username='s204003';
UPDATE student SET name='蔡杰', gender='男', college='计算机学院', class_name='计科 2024-2 班' WHERE username='s204004';

-- 3 楼房间 301-305 (部分住 3-4 人)
UPDATE student SET name='余磊', gender='男', college='计算机学院', class_name='软件工程 2024-1 班' WHERE username='s301001';
UPDATE student SET name='卢涛', gender='男', college='计算机学院', class_name='软件工程 2024-1 班' WHERE username='s301002';
UPDATE student SET name='魏军', gender='男', college='计算机学院', class_name='软件工程 2024-1 班' WHERE username='s301003';
UPDATE student SET name='丁飞', gender='男', college='计算机学院', class_name='软件工程 2024-1 班' WHERE username='s301004';

UPDATE student SET name='吕斌', gender='男', college='计算机学院', class_name='软件工程 2024-1 班' WHERE username='s302001';
UPDATE student SET name='姚亮', gender='男', college='计算机学院', class_name='软件工程 2024-1 班' WHERE username='s302002';
UPDATE student SET name='康勇', gender='男', college='计算机学院', class_name='软件工程 2024-1 班' WHERE username='s302003';
UPDATE student SET name='贺帅', gender='男', college='计算机学院', class_name='软件工程 2024-1 班' WHERE username='s302004';

UPDATE student SET name='钱辉', gender='男', college='计算机学院', class_name='软件工程 2024-1 班' WHERE username='s303001';
UPDATE student SET name='严翔', gender='男', college='计算机学院', class_name='软件工程 2024-1 班' WHERE username='s303002';
UPDATE student SET name='莫峰', gender='男', college='计算机学院', class_name='软件工程 2024-1 班' WHERE username='s303003';
UPDATE student SET name='薛波', gender='男', college='计算机学院', class_name='软件工程 2024-1 班' WHERE username='s303004';

UPDATE student SET name='雷杰', gender='男', college='计算机学院', class_name='软件工程 2024-1 班' WHERE username='s304001';
UPDATE student SET name='史磊', gender='男', college='计算机学院', class_name='软件工程 2024-1 班' WHERE username='s304002';
UPDATE student SET name='顾涛', gender='男', college='计算机学院', class_name='软件工程 2024-1 班' WHERE username='s304003';
UPDATE student SET name='毛军', gender='男', college='计算机学院', class_name='软件工程 2024-1 班' WHERE username='s304004';

UPDATE student SET name='常飞', gender='男', college='计算机学院', class_name='软件工程 2024-2 班' WHERE username='s305001';
UPDATE student SET name='万斌', gender='男', college='计算机学院', class_name='软件工程 2024-2 班' WHERE username='s305002';
UPDATE student SET name='欧阳亮', gender='男', college='计算机学院', class_name='软件工程 2024-2 班' WHERE username='s305003';
-- 305 房间住 3 人

-- 4 楼房间 401-405 (部分住 2-3 人)
UPDATE student SET name='侯帅', gender='男', college='计算机学院', class_name='软件工程 2024-2 班' WHERE username='s401001';
UPDATE student SET name='龙辉', gender='男', college='计算机学院', class_name='软件工程 2024-2 班' WHERE username='s401002';
UPDATE student SET name='段翔', gender='男', college='计算机学院', class_name='软件工程 2024-2 班' WHERE username='s401003';
UPDATE student SET name='钱峰', gender='男', college='计算机学院', class_name='软件工程 2024-2 班' WHERE username='s401004';

UPDATE student SET name='孔波', gender='男', college='计算机学院', class_name='软件工程 2024-2 班' WHERE username='s402001';
UPDATE student SET name='白杰', gender='男', college='计算机学院', class_name='软件工程 2024-2 班' WHERE username='s402002';
UPDATE student SET name='崔磊', gender='男', college='计算机学院', class_name='软件工程 2024-2 班' WHERE username='s402003';
UPDATE student SET name='龚涛', gender='男', college='计算机学院', class_name='软件工程 2024-2 班' WHERE username='s402004';

UPDATE student SET name='孟军', gender='男', college='计算机学院', class_name='网络工程 2024-1 班' WHERE username='s403001';
UPDATE student SET name='秦飞', gender='男', college='计算机学院', class_name='网络工程 2024-1 班' WHERE username='s403002';
UPDATE student SET name='尹斌', gender='男', college='计算机学院', class_name='网络工程 2024-1 班' WHERE username='s403003';
-- 403 房间住 3 人

UPDATE student SET name='姜亮', gender='男', college='计算机学院', class_name='网络工程 2024-1 班' WHERE username='s404001';
UPDATE student SET name='文涛', gender='男', college='计算机学院', class_name='网络工程 2024-1 班' WHERE username='s404002';
-- 404 房间住 2 人

UPDATE student SET name='欧阳峰', gender='男', college='计算机学院', class_name='网络工程 2024-1 班' WHERE username='s405001';
UPDATE student SET name='上官杰', gender='男', college='计算机学院', class_name='网络工程 2024-1 班' WHERE username='s405002';
UPDATE student SET name='司徒磊', gender='男', college='计算机学院', class_name='网络工程 2024-1 班' WHERE username='s405003';
-- 405 房间住 3 人

-- 5 楼房间 501-506 (部分住 2-4 人)
UPDATE student SET name='诸葛明', gender='男', college='计算机学院', class_name='网络工程 2024-1 班' WHERE username='s501001';
UPDATE student SET name='东方斌', gender='男', college='计算机学院', class_name='网络工程 2024-1 班' WHERE username='s501002';
UPDATE student SET name='皇甫飞', gender='男', college='计算机学院', class_name='网络工程 2024-1 班' WHERE username='s501003';
UPDATE student SET name='令狐涛', gender='男', college='计算机学院', class_name='网络工程 2024-1 班' WHERE username='s501004';

UPDATE student SET name='慕容军', gender='男', college='计算机学院', class_name='网络工程 2024-2 班' WHERE username='s502001';
UPDATE student SET name='夏侯亮', gender='男', college='计算机学院', class_name='网络工程 2024-2 班' WHERE username='s502002';
UPDATE student SET name='诸葛亮', gender='男', college='计算机学院', class_name='网络工程 2024-2 班' WHERE username='s502003';
UPDATE student SET name='独孤勇', gender='男', college='计算机学院', class_name='网络工程 2024-2 班' WHERE username='s502004';

UPDATE student SET name='张峰', gender='男', college='计算机学院', class_name='网络工程 2024-2 班' WHERE username='s503001';
UPDATE student SET name='李波', gender='男', college='计算机学院', class_name='网络工程 2024-2 班' WHERE username='s503002';
UPDATE student SET name='王杰', gender='男', college='计算机学院', class_name='网络工程 2024-2 班' WHERE username='s503003';
UPDATE student SET name='赵磊', gender='男', college='计算机学院', class_name='网络工程 2024-2 班' WHERE username='s503004';

UPDATE student SET name='刘辉', gender='男', college='计算机学院', class_name='物联网 2024-1 班' WHERE username='s504001';
UPDATE student SET name='陈翔', gender='男', college='计算机学院', class_name='物联网 2024-1 班' WHERE username='s504002';
-- 504 房间住 2 人

UPDATE student SET name='杨涛', gender='男', college='计算机学院', class_name='物联网 2024-1 班' WHERE username='s505001';
UPDATE student SET name='黄峰', gender='男', college='计算机学院', class_name='物联网 2024-1 班' WHERE username='s505002';
UPDATE student SET name='周波', gender='男', college='计算机学院', class_name='物联网 2024-1 班' WHERE username='s505003';
-- 505 房间住 3 人

UPDATE student SET name='吴杰', gender='男', college='计算机学院', class_name='物联网 2024-1 班' WHERE username='s506001';
UPDATE student SET name='徐磊', gender='男', college='计算机学院', class_name='物联网 2024-1 班' WHERE username='s506002';
UPDATE student SET name='孙涛', gender='男', college='计算机学院', class_name='物联网 2024-1 班' WHERE username='s506003';
UPDATE student SET name='朱明', gender='男', college='计算机学院', class_name='物联网 2024-1 班' WHERE username='s506004';

-- 6 楼房间 601-602 (剩余学生)
UPDATE student SET name='胡飞', gender='男', college='计算机学院', class_name='物联网 2024-1 班' WHERE username='s601001';
UPDATE student SET name='郭斌', gender='男', college='计算机学院', class_name='物联网 2024-1 班' WHERE username='s601002';
UPDATE student SET name='何亮', gender='男', college='计算机学院', class_name='物联网 2024-1 班' WHERE username='s601003';
UPDATE student SET name='高峰', gender='男', college='计算机学院', class_name='物联网 2024-2 班' WHERE username='s601004';

UPDATE student SET name='林杰', gender='男', college='计算机学院', class_name='物联网 2024-2 班' WHERE username='s602001';
UPDATE student SET name='梁磊', gender='男', college='计算机学院', class_name='物联网 2024-2 班' WHERE username='s602002';
-- 602 房间住 2 人


-- ==================== 楼栋 2 (文学院 - 女生) ====================
-- 共 20 个学生，分布在 1 楼 5 个房间

UPDATE student SET name='张敏', gender='女', college='文学院', class_name='汉语言 2024-1 班' WHERE username='s1101001';
UPDATE student SET name='李娜', gender='女', college='文学院', class_name='汉语言 2024-1 班' WHERE username='s1101002';
UPDATE student SET name='王芳', gender='女', college='文学院', class_name='汉语言 2024-1 班' WHERE username='s1101003';
UPDATE student SET name='赵静', gender='女', college='文学院', class_name='汉语言 2024-1 班' WHERE username='s1101004';

UPDATE student SET name='刘娟', gender='女', college='文学院', class_name='汉语言 2024-1 班' WHERE username='s1102001';
UPDATE student SET name='陈丽', gender='女', college='文学院', class_name='汉语言 2024-1 班' WHERE username='s1102002';
UPDATE student SET name='杨秀英', gender='女', college='文学院', class_name='汉语言 2024-1 班' WHERE username='s1102003';
UPDATE student SET name='黄英', gender='女', college='文学院', class_name='汉语言 2024-1 班' WHERE username='s1102004';

UPDATE student SET name='周玲', gender='女', college='文学院', class_name='汉语言 2024-2 班' WHERE username='s1103001';
UPDATE student SET name='吴燕', gender='女', college='文学院', class_name='汉语言 2024-2 班' WHERE username='s1103002';
UPDATE student SET name='徐红', gender='女', college='文学院', class_name='汉语言 2024-2 班' WHERE username='s1103003';
UPDATE student SET name='孙丹', gender='女', college='文学院', class_name='汉语言 2024-2 班' WHERE username='s1103004';

UPDATE student SET name='马超', gender='女', college='文学院', class_name='汉语言 2024-2 班' WHERE username='s1104001';
UPDATE student SET name='朱婷', gender='女', college='文学院', class_name='汉语言 2024-2 班' WHERE username='s1104002';
UPDATE student SET name='胡琳', gender='女', college='文学院', class_name='汉语言 2024-2 班' WHERE username='s1104003';
UPDATE student SET name='郭雪', gender='女', college='文学院', class_name='汉语言 2024-2 班' WHERE username='s1104004';

UPDATE student SET name='何梅', gender='女', college='文学院', class_name='新闻学 2024-1 班' WHERE username='s1105001';
UPDATE student SET name='高霞', gender='女', college='文学院', class_name='新闻学 2024-1 班' WHERE username='s1105002';
UPDATE student SET name='林霞', gender='女', college='文学院', class_name='新闻学 2024-1 班' WHERE username='s1105003';
UPDATE student SET name='罗丹', gender='女', college='文学院', class_name='新闻学 2024-1 班' WHERE username='s1105004';


-- ==================== 楼栋 3 (理学院 - 男生) ====================
-- 共 20 个学生，分布在 1 楼 5 个房间

UPDATE student SET name='郑强', gender='男', college='理学院', class_name='数学 2024-1 班' WHERE username='s2101001';
UPDATE student SET name='冯伟', gender='男', college='理学院', class_name='数学 2024-1 班' WHERE username='s2101002';
UPDATE student SET name='褚杰', gender='男', college='理学院', class_name='数学 2024-1 班' WHERE username='s2101003';
UPDATE student SET name='卫勇', gender='男', college='理学院', class_name='数学 2024-1 班' WHERE username='s2101004';

UPDATE student SET name='蒋峰', gender='男', college='理学院', class_name='数学 2024-1 班' WHERE username='s2102001';
UPDATE student SET name='沈磊', gender='男', college='理学院', class_name='数学 2024-1 班' WHERE username='s2102002';
UPDATE student SET name='韩涛', gender='男', college='理学院', class_name='数学 2024-1 班' WHERE username='s2102003';
UPDATE student SET name='杨波', gender='男', college='理学院', class_name='数学 2024-1 班' WHERE username='s2102004';

UPDATE student SET name='朱杰', gender='男', college='理学院', class_name='物理 2024-1 班' WHERE username='s2103001';
UPDATE student SET name='秦磊', gender='男', college='理学院', class_name='物理 2024-1 班' WHERE username='s2103002';
UPDATE student SET name='尤涛', gender='男', college='理学院', class_name='物理 2024-1 班' WHERE username='s2103003';
UPDATE student SET name='许峰', gender='男', college='理学院', class_name='物理 2024-1 班' WHERE username='s2103004';

UPDATE student SET name='何波', gender='男', college='理学院', class_name='物理 2024-1 班' WHERE username='s2104001';
UPDATE student SET name='吕杰', gender='男', college='理学院', class_name='物理 2024-1 班' WHERE username='s2104002';
UPDATE student SET name='施磊', gender='男', college='理学院', class_name='物理 2024-1 班' WHERE username='s2104003';
UPDATE student SET name='张涛', gender='男', college='理学院', class_name='物理 2024-1 班' WHERE username='s2104004';

UPDATE student SET name='孔峰', gender='男', college='理学院', class_name='化学 2024-1 班' WHERE username='s2105001';
UPDATE student SET name='曹波', gender='男', college='理学院', class_name='化学 2024-1 班' WHERE username='s2105002';
UPDATE student SET name='邓峰', gender='男', college='理学院', class_name='化学 2024-1 班' WHERE username='s2105003';
UPDATE student SET name='洪磊', gender='男', college='理学院', class_name='化学 2024-1 班' WHERE username='s2105004';

SELECT '所有学生信息更新完成！' AS message;
