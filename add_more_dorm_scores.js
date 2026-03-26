const mysql = require('mysql2/promise');

async function addMoreDormScores() {
    const connection = await mysql.createConnection({
        host: 'localhost',
        user: 'root',
        password: '123456',
        database: 'dormitory'
    });

    try {
        console.log('开始添加更多卫生评分数据...');

        const insertSQL = `
            INSERT INTO dorm_score (dorm_build_id, dorm_room_id, score, comment, score_time, scorer) VALUES
            (1, 101, 95, '宿舍非常干净，物品摆放整齐', '2026-03-26 10:30:00', '张宿管'),
            (1, 101, 88, '卫生良好，继续保持', '2026-03-25 10:35:00', '李宿管'),
            (1, 102, 92, '整洁有序，值得表扬', '2026-03-26 11:00:00', '张宿管'),
            (1, 102, 85, '整体不错，继续加油', '2026-03-25 11:05:00', '李宿管'),
            (1, 103, 90, '干净卫生，表现优秀', '2026-03-26 14:00:00', '张宿管'),
            (1, 104, 87, '还可以，需要改进', '2026-03-26 14:30:00', '张宿管'),
            (1, 105, 93, '非常整洁，继续保持', '2026-03-26 15:00:00', '李宿管'),
            (1, 106, 80, '基本合格，再努力', '2026-03-26 15:30:00', '李宿管'),
            (1, 107, 94, '非常棒，值得学习', '2026-03-26 16:00:00', '张宿管'),
            (1, 108, 86, '整体还行，继续加油', '2026-03-26 16:30:00', '张宿管')
        `;
        const [result] = await connection.execute(insertSQL);
        console.log('成功添加', result.affectedRows, '条评分记录');

        const [finalData] = await connection.execute('SELECT * FROM dorm_score ORDER BY id');
        console.log('最终评分数据:', finalData);
        console.log('\n✅ 更多卫生评分数据添加完成！');

    } catch (error) {
        console.error('执行出错:', error);
    } finally {
        await connection.end();
    }
}

addMoreDormScores();
