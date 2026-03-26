const mysql = require('mysql2/promise');

async function clearRepairData() {
    const connection = await mysql.createConnection({
        host: 'localhost',
        user: 'root',
        password: '123456',
        database: 'dormitory'
    });

    try {
        console.log('开始清空报修数据...');

        // 查看当前的报修数据
        const [currentData] = await connection.execute('SELECT * FROM repair ORDER BY id');
        console.log('当前报修数据:', currentData);

        // 删除所有旧的报修数据
        const [deleteResult] = await connection.execute('DELETE FROM repair');
        console.log('删除了', deleteResult.affectedRows, '条报修记录');

        // 重置自增ID
        await connection.execute('ALTER TABLE repair AUTO_INCREMENT = 1');
        console.log('已重置自增ID');

        // 查看删除后的数据
        const [newData] = await connection.execute('SELECT * FROM repair ORDER BY id');
        console.log('清空后的数据:', newData);

        console.log('\n✅ 报修数据已清空，可以重新开始测试！');

    } catch (error) {
        console.error('执行出错:', error);
    } finally {
        await connection.end();
    }
}

clearRepairData();
