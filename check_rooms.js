const mysql = require('mysql2/promise');

async function checkRooms() {
    const connection = await mysql.createConnection({
        host: 'localhost',
        user: 'root',
        password: '123456',
        database: 'dormitory'
    });

    try {
        console.log('查看房间数据...');
        const [rooms] = await connection.execute('SELECT dorm_build_id, dorm_room_id, floor_num FROM dorm_room WHERE dorm_build_id = 1 LIMIT 20');
        console.log('1号楼的房间:', rooms);

    } catch (error) {
        console.error('执行出错:', error);
    } finally {
        await connection.end();
    }
}

checkRooms();
