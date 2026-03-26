const mysql = require('mysql2/promise');

async function checkStudentRooms() {
    const connection = await mysql.createConnection({
        host: 'localhost',
        user: 'root',
        password: '123456',
        database: 'dormitory'
    });

    try {
        console.log('查看学生房间分配...');
        const [students] = await connection.execute(`
            SELECT username, name, dorm_build_id, dorm_room_id 
            FROM student 
            WHERE dorm_room_id IS NOT NULL 
            LIMIT 10
        `);
        console.log('学生房间分配:', students);

    } catch (error) {
        console.error('执行出错:', error);
    } finally {
        await connection.end();
    }
}

checkStudentRooms();
