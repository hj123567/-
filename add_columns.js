const mysql = require('mysql2');

console.log('Connecting to database to add columns...');

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
    
    const sqls = [
        'ALTER TABLE student ADD COLUMN IF NOT EXISTS college varchar(255) NULL',
        'ALTER TABLE student ADD COLUMN IF NOT EXISTS class_name varchar(255) NULL',
        'ALTER TABLE student ADD COLUMN IF NOT EXISTS dorm_build_id int(11) NULL',
        'ALTER TABLE student ADD COLUMN IF NOT EXISTS dorm_room_id int(11) NULL'
    ];
    
    let completed = 0;
    
    sqls.forEach((sql, index) => {
        connection.query(sql, (err, result) => {
            completed++;
            if (err) {
                console.error(`Error executing sql ${index + 1}:`, err);
            } else {
                console.log(`Executed sql ${index + 1} successfully!`);
            }
            
            if (completed === sqls.length) {
                console.log('\nAll columns added (or already exist)!');
                console.log('Now you can update the student data.');
                connection.end();
            }
        });
    });
});
