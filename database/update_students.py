import subprocess

# 定义学生数据
students_data = []

# 楼栋 1（计算机学院 - 男生）- 122 人
# 1 楼：16 个房间，64 人
for i in range(1, 65):
    students_data.append({
        'old_id': f's{i:06d}' if i <= 16 else f's{(i//16+1)*100000 + (i%16):06d}',
        'new_id': f's2024{i:04d}',
        'name': f'学生{i}',
        'gender': '男',
        'college': '计算机学院',
        'class_name': f'计科{(i-1)//16 + 1}班'
    })

# 生成 UPDATE 语句
update_statements = []
for student in students_data[:20]:  # 先更新前 20 个作为测试
    update_sql = f"UPDATE student SET username='{student['new_id']}', name='{student['name']}', gender='{student['gender']}', college='{student['college']}', class_name='{student['class_name']}' WHERE username='{student['old_id']}' OR username='{student['new_id']}';"
    update_statements.append(update_sql)

# 执行 SQL
mysql_cmd = r"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
cmd = f'"{mysql_cmd}" -u root -p123456 --default-character-set=utf8mb4 dormitory -e "' + "; ".join(update_statements) + '"'

print(f"Executing: {cmd[:200]}...")
result = subprocess.run(cmd, shell=True, capture_output=True, text=True, encoding='utf-8')
print(result.stdout)
print(result.stderr)
