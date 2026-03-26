const mysql = require('mysql2');
const fs = require('fs');
const path = require('path');

console.log('Updating student data...');

const connection = mysql.createConnection({
    host: '127.0.0.1',
    user: 'root',
    password: '123456',
    database: 'dormitory'
});

connection.connect((err) => {
    if (err) {
        console.error('Error connecting: ' + err.stack);
        return;
    }
    console.log('Connected!');
    
    const studentData = [
        ['Stest', '计算机学院', '计科2001', 1, 1101],
        ['stu1', '计算机学院', '计科2001', 1, 1101],
        ['stu10', '外国语学院', '英语2002', 2, 2101],
        ['stu11', '外国语学院', '英语2002', 2, 2101],
        ['stu12', '文学院', '中文2001', 2, 2201],
        ['stu13', '文学院', '中文2001', 2, 2201],
        ['stu14', '理学院', '数学2001', 4, 4101],
        ['stu15', '理学院', '物理2001', 3, 3101],
        ['stu16', '计算机学院', '计科2002', 1, 1201],
        ['stu17', '计算机学院', '计科2002', 1, 1201],
        ['stu18', '外国语学院', '英语2001', 2, 2102],
        ['stu19', '理学院', '物理2001', 3, 3102],
        ['stu2', '计算机学院', '计科2001', 1, 1101],
        ['stu20', '文学院', '中文2002', 2, 2202],
        ['stu21', '理学院', '数学2001', 4, 4102],
        ['stu22', '计算机学院', '计科2001', 1, 1101],
        ['stu3', '外国语学院', '英语2001', 2, 2102],
        ['stu4', '计算机学院', '计科2001', 1, 1101],
        ['stu5', '理学院', '物理2002', 3, 3201],
        ['stu6', '文学院', '中文2001', 2, 2201],
        ['newstu2024002', '信息工程学院', '软件工程2024-1班', 1, 1102]
    ];
    
    let completed = 0;
    const total = studentData.length;
    
    studentData.forEach((data, index) => {
        const updateSql = 'UPDATE student SET college = ?, class_name = ?, dorm_build_id = ?, dorm_room_id = ? WHERE username = ?';
        connection.query(updateSql, [data[1], data[2], data[3], data[4], data[0]], (err, result) => {
            completed++;
            if (err) {
                console.error(`Error updating ${data[0]}:`, err);
            } else {
                console.log(`Updated ${data[0]} successfully!`);
            }
            
            if (completed === total) {
                console.log('\nAll student data updated!');
                connection.end();
                
                // Update the Student entity file
                console.log('\nUpdating Student entity...');
                const studentEntityPath = path.join(__dirname, 'Dormitory_business', 'src', 'main', 'java', 'com', 'example', 'springboot', 'entity', 'Student.java');
                
                let content = fs.readFileSync(studentEntityPath, 'utf8');
                content = content.replace(/@TableField\(value = "college", exist = false\)/g, '@TableField("college")');
                content = content.replace(/@TableField\(value = "class_name", exist = false\)/g, '@TableField("class_name")');
                content = content.replace(/@TableField\(value = "dorm_build_id", exist = false\)/g, '@TableField("dorm_build_id")');
                content = content.replace(/@TableField\(value = "dorm_room_id", exist = false\)/g, '@TableField("dorm_room_id")');
                
                fs.writeFileSync(studentEntityPath, content, 'utf8');
                console.log('Student entity updated!');
                console.log('\nNow please restart the backend service!');
            }
        });
    });
});
