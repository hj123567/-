const mysql = require('mysql2');

console.log('Checking student table structure...');

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
    
    connection.query('DESCRIBE student', (err, results) => {
        if (err) {
            console.error('Error describing table:', err);
        } else {
            console.log('Current student table columns:');
            results.forEach((row, index) => {
                console.log(`${index + 1}. ${row.Field} - ${row.Type}`);
            });
            
            const columns = results.map(row => row.Field);
            const missingColumns = [];
            if (!columns.includes('college')) missingColumns.push('college');
            if (!columns.includes('class_name')) missingColumns.push('class_name');
            if (!columns.includes('dorm_build_id')) missingColumns.push('dorm_build_id');
            if (!columns.includes('dorm_room_id')) missingColumns.push('dorm_room_id');
            
            if (missingColumns.length > 0) {
                console.log(`\nMissing columns: ${missingColumns.join(', ')}`);
                console.log('\nAdding missing columns...');
                
                let added = 0;
                const total = missingColumns.length;
                
                const addColumn = (colName, colType) => {
                    const sql = `ALTER TABLE student ADD COLUMN ${colName} ${colType}`;
                    connection.query(sql, (err, result) => {
                        added++;
                        if (err) {
                            console.error(`Error adding ${colName}:`, err);
                        } else {
                            console.log(`Added ${colName} successfully!`);
                        }
                        
                        if (added === total) {
                            console.log('\nAll columns added!');
                            connection.end();
                        }
                    });
                };
                
                missingColumns.forEach(col => {
                    if (col === 'college' || col === 'class_name') {
                        addColumn(col, 'varchar(255) NULL');
                    } else {
                        addColumn(col, 'int(11) NULL');
                    }
                });
            } else {
                console.log('\nAll columns already exist!');
                connection.end();
            }
        }
    });
});
