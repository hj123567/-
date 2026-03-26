const mysql = require('mysql2/promise');

async function initDormScore() {
    const connection = await mysql.createConnection({
        host: 'localhost',
        user: 'root',
        password: '123456',
        database: 'dormitory'
    });

    try {
        console.log('开始检查 dorm_score 表...');

        // 检查表是否存在
        const [tables] = await connection.execute("SHOW TABLES LIKE 'dorm_score'");
        
        if (tables.length === 0) {
            console.log('表不存在，正在创建...');
            
            // 创建表
            const createTableSQL = `
                CREATE TABLE dorm_score (
                    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键 ID',
                    dorm_build_id INT NOT NULL COMMENT '楼栋号',
                    dorm_room_id INT NOT NULL COMMENT '房间号',
                    score INT NOT NULL COMMENT '评分 (0-100)',
                    comment VARCHAR(500) COMMENT '评语',
                    score_time VARCHAR(50) COMMENT '评分时间',
                    scorer VARCHAR(50) COMMENT '评分人',
                    INDEX idx_build_id (dorm_build_id),
                    INDEX idx_room_id (dorm_room_id),
                    INDEX idx_score_time (score_time)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='卫生评分表'
            `;
            await connection.execute(createTableSQL);
            console.log('表创建成功！');
        } else {
            console.log('表已存在！');
        }

        // 查看当前数据
        const [currentData] = await connection.execute('SELECT * FROM dorm_score ORDER BY id');
        console.log('当前评分数据:', currentData);

        if (currentData.length === 0) {
            console.log('正在添加测试数据...');
            
            // 添加测试数据
            const insertSQL = `
                INSERT INTO dorm_score (dorm_build_id, dorm_room_id, score, comment, score_time, scorer) VALUES
                (1, 101, 95, '宿舍非常干净，物品摆放整齐', '2026-03-26 10:30:00', '张宿管'),
                (1, 101, 88, '卫生良好，继续保持', '2026-03-25 10:35:00', '李宿管'),
                (1, 102, 92, '整洁有序，值得表扬', '2026-03-26 11:00:00', '张宿管'),
                (1, 102, 85, '整体不错，继续加油', '2026-03-25 11:05:00', '李宿管'),
                (2, 201, 90, '干净卫生，表现优秀', '2026-03-26 14:00:00', '王宿管'),
                (2, 202, 82, '还可以，需要改进', '2026-03-26 14:30:00', '王宿管')
            `;
            await connection.execute(insertSQL);
            console.log('测试数据添加成功！');
        }

        // 查看最终数据
        const [finalData] = await connection.execute('SELECT * FROM dorm_score ORDER BY id');
        console.log('最终评分数据:', finalData);
        console.log('\n✅ 卫生评分表初始化完成！');

    } catch (error) {
        console.error('执行出错:', error);
    } finally {
        await connection.end();
    }
}

initDormScore();
