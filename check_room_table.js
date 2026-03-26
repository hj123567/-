const mysql = require('mysql2/promise');

async function checkRoomTable() {
    const connection = await mysql.createConnection({
        host: 'localhost',
        user: 'root',
        password: '123456',
        database: 'dormitory'
    });

    try {
        console.log('查看房间表结构...');
        const [columns] = await connection.execute('DESCRIBE dorm_room');
        console.log('房间表字段:', columns);

        console.log('\n查看部分房间数据...');
        const [rooms] = await connection.execute('SELECT * FROM dorm_room LIMIT 10');
        console.log('房间数据:', rooms);

    } catch (error) {
        console.error('执行出错:', error);
    } finally {
        await connection.end();
    }
}

checkRoomTable();
